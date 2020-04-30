package com.league.service.player;

import com.league.model.League;
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
    List<Player> getTopScorers(League league);
    List<Player> getMostOwnGoals(League league);
    List<Player> getMostYellowCards(League league);
    List<Player> getMostRedCards(League league);
    List<Player> searchPlayersByPhrase(String phrase);
}
