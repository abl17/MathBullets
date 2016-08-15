package mathbullets.model;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import mathbullets.gameengine.GameWorld;
import mathbullets.gameengine.Sprite;

/**
 * Bullet extends sprite, and contains methods to determine its collision with other sprites
 * @author Austin
 *
 */
public class Bullet extends Sprite {
	
	private double radius;
	private Circle myBullet;
	
	/**
	 * Constructor for bullet; Creates a circle, which makes it easy to determine collisions
	 * @param fill
	 * @param number
	 */
	public Bullet(Color fill, int number) {
		setValue(number);
		radius = 5;
		myBullet = new Circle(radius);
		myBullet.setFill(fill);
		
		setNode(myBullet);
		setCollisionBounds(myBullet);
		}
		
	/**
	 * Gets radius of the circle defined as the shape of the bullet
	 * @return
	 */
	public double getRadius() {
		return radius;
	}
	
	/**
	 * Sets the radius of the circle shape of the bullet
	 * @param r
	 */
	public void setRadius(double r) {
		this.radius = r;
	}
	
	/**
	 * Updates position based on velocity
	 */
	@Override
	public void update() {
		getNode().setTranslateX(getNode().getTranslateX()+getXVelocity());
		getNode().setTranslateY(getNode().getTranslateY()+getYVelocity());	
	}	
	
	/**
     * Returns a node casted as a JavaFX Circle shape. 
     * @return Circle shape representing JavaFX node for convenience.
     */
    public Circle getAsCircle() {
        return (Circle) getNode();
    }
    
    /**
     * Graphical removing from view
     */
    public void implode(final GameWorld gameWorld) {
    	setXVelocity(0);
    	setYVelocity(0);
    	
    	FadeTransition ft = new FadeTransition(Duration.millis(100), getNode());
    	ft.setFromValue(1.0);
    	ft.setToValue(0);
    	ft.setCycleCount(5);
    	ft.setAutoReverse(true);
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
     * Determines what happens when sprite loses a health
     */
    @Override
    public void handleDeath(GameWorld gameWorld) {
    	implode(gameWorld);
    	super.handleDeath(gameWorld);
    }

}
