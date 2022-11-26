package com.david.kafkastreams.repo;

import com.david.kafkastreams.entity.TeamAggregationExpectedCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamAggregationExpectedCountRepo extends JpaRepository<TeamAggregationExpectedCount, Long> {

//    @Query("SELECT t FROM TeamAggregationExpectedCount t WHERE t.name = ?1")
    TeamAggregationExpectedCount findByName(String name);
}
