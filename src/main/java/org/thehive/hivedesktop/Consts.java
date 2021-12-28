package org.thehive.hivedesktop;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.File;

public class Consts {

    public static final String SERVER_HTTP_URI = "http://172.23.18.112:8080";
    public static final String SERVER_USER_HTTP_URI = SERVER_HTTP_URI + "/user";
    public static final String SERVER_SESSION_HTTP_URI = SERVER_HTTP_URI + "/session";
    public static final String SERVER_IMAGE_HTTP_URI = SERVER_HTTP_URI + "/image";
    public static final String SERVER_SUBMISSION_HTTP_URI = SERVER_HTTP_URI + "/submission";

    public static final String SERVER_WS_URI = "ws://172.23.18.112:8080";
    public static final String SERVER_WS_STOMP_URI = SERVER_WS_URI + "/stomp";
    public static final String SERVER_WS_STOMP_DESTINATION_PREFIX = "/websocket";
    public static final String SERVER_SESSION_WS_STOMP_SUBSCRIPTION_ENDPOINT = "/user/queue/session";
    public static final String SERVER_SESSION_WS_STOMP_CHAT_MESSAGE_PAYLOAD_ENDPOINT = "/session/chat";
    public static final String SERVER_SESSION_WS_STOMP_CODE_RECEIVING_REQUEST_PAYLOAD_ENDPOINT = "/session/code/receive";
    public static final String SERVER_SESSION_WS_STOMP_CODE_BROADCASTING_INFORMATION_PAYLOAD_ENDPOINT = "/session/code/broadcast";

    public static final int THREAD_POOL_SIZE = 10;

    public static final String JOINED_SESSION_SCENE_DATA_KEY = "joined-session";
    public static final String JOINED_SESSION_LIVE_ID_SCENE_DATA_KEY = "joined-session-live-id";
    public static final String SIGNED_UP_USERNAME_SESSION_DATA_KEY = "signed-up-username";

    public static final Color INFO_LABEL_DEFAULT_COLOR = Color.BLUE;
    public static final Color INFO_LABEL_SUCCESS_COLOR = Color.GREEN;
    public static final Color INFO_LABEL_ERROR_COLOR = Color.RED;

    public static final long SCENE_DELAY_MILLIS = 4000L;

    public static final String EMPTY_STRING = "";

    public static final Image LOGO=new Image(new File("src/main/resources/img/logo2.png").toURI().toString());

}
