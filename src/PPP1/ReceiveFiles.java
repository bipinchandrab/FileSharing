package PPP1;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Bipin
 */
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

public class ReceiveFiles extends Thread {

    int filePort;
    JTextArea txtAreaAnswer;

    public ReceiveFiles(int filePort, JTextArea txtAreaAnswer) {
        this.filePort = filePort;
        this.txtAreaAnswer = txtAreaAnswer;
        this.start(); //start Thread
    }

    public void run() {
        InputStream in;
        OutputStream out;
        Socket clientSocket;
        String ClassName = new String();
        try {

            ServerSocket listenSocket = new ServerSocket(6779);
            clientSocket = listenSocket.accept();

            in = clientSocket.getInputStream();
            // Construct data input stream to receive class files
            DataInputStream clientData = new DataInputStream(in);
            // Receive the class file name
            ClassName = clientData.readUTF();
            // Receive the class file length

            int size = clientData.readInt();
            // Construct a byte array to receive the class file
            byte[] buffer = new byte[size];
            int bytesRead = clientData.read(buffer, 0, buffer.length);
            // Construct a file output stream to save the class file
            FileOutputStream fo = new FileOutputStream("src\\PPP1\\SharingFiles\\Downloaded-CloudComputing.jpg");
            BufferedOutputStream bos = new BufferedOutputStream(fo);
            bos.write(buffer, 0, bytesRead);
            bos.close();
            System.out.println("------------------------------");
            System.out.println("The class file of " + ClassName + " has been Received.");

            txtAreaAnswer.append("\n The File of CloudComputing.jpg was found, \n downloaded and saved as Downloaded-CloudComputing.jpg");
        } catch (EOFException e) {
            System.out.println("EOF" + e.getMessage());

        } catch (FileNotFoundException e) {
            System.out.println("File " + ClassName + " cannot find.");

        } catch (SocketException e) {
            System.out.println("Client closed.");

        } catch (IOException e) {
            e.printStackTrace();

        }

    }
}
