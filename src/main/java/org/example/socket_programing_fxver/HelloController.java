package org.example.socket_programing_fxver;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.swing.*;

import java.util.ArrayList;

import static org.example.socket_programing_fxver.Client.client;


public class HelloController {



    @FXML
    public Button sendButton;
    @FXML
    public TextField txtBar;
    @FXML
    ArrayList<Label> labels = new ArrayList<>();
    public VBox chatHistory;

    public ObservableList<String> messages = FXCollections. observableArrayList();

    @FXML
    protected void sendMessage() {
        client.sendMassage(txtBar.getText());
        txtBar.clear();
    }
    @FXML
    public void showMessage(String newMessage) {
        Platform.runLater(() -> {

            messages.add(newMessage);
            labels.add(new Label(newMessage));
            chatHistory.getChildren().add(labels.getLast());

        });
    }


    @FXML

    public void initialize() {

        System.out.println("controller initialized");
        txtBar.setOnKeyPressed(event ->{
            if (event.getCode() == KeyCode.ENTER){
                sendMessage();
            }
        });

//        for (int i = 0; i < 20; i++) {
//            labels.add(new Label("Label " + i));
//            chatHistory.getChildren().add(labels.get(i));
//        }
    }
}