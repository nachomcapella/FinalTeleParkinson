package finalteleparkinson;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Doctor {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 9000);
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Doctor's connexion.");
        String[] credentials = IO.identifyDoctor();
        if (credentials == null) {
            //kill
        }

        //String signal_example = "2\n2\n-4\n9\nx"; //LAS SEÑALES  devueltas por el metofo que quedan tienen en la primera linea lel tipo de dat, en la última una x y en tre medias los valores grabados
        //System.out.println(signal_example);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nPlease, select a patient:");
            String path = "C:\\Users\\Nacho\\Desktop\\telemedicine\\";
            File folder = new File(path);
            IO.listFoldersForFolder(folder);
            System.out.print("Option: ");
            String name = scanner.nextLine();
            System.out.println("Patient " + name + " chosen.");

            System.out.println("\nWhat do you want to do?");
            System.out.println("View files (0)");
            System.out.println("Modify files (1)");
            System.out.println("Delete files (2)");
            System.out.print("Option: ");
            String line = scanner.nextLine();
            int option = Integer.parseInt(line);
            System.out.println("Option " + option + " chosen.");
            switch (option) {
                case 0: {
                    break;
                }
                case 1: {
                    break;
                }
                case 2: {
                    break;
                }
                case 3: {
                    break;
                }
            }

        }

    }

}
