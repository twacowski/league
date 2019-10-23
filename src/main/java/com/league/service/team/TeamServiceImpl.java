package com.league.service.team;

import com.league.model.Team;
import com.league.model.User;
import com.league.repository.TeamRepository;
import com.league.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    UserService userService;

    @Override
    public void saveTeam(Team team) {
        if(team.getUser() == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findByUsername(auth.getName());
            team.setUser(user);
        }
        teamRepository.save(team);
    }

    @Override
    public void deleteTeam(int teamId) {
        teamRepository.deleteById(teamId);
    }

    @Override
    public Team findById(int id) {
        return teamRepository.findById(id).get();
    }

    @Override
    public List<Team> getUserTeams() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Team> teams = teamRepository.getTeamsByUserName(auth.getName());
        return teams;
    }

    @Override
    public List<Team> getLeagueTeams(int id) {
        return teamRepository.getTeamsByLeagueId(id);
    }


}