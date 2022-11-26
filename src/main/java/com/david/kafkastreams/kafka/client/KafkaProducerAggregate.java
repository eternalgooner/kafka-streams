package com.david.kafkastreams.kafka.client;

import com.david.kafkastreams.kafka.KafkaMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.david.kafkastreams.config.KafkaConsumerStreamProperties.FINAL_TEAM_AGGREGATE_TOPIC;

/**
 * Class sends the complete aggregated message to the team aggregate topic
 */
@Service
public class KafkaProducerAggregate {

    @Autowired
    KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    public void send(String key, KafkaMessage message) {
        System.out.println("sending aggregate message to aggregate kafka topic: " + message);
        kafkaTemplate.send(FINAL_TEAM_AGGREGATE_TOPIC, key, message);
    }
}