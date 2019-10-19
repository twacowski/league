package com.league.controller;

import com.league.model.Player;
import com.league.model.Team;
import com.league.service.league.LeagueService;
import com.league.service.player.PlayerService;
import com.league.service.team.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("user/teams")
public class TeamController {

    @Autowired
    LeagueService leagueService;

    @Autowired
    TeamService teamService;

    @Autowired
    PlayerService playerService;

    @GetMapping("myTeams")
    public String myTeams(Model model) {
        model.addAttribute("teams", teamService.getUserTeams());
        return "user/teams/myTeams";
    }

    @GetMapping("addTeam")
    public String addTeam(Model model) {
        model.addAttribute("team", new Team());
        model.addAttribute("leagues", leagueService.getUserLeagues());
        return "user/teams/addTeam";
    }

    @PostMapping("addTeamProceed")
    public String addTeamProceed(@ModelAttribute("team") Team team) {
        teamService.saveTeam(team);
        return "redirect:/user/teams/myTeams";
    }

    @GetMapping("manageTeam")
    public String manageTeam(@RequestParam("teamId") int teamId, Model model) {
        List<Player> players = playerService.getTeamPlayers(teamId);
        model.addAttribute("players", players);
        model.addAttribute("leagues", leagueService.getUserLeagues());
        model.addAttribute("team", teamService.findById(teamId));
        return "user/teams/manageTeam";
    }

    @PostMapping("manageTeamProceed")
    public String manageTeamProceed(@ModelAttribute("team") Team team) {
        teamService.saveTeam(team);
        return "redirect:/user/teams/myTeams";
    }

    @GetMapping("deleteTeam")
    public String deleteTeamProceed(@RequestParam("teamId") int teamId) {
        teamService.deleteTeam(teamId);
        return "redirect:/user/teams/myTeams";
    }
}
