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
        if(credentials!=null){
            System.out.println("Log in successful.");
        }
        return credentials;
    }

    public static void newClient() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please, select an username: ");
        String username = scanner.nextLine();
        System.out.print("Please, select a password: ");
        String password = scanner.nextLine();
        try {
            saveNewClient(username, password);
        } catch (Exception ex) {
            Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
        }

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
                write_text.println(password);
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
            if (result){
                credentials[0]=username;
                credentials[1]=password;
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
            String passwordSaved = br.readLine();
            if (username.matches(usernameSaved) && password.matches(passwordSaved)) {
                return true;
            } else {
                return false;
            }
        }

    }

    public static void releaseResourcesThread(ServerSocket serverSocket) {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Thread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void releaseResourcesClient(InputStream inputStream, Socket socket) {
        try {
            inputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(Thread.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Thread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void releaseResourcesServer(ServerSocket serverSocket) {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Thread.class.getName()).log(Level.SEVERE, null, ex);
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
}
