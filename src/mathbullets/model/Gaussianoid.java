package mathbullets.model;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import mathbullets.gameengine.GameWorld;

/**
 * Gaussianoid extends Arithmetoid and describes a certain type of enemy that
 * generates other enemies. It is the "Boss"
 * @author Austin
 *
 */
public class Gaussianoid extends Arithmetoid {

	private final int bossMaxHealth = 20;
	private final Image boss;
	private ImageView bossImage;
	
	private double shootingSpeed;
	
	private boolean invulnerableStatus = false;
	private double reducedVelocity = 0.3;
	
	/**
	 * Gaussianoid constructor: Determines speed, its image, health, and equation
	 * @param number
	 */
	public Gaussianoid(int number) {
		shootingSpeed = 0.5;
		
		boss = new Image(getClass().getClassLoader().getResourceAsStream("boss.png"));
		bossImage = new ImageView(boss);
		
		setValue(number);
		setHealth(bossMaxHealth);

		setMathText(Integer.toString(number));
		getMathText().setFont(Font.font("Verdana", 20));
		getMathText().setFill(Color.RED);
		getMathText().setTranslateY(boss.getHeight()/4);
		
		getEnemyFrame().getChildren().add(bossImage);
		setNode(getEnemyFrame());
	}
	
	/**
	 * Randomly rather than algorithmically changes the XVelocity of the Gaussianoid, to be unpredictable
	 */
	public void moveRandomly() {
		setXVelocity(reducedVelocity*(Math.random()-0.5) + getXVelocity());
	}
	
	/**
	 * Similar to firing a bullet, but fires an enemy. Initializes the newtonoid
	 * @return
	 */
	public Newtonoid fire() {
		Newtonoid newtonoid = new Newtonoid();

		//Velocity Vector of bullet:
		newtonoid.setXVelocity(0);
		newtonoid.setYVelocity(shootingSpeed);

		//Offset
		double offsetX = (getNode().getBoundsInLocal().getWidth() - newtonoid.getNode().getBoundsInLocal().getWidth()) / 2;
		double offsetY = (getNode().getBoundsInLocal().getHeight());

		// initial launch of the missile
		newtonoid.getNode().setTranslateX(getNode().getTranslateX() + offsetX);
		newtonoid.getNode().setTranslateY(getNode().getTranslateY() + offsetY + newtonoid.getYVelocity());

		return newtonoid;
	}
	
	/**
	 * Gets the shooting speed, or essentially velocity of fired newtonoids
	 * @return
	 */
	public double getShootingSpeed() {
		return shootingSpeed;
	}
	
	/**
	 * Sets shooting speed
	 * @param newShootingSpeed
	 */
	public void setShootingSpeed(double newShootingSpeed) {
		shootingSpeed = newShootingSpeed;
	}
	
	/**
	 * Determines whether the Gaussianoid has been hit recently
	 * @return
	 */
	public boolean isInvulnerable() {
		return invulnerableStatus;
	}
	
	/**
	 * Sets the Gaussianoid to invulnerable
	 * @param invulnerabilityValue
	 */
	public void setInvulnerability(boolean invulnerabilityValue) {
		invulnerableStatus = invulnerabilityValue;
	}
	
	/**
	 * Reduces graphical image of Guassianoid to nothingness it dies
	 */
	@Override
	public void implode(final GameWorld gameWorld) {
		setXVelocity(0);
		setYVelocity(0);

		FadeTransition ft = new FadeTransition(Duration.millis(4000), getNode());
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
	 * Determines graphical output when Guassianoid loses a life.
	 * @param gameWorld
	 */
	public void flash(final GameWorld gameWorld) {
		setInvulnerability(true);
		
		FadeTransition ft = new FadeTransition(Duration.millis(150), getNode());
		ft.setFromValue(1.0);
		ft.setToValue(0.5);
		ft.setCycleCount(4);
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
	 * Handles losing a life
	 */
	@Override
	public void handleDeath(GameWorld gameWorld) {
		if (invulnerableStatus)
			return;
		
		setHealth(getHealth() - 1);
		if(getHealth() <= 0) {
			implode(gameWorld);
			super.handleDeath(gameWorld);
		} else {
			flash(gameWorld);
		}
	}
}
