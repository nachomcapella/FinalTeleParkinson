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

public class IO {

    public IO() {
    }

    public static String consoleReadLine() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        return line;
    }

    //Will generate 3 \n lines of info for each client
    public static String askInfoClient(String[] credentials) {

        Scanner scanner = new Scanner(System.in);
        String name = credentials[0];
        System.out.println("Symptoms and signs?");
        String symptoms = scanner.nextLine();

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

    public static String[] identifyClient() {

        Scanner scanner = new Scanner(System.in);
        String[] credentials = null;
        System.out.print("Welcome. Please, sign up (1) or sign in (2): ");
        String optionText = scanner.nextLine();
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

    public static void newClient() {
        Scanner scanner = new Scanner(System.in);
        //we create or window to ask
        Newclient nc = new Newclient();
        nc.setVisible(true);
        /*String username= nc.name;
        String password= nc.password;*/
        
        /*System.out.print("Please, select an username: ");
        String username = scanner.nextLine();
        System.out.print("Please, select a password: ");
        String password = scanner.nextLine(); ^*/
      /* try {
            saveNewClient(username, password);
        } catch (Exception ex) {
            Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
        }*/

    }

    public static void saveNewClient(String username, String password) throws Exception {
        String root = "C:\\Users\\Nacho\\Desktop\\telemedicine\\" + username;
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

    public static String[] knownClient() {
        Scanner scanner = new Scanner(System.in);
        String[] credentials = new String[2];
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        try {
            boolean result = signInClient(username, password);
            if (result) {
                credentials[0] = username;
                credentials[1] = password;
            }
        } catch (Exception ex) {
            Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return credentials;
    }

    public static boolean signInClient(String username, String password) throws Exception {
        String file = "C:\\Users\\Nacho\\Desktop\\telemedicine\\" + username + "\\credentials.txt";;
        PrintWriter write_text = null;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String usernameSaved = br.readLine();
            String encoded = br.readLine();
            String key = "ezeon8547";
            String passwordSaved = Encryption.decrypt(key, encoded);

            if (username.matches(usernameSaved) && password.matches(passwordSaved)) {
                return true;
            } else {
                return false;
            }
        }

    }

    public static void releaseResourcesServerThread(ServerSocket serverSocket) {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void releaseResourcesClient(InputStream inputStream, Socket socket) {
        try {
            inputStream.close();
        } catch (IOException ex) {
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
        }

    }

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
            Logger.getLogger(BitalinoDemo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Throwable ex) {
            Logger.getLogger(BitalinoDemo.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                //close bluetooth connection
                if (bitalino != null) {
                    bitalino.close();
                }
            } catch (BITalinoException ex) {
                Logger.getLogger(BitalinoDemo.class.getName()).log(Level.SEVERE, null, ex);
            }
            return frame;
        }

    }

    public static void saveBitalinoSignal(Frame[] frame, int signalType) {
        for (int i = 0; i < frame.length; i++) {
            System.out.println("" + frame[i].analog[signalType]);
        }
    }

}
