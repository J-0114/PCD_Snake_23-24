package game;

import environment.Board;
import environment.BoardPosition;
import remote.*;
/** Class for a remote snake, controlled by a human 
 * 
 * @author luismota
 *
 */

public class HumanSnake extends Snake {

	public String direction;

	public HumanSnake(int id, Board board) {
		super(id, board);
	}

	public void handleKeyPress(String s) {
		BoardPosition current = cells.getLast().getPosition();
		BoardPosition next = null;
		if (s == null) {
			System.err.println("Invalid command");
			return;
		}
		switch (s) {
			case "Above":
				next = current.getCellAbove();
				break;
			case "Below":
				next = current.getCellBelow();
				break;
			case "Left":
				next = current.getCellLeft();
				break;
			case "Right":
				next = current.getCellRight();
				break;
		}
		if(next != null && next.isValid() && !getBoard().getCell(next).isOcupied()){
			try {
				move(getBoard().getCell(next));
			} catch (InterruptedException e) {
				System.out.println("Invalid move");
				e.printStackTrace();
			}
		}	
	}

	@Override
	public void run() {
		doInitialPositioning();
		System.err.println("initial size:" + cells.size());
		cells.getLast();
		while (true) {
			try {
				handleKeyPress(direction);
				// Adjust the sleep duration according to your game's needs
				sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateDirection (String dir){
		direction = dir;
	}

}
