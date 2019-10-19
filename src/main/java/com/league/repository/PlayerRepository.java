package com.league.repository;

import com.league.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    List<Player> getPlayersByUserName(String username);
    List<Player> getPlayersByTeamId(int id);
}
