package com.league.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private int points;
    private int scoredGoals;
    private int concededGoals;
    private int balance;
    private int wins;
    private int draws;
    private int loses;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "league_id")
    private League league;

    @OneToMany(mappedBy = "team",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private List<Player> players;

    @OneToMany(mappedBy = "team",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    private List<Participation> participationList;

    @OneToMany(mappedBy = "host", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private List<Match> hostMatches;

    @OneToMany(mappedBy = "guest", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private List<Match> guestMatches;

    public List<Match> getHostMatches() {
        return hostMatches;
    }

    public void setHostMatches(List<Match> hostMatches) {
        this.hostMatches = hostMatches;
    }

    public List<Match> getGuestMatches() {
        return guestMatches;
    }

    public void setGuestMatches(List<Match> guestMatches) {
        this.guestMatches = guestMatches;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getScoredGoals() {
        return scoredGoals;
    }

    public void setScoredGoals(int scoredGoals) {
        this.scoredGoals = scoredGoals;
    }

    public int getConcededGoals() {
        return concededGoals;
    }

    public void setConcededGoals(int concededGoals) {
        this.concededGoals = concededGoals;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    @Override
    public String toString() {
        return getName();
    }

    public int numberOfMatches() {
        return wins+draws+loses;
    }

    public int numberOfPlayers() { return players.size();}
}
