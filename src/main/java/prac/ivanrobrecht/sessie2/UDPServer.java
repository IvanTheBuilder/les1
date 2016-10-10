package prac.ivanrobrecht.sessie2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by Ivan on 28/09/2016.
 */
public class UDPServer {

    public static void main(String[] args) {
        try {
            DatagramSocket datagramSocket = new DatagramSocket(10001);
            byte[] receiveData = new byte[1024];
            System.out.println("Starting server...");
            while(true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                datagramSocket.receive(receivePacket);      //wachten tot er een pakket binnenkomt
                String filename = new String( receivePacket.getData()).substring(0, receivePacket.getLength());
                System.out.println(filename+" size: "+receivePacket.getLength());
                new FileTransfer(datagramSocket, receivePacket.getAddress(),filename, receivePacket.getPort()).start();
            }

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }


}
