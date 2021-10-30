module org.thehive.hivedesktop {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.thehive.hivedesktop to javafx.fxml;
    exports org.thehive.hivedesktop;
}