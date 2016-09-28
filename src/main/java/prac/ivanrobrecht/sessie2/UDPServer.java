package prac.ivanrobrecht.sessie2;

import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Ivan on 28/09/2016.
 */
public class UDPServer {


    public static void main(String[] args) {
        try {
            DatagramSocket datagramSocket = new DatagramSocket(1366);
            while(true) {

            }

        } catch (SocketException e) {
            e.printStackTrace();
        }
    }


}
