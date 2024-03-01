package environment;

import java.io.Serializable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.sound.midi.SysexMessage;
import javax.xml.bind.NotIdentifiableEvent;

import game.GameElement;
import game.Goal;
import game.Obstacle;
import game.Snake;
/** Main class for game representation. 
 * 
 * @author luismota
 *
 */
public class Cell implements Serializable {
	private BoardPosition position;
	private Snake ocuppyingSnake = null;
	private GameElement gameElement=null;
	
	private transient Lock lock = new ReentrantLock();
	private transient Condition condition = lock.newCondition();

	
	public GameElement getGameElement() {
		return gameElement;
	}


	public Cell(BoardPosition position) {
		super();
		this.position = position;
	}

	public BoardPosition getPosition() {
		return position;
	}
	public void setPosition(BoardPosition position) {
		this.position=position;
	}

	public void request(Snake snake) throws InterruptedException {
		lock.lock();
		try {
			while(isOcupied()){
				condition.await();
			}
			ocuppyingSnake=snake;
		
		}finally{
			lock.unlock();
		}
	}

	public void release() {
		lock.lock();
		try{
			ocuppyingSnake = null;
			gameElement = null;
			condition.signalAll();
		}finally{
			lock.unlock();
		}
	}

	public boolean isOcupiedBySnake() {
		return ocuppyingSnake!=null;
	}


	public  void setGameElement(GameElement element) {
		lock.lock();
		try {
			while (isOcupied() || isOcupiedByGoal()) {
				condition.await();
			}
			gameElement=element;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
	}
	
	public boolean isOcupied() {
		return isOcupiedBySnake() || (gameElement!=null && gameElement instanceof Obstacle);
	}

	public Snake getOcuppyingSnake() {
		return ocuppyingSnake;
	}


	public Goal removeGoal() {
		lock.lock();
		try {
			if(gameElement != null && gameElement instanceof Goal){
				Goal g = (Goal) gameElement;
				this.gameElement=null;
				return g;
			}else{
				return null;
			}
		}finally{
			lock.unlock();
		}
	}

	public void removeObstacle() {
		lock.lock();
		try {
			if(gameElement != null && gameElement instanceof Obstacle){
				this.gameElement=null;
				condition.signalAll();
			}
		}finally{
			lock.unlock();
		}
	}


	public Goal getGoal() {
		return (Goal)gameElement;
	}

	public boolean isOcupiedByGoal() {
		return (gameElement!=null && gameElement instanceof Goal);
	}
	
	

}
