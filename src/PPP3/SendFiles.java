/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PPP3;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JTextArea;

/**
 *
 * @author Bipin
 */
public class SendFiles extends Thread {

    MulticastSocket ms = null;
    InetAddress group = null;
    int port = 8888;
    String myid = null;

    int fileport = 0;
    String userRequestFileName;
    JTextArea txtAreaAnswer;
    String Peer;

    public SendFiles(MulticastSocket ms, InetAddress group, int port, String myid, String Peer, int fileport, String userRequest, JTextArea txtAreaAnswer) { //argument constructor

        this.ms = ms;
        this.group = group;
        this.port = port;
        this.myid = myid;

        this.fileport = fileport;
        this.userRequestFileName = userRequest;
        this.txtAreaAnswer = txtAreaAnswer; //saved JTextArea in local
        this.Peer = Peer;
        this.start(); //start thread

    }

    public void run() {

        String computeTaskClass = null;

        computeTaskClass = "C:\\Users\\skkor\\Documents\\NetBeansProjects\\COIT20257_12122945_Assignment2\\src\\PPP3\\SharingFiles\\" + userRequestFileName;

        InetAddress ServerName = null;
        int ServerPort = 0;
        Socket s = null;
        OutputStream out = null;
        InputStream in = null;
        try {
            ServerName = InetAddress.getByName("localhost");
            ServerPort = 6789;
            s = new Socket(ServerName, ServerPort);
            out = s.getOutputStream();
            // Retrieve the compute class name
            String ClassName = computeTaskClass;
            // Read the class file into a byte array
            File ClassFile = new File(ClassName);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(ClassFile));
            DataInputStream dis = new DataInputStream(bis);
            byte[] mybytearray = new byte[(int) ClassFile.length()];
            dis.readFully(mybytearray, 0, mybytearray.length);
            // Use a data output stream to send the class file
            DataOutputStream dos = new DataOutputStream(out);
            // Send the class file name
            dos.writeUTF(ClassName);
            // Send the class file length
            dos.writeInt(mybytearray.length);
            // Send the class file
            dos.write(mybytearray, 0, mybytearray.length);
            dos.flush();
            // Report the the transfer state
            String displayMsg = "Uploading " + computeTaskClass + " is done.";
            System.out.println(displayMsg);

        } catch (UnknownHostException uhe) {
            System.out.println("UnknowHost:" + uhe.getMessage());

        } catch (IOException e) {
            System.out.println("IO errors:" + e.getMessage());
            //txtAreaAnswer.append("\n" + Peer + ": The requested file: '" + userRequestFileName + "' is not found on this PEER3!!!");
            //cast no file found msg
            new CastService(ms, group, port, myid, "cannot be found"); //start multicasting thread
        }
    }
}
