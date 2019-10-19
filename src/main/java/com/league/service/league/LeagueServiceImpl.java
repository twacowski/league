package com.league.service.league;

import com.league.model.League;
import com.league.model.User;
import com.league.repository.LeagueRepository;
import com.league.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeagueServiceImpl implements LeagueService {

    @Autowired
    LeagueRepository leagueRepository;

    @Autowired
    UserService userService;

    @Override
    public void saveLeague(League league) {
        if(league.getUser() == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findByUsername(auth.getName());
            league.setUser(user);
        }
        leagueRepository.save(league);
    }

    @Override
    public void deleteLeague(int leagueId) {
        leagueRepository.deleteById(leagueId);
    }

    @Override
    public League findById(int id) {
        return leagueRepository.findById(id).get();
    }

    @Override
    public List<League> getUserLeagues() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<League> leagues = leagueRepository.getLeaguesByUserName(auth.getName());
        return leagues;
    }
}
