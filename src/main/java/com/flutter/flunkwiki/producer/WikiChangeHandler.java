package com.flutter.flunkwiki.producer;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WikiChangeHandler implements EventHandler {

    private final Logger log = LoggerFactory.getLogger(WikiChangeHandler.class.getSimpleName());

    private BlockingQueue<MessageEvent> messageQueue = new ArrayBlockingQueue<>(1000);

    @Override
    public void onOpen() throws Exception {
        log.info("Opening Wikipedia Change Listener");
    }

    @Override
    public void onClosed() throws Exception {
        messageQueue.clear();
        log.info("Closing Wikipedia Change Listener");
    }

    @Override
    public void onMessage(String event, MessageEvent messageEvent)  {
        try {
            messageQueue.offer(messageEvent,100, TimeUnit.MILLISECONDS);
            log.debug("Message added to Event Queue");
        } catch (InterruptedException e) {
            log.error("Failed to add message to Event Queue. Event will be ignored.", e);
        }
    }

    @Override
    public void onComment(String comment) throws Exception {
        log.debug("Ignoring Comment Event");
    }

    @Override
    public void onError(Throwable t) {
        log.error("Error Handling Event", t);
    }

    public MessageEvent nextMessage() throws InterruptedException {
        return messageQueue.poll(1, TimeUnit.SECONDS);
    }
}
