package com.david.kafkastreams.kafka.streams.topology;

import com.david.kafkastreams.kafka.streams.processor.FootballProcessorSupplier;
import com.david.kafkastreams.kafka.streams.store.FootballTeamAggregationStore;
import com.david.kafkastreams.model.FootballTeam;
import com.david.kafkastreams.model.FootballTeamAggregation;
import com.david.kafkastreams.serialization.FootballTeamAggregationSerDes;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;

import static com.david.kafkastreams.config.KafkaConsumerStreamProperties.TEAM_STREAM_TOPIC;
import static com.david.kafkastreams.kafka.streams.store.FootballTeamAggregationStore.STATE_STORE_NAME;

/**
 * Describe the desired Kafka Stream Topology here
 * e.g. this Stream should stream from topic x and do
 * step 1 - group by key
 * step 2 - aggregate
 * step 3 - materialize state store for querying
 * step 4 - send record for processing
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

        //add state store
        builder.addStateStore(FootballTeamAggregationStore.getStore());

        KStream<String, FootballTeam> footyTeamKStream = builder.stream(TEAM_STREAM_TOPIC);
        KGroupedStream<String, FootballTeam> groupedStream = footyTeamKStream.groupByKey();

        // aggregate the stream & materialize the state store
        groupedStream.aggregate(FootballTeamAggregation::new,
            (key, value, agg) -> agg.addTeamToAggregate(key, value),
            Materialized.with(Serdes.String(), new FootballTeamAggregationSerDes()));

        //complete processing of FootballTeam stream with the state store
        footyTeamKStream.process(new FootballProcessorSupplier(), STATE_STORE_NAME);

        return builder.build();
    }
}
