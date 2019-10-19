package com.league.service.player;

import com.league.model.Player;

import java.util.List;

public interface PlayerService {
    void savePlayer(Player player);
    void deletePlayer(int playerId);
    Player findById(int id);
    List<Player> getUserPlayers();
    List<Player> getTeamPlayers(int id);
}
