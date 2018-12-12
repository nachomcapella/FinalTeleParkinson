package finalteleparkinson;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerThread implements Runnable {
    
    Socket socket;
    
    public ServerThread(Socket s2) {
        socket = s2;
    }
    
    @Override
    public void run() {
        try {
            String username = "";
            System.out.println("Connection client created");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Recieving text:");            
            String line = "";

//            while(true){
//                while((line = bufferedReader.readLine()) != null){
//                    System.out.println(line);
//                }
//            }
            //Server acts line by line. Will never recieve at once a bunch of lines (even though a full string can be sended by client)
            while (true) {
                //Only when there is something to recieve, recieving starts. 
                //1º line to recieve at the beggining of each loop is the type of data.
                while ((line = bufferedReader.readLine()) != null) {
                    int title = Integer.parseInt(line);
                    switch (title) {

                        //Se lee todo del tipo 1, 
                        //por lo tanto la prox vez que entremos en el switch volveremos a leer la primera linea de un archivo que nos indica en que case localizarnos
                        case 1:
                            String info = line + "\n";
                            //After reading 1 line, we now that there are 3 lines left to be readed
                            //Info is just a clinical history without type line as a header
                            for (int i = 0; i < 3; i++) {
                                String readed = bufferedReader.readLine();
                                if (i == 0) {
                                    username = readed;
                                }
                                info = info + readed + "\n";
                            }
                            //Now we have to save this in physicall memory
                            System.out.println(info);
                            IO.saveInfo(info, username);
                            break;

                        //Antes de hacer el caso 2 definimos los métodos que graben vitalino
                        case 2:
                            String biosignal = line;
                            String single_read = "";
                            //We will stop reading when client sends an x
                            while (((single_read = bufferedReader.readLine()).equalsIgnoreCase("x")) == false) {
                                biosignal = biosignal + "\n" + single_read;
                            }
                            IO.saveInfo(biosignal, username);
                            break;
                    }
                }
            }
        } catch (Exception e) {
        } finally {
            IO.releaseResourcesServerThread(socket);
        }
        
    }
}
