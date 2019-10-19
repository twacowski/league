package com.league.service.league;

import com.league.model.League;

import java.util.List;

public interface LeagueService {
    void saveLeague(League league);
    void deleteLeague(int leagueId);
    League findById(int id);
    List<League> getUserLeagues();
}
