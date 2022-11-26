package com.david.kafkastreams.serialization;

import com.david.kafkastreams.model.FootballTeamAggregation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

/**
 * Custom Deserializer for use with the Kafka Stream
 */
public class FootballTeamAggregationDeserializer implements Deserializer<FootballTeamAggregation> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public FootballTeamAggregation deserialize(String topic, byte[] data) {
        if (data == null)
            return null;
        try {
            return mapper.readValue(data, FootballTeamAggregation.class);
        } catch (Exception e) {
            throw new SerializationException("Error deserializing message", e);
        }
    }
}
