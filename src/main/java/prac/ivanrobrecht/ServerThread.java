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
        System.out.println("Accepted new client...");
        // Vraag bestandsnaam op
        try {
            String bestandsnaam = dataInputStream.readUTF();
            System.out.println("Client requested "+bestandsnaam);
            File file = new File(bestandsnaam);
            if(file.exists()) {
                System.out.println("File was found ("+file.length()+")");
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] fileBytes = new byte[(int) file.length()];
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                int count = 0;

                while ((count = bufferedInputStream.read(fileBytes)) != -1) {
                    dataOutputStream.write(fileBytes, 0, count);
                    System.out.println("Sending some bytes... "+count);
                }
                System.out.println(fileBytes);
                System.out.println("Completed, closing connection...");
                dataOutputStream.close();
                dataInputStream.close();
                socket.close();
                System.out.println("Socket has terminated");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
