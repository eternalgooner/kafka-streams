package com.david.kafkastreams.serialization;

import com.david.kafkastreams.model.FootballTeamAggregation;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

/**
 * Custom Football Team Aggregation Serializer & Deserializer
 * Used by Kafka Streams to Materialize custom POJO
 */
public class FootballTeamAggregationSerDes implements Serde<FootballTeamAggregation> {
    @Override
    public Serializer<FootballTeamAggregation> serializer() {
        return new JsonSerializer<>();
    }

    @Override
    public Deserializer<FootballTeamAggregation> deserializer() {
        return new FootballTeamAggregationDeserializer();
    }
}
