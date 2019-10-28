package com.league.service.player;

import com.league.model.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlayerService {
    Page<Player> findAll(Pageable pageable);
    void savePlayer(Player player);
    void deletePlayer(int playerId);
    Player findById(int id);
    List<Player> getUserPlayers();
    List<Player> getTeamPlayers(int id);
}
