package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JList;

public class MClientGUI extends JFrame {

	private JPanel contentPane;

	private MIClientGUI owner;
	private JTextField textIP;
	private JTextField textPort;
	private JTextField textUser;
	private JTextField textPassword;
	private JTextField textMessage;
	private JTextField textRecipient;
	private JTextPane textStatus;

	private JButton btnConnect;
	private JButton btnLogin;
	private JButton btnSignUp;
	private JButton btnSend;

	public String recipient = "";
	public String userName;
	public ArrayList<String> listUsers;
	public String status;
	private JList<String> listClients;

	public void setOwner(MIClientGUI owner) {
		this.owner = owner;
	}

	public void addStatus(String status) {
		this.status += "\n" + status;
		textStatus.setText(this.status);
	}

	public void updateViewBegin(boolean isBegin) {
		btnSend.setEnabled(!isBegin);
		textMessage.setEnabled(!isBegin);
		btnConnect.setEnabled(isBegin);
		textPort.setEnabled(isBegin);
		textIP.setEditable(isBegin);
		btnLogin.setEnabled(isBegin);
		btnSignUp.setEnabled(isBegin);
		textUser.setEnabled(isBegin);
		textPassword.setEnabled(isBegin);
	}

	public void updateViewConnected() {
		btnConnect.setEnabled(false);
	}

	public void updateViewLogined() {

	}

	public void UpdateArrayListUser() {
		DefaultListModel model = new DefaultListModel<>();
		for (int i = 0; i < listUsers.size(); i++) {
			model.addElement(listUsers.get(i));
		}
		listClients.setModel(model);
	}

	/**
	 * Create the frame.
	 */
	public MClientGUI() {
		setTitle("MClient");
		listUsers = new ArrayList<>();
		status = "";
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, -25, 559, 426);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		Object columns[] = { "All" };
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(49, 184, 320, 137);
		contentPane.add(scrollPane);

		textStatus = new JTextPane();
		scrollPane.setViewportView(textStatus);

		JLabel lblServer = new JLabel("Server");
		lblServer.setBounds(49, 32, 60, 14);
		contentPane.add(lblServer);

		textIP = new JTextField();
		textIP.setBounds(119, 29, 110, 20);
		contentPane.add(textIP);
		textIP.setColumns(10);
		textIP.setText("localhost");

		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(250, 32, 46, 14);
		contentPane.add(lblPort);

		textPort = new JTextField();
		textPort.setBounds(283, 29, 86, 20);
		contentPane.add(textPort);
		textPort.setColumns(10);
		textPort.setText("10000");

		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				owner.onConnect(textIP.getText(), Integer.parseInt(textPort.getText()));
			}
		});
		btnConnect.setBounds(398, 28, 110, 23);
		contentPane.add(btnConnect);

		JLabel lblUser = new JLabel("User");
		lblUser.setBounds(49, 74, 60, 14);
		contentPane.add(lblUser);

		textUser = new JTextField();
		textUser.setBounds(119, 71, 250, 20);
		contentPane.add(textUser);
		textUser.setColumns(10);
		textUser.setText("root");

		btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				owner.onLogIn(textUser.getText(), textPassword.getText(), textRecipient.getText());
			}
		});
		btnLogin.setBounds(398, 65, 110, 23);
		contentPane.add(btnLogin);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(49, 108, 60, 14);
		contentPane.add(lblPassword);

		textPassword = new JTextField();
		textPassword.setBounds(119, 102, 250, 20);
		contentPane.add(textPassword);
		textPassword.setColumns(10);
		textPassword.setText("admin");

		btnSignUp = new JButton("SignUp");
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				owner.onSignUp(textUser.getText(), textPassword.getText(), textRecipient.getText());
			}
		});
		btnSignUp.setBounds(398, 99, 110, 23);
		contentPane.add(btnSignUp);

		textMessage = new JTextField();
		textMessage.setBounds(48, 345, 321, 20);
		contentPane.add(textMessage);
		textMessage.setColumns(10);

		btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				owner.onSendMessage(textUser.getText(), textMessage.getText(), textRecipient.getText());
			}
		});
		btnSend.setBounds(398, 344, 116, 23);
		contentPane.add(btnSend);

		textRecipient = new JTextField();
		textRecipient.setEnabled(false);
		textRecipient.setBounds(119, 153, 250, 20);
		contentPane.add(textRecipient);
		textRecipient.setColumns(10);
		textRecipient.setText("server");

		JLabel lblRecipient = new JLabel("Recipient");
		lblRecipient.setBounds(49, 156, 60, 14);
		contentPane.add(lblRecipient);

		listClients = new JList();
		listClients.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int i = listClients.getSelectedIndex();
				if(i>=0) {
					textRecipient.setText(listUsers.get(i));
				}
			}
		});
		listClients.setBounds(398, 184, 116, 137);
		contentPane.add(listClients);
		
		JButton btnLogout = new JButton("LogOut");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				owner.onLogOut(textUser.getText());
			}
		});
		btnLogout.setBounds(398, 133, 110, 23);
		contentPane.add(btnLogout);
		setLocationRelativeTo(null);
	}
}
