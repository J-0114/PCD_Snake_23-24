package gui;

import java.io.Console;
import java.io.IOException;

import javax.net.ssl.StandardConstants;

import environment.LocalBoard;
import game.Server;

public class Main {
	public static void main(String[] args) throws IOException, InterruptedException {
		LocalBoard board=new LocalBoard();
		SnakeGui game = new SnakeGui(board,600,0);
		game.init();
		
		// Launch server
		Server server = new Server(8000, board);
        server.start();
		
	}
}
