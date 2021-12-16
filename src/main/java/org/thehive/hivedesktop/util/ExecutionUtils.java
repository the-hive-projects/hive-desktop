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

    public static void runOnUiThread(@NonNull Runnable r) {
        Platform.runLater(r);
    }

    public static void scheduleOnUiThread(@NonNull Runnable r, long delay) {
        SCHEDULED_EXECUTOR_SERVICE.schedule(() -> runOnUiThread(r), delay, TimeUnit.MILLISECONDS);
    }

}
