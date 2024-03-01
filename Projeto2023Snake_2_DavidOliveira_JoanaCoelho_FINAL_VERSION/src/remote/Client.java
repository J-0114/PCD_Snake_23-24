package remote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import environment.Board;
import environment.LocalBoard;
import gui.SnakeGui;

/**
 * Remore client, only for part II
 * 
 * @author luismota
 *
 */

public class Client {
	private ObjectInputStream input;
	private PrintWriter output;
	public String server; // endereco da aplicacao principal
	public RemoteBoard board;

	public static void main(String[] args) {
		new Client().start(8000);
	}

	public void start(int PORTO) {
		board = new RemoteBoard(this);
		SnakeGui game = new SnakeGui(board, 600, 0);
		game.init();

		try {
			// Estabelece conexão com o servidor
			InetAddress serverAdress = InetAddress.getByName(null);
			Socket server = new Socket(serverAdress, PORTO);

			input = new ObjectInputStream(server.getInputStream());
			output = new PrintWriter(server.getOutputStream(), true);

			// Receive board
			serverConnection();

			// close socket
			server.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void serverConnection() {
		while (true) {
			try {
				Board b = (Board) input.readObject();
				board.updateBoard(b);
				board.setChanged();
			} catch (ClassNotFoundException e) {
				System.out.println("Received not serializable");
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("Error receiving board");
				e.printStackTrace();
			}
		}
	}

	// Envia a tecla pressionada para o servidor
	public void sendToServer (String keyPressed){
		output.println(keyPressed);
	}

}