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
        try {
            Scanner scanner = new Scanner(System.in);
            Socket socket = new Socket("localhost", 3000);
            BufferedReader inputFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream outputToServer = new DataOutputStream(socket.getOutputStream());

            System.out.println("CLIENT CONNESSO");

            System.out.print("Inserisci la richiesta della risorsa: ");
            String request = scanner.nextLine();
            outputToServer.writeBytes("GET " + request + " HTTP/1.1\n\n");

            String responseLine;
            while ((responseLine = inputFromServer.readLine()) != null) {
                System.out.println(responseLine);
            }

            socket.close();
    }
        catch (Exception e) {
            System.out.println("Errore di connessione");
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
