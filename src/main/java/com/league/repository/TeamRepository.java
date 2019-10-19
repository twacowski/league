package com.league.repository;

import com.league.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Integer> {
    List<Team> getTeamsByUserName(String username);
    List<Team> getTeamsByLeagueId(int id);
}
