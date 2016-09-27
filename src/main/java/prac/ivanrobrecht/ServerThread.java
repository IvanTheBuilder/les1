package prac.ivanrobrecht;

import java.io.*;
import java.net.Socket;

/**
 * Created by Ivan on 27/09/2016.
 */
public class ServerThread extends Thread {

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public ServerThread(Socket socket) {
        this.socket = socket;
        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        // Vraag bestandsnaam op
        try {
            String bestandsnaam = dataInputStream.readUTF();
            File file = new File(bestandsnaam);
            if(file.exists()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] fileBytes = new byte[(int) file.length()];
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                dataOutputStream.write(fileBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
