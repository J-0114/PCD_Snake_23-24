package remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import game.Goal;
import game.Obstacle;
import game.ObstacleMover;
import game.Snake;

import java.awt.event.KeyEvent;

public class RemoteBoard extends Board {
	private Client client;

	public RemoteBoard(Client client) {
		super();
		init();
		this.client = client;
	}
	
	public void handleKeyPress(int keyCode) {
		String move = "";
		// Implementa a lógica para lidar com as teclas pressionadas
		switch(keyCode){
		case KeyEvent.VK_DOWN:
			move="Below";
			break;
		case KeyEvent.VK_UP:
			move="Above";
			break;
		case KeyEvent.VK_LEFT:
			move="Left";
			break;
		case KeyEvent.VK_RIGHT:
			move="Right";
			break;
		}
		client.sendToServer(move);
	}

	@Override
	public void handleKeyRelease() {
		client.sendToServer("Don't");
	}
	
	@Override
	public void init() {
		
		for(Snake s:snakes)
			s.start();

	}
	

}


