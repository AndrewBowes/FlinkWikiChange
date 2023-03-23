package com.flutter.flunkwiki.producer;

import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.MessageEvent;

import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WikiChangeProducer {

    private final Logger log = LoggerFactory.getLogger(WikiChangeProducer.class.getSimpleName());

    private final WikiChangeHandler eventHandler = new WikiChangeHandler();
    private final EventSource eventSource;

    public WikiChangeProducer(){
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
        EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(url));
        eventSource = builder.build();

        // start the producer in another thread
        eventSource.start();
    }

    public MessageEvent nextMesssage() {
        try {
            return eventHandler.nextMessage();
        } catch (InterruptedException e){
            log.debug("No waiting Messages");
            return  null;
        }
    }
}
