package com.league.controller;


import com.league.model.User;
import com.league.service.league.LeagueService;
import com.league.service.player.PlayerService;
import com.league.service.team.TeamService;
import com.league.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    UserService userService;

    @Autowired
    PlayerService playerService;

    @Autowired
    LeagueService leagueService;

    @Autowired
    TeamService teamService;

    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("popularLeagues", leagueService.getPopularLeagues());
        model.addAttribute("latestLeagues", leagueService.getLatestLeagues());
        return "index";
    }

    @GetMapping("/registration")
    public String registration(Model model) {

        User user = new User();
        model.addAttribute("user", user);

        return "registration";
    }

    @PostMapping("/registrationProceed")
    public String registrationProceed(@ModelAttribute("user") User user) {

        userService.addUser(user);

        return "redirect:/index";
    }

    @GetMapping("/search")
    public String search(@RequestParam String phrase, Model model) {

        model.addAttribute("users", userService.searchUsersByPhrase(phrase));
        model.addAttribute("players", playerService.searchPlayersByPhrase(phrase));
        model.addAttribute("teams", teamService.searchTeamsByPhrase(phrase));
        model.addAttribute("leagues", leagueService.searchLeaguesByPhrase(phrase));

        return "searchingList";
    }
}
