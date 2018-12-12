package client;


import static client.IO_client.reader;
import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        /*Manual insertion of data*/
        System.out.println("Put the IP of the server: ");
        String ip_address = reader.readLine();
        
        
        /*This initializes the socket with the ip address*/
        Socket socket = new Socket(ip_address, 9000);
        
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Connection established... sending text");
        
        String[] credentials = null;
        while (credentials == null) {
            credentials = new String[2];
        System.out.print("Welcome. Please, sign in : ");
        //String optionText = reader.readLine();
        //int option = Integer.parseInt(optionText);

        //switch (option) {
            //case 1: {
                IO_client.newClient();
                credentials[0] = IO_client.user;
                credentials[1] = IO_client.password;
               // try {
               //        printWriter.println("Envia chequeo de nombre");
               // } catch (Exception ex) {
               //Logger.getLogger(IO_client.class.getName()).log(Level.SEVERE, null, ex);
                
               // break;
           // }}
            //case 2: {
            //    IO_client.newClient();
             //   credentials[0] = IO_client.user;
            //    credentials[1] = IO_client.password;
            //    break;
           // }
        //}
        printWriter.println(credentials);
        if (credentials != null) {
            System.out.println("Log in successful.");
        }
        }  
        String clin_history = IO_client.askInfoClient(credentials);
        printWriter.println(clin_history);

        //After sending the clinic history time to record em signals.
        System.out.println("Signal recording begins. At least 2 have to be recorded.");
        System.out.println("Signal recording may take some time to start. Please, be patient.");

        //String signal_example = "2\n2\n-4\n9\nx"; 
        /*Signals given will have in the first line dat type, in between data and the last one an X*/

        //System.out.println(signal_example);
        System.out.println("Put the MAC adress of the Bitalino to use: ");
        String mac_bitalino = reader.readLine();
        for (int i = 0;; i++) {
            if (i < 2) {
                System.out.println("Which type of signal would you like to record?");
                System.out.println("EMG (0)");
                System.out.println("ECG (1)");
                System.out.println("EDA (2)");
                System.out.println("EEG (3)");
                System.out.println("ACC (4)");
                System.out.println("LUX (5)");
                System.out.print("Option: ");
                String line = reader.readLine();
                
                int signalType = Integer.parseInt(line);
                System.out.println("Option " + signalType + " chosen.");
                System.out.println("Record of signal " + (i + 1) + " begins");
                //String biosignal = IO.obtainBioSignal();
                //printWriter.println(signal_example);
                
                Frame[] frame = IO_client.readBitalinoSignal(signalType, mac_bitalino);
                String signal = "2\n";
                for (int k = 0; k < 10; k++) {
                    for (Frame frame1 : frame) {
                        signal = signal + frame1.analog[0] + "\n";
                    }
                }
                printWriter.println(signal);
            }
            
            if (i >= 2) {
                System.out.println("Do you want to record another one [Y]/[N]?");
                String answer = reader.readLine();
                if (answer.equalsIgnoreCase("y")) {
                    //String biosignal = IO.obtainBioSignal();
                    //System.out.println("Record of " + (i+1) + "signal begins");
                    //printWriter.println(signal_example);
                    System.out.println("Which type of signal would you like to record?");
                    System.out.println("EMG (0)");
                    System.out.println("ECG (1)");
                    System.out.println("EDA (2)");
                    System.out.println("EEG (3)");
                    System.out.println("ACC (4)");
                    System.out.println("LUX (5)");
                    System.out.print("Option: ");
                    String line = reader.readLine();
                    
                    int signalType = Integer.parseInt(line);
                    System.out.println("Option " + signalType + " chosen.");
                    Frame[] frame = IO_client.readBitalinoSignal(signalType, mac_bitalino);
                    String signal = "2\n";
                    for (int k = 0; k < 10; k++) {
                        for (Frame frame1 : frame) {
                            signal = signal + frame1.analog[signalType] + "\n";
                        }
                    }
                    printWriter.println(signal);
                } else if (answer.equalsIgnoreCase("n")) {
                    System.out.println("Closing client...");
                    printWriter.println("x");
                    break;
                } else {
                    System.out.println("Invalid answer");
                }
            }
        }
        reader.close();
        IO_client.releaseResourcesClient(printWriter, socket);
        System.out.println("Process finished. Proceeding to disconnect.");
        System.out.println("Goodbye.");
    }
    
 

}
