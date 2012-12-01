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

        
        GUI gui = new GUI();

        ClientMonitor monitor = new ClientMonitor(server1,server2,port1,port2, gui); 
        gui.setMonitor(monitor);

        ClientDataController clientDataController = new ClientDataController(monitor);
        
        
        ImageDispatcher imageDispatcher = new ImageDispatcher(monitor, gui);
        
        clientDataController.start();
        imageDispatcher.start();
        
    }

}
 
 
