package org.example.socket_programing_fxver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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
public class Client extends Application{
    protected Socket socket;
    protected BufferedReader bufferedReader;
    protected BufferedWriter bufferedWriter;
    public static Client client;

    private String userName ;



    public Client(){
    }

    public Client(Socket socket) {

        this.socket = socket;
    }

    public void closeConnection(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
        try {
            bufferedWriter.close();
            bufferedReader.close();
            socket.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void readFile(){

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

                }catch (IOException e){
                    System.out.println("there was a problem in saving downloaded file");
                }


    }
public void sendMassage(){
    new Thread((new Runnable() {
        @Override
        public void run() {
            try {

                bufferedWriter.write(userName);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                Scanner scanner = new Scanner( System.in);
                while (socket.isConnected()){
                    String massageToSend = scanner.nextLine();
                    bufferedWriter.write(massageToSend);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }

            }
            catch (IOException e){
                closeConnection(socket, bufferedReader,bufferedWriter);
            }

        }
    })).start();
}
    public void sendMassage(String messageToSend){
        try {

            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();



            bufferedWriter.write(messageToSend);
            bufferedWriter.newLine();
            bufferedWriter.flush();


        }
        catch (IOException e){
            closeConnection(socket, bufferedReader,bufferedWriter);
        }
    }


    @FXML
    public Text txt1;
    @FXML
    public Text txt2;
    @FXML
    public Text txt3;
    @FXML
    public Button sendButton;
    @FXML
    public TextField txtBar;

    @FXML
    protected void sendMessage() {
        client.sendMassage(txtBar.getText());
    }



public void receiveMassage(){
        new Thread((new Runnable() {
            @Override
            public void run() {
                String receivedMassage;
                while (socket.isConnected()){
                    try {
                        receivedMassage = bufferedReader.readLine();
                        System.out.println(receivedMassage);

                        if(receivedMassage.equals("/readFile")){
                            readFile();
                        }
                    }
                    catch (IOException e){
                        closeConnection(socket,bufferedReader,bufferedWriter);
                    }
                }
            }
        })).start();
}

    @Override
    public void start(Stage stage) throws IOException {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter your username:");
            String userNameInput = scanner.nextLine();


            client = new Client();


            client.socket = new Socket("localhost", 4444);

            try {
                client.bufferedWriter = new BufferedWriter(new OutputStreamWriter(client.socket.getOutputStream()));
                client.bufferedReader = new BufferedReader(new InputStreamReader(client.socket.getInputStream()));

                client.userName = userNameInput;
            } catch (IOException e) {
                client.closeConnection(socket, bufferedReader, bufferedWriter);
            }
            client.receiveMassage();
            client.sendMassage();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 500);
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();



        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
    public static void main(String[] args) {

        launch();

    }
}