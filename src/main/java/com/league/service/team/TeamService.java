package com.league.service.team;

import com.league.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TeamService {
    Page<Team> findAll(Pageable pageable);
    void saveTeam(Team team);
    void deleteTeam(int teamId);
    Team findById(int id);
    List<Team> getUserTeams();
    List<Team> getLeagueTeams(int leagueId);
    List<Team> searchTeamsByPhrase(String phrase);

}
