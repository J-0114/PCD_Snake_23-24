package environment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import game.GameElement;
import game.Goal;
import game.HumanSnake;
import game.Obstacle;
import game.Snake;

public abstract class Board extends Observable implements Serializable {
	protected Cell[][] cells;
	private transient BoardPosition goalPosition;
	public transient static final long PLAYER_PLAY_INTERVAL = 100;
	public transient static final long REMOTE_REFRESH_INTERVAL = 200;
	public transient static final int NUM_COLUMNS = 30;
	public transient static final int NUM_ROWS = 30;
	protected LinkedList<Snake> snakes = new LinkedList<Snake>();
	private transient LinkedList<Obstacle> obstacles= new LinkedList<Obstacle>();
	private transient LinkedList<BoardPosition> obstaclesPos= new LinkedList<BoardPosition>();
	protected boolean isFinished;

	public Board() {
		cells = new Cell[NUM_COLUMNS][NUM_ROWS];
		for (int x = 0; x < NUM_COLUMNS; x++) {
			for (int y = 0; y < NUM_ROWS; y++) {
				cells[x][y] = new Cell(new BoardPosition(x, y));
			}
		}

	}
	public void setIsFinished() {
		this.isFinished=true;
	}
	public boolean getIsFinished() {
		return isFinished;
	}

	public Cell getCell(BoardPosition cellCoord) {
		return cells[cellCoord.x][cellCoord.y];
	}

	public BoardPosition getRandomPosition() {
		return new BoardPosition((int) (Math.random() *NUM_ROWS),(int) (Math.random() * NUM_ROWS));
	}

	public BoardPosition getGoalPosition() {
		return goalPosition;
	}

	public void setGoalPosition(BoardPosition goalPosition) {
		this.goalPosition = goalPosition;
	}

	public BoardPosition addGameElement(GameElement gameElement) {
		BoardPosition lastPos=null;
		boolean placed=false;
		while(!placed) {
			BoardPosition pos=getRandomPosition();
			if(!getCell(pos).isOcupied() && !getCell(pos).isOcupiedByGoal()) {
				getCell(pos).setGameElement(gameElement);
				lastPos=pos;
				if(gameElement instanceof Goal) {
					setGoalPosition(pos);
				}
				placed=true;
			}
		}
		return lastPos;
	}

	public List<BoardPosition> getNeighboringPositions(Cell cell) {
		ArrayList<BoardPosition> possibleCells=new ArrayList<BoardPosition>();
		BoardPosition pos=cell.getPosition();
		if(pos.x>0)
			possibleCells.add(pos.getCellLeft());
		if(pos.x<NUM_COLUMNS-1)
			possibleCells.add(pos.getCellRight());
		if(pos.y>0)
			possibleCells.add(pos.getCellAbove());
		if(pos.y<NUM_ROWS-1)
			possibleCells.add(pos.getCellBelow());
		return possibleCells;

	}



	protected Goal addGoal() {
		Goal goal=new Goal(this);
		addGameElement( goal);
		return goal;
	}

	protected void addObstacles(int numberObstacles) {
		// clear obstacle list , necessary when resetting obstacles.
		getObstacles().clear();
		while(numberObstacles>0) {
			Obstacle obs=new Obstacle(this);
			obs.setPos(addGameElement(obs));
			getObstacles().add(obs);
			numberObstacles--;
		}

	}

	public Cell[][] getCells() {
		return cells;
	}

	public LinkedList<Snake> getSnakes() {
		return snakes;
	}


	@Override
	public void setChanged() {
		super.setChanged();
		notifyObservers();
	}

	public LinkedList<Obstacle> getObstacles() {
		return obstacles;
	}


	public abstract void init(); 

	public abstract void handleKeyPress(int keyCode);

	public abstract void handleKeyRelease();

	public int goalReached(){
		return getCell(goalPosition).getGoal().captureGoal();
	}

	public void addSnake(Snake snake) {
		snakes.add(snake);
	}

	public LinkedList<BoardPosition> getObstaclesPos() {
		return obstaclesPos;
	}

	public void updateBoard(Board board){
		cells = board.cells;
		snakes = board.snakes;
	}

	public void addPlayer (int id){
		Snake snakePlayer = new HumanSnake(id,this);
		snakePlayer.start();
		snakes.add(snakePlayer);
	}

	public void comunicateWithHumanSnake (int id, String message){
		for (Snake s : snakes) {
			if(s.getIdentification() == id){
				((HumanSnake)s).updateDirection(message);
			}
		}
	}

}