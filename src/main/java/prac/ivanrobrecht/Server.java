package prac.ivanrobrecht;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Ivan on 27/09/2016.
 */
public class Server {

    private static ServerSocket serversocket;


    public static void main(String[] args) {
        try {
            serversocket = new ServerSocket(1366);
            Socket clientsocket;
            while(true) {
                clientsocket = serversocket.accept();
                ServerThread serverThread = new ServerThread(clientsocket);
                serverThread.start();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
