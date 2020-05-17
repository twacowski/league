package com.league.service.league;

import com.league.model.*;
import com.league.model.enums.Status;
import com.league.repository.LeagueRepository;
import com.league.repository.ParticipationRepository;
import com.league.repository.TeamRepository;
import com.league.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class LeagueServiceImpl implements LeagueService {

    @Autowired
    LeagueRepository leagueRepository;

    @Autowired
    UserService userService;

    @Autowired
    ParticipationRepository participationRepository;

    @Autowired
    TeamRepository teamRepository;

    @Override
    public void saveLeague(League league) {
        if (league.getUser() == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findByUsername(auth.getName());
            league.setUser(user);
        }
        leagueRepository.save(league);
    }

    @Override
    public void deleteLeague(int leagueId) {
        leagueRepository.deleteById(leagueId);
    }

    @Override
    public League findById(int id) {
        return leagueRepository.findById(id).get();
    }

    @Override
    public List<League> getUserLeagues() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<League> leagues = leagueRepository.getLeaguesByUserNameOrderByIdDesc(auth.getName());
        return leagues;
    }

    @Override
    public boolean isAbleToStart(int leagueId) {
        League league = leagueRepository.findById(leagueId).get();
        if(league.getStatus() == Status.ACTIVE || league.getStatus() == Status.ARCHIVED ||
                league.getParticipationList().size() < 2 || !isOwner(leagueId)) {
            return false;
        }
        return true;
    }

    @Override
    public void startLeague(League league) {
        league.setStatus(Status.ACTIVE);
        createGameweeks(league);
        populateGameweeks(league);
        leagueRepository.save(league);
    }

    @Override
    public void createGameweeks(League league) {
        int numberOfGameweeks = league.getParticipationList().size();
        if (numberOfGameweeks % 2 == 0) {
            numberOfGameweeks--;
        }
        if (league.isRematch()) {
            numberOfGameweeks *= 2;
        }
        int numberOfMatches = league.getParticipationList().size() / 2;

        List<Gameweek> gameweeks = new ArrayList<>();

        for (int i = 0; i < numberOfGameweeks; i++) {
            Gameweek gameweek = new Gameweek(i + 1, league.getUser(), league);
            List<Match> matches = new ArrayList<>();
            for (int j = 0; j < numberOfMatches; j++) {
                matches.add(new Match(league.getUser(), league, gameweek, false));
            }
            gameweek.setMatches(matches);
            gameweeks.add(gameweek);
        }
        league.setGameweeks(gameweeks);
    }

    @Override
    public void populateGameweeks(League league) {
        List<Participation> participations = league.getParticipationList();
        int numberOfTeams = participations.size();
        Collections.shuffle(participations);
        List<Gameweek> gameweeks = league.getGameweeks();
        List<Match> matches = gameweeks.get(0).getMatches();
        List<Match> previousMatches;

        if(participations.size() == 2) {
            matches.get(0).setHost(participations.get(0));
            matches.get(0).setGuest(participations.get(1));
            if(league.isRematch()) {
                matches = gameweeks.get(1).getMatches();
                matches.get(0).switchHost();
            }
            leagueRepository.save(league);
            return;
        }

        int numberOfMatches = matches.size();

        for (int i = 0; i < numberOfMatches; i++) {
            matches.get(i).setHost(participations.get(i));
            matches.get(i).setGuest(participations.get(numberOfTeams - 1 - i));
        }

        int numberOfGameweeks = gameweeks.size();

        if (league.isRematch()) {
            numberOfGameweeks /= 2;
        }

        if(numberOfTeams % 2 == 0) {
            for (int i = 1; i < numberOfGameweeks; i++) {
                previousMatches = gameweeks.get(i - 1).getMatches();
                matches = gameweeks.get(i).getMatches();
                matches.get(0).setHost(participations.get(0));
                matches.get(1).setHost(previousMatches.get(0).getGuest());
                matches.get(0).setGuest(previousMatches.get(1).getGuest());
                matches.get(numberOfMatches - 1).setGuest(previousMatches.get(numberOfMatches - 1).getHost());
                for (int j = 2; j < numberOfMatches; j++) {
                    matches.get(j).setHost(previousMatches.get(j - 1).getHost());
                    matches.get(j - 1).setGuest(previousMatches.get(j).getGuest());
                }
            }
        } else {
            Participation pauseTeam = participations.get(numberOfTeams/2);
            for (int i = 1; i < numberOfGameweeks; i++) {
                previousMatches = gameweeks.get(i - 1).getMatches();
                matches = gameweeks.get(i).getMatches();
                matches.get(0).setHost(pauseTeam);
                pauseTeam = previousMatches.get(0).getGuest();
                matches.get(numberOfMatches - 1).setGuest(previousMatches.get(numberOfMatches - 1).getHost());
                for (int j = 1; j < numberOfMatches; j++) {
                    matches.get(j).setHost(previousMatches.get(j - 1).getHost());
                    matches.get(j - 1).setGuest(previousMatches.get(j).getGuest());
                }
            }
        }

        for (int i = 1; i < numberOfGameweeks; i += 2) {
            matches = gameweeks.get(i).getMatches();
            for (Match match : matches) {
                match.switchHost();
            }
        }

        if (league.isRematch()) {
            for (int i = numberOfGameweeks; i < 2 * numberOfGameweeks; i++) {
                matches = gameweeks.get(i).getMatches();
                previousMatches = gameweeks.get(i - numberOfGameweeks).getMatches();
                for (int j = 0; j < numberOfMatches; j++) {
                    matches.get(j).setHost(previousMatches.get(j).getGuest());
                    matches.get(j).setGuest(previousMatches.get(j).getHost());
                }
            }
        }

        leagueRepository.save(league);
    }

    @Override
    public List<Participation> getStandings(League league) {
        List<Participation> standings = league.getParticipationList();
        int leagueSize = league.getParticipationList().size();
        int max = 0;
        for (int i = 0; i < leagueSize; i++) {
            max = i;
            for (int j = i + 1; j < leagueSize; j++) {
                if (standings.get(max).getPoints() < standings.get(j).getPoints() ||
                        (standings.get(max).getPoints() == standings.get(j).getPoints() &&
                                standings.get(max).getBalance() < standings.get(j).getBalance()) ||
                        (standings.get(max).getPoints() == standings.get(j).getPoints() &&
                                standings.get(max).getBalance() == standings.get(j).getBalance() &&
                                standings.get(max).getScoredGoals() == standings.get(j).getScoredGoals())) {
                    max = j;
                }
            }
            Collections.swap(standings, max, i);
        }
        return standings;
    }

    @Override
    public List<League> searchLeaguesByPhrase(String phrase) {
        return leagueRepository.findLeaguesByPhrase(phrase);
    }

    @Override
    public List<League> getPopularLeagues() {
        return leagueRepository.findTop5ByOrderByNumberDesc();
    }

    @Override
    public List<League> getLatestLeagues() {
        return leagueRepository.findTop5ByOrderByIdDesc();
    }

    @Override
    public List<League> getAllLeaguesToView() {
        return leagueRepository.getAllLeaguesToView();
    }

    @Override
    public List<League> getLeaguesByLocation(Voivodeship voivodeship, County county) {
        if(county != null) {
            voivodeship = county.getVoivodeship();
            return leagueRepository.findLeaguesByLocation(voivodeship.getId(), county.getId());
        } else if (voivodeship != null) {
            return leagueRepository.findLeaguesByVoivodeship(voivodeship.getId());
        }
        return leagueRepository.findAll();
    }

    @Override
    public void saveParticipation(Participation participation) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        if(user.getId() == participation.getLeague().getUser().getId()) {
            participation.setAccepted(true);
        }
        participationRepository.save(participation);
    }

    @Override
    public List<Participation> getAllUserParticipations() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Team> teams = teamRepository.getTeamsByUserName(auth.getName());
        List<Participation> participations = new ArrayList<>();
        for(Team team : teams) {
            for(Participation participation : team.getParticipationList()) {
                if(!participation.getLeague().getUser().getUserName().equals(auth.getName())) {
                    participations.add(participation);
                }
            }
        }
        Collections.sort(participations, Comparator.comparingInt(Participation::getId));
        return participations;
    }

    @Override
    public Team participatingTeam(List<Participation> participations) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getName().equals("anonymousUser")) {
            return null;
        }

        User user = userService.findByUsername(auth.getName());

        for(Participation participation : participations) {
            if(user.getId() == participation.getTeam().getUser().getId()) {
                return participation.getTeam();
            }
        }

        return null;
    }

    @Override
    public void cancelParticipation(League league) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());

        for(Participation participation : league.getParticipationList()) {
            if(user.getId() == participation.getTeam().getUser().getId() && league.getStatus() == Status.TOREGISTER) {
                int id = participation.getId();
                league.cancelParticipation(participation);
                participation.getTeam().cancelParticipation(participation);
                participationRepository.deleteById(id);
                return;
            }
        }
    }

    @Override
    public League acceptTeam(int participationId) {
        Participation participation = participationRepository.findById(participationId).get();
        participation.setAccepted(true);
        participationRepository.save(participation);
        return participation.getLeague();
    }

    @Override
    public League rejectTeam(int participationId) {
        League league = participationRepository.findById(participationId).get().getLeague();
        participationRepository.deleteById(participationId);
        return league;
    }

    @Override
    public void openRegistration(int leagueId) {
        League league = leagueRepository.findById(leagueId).get();
        league.setStatus(Status.TOREGISTER);
        leagueRepository.save(league);
    }

    @Override
    public void toArchieve(int leagueId) {
        League league = leagueRepository.findById(leagueId).get();
        league.setStatus(Status.ARCHIVED);
        leagueRepository.save(league);

    }

    @Override
    public Participation getParticipation(int leagueId, int teamId) {
        return participationRepository.getParticipation(leagueId, teamId);
    }

    @Override
    public boolean isOwner(int leagueId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        League league = leagueRepository.findById(leagueId).get();
        if(user.getId() == league.getUser().getId()) {
            return true;
        }
        return false;
    }
}
