package prac.ivanrobrecht.sessie2;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

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
        System.out.println("Checking for filename....");
        File file = new File(filename.toString());
        System.out.println(file.getAbsolutePath());
        if(file.getAbsoluteFile().exists()) {
            System.out.println("File was found ("+file.length()+") Generating hash...");
            byte[] digest;
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                FileInputStream fileInputStream = null;
                DigestInputStream dis = new DigestInputStream(new FileInputStream(file), md);
                digest = md.digest();
                socket.send(new DatagramPacket(digest, digest.length, ip, port));


            byte[] fileBytes = new byte[512];
                fileInputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            int count = 0;

            while ((count = bufferedInputStream.read(fileBytes)) != -1) {
                socket.send(new DatagramPacket(fileBytes, fileBytes.length, ip, port));
                System.out.println("Sent a packet... "+count);
            }
            System.out.println("Completed...");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        }
        else {
            System.out.println("file not found");
        }
    }

    public void write() {

    }

    public InetAddress getIp() {
        return ip;
    }

    public String getFilename() {
        return filename;
    }

    public String getHash() {
        return hash;
    }

    public boolean checkHash(String string) {
        return hash.equalsIgnoreCase(string);
    }

}
