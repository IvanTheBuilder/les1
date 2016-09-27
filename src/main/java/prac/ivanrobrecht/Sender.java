package prac.ivanrobrecht;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Ivan on 27/09/2016.
 */
public class Sender {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1366);
            DataOutputStream stream = new DataOutputStream(socket.getOutputStream());
            stream.writeUTF("Jow what's up");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
