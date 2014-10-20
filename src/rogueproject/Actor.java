package rogueproject;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import jig.Entity;
import jig.ResourceManager;

public class Actor extends Entity {
	
	private int level;
	private float hitPoints;
	private float attack;
	private float armor;
	private int type; // Player, creature, etc.
	private Animation anim;
	private boolean isMoving = false;
	
	/* Constructors */
	/**
	 * 
	 * @param lvl starting level
	 * @param hp starting hitPoints
	 * @param att starting attack
	 * @param arm starting armor
	 * @param t actor type
	 * @param x tile x coordinate
	 * @param y tile y coordinate
	 */
	public Actor(int lvl, float hp, float att, float arm, int t, int x, int y){
		super((x * RogueGame.TILE_SIZE) + 8, // scale tile coordinates to x and y on screen
				(y * RogueGame.TILE_SIZE) + 8); // +8 to center the coordinates in the tile
		level = lvl;
		hitPoints = hp;
		attack = att;
		armor = arm;
		type = t;
		getTypeImage();
		addAnimation(anim);
		anim.setLooping(true);
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
	
	public int getTileX(){
		return (int) (getX() / RogueGame.TILE_SIZE);
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
