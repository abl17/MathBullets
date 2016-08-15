package mathbullets.view;

import javafx.animation.Animation.Status;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import mathbullets.model.Operoid;

/**
 * Class used to play Arcade Mode
 * @author Austin
 *
 */
public class ArcadeMode extends SideScrollGameMode {

	/**
	 * Variables
	 */
	private static final Label ENEMY_KILL_GOAL = new Label("Number of Enemy Kills Needed: ");
	private Label enemyKillGoal = new Label();
	private HBox enemyKillGoalHBox;
	
	private int maxLevel = 10;

	public ArcadeMode(int fps, String title) {
		super(fps, title);
	}
	
	/**
	 * Sets the initial parameters of the game (Velocity of enemy travel, enemy generation speed, goals, objectives, etc.)
	 */
	protected void setInitialLevelStats() {
		averageInitialVy = 1;
		averageInitialGenerationSpeed = 150;
		initialGoal = 5;
		initialLevel = 1;

		averageVy = 1;
		averageGenerationSpeed = 150;
		goal = 5;
		level = 1;
	}
	
	/**
	 * Sets up how much harder the game gets as levels progress
	 */
	protected void setLevelUpStats() {
		goalIncreaseRate = 5;
		averageVyIncreaseRate = 0.1;
		averageGenerationSpeedIncreaseRate = 10;
	}
	
	/**
	 * Initializes the scene through the primary stage, and builds the initial game settings
	 */
	@Override
	public void initialize(final Stage primaryStage) {
		super.initialize(primaryStage);
		initializeHero();
		setLevel(initialLevel);
	}

	/**
	 * Allows for updating speeds and operoid generation speed alongside regular updateSprites
	 */
	@Override
	protected void updateSprites() {
		generateOperoidRandomly();
		updateSpeedsBasedOnLevel();
		super.updateSprites();
	}

	/**
	 * Updates corner statistics for ease of user understanding
	 */
	@Override
	protected void updateStats() {
		super.updateStats();
		enemyKillGoal.setText(Integer.toString(goal));
	}
	
	/**
	 * Check to see win/lose/next level conditions
	 */
	@Override
	protected void checkAdvancements() {
		super.checkAdvancements();
		if(level >= maxLevel && myHero.getHealth() > 0) {
			shutdown();
			initializeWinScreen();
		}
	}
	
	/**
	 * Sets the next level
	 */
	@Override
	public void setLevel(int newLevel) {
		super.setLevel(newLevel);
		clearEnemies();
		setEnemyCount(0);
		myHero.setHealth(myHero.getMaxHealth());
	}
	
	/**
	 * Generates a single operoid with an initial velocity and a random value
	 */
	private void generateOperoid() {
		Operoid operoid = new Operoid((int) Math.floor(Math.random()*10));
		addSpriteToSpriteManagerAndScene(operoid);
		operoid.setYVelocity(Math.random()*averageVy);
		operoid.setInitialPosition(Math.floor(Math.random()*(operoid.getRightXPositionForNode(this))), 0);
	}

	/**
	 * Either creates a single operoid or doesn't, based on probability
	 */
	public void generateOperoidRandomly() {
		int MagicNumber = (int) Math.floor(Math.random()*averageGenerationSpeed);
		if (MagicNumber == 0) {
			generateOperoid();
		}
	}
	
	/**
	 * Modifies the text colors in the corner
	 */
	@Override
	protected void modifyLabelColors() {
		super.modifyLabelColors();
		ENEMY_KILL_GOAL.setTextFill(Color.WHITE);
		enemyKillGoal.setTextFill(Color.GOLD);
	}
	
	/**
	 * Helper function for initializeStats()
	 */
	@Override
	protected void initializeHBoxStats() {
		super.initializeHBoxStats();
		enemyKillGoalHBox = new HBox();
		enemyKillGoalHBox.setSpacing(5);
		enemyKillGoalHBox.getChildren().add(ENEMY_KILL_GOAL);
		enemyKillGoalHBox.getChildren().add(enemyKillGoal);
	}
	
	/**
	 * Adds each statistic line to a VBox
	 */
	@Override
	protected void initializeStatsBox() {
		super.initializeStatsBox();
		getStatsBox().getChildren().addAll(levelHBox, livesHBox, enemyKillGoalHBox, numEnemiesDestroyedHBox);
	}

	/**
	 * Handles what the user inputs
	 */
	protected void handleGameLoopKeyInput (KeyCode code) {
		if(getGameLoop().getStatus() != Status.RUNNING || myHero.getHealth() <= 0) {return;}
		Color bulletColor;
		switch (code) {
		case RIGHT: {moveAlgebravianOneSpaceRight(); break;}
		case LEFT: {moveAlgebravianOneSpaceLeft(); break;}
		case UP:  {moveAlgebravianOneSpaceUp(); break;}
		case DOWN: {moveAlgebravianOneSpaceDown(); break;}
		case DIGIT0: {bulletColor = Color.RED; shootBullet(bulletColor, 0); break;}
		case DIGIT1: {bulletColor = Color.ORANGE; shootBullet(bulletColor, 1); break;}
		case DIGIT2: {bulletColor = Color.YELLOW; shootBullet(bulletColor, 2); break;}
		case DIGIT3: {bulletColor = Color.GREEN; shootBullet(bulletColor, 3); break;}
		case DIGIT4: {bulletColor = Color.BLUE; shootBullet(bulletColor, 4); break;}
		case DIGIT5: {bulletColor = Color.ANTIQUEWHITE; shootBullet(bulletColor, 5); break;}
		case DIGIT6: {bulletColor = Color.AQUAMARINE; shootBullet(bulletColor, 6); break;}
		case DIGIT7: {bulletColor = Color.BROWN; shootBullet(bulletColor, 7); break;}
		case DIGIT8: {bulletColor = Color.CYAN; shootBullet(bulletColor, 8); break;}
		case DIGIT9: {bulletColor = Color.HOTPINK; shootBullet(bulletColor, 9); break;}
		case ESCAPE: {clearAllBullets(); break;}
		case T: {clearEnemies(); break;}
		default: // do nothing
		}
	}
}
