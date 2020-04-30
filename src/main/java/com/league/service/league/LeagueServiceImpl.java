package com.league.service.league;

import com.league.model.*;
import com.league.repository.LeagueRepository;
import com.league.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class LeagueServiceImpl implements LeagueService {

    @Autowired
    LeagueRepository leagueRepository;

    @Autowired
    UserService userService;

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
        List<League> leagues = leagueRepository.getLeaguesByUserName(auth.getName());
        return leagues;
    }

    @Override
    public void startLeague(League league) {
        league.setStarted(true);
        createGameweeks(league);
        populateGameweeks(league);
        leagueRepository.save(league);
        //TODO save league!


    }

    @Override
    public void populateGameweeks(League league) {
        List<Team> teams = league.getTeams();
        int numberOfTeams = teams.size();
        Collections.shuffle(teams);
        List<Gameweek> gameweeks = league.getGameweeks();
        List<Match> matches = gameweeks.get(0).getMatches();
        List<Match> previousMatches;

        if(teams.size() == 2) {
            matches.get(0).setHost(teams.get(0));
            matches.get(0).setGuest(teams.get(1));
            if(league.isRematch()) {
                matches = gameweeks.get(1).getMatches();
                matches.get(0).switchHost();
            }
            leagueRepository.save(league);
            return;
        }

        int numberOfMatches = matches.size();

        for (int i = 0; i < numberOfMatches; i++) {
            matches.get(i).setHost(teams.get(i));
            matches.get(i).setGuest(teams.get(numberOfTeams - 1 - i));
        }

        int numberOfGameweeks = gameweeks.size();

        if (league.isRematch()) {
            numberOfGameweeks /= 2;
        }

        if(numberOfTeams % 2 == 0) {
            for (int i = 1; i < numberOfGameweeks; i++) {
                previousMatches = gameweeks.get(i - 1).getMatches();
                matches = gameweeks.get(i).getMatches();
                matches.get(0).setHost(teams.get(0));
                matches.get(1).setHost(previousMatches.get(0).getGuest());
                matches.get(0).setGuest(previousMatches.get(1).getGuest());
                matches.get(numberOfMatches - 1).setGuest(previousMatches.get(numberOfMatches - 1).getHost());
                for (int j = 2; j < numberOfMatches; j++) {
                    matches.get(j).setHost(previousMatches.get(j - 1).getHost());
                    matches.get(j - 1).setGuest(previousMatches.get(j).getGuest());
                }
            }
        } else {
            Team pauseTeam = teams.get(numberOfTeams/2);
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
    public void createGameweeks(League league) {
        int numberOfGameweeks = league.numberOfTeams();
        if (numberOfGameweeks % 2 == 0) {
            numberOfGameweeks--;
        }
        if (league.isRematch()) {
            numberOfGameweeks *= 2;
        }
        int numberOfMatches = league.numberOfTeams() / 2;

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
    public List<Team> getStandings(League league) {
        List<Team> standings = league.getTeams();
        int max = 0;
        for (int i = 0; i < league.numberOfTeams(); i++) {
            max = i;
            for (int j = i + 1; j < league.numberOfTeams(); j++) {
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
        return leagueRepository.findTop10ByOrderByNumberDesc();
    }

    @Override
    public List<League> getLatestLeagues() {
        return leagueRepository.findTop10ByOrderByIdDesc();
    }
}
