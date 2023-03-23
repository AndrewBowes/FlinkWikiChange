package com.flutter.flunkwiki.source;

import com.flutter.flunkwiki.producer.WikiChangeProducer;
import com.launchdarkly.eventsource.MessageEvent;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

public class WikiChangeSource implements SourceFunction<MessageEvent> {

    private boolean isRunning = true;

    @Override
    public void run(SourceContext<MessageEvent> ctx) throws Exception {
        WikiChangeProducer wikiChangeProducer = new WikiChangeProducer();
        while (isRunning){
            MessageEvent message = wikiChangeProducer.nextMesssage();
            if (message != null){
                ctx.collect(message);
            }
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}
