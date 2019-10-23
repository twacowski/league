package com.league.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="league_id")
    private League league;

    @ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="gameweek_id")
    private Gameweek gameweek;

    @ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="host_id")
    private Team host;

    @ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="guest_id")
    private Team guest;

    @OneToMany(mappedBy="match", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private List<ScoreSheet> hostScoreSheets;

    @OneToMany(mappedBy="match", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private List<ScoreSheet> guestScoreSheets;


    private Integer hostScore;
    private Integer guestScore;
    private Date date;
    private String time;
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

    public Team getHost() {
        return host;
    }

    public void setHost(Team host) {
        this.host = host;
    }

    public Team getGuest() {
        return guest;
    }

    public void setGuest(Team guest) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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


    //TODO EDIT THIS GETTERS AND SETTERS ----> TOO COMPLICATED!
    public List<ScoreSheet> getHostScoreSheets() {
        if(hostScoreSheets == null) {
            return null;
        } else {
            for(int i = 0; i < hostScoreSheets.size(); i++) {
                if (hostScoreSheets.get(i) == null || hostScoreSheets.get(i).getMatch() == null || hostScoreSheets.get(i).getPlayer() == null) {
                    break;
                }
                if (hostScoreSheets.get(i).getMatch().getGuest() == hostScoreSheets.get(i).getPlayer().getTeam()) {
                    hostScoreSheets.remove(i);
                }
            }
        }
        return hostScoreSheets;
    }

    public void setHostScoreSheets(List<ScoreSheet> hostScoreSheets) {
        if(hostScoreSheets == null) {
            this.hostScoreSheets = hostScoreSheets;
            return;
        } else {
            for(int i = 0; i < hostScoreSheets.size(); i++) {
                if (hostScoreSheets.get(i) == null) {
                    break;
                }
                if (hostScoreSheets.get(i).getMatch().getGuest() == hostScoreSheets.get(i).getPlayer().getTeam()) {
                    hostScoreSheets.remove(i);
                }
            }
        }
        this.hostScoreSheets = hostScoreSheets;
    }

    public List<ScoreSheet> getGuestScoreSheets() {
        if(guestScoreSheets == null) {
            return null;
        } else {
            for (int i = 0; i < guestScoreSheets.size(); i++) {
                if (guestScoreSheets.get(i) == null || guestScoreSheets.get(i).getMatch() == null || guestScoreSheets.get(i).getPlayer() == null) {
                    break;
                } else if (guestScoreSheets.get(i).getMatch().getHost().equals(guestScoreSheets.get(i).getPlayer().getTeam())) {
                    guestScoreSheets.remove(i);
                }
            }
        }
        return guestScoreSheets;
    }

    public void setGuestScoreSheets(List<ScoreSheet> guestScoreSheets) {
        if(guestScoreSheets == null) {
            this.guestScoreSheets = guestScoreSheets;
            return;
        } else {
            for (int i = 0; i < guestScoreSheets.size(); i++) {
                if (guestScoreSheets.get(i) == null) {
                    break;
                } else if (guestScoreSheets.get(i).getMatch().getHost() == guestScoreSheets.get(i).getPlayer().getTeam()) {
                    guestScoreSheets.remove(i);
                }
            }
        }
        this.guestScoreSheets = guestScoreSheets;
    }

    public void switchHost() {
        Team temp = host;
        host = guest;
        guest = temp;
    }
}
