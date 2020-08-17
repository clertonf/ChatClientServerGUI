package packageServer;



import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import InterfaceUI.ChatPanel;

import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTabbedPane;

public class Server extends JFrame implements Runnable {

	private JPanel contentPane;
	private JTextField txtServerPort;

	ServerSocket srvSocket = null;
	BufferedReader bf = null;
	Thread t; // thread para explorar as conexões de usuários
	private JTabbedPane tabbedPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server frame = new Server();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Server() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(1, 2, 0, 0));

		JLabel lblNewLabel = new JLabel("Servidor Porta:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblNewLabel);

		txtServerPort = new JTextField();
		txtServerPort.setText("12340");
		panel.add(txtServerPort);
		txtServerPort.setColumns(10);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		this.setSize(600, 300);
		int serverPort = Integer.parseInt(txtServerPort.getText());
		try {
			srvSocket = new ServerSocket(serverPort);
		} catch (Exception e) {
			e.printStackTrace();
		}
		t = new Thread(this);
		t.start();

	}

	public void run() {
		ChatPanel p = null;
		String staffName = null;
		
		while (true) {
			try { // esperar por um cliente
				Socket aStaffSocket = srvSocket.accept();
				if (aStaffSocket != null) { // se houver a conexão
					// Pega o usuário
					//
					bf = new BufferedReader(new InputStreamReader(aStaffSocket.getInputStream()));
					String S = bf.readLine();
					int pos = S.indexOf(":");
					staffName = S.substring(pos + 1);

					// Create a tab for this connection
					p = new ChatPanel(aStaffSocket, "Server ", staffName);
					tabbedPane.add(staffName, p);
					p.getTxtMensagens().append("Conexão estabelecida!");
					p.updateUI();

				}
				if(srvSocket.isClosed()) {
					p.getTxtMensagens().append(staffName + "");
					p.updateUI();
				}
				// faz com que a thread atual pare de executar por um intervalo de tempo
				// pré-determinado
				
				Thread.sleep(1000);
				
			} catch (Exception e) {
//				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "O Servidor não está rodando");
				System.exit(0);
			}
		}
	}
}
