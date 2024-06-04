package org.example.socket_programing_fxver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

// Client Class
public class Client extends Application {

    protected Socket socket;
    protected BufferedReader bufferedReader;
    protected BufferedWriter bufferedWriter;
    public static Client client;
    public HelloController controller ;
    private String userName;


    public Client() {
    }

    public Client(Socket socket) {

        this.socket = socket;
    }

    public void closeConnection(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            bufferedWriter.close();
            bufferedReader.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readFile() {

        try {
            File file = new File("D:\\AP\\Socket_Programing_FXver\\src\\main\\java\\org\\example\\socket_programing_fxver\\client_data\\downloaded_file.txt");


            FileOutputStream fileOutputStream = new FileOutputStream(file);
            DataInputStream dataIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            byte[] bytes = new byte[1024];
//                    String order = bufferedReader.readLine();
//                    if (order.equals("/sending")) {
            dataIn.read(bytes);
            fileOutputStream.write(bytes);
            System.out.println("downloaded file saved");
//                    }

        } catch (IOException e) {
            System.out.println("there was a problem in saving downloaded file");
        }


    }

    public void sendMassage() {
        new Thread((new Runnable() {
            @Override
            public void run() {
                try {

                    bufferedWriter.write(userName);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                    Scanner scanner = new Scanner(System.in);
                    while (socket.isConnected()) {
                        String massageToSend = scanner.nextLine();
                        bufferedWriter.write(massageToSend);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }

                } catch (IOException e) {
                    closeConnection(socket, bufferedReader, bufferedWriter);
                }

            }
        })).start();
    }

    public void sendMassage(String messageToSend) {
        try {
            bufferedWriter.write(messageToSend);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            controller.showMessage(messageToSend);

        } catch (IOException e) {
            closeConnection(socket, bufferedReader, bufferedWriter);
        }
    }





    public void receiveMassage() {
        new Thread((new Runnable() {
            @Override
            public void run() {
                String receivedMassage;
                while (socket.isConnected()) {
                    try {

                        receivedMassage = bufferedReader.readLine();
                        System.out.println(receivedMassage);

                        if (receivedMassage.equals("/readFile")) {
                            readFile();
                        } else {
                            controller.showMessage(receivedMassage);
                        }
                    } catch (IOException e) {
                        closeConnection(socket, bufferedReader, bufferedWriter);
                    }
                }
            }



        })).start();
    }

    private String getUsername() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username:");
        return scanner.nextLine();
    }


    public void initClient() {


        try {
            client.socket = new Socket("localhost", 4444);
            client.bufferedWriter = new BufferedWriter(new OutputStreamWriter(client.socket.getOutputStream()));
            client.bufferedReader = new BufferedReader(new InputStreamReader(client.socket.getInputStream()));
            client.userName = getUsername();


        } catch (IOException e) {
            client.closeConnection(socket, bufferedReader, bufferedWriter);
        }

        client.receiveMassage();
        client.sendMassage();
    }
    @Override
    public void start(Stage stage) throws IOException {
        try {



            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

            client = new Client();

            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root, 700, 700);
            stage.setResizable(false);
            stage.setTitle("Chat Application");
            stage.setScene(scene);
            stage.show();

            client.controller = fxmlLoader.getController();
            client.initClient();

        } catch (Exception e) {
            e.printStackTrace();

        }


}


    public static void main(String[] args) {

        launch();

    }

}
