module org.thehive.hivedesktop {

    requires hive.server.client;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;
    requires java.datatransfer;
    requires java.desktop;
    requires java.compiler;
    requires MaterialFX;
    requires org.apache.httpcomponents.httpclient;
    requires terminalfx;
    requires pty4j;
    requires com.fasterxml.jackson.databind;
    requires javafx.web;
    requires jdk.jsobject;
    requires org.apache.httpcomponents.httpcore;
    requires com.jfoenix;
    requires org.slf4j;
    requires eu.mihosoft.monacofx;
    requires lombok;
    requires spring.websocket;
    requires spring.messaging;

    opens org.thehive.hivedesktop to javafx.fxml;
    opens org.thehive.hivedesktop.scene to javafx.fxml;
    opens org.thehive.hivedesktop.controllers to javafx.fxml;

    exports org.thehive.hivedesktop to javafx.fxml, javafx.graphics;
    exports org.thehive.hivedesktop.scene to javafx.fxml, javafx.graphics;
    exports org.thehive.hivedesktop.controllers to javafx.fxml, javafx.graphics;

}