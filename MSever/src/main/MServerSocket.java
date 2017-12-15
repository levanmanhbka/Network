package main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.PseudoColumnUsage;
import java.util.ArrayList;

import javax.jws.soap.SOAPBinding.Use;

public class MServerSocket implements Runnable {
	public static final int numnax = 51;
	private MServerThread clients[];
	private ServerSocket server;
	private Thread thread;
	private int clientCount;
	public MServerGUI gui;
	private MFileIO fileIO;
	private boolean isRunning;
	public ArrayList<MUser> users;
	public ArrayList<String> online;
	public int port;

	public MServerSocket(MServerGUI gui) {
		this.gui = gui;
		this.clients = new MServerThread[numnax];
		this.online = new ArrayList<>();
		this.fileIO = new MFileIO();
		this.users = fileIO.getListUsers();
		this.clientCount = 0;
		this.port = gui.getPort();
		try {
			this.server = new ServerSocket(port);
			gui.setIpAddress("" + InetAddress.getLocalHost());
			gui.setPort("" + server.getLocalPort());
			gui.addStatus("Server started IP : " + InetAddress.getLocalHost() + "\nPort : " + server.getLocalPort());
			System.out.println("[MServerSocket]Server started IP : " + InetAddress.getLocalHost() + ", Port : "
					+ server.getLocalPort());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("[MServerSocket]Can not started server");
		}

	}

	public void start() {
		if (isRunning)
			return;
		isRunning = true;
		if (thread == null)
			thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		while (isRunning) {
			System.out.println("[MServerSocket]Waiting client" + (clientCount + 1));
			gui.addStatus("Waiting client" + (clientCount + 1));
			try {
				addClient(server.accept());
			} catch (IOException e) {
				System.out.println("[MServerSocket]Server accept failed");
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		sendAll(new MMessage(MMessage.STOP_SERVICE, "MSERVER", "server stoped", "all"));
		for (int i = 0; i < clientCount; i++)
			clients[i].close();
		isRunning = false;
	}

	public synchronized void handleMessage(int clientId, MMessage message) {
		System.out.println("[MServerSocket]Handle message: " + message.MessageToJsonString());
		gui.addStatus("[up]["+message.type+"][" + message.sender + "->" + message.recipient + "]  " + message.content);
		if (message.type.equals(MMessage.TEST)) {
			MServerThread serverThread = findClientById(clientId);
			if (serverThread != null)
				serverThread.sendMessage(new MMessage(MMessage.TEST, "MSERVER", "OK", message.sender));
		} else if (message.type.equals(MMessage.LOG_OUT)) {
			removeClient(clientId);
			removeOnlineUser(message.sender);
			sendAll(new MMessage(MMessage.USERS_LIST, "MSERVER", onlineToStringToken(), "all"));
		} else if (message.type.equals(MMessage.LOG_IN)) {
			MServerThread serverThread = findClientById(clientId);
			if (authenticateUser(message.sender, message.content)) {
				online.add(message.sender);
				serverThread.username = message.sender;
				serverThread.sendMessage(new MMessage(MMessage.LOG_IN, "MSERVER", "OK", message.sender));
				sendAll(new MMessage(MMessage.USERS_LIST, "MSERVER", onlineToStringToken(), "all"));
			} else {
				serverThread.sendMessage(new MMessage(MMessage.LOG_IN, "MSERVER", "FAILED", message.sender));
			}

		} else if (message.type.equals(MMessage.SIGN_UP)) {
			MServerThread serverThread = findClientById(clientId);
			if (checkValidSignUp(message.sender, message.content)) {
				online.add(message.sender);
				serverThread.username = message.sender;
				serverThread.sendMessage(new MMessage(MMessage.SIGN_UP, "MSERVER", "OK", message.sender));
				serverThread.sendMessage(new MMessage(MMessage.LOG_IN, "MSERVER", "OK", message.sender));
				sendAll(new MMessage(MMessage.USERS_LIST, "MSERVER", onlineToStringToken(), "all"));
			} else {
				serverThread.sendMessage(new MMessage(MMessage.SIGN_UP, "MSERVER", "FAILED", message.sender));
			}
		} else if (message.type.equals(MMessage.MESSAGE)) {
			MServerThread serverThread = findClientByUser(message.recipient);
			if (serverThread != null)
				serverThread.sendMessage(message);
		} else {
			System.out.println("message error format!");
		}
		
		gui.listUsers = online;
		gui.updateArrayListUser();
	}

	private void addClient(Socket socket) {
		clients[clientCount] = new MServerThread(this, socket);
		clients[clientCount].open();
		clients[clientCount].start();
		if (clientCount == numnax - 1) {
			clients[clientCount].sendMessage(new MMessage(MMessage.TEST, "MSERVER", "FAILED", ""));
			clients[clientCount].close();
		}
		clientCount++;
	}

	private void removeClient(int id) {
		int index = -1;
		for (int i = 0; i < clientCount; i++)
			if (clients[i].id == id)
				index = i;
		if (index != -1) {
			clients[index].close();
			for (int i = index; i < clientCount - 1; i++) {
				clients[i] = clients[i + 1];
			}
			clientCount--;
		}
	}

	private void sendAll(MMessage message) {
		for (int i = 0; i < clientCount; i++)
			clients[i].sendMessage(message);
	}

	private MServerThread findClientById(int id) {
		for (int i = 0; i < clientCount; i++)
			if (clients[i].id == id)
				return clients[i];
		return null;
	}

	private MServerThread findClientByUser(String user) {
		for (int i = 0; i < clientCount; i++)
			if (clients[i].username.equals(user))
				return clients[i];
		return null;
	}

	private boolean authenticateUser(String userName, String password) {
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).name.equals(userName))
				return true;
		}
		return false;
	}

	private boolean checkValidSignUp(String userName, String password) {
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).name.equals(userName))
				return false;
		}
		users.add(new MUser(userName, password));
		fileIO.SaveListUser(users);
		return true;
	}

	private void removeOnlineUser(String user) {
		for (int i = 0; i < online.size(); i++) {
			if (online.get(i).equals(user))
				online.remove(i);
		}
	}

	private String onlineToStringToken() {
		String string = "";
		for (int i = 0; i < online.size(); i++) {
			string += online.get(i);
			if (i != online.size() - 1) 
				string += "+";
		}
		return string;
	}
}
