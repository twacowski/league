package com.league.controller;

import com.league.model.Player;
import com.league.service.player.PlayerService;
import com.league.service.team.TeamService;
import com.league.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("user/players")
public class PlayerController {

    @Autowired
    TeamService teamService;

    @Autowired
    PlayerService playerService;

    @Autowired
    UserService userService;

    @GetMapping("myPlayers")
    public String index(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("page", 0);
        redirectAttributes.addAttribute("size", 10);
        return "redirect:playersList";
    }

    @GetMapping("playersList")
    public String playersList(Pageable pageable, Model model) {
        Page<Player> players = playerService.findAll(pageable);
        model.addAttribute("players", players);
        return "user/players/myPlayers";
    }

    @GetMapping("addPlayer")
    public String addPlayer(Model model) {
        model.addAttribute("player", new Player(userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())));
        return "user/players/addPlayer";
    }

    @PostMapping("addPlayerProceed")
    public String addPlayerProceed(@Valid @ModelAttribute("player") Player player, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "user/players/addPlayer";
        }

        playerService.savePlayer(player);
        return "redirect:/user/players/myPlayers";
    }

    @GetMapping("editPlayer")
    public String editPlayer(@RequestParam("playerId") int playerId, Model model) {
        Player player = playerService.findById(playerId);
        model.addAttribute("player", player);
        return "user/players/editPlayer";
    }

    @PostMapping("editPlayerProceed")
    public String editPlayerProceed(@Valid @ModelAttribute("player") Player player, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "user/players/editPlayer";
        }

        playerService.savePlayer(player);
        return "redirect:/user/players/myPlayers";
    }

    @GetMapping("deletePlayer")
    public String deletePlayerProceed(@RequestParam("playerId") int playerId) {
        playerService.deletePlayer(playerId);
        return "redirect:/user/players/myPlayers";
    }
}
