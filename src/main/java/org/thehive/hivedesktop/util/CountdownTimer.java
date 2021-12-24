package org.thehive.hivedesktop.util;

import lombok.NonNull;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class CountdownTimer {

    private final Thread thread;
    private final AtomicLong endTimeMsRef;

    public CountdownTimer(long eachTimeMs, @NonNull Consumer<Long> consumerEach, Runnable callbackLast) {
        this.endTimeMsRef = new AtomicLong();
        this.thread = new Thread(() -> {
            while (true) {
                var endTimeMs = this.endTimeMsRef.get();
                var remTime = endTimeMs - System.currentTimeMillis();
                if (endTimeMs != 0L)
                    try {
                        if (remTime > 0)
                            if (callbackLast != null)
                                callbackLast.run();
                            else {
                                Thread.sleep(eachTimeMs);
                                consumerEach.accept(remTime);
                            }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        });
        thread.setName("CountDownTimer");
        thread.setDaemon(true);
        thread.start();
    }

    public void start(long endTimeMs) {
        endTimeMsRef.set(endTimeMs);
    }

    public void stop() {
        endTimeMsRef.set(0L);
    }

}
