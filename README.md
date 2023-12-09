# Real-Time-Log-Data-Streaming-Using-Kafka

## Steps to run the application (on Windows):
### Download and Install Apache Kafka 3.6.0
### Download and install IntelliJ, where project/code has to be uploaded
### Start Zookeeper using: .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties (in a separate command prompt)
### Start Kafka Broker using: .\bin\windows\kafka-server-start.bat .\config\server.properties (in a separate command prompt)
### Create Kafka Topic using: .\bin\windows\kafka-topics.bat --create --topic log-data-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1 (in a separate command prompt)
### Run the Producer class from LogProducer and simultaneously the Consumer class from LogConsumer to get the desired output on console. Desired output being the ingestion rate and the confirmation of data being sent at the Producer end, and at the Consumer end the confirmation of the data recieved, latency and log data analysis (counts).
### Run the Producer class from Log Producer and simultaneously the project from LogDataStreaming to get the desired output on webpage. Desired output being the streaming of log data on web page and latency values on console. Note: Load the web page only after LogDataStreaming has finished its running (but not terminated). You can check the webpage at: localhost:8080
