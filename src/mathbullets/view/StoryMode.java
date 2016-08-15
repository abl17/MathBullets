// This entire file is part of my masterpiece.
// Austin Liu

package mathbullets.view;

import javafx.animation.Animation.Status;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import mathbullets.gameengine.Sprite;
import mathbullets.model.Arithmetoid;
import mathbullets.model.Gaussianoid;
import mathbullets.model.MagicMathBox;

/**
 * The purpose of this class is to start the StoryMode game section 
 * @author Austin
 *
 */
public class StoryMode extends SideScrollGameMode {
	/**
	 * Variables
	 */
	private Gaussianoid myBoss;
	private MagicMathBox myMagicMathBox;
	private String myMagicMathBoxText;
	
	private static final Label BOSS_HEALTH = new Label("Boss Health: ");
	private Label bossHealth = new Label();
	private HBox bossHealthHBox;
	
	private static final int maxBoxLength = 3;

	/**
	 * Constructor
	 * @param fps
	 * @param title
	 */
	public StoryMode(int fps, String title) {
		super(fps, title);
	}
	
	/**
	 * Sets initial level stats
	 */
	protected void setInitialLevelStats() {
		averageInitialVy = 0.5;
		averageInitialGenerationSpeed = 200;
		initialGoal = 5;
		initialLevel = 1;

		averageVy = 0.5;
		averageGenerationSpeed = 200;
		goal = 5;
		level = 1;
	}
	
	/**
	 * Set Next levels
	 */
	protected void setLevelUpStats() {
		goalIncreaseRate = 5;
		averageVyIncreaseRate = 0.01;
		averageGenerationSpeedIncreaseRate = 1;
	}

	/**
	 * Initializes the sprites before the loop starts
	 */
	@Override
	public void initialize(final Stage primaryStage) {
		super.initialize(primaryStage);
		setLevel(initialLevel);
		initializeHero();
		initializeBoss();
		initializeMagicMathBox();
	}

	/**
	 * Updates the sprites, and adds more potentially
	 */
	@Override
	protected void updateSprites() {
		myBoss.moveRandomly();
		shootNewtonoid();
		updateSpeedsBasedOnLevel();
		super.updateSprites();
	}

	/**
	 * Updates dynamics stats in corner
	 */
	@Override
	protected void updateStats() {
		super.updateStats();
		bossHealth.setText(Integer.toString(myBoss.getHealth()));
	}

	/**
	 * Check win/lose conditions
	 */
	@Override
	protected void checkAdvancements() {
		super.checkAdvancements();
		if (myBoss.getDeathStatus() && myHero.getHealth() > 0) {
			shutdown();
			initializeWinScreen();
		}
	}
	
	/**
	 * Every Frame, check to see if speed changes based on level
	 */
	@Override
	public void updateSpeedsBasedOnLevel() {
		super.updateSpeedsBasedOnLevel();
		myBoss.setShootingSpeed(averageVy);
	}
	
	/**
	 * Gaussian shoots a newtonoid randomly
	 */
	public void shootNewtonoid() {
		int number = (int) Math.floor(Math.random()*averageGenerationSpeed);
		if (number == 0 && !myBoss.getDeathStatus())
			addSpriteToSpriteManagerAndScene(myBoss.fire());
	}
	
	/**
	 * Modifies the colors of the text
	 */
	@Override
	protected void modifyLabelColors() {
		super.modifyLabelColors();
		BOSS_HEALTH.setTextFill(Color.WHITE);
		bossHealth.setTextFill(Color.PURPLE);
	}
	
	/**
	 * Create RHS stats for the first time
	 */
	@Override
	protected void initializeHBoxStats() {
		super.initializeHBoxStats();
		bossHealthHBox = new HBox();
		bossHealthHBox.setSpacing(5);
		bossHealthHBox.getChildren().add(BOSS_HEALTH);
		bossHealthHBox.getChildren().add(bossHealth);
	}
	
	/**
	 * Creates vertical box to house all the horizontal one
	 */
	@Override
	protected void initializeStatsBox() {
		super.initializeStatsBox();
		getStatsBox().getChildren().addAll(levelHBox, numEnemiesDestroyedHBox, bossHealthHBox, livesHBox);
	}
	
