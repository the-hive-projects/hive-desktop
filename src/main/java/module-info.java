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




    opens org.thehive.hivedesktop to javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires org.apache.httpcomponents.httpcore;
    exports org.thehive.hivedesktop;
}