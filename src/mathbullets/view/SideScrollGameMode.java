package mathbullets.view;

import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import mathbullets.gameengine.Sprite;
import mathbullets.model.Algebravian;
import mathbullets.model.Arithmetoid;
import mathbullets.model.Bullet;
import mathbullets.model.Gaussianoid;
import mathbullets.model.MagicMathBox;
import mathbullets.model.Newtonoid;
import mathbullets.model.Operoid;
import mathbullets.view.GameMode;

/**
 * The purpose of this class is to provide a way to abstract the two modes of my game
 * @author Austin
 *
 */
public abstract class SideScrollGameMode extends GameMode {

	/**
	 * Variables
	 */
	protected Algebravian myHero;

	protected static final Label NUM_ENEMIES_DESTROYED = new Label("Enemies Slain: ");
	protected static final Label LEVEL = new Label("Current Level: ");
	protected static final Label LIVES = new Label("Lives Remaining: ");

	protected Label numEnemiesDestroyed = new Label();
	protected Label lives = new Label();
	protected Label levelLabel = new Label();

	protected HBox numEnemiesDestroyedHBox;
	protected HBox livesHBox;
	protected HBox levelHBox;

	protected double averageInitialVy;
	protected double averageInitialGenerationSpeed;
	protected int initialGoal;
	protected int initialLevel;

	protected double averageVy;
	protected double averageGenerationSpeed;
	protected int goal;
	protected int level;

	protected int goalIncreaseRate;
	protected double averageVyIncreaseRate;
	protected double averageGenerationSpeedIncreaseRate;

	protected int enemyCount = 0;

	/**
	 * Constructor
	 * @param fps
	 * @param title
	 */
	public SideScrollGameMode(int fps, String title) {
		super(fps, title);
		setInitialLevelStats();
		setLevelUpStats();
	}
	
	/**
	 * Updates the scene at the beginning, before the timeline runs
	 */
	@Override
	public void initialize(final Stage primaryStage) {
		super.initialize(primaryStage);

		clearAllHostileSprites();
	}
	
	/**
	 * Clear all hostiles, i.e. Arithmetoids
	 */
	public void clearAllHostileSprites() {
		for (Sprite sprite:getSpriteManager().getAllSprites()) {
			if(sprite instanceof Arithmetoid)
				((Arithmetoid) sprite).setHealth(0);
			sprite.handleDeath(this);
		}
	}

	/**
	 * Sets up initial sprites
	 */
	protected abstract void setInitialLevelStats();

	/**
	 * Rate of increase of difficulty as levels increase
	 */
	protected abstract void setLevelUpStats();

	/**
	 * Set current level as a new level
	 * @param newLevel
	 */
	public void setLevel(int newLevel) {
		level = newLevel;
	}

	/**
	 * Updates the dynamic status
	 */
	protected void updateStats() {
		numEnemiesDestroyed.setText(Integer.toString(getEnemyCount()));
		lives.setText(Integer.toString(myHero.getHealth()));
		levelLabel.setText(Integer.toString(level));
	}

	/**
	 * Checks win/lose/draw conditions
	 */
	protected void checkAdvancements() {
		if(myHero.getDeathStatus()) {
			shutdown();
			initializeDeathScreen();
		}
		else if (getEnemyCount() >= goal && myHero.getHealth() > 0) {
			setLevel(level+1);
		}
	}

	/**
	 * Shoots a bullet 
	 * @param bulletColor
	 * @param number
	 */
	public void shootBullet(Color bulletColor, int number) {
		if (!myHero.getDeathStatus()) {
			Bullet bullet = myHero.fire(bulletColor, number);
			addSpriteToSpriteManagerAndScene(bullet);
		}
	}

	/**
	 * Update level parameters every frame. 
	 */
	public void updateSpeedsBasedOnLevel() {
		goal = initialGoal + goalIncreaseRate*(level - initialLevel);
		averageVy = averageInitialVy + (level - initialLevel)*averageVyIncreaseRate;
		averageGenerationSpeed = averageInitialGenerationSpeed - (level - initialLevel)*averageGenerationSpeedIncreaseRate;
	}

	/**
	 * Creating the hero
	 */
	public void initializeHero() {
		myHero = new Algebravian();
		addSpriteToSpriteManagerAndScene(myHero);

		//Set initial position
		myHero.getNode().setTranslateX(myHero.getCenterXPositionForNode(this));
		myHero.getNode().setTranslateY(myHero.getBottomYPositionForNode(this));
	}

