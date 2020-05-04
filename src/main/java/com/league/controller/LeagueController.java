package com.league.controller;

import com.league.model.League;
import com.league.model.Participation;
import com.league.model.Team;
import com.league.model.enums.Status;
import com.league.service.league.LeagueService;
import com.league.service.location.LocationService;
import com.league.service.player.PlayerService;
import com.league.service.team.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    LocationService locationService;

    @Autowired
    private PlayerService playerService;

    @GetMapping("myLeagues")
    public String myLeagues(Model model) {
        model.addAttribute("leagues", leagueService.getUserLeagues());
        model.addAttribute("participations", leagueService.getAllUserParticipations());
        return "user/leagues/myLeagues";
    }

    @GetMapping("addLeague")
    public String addLeague(Model model) {
        model.addAttribute("league", new League());
        model.addAttribute("voivodeships", locationService.getListOfVoivodeships());
        model.addAttribute("counties", locationService.getListOfCounties());
        return "user/leagues/addLeague";
    }

    @PostMapping("addLeagueProceed")
    public String addTeamLeague(@ModelAttribute("league") League league) {
        league.setStatus(Status.INACTIVE);
        leagueService.saveLeague(league);
        return "redirect:/user/leagues/myLeagues";
    }

    @GetMapping("manageLeague")
    public String manageLeague(@RequestParam("leagueId") int leagueId, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        League league = leagueService.findById(leagueId);
        if (!league.getUser().getUserName().equals(auth.getName()) || league.getStatus() == Status.ARCHIVED) {
            return "accessDenied";
        }

        if (league.getStatus() == Status.ACTIVE) {
            return "redirect:/user/leagues/manageActiveLeague?leagueId=" + leagueId;
        }

        model.addAttribute("teams", teamService.getLeagueTeams(leagueId));
        model.addAttribute("league", league);
        model.addAttribute("voivodeships", locationService.getListOfVoivodeships());
        model.addAttribute("counties", locationService.getListOfCountiesFromVoivodeship(league.getVoivodeship()));

        return "user/leagues/manageLeague";
    }

    @PostMapping("manageLeagueProceed")
    public String manageLeagueProceed(@ModelAttribute("league") League league) {
        leagueService.saveLeague(league);
        return "redirect:/user/leagues/myLeagues";
    }

    @GetMapping("deleteLeague")
    public String deleteLeagueProceed(@RequestParam("leagueId") int leagueId) {
        League league = leagueService.findById(leagueId);
        if (league.getStatus() == Status.ARCHIVED || league.getStatus() == Status.ACTIVE) {
            return "redirect:/user/leagues/myLeagues";
        }

        leagueService.deleteLeague(leagueId);
        return "redirect:/user/leagues/myLeagues";
    }

    @GetMapping("signUp")
    public String signUp(@RequestParam("leagueId") int leagueId, Model model) {
        Participation participation = new Participation(leagueService.findById(leagueId));
        List<Team> teams = teamService.getUserTeams();

        model.addAttribute("participation", participation);
        model.addAttribute("teams", teams);

        return "user/leagues/signUp";
    }

    @PostMapping("signUpProceed")
    public String signUp(@ModelAttribute("participation") Participation participation) {
        leagueService.saveParticipation(participation);
        return "redirect:/user/leagues/myLeagues";
    }

    @GetMapping("cancelParticipation")
    public String cancelParticipation(@RequestParam("leagueId") int leagueId) {
        leagueService.cancelParticipation(leagueService.findById(leagueId));

        return "redirect:/user/leagues/myLeagues";
    }

    @GetMapping("acceptTeam")
    public String acceptTeam(@RequestParam("participationId") int participationId) {
        League league = leagueService.acceptTeam(participationId);

        return "redirect:/user/leagues/manageLeague?leagueId=" + league.getId();
    }

    @GetMapping("rejectTeam")
    public String rejectTeam(@RequestParam("participationId") int participationId) {
        League league = leagueService.rejectTeam(participationId);

        return "redirect:/user/leagues/manageLeague?leagueId=" + league.getId();
    }

    @GetMapping("startLeague")
    public String startLeague(@RequestParam("leagueId") int leagueId, Model model) {
        League league = leagueService.findById(leagueId);
        if (league.isStarted() || league.numberOfTeams() < 2) {
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

    @GetMapping("topScorers")
    public String topScorers(@RequestParam("leagueId") int leagueId, Model model) {
        League league = leagueService.findById(leagueId);
        model.addAttribute("league", league);
        model.addAttribute("standings", leagueService.getStandings(league));
        model.addAttribute("topScorers", playerService.getTopScorers(league));
        return "/user/leagues/topScorers";
    }

    @GetMapping("ownGoals")
    public String ownGoals(@RequestParam("leagueId") int leagueId, Model model) {
        League league = leagueService.findById(leagueId);
        model.addAttribute("league", league);
        model.addAttribute("standings", leagueService.getStandings(league));
        model.addAttribute("ownGoals", playerService.getMostOwnGoals(league));
        return "/user/leagues/ownGoals";
    }

    @GetMapping("yellowCards")
    public String yellowCards(@RequestParam("leagueId") int leagueId, Model model) {
        League league = leagueService.findById(leagueId);
        model.addAttribute("league", league);
        model.addAttribute("standings", leagueService.getStandings(league));
        model.addAttribute("yellowCards", playerService.getMostYellowCards(league));
        return "/user/leagues/yellowCards";
    }

    @GetMapping("redCards")
    public String redCards(@RequestParam("leagueId") int leagueId, Model model) {
        League league = leagueService.findById(leagueId);
        model.addAttribute("league", league);
        model.addAttribute("standings", leagueService.getStandings(league));
        model.addAttribute("redCards", playerService.getMostRedCards(league));
        return "/user/leagues/redCards";
    }

}
