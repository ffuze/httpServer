package com.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class MyThread extends Thread{
    
    Socket s;

    public MyThread(Socket s){
        this.s = s;
    }

    private static void sendBinaryFile(Socket socket, File file) throws IOException{
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            output.writeBytes("HTTP/1.1 200 OK\n");
            output.writeBytes("Content-Length: " + file.length() + "\n");

            //SCEGLIERE IL GIUSTO CONTENT TYPE
            output.writeBytes("Content-Type: image/png\n");

            output.writeBytes("\n");
            InputStream input = new FileInputStream(file);
            byte[] buf = new byte[3000];
            int n;
            while((n = input.read(buf)) != -1){
                output.write(buf, 0, n);
            }
            input.close();
        }

    @Override
    public void run(){
        try{
            BufferedReader inputDalClient = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream outputVersoClient = new DataOutputStream(s.getOutputStream());

            System.out.println("Server avviato");

            outputVersoClient.writeBytes("Digita la richiesta");
            String richiestaClient = inputDalClient.readLine();

            String[] stringa = richiestaClient.split(" ");

            String stringaRicevuta;
            do{
                stringaRicevuta = inputDalClient.readLine();
                System.out.println(stringaRicevuta);
                s.close();
            }while(!stringaRicevuta.isEmpty());

            File nuovoFile = new File(stringa[1].substring(1));

            if(nuovoFile.exists()){
                /*
                String msg = "Il file esiste";
                outputVersoClient.writeBytes("HTTP/1.1 200 OK" + "\n");
                outputVersoClient.writeBytes("Content-Length: " + stringaRicevuta.length());
                outputVersoClient.writeBytes("Content-Type: text/plain");
                outputVersoClient.writeBytes("\n");
                outputVersoClient.writeBytes(msg);
                */
                sendBinaryFile(s, nuovoFile);
            }
            else{
                String msg = "Il file non esiste";
                outputVersoClient.writeBytes("HTTP/1.1 404 NotFound" + "\n");
                outputVersoClient.writeBytes("Content-Length: " + stringaRicevuta.length());
                outputVersoClient.writeBytes("\n");
                outputVersoClient.writeBytes(msg);
            }

            s.close();
        }
        catch(Exception e){
            System.out.println("Errore di connessione");
            System.out.println(e.getMessage());
            System.exit(1);
        }

        
    }
}
