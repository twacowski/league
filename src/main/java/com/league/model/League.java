package com.league.model;

import com.league.model.enums.CompetitionType;
import com.league.model.enums.Status;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "leagues")
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private boolean isStarted;
    private boolean rematch;
    private boolean enrollment;
    private String city;
    private int number;
    private String details;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private CompetitionType type;
    private int season;

    @ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="voivodeship_id")
    private Voivodeship voivodeship;

    @ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="county_id")
    private County county;

    @OneToMany(mappedBy="league", cascade={CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private List<Participation> participationList;

    @OneToMany(mappedBy="league", cascade={CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private List<Participation> stats;

    @OneToMany(mappedBy="league", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private List<Gameweek> gameweeks;

    @OneToMany(mappedBy="league", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private List<Match> matches;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public boolean isRematch() {
        return rematch;
    }

    public void setRematch(boolean rematch) {
        this.rematch = rematch;
    }

    public List<Gameweek> getGameweeks() {
        return gameweeks;
    }

    public void setGameweeks(List<Gameweek> gameweeks) {
        this.gameweeks = gameweeks;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    @Override
    public String toString() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public CompetitionType getType() {
        return type;
    }

    public void setType(CompetitionType type) {
        this.type = type;
    }

    public boolean isEnrollment() {
        return enrollment;
    }

    public void setEnrollment(boolean enrollment) {
        this.enrollment = enrollment;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public List<Participation> getParticipationList() {
        return participationList;
    }

    public void setParticipationList(List<Participation> participationList) {
        this.participationList = participationList;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Voivodeship getVoivodeship() {
        return voivodeship;
    }

    public void setVoivodeship(Voivodeship voivodeship) {
        this.voivodeship = voivodeship;
    }

    public County getCounty() {
        return county;
    }

    public void setCounty(County county) {
        this.county = county;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public List<Participation> getStats() {
        return stats;
    }

    public void setStats(List<Participation> stats) {
        this.stats = stats;
    }

    public void cancelParticipation(Participation participation) {
        this.participationList.remove(participation);
    }

    public int acceptedParticipations() {
        int count = 0;
        for(Participation participation : this.participationList) {
            if(participation.isAccepted()) {
                count++;
            }
        }
        return count;
    }

    public List<Participation> getAcceptedParticipationList() {
        List<Participation> accepted = new ArrayList<>();
        for(Participation participation : getParticipationList()) {
            if(participation.isAccepted()) {
                accepted.add(participation);
            }
        }
        return accepted;
    }
}
