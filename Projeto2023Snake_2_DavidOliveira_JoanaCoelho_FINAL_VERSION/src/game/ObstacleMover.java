package game;

import environment.*;

public class ObstacleMover extends Thread {
	private Obstacle obstacle;
	private LocalBoard board;

	public ObstacleMover(Obstacle obstacle, LocalBoard board) {
		super();
		this.obstacle = obstacle;
		this.board = board;
	}

	@Override
	public void run() {
		while(!board.getIsFinished() && obstacle.getRemainingMoves() > 0){
			try{
			sleep(obstacle.getMvInterval());
			board.getCell(obstacle.getPos()).removeObstacle();
			BoardPosition pos=board.getRandomPosition();
			
			obstacle.setPos(pos);
			board.getCell(pos).setGameElement(obstacle);
			obstacle.moved();

			board.setChanged();
			} catch	(InterruptedException e) {}
		}
	}

}