	/**
	 * Handles what the human inputs
	 */
	protected void handleGameLoopKeyInput (KeyCode code) {
		if(getGameLoop().getStatus() != Status.RUNNING || myHero.getHealth() <= 0) {return;}
		myMagicMathBoxText = myMagicMathBox.getMathText().getText();
		switch (code) {
		case RIGHT: {moveAlgebravianOneSpaceRight(); break;}
		case LEFT: {moveAlgebravianOneSpaceLeft(); break;}
		case UP:  {moveAlgebravianOneSpaceUp(); break;}
		case DOWN: {moveAlgebravianOneSpaceDown(); break;}
		case DIGIT0: {putDigitIntoMagicMathBox("0"); break;}
		case DIGIT1: {putDigitIntoMagicMathBox("1"); break;}
		case DIGIT2: {putDigitIntoMagicMathBox("2"); break;}
		case DIGIT3: {putDigitIntoMagicMathBox("3"); break;}
		case DIGIT4: {putDigitIntoMagicMathBox("4"); break;}
		case DIGIT5: {putDigitIntoMagicMathBox("5"); break;}
		case DIGIT6: {putDigitIntoMagicMathBox("6"); break;}
		case DIGIT7: {putDigitIntoMagicMathBox("7"); break;}
		case DIGIT8: {putDigitIntoMagicMathBox("8"); break;}
		case DIGIT9: {putDigitIntoMagicMathBox("9"); break;}
		case BACK_SPACE: {backSpaceDigitFromMagicMathBox(); break;}
		case Q: {shootBullet(Color.HOTPINK, -1); break;}
		case ESCAPE: {clearAllBullets(); break;}
		case ENTER: {processMagicMathBox(); break;}
		case T: {clearEnemies(); break;}
		default: // do nothing
		}
	}
	
	/**
	 * Update sprites based on numbers 
	 */
	public void processMagicMathBox() {
		clearNumberEnemies(myMagicMathBox.getValue());
		myMagicMathBox.setMathText("");
		myMagicMathBox.setValue(myMagicMathBox.getMaxMagicMathBoxNumber() + 1);
	}
	
	/**
	 * Deletes one by one entries in the mathMagicBox
	 */
	public void backSpaceDigitFromMagicMathBox() {
		if(myMagicMathBoxText.length() >= 0 &&
				!myMagicMathBoxText.equals(""))
			myMagicMathBox.setMathText(myMagicMathBoxText.substring(0, myMagicMathBoxText.length() - 1));
	}
	
	/**
	 * Writes a digit into the Box
	 * @param digitString
	 */
	public void putDigitIntoMagicMathBox(String digitString) {
		if(myMagicMathBoxText.length() < maxBoxLength)
			myMagicMathBox.setMathText(myMagicMathBoxText + digitString);
	}
	
	/**
	 * Clear all enemies of a certain number
	 * @param number
	 */
	public void clearNumberEnemies(int number) {
		for (Sprite sprite:getSpriteManager().getAllSprites()) {
			if (sprite instanceof Arithmetoid) {
				if(sprite.getValue() == number) {
					sprite.handleDeath(this);
					if (((Arithmetoid) sprite).getHealth() <= 0) {
						setEnemyCount(getEnemyCount() + 1);
					}
				}
			}
		}
	}

	/**
	 * Initialize the Guassianoid
	 */
	public void initializeBoss() {
		myBoss = new Gaussianoid(-1);
		addSpriteToSpriteManagerAndScene(myBoss);

		myBoss.setInitialPosition(myBoss.getCenterXPositionForNode(this), 0);
	}
	
	/**
	 * initialize the magicBox
	 */
	public void initializeMagicMathBox() {
		myMagicMathBox = new MagicMathBox();
		addSpriteToSpriteManagerAndScene(myMagicMathBox);
		
		myMagicMathBox.setInitialPosition(myMagicMathBox.getRightXPositionForNode(this), myMagicMathBox.getBottomYPositionForNode(this));
	}
}