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
public class JPEGHTTPClient {

    public static void main(String[] args) {
        if (args.length!=2) {
            System.out.println("Syntax: JPEGHTTPClient <address> <port>");
            System.exit(1);
        }
        JPEGBuffer buffer1 = new JPEGBuffer();
        JPEGBuffer buffer2 = new JPEGBuffer();

        ClientController clientController = new ClientController(server1,server2,port1,port2,buffer1,buffer2); 
        new GUI(args[0],Integer.parseInt(args[1]));
    }

}
 
 
