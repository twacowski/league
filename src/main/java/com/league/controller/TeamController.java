package com.league.controller;

import com.league.model.Player;
import com.league.model.Team;
import com.league.service.league.LeagueService;
import com.league.service.player.PlayerService;
import com.league.service.team.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("user/teams")
public class TeamController {

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @Autowired
    LeagueService leagueService;

    @Autowired
    TeamService teamService;

    @Autowired
    PlayerService playerService;

    @GetMapping("myTeams")
    public String myTeams(RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("page", 0);
        redirectAttributes.addAttribute("size", 10);
        return "redirect:teamsList";
    }

    @GetMapping("teamsList")
    public String teamsList(Pageable pageable, Model model) {
        Page<Team> teams = teamService.findAll(pageable);
        model.addAttribute("teams", teams);
        return "user/teams/myTeams";
    }

    @GetMapping("addTeam")
    public String addTeam(Model model) {
        model.addAttribute("team", new Team());
        return "user/teams/addTeam";
    }

    @PostMapping("addTeamProceed")
    public String addTeamProceed(@Valid @ModelAttribute("team") Team team, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "user/teams/addTeam";
        }

        teamService.saveTeam(team);
        return "redirect:/user/teams/myTeams";
    }

    @GetMapping("manageTeam")
    public String manageTeam(@RequestParam("teamId") int teamId, Model model) {
        Team team = teamService.findById(teamId);
        model.addAttribute("team", team);
        return "user/teams/manageTeam";
    }

    @PostMapping("manageTeamProceed")
    public String manageTeamProceed(@Valid @ModelAttribute("team") Team team, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "user/teams/manageTeam";
        }

        teamService.saveTeam(team);
        return "redirect:/user/teams/myTeams";
    }

    @GetMapping("deleteTeam")
    public String deleteTeamProceed(@RequestParam("teamId") int teamId) {
        teamService.deleteTeam(teamId);
        return "redirect:/user/teams/myTeams";
    }
}
