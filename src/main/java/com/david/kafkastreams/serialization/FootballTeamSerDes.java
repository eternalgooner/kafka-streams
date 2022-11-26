package com.david.kafkastreams.serialization;

import com.david.kafkastreams.model.FootballTeam;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

/**
 * Custom Football Team Serde implementation
 * Required by Kafka Streams if using your own POJOs
 */
public class FootballTeamSerDes implements Serde<FootballTeam> {
    @Override
    public Serializer<FootballTeam> serializer() {
        return new JsonSerializer<>();
    }

    @Override
    public Deserializer<FootballTeam> deserializer() {
        return new JsonDeserializer<>(FootballTeam.class);
    }
}
