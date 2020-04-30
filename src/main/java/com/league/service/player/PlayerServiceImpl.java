package com.league.service.player;

import com.league.model.League;
import com.league.model.Player;
import com.league.model.User;
import com.league.repository.PlayerRepository;
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
    public List<Player> getUserPlayers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Player> list = playerRepository.getPlayersByUserName(auth.getName());
        return list;
    }

    @Override
    public List<Player> getTeamPlayers(int id) {
        return playerRepository.getPlayersByTeamId(id);
    }

    @Override
    public List<Player> getTopScorers(League league) {
        return playerRepository.getTopScorers(league.getId());
    }

    @Override
    public List<Player> getMostOwnGoals(League league) {
        return playerRepository.getMostOwnGoals(league.getId());
    }

    @Override
    public List<Player> getMostYellowCards(League league) {
        return playerRepository.getMostYellowCards(league.getId());
    }

    @Override
    public List<Player> getMostRedCards(League league) {
        return playerRepository.getMostRedCards(league.getId());
    }

    @Override
    public List<Player> searchPlayersByPhrase(String phrase) {
        return playerRepository.findPlayersByPhrase(phrase);
    }
}
