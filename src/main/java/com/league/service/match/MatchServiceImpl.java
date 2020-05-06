package com.league.service.match;

import com.league.model.*;
import com.league.repository.MatchRepository;
import com.league.repository.StatRepository;
import com.league.service.league.LeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    MatchRepository matchRepository;

    @Autowired
    StatRepository statRepository;

    @Autowired
    LeagueService leagueService;

    @Override
    public void saveMatch(Match match) {
        Match theMatch = matchRepository.findById(match.getId()).get();
        theMatch.setHostScore(match.getHostScore());
        theMatch.setGuestScore(match.getGuestScore());
        theMatch.setScoreSheets(match.getScoreSheets());
        if(theMatch.getHostScore() != null && theMatch.getGuestScore() != null) {
            theMatch.setPlayed(true);
        } else {
            theMatch.setPlayed(false);
        }
        updateMatchStats(theMatch);
        matchRepository.save(theMatch);
        updatePlayers(theMatch);
        matchRepository.save(theMatch);
    }

    @Override
    public Match findById(int matchId) {
        return matchRepository.findById(matchId).get();
    }

    @Override
    public void createSheets(Match match) {
        List<ScoreSheet> hostScoreSheets;
        List<ScoreSheet> guestScoreSheets;

        if(match.getHostScoreSheets().isEmpty()) {
            hostScoreSheets = new ArrayList<>();
            for(int i = 0; i < match.getHost().getTeam().numberOfPlayers(); i++) {
                Player player = match.getHost().getTeam().getPlayers().get(i);
                Stat stats = statRepository.getPlayerStat(match.getLeague().getId(), player.getId());
                if(stats == null) {
                    Stat newStat = new Stat(match.getLeague(), player);
                    statRepository.save(newStat);
                }
                hostScoreSheets.add(new ScoreSheet(match.getUser(), match, player, true));
            }
        } else {
            hostScoreSheets = match.getHostScoreSheets();
            for(int i = hostScoreSheets.size(); i < match.getHost().getTeam().numberOfPlayers(); i++) {
                hostScoreSheets.add(new ScoreSheet(match.getUser(), match, match.getHost().getTeam().getPlayers().get(i), true));
            }
        }

        if(match.getGuestScoreSheets().isEmpty()) {
            guestScoreSheets = new ArrayList<>();
            for(int i = 0; i < match.getGuest().getTeam().numberOfPlayers(); i++) {
                Player player = match.getGuest().getTeam().getPlayers().get(i);
                Stat stats = statRepository.getPlayerStat(match.getLeague().getId(), player.getId());
                if(stats == null) {
                    Stat newStat = new Stat(match.getLeague(), player);
                    statRepository.save(newStat);
                }
                guestScoreSheets.add(new ScoreSheet(match.getUser(), match, player, false));
            }
        } else {
            guestScoreSheets = match.getGuestScoreSheets();
            for(int i = guestScoreSheets.size(); i < match.getGuest().getTeam().numberOfPlayers(); i++) {
                guestScoreSheets.add(new ScoreSheet(match.getUser(), match, match.getGuest().getTeam().getPlayers().get(i), false));
            }
        }
        match.setHostScoreSheets(hostScoreSheets);
        match.setGuestScoreSheets(guestScoreSheets);
        matchRepository.save(match);
    }

    @Override
    public void updateMatchStats(Match match) {
        match.setHost(updateTeamStats(match.getHost()));
        match.setGuest(updateTeamStats(match.getGuest()));
    }

    @Override
    public Participation updateTeamStats(Participation participation) {
        int wins = 0, draws = 0, loses = 0, scoredGoals = 0, concededGoals = 0, points = 0, balance = 0;
        List<Match> matches = participation.getHostMatches();
        for(Match match : matches) {
            if(match.isPlayed()) {
                scoredGoals += match.getHostScore();
                concededGoals += match.getGuestScore();
                if(match.getHostScore() > match.getGuestScore()) {
                    wins++;
                    points += 3;
                } else if(match.getHostScore() == match.getGuestScore()) {
                    draws++;
                    points++;
                } else {
                    loses++;
                }
            }
        }

        matches = participation.getGuestMatches();
        for(Match match : matches) {
            if(match.isPlayed()) {
                scoredGoals += match.getGuestScore();
                concededGoals += match.getHostScore();
                if(match.getGuestScore() > match.getHostScore()) {
                    wins++;
                    points += 3;
                } else if(match.getHostScore() == match.getGuestScore()) {
                    draws++;
                    points++;
                } else {
                    loses++;
                }
            }
            balance = scoredGoals - concededGoals;
        }

        participation.setWins(wins);
        participation.setDraws(draws);
        participation.setLoses(loses);
        participation.setScoredGoals(scoredGoals);
        participation.setConcededGoals(concededGoals);
        participation.setBalance(balance);
        participation.setPoints(points);

        return participation;
    }

    @Override
    public void updatePlayers(Match match) {
        for (Player player : match.getHost().getTeam().getPlayers()) {
            updatePlayerStats(player, match);
        }

        for (Player player : match.getGuest().getTeam().getPlayers()) {
            updatePlayerStats(player, match);
        }
    }

    @Override
    public void updatePlayerStats(Player player, Match match) {
        int goals = 0, ownGoals = 0, yellowCards = 0, redCards = 0;
        for (ScoreSheet sheet: player.getScoreSheets()) {
            if(sheet.getMatch().isPlayed() && sheet.getMatch().getLeague().getId() == match.getLeague().getId()) {
                goals += sheet.getGoals();
                ownGoals += sheet.getOwnGoals();
                yellowCards += sheet.getYellowCards();
                redCards += sheet.getRedCards();
            }
        }
        List<Stat> statsList = player.getStats();
        Stat tempStat = null;
        for(Stat stat : statsList) {
            if(stat.getLeague().getId() == match.getLeague().getId()){
                tempStat = stat;
            }
        }
        statsList.remove(tempStat);
        Stat stats = statRepository.getPlayerStat(match.getLeague().getId(), player.getId());
        stats.setGoals(goals);
        stats.setOwnGoals(ownGoals);
        stats.setYellowCards(yellowCards);
        stats.setRedCards(redCards);
        statsList.add(stats);
        player.setStats(statsList);
    }
}
