package PPP1;

import java.net.*;
import java.io.*;

/**
 *
 * @author Bipin
 */

public class CastService extends Thread {

    MulticastSocket ms = null;
    InetAddress group = null;
    int port = 8888;

    String myid;
    String castMsg;

    public CastService(MulticastSocket ms, InetAddress group, int port, String myid, String castMsg) { //argument constructor

        this.ms = ms;
        this.group = group;
        this.port = port;
        this.myid = myid;
        this.castMsg = castMsg;

        this.start(); //start thread 
    } 

    public void run() {

        try {

            //multicast the message
            castMsg = new String(myid + ":" + castMsg);
            byte[] m = castMsg.getBytes();
            DatagramPacket messageOut = new DatagramPacket(m, m.length, group, port);
            ms.send(messageOut);
            //multicast the message

        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }

}
