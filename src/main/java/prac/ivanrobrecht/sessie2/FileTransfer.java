package prac.ivanrobrecht.sessie2;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Ivan on 28/09/2016.
 */
public class FileTransfer extends Thread {

    private InetAddress ip;
    private String filename;
    private String hash;
    private int port;
    private DatagramSocket socket;

    public FileTransfer(DatagramSocket socket, InetAddress ip, String filename, int port) {
        this.socket = socket;
        this.ip = ip;
        this.filename = filename;
        this.port = port;
    }

    public void run() {
        System.out.println("Starting filetransfer of "+filename+" to "+ip.toString());
        File file = new File(filename.toString());
        if(file.getAbsoluteFile().exists()) {
            System.out.println("File was found ("+file.length()+") Generating hash...");
            try {
                /**
                 * Calculate the hash and send the hash over to the client, who is expecting this packet.
                 */
                byte[] digest = getHash(file);
                System.out.println("Sending hash... ("+bytesToHex(digest)+")");
                socket.send(new DatagramPacket(digest, digest.length, ip, port));

                /**
                 * We convert the filesize into a bytebuffer and send this over to the client too, which is expecting
                 * the amount of bytes to receive.
                 */
                byte[] filesizeBytes = ByteBuffer.allocate(64).putLong((int) file.length()).array();
                System.out.println("Sending filesize in bytes... ("+file.length()+" bytes)");
                // Send the filesize to the client so he knows when the transfer is finished...
                socket.send(new DatagramPacket(filesizeBytes, filesizeBytes.length, ip, port));


                /**
                 * Start the actual filetransfer. Every time we read 512 bytes from the file and send it over to the
                 * client until we reach the end of file.
                 */
                System.out.println("Starting actual file transfer...");
                byte[] fileBytes = new byte[512];
                FileInputStream fileInputStream = new FileInputStream(file);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                int count = 0;
                while ((count = bufferedInputStream.read(fileBytes)) != -1) {
                    socket.send(new DatagramPacket(fileBytes, fileBytes.length, ip, port));
                    System.out.println("Sent a packet... ("+count+" bytes)");
                }
                System.out.println("Completed...");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    public boolean checkHash(String string) {
        return hash.equalsIgnoreCase(string);
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
