package com.league.service.player;

import com.league.model.Player;
import com.league.model.User;
import com.league.repository.PlayerRepository;
import com.league.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
