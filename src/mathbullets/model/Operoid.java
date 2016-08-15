package mathbullets.model;


import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import mathbullets.gameengine.GameWorld;
import mathbullets.util.MathDictionary;

/**
 * This class describes the operoid, which has values 0-9, which the Algebravian must destroy
 * @author Austin
 *
 */
public class Operoid extends Arithmetoid {
	private Rectangle operoidShape;
	private int operoidMaxHealth = 1;

	/**
	 * Constructor making the operoid shape, width, health, value, and image shown
	 * @param number
	 */
	public Operoid(int number) {
		setValue(number);
		setHealth(operoidMaxHealth);
		setMathDictionary(new MathDictionary(number));

		double width = 60;
		double height = 60;
		operoidShape = new Rectangle(width, height);
		operoidShape.setFill(Color.RED);

		setRandomOperation(getMathText(), number);

		getEnemyFrame().getChildren().add(operoidShape);
		getEnemyFrame().getChildren().add(getMathText());
		setNode(getEnemyFrame());
	}

	/**
	 * Deals with animation of death
	 */
	public void implode(final GameWorld gameWorld) {
		setXVelocity(0);
		setYVelocity(0);

		FadeTransition ft = new FadeTransition(Duration.millis(1000), getNode());
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
	 * Handles losing health
	 */
	@Override
	public void handleDeath(GameWorld gameWorld) {
		setHealth(getHealth() - 1);
		if(getHealth() <= 0) {
			implode(gameWorld);
			super.handleDeath(gameWorld);
		}
	}
}
