package packageClient;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import InterfaceUI.ChatPanel;
import services.OutputThread;

import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Client extends JFrame {

	private JPanel contentPane;
	private JTextField txtStaff;
	private JTextField txtServerIP;
	private JTextField txtServerPort;

	Socket mngSocket = null; // Tomada de controle (SOCKET)
	String mngIP = ""; // Manager IP
	int mngPort = 0; // Manager Port
	String staffName = ""; // Nome do usuário
	BufferedReader bf = null; // Entrada buffer
	DataOutputStream os = null; // Saída buffer
	// Thread: permite apresentar dados recebidos automaticamente
	OutputThread t = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client frame = new Client();
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
	public Client() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		panel.setBorder(
				new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Informacoes do usuario e servidor.",
						TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(1, 7, 0, 0));

		JLabel lblNewLabel = new JLabel("Usu\u00E1rio:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblNewLabel);

		txtStaff = new JTextField();
		panel.add(txtStaff);
		txtStaff.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("IP:");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblNewLabel_1);

		txtServerIP = new JTextField();
		panel.add(txtServerIP);
		txtServerIP.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Porta:");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblNewLabel_2);

		txtServerPort = new JTextField();
		panel.add(txtServerPort);
		txtServerPort.setColumns(10);

		JFrame thisFrame = this;

		JButton btnConnect = new JButton("Conectar");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mngIP = txtServerIP.getText(); // Pega o IP do servidor e a porta do servidor
				mngPort = Integer.parseInt(txtServerPort.getText());
				staffName = txtStaff.getText();
				try {
					mngSocket = new Socket(mngIP, mngPort); // Conecta com o servidor
					if (mngSocket != null) { // Se a conexão for bem-sucedida
						// crie o componente de bate-papo e adicione ao GUI
						ChatPanel p = new ChatPanel(mngSocket, staffName, "Server");
						thisFrame.getContentPane().add(p);
						p.getTxtMensagens().append("Conexão estabelecida!");
						p.updateUI();
						// Get the socket input stream and output stream
						bf = new BufferedReader(new InputStreamReader(mngSocket.getInputStream()));
						os = new DataOutputStream(mngSocket.getOutputStream());

						// Anuncia ao servidor
						os.writeBytes("Usuário: " + staffName);
						os.write(13);
						os.write(10);
						os.flush();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		panel.add(btnConnect);
	}

}
