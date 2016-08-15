// This entire file is part of my masterpiece.
// Austin Liu

package mathbullets.view;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import mathbullets.gameengine.GameWorld;

/**
 * The purpose of this class is to give an abstract overview of variations of games
 * @author Austin
 *
 */
public abstract class GameMode extends GameWorld {
	
	/**
	 * Variables
	 */
	private VBox stats;
	private Button winButton;

	/**
	 * Regular GameWorld constructor, but initializes two buttons (Win/Lose)
	 * @param fps
	 * @param title
	 */
	public GameMode(int fps, String title) {
		super(fps, title);
		
		buildAndSetGameLoop();
		setDeathButton(new Button("You have Lost! Return to Splash Screen"));
		setWinButton(new Button("You Have Won! Return to Splash Screen"));
	}
	
	/**
	 * Include two more steps: updateStats() and checkAdvancements()
	 */
	protected final void buildAndSetGameLoop() {

		final Duration oneFrameAmt = Duration.millis(1000/ (float) getFramesPerSecond());
		final KeyFrame oneFrame = new KeyFrame(oneFrameAmt, 
				new EventHandler<ActionEvent>() {

			@Override
			public void handle(javafx.event.ActionEvent event) {

				// update actors
				updateSprites(); 

				// check for collision
				checkCollisions();

				// removed dead things
				cleanupSprites();
				
				// Update Statistics
				updateStats();
				
				//Check whether you win/lose/advance a level
				checkAdvancements();

			}
		}); // oneFrame

		// sets the game world's game loop (Timeline)
		final Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(oneFrame);
		timeline.setCycleCount(Animation.INDEFINITE);

		setGameLoop(timeline);
	}
	// Updates stats associated with game
	protected abstract void updateStats();
	// Check Win/Loss/New Level conditions
	protected abstract void checkAdvancements();

	/**
	 * Updates the scene at the beginning, before the timeline runs
	 */
	@Override
	public void initialize(final Stage primaryStage) {
		super.initialize(primaryStage);

		setupInput(primaryStage);

		initializeStats();
	}
	
	/**
	 * Creates a win screen based on win conditions
	 */
	public void initializeWinScreen() {
		getSplashScreen().getChildren().removeAll(getSplashScreen().getChildren());
		addWallpaperToSplashScreen("win_wallpaper.jpg");
		GridPane grid = makeGridAndBuildForSplashScreen();

		addButtonToGrid(grid, winButton, 0, buttonYPosition);

		getSplashScreen().getChildren().add(grid);

		setAndPlayAnimationEffectsForWinScreen();

		getSceneNodes().getChildren().add(getSplashScreen());
	}

	/**
	 * Sets up inputs based on user input parameters
	 * @param primaryStage
	 */
	protected abstract void setupInput(Stage primaryStage);

	/**
	 * Creates stats in corner for first time
	 */
	protected abstract void initializeStats(); 

	/**
	 * Return the vertical box containing all the stats
	 * @return
	 */
	public VBox getStatsBox() {
		return stats;
	}
	
	/**
	 * Sets and animates win screen effects
	 */
	public void setAndPlayAnimationEffectsForWinScreen() {
		getSplashScreen().setOpacity(0);
		FadeTransition ft = new FadeTransition(Duration.millis(2000), getSplashScreen());
		ft.setFromValue(0);
		ft.setToValue(1.0);
		ft.play();
	}
	
	/**
	 * Sets the vertical box as something new
	 * @param newVBox
	 */
	public void setStatsBox(VBox newVBox) {
		stats = newVBox;
	}
	
	/**
	 * Obtains the win button
	 * @return
	 */
	public Button getWinButton() {
		return winButton;
	}
	
	/**
	 * Sets the win button
	 * @param newWinButton
	 */
	public void setWinButton(Button newWinButton) {
		winButton = newWinButton;
	}

}
