package services;
// Thread apresenta mensagens recebidas automaticamente //

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class OutputThread extends Thread {
	Socket socket; // socket comunicação
	JTextArea txt; // área de texto que vai conter a mensagem comunicada
	BufferedReader bf; //  buffer colocado do socket
	String sender; // remetente
	String receiver; // receptor

	public OutputThread(Socket socket, JTextArea txt, String sender, String receiver) {
		super();
		this.socket = socket;
		this.txt = txt;
		this.sender = sender;
		this.receiver = receiver;

		try {
			bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// obtém dados do fluxo de entrada periodicamente (1 vez / segundos)
	// o momento em que os dados chegam não pode ser conhecido com antecedência
	
	public void run() {
		while (true)
			try {
				if (socket != null) {
					String msg = ""; // obtém os dados do fluxo de entrada

					if ((msg = bf.readLine()) != null && msg.length() > 0) {
						txt.append("\n" + receiver + ": " + msg);
					}

					sleep(1000);
				}
			} catch (Exception e) {
			}
	}
}
