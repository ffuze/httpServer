package com.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try{
            Scanner scanner = new Scanner(System.in);
            Socket s = new Socket("localhost", 3000);
            BufferedReader inputDalServer = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream outputVersoServer = new DataOutputStream(s.getOutputStream());

            System.out.println(inputDalServer.readLine());
            String richiesta = scanner.nextLine();
            outputVersoServer.writeBytes(richiesta);

        }
        catch(Exception e){
            System.out.println("Errore di connessione");
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
