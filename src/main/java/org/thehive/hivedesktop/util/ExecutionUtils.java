package org.thehive.hivedesktop.util;

import javafx.application.Platform;
import lombok.NonNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecutionUtils {

    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE;

    static {
        SCHEDULED_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();
    }

    public static void runOnUi(@NonNull Runnable r) {
        Platform.runLater(r);
    }

    public static void scheduleOnUi(@NonNull Runnable r, long delay) {
        SCHEDULED_EXECUTOR_SERVICE.schedule(() -> runOnUi(r), delay, TimeUnit.MILLISECONDS);
    }

}
