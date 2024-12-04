module com.example.recommendationsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;
    requires java.sql;
    requires org.json;
    requires jdk.jfr;
    requires stanford.corenlp;


    opens com.example.recommendationsystem to javafx.fxml;
    exports com.example.recommendationsystem;
    exports Controllers;
    opens Controllers to javafx.fxml;
}