	/**
	 * Bring up stats in the corner
	 */
	@Override
	protected void initializeStats() {
		modifyLabelColors();
		initializeHBoxStats();
		initializeStatsBox();

		getSceneNodes().getChildren().add(getStatsBox());
	}

	/**
	 * Helper method to modify label colors
	 */
	protected void modifyLabelColors() {
		LEVEL.setTextFill(Color.WHITE);
		NUM_ENEMIES_DESTROYED.setTextFill(Color.WHITE);
		LIVES.setTextFill(Color.WHITE);

		levelLabel.setTextFill(Color.SKYBLUE);
		numEnemiesDestroyed.setTextFill(Color.GOLD);
		lives.setTextFill(Color.LIME);
	}

	/**
	 * Puts all stats in an ordered fashion
	 */
	protected void initializeHBoxStats() {
		levelHBox = new HBox();
		levelHBox.setSpacing(5);
		levelHBox.getChildren().add(LEVEL);
		levelHBox.getChildren().add(levelLabel);
		
		numEnemiesDestroyedHBox = new HBox();
		numEnemiesDestroyedHBox.setSpacing(5);
		numEnemiesDestroyedHBox.getChildren().add(NUM_ENEMIES_DESTROYED);
		numEnemiesDestroyedHBox.getChildren().add(numEnemiesDestroyed);
		
		livesHBox = new HBox();
		livesHBox.setSpacing(5);
		livesHBox.getChildren().add(LIVES);
		livesHBox.getChildren().add(lives);
	}
	
	/**
	 * tacks all code in a nice vertical way
	 */
	protected void initializeStatsBox() {
		setStatsBox(new VBox());
		getStatsBox().setSpacing(5);
		getStatsBox().setTranslateX(0);
		getStatsBox().setTranslateY(0);
	}

	/**
	 * Sets up user input
	 */
	protected void setupInput(Stage primaryStage) {
		primaryStage.getScene().setOnKeyPressed(e -> handleGameLoopKeyInput(e.getCode()));
	}
	
	/**
	 * Handles what keys actually do
	 * @param code
	 */
	protected abstract void handleGameLoopKeyInput(KeyCode code);
	
	/**
	 * Handles interactions of sprites with other sprites
	 */
	@Override
	protected void handleUpdate(Sprite sprite) {
		sprite.update();
		if (!(sprite instanceof Algebravian) && !(sprite instanceof MagicMathBox)) {
			interactWithWall(sprite);
		}
	}
	
	/**
	 * Displays each sprites' interaction with the wall
	 * @param sprite
	 */
	protected void interactWithWall(Sprite sprite) {
		if(sprite.getNode().getTranslateX() > (getGameSurface().getWidth() - sprite.getNode().getBoundsInParent().getWidth()) ||
				sprite.getNode().getTranslateX() < 0) {
			if(sprite instanceof Gaussianoid ||
					sprite instanceof Newtonoid) {
						sprite.getNode().setTranslateX(Math.abs(sprite.getNode().getTranslateX() - sprite.getNode().getLayoutBounds().getWidth()/20 ));
						sprite.setXVelocity(-sprite.getXVelocity()/2);
			} 
			else if (sprite instanceof Operoid){
				((Operoid) sprite).setHealth(0);
				sprite.handleDeath(this);
				return;
			} 
			else if (!(sprite instanceof MagicMathBox)) {
					sprite.handleDeath(this);
					return;
			}
		} 
		
		if(sprite.getNode().getTranslateY() > getGameSurface().getHeight() - sprite.getNode().getBoundsInParent().getHeight() ||
				sprite.getNode().getTranslateY() < 0) {
			
			if(sprite instanceof Gaussianoid) {
				sprite.getNode().setTranslateY(Math.abs(sprite.getNode().getTranslateY() - sprite.getNode().getLayoutBounds().getHeight()/20 ));
				sprite.setYVelocity(-sprite.getYVelocity()/2 );
			} 
			else if (sprite instanceof Operoid ||
					sprite instanceof Newtonoid) {
				
				if (sprite instanceof Operoid)
					((Operoid) sprite).setHealth(0);
				else if (sprite instanceof Newtonoid)
					((Newtonoid) sprite).setHealth(0);
				sprite.handleDeath(this);

				myHero.setHealth(0);
				myHero.handleDeath(this);
				
				return;
			} 
			else if (!(sprite instanceof MagicMathBox)) {
					sprite.handleDeath(this);
					return;
			}
		}
	}

