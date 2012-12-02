package client;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

/**
	ConnectionButtonHandler
	
	An ActionListener added to the connection buttons to connect the cameras.

*/
class ConnectionButtonHandler implements ActionListener {

    GUI gui;
    int camera;

    public ConnectionButtonHandler(GUI gui, int cam) {
        this.gui = gui;
        this.camera = cam;
    }

    public void actionPerformed(ActionEvent evt) {
    	gui.pack();
        gui.toggleConnection(camera);
    }
}

/**
	MovementButtonHandler
	
	An ActionListener added to the movement radio buttons to select one.

*/
class MovementButtonHandler implements ActionListener {

    GUI gui;
    int button;

    public MovementButtonHandler(GUI gui, int button) {
        this.gui = gui;
        this.button = button;
    }

    public void actionPerformed(ActionEvent evt) {
        gui.selectMovement(button);
    }
}

/**
	SynchronizeButtonHandler
	
	An ActionListener added to the synchronize radio buttons to select one.

*/
class SynchronizeButtonHandler implements ActionListener {

    GUI gui;
    int button;

    public SynchronizeButtonHandler(GUI gui, int button) {
        this.gui = gui;
        this.button = button;
    }

    public void actionPerformed(ActionEvent evt) {
        gui.selectSync(button);
    }
}

/**
	GUI object
*/
class GUI extends JFrame {

    ClientMonitor monitor;

    boolean connectingOrDisconnecting = false;

	// Camera images
    JLabel camera1;
    JLabel camera2;
    
    // Connection buttons
    JButton connectCamera1;
    JButton connectCamera2;
    JButton disconnectCamera1;
    JButton disconnectCamera2;
    
    // Text below the connection buttons
    JLabel delay1, fps1, movement1;
    JLabel delay2, fps2, movement2;
    
    // alert labels
    JLabel alert1;
    JLabel alert2;
    
    // Radio buttons at the bottom
    JRadioButton movie, idle, auto, sync, async;
    
    // True if respective camera is connected
    boolean camera1connected = false;
    boolean camera2connected = false;
    
    public GUI() {
        super();
        
        // Default camera image at the start
        ImageIcon icon = new ImageIcon("../camera.jpeg");
        
        
        // Initialize all the objects
    	camera1 = new JLabel(icon);
    	camera2 = new JLabel(icon);
        
        connectCamera1 = new JButton("Camera 1 Connect");
        connectCamera2 = new JButton("Camera 2 Connect");
        disconnectCamera1 = new JButton("Camera 1 Disconnect");
        disconnectCamera2 = new JButton("Camera 2 Disconnect");
        
        delay1 = new JLabel("Delay: 0.0s");
        fps1 = new JLabel("FPS: 60.0");
        movement1 = new JLabel("Movement: Idle");
        
        delay2 = new JLabel("Delay: 0.0s");
        fps2 = new JLabel("FPS: 60.0");
        movement2 = new JLabel("Movement: Idle");
        
        // Radio Buttons
        movie = new JRadioButton("Movie");
        idle = new JRadioButton("Idle");
        auto = new JRadioButton("Auto");
        
        alert1 = new JLabel();
        alert2 = new JLabel();
        
        sync = new JRadioButton("Synchronus");
        async = new JRadioButton("Asynchronus");
        
        
        connectCamera1.addActionListener(new ConnectionButtonHandler(this, 1));
        disconnectCamera1.addActionListener(new ConnectionButtonHandler(this, 1));
        connectCamera2.addActionListener(new ConnectionButtonHandler(this, 2));
        disconnectCamera2.addActionListener(new ConnectionButtonHandler(this, 2));
        
        movie.addActionListener(new MovementButtonHandler(this, ClientMonitor.MOVIE));
        idle.addActionListener(new MovementButtonHandler(this, ClientMonitor.IDLE));
        auto.addActionListener(new MovementButtonHandler(this, ClientMonitor.AUTO));
        
        sync.addActionListener(new SynchronizeButtonHandler(this, 1));
        async.addActionListener(new SynchronizeButtonHandler(this, 2));
		
		// Main outside content pane
		Container pane = this.getContentPane();
		
		// Left pane with camera 1 connection buttons & labels
		Container left = new Container();
		left.setLayout(new GridBagLayout());
		
		// Right pane with camera 2 connection buttons & labels
		Container right = new Container();
		right.setLayout(new GridBagLayout());
		
		// Bottom left pane with movement options
		Container bottom_left = new Container();
		bottom_left.setLayout(new GridBagLayout());
		
		// Bottom right pane with synchronize options
		Container bottom_right = new Container();
		bottom_right.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		// Camera connection buttons
		c.gridx = 0;
		c.gridy = 0;
		left.add(connectCamera1, c);
		right.add(connectCamera2, c);
		
		// Camera disconnection buttons
		c.gridy = 1;
		left.add(disconnectCamera1, c);
		right.add(disconnectCamera2, c);
		
		// Camera delay labels
		c.gridy = 2;
		left.add(delay1, c);
		right.add(delay2, c);
		
		// Camera FPS labels
		c.gridy = 3;
		left.add(fps1, c);
		right.add(fps2, c);
		
		// Camera Movement labels
		c.gridy = 4;
		left.add(movement1, c);
		right.add(movement2, c);
		
		c.gridy = 5;
		
		left.add(alert1, c);
		right.add(alert2, c);
		
		// just reusing this, new object for default vals
		c = new GridBagConstraints();
		
		// First row of bottom; movie and sync radio buttons
		c.gridx = 0;
		c.gridy = 0;
		sync.setSelected(true);
		bottom_left.add(movie, c);
		bottom_right.add(sync, c);
		
		// Second row, idle and async radio buttons
		c.gridy = 1;
		idle.setSelected(true);
		bottom_left.add(idle, c);
		bottom_right.add(async, c);
		
		// Last row. Auto movement
		c.gridy = 2;
		bottom_left.add(auto, c);
		
		
		pane.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
        
        c.ipadx = 50;
        c.ipady = 50;
        
        c.gridx = 0;
		c.gridy = 0;
        pane.add(left, c);
        
        c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 0;
        pane.add(camera1, c);
        
		c.gridx = 2;
		c.gridy = 0;
        pane.add(camera2, c);
        
        c.gridx = 3;
        c.gridy = 0;
        pane.add(right, c);
        
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 1;
        pane.add(bottom_left, c);
        
        c.gridx = 2;
        
        pane.add(bottom_right, c);
        
        this.setLocationRelativeTo(null);
        this.pack();
        setVisible(true);
        updateButtons();
    }
    
