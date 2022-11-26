package com.david.kafkastreams.kafka.client;

import com.david.kafkastreams.kafka.KafkaMessage;
import com.david.kafkastreams.model.FootballTeam;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

import static com.david.kafkastreams.config.KafkaConsumerStreamProperties.TEAM_STREAM_TOPIC;

/**
 * Class that reads in a csv file, converts to a List<FootballTeam> and sends to a topic
 */
@Service
public class KafkaProducerStream implements Runnable {

    @Autowired
    KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    @Override
    public void run() {
        try {
            File csvFile = ResourceUtils.getFile("classpath:teams.csv");

            CsvSchema csvSchema = CsvSchema.emptySchema().withHeader();
            CsvMapper csvMapper = new CsvMapper();

            MappingIterator<FootballTeam> teams = csvMapper
                    .readerFor(FootballTeam.class)
                    .with(csvSchema)
                    .readValues(csvFile);

            List<FootballTeam> teamList = teams.readAll();
            teamList.forEach(sendToTopic);
        } catch (Exception e) {
            System.out.println("Exception caught while running kafka producer");
        }
        System.out.println("Finished sending the football team stream");
    }

    // simulate a stream of data by sending Teams to the kafka topic every x seconds
    Consumer<FootballTeam> sendToTopic = team -> {
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("sending team to kafka topic: " + team);
        kafkaTemplate.send(TEAM_STREAM_TOPIC, team.getLeague(), team);
    };
}