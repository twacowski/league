package com.league.service.league;

import com.league.model.County;
import com.league.model.League;
import com.league.model.Team;
import com.league.model.Voivodeship;

import java.util.List;

public interface LeagueService {
    void saveLeague(League league);
    void deleteLeague(int leagueId);
    League findById(int id);
    List<League> getUserLeagues();
    void startLeague(League league);
    List<Team> getStandings(League league);
    void populateGameweeks(League league);
    void createGameweeks(League league);
    List<League> searchLeaguesByPhrase(String phrase);
    List<League> getPopularLeagues();
    List<League> getLatestLeagues();
    List<League> getAllLeagues();
    List<League> getLeaguesByLocation(Voivodeship voivodeship, County county);

}