    /**
    Toggles connection. 1 for camera 1 and 2 for camera 2.
    */
    public void toggleConnection(int camera){
        if(!connectingOrDisconnecting){
          connectingOrDisconnecting = true;
    	  if(camera == 1){
    	    if(monitor.camera1Connected)
    	      monitor.disconnect(1);
            else
              monitor.connect(1);
    	  	camera1connected = !camera1connected;
    	  }
    	  else if (camera == 2){
    	    if(monitor.camera2Connected)
    	      monitor.disconnect(2);
            else
              monitor.connect(2);
    	  	camera2connected = !camera2connected;
    	  	
    	  }
          updateButtons();
          connectingOrDisconnecting = false;
        }
    }
    
    // Based on whether the cameras are connected or not, updates the buttons to be grayed out
    public void updateButtons(){
    	connectCamera1.setEnabled(!camera1connected);
    	connectCamera2.setEnabled(!camera2connected);
    	disconnectCamera1.setEnabled(camera1connected);
    	disconnectCamera2.setEnabled(camera2connected);
    }
    
    //sets what will be displayed visually for the delay val
    public void setDelay(double delay, int camera){
    	JLabel j = (camera == 1) ? delay1 : delay2;
    	j.setText("Delay: " + delay);
	}
	
	public void setFPS(double fps, int camera){
    	JLabel j = (camera == 1) ? fps1 : fps2;
    	j.setText("FPS: " + fps);
	}
	
	public void setMovement(int movement, int camera){
    	JLabel j = (camera == 1) ? movement1 : movement2;
    	String text = "";
    	// Set a color based on movement
    	switch(movement){
    	
    	case ClientMonitor.MOVIE:
    	j.setForeground(Color.green);
    	text += "Movie";
    	break;
    	
    	case ClientMonitor.IDLE:
    	j.setForeground(Color.red);
    	text += "Idle";
    	break;
    	
    	case ClientMonitor.AUTO:
    	j.setForeground(Color.blue);
    	text += "Auto";
    	break;
    	
    	}
    	
        j.setText("Movement: " + text);
	}
	
	// sets the movement mode based on a number 1 - 2 - 3 (sequentially vertical)
	public void selectMovement(int num){
		 //deselect all
		 movie.setSelected(false);
		 idle.setSelected(false);
		 auto.setSelected(false);
		 try{
		 
		 switch(num){
		 	case ClientMonitor.MOVIE:
		 	    movie.setSelected(true);
		 		monitor.set_mode(ClientMonitor.MOVIE);
		 		monitor.send_command(ClientMonitor.MOVIE);
		 		break;
		 	case ClientMonitor.IDLE:
		 		idle.setSelected(true);
		 		monitor.set_mode(ClientMonitor.IDLE);
		 		monitor.send_command(ClientMonitor.IDLE);
		 		break;
		 	case ClientMonitor.AUTO:
		 		auto.setSelected(true);
		 		monitor.set_mode(ClientMonitor.AUTO);
		 		monitor.send_command(ClientMonitor.AUTO);
		 		break;
		 }
		 
		 } catch(java.io.IOException e){
		    System.out.println("generic error message");
		 }
		 setMovement(num, 1);
		 setMovement(num, 2);
		 alert(0); // reset alert
	}
	
	// sets the Sync mode based on a number 1 - 2 (sequentially vertical)
	public void selectSync(int num){
		 //deselect all
		 sync.setSelected(false);
		 async.setSelected(false);
		 
		 switch(num){
		 	case 1:
		 		sync.setSelected(true);
		 		break;
		 	case 2:
		 		async.setSelected(true);
		 		break;
		 }
	}
	
	public void updateCamera1(byte[] data) {
        Image theImage = getToolkit().createImage(data);
        getToolkit().prepareImage(theImage,-1,-1,null);     
        camera1.setIcon(new ImageIcon(theImage));
    }
    
    public void updateCamera2(byte[] data) {
        Image theImage = getToolkit().createImage(data);
        getToolkit().prepareImage(theImage,-1,-1,null);     
        camera2.setIcon(new ImageIcon(theImage));
    }
    
    // Sort of hack to deal with GUI and Monitor's dependencies on eachother
    public void setMonitor(ClientMonitor mon){
        monitor = mon;
    }
    
    // set the camera alert for a camera. 0 for none, 1 or 2 for camera
    public void alert(int cam){
        alert1.setText("");
        alert2.setText("");
        
        if(cam == 1){
            alert1.setText("ALERT!");
            alert1.setForeground(Color.red);
        } else if(cam == 2){
            alert2.setText("ALERT!");
            alert2.setForeground(Color.red);
        }
        
    }
	
}
