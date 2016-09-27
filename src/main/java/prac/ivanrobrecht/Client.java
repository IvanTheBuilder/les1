package prac.ivanrobrecht;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Ivan on 27/09/2016.
 */
public class Client {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("ip", 1366);
            //Vraag bestand op
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("test.txt");
            FileOutputStream fileOutputStream = new FileOutputStream("test.txt");
            fileOutputStream.write(socket.getInputStream().read());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
