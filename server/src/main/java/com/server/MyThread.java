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

    public static String getContentType(File file) {
        String nomeFile = file.getName();
        String estensioneFile = nomeFile.substring(nomeFile.lastIndexOf('.') + 1);

        switch (estensioneFile.toLowerCase()) {
            case "html":
                return "text/html";
            case "txt":
                return "text/plain";
            case "png":
                return "image/png";
            default:
                return "application/octet-stream";
        }
    }

    private static void sendBinaryFile(Socket socket, File file) throws IOException {
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        String contentType = getContentType(file);
        output.writeBytes("HTTP/1.1 200 OK\n");
        output.writeBytes("Content-Length: " + file.length() + "\n");
        output.writeBytes("Content-Type: " + contentType + "\n");
        output.writeBytes("\n");

        try (InputStream input = new FileInputStream(file)) {
            byte[] buffer = new byte[3000];
            int n;
            while ((n = input.read(buffer)) != -1) {
                output.write(buffer, 0, n);
            }
        }
    }

    @Override
    public void run() {
        try {
            BufferedReader inputDalClient = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream outputVersoClient = new DataOutputStream(s.getOutputStream());

            System.out.println("Server avviato");

            String richiestaClient = inputDalClient.readLine();

            String[] stringa = richiestaClient.split(" ");
            String metodo = stringa[0];
            String percorso = stringa[1];

            if(metodo.equals("GET") && !percorso.isEmpty()){
                percorso = percorso.substring(1);
                File nuovoFile = new File(percorso);
                if(nuovoFile.exists()){
                    sendBinaryFile(s, nuovoFile);
                }
                else{
                    outputVersoClient.writeBytes("HTTP/1.1 404 Not Found\n\n");
                }
            }
            else{
                outputVersoClient.writeBytes("HTTP/1.1 400 Bad Request\n\n");
            }

            s.close();
        } catch (Exception e) {
            System.out.println("Errore di connessione");
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
