package finalteleparkinson;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 9000);
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Connection established... sending text");

        //String clin_history = "1\nde\nfe\ndh";
        String clin_history = IO.askInfoClient();
        printWriter.println(clin_history);

        //After sending the clinic history time to record em signals.
        System.out.println("Signal recording begins. At least 2 have to be recorded");
        String signal_example = "2\n2\n-4\n9\nx"; //LAS SEÑALES  devueltas por el metofo que quedan tienen en la primera linea lel tipo de dat, en la última una x y en tre medias los valores grabados
        System.out.println(signal_example);

        for (int i = 0;; i++) {

            if (i < 2) {
                System.out.println("Record of " + (i+1) + "signal begins");
                //String biosignal = IO.obtainBioSignal();
                printWriter.println(signal_example);
            }
            if (i >= 2) {
                System.out.println("Do u want to record another one [Y]/[N]?");
                String answer = IO.consoleReadLine();
                if (answer.equalsIgnoreCase("y")) {
                    //String biosignal = IO.obtainBioSignal();
                    System.out.println("Record of " + (i+1) + "signal begins");
                    printWriter.println(signal_example);
                } else if (answer.equalsIgnoreCase("n")) {
                    System.out.println("Closing client...");
                    printWriter.println("x");
                    break;
                } else {
                    System.out.println("Invalid answer");
                }
            }
        }
    }
}