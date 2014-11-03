package rogueproject;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/**
 * 
 * @author Zacharias Shufflebarger
 *
 *	This file is part of El Rogue del Rey.
 *
 *  El Rogue del Rey is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  El Rogue del Rey is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with El Rogue del Rey.  If not, see <http://www.gnu.org/licenses/>.
 *
 *	Copyright 2014 Zacharias Shufflebarger
 *
 */
public class Actor extends Entity implements Mover{
	
	// RPG attributes
	private int level;
	private float maxHitPoints;
	private float hitPoints;
	private float attack;
	private float armor;
	private int experience; // for leveling up. Player accrues the experience enemies hold.
	// Graphics attributes
	private int type; // Player, creature, etc.
	private Animation anim;
	// Action attributes
	private boolean turn;
	public boolean energyGained = false; // false = no energy gained this turn.
	private boolean moving = false;
	private Vector nextTile, // adjacent tile to interact with.
					toNextTile; // direction vector from actor to nextTile
	private float energy, gain; // energy is used for actions per turn
	// consider also attackCost to differentiate from movement and attack costs
	// i.e. faster movement, but regular attack speed.
	
	/* Constructors */
	/**
	 * 
	 * @param settype actor type
	 * @param setx tile x coordinate
	 * @param sety tile y coordinate
	 */	
	public Actor(int settype, int setx, int sety){
		super((setx * RogueGame.TILE_SIZE), //multiply to scale tile coordinates to x and y on screen
				(sety * RogueGame.TILE_SIZE));
		this.nextTile = getTilePosition();
		this.toNextTile = new Vector(0,0);
		this.type = settype;
		this.getTypeImage();
		this.addAnimation(anim);
		this.anim.setLooping(true);
		this.energy = 0;
		this.setTypeAttributes();
		this.turn = false;
	}
	
	public Actor(){
		super(0,0);
		nextTile = toNextTile = new Vector(0,0);
	}
	/* Getters */
	
	public int getLevel()			{return level;}
	public float getMaxHitPoints()	{return maxHitPoints;}
	public float getHitPoints()		{return hitPoints;}
	public float getAttack()		{return attack;}
	public float getArmor()			{return armor;}
	public int getType()			{return type;}
	public float getEnergy()		{return energy;}
	public float getGain()			{return gain;}
	public boolean isMoving()		{return moving;}
	public Vector getNextTile()		{return nextTile;}
	public boolean getTurn()		{return this.turn;}
	public boolean getGained()		{return this.energyGained;}
	public Vector getToNextTile()	{return this.toNextTile;}
	public int getExp()				{return this.experience;}

	public int getTileX()			{return (int) (getX() / RogueGame.TILE_SIZE);}
	public int getTileY()			{return (int) (getY()/ RogueGame.TILE_SIZE);}
	public Vector getTilePosition()	{return new Vector(getTileX(), getTileY());}

	public void getTypeImage(){
		switch(type){
		case 0:	// little zombie
			anim = new Animation(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_UNDEAD0_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					, 0, 0, 0, 0, true, 300, true);
			anim.addFrame(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_UNDEAD1_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					.getSprite(0, 0), 300);
			break;
		case 1: // little mummy
			anim = new Animation(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_UNDEAD0_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					, 0, 1, 0, 1, true, 300, true);
			anim.addFrame(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_UNDEAD1_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					.getSprite(0, 1), 300);
			break;
		case 2: // skeleton
			anim = new Animation(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_UNDEAD0_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					, 0, 2, 0, 2, true, 300, true);
			anim.addFrame(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_UNDEAD1_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					.getSprite(0, 2), 300);
			break;
		case 3: // large zombie
			anim = new Animation(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_UNDEAD0_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					, 5, 0, 5, 0, true, 300, true);
			anim.addFrame(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_UNDEAD1_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					.getSprite(5, 0), 300);
			break;
		case 4: // large mummy
			anim = new Animation(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_UNDEAD0_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					, 5, 1, 5, 1, true, 300, true);
			anim.addFrame(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_UNDEAD1_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					.getSprite(5, 1), 300);
			break;
		case 5: // death
			anim = new Animation(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_UNDEAD0_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					, 0, 5, 0, 5, true, 300, true);
			anim.addFrame(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_UNDEAD1_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					.getSprite(0, 5), 300);
			break;
		case 6: // little spider
			anim = new Animation(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_PEST0_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					, 0, 2, 0, 2, true, 300, true);
			anim.addFrame(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_PEST1_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					.getSprite(0, 2), 300);
			break;
		case 7: // little scorpion
			anim = new Animation(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_PEST0_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					, 3, 2, 3, 2, true, 300, true);
			anim.addFrame(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_PEST1_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					.getSprite(3, 2), 300);
			break;
		case 8: // slug 
			anim = new Animation(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_PEST0_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					, 3, 7, 3, 7, true, 300, true);
			anim.addFrame(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_PEST1_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					.getSprite(3, 7), 300);
			break;
		case 9: // large spider
			anim = new Animation(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_PEST0_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					, 2, 2, 2, 2, true, 300, true);
			anim.addFrame(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_PEST1_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					.getSprite(2, 2), 300);
			break;
		case 10: // large scorpion
			anim = new Animation(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_PEST0_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					, 5, 2, 5, 2, true, 300, true);
			anim.addFrame(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_PEST1_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					.getSprite(5, 2), 300);
			break;
		case 11: // red leech
			anim = new Animation(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_PEST0_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					, 4, 3, 4, 3, true, 300, true);
			anim.addFrame(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_PEST1_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					.getSprite(4, 3), 300);
			break;
		default:
			break;
		}
	}
	
