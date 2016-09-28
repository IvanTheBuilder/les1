package prac.ivanrobrecht.sessie2;
import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.net.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;

/**
 * Created by Ivan on 28/09/2016.
 */
public class UDPClient {
    public static void main(String args[]) throws Exception
    {
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("192.168.0.2");
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];

        String sentence = "test.txt";
        sendData = sentence.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 10001);
        clientSocket.send(sendPacket);
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);

        String receivedSentence = new String(receivePacket.getData());
        String hash = receivedSentence;

        receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);

        receivedSentence = new String(receivePacket.getData());

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest;
        InputStream stream = new ByteArrayInputStream(receivedSentence.getBytes());
        DigestInputStream dis = new DigestInputStream(stream , md);
        digest = md.digest();
        if(digest.equals(hash))
            System.out.println("hash klopt");

        FileOutputStream fileOutputStream = new FileOutputStream("testUDP.txt");
        fileOutputStream.write(receivedSentence.getBytes());
        clientSocket.close();
    }
}
