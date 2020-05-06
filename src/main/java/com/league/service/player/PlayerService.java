package com.league.service.player;

import com.league.model.League;
import com.league.model.Player;
import com.league.model.Stat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlayerService {
    Page<Player> findAll(Pageable pageable);
    void savePlayer(Player player);
    void deletePlayer(int playerId);
    Player findById(int id);
    List<Player> getTeamPlayers(int id);
    List<Stat> getTopScorers(League league);
    List<Stat> getMostOwnGoals(League league);
    List<Stat> getMostYellowCards(League league);
    List<Stat> getMostRedCards(League league);
    List<Player> searchPlayersByPhrase(String phrase);
}
