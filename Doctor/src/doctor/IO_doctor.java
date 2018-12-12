package doctor;

//This one controls input and output communications.
import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;
import java.util.Date;
import java.util.Vector;
import javax.bluetooth.RemoteDevice;

public class IO_doctor {

    public static void modifyFile(File fileToModify) throws IOException {
        try {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(fileToModify, true));
                System.out.println("Please, add an annotation: ");
                String newline = IO_doctor.consoleReadLine();
                bw.append(newline);
                bw.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception ex) {
        }
    }

    public static void displayFile(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while (line != null) {
                System.out.println(line);

                line = br.readLine();
            }
            br.close();
        }

    }

    public static String consoleReadLine() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        reader.close();
        return line;
    }


    public static String[] identifyDoctor() throws IOException {
        String[] credentials = null;
        System.out.println("Welcome. Please, sign in. ");
        credentials = knownDoctor();
        if (credentials != null) {
            System.out.println("Log in successful.");
        }
        return credentials;
    }

    public static String[] knownDoctor() throws IOException {
        String[] credentials = new String[2];
        System.out.println("Username: Doctor");
        String username = "Doctor";
        System.out.print("Password: ");
        String password = IO_doctor.consoleReadLine();
        try {
            boolean result = signInClient(username, password);
            if (result) {
                credentials[0] = username;
                credentials[1] = password;
            }
        } catch (Exception ex) {
            Logger.getLogger(IO_doctor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return credentials;
    }
    
    public static boolean signInClient(String username, String password) throws Exception {
        String file = "C:\\Users\\Ana\\Escritorio" + username + "\\credentials.txt";;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String usernameSaved = br.readLine();
            String encoded = br.readLine();
            String key = "ezeon8547";
            String passwordSaved = Encryption.decrypt(key, encoded);
            br.close();
            if (username.matches(usernameSaved) && password.matches(passwordSaved)) {
                return true;
            } else {
                return false;
            }
        }

    }

    public static void listFilesForFolder(File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else if (fileEntry.getName().matches("credentials.txt") != true) {
                System.out.println(fileEntry.getName());
            }
        }
    }

    public static void listFoldersForFolder(File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                if (fileEntry.isDirectory()) {
                    if ((fileEntry.getName().matches("Doctor")) != true) {
                        System.out.println(fileEntry.getName());
                    }
                }
            } else {
                System.out.println(fileEntry.getName());
            }
        }
    }

    public static void releaseResourcesServerThread(Socket socket) {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Doctor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    public static void releaseResourcesServer(ServerSocket serverSocket) {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Doctor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void releaseResourcesDoctor(PrintWriter printWriter, OutputStream outputStream, Socket socket) {
        try {
            printWriter.close();
        } catch (Exception ex) {
            Logger.getLogger(Doctor.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            outputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(Doctor.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Doctor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
