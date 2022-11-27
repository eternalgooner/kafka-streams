package com.david.kafkastreams.kafka.streams.processor;

import com.david.kafkastreams.SpringContext;
import com.david.kafkastreams.kafka.client.KafkaProducerAggregate;
import com.david.kafkastreams.model.FootballTeam;
import com.david.kafkastreams.model.FootballTeamAggregation;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueStore;

import static com.david.kafkastreams.kafka.streams.store.FootballTeamAggregationStore.STATE_STORE_NAME;

/**
 * Class that processes the stream of records, updates the aggregate and sends to output topic
 */
public class FootballTeamProcessor implements Processor<String, FootballTeam, Void, Void> {

    private KeyValueStore<String, FootballTeamAggregation> store;

    @Override
    public void init(ProcessorContext<Void, Void> context) {
        this.store = context.getStateStore(STATE_STORE_NAME);
    }

    @Override
    public void process(Record<String, FootballTeam> record) {
        System.out.println("in Processor process with record: " + record);
        String leagueKey = record.key();
        FootballTeam team = record.value();

        FootballTeamAggregation aggregation = store.get(leagueKey);
        //if agg is null it is probably the first message for the aggregation
        if(aggregation == null) {
            System.out.println("aggregation from store is null, create new agg & add to store");
            FootballTeamAggregation footballTeamAggregation = new FootballTeamAggregation();
            footballTeamAggregation.addTeamToAggregate(leagueKey, team);
            footballTeamAggregation.setName(leagueKey);
            System.out.println("updating state store, putting updated agg in");
            store.put(leagueKey, footballTeamAggregation);
            return;
        }
        System.out.println("FTA retrieved from state store: " + aggregation);

        int expectedCount = aggregation.getExpectedCount();
        System.out.println("state store expected count: " + expectedCount);
        int actualCount = aggregation.getTeamSet().size();
        System.out.println("state store actual count: " + actualCount);

        // check state store for current state
        // if it's the expected size then send what's in the store
        // The aggregation could be updated to have a boolean field like 'alreadySent' to stop sending again if duplicates arrive
        if (expectedCount == actualCount) {
            //send to topic
            System.out.println("counts are equal, send to output topic");
            getAggregateProducer().send(leagueKey, aggregation);
            return;
        }

        // if it's less, try adding the new record to the aggregation
        if (expectedCount > actualCount) {
            System.out.println("actual count is < expected count, add FootballTeam to aggregate");
            aggregation.addTeamToAggregate(leagueKey, team);
            System.out.println("updating state store, putting updated agg");
            store.put(leagueKey, aggregation);
        }

        // purely for debugging to see if this condition is hit
        if(expectedCount < actualCount) {
            System.out.println("actual count > less than expected count, do nothing...");
        }
    }

    // get reference to Spring Bean from POJO
    private static KafkaProducerAggregate getAggregateProducer() {
        return SpringContext.getBean(KafkaProducerAggregate.class);
    }
}
