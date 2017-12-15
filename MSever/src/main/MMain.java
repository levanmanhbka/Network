package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MMain {
	public static void main(String[] args) {
		System.out.println("server hello");
		MServerGUI gui = new MServerGUI();
		gui.setVisible(true);
		gui.updateArrayListUser();
		MServerSocket serverSocket = new MServerSocket(gui);
		gui.setOnwer(new MIServerGUI() {

			@Override
			public void onStopServer() {
				serverSocket.stop();

			}

			@Override
			public void onStartServer() {
				serverSocket.start();

			}
		});
		
		gui.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				serverSocket.stop();
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
