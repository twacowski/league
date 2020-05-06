package com.league.controller;


import com.league.model.County;
import com.league.model.User;
import com.league.model.Voivodeship;
import com.league.service.league.LeagueService;
import com.league.service.location.LocationService;
import com.league.service.player.PlayerService;
import com.league.service.team.TeamService;
import com.league.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    LocationService locationService;

    @GetMapping("/")
    public String index(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!auth.getName().equals("anonymousUser")){
            return "user/index";
        }

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

        return "redirect:/";
    }

    @GetMapping("/search")
    public String search(@RequestParam String phrase, Model model) {

        model.addAttribute("users", userService.searchUsersByPhrase(phrase));
        model.addAttribute("players", playerService.searchPlayersByPhrase(phrase));
        model.addAttribute("teams", teamService.searchTeamsByPhrase(phrase));
        model.addAttribute("leagues", leagueService.searchLeaguesByPhrase(phrase));

        return "searchingList";
    }

    @GetMapping("/getCountiesFromVoivodeship")
    @ResponseBody
    public ResponseEntity<?> getCountiesFromVoivodeship(@RequestParam("voivodeship") Voivodeship voivodeship) {
        return ResponseEntity.ok(locationService.getCountiesFromVoivodeship(voivodeship));
    }

    @GetMapping("/competitionsBase")
    public String competitionsBase(Model model) {

        model.addAttribute("voivodeship", new Voivodeship());
        model.addAttribute("county", new County());
        model.addAttribute("voivodeships", locationService.getListOfVoivodeships());
        model.addAttribute("counties", locationService.getListOfCounties());
        model.addAttribute("leagues", leagueService.getAllLeagues());

        return "competitionsBase";
    }

    @PostMapping("/competitionsBase")
    public String competitionsBaseWithFilters(@RequestParam("voivodeship") Voivodeship voivodeship,
                                              @RequestParam("county") County county,
                                              Model model) {

        model.addAttribute("voivodeship", voivodeship);
        model.addAttribute("county", county);
        model.addAttribute("voivodeships", locationService.getListOfVoivodeships());
        model.addAttribute("counties", locationService.getListOfCountiesFromVoivodeship(voivodeship));
        model.addAttribute("leagues", leagueService.getLeaguesByLocation(voivodeship, county));

        return "competitionsBase";
    }

}
