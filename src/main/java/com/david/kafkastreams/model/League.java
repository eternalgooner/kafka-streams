package com.david.kafkastreams.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * POJO modelling a Football league
 * A League will contain many unique Football Teams
 */
public class League {

    private String name;
    private Set<FootballTeam> teamSet = new HashSet<>();

    public boolean addTeam(FootballTeam footballTeam) {
        boolean teamAdded = teamSet.add(footballTeam);
        System.out.println("team: " + footballTeam.getName() + " added: " + teamAdded);
        return teamAdded;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<FootballTeam> getTeamSet() {
        return teamSet;
    }

    public void setTeamSet(Set<FootballTeam> teamSet) {
        this.teamSet = teamSet;
    }

    @Override
    public String toString() {
        return "League{" +
                "name='" + name + '\'' +
                ", teamSet=" + teamSet +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        League league = (League) o;
        return name.equals(league.name) && teamSet.equals(league.teamSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, teamSet);
    }
}
