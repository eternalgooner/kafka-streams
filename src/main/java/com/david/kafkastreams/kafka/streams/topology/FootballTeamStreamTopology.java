package com.david.kafkastreams.kafka.streams.topology;

import com.david.kafkastreams.SpringContext;
import com.david.kafkastreams.kafka.client.KafkaProducerAggregate;
import com.david.kafkastreams.model.FootballTeam;
import com.david.kafkastreams.model.FootballTeamAggregation;
import com.david.kafkastreams.serialization.FootballTeamAggregationSerDes;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

import java.util.Objects;

import static com.david.kafkastreams.config.KafkaConsumerStreamProperties.AGG_TEAM_STORE;
import static com.david.kafkastreams.config.KafkaConsumerStreamProperties.TEAM_STREAM_TOPIC;

/**
 * Describe the desired Kafka Topology here
 * e.g. this Stream should consume from topic x and do
 * step 1 - group by key
 * step 2 - aggregate
 * step 3 - create state store for querying
 * step 4 - send aggregate for processing
 *
 * Return a Topology object from this class which is then used as a param to start the stream
 */
public class FootballTeamStreamTopology {

    /**
     * This method creates the Topology to be used in the Stream
     * @return the Topology
     */
    public static Topology getTopology() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, FootballTeam> footyTeamKStream = builder.stream(TEAM_STREAM_TOPIC);

        KGroupedStream<String, FootballTeam> groupedStream = footyTeamKStream.groupByKey();

        KTable<String, FootballTeamAggregation> kTable = groupedStream.aggregate(FootballTeamAggregation::new,
                        (key, value, agg) -> agg.addTeamToAggregate(key, value),
                        Materialized.with(Serdes.String(), new FootballTeamAggregationSerDes()));

        //create local state store for the total count of teams received for a league
        kTable.toStream().groupByKey().count(Materialized.as(AGG_TEAM_STORE));

        //complete processing on aggregation
        kTable.toStream().filter((k,v) -> Objects.nonNull(v)).foreach(checkForSending);

        return builder.build();
    }

    /**
     * Function to check if an aggregated stream is in a complete state & ready for sending to the next Kafka topic
     */
    //TODO can this be changed to process()...
    public static ForeachAction<String, FootballTeamAggregation> checkForSending = (sKey, ftaValue) -> {
        //check cached value
        System.out.println("checking if can send or not with key: " + sKey + " value: " + ftaValue);
        int expectedCount = ftaValue.getExpectedCount();
        int actualCount = ftaValue.getTeamSet().size();
        System.out.println("expected count is: " + expectedCount + ", actual count is: " + actualCount);
        if (expectedCount == actualCount) {
            System.out.println("league expected team count == current league team size");
            System.out.println(" --- SEND - AGGREGATE COMPLETE ---");
            getAggregateProducer().send(sKey, ftaValue);
        } else {
            System.out.println("league expected team count != current league team size");
            System.out.println(" --- DONT SEND - WAITING FOR MORE MESSAGES ---");
        }
    };

    // get reference to Spring Bean from POJO
    private static KafkaProducerAggregate getAggregateProducer() {
        return SpringContext.getBean(KafkaProducerAggregate.class);
    }
}
