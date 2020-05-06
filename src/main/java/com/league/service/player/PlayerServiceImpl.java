package com.league.service.player;

import com.league.model.League;
import com.league.model.Player;
import com.league.model.Stat;
import com.league.model.User;
import com.league.repository.PlayerRepository;
import com.league.repository.StatRepository;
import com.league.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    StatRepository statRepository;

    @Autowired
    UserService userService;

    @Override
    public Page<Player> findAll(Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return playerRepository.findAllByUserName(pageable, auth.getName());
    }

    @Override
    public void savePlayer(Player player) {
        if (player.getUser() == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findByUsername(auth.getName());
            player.setUser(user);
        }
        playerRepository.save(player);
    }

    @Override
    public void deletePlayer(int playerId) {
        playerRepository.deleteById(playerId);
    }

    @Override
    public Player findById(int id) {
        return playerRepository.findById(id).get();
    }

    @Override
    public List<Player> getTeamPlayers(int id) {
        return playerRepository.getPlayersByTeamId(id);
    }

    @Override
    public List<Stat> getTopScorers(League league) {
        return statRepository.getTopScorers(league.getId());
    }

    @Override
    public List<Stat> getMostOwnGoals(League league) {
        return statRepository.getMostOwnGoals(league.getId());
    }

    @Override
    public List<Stat> getMostYellowCards(League league) {
        return statRepository.getMostYellowCards(league.getId());
    }

    @Override
    public List<Stat> getMostRedCards(League league) {
        return statRepository.getMostRedCards(league.getId());
    }

    @Override
    public List<Player> searchPlayersByPhrase(String phrase) {
        return playerRepository.findPlayersByPhrase(phrase);
    }
}
