package org.thehive.hivedesktop.component;

import javafx.scene.Node;

public interface Component<T extends Node> {

    T getParentNode();

}
