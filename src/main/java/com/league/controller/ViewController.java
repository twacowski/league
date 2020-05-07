package com.league.controller;

import com.league.model.*;
import com.league.model.enums.Status;
import com.league.service.league.LeagueService;
import com.league.service.match.MatchService;
import com.league.service.player.PlayerService;
import com.league.service.team.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("view")
public class ViewController {

    @Autowired
    LeagueService leagueService;

    @Autowired
    MatchService matchService;

    @Autowired
    PlayerService playerService;

    @Autowired
    TeamService teamService;

    @GetMapping("matchDetails")
    public String matchDetails(@RequestParam("matchId") int matchId, Model model) {
        Match match = matchService.findById(matchId);
        matchService.createSheets(match);
        model.addAttribute("match", match);
        return "view/matchDetails";
    }

    @GetMapping("league")
    public String league(@RequestParam("leagueId") int leagueId, Model model) {
        League league = leagueService.findById(leagueId);
        List<Participation> participations = league.getAcceptedParticipationList();
        Team team = leagueService.participatingTeam(participations);

        model.addAttribute("team", team);
        model.addAttribute("league", league);
        model.addAttribute("participations", participations);

        if(league.getStatus() == Status.TOREGISTER) {
            return "view/leagueInfo";
        }

        model.addAttribute("standings", leagueService.getStandings(league));
        return "view/league";
    }

    @GetMapping("team")
    public String team(@RequestParam("teamId") int teamId, Model model) {
        Team team = teamService.findById(teamId);
        model.addAttribute("players", team.getPlayers());
        model.addAttribute("team", team);
        return "view/team";
    }

    @GetMapping("player")
    public String player(@RequestParam("playerId") int playerId, Model model) {
        Player player = playerService.findById(playerId);
        model.addAttribute("player", player);
        return "view/player";
    }

    @GetMapping("topScorers")
    public String topScorers(@RequestParam("leagueId") int leagueId, Model model) {
        League league = leagueService.findById(leagueId);
        model.addAttribute("league", league);
        model.addAttribute("topScorers", playerService.getTopScorers(league));
        return "view/topScorers";
    }

    @GetMapping("ownGoals")
    public String ownGoals(@RequestParam("leagueId") int leagueId, Model model) {
        League league = leagueService.findById(leagueId);
        model.addAttribute("league", league);
        model.addAttribute("standings", leagueService.getStandings(league));
        model.addAttribute("ownGoals", playerService.getMostOwnGoals(league));
        return "view/ownGoals";
    }

    @GetMapping("yellowCards")
    public String yellowCards(@RequestParam("leagueId") int leagueId, Model model) {
        League league = leagueService.findById(leagueId);
        model.addAttribute("league", league);
        model.addAttribute("standings", leagueService.getStandings(league));
        model.addAttribute("yellowCards", playerService.getMostYellowCards(league));
        return "view/yellowCards";
    }

    @GetMapping("redCards")
    public String redCards(@RequestParam("leagueId") int leagueId, Model model) {
        League league = leagueService.findById(leagueId);
        model.addAttribute("league", league);
        model.addAttribute("standings", leagueService.getStandings(league));
        model.addAttribute("redCards", playerService.getMostRedCards(league));
        return "view/redCards";
    }

}
