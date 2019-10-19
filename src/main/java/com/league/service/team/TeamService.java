package com.league.service.team;

import com.league.model.Team;

import java.util.List;

public interface TeamService {
    void saveTeam(Team team);
    void deleteTeam(int teamId);
    Team findById(int id);
    List<Team> getUserTeams();
    List<Team> getLeagueTeams(int leagueId);

}
