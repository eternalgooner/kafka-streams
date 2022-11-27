package com.david.kafkastreams.controller;

import com.david.kafkastreams.StreamProcessingSimulation;
import com.david.kafkastreams.model.FootballTeamAggregation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoint which exposes the current aggregation for a given key in the local state store
 */
@RestController
public class LocalStateStore {

    @Autowired
    private StreamProcessingSimulation simulation;

    @GetMapping("/store/{league}")
    public FootballTeamAggregation getLeagueAggregation(@PathVariable String league) {
        System.out.println("API call to get agg for league: " + league);
        FootballTeamAggregation aggregation = simulation.getLeagueAggStore().get(league);
        System.out.println("returning agg value " + aggregation + " for league: " + league);
        return aggregation;
    }
}
