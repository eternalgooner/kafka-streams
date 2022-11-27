package com.david.kafkastreams.kafka.streams.store;

import com.david.kafkastreams.model.FootballTeamAggregation;
import com.david.kafkastreams.serialization.FootballTeamAggregationSerDes;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;

public class FootballTeamAggregationStore {

    public static final String STATE_STORE_NAME = "fta-agg-store";

    public static StoreBuilder<KeyValueStore<String, FootballTeamAggregation>> getStore() {
        return Stores.keyValueStoreBuilder(
                    Stores.persistentKeyValueStore(STATE_STORE_NAME),
                    Serdes.String(),
                    new FootballTeamAggregationSerDes())
                .withCachingEnabled();
    }
}
