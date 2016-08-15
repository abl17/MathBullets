package mathbullets.gameengine;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Code Adopted from: COMPSCI-308 Tutorial: Building a JAVAFX Game: Part 1 - 4
 * https://dzone.com/articles/create-your-own-game-using
 * 
 * Describes and comprises of the fundamentals to a simple game loop in JavaFX:
 * <pre>
 *  <b>initialize()</b> - Initialize the game world.
 *  <b>beginGameLoop()</b> - Creates a JavaFX Timeline object containing the game life cycle.
 *  <b>updateSprites()</b> - Updates the sprite objects each period (per frame)
 *  <b>checkCollisions()</b> - Method will determine objects that collide with each other.
 *  <b>cleanupSprites()</b> - Any sprite objects needing to be removed from play.
 * </pre>
 * @author Austin
 */
public abstract class GameWorld {

	private Scene gameSurface;
	private Group sceneNodes;
	private static Timeline gameLoop;
	private final int framesPerSecond;
	private final String windowTitle;
	private final StackPane SPLASHSCREEN = new StackPane();
	private final SpriteManager spriteManager = new SpriteManager();
	private ImageView wallpaperImage;
	
	//Buttons for Game Modes
	private Button deathButton;
	private Button startButton;
	private Button bossButton;
	
	//Frame Width and Height
	private final double WIDTH = 600;
	private final double HEIGHT = 800;
	
	protected int buttonXPosition = 10;
	protected int buttonYPosition = 50;

	/**
	 * Constructor that will set the frames per second, title, and setup the game loop.
	 * @param fps - Frames per second.
	 * @param title - Title of the application window.
	 */
	public GameWorld(final int fps, final String title) {
		framesPerSecond = fps;
		windowTitle = title;
	}

	/**
	 * Creates the Splash Screen as a Scene
	 */
	public void initializeSplashScreen() {
		addWallpaperToSplashScreen("wallpaper_text.jpg");
		GridPane grid = makeGridAndBuildForSplashScreen();
		
		addButtonToGrid(grid, startButton, 0, buttonYPosition);
		addButtonToGrid(grid, bossButton, buttonXPosition, buttonYPosition);

		addGridToSplashScreen(grid);
		
	}

	/**
	 * Helper method to add a grid that you create to the Splash Screen
	 * @param gridToAdd
	 */
	public void addGridToSplashScreen(GridPane gridToAdd) {
		SPLASHSCREEN.getChildren().add(gridToAdd);
		getSceneNodes().getChildren().add(SPLASHSCREEN);
	}
	
