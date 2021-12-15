package org.thehive.hivedesktop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.thehive.hivedesktop.scene.AppSceneManager;
import org.thehive.hivedesktop.scene.AppSceneManagerImpl;
import org.thehive.hiveserverclient.net.http.*;
import org.thehive.hiveserverclient.net.websocket.UrlEndpointResolver;
import org.thehive.hiveserverclient.net.websocket.UrlEndpointResolverImpl;
import org.thehive.hiveserverclient.net.websocket.WebSocketClient;
import org.thehive.hiveserverclient.net.websocket.WebSocketClientImpl;
import org.thehive.hiveserverclient.payload.ChatMessage;
import org.thehive.hiveserverclient.service.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Ctx {

    private static Ctx instance;

    public final AppSceneManager sceneManager;
    public final UserService userService;
    public final SessionService sessionService;
    public final ImageService imageService;
    public final WebSocketSingleConnService webSocketService;
    public final ExecutorService executorService;

    public Ctx() {
        this.sceneManager = new AppSceneManagerImpl();
        ObjectMapper objectMapper = new ObjectMapper();
        CloseableHttpClient httpClient = HttpClients.createSystem();
        this.executorService = Executors.newFixedThreadPool(Consts.THREAD_POOL_SIZE);
        UserClient userClient = new UserClientImpl(Consts.SERVER_USER_HTTP_URI, objectMapper, httpClient, executorService);
        this.userService = new UserServiceImpl(userClient);
        SessionClient sessionClient = new SessionClientImpl(Consts.SERVER_SESSION_HTTP_URI, objectMapper, httpClient, executorService);
        this.sessionService = new SessionServiceImpl(sessionClient);
        ImageClient imageClient = new ImageClientImpl(Consts.SERVER_IMAGE_HTTP_URI, objectMapper, httpClient, executorService);
        this.imageService = new ImageServiceImpl(imageClient);
        UrlEndpointResolver urlEndpointResolver = new UrlEndpointResolverImpl(Consts.SERVER_SESSION_WS_STOMP_SUBSCRIPTION_ENDPOINT, Consts.SERVER_WS_STOMP_DESTINATION_PREFIX);
        urlEndpointResolver.addDestinationUrlEndpoint(ChatMessage.class, Consts.SERVER_SESSION_WS_STOMP_CHAT_MESSAGE_PAYLOAD_ENDPOINT);
        var wsClient = new StandardWebSocketClient();
        var wsStompClient = new WebSocketStompClient(wsClient);
        wsStompClient.setMessageConverter(new MappingJackson2MessageConverter());
        WebSocketClient webSocketClient = new WebSocketClientImpl(Consts.SERVER_WS_STOMP_URI, urlEndpointResolver, wsStompClient, executorService);
        this.webSocketService = new WebSocketSingleConnServiceImpl(webSocketClient);
    }

    public static Ctx getInstance() {
        return instance;
    }

    public static void initialize() {
        if (instance == null) {
            instance = new Ctx();
            log.info("Context instance was initialized successfully");
        } else
            log.warn("Context instance has already been initialized");
    }

}
