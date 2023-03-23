package com.flutter.flunkwiki.model;

import org.apache.flink.api.common.serialization.SerializationSchema;

public class WikiChangeDetailSerializer implements SerializationSchema<WikiChangeDetail> {

    @Override
    public void open(InitializationContext context) throws Exception {
        SerializationSchema.super.open(context);
    }

    @Override
    public byte[] serialize(WikiChangeDetail element) {
        return element.toString().getBytes();
    }
}
