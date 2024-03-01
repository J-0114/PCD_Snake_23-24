package game;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import environment.Board;
import environment.BoardPosition;
import environment.LocalBoard;

public class Goal extends GameElement  {
	private int value=1;
	private Board board;
	private final Lock lock = new ReentrantLock();
	public static final int MAX_VALUE=10;
	public Goal( Board board2) {
		this.board = board2;
	}
	
	public int getValue() {
		return value;
	}
	public void incrementValue() throws InterruptedException {
			this.value=value+1;

	}

	public int captureGoal() {
		lock.lock();	
		try{
			incrementValue();
			BoardPosition prev=board.getGoalPosition();
			board.getCell(prev).removeGoal();
			boolean placed=false;
			while(!placed) {
				BoardPosition pos=board.getRandomPosition();
				if(!(board.getCell(pos).isOcupied())) {
					board.getCell(pos).setGameElement(this);
					board.setGoalPosition(pos);
					board.setChanged();
					placed=true;
				}
			}
			if (value>=MAX_VALUE){
				board.setIsFinished();
			}

		}catch(InterruptedException e){}
		finally {lock.unlock();

		}

		return value;
	}
}
