package finalteleparkinson;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class Server {

    public static void main(String[] args) throws IOException {
       ServerSocket serverSocket = new ServerSocket(9000);
       NewJFrameServer window1 = new NewJFrameServer();
       window1.setVisible(true);
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