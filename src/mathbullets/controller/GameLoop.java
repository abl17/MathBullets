package mathbullets.controller;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * This class initializes the Game Manager and Launches the Math Bullets application
 * @author Austin
 */
public class GameLoop extends Application {
	
	GameModeManager myManager = new GameModeManager(60, "Math Bullets");
	
	@Override
	public void start(Stage primaryStage) {
		
		setStagePosition(primaryStage, 0, 0);
		
		myManager.initialize(primaryStage);
		
	}
	
	/**
	 * Brings the stage into view and sets the initial position of the stage
	 * @param primaryStage
	 * @param xPosition
	 * @param yPosition
	 */
	public void setStagePosition(Stage primaryStage, int xPosition, int yPosition) {
		primaryStage.show();
		primaryStage.setX(0);
		primaryStage.setY(0);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
