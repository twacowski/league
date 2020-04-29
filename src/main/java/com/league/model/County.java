package com.league.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "counties")
public class County {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    String name;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "voivodeship_id")
    private Voivodeship voivodeship;

    @OneToMany(mappedBy="voivodeship", cascade={CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private List<League> leagueList;

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

    public Voivodeship getVoivodeship() {
        return voivodeship;
    }

    public void setVoivodeship(Voivodeship voivodeship) {
        this.voivodeship = voivodeship;
    }

    public List<League> getLeagueList() {
        return leagueList;
    }

    public void setLeagueList(List<League> leagueList) {
        this.leagueList = leagueList;
    }
}
