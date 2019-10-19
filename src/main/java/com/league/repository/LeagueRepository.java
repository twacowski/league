package com.league.repository;

import com.league.model.League;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeagueRepository extends JpaRepository<League, Integer> {
    List<League> getLeaguesByUserName(String username);
}
