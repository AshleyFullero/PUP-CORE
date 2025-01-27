module org.htech.universityproject {
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;
    requires mysql.connector.j;
    requires fontawesomefx;
    requires com.jfoenix;


    opens org.htech.universityproject.controllers to javafx.fxml;
    exports org.htech.universityproject.modal;
    exports org.htech.universityproject.controllers;
    opens org.htech.universityproject.start to javafx.fxml;
    exports org.htech.universityproject.start;
    exports org.htech.universityproject;
    opens org.htech.universityproject to javafx.fxml;
}