package com.david.kafkastreams.model;

import com.david.kafkastreams.SpringContext;
import com.david.kafkastreams.entity.TeamAggregationExpectedCount;
import com.david.kafkastreams.kafka.KafkaMessage;
import com.david.kafkastreams.repo.TeamAggregationExpectedCountRepo;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *  This class represents the total aggregated view of all grouped Football Teams
 *  The League POJO will hold all grouped Football Teams, using their league name as the grouping key
 *
 *  A new instance of this class is used by the Kafka Stream every time a new key is detected
 */
public class FootballTeamAggregation implements KafkaMessage {

//    private League league = new League();
    private String name;
    private Set<FootballTeam> teamSet = new HashSet<>();
    private int expectedCount;

    public FootballTeamAggregation() {
        System.out.println("instantiating new FTA...should only happen when new key detected");
    }

    // get reference to Spring Bean from POJO
    private TeamAggregationExpectedCountRepo getRepo() {
        return SpringContext.getBean(TeamAggregationExpectedCountRepo.class);
    }

    /**
     *  This method initializes the expected count for the FootballTeamAggregation
     *  It calls the DB and gets the expected count using the league name as param
     * @param leagueName the value used to get the expected count from the DB
     */
    private void initExpectedCount(String leagueName) {
        System.out.println("setting expected count in Team Aggregation");
        TeamAggregationExpectedCount expectedCountDetails = getRepo().findByName(leagueName);
        System.out.println("got Expected details from DB: " + expectedCountDetails);
        this.expectedCount = expectedCountDetails.getExpectedCount();
    }

    /**
     * This method is called every time the stream aggregates a new Football Team
     * @param leagueKey the key is used to get the expected count from the DB
     * @param footballTeam the team to add to the aggregate
     * @return the aggregation to be used in the stream
     */
    public FootballTeamAggregation addTeamToAggregate(String leagueKey, FootballTeam footballTeam) {
        System.out.println("adding team: " + footballTeam + " to aggregate with league key: " + leagueKey);
        // we need to check here if this is the first time we've seen this key
        // if it is, we need to get the expected count from the DB
        // 1st check is the league name set
        if (this.getName() == null) {
            System.out.println("league name is null, get expected count from DB and then set the league name");
            initExpectedCount(leagueKey);
            this.setName(leagueKey);
        }

        // add team to aggregate if it's a new unique record
        boolean newTeamAdded = teamSet.add(footballTeam);
        System.out.println("new team added in agg: " + newTeamAdded);
        return newTeamAdded ? this : null;
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

    public int getExpectedCount() {
        return expectedCount;
    }

    public void setExpectedCount(int expectedCount) {
        this.expectedCount = expectedCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FootballTeamAggregation that = (FootballTeamAggregation) o;
        return expectedCount == that.expectedCount && name.equals(that.name) && teamSet.equals(that.teamSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, teamSet, expectedCount);
    }

    @Override
    public String toString() {
        return "FootballTeamAggregation{" +
                "name='" + name + '\'' +
                ", teamSet=" + teamSet +
                ", expectedCount=" + expectedCount +
                '}';
    }
}
