package finalteleparkinson;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

//
public class prueba {

    public static void main(String[] args) throws IOException, Exception {
        String lol = "pepe \nlol \njeje";
        Scanner scanner = new Scanner(lol);
        while(scanner.hasNext()){
            System.out.println("\n"+ scanner.nextLine());
        }

    }
}
