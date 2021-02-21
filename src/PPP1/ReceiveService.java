package PPP1;

import PPP1.*;
import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.JTextArea;

/**
 *
 * @author Bipin
 */
public class ReceiveService extends Thread {

    private static String msgDisplay;
    MulticastSocket ms = null;
    InetAddress group = null;
    int port = 8888;

    String myid = null;
    JTextArea txtAreaAnswer;
    String userSelection;

    public ReceiveService(MulticastSocket ms, InetAddress group, int port, String myid, JTextArea txtAreaAnswer, String userSelection) {  //argument constructor

        this.ms = ms;
        this.group = group;
        this.port = port;
        this.myid = myid;
        this.txtAreaAnswer = txtAreaAnswer; //saved JTextArea in local
        this.userSelection = userSelection;

        this.start(); //start Thread

    }

    public void run() {

        while (true) {

            BufferedReader reader = null;
            try {

                //Start- receive the multicast
                byte[] buffer = new byte[50];
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                ms.receive(messageIn); //msg received
                String msg = new String(messageIn.getData(), 0, messageIn.getLength());

                String[] sArray = msg.split(":");; //split the receiveed msg

                if (sArray[0].equals("PPP3")) { //check first split is equals to currrent peerID
                    ///file transfer
                    //6779 = PPP1 reception port
                    // sArray[1] = user request
                    if (!sArray[1].equals("cannot be found")) {
                        System.out.println("Receied msg from PPP3");
                        new SendFiles(ms, group, port, myid, sArray[0], 6799, sArray[1], txtAreaAnswer);
                    }
                }

                if (sArray[0].equals("PPP2")) { //check first split is equals to currrent peerID
                    ///file transfer
                    //6779 = PPP1 reception port
                    // sArray[1] = user request
                    if (sArray[1].equals("cannot be found")) {
                        System.out.println("Error msg from PPP2");
                        System.out.println(userSelection);
                        txtAreaAnswer.append("\n The requested file: " + userSelection + " " + (sArray[1]) + " on the peer overlay!!"); //print error msg on peer
                    }

                }

                //End- receive the multicast
            } catch (SocketException e) {
                System.out.println("Socket: " + e.getMessage());
            } catch (UnknownHostException e) {
                System.out.println("Socket:" + e.getMessage());
            } catch (EOFException e) {
                System.out.println("EOF:" + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO: " + e.getMessage());
            }

        }

    }

}
