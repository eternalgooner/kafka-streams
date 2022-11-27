package com.david.kafkastreams;

import com.david.kafkastreams.kafka.client.KafkaProducerStream;
import com.david.kafkastreams.model.FootballTeamAggregation;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.david.kafkastreams.config.KafkaConsumerStreamProperties.getProperties;
import static com.david.kafkastreams.kafka.streams.store.FootballTeamAggregationStore.STATE_STORE_NAME;
import static com.david.kafkastreams.kafka.streams.topology.FootballTeamStreamTopology.getTopology;

@Service
public class StreamProcessingSimulation {

    @Autowired
    private KafkaProducerStream kafkaProducerStream;

    private ReadOnlyKeyValueStore<String, FootballTeamAggregation> leagueAggStore;

    public void start() {
        // run kafka producer to send data into the input stream topic
        // run on separate thread so we can see data coming in at the same time on the stream
        System.out.println("kafka producer starting to send teams to the input stream topic");
        new Thread(kafkaProducerStream).start();

        //start consuming stream
        // create the Kafka Stream passing in our Topology & properties
        System.out.println("starting kafka stream consumer");
        KafkaStreams kafkaStreams = new KafkaStreams(getTopology(), getProperties());

        // start the stream
        kafkaStreams.start();

        // add shutdown hook to close stream
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));

        //expose a read only state store through an API
        this.leagueAggStore =
                kafkaStreams.store(StoreQueryParameters.fromNameAndType(STATE_STORE_NAME, QueryableStoreTypes.keyValueStore()));
    }

    public ReadOnlyKeyValueStore<String, FootballTeamAggregation> getLeagueAggStore(){
        return leagueAggStore;
    }
}
