package com.league.service.match;

import com.league.model.Match;
import com.league.model.Player;
import com.league.model.Team;

public interface MatchService {
    void saveMatch(Match match);
    Match findById(int matchId);
    void createSheets(Match match);
    void updateMatchStats(Match match);
    Team updateTeamStats(Team team);
    void updatePlayers(Match match);
    void updatePlayerStats(Player player);

}
