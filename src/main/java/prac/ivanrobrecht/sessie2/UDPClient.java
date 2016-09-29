package prac.ivanrobrecht.sessie2;

import java.io.*;
import java.net.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;

/**
 * Created by Robrecht on 28/09/2016.
 */
public class UDPClient {
    public static void main(String args[]) throws Exception
    {
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));   //input stream van de terminal
        DatagramSocket clientSocket = new DatagramSocket();                                 //server socket
        byte[] receiveData = new byte[1024];                                                //array waarin ontvangen data wordt opgeslagen
        byte[] hash = new byte[16];                                                         //array waarin de hashcode wordt opgeslagen

        System.out.print("geef het adres van de server: ");                                 //ip adres van de server opvragen
        InetAddress IPAddress =InetAddress.getByName(inFromUser.readLine());
        System.out.print("geef een bestandsnaam op: ");                                     //naam van het bestand dat moet verzonden worden
        byte[] sendData = inFromUser.readLine().getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 10001);    //nieuw pakket aanmaken met de bestandsnaam en adres van de server
        clientSocket.send(sendPacket);
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);             //antwoord van de server, in dit pakket zit de hashcode
        clientSocket.receive(receivePacket);
        System.arraycopy(receivePacket.getData(),0,hash,0,receivePacket.getLength());                   //opslagen van de hashcode in de 'hash' array

        clientSocket.receive(receivePacket);                                                            //eigenlijke bestand ontvangen
        String receivedSentence = new String(receivePacket.getData());

        MessageDigest md = MessageDigest.getInstance("MD5");                                            //hashcode op het bericht berekenen en checken
        byte[] digest;
        InputStream stream = new ByteArrayInputStream(receivedSentence.getBytes());
        DigestInputStream dis = new DigestInputStream(stream , md);
        digest = md.digest();
        boolean ongelijk = false;
        int i =0;
        while(i<16 && !ongelijk)
        {
            if(digest[i]!=hash[i]) {
                ongelijk = true;
                System.out.println("hash klopt niet");
            }
            i++;
        }
        if(i>=16 && !ongelijk)
            System.out.println("hash klopt");

        FileOutputStream fileOutputStream = new FileOutputStream("testUDP.txt");
        fileOutputStream.write(receivedSentence.getBytes());
        System.out.println("bestand is aangekomen");
        clientSocket.close();
    }
}
