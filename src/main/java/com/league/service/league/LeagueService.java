package com.league.service.league;

import com.league.model.*;
import java.util.List;

public interface LeagueService {
    void saveLeague(League league);
    void deleteLeague(int leagueId);
    League findById(int id);
    List<League> getUserLeagues();
    boolean isAbleToStart(int leagueId);
    void startLeague(League league);
    List<Participation> getStandings(League league);
    void populateGameweeks(League league);
    void createGameweeks(League league);
    List<League> searchLeaguesByPhrase(String phrase);
    List<League> getPopularLeagues();
    List<League> getLatestLeagues();
    List<League> getAllLeaguesToView();
    List<League> getLeaguesByLocation(Voivodeship voivodeship, County county);
    void saveParticipation(Participation participation);
    List<Participation> getAllUserParticipations();
    Team participatingTeam(List<Participation> participations);
    void cancelParticipation(League league);
    League acceptTeam(int participationId);
    League rejectTeam(int participationId);
    void openRegistration(int leagueId);
    void toArchieve(int leagueId);
    Participation getParticipation(int leagueId, int teamId);
    boolean isOwner(int leagueId);

}
