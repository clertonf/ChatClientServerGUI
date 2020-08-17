package InterfaceUI;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.border.TitledBorder;

import services.OutputThread;

import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ChatPanel extends JPanel {

	Socket socket = null;
	BufferedReader bf = null;
	DataOutputStream os = null;
	OutputThread t = null; //
	String sender; // remetente
	String receiver; // receptor
	JTextArea txtMensagens;

	/**
	 * Create the panel.
	 */
	public ChatPanel(Socket s, String sender, String receiver) {
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Mensagem", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new GridLayout(1, 2, 0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);

		JTextArea txtMensagem = new JTextArea();
		scrollPane.setViewportView(txtMensagem);

		JButton btnSend = new JButton("Enviar");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtMensagem.getText().trim().length() == 0) 
					return;
					
				try {
					os.writeBytes(txtMensagem.getText());
					os.write(13);
					os.write(10);
					os.flush(); //
					txtMensagens.append("\n" + sender + ": " + txtMensagem.getText());
					txtMensagem.setText("");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		panel.add(btnSend);

		JButton btnExit = new JButton("Sair");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		panel.add(btnExit);

		JScrollPane scrollPane_1 = new JScrollPane();
		add(scrollPane_1, BorderLayout.CENTER);

		txtMensagens = new JTextArea();
		scrollPane_1.setViewportView(txtMensagens);

		socket = s;
		this.sender = sender;
		this.receiver = receiver;
		try {
			// Buffer de entrada e buffer de saída
			bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			os = new DataOutputStream(socket.getOutputStream());
			t = new OutputThread(s, txtMensagens, sender, receiver);
			t.start();
		} catch (Exception e) {

		}

	}

	public JTextArea getTxtMensagens() {
		return this.txtMensagens;
	}
}
