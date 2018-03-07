package com.org.kobito.kobitomsg;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
@EnableKafka
@EnableKafkaStreams
public class KobitoMsgApplication {

	public static void main(String[] args) {
		SpringApplication.run(KobitoMsgApplication.class, args);
	}


	@Autowired
	private KafkaTemplate<String, String> template;

	@RequestMapping("/sendMsg")
	public String sendMsg() {
		for (int i = 0; i < 1000; i++) {
			ListenableFuture<SendResult<String, String>> resultListenableFuture = this.template.send("streamingTopic1","10001", "Increase Msg");
			//TODO - Handle response result with resultListenableFuture
		}

		System.out.println("All message send");
		System.out.println("---------------------------------------");
		return "OK";
	}

	@KafkaListener(topics = "myTopic")
	public void listen(ConsumerRecord<?, ?> cr) throws Exception {
		System.out.println("---------------------------------------");
		System.out.println(cr.toString());
	}

	////////////////////
	@Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
	public StreamsConfig kStreamsConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.99.100:9092");
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "testStreams");
		props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.Integer().getClass().getName());
		props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		props.put(StreamsConfig.TIMESTAMP_EXTRACTOR_CLASS_CONFIG,
				WallclockTimestampExtractor.class.getName());
		return new StreamsConfig(props);
	}

	@Bean
	public KStream<Integer, String> kStream(StreamsBuilder kStreamBuilder) {
		KStream<Integer, String> stream = kStreamBuilder.stream("streamingTopic1");
		stream
				.mapValues(String::toUpperCase)
				.groupByKey()
				.reduce((String value1, String value2) -> value1 + value2,
						TimeWindows.of(1000),
						"windowStore")
				.toStream()
				.map((windowedId, value) -> new KeyValue<>(windowedId.key(), value))
				.filter((i, s) -> s.length() > 40)
				.to("streamingTopic2");
		stream.print();
		return stream;
	}
}
