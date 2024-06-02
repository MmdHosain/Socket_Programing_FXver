package org.example.socket_programing_fxver;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

import static org.example.socket_programing_fxver.Server.listOfFiles;

public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;


    String userName;


    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            this.userName = bufferedReader.readLine();
            Server.ClientHandlerList.add(this);

            Server.stat(" has Entered the chat ", userName);


        } catch (IOException e) {
            closeClientHandler(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override

    public void run() {
        String massage;
        while (socket.isConnected()) {
            try {
                massage = bufferedReader.readLine();


                switch (massage) {
                    case "/showFile":
                        showFiles();
                        break;
                    case "/downloadFile":
                        downloadFiles();
                        break;
                    default:
                        Server.broadcast(massage, userName);
                        break;
                }
            } catch (IOException e) {
                closeClientHandler(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void showFiles() {

        int counter = 0;
        for (File file : listOfFiles) {
            counter++;
            try {

                bufferedWriter.write(counter + "." + file.getName());
                bufferedWriter.newLine();
                bufferedWriter.flush();

            } catch (IOException ignored) {

            }
        }
    }

    public void downloadFiles() throws IOException {



        try {
            assert listOfFiles != null;

            bufferedWriter.write("/readFile");
            bufferedWriter.newLine();
            bufferedWriter.flush();

            String command;
            command = bufferedReader.readLine();
//            bufferedWriter.write("/sending");
//            bufferedWriter.newLine();
//            bufferedWriter.flush();


            File file = listOfFiles[Integer.parseInt(command) - 1];

            try {

                DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

                long length = file.length();
                byte[] bytes = new byte[(int) length];
                FileInputStream fileInputStream= new FileInputStream(file);
                fileInputStream.read(bytes);
                dataOut.write(bytes);                                          //*
                dataOut.flush();
                System.out.println("data has been sent");



            } catch (IOException e) {

            }
        }
     catch(IOException e){}

    }



    public void closeClientHandler(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){




        Server.ClientHandlerList.remove(userName);
        Server.stat(" has left the chat.",userName);
        try {
            bufferedReader.close();
            bufferedWriter.close();
            socket.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
