package com.david.kafkastreams.model;

import com.david.kafkastreams.kafka.KafkaMessage;

import java.util.Objects;

/**
 *  POJO modelling a Football Team
 */
public class FootballTeam implements KafkaMessage {

    private String name;
    private int capacity;
    private String stadium;
    private String league;

    public FootballTeam() {
    }

    @Override
    public String toString() {
        return "FootyTeam{" +
                "name='" + name + '\'' +
                ", capacity=" + capacity +
                ", stadium='" + stadium + '\'' +
                ", league='" + league + '\'' +
                '}';
    }

    public FootballTeam(String name, int capacity, String stadium, String league) {
        this.name = name;
        this.capacity = capacity;
        this.stadium = stadium;
        this.league = league;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FootballTeam that = (FootballTeam) o;
        return name.equals(that.name) && league.equals(that.league);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, league);
    }
}
