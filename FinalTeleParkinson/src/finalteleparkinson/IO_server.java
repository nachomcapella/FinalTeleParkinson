package finalteleparkinson;

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

public class IO_server {

    static void modifyFile(File fileToModify) throws IOException {
        try {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(fileToModify, true));
                System.out.println("Please, add an annotation: ");
                String newline = IO_server.consoleReadLine();
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

    //Will generate 3 \n lines of info for each client
    public static String askInfoClient(String[] credentials) throws IOException {

        String name = credentials[0];
        System.out.println("Symptoms and signs?");
        String symptoms = IO_server.consoleReadLine();

        //Getting date
        //First the day
        Date date = new Date();
        String title = "1";
        String strDateFormat = "dd-MM-yy-HH-mm-ss";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);

        //Final structure of type 1 file
        String info = title + "\n" + name + "\n" + symptoms + "\n" + formattedDate;
        return info;
    }

    public static String[] identifyClient() throws IOException {
        String[] credentials = null;
        System.out.print("Welcome. Please, sign up (1) or sign in (2): ");
        String optionText = IO_server.consoleReadLine();
        int option = Integer.parseInt(optionText);
        switch (option) {
            case 1: {
                newClient();
                credentials = knownClient();
                break;
            }
            case 2: {
                credentials = knownClient();
                break;
            }
        }
        if (credentials != null) {
            System.out.println("Log in successful.");
        }
        return credentials;
    }

    public static void newClient() throws IOException {
        System.out.print("Please, select an username: ");
        String username = IO_server.consoleReadLine();
        System.out.print("Please, select a password: ");
        String password = IO_server.consoleReadLine();
        try {
            saveNewClient(username, password);
        } catch (Exception ex) {
            Logger.getLogger(IO_server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void saveNewClient(String username, String password) throws Exception {
        String root = "C:\\Users\\Ana\\Desktop\\" + username;
        if (new File(root).mkdirs()) {
            Files.createDirectories(Paths.get(root));
            String path = root + "\\credentials.txt";
            File file = new File(path);
            //Now we save info into specific .txt (except username which is info already in the folder name) in the path
            PrintWriter write_text = null;
            try {
                write_text = new PrintWriter(file);
                write_text.println(username);
                String key = "ezeon8547";
                String encoded = Encryption.encrypt(key, password);
                write_text.println(encoded);

                System.out.println("Sign up succeeded. Please, log in.");
            } catch (Exception e) {
                System.out.println("Something has gone wrong");
                e.printStackTrace();
            } finally {
                if (write_text != null) {
                    write_text.close();
                }
            }

        } else {
            System.out.println("That username is already taken.");
        }

    }

    public static String[] knownClient() throws IOException {
        String[] credentials = new String[2];
        System.out.print("Username: ");
        String username = IO_server.consoleReadLine();
        System.out.print("Password: ");
        String password = IO_server.consoleReadLine();
        try {
            boolean result = signInClient(username, password);
            if (result) {
                credentials[0] = username;
                credentials[1] = password;
            }
        } catch (Exception ex) {
            Logger.getLogger(IO_server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return credentials;
    }

    public static boolean signInClient(String username, String password) throws Exception {
        String file = "C:\\Users\\Nacho\\Desktop\\telemedicine\\" + username + "\\credentials.txt";;
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
        String password = IO_server.consoleReadLine();
        try {
            boolean result = signInClient(username, password);
            if (result) {
                credentials[0] = username;
                credentials[1] = password;
            }
        } catch (Exception ex) {
            Logger.getLogger(IO_server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return credentials;
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
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void releaseResourcesClient(PrintWriter printWriter, Socket socket) {
        try {
            printWriter.close();
        } catch (Exception ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void releaseResourcesServer(ServerSocket serverSocket) {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void releaseResourcesDoctor(PrintWriter printWriter, OutputStream outputStream, Socket socket) {
        try {
            printWriter.close();
        } catch (Exception ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            outputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void saveInfo(String info, String username) throws Exception {
        String root = "C:\\Users\\Nacho\\Desktop\\telemedicine\\" + username;
        Scanner scanner = new Scanner(info);
        //Splits string finding specified caracter
        String splitted_info[] = info.split("\n");
        int info_number = Integer.parseInt(splitted_info[0]);
        String path = "";

        if (info_number == 1) {
            path = root + "\\" + "session" + "_" + splitted_info[3] + ".txt";
        } else {
            Date date = new Date();
            String strDateFormat = "dd-MM-yy-HH-mm-ss";
            DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
            String formattedDate = dateFormat.format(date);
            //This is how we save the biosignals that we recieve as strings
            //The format of our biosignals SAVED is a header , followed by the actual values of the signal.
            path = root + "\\" + "signal" + "_" + formattedDate + ".txt";
        }

        //This will create the folder of a person, if it does not exist, and its info file.
        if (new File(root).mkdirs()) {
            Files.createDirectories(Paths.get(root));
        }
        File file = new File(path);
        System.out.println(path);
        //Now we save info into specific .txt (except username which is info already in the folder name) in the path
        PrintWriter write_text = null;
        try {
            write_text = new PrintWriter(file);
            //Write until nothing can be written into file direction
            while (scanner.hasNext()) {
                write_text.println(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
        } finally {
            if (write_text != null) {
                write_text.close();
            }
            if (scanner != null) {
                scanner.close();
            }

        }

    }
/*
    public static Frame[] readBitalinoSignal(int signalType) {
        Frame[] frame = null;
        BITalino bitalino = null;
        try {
            bitalino = new BITalino();
            // find devices
            //Only works on some OS
            Vector<RemoteDevice> devices = bitalino.findDevices();
            System.out.println(devices);

            //You need TO CHANGE THE MAC ADDRESS
            String macAddress = "20:17:09:18:49:99";
            int SamplingRate = 10;
            bitalino.open(macAddress, SamplingRate);

            // start acquisition on analog channels A2 and A6
            //If you want A1, A3 and A4 you should use {0,2,3}
            int[] channelsToAcquire = {signalType};
            bitalino.start(channelsToAcquire);

            //read 10000 samples
            for (int j = 0; j < 10; j++) {

                //Read a block of 100 samples 
                frame = bitalino.read(10);

                System.out.println("size block: " + frame.length);

                //Print the samples
                for (int i = 0; i < frame.length; i++) {
                    System.out.println((j * 100 + i) + " seq: " + frame[i].seq + " "
                            + frame[i].analog[0] + " "
                            + frame[i].analog[1] + " "
                            + frame[i].analog[2] + " "
                            + frame[i].analog[3] + " "
                            + frame[i].analog[4] + " "
                            + frame[i].analog[5]
                    );

                }
            }
            //stop acquisition
            bitalino.stop();
        } catch (BITalinoException ex) {
            Logger.getLogger(IO_server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Throwable ex) {
            Logger.getLogger(IO_server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                //close bluetooth connection
                if (bitalino != null) {
                    bitalino.close();
                }
            } catch (BITalinoException ex) {
                Logger.getLogger(IO_server.class.getName()).log(Level.SEVERE, null, ex);
            }
            return frame;
        }

    }

    public static void saveBitalinoSignal(Frame[] frame, int signalType) {
        for (int i = 0; i < frame.length; i++) {
            System.out.println("" + frame[i].analog[signalType]);
        }
    }
    
    */

}
