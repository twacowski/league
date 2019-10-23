package com.league.controller;

import com.league.model.League;
import com.league.model.Team;
import com.league.service.league.LeagueService;
import com.league.service.team.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("user/leagues")
public class LeagueController {

    @Autowired
    LeagueService leagueService;

    @Autowired
    TeamService teamService;

    @GetMapping("myLeagues")
    public String myLeagues(Model model) {
        model.addAttribute("leagues", leagueService.getUserLeagues());
        return "user/leagues/myLeagues";
    }

    @GetMapping("addLeague")
    public String addLeague(Model model) {
        model.addAttribute("league", new League());
        return "user/leagues/addLeague";
    }

    @PostMapping("addLeagueProceed")
    public String addTeamLeague(@ModelAttribute("league") League league) {
        leagueService.saveLeague(league);
        return "redirect:/user/leagues/myLeagues";
    }

    @GetMapping("manageLeague")
    public String manageLeague(@RequestParam("leagueId") int leagueId, Model model) {
        List<Team> teams = teamService.getLeagueTeams(leagueId);
        model.addAttribute("teams", teams);
        League league = leagueService.findById(leagueId);
        model.addAttribute("league", league);
        if(!league.isStarted()) {
            return "user/leagues/manageLeague";
        }
        return "redirect:/user/leagues/manageActiveLeague?leagueId=" + leagueId;
    }

    @PostMapping("manageLeagueProceed")
    public String manageLeagueProceed(@ModelAttribute("league") League league) {
        leagueService.saveLeague(league);
        return "redirect:/user/leagues/myLeagues";
    }

    @GetMapping("deleteLeague")
    public String deleteLeagueProceed(@RequestParam("leagueId") int leagueId) {
        leagueService.deleteLeague(leagueId);
        return "redirect:/user/leagues/myLeagues";
    }

    @GetMapping("startLeague")
    public String startLeague(@RequestParam("leagueId") int leagueId, Model model) {
        League league = leagueService.findById(leagueId);
        if(league.isStarted() || league.numberOfTeams()<2) {
            //TODO stworzyc endpoint dla tego erroru
            return "redirect:/user/leagues/myLeagues";
        }
        model.addAttribute("league", league);
        return "/user/leagues/startLeague";
    }

    @PostMapping("startLeagueProceed")
    public String startLeagueProceed(@ModelAttribute("league") League league) {
        League theLeague = leagueService.findById(league.getId());
        theLeague.setStarted(league.isStarted());
        theLeague.setRematch(league.isRematch());
        leagueService.startLeague(theLeague);
        return "redirect:/user/leagues/myLeagues";
    }

    @GetMapping("manageActiveLeague")
    public String manageActiveTable(@RequestParam("leagueId") int leagueId, Model model) {
        League league = leagueService.findById(leagueId);
        model.addAttribute("league", league);
        model.addAttribute("standings", leagueService.getStandings(league));
        return "/user/leagues/manageActiveLeague";
    }

}