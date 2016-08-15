package mathbullets.controller;

import javafx.stage.Stage;
import mathbullets.gameengine.GameWorld;
import mathbullets.view.ArcadeGameUniverse;
import mathbullets.view.ArcadeMode;
import mathbullets.view.StoryMode;

/**
 * This manager class serves as the bridge between the three game modes. Uses event handlers to listen and
 * switch screens.
 * @author Austin
 */
public class GameModeManager {
	/**
	 * Variables
	 */
	private int fps;
	private String title;
	private ArcadeGameUniverse titleScreen;
	private ArcadeMode arcadeMode;
	private StoryMode storyMode;

	/**
	 * Constructor: Initializes the manager for the splash screen and the two game modes
	 * Creates objects for each mode
	 * @param framesPerSecond
	 * @param windowTitle
	 */
	public GameModeManager(int framesPerSecond, String windowTitle) {
		fps = framesPerSecond;
		title = windowTitle;
		titleScreen = new ArcadeGameUniverse(framesPerSecond, windowTitle);
		storyMode = new StoryMode(framesPerSecond, windowTitle);
		arcadeMode = new ArcadeMode(framesPerSecond, windowTitle);
	}

	/**
	 * Creates event listeners to determine whether player clicks on button or not.
	 * On event, switch modes
	 * @param primaryStage
	 */
	public void initialize(Stage primaryStage) {
		setTitleScreen(new ArcadeGameUniverse(fps, title));
		titleScreen.initialize(primaryStage);
		
		titleScreen.getStartButton().setOnAction((event) -> {
			startGameModeOnClick(titleScreen);
			
			setArcadeMode(new ArcadeMode(fps, title));
			arcadeMode.getDeathButton().setOnMouseClicked((e) -> {
				startGameModeOnClick(arcadeMode);
				titleScreen.initialize(primaryStage);
			});
			arcadeMode.getWinButton().setOnMouseClicked((e) -> {
				startGameModeOnClick(arcadeMode);
				titleScreen.initialize(primaryStage);
			});
			
			arcadeMode.initialize(primaryStage);
			arcadeMode.beginGameLoop();
		});
		
		titleScreen.getBossButton().setOnAction((event) -> {
			startGameModeOnClick(titleScreen);
			
			setStoryMode(new StoryMode(fps, title));
			storyMode.getDeathButton().setOnMouseClicked((e) -> {
				startGameModeOnClick(storyMode);
				titleScreen.initialize(primaryStage);
			});
			storyMode.getWinButton().setOnMouseClicked((e) -> {
				startGameModeOnClick(storyMode);
				titleScreen.initialize(primaryStage);
			});
			
			storyMode.initialize(primaryStage);
			storyMode.beginGameLoop();
		});
	}
	
	/**
	 * Removes the Splash Screen and its children to make way for new splash screens
	 * @param myTitleScreen
	 */
	private void startGameModeOnClick(GameWorld myTitleScreen) {
		myTitleScreen.getSplashScreen().getChildren().removeAll(myTitleScreen.getSplashScreen().getChildren());
		myTitleScreen.getSceneNodes().getChildren().removeAll(myTitleScreen.getSceneNodes().getChildren());
		myTitleScreen.getSceneNodes().getChildren().remove(myTitleScreen.getSplashScreen());
	}
	
	/**
	 * @return Object to control Initial Splash Screen
	 */
	public ArcadeGameUniverse getTitleScreen() {
		return titleScreen;
	}
	
	/**
	 * Sets the titleScreen object
	 * @param newTitleScreen
	 */
	public void setTitleScreen(ArcadeGameUniverse newTitleScreen) {
		titleScreen = newTitleScreen;
	}
	
	/**
	 * Obtain ArcadeMode Object
	 * @return ArcadeMode object
	 */
	public ArcadeMode getArcadeMode() {
		return arcadeMode;
	}
	
	/**
	 * Sets the arcade mode object
	 * @param newArcadeMode
	 */
	public void setArcadeMode(ArcadeMode newArcadeMode) {
		arcadeMode = newArcadeMode;
	}
	
	/**
	 * Obtain StoryMode Object
	 * @return StoryMode object
	 */
	public StoryMode getStoryMode() {
		return storyMode;
	}
	
	/**
	 * Sets StoryMode Object
	 * @param newStoryMode
	 */
	public void setStoryMode(StoryMode newStoryMode) {
		storyMode = newStoryMode;
	}
}
