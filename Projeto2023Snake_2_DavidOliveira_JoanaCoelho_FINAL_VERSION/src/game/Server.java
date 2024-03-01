package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import environment.Board;
import environment.LocalBoard;
import remote.RemoteBoard;

public class Server {
	public static int PORTO = 8000;
	public static LocalBoard board;
	public final int  SNAKE_ID_OF_SET = 10000;
	public int numRemotePlayers = 0;

	public Server(int PORTO, LocalBoard board) {
		Server.PORTO = PORTO;
		Server.board = board;
	}

	public static void main(String[] args) throws InterruptedException {
		try {
			new Server(PORTO, board).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() throws IOException, InterruptedException {
		ServerSocket server = new ServerSocket(PORTO);
		//String serverIp = InetAddress.getLocalHost().getHostAddress();

		try {
			// Accept various clients simultaneously
			while (!(board.getIsFinished())) {

				Socket socket = server.accept();

				new DealWithClient(socket, board, numRemotePlayers+SNAKE_ID_OF_SET).start();

				new Receiver(socket,numRemotePlayers+SNAKE_ID_OF_SET).start();

				numRemotePlayers++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		server.close();
	}

	public class DealWithClient extends Thread{
		public ObjectOutputStream output; // Canal de Objetos
		public LocalBoard localBoard;

		public DealWithClient(Socket socket, LocalBoard localBoard, int id) throws IOException {
			output = new ObjectOutputStream(socket.getOutputStream());
			this.localBoard = localBoard;
			localBoard.addPlayer(id);
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();

			while (!(board.getIsFinished())) {
				
				try{
					output.writeObject(board);
					output.reset();

					sleep(Board.REMOTE_REFRESH_INTERVAL);
				}
				catch (InterruptedException e){
					System.out.println("Thread interrupted");
					break;
				}
				catch(IOException e){
					System.out.println(" I/O error has occurred");
					break;
				}
			}

			try {
				output.close();
			} catch (IOException e) {
				System.out.println("Couldn't close output stream");
				e.printStackTrace();
			}
		}
	}

	public class Receiver extends Thread{
		private BufferedReader input; // Canal de Texto
		public int clientSnakeId;
		
		public Receiver (Socket socket, int id) throws IOException{
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			clientSnakeId = id;
		}

		@Override
		public void run() {
			super.run();

			try {
				while (true) {
					board.comunicateWithHumanSnake(clientSnakeId, input.readLine());
				}
			} catch (IOException e) {
				System.out.println("Couldn't read Client input");
				e.printStackTrace();
			}

			try {
				input.close();
			} catch (IOException e) {
				System.out.println("Couldn't close input stream");
				e.printStackTrace();
			}
		}
	}
}