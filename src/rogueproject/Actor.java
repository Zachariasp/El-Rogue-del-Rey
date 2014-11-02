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
		super((setx * RogueGame.TILE_SIZE), //multiply to scale tile coordinates to x and y on screen
				(sety * RogueGame.TILE_SIZE)); 
		nextTile = getTilePosition();
		toNextTile = new Vector(0,0);
		level = setlevel;
		hitPoints = sethitpoints;
		attack = setattack;
		armor = setarmor;
		type = settype;
		getTypeImage();
		addAnimation(anim);
		anim.setLooping(true);
		energy = 0;
		gain = setgain;
	}
	
	public Actor(){
		super(0,0);
		nextTile = toNextTile = new Vector(0,0);
	}
	/* Getters */
	
	public int getLevel()			{return level;}
	public float getHitPoints()		{return hitPoints;}
	public float getAttack()		{return attack;}
	public float getArmor()			{return armor;}
	public int getType()			{return type;}
	public float getEnergy()		{return energy;}
	public float getGain()			{return gain;}
	public boolean isMoving()		{return moving;}
	public Vector getNextTile()		{return nextTile;}
	
	public int getTileX()			{return (int) (getX() / RogueGame.TILE_SIZE);}
	public int getTileY()			{return (int) (getY()/ RogueGame.TILE_SIZE);}
	public Vector getTilePosition()	{return new Vector(getTileX(), getTileY());}

	public void getTypeImage(){
		switch(type){
		case 0:		
			break;
		case 1:
			anim = new Animation(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_UNDEAD0_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					, 0, 0, 0, 0, true, 300, true);
			anim.addFrame(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_UNDEAD1_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					.getSprite(0, 0), 300);
		default:
			break;
		}
	}
	
	/* Setters */
	
	public void setLevel(int set)		{this.level = set;}
	public void setHitPonts(float set) 	{this.hitPoints = set;}
	public void setAttack(float set)	{this.attack = set;}
	public void setArmor(float set)		{this.armor = set;}
	public void setEnergy(float set)	{this.energy = set;}
	public void setGain(float set)		{this.gain = set;}
	
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
	
	public void setTilePosition(int setx, int sety){
		this.setPosition(setx * RogueGame.TILE_SIZE, sety * RogueGame.TILE_SIZE);
	}
	
	/* Actions */
	
	public boolean act(RogueGame rg){
		return false;
	}
	
	/**
	 * The basic move instruction. Sets the actor's destination to a neighboring tile
	 * using a direction vector in tile coordinates.
	 * @param direction cardinal or diagonal direction to a neighboring tile
	 */
	public void setNextTile(int direction){
		switch(direction){ // cardinal directions and 4 diagonals
		case PlayingState.WAIT:
			toNextTile = new Vector(0, 0); // go nowhere
			break;
		case PlayingState.N:
			toNextTile = new Vector(0, -1);
			break;
		case PlayingState.E:
			toNextTile = new Vector(1, 0);
			break;
		case PlayingState.S:
			toNextTile = new Vector(0, 1);
			break;
		case PlayingState.W:
			toNextTile = new Vector(-1, 0);
			break;
		case PlayingState.NW:
			toNextTile = new Vector(-1, -1);
			break;
		case PlayingState.NE:
			toNextTile = new Vector(1, -1);
			break;
		case PlayingState.SW:
			toNextTile = new Vector(-1, 1);
			break;
		case PlayingState.SE:
			toNextTile = new Vector(1, 1);
			break;
		default:
			toNextTile = new Vector(0, 0); // go nowhere, invalid order
			break;
		}
		nextTile = getTilePosition().add(toNextTile);
	}
	/**
	 * Takes a sneak peak at the next tile without setting any internal Actor attributes.
	 * @param direction cardinal or diagonal direction to a neighboring tile
	 * @return vector to destination tile
	 */
	public Vector seeNextTile(int direction){
		Vector nt = null;
		switch(direction){ // cardinal directions and 4 diagonals
		case PlayingState.N:
			nt = new Vector(0, -1);
			break;
		case PlayingState.E:
			nt = new Vector(1, 0);
			break;
		case PlayingState.S:
			nt = new Vector(0, 1);
			break;
		case PlayingState.W:
			nt = new Vector(-1, 0);
			break;
		case PlayingState.NW:
			nt = new Vector(-1, -1);
			break;
		case PlayingState.NE:
			nt = new Vector(1, -1);
			break;
		case PlayingState.SW:
			nt = new Vector(-1, 1);
			break;
		case PlayingState.SE:
			nt = new Vector(1, 1);
			break;
		default:
			break;
		}
		return getTilePosition().add(nt);
	}
	
	public void gainEnergy(){
		energy += gain;
	}
	
	public void consumeEnergy(){
		energy--;
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
	
	@Override
	public void render (Graphics g){
		/*
		 * Position in Tile coordinates is tracked by the top left corner of each tile,
		 * but Entity position renders sprite images centered at the Entity's position.
		 * To fix this, the Actor's Entity position is translated by half of the tile size
		 * towards the bottom right corner of the tile, so the Actor's Entity position
		 * is the middle of the tile. Then the Actor gets rendered correctly to the screen.
		 * After the Actor is rendered, its Entity position is translated back to Tile
		 * coordinates.
		 */
		setPosition(getPosition().add(new Vector(RogueGame.TILE_SIZE/2, RogueGame.TILE_SIZE/2)));
		super.render(g);
		setPosition(getPosition().add(new Vector(-RogueGame.TILE_SIZE/2, -RogueGame.TILE_SIZE/2)));
	}

	public void remove(){
		this.removeAnimation(anim);
		anim = null;
	}
	
}
