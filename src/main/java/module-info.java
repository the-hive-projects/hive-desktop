module org.thehive.hivedesktop {
    requires javafx.controls;
    requires javafx.fxml;

    requires javafx.graphics;
    requires javafx.base;

    requires java.sql;
    requires java.datatransfer;
    requires java.desktop;
    requires java.compiler;
    requires MaterialFX;
    requires hiveserverclient;
    requires org.apache.httpcomponents.httpclient;
    requires terminalfx;
    requires pty4j;
    requires org.apache.commons.lang3;
    requires com.fasterxml.jackson.databind;
    requires javafx.web;
    requires jdk.jsobject;
    requires org.apache.httpcomponents.httpcore;
    requires com.jfoenix;

    exports org.thehive.hivedesktop to javafx.graphics, javafx.fxml;
    opens org.thehive.hivedesktop to  javafx.fxml;


}