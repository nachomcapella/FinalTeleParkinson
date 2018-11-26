package finalteleparkinson;

//This one controls input and output communications.
import java.net.*;
import java.io.*;
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
    public static String askInfoClient() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Username?");
        String name = scanner.nextLine();
        System.out.println("Symptoms and signs?");
        String symptoms = scanner.nextLine();

        //Getting date
        //First the day
        Date date = new Date();
        String title = "1";
        String strDateFormat = "dd/MM/yy HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);

        //Final structure of type 1 file
        String info = title + "\n" + name + "\n" + symptoms + "\n" + formattedDate;
        return info;
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
        String root = "D:/Documetos/Universidad 4/Telemedicina/proyect/savedExamples/"+ username + "/";
        Scanner scanner = new Scanner(info);
        //Splits string finding specified caracter
        String splitted_info[] = info.split("\n");
        int info_number = Integer.parseInt(splitted_info[0]);
        String path = "";
        if (info_number == 1) {
            //We now that date is at line 4
            //Now we create the definitive path
            //CREO QUE FALLA POR QUE HAY QUE CREAR LA CARPETA DE ROOT ANTES QUE NADA. No se como se hace! :( (lo último que he añadido antes de que fallara)
            //POR OTRO LADO, puede que la fecha al tener barras genere problemas.
            //El deubgger es muy útil para esta parte <3
            path = root + info_number + "_" + splitted_info[3] + ".txt";
        } else {
            Date date = new Date();
            String strDateFormat = "dd/MM/yy HH:mm:ss";
            DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
            String formattedDate = dateFormat.format(date);
            //This is how we save the biosignals that we recieve as strings
            //The format of our biosignals SAVED is a header , followed by the actual values of the signal.
            path = root + info_number + "_" + formattedDate +".txt";
        }

        //This will create the folder of a person and its info file
        File file = new File(path);

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
