package mathbullets.gameengine;

import javafx.scene.Node;
import javafx.scene.shape.Circle;

/**
 * Code Adopted from: COMPSCI-308 Tutorial: Building a JAVAFX Game: Part 1 - 4
 * https://dzone.com/articles/create-your-own-game-using
 * 
 * The Sprite class represents a image or node to be displayed.
 * In a 2D game a sprite will contain a velocity for the image to 
 * move across the scene area. The game loop will call the update()
 * and collide() method at every interval of a key frame. A list of 
 * animations can be used during different situations in the game
 * such as rocket thrusters, walking, jumping, etc.
 * @author Austin
 */
public abstract class Sprite {
    
	private int value;
    private Node node;
    private double vX = 0;
    private double vY = 0;
    private boolean isDead = false;
    private Circle collisionBounds;
    
    public abstract void update();
    
    /**
     * Did this sprite collide into the other sprite?
     * 
     * @param other - The other sprite.
     * @return 
     */
    
    public boolean collide(Sprite other) {
		if(this.node == null || other.node == null) {
			return false;
		}
		return (this.node.getBoundsInParent().intersects(other.node.getBoundsInParent()));
    }
    
    /**
     * Displays animation for death
     * @param gameWorld
     */
    public abstract void implode(GameWorld gameWorld);
    
    /**
     * Handles Sprite Death:
     */
    public void handleDeath(GameWorld gameWorld) {
    	gameWorld.getSpriteManager().addSpritesToBeRemoved(this);
    }
    
    /**
     * Gets value of the sprite (mostly for enemies)
     * @return
     */
    public int getValue() {
    	return value;
    }
    
    /**
     * Sets value of the sprite
     * @param newValue
     */
    public void setValue(int newValue) {
    	value = newValue;
    }
    
    /**
     * Gets node for managing sprite views
     * @return
     */
    public Node getNode() {
    	return node;
    }
    
    /**
     * Sets the node for managing sprites
     * @param newNode
     */
    public void setNode(Node newNode) {
    	node = newNode;
    }
    
    /**
     * Get the velocity at which each sprite is moving
     * @return
     */
    public double getXVelocity() {
    	return vX;
    }
    
    /**
     * Sets the XVelocity at which the sprite is moving
     * @param newVX
     */
    public void setXVelocity(double newVX) {
    	vX = newVX;
    }
    
    /**
     * Gets the YVelocity at which the sprite is moving
     * @return
     */
    public double getYVelocity() {
    	return vY;
    }
    
    /**
     * Sets the YVelocity at which the sprite is moving
     * @param newVY
     */
    public void setYVelocity(double newVY) {
    	vY = newVY;
    }
    
    /**
     * Determine whether the sprite is dead or not
     * @return
     */
    public boolean getDeathStatus() {
    	return isDead;
    }
    
    /**
     * Sets the sprite to dead or no
     * @param newDeathStatus
     */
    public void setDeathStatus(boolean newDeathStatus) {
    	isDead = newDeathStatus;
    }
    
    /**
     * Useful for determining intersections and collisions
     * @return
     */
    public Circle getCollisionBounds() {
    	return collisionBounds;
    }
    
    /**
     * Sets the bounds for collisions
     * @param newCollisionBounds
     */
    public void setCollisionBounds(Circle newCollisionBounds) {
    	collisionBounds = newCollisionBounds;
    }
    
    /**
     * Get position where the sprite node would be in the center X position
     * @param gameWorld
     * @return
     */
	public double getCenterXPositionForNode(GameWorld gameWorld) {
		return gameWorld.getGameSurface().getWidth() / 2 - getNode().getBoundsInLocal().getWidth() / 2;
	}
	
	/**
	 * Get position where the sprite would be in the center Y position
	 * @param gameWorld
	 * @return
	 */
	public double getCenterYPositionForNode(GameWorld gameWorld) {
		return gameWorld.getGameSurface().getHeight() / 2 - getNode().getBoundsInLocal().getHeight() / 2;
	}
	
	/**
	 * Get position where the sprite would be in the rightmost X position
	 * @param gameWorld
	 * @return
	 */
	public double getRightXPositionForNode(GameWorld gameWorld) {
		return gameWorld.getGameSurface().getWidth() - getNode().getBoundsInLocal().getWidth();
	}
	
	/**
	 * Get position where the sprite would be in the Bottommost Y position
	 * @param gameWorld
	 * @return
	 */
	public double getBottomYPositionForNode(GameWorld gameWorld) {
		return gameWorld.getGameSurface().getHeight()  - getNode().getBoundsInLocal().getHeight();
	}
	
	/**
	 * Sets the initial position of the sprite
	 * @param xPosition
	 * @param yPosition
	 */
	public void setInitialPosition(double xPosition, double yPosition) {
		getNode().setTranslateX(xPosition);
		getNode().setTranslateY(yPosition);
	}
}