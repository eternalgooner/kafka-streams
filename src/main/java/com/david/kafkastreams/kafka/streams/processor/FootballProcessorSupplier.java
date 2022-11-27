package com.david.kafkastreams.kafka.streams.processor;

import com.david.kafkastreams.model.FootballTeam;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorSupplier;

public class FootballProcessorSupplier implements ProcessorSupplier<String, FootballTeam, Void, Void> {
    @Override
    public Processor<String, FootballTeam, Void, Void> get() {
        return new FootballTeamProcessor();
    }
}
