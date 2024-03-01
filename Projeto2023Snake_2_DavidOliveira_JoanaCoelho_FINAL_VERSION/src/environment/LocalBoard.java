package environment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import game.GameElement;
import game.Goal;
import game.Obstacle;
import game.ObstacleMover;
import game.Server;
import game.Snake;
import game.AutomaticSnake;

/** Class representing the state of a game running locally
 * 
 * @author luismota
 *
 */
public class LocalBoard extends Board{
	
	public transient static final int NUM_SNAKES = 2;
	private transient static final int NUM_OBSTACLES = 25;
	private transient static final int NUM_SIMULTANEOUS_MOVING_OBSTACLES = 3;
	public transient Goal goal;

	

	public LocalBoard() {
		
		for (int i = 0; i < NUM_SNAKES; i++) {
			AutomaticSnake snake = new AutomaticSnake(i, this);
			snakes.add(snake);
		}

		addObstacles( NUM_OBSTACLES);
		
		Goal goal=addGoal();
		this.goal=goal;
	}

	public void init() {
		ExecutorService executor = Executors.newFixedThreadPool(NUM_SIMULTANEOUS_MOVING_OBSTACLES);
		for(Snake s:snakes)
			s.start();
		
		for(Obstacle o:getObstacles()){
			ObstacleMover m=new ObstacleMover(o,this);
			executor.execute(m);
		}
		executor.shutdown();
		setChanged();
		
	}



	@Override
	public void handleKeyPress(int keyCode) {
		// do nothing... No keys relevant in local game
	}

	@Override
	public void handleKeyRelease() {
		// do nothing... No keys relevant in local game
	}





}
