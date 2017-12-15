package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;

public class MClientSocket implements Runnable {
	private Thread thread;
	public int port;
	public String serverAddr;
	public Socket socket;
	public MClientGUI gui;
	public ObjectInputStream streamIn;
	public ObjectOutputStream streamOut;
	public MFileIO fileIO;
	public boolean isRunning;
	private boolean isConnect;

	public MClientSocket(MClientGUI gui) {
		this.gui = gui;
		this.isRunning = false;
		this.isConnect = false;
	}

	@Override
	public void run() {
		while (isRunning) {
			try {
				String content = (String) streamIn.readObject();
				MMessage message = new MMessage(content);
				handleMessage(message);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	/**
	 * Initial before open
	 */
	public void open(String serverAddress, int port) {
		if (isConnect)
			return;
		this.serverAddr = serverAddress;
		this.port = port;
		try {
			socket = new Socket(InetAddress.getByName(serverAddress), port);
			streamOut = new ObjectOutputStream(socket.getOutputStream());
			streamOut.flush();
			streamIn = new ObjectInputStream(socket.getInputStream());
			isConnect = true;
		} catch (Exception e) {
			isConnect = false;
			System.out.println("[MClient] connect failed");
		}
		sendMessage(new MMessage(MMessage.TEST, "new client", "hello", "server"));
	}

	/**
	 * open before start
	 */
	public void start() {
		if (isRunning)
			return;
		isRunning = true;
		if (thread == null)
			thread = new Thread(this);
		thread.start();
	}

	/**
	 * close when do not use anymore
	 */
	public void close() {
		isRunning = false;
		thread = null;
		try {
			if (socket != null)
				socket.close();
			if (streamIn != null)
				streamIn.close();
			if (streamOut != null)
				streamOut.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void sendMessage(MMessage message) {
		if (streamOut != null)
			try {
				gui.addStatus("[" + message.type + "][" + message.sender + "->" + message.recipient + "]  "
						+ message.content);
				streamOut.writeObject(message.MessageToJsonString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public synchronized void handleMessage(MMessage message) {
		System.out.println("[" + gui.userName + "]Handle message: " + message.MessageToJsonString());
		gui.addStatus("[" + message.type + "][" + message.sender + "->" + message.recipient + "]  " + message.content);
		if (message.type.equals(MMessage.LOG_IN)) {
			if (message.content.equals("OK"))
				gui.updateViewBegin(false);
		} else if (message.type.equals(MMessage.USERS_LIST)) {
			handleUserListMessage(message);
		} else if (message.type.equals(MMessage.STOP_SERVICE)) {
			close();
			gui.updateViewBegin(true);
		}
	}

	private void handleUserListMessage(MMessage message) {
		gui.listUsers.clear();
		StringTokenizer tokenizer = new StringTokenizer(message.content, "+");
		while (tokenizer.hasMoreElements()) {
			String object = (String) tokenizer.nextElement();
			gui.listUsers.add(object);
		}
		gui.UpdateArrayListUser();

	}
}
