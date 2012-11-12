import java.io.*;
import java.net.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import se.lth.cs.fakecamera.*; // To gain access to maximum image size

public class JPEGHTTPClient {

    public static void main(String[] args) {
        if (args.length!=2) {
            System.out.println("Syntax: JPEGHTTPClient <address> <port>");
            System.exit(1);
        }
        new GUI(args[0],Integer.parseInt(args[1]));
    }

}
 
 
