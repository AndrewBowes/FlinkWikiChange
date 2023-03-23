package com.flutter.flunkwiki.job;

import com.flutter.flunkwiki.mapper.WikiChangeMapper;
import com.flutter.flunkwiki.model.WikiChangeDetail;
import com.flutter.flunkwiki.model.WikiChangeDetailSerializer;
import com.flutter.flunkwiki.source.WikiChangeSource;
import com.launchdarkly.eventsource.MessageEvent;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WikiChangeJob {

    private final Logger log = LoggerFactory.getLogger(WikiChangeJob.class.getSimpleName());

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<MessageEvent> messageEvents = env
                .addSource(new WikiChangeSource())
                .name("WikipediaChanges");

        DataStream<WikiChangeDetail> wikipediaChanges = messageEvents
                .map(new WikiChangeMapper())
                .filter((FilterFunction<WikiChangeDetail>) changeDetail
                        -> !changeDetail.isBot() && changeDetail.getType().equals("edit"))
                ;

        KafkaSink<WikiChangeDetail> sink = KafkaSink.<WikiChangeDetail>builder()
                .setBootstrapServers("localhost:19092")
                .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                        .setTopic("flutter-wikipedia-manualedits")
                        .setValueSerializationSchema(new WikiChangeDetailSerializer())
                        .build()
                )
                .setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                .build();

        wikipediaChanges.sinkTo(sink);

        env.execute("Wikipedia Manual Edit Detection");
    }
}
