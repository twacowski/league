package com.league.controller;

import com.league.service.league.LeagueService;
import com.league.service.player.PlayerService;
import com.league.service.team.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    LeagueService leagueService;

    @Autowired
    TeamService teamService;

    @Autowired
    PlayerService playerService;

    @GetMapping("index")
    public String index() {
        return "user/index";
    }

}
