package prac.ivanrobrecht.sessie2;
import java.io.*;
import java.net.*;

/**
 * Created by Ivan on 28/09/2016.
 */
public class UDPClient {
    public static void main(String args[]) throws Exception
    {
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("localhost");
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];

        String sentence = inFromUser.readLine();
        sendData = sentence.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 1366);
        clientSocket.send(sendPacket);
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);

        String receivedSentence = new String(receivePacket.getData());
        FileOutputStream fileOutputStream = new FileOutputStream("testUDP.txt");
        fileOutputStream.write(receivedSentence.getBytes());
        clientSocket.close();
    }
}
