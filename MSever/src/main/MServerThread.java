package main;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MServerThread extends Thread {
	public MServerSocket server;
	public Socket socket;
	public int id;	
	public String username = "";
	public ObjectInputStream streamIn;
	public ObjectOutputStream streamOut;
	public MServerGUI gui;
	public boolean isRunning;

	public MServerThread() {
		id = -1;
	}

	public MServerThread(MServerSocket server, Socket socket) {
		this.server = server;
		this.socket = socket;
		this.id = socket.getPort();
		this.gui = server.gui;
	}
	/**
	 * Initial before open
	 */
	public void open() {
		try {
			streamIn = new ObjectInputStream(socket.getInputStream());
			streamOut = new ObjectOutputStream(socket.getOutputStream());
			streamOut.flush();
			isRunning = true;
		} catch (IOException e) {
			isRunning = false;
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while (isRunning) {
			try {
				String content = (String) streamIn.readObject();
				MMessage message = new MMessage(content);
				server.handleMessage(id, message);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		super.run();
	}

	public void sendMessage(MMessage message) {
		try {
			gui.addStatus("[down]["+message.type+"][" + message.sender + "->" + message.recipient + "]  " + message.content);
			streamOut.writeObject(message.MessageToJsonString());
			streamOut.flush();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void close() {
		isRunning = false;
		try {
			if (socket != null)
				socket.close();
			if (streamIn != null)
				streamIn.close();
			if (streamOut != null)
				streamOut.close();
		} catch (Exception e) {
		}
	}
}
