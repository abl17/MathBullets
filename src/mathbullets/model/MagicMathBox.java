package mathbullets.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import mathbullets.gameengine.GameWorld;
import mathbullets.gameengine.Sprite;

/**
 * The purpose of this class is to be used as a sort of weapon
 * @author Austin
 *
 */
public class MagicMathBox extends Sprite {

	private StackPane mathFrame = new StackPane();
	private final Image mathBox;
	private ImageView mathBoxImage;
	private Text mathText = new Text("");
	private static final int maxMagicMathBoxNumber = 999;
	
	/**
	 * MagicMathBox contructor, that sets value and image.
	 */
	public MagicMathBox() {
		mathBox = new Image(getClass().getClassLoader().getResourceAsStream("frame1.png"));
		mathBoxImage = new ImageView(mathBox);
		
		setValue(maxMagicMathBoxNumber + 1);
		setMathText("");
		getMathText().setFont(Font.font("Verdana", 30));
		getMathText().setFill(Color.LIGHTBLUE);
		
		getMathFrame().getChildren().add(mathBoxImage);
		getMathFrame().getChildren().add(getMathText());
		setNode(getMathFrame());
	}
	
	/**
	 * Returns text in the MagicMathBox
	 * @return
	 */
	public Text getMathText() {
		return mathText;
	}
	
	/**
	 * Sets text in the MagicMathBox to 0
	 * @param newMathText
	 */
	public void setMathText(String newMathText) {
		mathText.setText(newMathText);
	}
	
	/**
	 * Obtains the frame of the MagicMathBox
	 * @return
	 */
	public StackPane getMathFrame() {
		return mathFrame;
	}
	
	/**
	 * Sets a frame, or switches one
	 * @param newMathFrame
	 */
	public void setMathFrame(StackPane newMathFrame) {
		mathFrame = newMathFrame;
	}
	
	/**
	 * Updates the value of the Box depending on the text in the box
	 */
	@Override
	public void update() {
		String myMathText = getMathText().getText();

		if(myMathText.length() >= 1) {
			if(Integer.parseInt(myMathText) >= 0 &&
					Integer.parseInt(myMathText) <= maxMagicMathBoxNumber) {
				setValue(Integer.parseInt(myMathText));
			}
		}
	}
	
	/**
	 * Gets maximum possible magic math box number
	 */
	public int getMaxMagicMathBoxNumber() {
		return maxMagicMathBoxNumber;
	}

	/**
	 * Remove from play
	 */
	@Override
	public void implode(GameWorld gameWorld) {
		setDeathStatus(true);
		gameWorld.getSceneNodes().getChildren().remove(getNode());
	}
	
	/**
	 * Handles completely destroying the box
	 */
	@Override
	public void handleDeath(GameWorld gameWorld) {
		setValue(maxMagicMathBoxNumber + 1);
		implode(gameWorld);
		super.handleDeath(gameWorld);
	}

}
