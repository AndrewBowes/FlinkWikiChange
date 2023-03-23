package com.flutter.flunkwiki.mapper;

import com.flutter.flunkwiki.model.WikiChangeDetail;
import com.launchdarkly.eventsource.MessageEvent;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WikiChangeMapper implements MapFunction<MessageEvent, WikiChangeDetail> {

    private final Logger log = LoggerFactory.getLogger(WikiChangeMapper.class.getSimpleName());

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public WikiChangeDetail map(MessageEvent messageEvent) throws Exception {
        JsonNode jsonNode = OBJECT_MAPPER.readTree(messageEvent.getData());
        int id = jsonNode.hasNonNull("id") ? jsonNode.get("id").asInt() : -1;
        String type = jsonNode.hasNonNull("type") ? jsonNode.get("type").asText() : "<Unknown>";
        String user = jsonNode.hasNonNull("user") ? jsonNode.get("user").asText() : "<Unknown>";
        boolean bot = jsonNode.hasNonNull("bot") ? jsonNode.get("bot").asBoolean() : false;
        String serverName = jsonNode.hasNonNull("server_name") ? jsonNode.get("server_name").asText() : "<Unknown>";
        long timestamp = jsonNode.get("timestamp").asLong();
        return new WikiChangeDetail(id, type, user, bot, serverName, timestamp);
    }

}