	/**
	 * Helper method to add buttons to grid
	 * @param gridHome
	 * @param buttonToAdd
	 * @param xPositionOfButton
	 * @param yPositionOfButton
	 */
	public void addButtonToGrid(GridPane gridHome, Button buttonToAdd, int xPositionOfButton, int yPositionOfButton) {
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.CENTER);
		hbBtn.getChildren().add(buttonToAdd);
		gridHome.add(buttonToAdd, xPositionOfButton, yPositionOfButton);
	}
	
	/**
	 * Makes a GridPane to format for Splash Screen
	 * @return
	 */
	public GridPane makeGridAndBuildForSplashScreen() {
		GridPane grid = new GridPane();
		buildGridForSplashScreen(grid);
		return grid;
	}
	
	/**
	 * Adds Wallpaper image as child to Splashscreen
	 */
	public void addWallpaperToSplashScreen(String imageLocationName) {
		Image wallpaper = new Image(getClass().getClassLoader().getResourceAsStream(imageLocationName));
		wallpaperImage = new ImageView(wallpaper);
		SPLASHSCREEN.getChildren().add(wallpaperImage);
	}

	/**
	 * Creates screen that appears at losing conditions
	 */
	public void initializeDeathScreen() {
		SPLASHSCREEN.getChildren().removeAll(SPLASHSCREEN.getChildren());

		addWallpaperToSplashScreen("death_wallpaper.jpg");
		GridPane grid = makeGridAndBuildForSplashScreen();

		addButtonToGrid(grid, deathButton, 0, buttonYPosition);

		SPLASHSCREEN.getChildren().add(grid);

		SPLASHSCREEN.setOpacity(0);
		FadeTransition ft = new FadeTransition(Duration.millis(2000), SPLASHSCREEN);
		ft.setFromValue(0);
		ft.setToValue(1.0);
		ft.play();

		getSceneNodes().getChildren().add(SPLASHSCREEN);
	}

	/**
	 * Initialize the game world by update the JavaFX Stage.
	 * @param primaryStage 
	 */
	public void initialize(final Stage primaryStage) {
		primaryStage.setTitle(getWindowTitle());
		// Uncomment to set to full screen.
//		primaryStage.setFullScreen(true);
		
		setSceneNodes(new Group());
		setGameSurface(new Scene(getSceneNodes(), WIDTH, HEIGHT));
		getGameSurface().setFill(Color.BLACK);

		getSceneNodes().getChildren().removeAll(getSceneNodes().getChildren());

		primaryStage.setScene(getGameSurface());
	}

	/**Kicks off (plays) the Timeline objects containing one key frame
	 * that simply runs indefinitely with each frame invoking a method
	 * to update sprite objects, check for collisions, and cleanup sprite 
	 * objects.
	 * 
	 */
	public void beginGameLoop() {
		getGameLoop().play();
	} 

	/**
	 * Updates each game sprite in the game world. This method will 
	 * loop through each sprite and passing it to the handleUpdate() 
	 * method. The derived class should override handleUpdate() method.
	 * 
	 */
	protected void updateSprites() {
		for (Sprite sprite:spriteManager.getAllSprites()){
			handleUpdate(sprite);
		}
	}

	/** Updates the sprite object's information to position on the game surface.
	 * @param sprite - The sprite to update.
	 */
	protected void handleUpdate(Sprite sprite) {
	}

	/**
	 * Checks each game sprite in the game world to determine a collision
	 * occurred. The method will loop through each sprite and
	 * passing it to the handleCollision()
	 * method. The derived class should override handleCollision() method.
	 *
	 */
	protected void checkCollisions() {
		// check other sprite's collisions
		spriteManager.resetCollisionsToCheck();
		// check each sprite against other sprite objects.
		for (Sprite spriteA:spriteManager.getCollisionsToCheck()){
			for (Sprite spriteB:spriteManager.getAllSprites()){
				if (handleCollision(spriteA, spriteB)) {
					// The break helps optimize the collisions
					//  The break statement means one object only hits another
					// object as opposed to one hitting many objects.
					// To be more accurate comment out the break statement.
					break;
				}
			}
		}
	}

	/**
	 * When two objects collide this method can handle the passed in sprite
	 * objects. By default it returns false, meaning the objects do not
	 * collide.
	 * @param spriteA - called from checkCollision() method to be compared.
	 * @param spriteB - called from checkCollision() method to be compared.
	 * @return boolean True if the objects collided, otherwise false.
	 */
	protected boolean handleCollision(Sprite spriteA, Sprite spriteB) {
		return false;
	}

	/**
	 * Sprites to be cleaned up.
	 */
	protected void cleanupSprites() {
		spriteManager.cleanupSprites();
	}

	/**
	 * Returns the frames per second.
	 * @return int The frames per second.
	 */
	protected int getFramesPerSecond() {
		return framesPerSecond;
	}

	/**
	 * Returns the game's window title.
	 * @return String The game's window title.
	 */
	public String getWindowTitle() {
		return windowTitle;
	}

	/**
	 * The game loop (Timeline) which is used to update, check collisions, and
	 * cleanup sprite objects at every interval (fps).
	 * @return Timeline An animation running indefinitely representing the game
	 * loop.
	 */
	protected static Timeline getGameLoop() {
		return gameLoop;
	}

	/**
	 * The sets the current game loop for this game world.
	 * @param gameLoop Timeline object of an animation running indefinitely
	 * representing the game loop.
	 */
	protected static void setGameLoop(Timeline gameLoop) {
		GameWorld.gameLoop = gameLoop;
	}

	/**
	 * Returns the sprite manager containing the sprite objects to
	 * manipulate in the game.
	 * @return SpriteManager The sprite manager.
	 */
	protected SpriteManager getSpriteManager() {
		return spriteManager;
	}

	/**
	 * Returns the JavaFX Scene. This is called the game surface to
	 * allow the developer to add JavaFX Node objects onto the Scene.
	 * @return
	 */
	public Scene getGameSurface() {
		return gameSurface;
	}

	/**
	 * Sets the JavaFX Scene. This is called the game surface to
	 * allow the developer to add JavaFX Node objects onto the Scene.
	 * @param gameSurface The main game surface (JavaFX Scene).
	 */
	protected void setGameSurface(Scene gameSurface) {
		this.gameSurface = gameSurface;
	}

	/**
	 * All JavaFX nodes which are rendered onto the game surface(Scene) is
	 * a JavaFX Group object.
	 * @return Group The root containing many child nodes to be displayed into
	 * the Scene area.
	 */
	public Group getSceneNodes() {
		return sceneNodes;
	}

	/**
	 * Sets the JavaFX Group that will hold all JavaFX nodes which are rendered
	 * onto the game surface(Scene) is a JavaFX Group object.
	 * @param sceneNodes The root container having many children nodes
	 * to be displayed into the Scene area.
	 */
	protected void setSceneNodes(Group sceneNodes) {
		this.sceneNodes = sceneNodes;
	}

	/**
	 * Stops the thread
	 */
	public void shutdown() {
		getGameLoop().stop();
	}
	
	public void addSpriteToSpriteManagerAndScene(Sprite sprite) {
		getSpriteManager().addSprites(sprite);
		getSceneNodes().getChildren().add(0, sprite.getNode());
	}
	
	/**
	 * Adjusts parameters for Splash Screen
	 * @param grid
	 */
	public void buildGridForSplashScreen(GridPane grid) {
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.setGridLinesVisible(false);
	}
	
	/**
	 * Obtains splash screen
	 * @return
	 */
	public StackPane getSplashScreen() {
		return SPLASHSCREEN;
	}
	
	/**
	 * Obtains death Button
	 * @return
	 */
	public Button getDeathButton() {
		return deathButton;
	}
	
	/**
	 * Set the button to a new button for losing condition
	 * @param newDeathButton
	 */
	public void setDeathButton(Button newDeathButton) {
		deathButton = newDeathButton;
	}
	
	/**
	 * Obtain start button for winning condition
	 * @return
	 */
	public Button getStartButton() {
		return startButton;
	}
	
	/**
	 * Get start button for winning condition
	 * @param newStartButton
	 */
	public void setStartButton(Button newStartButton) {
		startButton = newStartButton;
	}
	
	/**
	 * Gets bass button to start story mode
	 * @return
	 */
	public Button getBossButton() {
		return bossButton;
	}
	
	/**
	 * Sets the boss button to start story mode
	 * @param newBossButton
	 */
	public void setBossButton(Button newBossButton) {
		bossButton = newBossButton;
	}
	
	/**
	 * Gets the width of the scene
	 * @return
	 */
	public double getWidth() {
		return WIDTH;
	}
	
	/**
	 * Gets the height of the scene
	 * @return
	 */
	public double getHeight() {
		return HEIGHT;
	}
	
	/**
	 * Gets the wallpaper image for the scene
	 * @return
	 */
	public ImageView getWallpaperImage() {
		return wallpaperImage;
	}
	
	/**
	 * Sets the wallpaper image for the scene
	 * @param newWallpaperImage
	 */
	public void setWallpaperImage(ImageView newWallpaperImage) {
		wallpaperImage = newWallpaperImage;
	}

}
