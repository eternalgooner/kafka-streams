package com.david.kafkastreams.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Entity used to get the expected count from the DB
 */
@Entity
@Table(name = "league_agg")
public class TeamAggregationExpectedCount {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private int expectedCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExpectedCount() {
        return expectedCount;
    }

    public void setExpectedCount(int expectedCount) {
        this.expectedCount = expectedCount;
    }

    @Override
    public String toString() {
        return "TeamAggregationExpectedCount{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", expectedCount=" + expectedCount +
                '}';
    }
}
