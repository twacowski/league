package com.league.service.match;

import com.league.model.Match;
import com.league.model.ScoreSheet;
import com.league.model.Team;
import com.league.repository.MatchRepository;
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
    LeagueService leagueService;

    @Override
    public void saveMatch(Match match) {
        Match theMatch = matchRepository.findById(match.getId()).get();
        theMatch.setHostScoreSheets(match.getHostScoreSheets());
        theMatch.setGuestScoreSheets(match.getGuestScoreSheets());
        theMatch.setHostScore(match.getHostScore());
        theMatch.setGuestScore(match.getGuestScore());
        if(theMatch.getHostScore() != null && theMatch.getGuestScore() != null) {
            theMatch.setPlayed(true);
        }
        actualizeMatchStats(theMatch);
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

        if(match.getHostScoreSheets() == null) {
            hostScoreSheets = new ArrayList<>();
            for(int i = 0; i < match.getHost().numberOfPlayers(); i++) {
                hostScoreSheets.add(new ScoreSheet(match.getUser(), match, match.getHost().getPlayers().get(i)));
            }
        } else {
            hostScoreSheets = match.getHostScoreSheets();
            for(int i = hostScoreSheets.size(); i < match.getHost().numberOfPlayers(); i++) {
                hostScoreSheets.add(new ScoreSheet(match.getUser(), match, match.getHost().getPlayers().get(i)));
            }
        }

        if(match.getGuestScoreSheets() == null) {
            guestScoreSheets = new ArrayList<>();
            for(int i = 0; i < match.getGuest().numberOfPlayers(); i++) {
                guestScoreSheets.add(new ScoreSheet(match.getUser(), match, match.getGuest().getPlayers().get(i)));
            }
        } else {
            guestScoreSheets = match.getGuestScoreSheets();
            for(int i = guestScoreSheets.size(); i < match.getGuest().numberOfPlayers(); i++) {
                guestScoreSheets.add(new ScoreSheet(match.getUser(), match, match.getGuest().getPlayers().get(i)));
            }
        }
        match.setHostScoreSheets(hostScoreSheets);
        match.setGuestScoreSheets(guestScoreSheets);
        matchRepository.save(match);
    }

    public void actualizeMatchStats(Match match) {
        match.setHost(actualizeTeamStats(match.getHost()));
        match.setGuest(actualizeTeamStats(match.getGuest()));
    }

    public Team actualizeTeamStats(Team team) {
        int wins = 0, draws = 0, loses = 0, scoredGoals = 0, concededGoals = 0, points = 0, balance = 0;
        List<Match> matches = team.getHostMatches();
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

        matches = team.getGuestMatches();
        for(Match match : matches) {
            if(match.isPlayed()) {
                scoredGoals += match.getGuestScore();
                concededGoals += match.getHostScore();
                if(match.getGuestScore() > concededGoals) {
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

        team.setWins(wins);
        team.setDraws(draws);
        team.setLoses(loses);
        team.setScoredGoals(scoredGoals);
        team.setConcededGoals(concededGoals);
        team.setBalance(balance);
        team.setPoints(points);

        return team;
    }
}
