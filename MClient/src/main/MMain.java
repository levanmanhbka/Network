package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MMain{

	public static void main(String[] args) {
		MClientGUI gui = new MClientGUI();
		gui.setVisible(true);
		MClientSocket clientSocket = new MClientSocket(gui);
		gui.setOwner(new MIClientGUI() {
			
			@Override
			public void onLogIn(String user, String pass, String recipient) {
				MMessage message = new MMessage(MMessage.LOG_IN, user, pass, recipient);
				clientSocket.sendMessage(message);
				
			}
			
			@Override
			public void onSignUp(String user, String pass, String recipient){
				MMessage message = new MMessage(MMessage.SIGN_UP, user, pass, recipient);
				clientSocket.sendMessage(message);
				
			}
			
			@Override
			public void onSendMessage(String user, String content, String recipient) {
				MMessage message = new MMessage(MMessage.MESSAGE, user, content, recipient);
				clientSocket.sendMessage(message);
				
			}
			
			@Override
			public void onConnect(String ip, int port) {
				clientSocket.open(ip, port);
				clientSocket.start();
			}

			@Override
			public void onLogOut(String user) {
				MMessage message = new MMessage(MMessage.LOG_OUT, user, "logout", "server");
				clientSocket.sendMessage(message);
				gui.updateViewBegin(true);
				
			}
		});
		
		gui.UpdateArrayListUser();
		gui.updateViewBegin(true);
	}

}
