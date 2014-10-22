package rogueproject;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/**
 * 
 * @author Zacharias Shufflebarger
 *
 */
public class Actor extends Entity {
	
	// RPG attributes
	private int level;
	private float hitPoints;
	private float attack;
	private float armor;
	// Graphics attributes
	private int type; // Player, creature, etc.
	private Animation anim;
	// Action attributes
	private boolean moving = false;
	private float xdest, ydest;
	private Vector dest, // AI pathfinding final destination
					nextTile, // short-term destination
					toNextTile; // direction vector from actor to nextTile
	private float energy, gain; // energy is used for actions per turn
	// consider also attackCost to differentiate from movement and attack costs
	// i.e. faster movement, but regular attack speed.
	
	/* Constructors */
	/**
	 * 
	 * @param setlevel starting level
	 * @param sethitppoints starting hitPoints
	 * @param setattack starting attack
	 * @param setarmor starting armor
	 * @param settype actor type
	 * @param setgain energy gained per turn
	 * @param setx tile x coordinate
	 * @param sety tile y coordinate
	 */
	public Actor(int setlevel, float sethitpoints, float setattack, float setarmor, 
			int settype, float setgain, int setx, int sety){
		super((setx * RogueGame.TILE_SIZE) + (RogueGame.TILE_SIZE/2), //multiply to scale tile coordinates to x and y on screen
				(sety * RogueGame.TILE_SIZE) + (RogueGame.TILE_SIZE/2)); // then add to center the coordinates in the tile
		level = setlevel;
		hitPoints = sethitpoints;
		attack = setattack;
		armor = setarmor;
		type = settype;
		getTypeImage();
		addAnimation(anim);
		anim.setLooping(true);
		energy = gain = setgain;
	}
	
	/* Getters */
	
	public int getLevel(){
		return level;
	}
	
	public float getHitPoints(){
		return hitPoints;
	}
	
	public float getAttack(){
		return attack;
	}
	
	public float getArmor(){
		return armor;
	}
	
	public int getType(){
		return type;
	}
	
	public float getEnergy(){
		return energy;
	}
	
	public float getGain(){
		return gain;
	}
	
	public int getTileX(){
		return (int) ((getX() - (RogueGame.TILE_SIZE/2)) / RogueGame.TILE_SIZE);
	}
	
	public int getTileY(){
		return (int) ((getY() - (RogueGame.TILE_SIZE/2))/ RogueGame.TILE_SIZE);
	}
	
	public Vector getTilePosition(){
		return new Vector(getTileX(), getTileY());
	}
	
	public boolean isMoving(){
		return moving;
	}
	
	public Vector getNextTile(){
		return nextTile;
	}
	
	public void getTypeImage(){
		switch(type){
		case 0:			
			anim = new Animation(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_PLAYER0_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					, 0, 0, 0, 0, true, 300, true);
			anim.addFrame(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_PLAYER1_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					.getSprite(0, 0), 300);
			break;
		default:
			break;
		}
	}
	
	/* Setters */
	/**
	 * sets a destination tile.
	 * @param setx tile x coordinate
	 * @param sety tile y coordinate
	 */
	public void setDestination(int setx, int sety){
		// convert tile coordinates to screen coordinates
		dest = new Vector ( (setx * RogueGame.TILE_SIZE) - (RogueGame.TILE_SIZE/2),
					(sety * RogueGame.TILE_SIZE) - (RogueGame.TILE_SIZE/2) );
	}
	
	public void setMoving(boolean setmoving){
		moving = setmoving;
	}
	/* Actions */
	/**
	 * The basic move instruction. Sets the actor's destination to a neighboring tile.
	 * @param direction cardinal direction to a neighboring tile
	 */
	public void setNextTile(java.lang.String direction){
		switch(direction){ // cardinal directions and 4 diagonals
		case "n":
			toNextTile = new Vector(0, -1);
			break;
		case "e":
			toNextTile = new Vector(1, 0);
			break;
		case "s":
			toNextTile = new Vector(0, 1);
			break;
		case "w":
			toNextTile = new Vector(-1, 0);
			break;
		case "nw":
			toNextTile = new Vector(-1, -1);
			break;
		case "ne":
			toNextTile = new Vector(1, -1);
			break;
		case "sw":
			toNextTile = new Vector(-1, 1);
			break;
		case "se":
			toNextTile = new Vector(1, 1);
			break;
		default:
			break;
		}
		nextTile = getTilePosition().add(toNextTile);
	}
	
	public void gainEnergy(){
		energy += gain;
	}
	
	/* Update */
	public void update(final int delta){
		translate(toNextTile);
	}
	/*
	public boolean atDestination(){
		Vector move = new Vector(getX(), getY());
	}
	*/
	
	/* Render */
/*
	public void draw(){
		anim.draw(getX(), getY());
	}
*/
/*
	@Override
	public void render (Graphics g){
		g.drawImage(anim0, getX(), getY());
	}
*/
}
