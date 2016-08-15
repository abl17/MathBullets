package mathbullets.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
 * The purpose of this class is to act as a generator for my Guassianoid class:
 * @author Austin
 *
 */
public class Newtonoid extends Arithmetoid {

	private int newtonoidMaxHealth = 1;
	
	private int maxNumber1 = 21;
	private int maxNumber2 = 21;
	private int maxValue = 999;
	private List<String> possibleOperators = new ArrayList<>(Arrays.asList("+", "-", "*", "/"));
	
	private final Image newtonoidPicture = new Image(getClass().getClassLoader().getResourceAsStream("Ship2.gif"));;
	private ImageView newtonoidImage = new ImageView(newtonoidPicture);
	
	/**
	 * Constructs the Newtonoid object including the value and corresponding operation string
	 */
	public Newtonoid() {
		int number1 = (int) Math.floor(Math.random()*maxNumber1);
		int number2 = (int) Math.floor(Math.random()*maxNumber2);
		Collections.shuffle(possibleOperators);
		String myString = "";
		setValue(0);
		
		for (String operator:possibleOperators) {
			switch (operator) {
			case "+":
				if (number1 + number2 > maxValue)
					break;
				else
					myString = Integer.toString(number1) + operator + Integer.toString(number2); 
					setValue(number1 + number2);
				break;
			case "-":
				if (number1 - number2 < 0)
					break;
				else
					myString = Integer.toString(number1) + operator + Integer.toString(number2); 
					setValue(number1 - number2);
				break;
			case "*":
				if (number1 * number2 > maxValue)
					break;
				else
					myString = Integer.toString(number1) + operator + Integer.toString(number2); 
					setValue(number1 * number2);
				break;
			case "/":
				if (number2 == 0)
					break;
				if (number1%number2 != 0)
					break;
				else
					myString = Integer.toString(number1) + operator + Integer.toString(number2);
					setValue(number1 / number2);
				break;
			}
		}
	
		setMathText(myString);
		getMathText().setFont(Font.font("Verdana", 15));
		getMathText().setFill(Color.WHITE);
		getMathText().setTranslateY(newtonoidPicture.getHeight()/4);
		
		setHealth(newtonoidMaxHealth);
		
		getEnemyFrame().getChildren().add(newtonoidImage);
		getEnemyFrame().getChildren().add(getMathText());
		setNode(getEnemyFrame());
	}

	/**
	 * Remove from play
	 */
	@Override
	public void implode(GameWorld gameWorld) {
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
	 * Handles newtonoids losing health
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
