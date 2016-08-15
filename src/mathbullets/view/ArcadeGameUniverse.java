package mathbullets.view;

import mathbullets.gameengine.GameWorld;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Class used to initialize the Splash Screen
 * @author Austin
 *
 */
public class ArcadeGameUniverse extends GameWorld {
	/**
	 * Variables:
	 */

	/**
	 * ArcadeGameUniverse Constructor:
	 * @param fps - Frames per second
	 * @param title - Title of application window
	 */
	public ArcadeGameUniverse(int fps, String title) {
		// Use GameWorld constructor
		super(fps, title);
		setStartButton(new Button("Play Arcade Mode"));
		setBossButton(new Button("Play Story Mode"));
	}

	/**
	 * Initializes the Arcade Mode Universe by updating primaryStage
	 * @param primaryStage - The JavaFx Stage
	 */
	@Override
	public void initialize(final Stage primaryStage) {
		super.initialize(primaryStage);

		initializeSplashScreen();
	}

	/**
	 * Helps initialize splash screen
	 */
	@Override
	public void initializeSplashScreen() {
		super.initializeSplashScreen();
	}

	/**
	 * No such existence of death screen on splash screen
	 */
	@Override
	public void initializeDeathScreen() {
		return;
	}
}
