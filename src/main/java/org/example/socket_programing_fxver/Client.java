package org.example.socket_programing_fxver;


import java.io.*;
import java.net.Socket;
import java.util.Scanner;

// Client Class
public class Client {
    protected final Socket socket;
    protected BufferedReader bufferedReader;
    protected BufferedWriter bufferedWriter;

    private String userName ;



    public Client(Socket socket,String userName){
        try {

            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            this.userName = userName;
        }catch(IOException e){
                closeConnection(socket,bufferedReader,bufferedWriter);
        }
        this.socket = socket;

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
public void sendMassage(){
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
public void receiveMassage(){
        new Thread((new Runnable() {
            @Override
            public void run() {
                String receivedMassage;
                while (socket.isConnected()){
                    try {
                        receivedMassage = bufferedReader.readLine();
                        System.out.println(receivedMassage);
                    }
                    catch (IOException e){
                        closeConnection(socket,bufferedReader,bufferedWriter);
                    }
                }
            }
        })).start();
}
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username:");
        String userNameInput = scanner.nextLine();

        try {
            Socket socket1 = new Socket("localhost",4444);
            Client client = new Client(socket1,userNameInput);
            client.receiveMassage();
            client.sendMassage();

        }
        catch (IOException e){}

    }
}