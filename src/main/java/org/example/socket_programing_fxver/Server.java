package org.example.socket_programing_fxver;

import javax.imageio.IIOException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


// Server Class
public class Server {
    private final ServerSocket serverSocket;
    private final ExecutorService pool = Executors.newFixedThreadPool(8);
    private static final ArrayList<Massage> massageList = new ArrayList<>();
    static public final ArrayList<ClientHandler>  ClientHandlerList = new ArrayList<>();

    public Server(ServerSocket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
    }

    public static void broadcast(String massage, String sender){
        massageList.add(new Massage(massage ,sender));

        massage = sender + ": " + massage;
        System.out.println(massage);
        for (ClientHandler clientHandler : ClientHandlerList){
            try {
                if(!clientHandler.userName.equals(sender)){
                    clientHandler.bufferedWriter.write(massage);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }
            catch (IOException e){
            }
        }
    }
    public static void stat(String massage, String sender){

        massageList.add(new Massage(massage ,"SERVER"));

        massage = sender + ": " + massage;
        System.out.println(massage);

        for (ClientHandler clientHandler : ClientHandlerList){
            try {
                if(!clientHandler.userName.equals(sender)){
                    clientHandler.bufferedWriter.write(massage);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();

                }
            }
            catch (IOException e){
            }
        }
    }

    public void  startServer(){
        try {

            while(!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("a new member has been joined into group chat ");
                ClientHandler clientHandler = new ClientHandler(socket);
                pool.execute(clientHandler);
            }
        }
        catch (IIOException e){
            e.printStackTrace();

        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public void closeServerSocket(){
        try {
            if (serverSocket != null){
                serverSocket.close();
            }
        }
        catch (IIOException e){
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        try {

            ServerSocket serverSocket1 = new ServerSocket(4444);
            Server server = new Server(serverSocket1);
            server.startServer();


        }
        catch(IOException e){
            System.out.println("eror");
        }

    }

}