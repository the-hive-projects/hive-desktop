package org.thehive.hivedesktop.util;

import lombok.NonNull;
import org.slf4j.Logger;
import org.thehive.hiveserverclient.net.websocket.WebSocketConnection;
import org.thehive.hiveserverclient.net.websocket.WebSocketListener;
import org.thehive.hiveserverclient.net.websocket.header.AppStompHeaders;
import org.thehive.hiveserverclient.net.websocket.subscription.StompSubscription;
import org.thehive.hiveserverclient.payload.Payload;

public class WebSocketLoggingListener implements WebSocketListener {

    private final Logger logger;

    public WebSocketLoggingListener(@NonNull Logger logger) {
        this.logger = logger;
    }

    @Override
    public void onConnect(WebSocketConnection webSocketConnection) {
        logger.info("#onConnect");
    }

    @Override
    public void onSubscribe(StompSubscription stompSubscription) {
        logger.info("#onSubscribe");
    }

    @Override
    public void onUnsubscribe(StompSubscription stompSubscription) {
        logger.info("#onUnsubscribe");
    }

    @Override
    public void onReceive(AppStompHeaders appStompHeaders, Payload payload) {
        logger.info("#onReceive");
    }

    @Override
    public void onSend(Payload payload) {
        logger.info("#onSend");
    }

    @Override
    public void onException(Throwable throwable) {
        logger.info("#onExcepiton");
    }

    @Override
    public void onDisconnect(WebSocketConnection webSocketConnection) {
        logger.info("#onDisconnect");
    }

}
