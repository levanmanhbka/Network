package main;

public interface MIClientGUI {
	public void onConnect(String ip, int port);
	public void onLogIn(String user, String pass, String recipient);
	public void onSignUp(String user, String pass, String recipient);
	public void onSendMessage(String user, String content, String recipient);
	public void onLogOut(String user);
}
