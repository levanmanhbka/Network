package main;

import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JList;

public class MServerGUI extends JFrame {

	private JPanel contentPane;
	private JTextField textIP;
	private JTextField textPort;
	private JTextPane textStatus;
	private JList listUser;
	
	private String stringStatus;
	private MIServerGUI onwer;

	public ArrayList<String> listUsers;
	private JScrollPane scrollPane;
	
	public void setOnwer(MIServerGUI onwer) {
		this.onwer = onwer;
	}

	public void addStatus(String status) {
		stringStatus += status + "\n";
		textStatus.setText(stringStatus);
	}
	
	public void setIpAddress(String ip) {
		textIP.setText(ip);
	}
	
	public void setPort(String port) {
		textPort.setText(port);
	}
	
	public int getPort() {
		return Integer.parseInt(textPort.getText());
	}
	
	public void updateArrayListUser() {
		DefaultListModel model = new DefaultListModel<>();
		for (int i = 0; i < listUsers.size(); i++) {
			model.addElement(listUsers.get(i));
		}
		listUser.setModel(model);
		
	}
	/**
	 * Create the frame.
	 */
	public MServerGUI() {
		listUsers = new ArrayList<>();
		stringStatus = "";
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 519, 342);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblIp = new JLabel("IP");
		lblIp.setBounds(30, 28, 46, 14);
		contentPane.add(lblIp);
		
		textIP = new JTextField();
		textIP.setEnabled(false);
		textIP.setBounds(62, 25, 110, 20);
		contentPane.add(textIP);
		textIP.setColumns(10);
		
		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(204, 28, 46, 14);
		contentPane.add(lblPort);
		
		textPort = new JTextField();
		textPort.setBounds(231, 25, 56, 20);
		contentPane.add(textPort);
		textPort.setColumns(10);
		textPort.setText("10000");
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onwer.onStartServer();
			}
		});
		btnStart.setBounds(355, 24, 103, 23);
		contentPane.add(btnStart);
		
		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onwer.onStopServer();
			}
		});
		
		btnStop.setBounds(355, 58, 103, 23);
		contentPane.add(btnStop);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 102, 303, 174);
		contentPane.add(scrollPane);
		
		textStatus = new JTextPane();
		textStatus.setEditable(false);
		scrollPane.setViewportView(textStatus);
		
		listUser = new JList();
		listUser.setBounds(355, 102, 103, 174);
		contentPane.add(listUser);
		Object columns[] = { "All" };
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);
		setLocationRelativeTo(null);
	}
}
