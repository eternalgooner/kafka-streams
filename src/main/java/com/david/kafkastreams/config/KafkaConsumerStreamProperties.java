package com.david.kafkastreams.config;

import com.david.kafkastreams.serialization.FootballTeamSerDes;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;

public class KafkaConsumerStreamProperties {

    public static final String TEAM_STREAM_TOPIC = "team-stream";
    public static final String FINAL_TEAM_AGGREGATE_TOPIC = "final-team-aggregate";

    public static Properties getProperties() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "football-teams-agg-app");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 5000);

        // stream config for use by kafka streams under the hood
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, FootballTeamSerDes.class);

        // standard kafka config for reading from topic
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonDeserializer.class);

        return properties;
    }
}
