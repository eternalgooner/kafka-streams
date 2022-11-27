# A sample Springboot Kafka Streams application 

## Introduction
This demo application uses Kafka Streams Aggregation. The aggregation objects used in the app are football teams and leagues.
A league will have an aggregate of football teams.

In this demo app the sample data used to initialize the DB table `league_agg` looks like this:  

| id          | name        | expected_count |
| ----------- | ----------- | -------------- |
| 1           | prem        | 8              |
| 2           | champ       | 5              |
| 3           | laliga      | 5              |

Each league has an expected count, which deems the aggregate as complete and ready for sending to the output topic.
So, as the kafka stream consumer receives new football teams in the stream, it will group the teams by their league name and then check
if the current team count for that league matches the expected team count from the DB. If it matches
then it will deem the aggregate complete and send it to the output topic.

## The App Contains
- Kafka Stream Producer to simulate a stream of messages arriving on a streams topic
- Kafka Stream Consumer to consume the stream of messages arriving on the streams topic
- Kafka Local State Store which is used to update & query the state of the aggregated stream
- Kafka Producer to send the final aggregated message to the next topic for processing
- API endpoint to get the current aggregate state for a given key (league)
  - Exposed at `http://localhost:8080/store/{league}`
  - Calling the endpoint before the consumer stream is running with that key (league) will throw an error

## Prerequisites
- MySQL running locally with a schema named `football`
- Kafka running locally - 2 topics are used in the app
  - the input topic `team-stream`
  - the output topic `final-team-aggregate`

## Running the app will
1. Initialise the DB with some pre-configured state required by the app to determine when an aggregate is deemed complete and ready to send to the output topic.
2. Start the Kafka Stream Producer which starts in a separate thread, sending messages onto the stream topic every 6 seconds.
This producer reads in a csv file with football team data.
3. Start the Kafka Streams Consumer which will then  
   (i) group the messages by key (league name)  
   (ii) aggregates the messages using the key  
   (iii) create the local state store, ready for queries & updates  
   (iv) checks if the aggregate is complete and ready to be sent to the output topic 
4. Two aggregated messages should appear on the output topic after the 1st producer has sent all the csv data (for the leagues `prem` and `champ` which both have their
expected count of teams met. The league `laliga` never receives its expected
count and thus no message is ever sent for that league)

Once the application is running you can test it working with a new key aggregation by:
- Manually insert another record into the DB table `league-agg` e.g `insert into league_agg values (4, "bundesliga", "3");`
- Manually send in 3 Kafka messages to the topic `team-stream` making sure to use the matching key for the record (league name) used in the previous SQL insert
- Observe the logs and watch as the first 2 messages are received and log that the expected count isn't met yet
- On the 3rd message received, it should trigger sending the aggregate message to the output topic


## Kafka Stream Concepts
- Topology
  - Each Stream has a Topology
  - A Topology describes what you want to do with the stream e.g. group, aggregate, filter, count etc.
- Changelog
  - Under the hood, Kafka aggregates the stream and stores it in a changelog (another topic)
- State Store
  - A State Store (local or global) can be used to store the state of a stream
  - it can also be used to query the present state of the store
- Stream Processor
  - The processor has its own API and can be used in the Streams API
  - In this app, the processor is used as the last step in the topology and processes the stream records
