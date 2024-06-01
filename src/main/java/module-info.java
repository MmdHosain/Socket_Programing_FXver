module org.example.socket_programing_fxver {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.socket_programing_fxver to javafx.fxml;
    exports org.example.socket_programing_fxver;
}