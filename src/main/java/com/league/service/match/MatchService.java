package com.league.service.match;

import com.league.model.Match;

public interface MatchService {
    void saveMatch(Match match);
    Match findById(int matchId);
    void createSheets(Match match);
}
