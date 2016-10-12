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
            Socket socket = new Socket("192.168.1.3", 1366);
            //Vraag bestand op
            byte[] bytes = new byte[16*1024];
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("foto1.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream("foto3.jpg");
            int count;
            while ((count = socket.getInputStream().read(bytes)) > 0) {
                fileOutputStream.write(bytes, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