	/* Setters */
	
	public void setLevel(int set)			{this.level = set;}
	public void setMaxHitPoints(float set)	{this.maxHitPoints = set;}
	public void setHitPonts(float set)	 	{this.hitPoints = Math.min(set, this.maxHitPoints);}
	public void setAttack(float set)		{this.attack = set;}
	public void setArmor(float set)			{this.armor = set;}
	public void setEnergy(float set)		{this.energy = set;}
	public void setGain(float set)			{this.gain = set;}
	public void setTurn(boolean set)		{this.turn = set;}
	public void setGained(boolean set)		{this.energyGained = set;}
	public void setToNextTile(Vector set)	{this.toNextTile = set;}
	public void setExp(int set)				{this.experience = set;}
	
	public void setMoving(boolean setmoving){
		moving = setmoving;
	}
	
	public void setTilePosition(int setx, int sety){
		this.setPosition(setx * RogueGame.TILE_SIZE, sety * RogueGame.TILE_SIZE);
	}
	
	public void setTypeAttributes(){
		switch(this.getType()){
		// Undead
		case 0: // little zombie
			this.setLevel(1);
			this.setMaxHitPoints(7);
			this.setHitPonts(7);
			this.setAttack(1);
			this.setArmor(0);
			this.setGain(0.5f);
			this.setExp(1);
			break;
		case 1: // little mummy
			this.setLevel(1);
			this.setMaxHitPoints(5);
			this.setHitPonts(5);
			this.setAttack(1);
			this.setArmor(0);
			this.setGain(1);
			this.setExp(2);
			break;
		case 2: // skeleton
			this.setLevel(1);
			this.setMaxHitPoints(5);
			this.setHitPonts(5);
			this.setAttack(1);
			this.setArmor(1);
			this.setGain(1.5f);
			this.setExp(3);
			break;
		case 3: // large zombie
			this.setLevel(1);
			this.setMaxHitPoints(9);
			this.setHitPonts(5);
			this.setAttack(1);
			this.setArmor(0);
			this.setGain(0.75f);
			this.setExp(3);
			break;
		case 4: // large mummy
			this.setLevel(1);
			this.setMaxHitPoints(8);
			this.setHitPonts(5);
			this.setAttack(1);
			this.setArmor(1);
			this.setGain(1);
			this.setExp(3);
			break;
		case 5: // death
			this.setLevel(1);
			this.setMaxHitPoints(12);
			this.setHitPonts(12);
			this.setAttack(3);
			this.setArmor(1);
			this.setGain(0.8f);
			this.setExp(5);
			break;
		// Pests
		case 6: // little spider
			this.setLevel(2);
			this.setMaxHitPoints(15);
			this.setHitPonts(15);
			this.setAttack(2);
			this.setArmor(1);
			this.setGain(1);
			this.setExp(4);
			break;
		case 7: // little scorpion
			this.setLevel(2);
			this.setMaxHitPoints(15);
			this.setHitPonts(15);
			this.setAttack(2.25f);
			this.setArmor(1.5f);
			this.setGain(1);
			this.setExp(5);
			break;
		case 8: // slug
			this.setLevel(2);
			this.setMaxHitPoints(20);
			this.setHitPonts(20);
			this.setAttack(2);
			this.setArmor(3);
			this.setGain(0.7f);
			this.setExp(6);
			break;
		case 9: // large spider
			this.setLevel(2);
			this.setMaxHitPoints(20);
			this.setHitPonts(20);
			this.setAttack(3);
			this.setArmor(1.5f);
			this.setGain(1.5f);
			this.setExp(7);
			break;
		case 10: // large scorpion
			this.setLevel(2);
			this.setMaxHitPoints(20);
			this.setHitPonts(20);
			this.setAttack(3);
			this.setArmor(2);
			this.setGain(1.4f);
			this.setExp(8);
			break;
		case 11: // red leech
			this.setLevel(2);
			this.setMaxHitPoints(20);
			this.setHitPonts(20);
			this.setAttack(5);
			this.setArmor(2);
			this.setGain(1);
			this.setExp(15);
			break;
		default:
			break;
		}
	}
	
