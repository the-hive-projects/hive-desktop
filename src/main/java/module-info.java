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



    opens org.thehive.hivedesktop to javafx.fxml;
    exports org.thehive.hivedesktop;
}