package doctor;

import doctor.*;
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
        String[] credentials = null;
        while (credentials == null) {
            credentials = IO_doctor.identifyDoctor();
        };

        //String signal_example = "2\n2\n-4\n9\nx"; //LAS SEÑALES  devueltas por el metofo que quedan tienen en la primera linea lel tipo de dat, en la última una x y en tre medias los valores grabados
        //System.out.println(signal_example);
        while (true) {
            System.out.println("\nPlease, select a patient:");
            String path = "C:\\Users\\Nacho\\Desktop\\telemedicine\\";
            File folder = new File(path);
            IO_doctor.listFoldersForFolder(folder);
            System.out.print("Option («exit» to leave): ");
            String name = IO_doctor.consoleReadLine();
            if (name.matches("exit")) {
                IO_doctor.releaseResourcesDoctor(printWriter, outputStream, socket);
                System.exit(0);
            }
            while (true) {
                boolean finished = patientOptions(name);
                if (finished == false) {
                    break;
                }
            }

        }
    }

    public static boolean patientOptions(String name) throws IOException {
        System.out.println("\nWhat do you want to do?");
        System.out.println("View files (0)");
        System.out.println("Modify files (1)");
        System.out.println("Delete files (2)");
        System.out.println("Exit (3)");
        System.out.print("Option: ");

        String line = IO_doctor.consoleReadLine();
        int option = Integer.parseInt(line);
        switch (option) {
            case 0: {
                String path2 = "C:\\Users\\Nacho\\Desktop\\telemedicine\\" + name + "\\";
                File folder2 = new File(path2);
                IO_doctor.listFilesForFolder(folder2);
                System.out.println("\nWhat file do you want to view?");
                System.out.print("Option: ");
                String file = IO_doctor.consoleReadLine();
                String path3 = path2 + file;
                File fileToView = new File(path3);
                IO_doctor.displayFile(fileToView);
                return true;
            }
            case 1: {
                String path2 = "C:\\Users\\Nacho\\Desktop\\telemedicine\\" + name + "\\";
                File folder2 = new File(path2);
                IO_doctor.listFilesForFolder(folder2);
                System.out.println("\nWhat file do you want to modify?");
                System.out.print("Option: ");
                String file = IO_doctor.consoleReadLine();
                String path3 = path2 + file;
                File fileToModify = new File(path3);
                IO_doctor.displayFile(fileToModify);
                IO_doctor.modifyFile(fileToModify);
                return true;
            }
            case 2: {
                String path2 = "C:\\Users\\Nacho\\Desktop\\telemedicine\\" + name + "\\";
                File folder2 = new File(path2);
                IO_doctor.listFilesForFolder(folder2);
                System.out.println("\nWhat file do you want to delete?");
                System.out.print("Option: ");
                String file = IO_doctor.consoleReadLine();
                String path3 = path2 + file;

                File fileToDelete = new File(path3);
                System.out.println(fileToDelete.isFile());
                boolean result = fileToDelete.delete();
                System.out.println("File deleted: " + result);
                return true;
            }
            case 3: {
                return false;

            }

        }
        return false;

    }

}
