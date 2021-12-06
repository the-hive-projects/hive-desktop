package org.thehive.hivedesktop;

import javafx.scene.paint.Color;

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

    public static final String JOINED_SESSION_SCENE_DATA_KEY = "joined-session";
    public static final String SIGNED_UP_USERNAME_SESSION_DATA_KEY = "signed-up-username";

    public static final Color INFO_LABEL_DEFAULT_COLOR = Color.BLUE;
    public static final Color INFO_LABEL_SUCCESS_COLOR = Color.GREEN;
    public static final Color INFO_LABEL_ERROR_COLOR = Color.RED;

    public static final long INFO_DELAY_MILLIS = 1000L;

    public static final String EMPTY_STRING = "";

}
