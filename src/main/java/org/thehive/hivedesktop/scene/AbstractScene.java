package org.thehive.hivedesktop.scene;

import lombok.NonNull;

public abstract class AbstractScene implements AppScene {

    protected final String name;

    protected AbstractScene(@NonNull String name) {
        this.name = name;
    }

    public final String getName() {
        return name;
    }

}
