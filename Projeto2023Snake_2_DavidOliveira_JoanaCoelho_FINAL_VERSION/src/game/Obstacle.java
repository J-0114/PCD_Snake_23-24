package game;

import environment.*;

public class Obstacle extends GameElement {
	
	
	private static final int NUM_MOVES=3;
	private static final int OBSTACLE_MOVE_INTERVAL = 400;
	private int remainingMoves=NUM_MOVES;
	private Board board;
	private BoardPosition pos;
	private Cell cell;

	public Obstacle(Board board) {
		this.board = board;
	}
	
	public int getRemainingMoves() {
		return remainingMoves;
	}
	public void moved(){
		remainingMoves--;
	}

	public int getMvInterval(){
		return OBSTACLE_MOVE_INTERVAL;
	}

	public BoardPosition getPos() {
		return pos;
	}

	public void setPos(BoardPosition pos) {
		this.pos = pos;
	}

	public void setCell(Cell cell){
		this.cell = cell;
	}

	// public void releaseCell(){
	// 	cell.removeObstacle();
	// 	cell.release();
	// }
	
}
