package org.example.socket_programing_fxver;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private Socket socket;
    private BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;


    String userName ;


    public ClientHandler(Socket socket){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            this.userName = bufferedReader.readLine();
            Server.ClientHandlerList.add(this);

            Server.stat(" has Entered the chat " , userName);


        }
        catch (IOException e){
            closeClientHandler(socket,bufferedReader,bufferedWriter);
        }
    }
    @Override

    public void run(){
        String massage;
        while (socket.isConnected()){
            try
            {
                massage = bufferedReader.readLine();
                Server.broadcast(massage,userName);
            }
            catch (IOException e){
                closeClientHandler(socket,bufferedReader,bufferedWriter);
                break;
            }
        }
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
