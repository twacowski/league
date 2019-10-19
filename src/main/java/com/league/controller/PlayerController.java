package com.league.controller;

import com.league.model.Player;
import com.league.service.player.PlayerService;
import com.league.service.team.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("user/players")
public class PlayerController {

    @Autowired
    TeamService teamService;

    @Autowired
    PlayerService playerService;

    @GetMapping("myPlayers")
    public String myPlayers(Model model) {
        model.addAttribute("players", playerService.getUserPlayers());
        return "user/players/myPlayers";
    }

    @GetMapping("addPlayer")
    public String addPlayer(Model model) {
        model.addAttribute("player", new Player());
        model.addAttribute("teams", teamService.getUserTeams());
        return "user/players/addPlayer";
    }

    @PostMapping("addPlayerProceed")
    public String addPlayerProceed(@ModelAttribute("player") Player player) {
        playerService.savePlayer(player);
        return "redirect:/user/players/myPlayers";
    }

    @GetMapping("editPlayer")
    public String editPlayer(@RequestParam("playerId") int playerId, Model model) {
        Player player = playerService.findById(playerId);
        model.addAttribute("player", player);
        model.addAttribute("teams", teamService.getUserTeams());
        return "user/players/editPlayer";
    }

    @PostMapping("editPlayerProceed")
    public String editPlayerProceed(@ModelAttribute("player") Player player) {
        playerService.savePlayer(player);
        return "redirect:/user/players/myPlayers";
    }

    @GetMapping("deletePlayer")
    public String deletePlayerProceed(@RequestParam("playerId") int playerId) {
        playerService.deletePlayer(playerId);
        return "redirect:/user/players/myPlayers";
    }
}
