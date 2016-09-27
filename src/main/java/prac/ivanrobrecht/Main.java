package prac.ivanrobrecht;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by Ivan on 27/09/2016.
 */
public class Main {

    private static ServerSocket socket;


    public static void main(String[] args) {
        try {
            socket = new ServerSocket(1366);
            DataInputStream stream = new DataInputStream(socket.accept().getInputStream());

            while(true) {
                System.out.println(stream.readUTF());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
