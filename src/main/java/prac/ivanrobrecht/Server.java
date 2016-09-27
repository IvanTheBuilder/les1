package prac.ivanrobrecht;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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
            Socket clientsocket = serversocket.accept();
            DataInputStream stream = new DataInputStream(clientsocket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(clientsocket.getOutputStream());

            while(true) {
                System.out.println(stream.readUTF());
                dataOutputStream.writeUTF("Hier is uwen file");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
