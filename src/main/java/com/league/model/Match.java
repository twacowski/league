package com.league.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "league_id")
    private League league;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "gameweek_id")
    private Gameweek gameweek;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "host_id")
    private Participation host;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "guest_id")
    private Participation guest;

    @OneToMany(mappedBy = "match", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private List<ScoreSheet> scoreSheets;


    private Integer hostScore;
    private Integer guestScore;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDate;

    private String pitch;
    private boolean isPlayed;

    public Match() {
    }

    public Match(User user, League league, Gameweek gameweek, boolean isPlayed) {
        this.user = user;
        this.league = league;
        this.gameweek = gameweek;
        this.isPlayed = isPlayed;
        this.hostScore = null;
        this.guestScore = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public Gameweek getGameweek() {
        return gameweek;
    }

    public void setGameweek(Gameweek gameweek) {
        this.gameweek = gameweek;
    }

    public Participation getHost() {
        return host;
    }

    public void setHost(Participation host) {
        this.host = host;
    }

    public Participation getGuest() {
        return guest;
    }

    public void setGuest(Participation guest) {
        this.guest = guest;
    }

    public Integer getHostScore() {
        return hostScore;
    }

    public void setHostScore(Integer hostScore) {
        this.hostScore = hostScore;
    }

    public Integer getGuestScore() {
        return guestScore;
    }

    public void setGuestScore(Integer guestScore) {
        this.guestScore = guestScore;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public String getDate() {
        if(this.startDate == null) {
            return null;
        }
        String date ="";
        date += String.format("%02d", this.startDate.getDayOfMonth()) + "/"
                + String.format("%02d", this.startDate.getMonthValue()) + "/"
                + this.startDate.getYear() + " \n ";
        date += String.format("%02d", this.startDate.getHour()) + ":" + String.format("%02d", this.startDate.getMinute());
        return date;
    }

    public String getPitch() {
        return pitch;
    }

    public void setPitch(String pitch) {
        this.pitch = pitch;
    }

    public boolean isPlayed() {
        return isPlayed;
    }

    public void setPlayed(boolean played) {
        isPlayed = played;
    }

    public List<ScoreSheet> getScoreSheets() {
        return this.scoreSheets;
    }

    public void setScoreSheets(List<ScoreSheet> scoreSheets) {
        this.scoreSheets = scoreSheets;
    }

    public List<ScoreSheet> getHostScoreSheets() {
        List<ScoreSheet> scoreSheets = getScoreSheets();
        List<ScoreSheet> hostScoreSheets = new ArrayList<>();
        if(scoreSheets == null) {
            return hostScoreSheets;
        }
        for (ScoreSheet scoreSheet : scoreSheets) {
            if(scoreSheet.isHost()) {
                hostScoreSheets.add(scoreSheet);
            }
        }
        return hostScoreSheets;
    }

    public void setHostScoreSheets(List<ScoreSheet> hostScoresheets) {
        List<ScoreSheet> scoreSheets = new ArrayList<>();
        scoreSheets.addAll(getScoreSheets());
        if(scoreSheets == null) {
            this.scoreSheets = hostScoresheets;
            return;
        } else if(scoreSheets.isEmpty()) {
            this.scoreSheets = hostScoresheets;
            return;
        }
        for (ScoreSheet scoreSheet : getScoreSheets()) {
            if(scoreSheet.isHost()) {
                scoreSheets.remove(scoreSheet);
            }
        }
        scoreSheets.addAll(hostScoresheets);
        this.scoreSheets = scoreSheets;
    }

    public List<ScoreSheet> getGuestScoreSheets() {
        List<ScoreSheet> scoreSheets = getScoreSheets();
        List<ScoreSheet> guestScoreSheets = new ArrayList<>();
        if(scoreSheets == null) {
            return guestScoreSheets;
        }
        for (ScoreSheet scoreSheet : scoreSheets) {
            if(!scoreSheet.isHost()) {
                guestScoreSheets.add(scoreSheet);
            }
        }
        return guestScoreSheets;
    }

    public void setGuestScoreSheets(List<ScoreSheet> guestScoresheets) {
        List<ScoreSheet> scoreSheets = new ArrayList<>();
        scoreSheets.addAll(getScoreSheets());
        if(scoreSheets == null) {
            this.scoreSheets = guestScoresheets;
            return;
        } else if(scoreSheets.isEmpty()) {
            this.scoreSheets = guestScoresheets;
            return;
        }
        for (ScoreSheet scoreSheet : getScoreSheets()) {
            if(!scoreSheet.isHost()) {
                scoreSheets.remove(scoreSheet);
            }
        }
        scoreSheets.addAll(guestScoresheets);
        this.scoreSheets = scoreSheets;
    }

    public void switchHost() {
        Participation temp = host;
        host = guest;
        guest = temp;
    }
}
