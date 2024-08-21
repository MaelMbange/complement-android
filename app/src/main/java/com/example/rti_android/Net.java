package com.example.rti_android;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Net {

    public static final String ip = "192.168.1.8";
    public static final int port = 50000;

    private static Socket socket;

    public static Socket getSocket(){
        Log.e("MainActivity_app",  "Acces socket informations");
        if(socket == null){
            try {
                Log.e("MainActivity_app",  "Creating socket");
                socket = new Socket(ip,port);
                Log.e("MainActivity_app",  "Socket created");
            }catch (Exception e){
                Log.e("MainActivity_app",  "Socket creation - failed : " + e.getMessage());
            }
        }
        return socket;
    }

    public  static void sendMessage(String message) throws IOException {

        DataOutputStream dos = new DataOutputStream(getSocket().getOutputStream());
        String formated = message + "\r\n";
        dos.writeBytes(formated);
        System.out.println("Sending : " + formated);
    }

    public static String RecevoirData() throws IOException {
        DataInputStream dis = new DataInputStream(getSocket().getInputStream());

        StringBuilder sb = new StringBuilder();
        byte b;

        while(true){
            b = dis.readByte();
            //System.out.println((char)b);
            sb.append((char)b);
            if (sb.toString().endsWith("\r\n")) {
                System.out.println("Message end");
                break;
            }
        }
        String resultat = sb.toString();
        System.out.println("Receiving " + resultat);
        return resultat;
    }


}