	/* Actions */
	
	/**
	 * AI actions
	 * @param rg
	 * @return
	 */
	public boolean act(RogueGame rg){
		
		if(this.turn){
			if(!this.isMoving()){
				if(!this.energyGained){
					this.gainEnergy();
					this.energyGained = true;
				}
				if(this.getEnergy() >= 1){
					// find path
					AStarPathFinder pathFinder = new AStarPathFinder(rg.pathmap, 7, true);
					Path path = pathFinder.findPath(this, this.getTileX(), this.getTileY(), 
							rg.player.getTileX(), rg.player.getTileY());
					if(path != null){
						this.nextTile = new Vector(path.getX(1), path.getY(1)); // set orders
					} else{ // no orders, hang out a bit.
						this.nextTile = this.getPosition(); 
					}
					if(this.getEnergy() >= 1){
						if(!this.nextTile.equals(this.getPosition())){
							if (!isBlocked(rg.blocked) && !isOccupied(rg.occupied)){
								this.toNextTile = this.nextTile.subtract(this.getTilePosition());
								move();
								this.consumeEnergy();
								return true;
							}
							else if (isOccupied(rg.occupied)){
								attackPlayer(rg.player);
								this.consumeEnergy();
								this.nextTile = this.getPosition();
								this.turn = false;
								return true;
							}
						} else { // rest
							this.consumeEnergy();
							this.turn = false;
							return true;
						}
					}else{
						return false;
					}

					this.nextTile = this.getPosition();
					this.turn = false;
					return false;
				}
				this.turn = false;
				this.setGained(false);
			}
		}
		return false;
	}
	
	public boolean isBlocked(boolean[][] blocked){
		return blocked[(int)this.getNextTile().getX()][(int)this.getNextTile().getY()];
	}
	
	public boolean isOccupied(boolean[][] occupied){
		return occupied[(int)this.getNextTile().getX()][(int)this.getNextTile().getY()];
	}
	
	public void move(){
		setMoving(true);
	}
	
	public void attackActor(Actor enemy){
		// damage done to enemy is player's attack minus enemies armor. if that is less than 0, do 0 damage instead.
		enemy.setHitPonts(enemy.getHitPoints() - Math.max(this.getAttack() - enemy.getArmor(), 0));
		if(enemy.getHitPoints() <= 0){
			this.setExp(this.getExp() + enemy.getExp());
		}
		while(this.getExp() > (this.getLevel() * 5)){ //defeating the enemy leads to a level up!
			System.out.println("levelup!");
			this.setExp(this.getExp() - (this.getLevel() * 5));
			this.setLevel(this.getLevel() + 1);
			this.setHitPonts(this.getHitPoints() + 5);
			this.setAttack(this.getAttack() + 1);
			this.setArmor(this.getArmor() + 0.5f);
		}
	}
	
	public void attackPlayer(Player enemy){
		// damage done to enemy is player's attack minus enemies armor. if that is less than 0, do 0 damage instead.
		enemy.setHitPonts(enemy.getHitPoints() - Math.max(this.getAttack() - enemy.getArmor(), 0));
	}
	
	public void gainEnergy(){
		energy += gain;
	}
	
	public void consumeEnergy(){
		energy--;
	}
	
	/**
	 * The basic move instruction for the player. Sets the player's destination to a 
	 * neighboring tile using a direction vector in tile coordinates.
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
		case PlayingState.REST:
			toNextTile = new Vector(0, 0);
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
	
	/* Update */
	public void update(final int delta){
		translate(toNextTile);
	}
	
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
