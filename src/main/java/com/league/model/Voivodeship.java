package com.league.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "voivodeships")
public class Voivodeship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;

    @OneToMany(mappedBy="voivodeship", cascade={CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private List<County> countiesList;

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

    public List<County> getCountiesList() {
        return countiesList;
    }

    public void setCountiesList(List<County> countiesList) {
        this.countiesList = countiesList;
    }

    public List<League> getLeagueList() {
        return leagueList;
    }

    public void setLeagueList(List<League> leagueList) {
        this.leagueList = leagueList;
    }
}
