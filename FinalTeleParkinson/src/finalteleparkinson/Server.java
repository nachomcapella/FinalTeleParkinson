package finalteleparkinson;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9000);
        System.out.println("Application started. Waiting for a patient...");
        try {
            while (true) {
                //Thie executes when we have a client
                Socket socket = serverSocket.accept();
                System.out.println("New patient connected.");
                new java.lang.Thread(new ServerThread(socket)).start();
            }
        } finally {
            //Can do this because the method itself is static
            IO.releaseResourcesServer(serverSocket);
        }
    }
    
}