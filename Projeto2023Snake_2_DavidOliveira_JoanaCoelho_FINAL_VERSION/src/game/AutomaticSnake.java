package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.text.Position;

import environment.LocalBoard;
import gui.SnakeGui;
import environment.Cell;
import environment.Board;
import environment.BoardPosition;

public class AutomaticSnake extends Snake {
	private transient Board board;

	public AutomaticSnake(int id, LocalBoard board) {
		super(id, board);
		this.board = board;
	}

	@Override
	public void run() {

		doInitialPositioning();
		System.err.println("initial size:" + cells.size());

		// Movimento Automático
		while (!board.getIsFinished()) {
			try {
				sleep(Board.PLAYER_PLAY_INTERVAL);
				moveToGoal();
			} catch (InterruptedException e) {
				handleInterruptedException();
			}
		}
	}

	void moveToGoal() throws InterruptedException {
		double[] distances = new double[] {
				goalDistance(direction("right")),
				goalDistance(direction("left")),
				goalDistance(direction("above")),
				goalDistance(direction("below"))
		};

		double min = distances[0];
		int nextMove = -1;

		for (int i = 0; i < distances.length; i++) {
			if (distances[i] <= min && isValidMove(i)){
				min = distances[i];
				nextMove = i;
			}
		}

		if (nextMove != -1) {
			move(board.getCell(getNextBoardPosition(nextMove)));
		}

	}

	BoardPosition getNextBoardPosition(int nextMove) {
		BoardPosition lastPos = getCells().getLast().getPosition();
		switch (nextMove) {
			case 0:
				return lastPos.getCellRight();
			case 1:
				return lastPos.getCellLeft();
			case 2:
				return lastPos.getCellAbove();
			case 3:
				return lastPos.getCellBelow();
			default:
				throw new IllegalArgumentException();
		}
	}

	boolean isValidMove(int i) {
		BoardPosition nextPosition = getNextBoardPosition(i);
		return nextPosition.isValid() && board.getCell(nextPosition).getOcuppyingSnake() != this;
	}

	public BoardPosition direction(String x) {
		BoardPosition position = this.getCells().getLast().getPosition();

		switch (x) {
			case "right":
				return position.getCellRight();
			case "left":
				return position.getCellLeft();
			case "above":
				return position.getCellAbove();
			case "below":
				return position.getCellBelow();
		}

		throw new IllegalArgumentException();
	}

	public double goalDistance(BoardPosition p) {
		return p.distanceTo(board.getGoalPosition());
	}

	void handleInterruptedException() {
		int nextMove = -1;

		for (int i = 0; i < 11 ; i++) {
			int r = (int) (Math.random()*4);
			if(isValidMove(r)){
				nextMove = r;
				break;
			}
		}

		if (nextMove != -1) {
			try {
				move(board.getCell(getNextBoardPosition(nextMove)));
			} catch (InterruptedException e) {
				handleInterruptedException();
			}
		}
	}
}
