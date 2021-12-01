package org.thehive.hivedesktop.scene;

import java.util.Optional;

public abstract class AbstractScene implements AppScene {

    protected AppController controller;

    @Override
    public final Optional<AppController> getController() {
        return Optional.ofNullable(controller);
    }

    @Override
    public final void setController(AppController controller) {
        this.controller = controller;
    }

}
