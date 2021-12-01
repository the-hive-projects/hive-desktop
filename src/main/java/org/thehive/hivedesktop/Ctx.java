package org.thehive.hivedesktop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.thehive.hivedesktop.scene.AppSceneManager;
import org.thehive.hivedesktop.scene.AppSceneManagerImpl;
import org.thehive.hiveserverclient.net.http.ImageClient;
import org.thehive.hiveserverclient.net.http.ImageClientImpl;
import org.thehive.hiveserverclient.net.http.UserClient;
import org.thehive.hiveserverclient.net.http.UserClientImpl;
import org.thehive.hiveserverclient.service.ImageService;
import org.thehive.hiveserverclient.service.ImageServiceImpl;
import org.thehive.hiveserverclient.service.UserService;
import org.thehive.hiveserverclient.service.UserServiceImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Ctx {

    private static Ctx instance;

    public final AppSceneManager sceneManager;
    public final UserService userService;
    public final ImageService imageService;

    public Ctx() {
        this.sceneManager = new AppSceneManagerImpl();
        ObjectMapper objectMapper = new ObjectMapper();
        CloseableHttpClient httpClient = HttpClients.createSystem();
        ExecutorService executorService = Executors.newFixedThreadPool(Consts.THREAD_POOL_SIZE);
        UserClient userClient = new UserClientImpl(Consts.SERVER_USER_URI, objectMapper, httpClient, executorService);
        this.userService = new UserServiceImpl(userClient);
        ImageClient imageClient = new ImageClientImpl(Consts.SERVER_IMAGE_URI, objectMapper, httpClient, executorService);
        this.imageService = new ImageServiceImpl(imageClient);
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
