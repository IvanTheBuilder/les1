package prac.ivanrobrecht;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Ivan on 27/09/2016.
 */
public class Client {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1366);
            DataOutputStream stream = new DataOutputStream(socket.getOutputStream());
            stream.writeUTF("Jo give me the shit");
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            System.out.println(inputStream.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
