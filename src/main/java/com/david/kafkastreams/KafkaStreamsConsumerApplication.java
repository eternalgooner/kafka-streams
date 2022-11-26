package com.david.kafkastreams;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication
public class KafkaStreamsConsumerApplication {

	@Autowired
	private StreamProcessingSimulation streamProcessingSimulation;

	public static void main(String[] args) {
		SpringApplication.run(KafkaStreamsConsumerApplication.class, args);
	}

	@PostConstruct
	private void startSimulation() throws IOException {
		streamProcessingSimulation.start();
	}
}
