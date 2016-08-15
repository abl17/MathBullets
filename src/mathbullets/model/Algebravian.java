package mathbullets.model;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import mathbullets.gameengine.GameWorld;
import mathbullets.gameengine.Sprite;

/**
 * This class extends Sprite and is used for creating the main character.
 * Has health, shooting abilities, and methods handling death and losing health.
 * @author Austin
 *
 */
public class Algebravian extends Sprite {

	/**
	 * A group contain all of the ship image view nodes.
	 */
	private final Group flipBook = new Group();

	private ImageView heroImage;

	private int KEY_INPUT_SPEED;
	private int SHOOTING_SPEED;
	private int health;
	private int maxHealth;
	
	private boolean invulnerableStatus = false;

	/**
	 * Algebravian constructor: Loads hero, adds image view, and sets up input parameters
	 */
	public Algebravian() {
		Image hero = new Image(getClass().getClassLoader().getResourceAsStream("hero.png"));
		heroImage = new ImageView(hero);

		flipBook.getChildren().add(heroImage);
		setNode(flipBook);

		KEY_INPUT_SPEED = 15;
		SHOOTING_SPEED = -10;
		maxHealth = 5;
		health = maxHealth;
	}
	
	/**
	 * Determines whether the algebravian has been hit recently, implying invulnerability
	 * @return
	 */
	public boolean isInvulnerable() {
		return invulnerableStatus;
	}
	
	/**
	 * Sets the status of Algebravian to invulnerable. Collisions do nothing.
	 * @param invulnerabilityValue
	 */
	public void setInvulnerability(boolean invulnerabilityValue) {
		invulnerableStatus = invulnerabilityValue;
	}

	/**
	 * Gets the current health of the algebravian
	 * @return
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Sets health
	 * @param newHealth
	 */
	public void setHealth(int newHealth) {
		health = newHealth;
	}
	
	/**
	 * Gets max health
	 * @return
	 */
	public int getMaxHealth() {
		return maxHealth;
	}
	
	/**
	 * Sets max health
	 * @param newMaxHealth
	 */
	public void setMaxHealth(int newMaxHealth) {
		maxHealth = newMaxHealth;
	}

	/**
	 * Get rate at which the hero moves
	 * @return
	 */
	public int getKeyInputSpeed() {
		return KEY_INPUT_SPEED;
	}

	/**
	 * Sets rate to modify
	 * @param NEW_KEY_INPUT_SPEED
	 */
	public void setKeyInputSpeed(int NEW_KEY_INPUT_SPEED) {
		KEY_INPUT_SPEED = NEW_KEY_INPUT_SPEED;
	}

	/**
	 * Fires a bullet object from in front
	 * @param color
	 * @param number
	 * @return
	 */
	public Bullet fire(Color color, int number) {
		Bullet bullet = new Bullet(color, number);

		//Velocity Vector of bullet:
		bullet.setXVelocity(0);
		bullet.setYVelocity(SHOOTING_SPEED);

		//Offset
		double offsetX = (getNode().getBoundsInLocal().getWidth() - bullet.getNode().getBoundsInLocal().getWidth()) / 2;
		double offsetY = 0;

		// initial launch of the missile
		bullet.getNode().setTranslateX(getNode().getTranslateX() + offsetX + bullet.getXVelocity() + bullet.getRadius());
		bullet.getNode().setTranslateY(getNode().getTranslateY() + offsetY + bullet.getYVelocity() - bullet.getRadius());

		return bullet;
	}

	/**
	 * Change the velocity of the Algebravian.
	 */
	@Override
	public void update() {
		getNode().setTranslateX(getNode().getTranslateX() + getXVelocity());
		getNode().setTranslateY(getNode().getTranslateY() + getYVelocity());

	}

	/**
	 * Animation for dying
	 */
	public void implode(final GameWorld gameWorld) {
		setXVelocity(0);
		setYVelocity(0);
		
		FadeTransition ft = new FadeTransition(Duration.millis(2000), getNode());
		ft.setFromValue(1.0);
		ft.setToValue(0);
		ft.setCycleCount(1);
		ft.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				setDeathStatus(true);
				gameWorld.getSceneNodes().getChildren().remove(getNode());
			}
		});

		ft.play();
	}
	
	/**
	 * Animation when hit, but not dead
	 * @param gameWorld
	 */
	public void flash(final GameWorld gameWorld) {
		setInvulnerability(true);
		
		FadeTransition ft = new FadeTransition(Duration.millis(100), getNode());
		ft.setFromValue(1.0);
		ft.setToValue(0);
		ft.setCycleCount(8);
		ft.setAutoReverse(true);

		ft.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				setInvulnerability(false);
			}
		});
		
		ft.play();
	}

	/**
	 * Handles a loss in health
	 */
	@Override
	public void handleDeath(GameWorld gameWorld) {
		if(invulnerableStatus)
			return;
		
		setHealth(getHealth() - 1);
		if (getHealth() <= 0) {
			implode(gameWorld);
			super.handleDeath(gameWorld);
		} else {
			flash(gameWorld);
		}
	}
}
