package com.server;

import java.net.ServerSocket;
import java.net.Socket;

public class App 
{
    public static void main( String[] args )
    {
        try{
            ServerSocket server = new ServerSocket(3000);
            do{
                Socket s = server.accept();
                MyThread t = new MyThread(s);
                t.start();
            }while(true);
        }
        catch(Exception e){
            System.out.println("Errore di connessione");
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
