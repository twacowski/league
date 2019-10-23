package com.league.controller;

import com.league.model.Match;
import com.league.service.match.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("user/matches")
public class MatchController {

    @Autowired
    MatchService matchService;

    @GetMapping("matchDetails")
    public String matchDetails(@RequestParam("matchId") int matchId, Model model) {
        Match match = matchService.findById(matchId);
        matchService.createSheets(match);
        model.addAttribute("match", match);
        return "/user/matches/matchDetails";
    }

    @PostMapping("saveMatch")
    public String saveMatch(@ModelAttribute("match") Match match) {
        matchService.saveMatch(match);

        return "redirect:/user/leagues/manageActiveLeague?leagueId="+match.getLeague().getId();
    }
}
