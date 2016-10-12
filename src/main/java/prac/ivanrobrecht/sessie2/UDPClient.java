package prac.ivanrobrecht.sessie2;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by Ivan on 28/09/2016.
 */
public class UDPClient {
    public static void main(String args[]) throws Exception
    {
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("192.168.1.2");





        /**
         * We send over the filename of the requested file.
         */
        System.out.println("filename?\n");
        String filename = inFromUser.readLine();
        byte[] fileNameBytes = filename.getBytes();
        System.out.println("Sending filename: "+filename);
        DatagramPacket sendPacket = new DatagramPacket(fileNameBytes, fileNameBytes.length, IPAddress, 10001);
        clientSocket.send(sendPacket);

        /**
         * Wait until we receive the hash of the file. We can use this hash to check if the file-transfer was
         * successful.
         */
        byte[] hash = new byte[16];
        DatagramPacket receivePacket = new DatagramPacket(hash, hash.length);
        clientSocket.receive(receivePacket);
        System.out.println("Hash received ("+bytesToHex(hash)+").");

        /**
         * Wait until the server sends the filesize. We need to know the filesize so we know when to stop waiting for
         * new packets and so we can prevent null bytes from being sent at the end of the transfer.
         */
        byte[] fileLengthBytes = new byte[64];
        receivePacket = new DatagramPacket(fileLengthBytes, hash.length);
        clientSocket.receive(receivePacket);
        long fileLength = ByteBuffer.wrap(fileLengthBytes).getLong();
        System.out.println("Filelength received, expecting "+fileLength+" bytes...");

        /**
         * We open a FileOutputStream and we start receiving bytes. We keep track of the received bytes so we know when
         * to stop waiting for packets and when to stop writing to the file.
         */
        long bytesRemaining = fileLength;
        FileOutputStream fileOutputStream = new FileOutputStream("foto2.jpg");
        byte[] receiveData;

        while(bytesRemaining > 0) {
            System.out.println("Bytes remaining: "+bytesRemaining);

            /**
             * If we are expecting less than 512 bytes of data, shrink the array so we don't end up writing empty bytes
             * to a file.
             */
            if(bytesRemaining < 512) {
                receiveData = new byte[(int) bytesRemaining];
            } else {
                receiveData = new byte[512];
            }

            /**
             * Receive the data and write the received bytes to the new file.
             */

            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            fileOutputStream.write(receiveData);

            /**
             * Subtract the remaining bytes so we know how many we still have to receive.
             */
            bytesRemaining -= receiveData.length;
        }
        System.out.println("File transfer completed...");

        /**
         * We check if the hash matches. We notify the user if they do or not.
         */
        if(Arrays.equals(hash, getHash(new File("foto2.jpg")))) {
            System.out.println("The hash of the file matches received hash. File transfer successful!");
        } else {
            System.out.println("Hashes don't match!! Don't trust the file.");
        }
        clientSocket.close();
    }


    public static byte[] getHash(File file) {
        byte[] digest;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            DigestInputStream dis = new DigestInputStream(new FileInputStream(file), md);
            try {
                while(dis.read() != -1) {}
                digest = md.digest();
                return digest;
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * http://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
     */
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


}
