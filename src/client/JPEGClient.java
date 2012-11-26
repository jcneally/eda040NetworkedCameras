package client;

import java.io.*;
import java.net.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import se.lth.cs.fakecamera.*; // To gain access to maximum image size
//Trying to synchronize
//received
//p
public class JPEGClient {

    public static void main(String[] args) {
//        if (args.length!=2) {
//            System.out.println("Syntax: JPEGHTTPClient <address> <port>");
//            System.exit(1);
//        }
        JPEGBuffer buffer1 = new JPEGBuffer();
        JPEGBuffer buffer2 = new JPEGBuffer();
        
        String server1 = "localhost", server2 = "localhost";
        int port1 = 6077, port2 = 6078;

        GUI gui = new GUI();
        
        ClientDataController clientDataController = new ClientDataController(server1,server2,port1,port2,buffer1,buffer2); 
        ImageDispatcher imageDispatcher = new ImageDispatcher(buffer1, buffer2, gui);
        
        clientDataController.start();
        imageDispatcher.start();
        
    }

}
 
 