	/**
	 * Handles interactions (collisions) between enemies
	 */
	@Override
	protected boolean handleCollision(Sprite spriteA, Sprite spriteB) {
		if(spriteA != spriteB && spriteA.collide(spriteB)) {
			if (spriteB instanceof Bullet && !(spriteA instanceof MagicMathBox)) {
				
				spriteB.handleDeath(this);
				
				if(spriteA instanceof Bullet ||
						spriteA instanceof Algebravian) {
					spriteA.handleDeath(this);
				} 
				else if (spriteA instanceof Arithmetoid && 
						(spriteA.getValue() == spriteB.getValue()) &&
						!(spriteA instanceof Newtonoid)) {
					
					spriteA.handleDeath(this);
					
					if (((Arithmetoid) spriteA).getHealth() <= 0) {
						setEnemyCount(getEnemyCount() + 1);
					}
				}
			}

			else if ((spriteA instanceof Algebravian) && (spriteB instanceof Arithmetoid)) {
				if (spriteB instanceof Operoid) {
					((Operoid) spriteB).setHealth(0);
					setEnemyCount(getEnemyCount() + 1);
					spriteB.handleDeath(this);
				}
				else if(spriteB instanceof Newtonoid) {
					((Newtonoid) spriteB).setHealth(0);
					setEnemyCount(getEnemyCount() + 1);
					spriteB.handleDeath(this);
				}
				spriteA.handleDeath(this);
			}
		}

		return false;
	}

	/**
	 * Moves Algebravian 1 space right
	 */
	protected void moveAlgebravianOneSpaceRight() {
		if (myHero.getNode().getTranslateX() + myHero.getKeyInputSpeed() <= 
				getGameSurface().getWidth() - myHero.getNode().getBoundsInParent().getWidth()) {
			myHero.getNode().setTranslateX(myHero.getNode().getTranslateX() + myHero.getKeyInputSpeed());
		}
	}
	/**
	 * Moves Algebravian 1 space left
	 */
	protected void moveAlgebravianOneSpaceLeft() {
		if (myHero.getNode().getTranslateX() - myHero.getKeyInputSpeed() >= 0) {
			myHero.getNode().setTranslateX(myHero.getNode().getTranslateX() - myHero.getKeyInputSpeed());
		}
	}
	/**
	 * Moves Algebravian 1 space up
	 */
	protected void moveAlgebravianOneSpaceUp() {
		if (myHero.getNode().getTranslateY() - myHero.getKeyInputSpeed() >= 0) {
			myHero.getNode().setTranslateY(myHero.getNode().getTranslateY() - myHero.getKeyInputSpeed());
		}
	}
	/**
	 * Moves Algebravian 1 space down
	 */
	protected void moveAlgebravianOneSpaceDown() {
		if(myHero.getNode().getTranslateY() + myHero.getKeyInputSpeed() <=
				getGameSurface().getHeight() - myHero.getNode().getBoundsInParent().getHeight()) {
			myHero.getNode().setTranslateY(myHero.getNode().getTranslateY() + myHero.getKeyInputSpeed());
		}
	}
	
	/**
	 * Destroy currently visible bullets
	 */
	protected void clearAllBullets() {
		for (Sprite sprite:getSpriteManager().getAllSprites()) {
			if(sprite instanceof Bullet)
				sprite.handleDeath(this);
		}
	}
	
	/**
	 * Loops through sprite manager and deletes all enemies besides the boss
	 */
	public void clearEnemies() {
		for (Sprite sprite:getSpriteManager().getAllSprites()) {
			if (sprite instanceof Arithmetoid && !(sprite instanceof Gaussianoid)) {
				((Arithmetoid) sprite).setHealth(0);
			}

			if (!(sprite instanceof Algebravian) && !(sprite instanceof Bullet) 
					&& !(sprite instanceof MagicMathBox) && !(sprite instanceof Gaussianoid)) {
				sprite.handleDeath(this);
				setEnemyCount(getEnemyCount() + 1);
			}
		}
	}
	
	/**
	 * obtain enemy count
	 * @return
	 */
	public int getEnemyCount() {
		return enemyCount;
	}

	/**
	 * sets enemy count
	 * @param newEnemyCount
	 */
	public void setEnemyCount(int newEnemyCount) {
		enemyCount = newEnemyCount;
	}
}
