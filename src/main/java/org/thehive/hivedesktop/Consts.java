package org.thehive.hivedesktop;

public class Consts {

    public static final String SERVER_HTTP_URI = "http://127.0.0.1:8080";
    public static final String SERVER_USER_HTTP_URI = SERVER_HTTP_URI + "/user";
    public static final String SERVER_SESSION_HTTP_URI = SERVER_HTTP_URI + "/session";
    public static final String SERVER_IMAGE_HTTP_URI = SERVER_HTTP_URI + "/image";

    public static final String SERVER_WS_URI = "ws://127.0.0.1:8080";
    public static final String SERVER_WS_STOMP_URI = SERVER_WS_URI + "/stomp";
    public static final String SERVER_WS_STOMP_DESTINATION_PREFIX = "/websocket";
    public static final String SERVER_SESSION_WS_STOMP_SUBSCRIPTION_ENDPOINT = "/topic/session/{id}";
    public static final String SERVER_SESSION_WS_STOMP_CHAT_PAYLOAD_ENDPOINT = "/session/chat/{id}";

    public static final int THREAD_POOL_SIZE = 10;

    public static final String SCENE_MESSAGE_DATA_KEY = "message";
    public static final String SCENE_SESSION_DATA_KEY = "session";

}
