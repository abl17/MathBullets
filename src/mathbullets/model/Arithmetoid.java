package mathbullets.model;

import java.util.Collections;
import java.util.List;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import mathbullets.gameengine.Sprite;
import mathbullets.util.MathDictionary;

/**
 * This class extends sprite and is used primarily as a baseline for enemy sprites
 * Has health, value, and death/implode conditions.
 * @author Austin
 *
 */
public abstract class Arithmetoid extends Sprite {
	private StackPane enemyFrame = new StackPane();
	private Text mathText = new Text();
	private MathDictionary mathDictionary;
	private int health;
	
	/**
	 * Sets the value to be a random number 
	 * @param targetMathText
	 * @param number
	 */
	public void setRandomOperation(Text targetMathText, int number) {
		List<String> operationList = mathDictionary.getOperationList(number);
		Collections.shuffle(operationList);
		String operationString = operationList.get(0);
		setMathText(operationString);
	}
	
	/**
	 * Gets the text which is displayed on the sprite during the game
	 * @return
	 */
	public Text getMathText() {
		return mathText;
	}
	
	/**
	 * Sets the math text to a new value
	 * @param newMathText
	 */
	public void setMathText(String newMathText) {
		mathText.setText(newMathText);
	}
	
	/**
	 * Obtains the math dictionary for arcade mode
	 * @return
	 */
	public MathDictionary getMathDictionary() {
		return mathDictionary;
	}
	
	/**
	 * Sets math dictionary
	 * @param newMathDictionary
	 */
	public void setMathDictionary(MathDictionary newMathDictionary) {
		mathDictionary = newMathDictionary;
	}
	
	/**
	 * Sets health of the arithmetoid sprite
	 * @param newHealth
	 */
	public void setHealth(int newHealth) {
		health = newHealth;
	}
	
	/**
	 * Gets health of the arithmetoid sprite
	 * @return
	 */
	public int getHealth() {
		return health;
	}
	
	/**
	 * Obtains the section where the enemy display node is indicated
	 * @return
	 */
	public StackPane getEnemyFrame() {
		return enemyFrame;
	}
	
	/**
	 * Sets the enemy frame to be different
	 * @param newEnemyFrame
	 */
	public void setEnemyFrame(StackPane newEnemyFrame) {
		enemyFrame = newEnemyFrame;
	}
	
	/**
	 * Sets new x based on the velocity
	 */
	public void update() {
		getNode().setTranslateX(getNode().getTranslateX() + getXVelocity());
		getNode().setTranslateY(getNode().getTranslateY() + getYVelocity());
	}
}
