package client;

import java.io.*;
import java.net.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import se.lth.cs.fakecamera.*; // To gain access to maximum image size

public class JPEGClient {

    public static void main(String[] args) {
        
        String server1 = "localhost", server2 = "localhost";
        int port1 = 6077, port2 = 6078;

        ClientMonitor monitor = new ClientMonitor(server1,server2,port1,port2); 

        ClientDataController clientDataController = new ClientDataController(monitor);

        GUI gui = new GUI(monitor);
        
        
        ImageDispatcher imageDispatcher = new ImageDispatcher(monitor, gui);
        
        clientDataController.start();
        imageDispatcher.start();
        
    }

}
 
 
