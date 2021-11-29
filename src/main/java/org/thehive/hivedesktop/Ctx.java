package org.thehive.hivedesktop;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.thehive.hivedesktop.scene.AppSceneManager;
import org.thehive.hivedesktop.scene.AppSceneManagerImpl;
import org.thehive.hiveserverclient.net.http.UserClient;
import org.thehive.hiveserverclient.net.http.UserClientImpl;
import org.thehive.hiveserverclient.service.UserService;
import org.thehive.hiveserverclient.service.UserServiceImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Ctx {

    private static Ctx instance;

    public final AppSceneManager sceneManager;
    public final UserService userService;

    public Ctx() {
        this.sceneManager = new AppSceneManagerImpl();
        ObjectMapper objectMapper=new ObjectMapper();
        CloseableHttpClient httpClient= HttpClients.createSystem();
        ExecutorService executorService= Executors.newFixedThreadPool(10);
        UserClient userClient=new UserClientImpl("http://localhost:8080/user",objectMapper,httpClient,executorService);
        this.userService=new UserServiceImpl(userClient);
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
