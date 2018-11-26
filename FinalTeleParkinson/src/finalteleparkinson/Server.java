package finalteleparkinson;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9000);
        try {
            while (true) {
                //Thie executes when we have a client
                Socket socket = serverSocket.accept();
                new java.lang.Thread(new Thread(socket)).start();
            }
        } finally {
            //Can do this because the method itself is static
            IO.releaseResourcesServer(serverSocket);
        }
    }
    
}