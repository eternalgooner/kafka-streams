package com.david.kafkastreams.controller;

import com.david.kafkastreams.StreamProcessingSimulation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoint which exposes the total count for a given key in the local state store
 */
@RestController
public class LocalStateStore {

    @Autowired
    private StreamProcessingSimulation simulation;

    @GetMapping("/store/total/{league}")
    public Long getTotalReceivedForLeague(@PathVariable String league) {
        System.out.println("API call to get total count value for league: " + league);
        long currentCountForLeague = simulation.getLeagueTeamTotalStore().get(league);
        System.out.println("returning total count value " + currentCountForLeague + " for league: " + league);
        return currentCountForLeague;
    }
}
