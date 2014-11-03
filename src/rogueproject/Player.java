package rogueproject;

import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.Animation;

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
public class Player extends Actor{

	private int depth; // track the player's depth in the dungeon
	private int orders = PlayingState.WAIT; // store the input here for acting
	private int classtype;
	//private boolean turn = true;
	private Animation anim;
	
	public Player(int charClass){
		super();
		classtype = charClass;
		depth = 1;
		setTypeAttributes();
		getTypeImage();
		addAnimation(anim);
		anim.setLooping(true);
		this.setTurn(true);
	}
	/* Getters */
	
	public int getDepth()		{return this.depth;}
	public int getOrders()		{return this.orders;}
	
	@Override
	public void getTypeImage(){
		switch(this.classtype){
		case 0:			
			this.anim = new Animation(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_PLAYER0_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					, 0, 0, 0, 0, true, 300, true);
			this.anim.addFrame(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_PLAYER1_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					.getSprite(0, 0), 300);
			break;
		default:
			break;
		}
	}
	
	/* Setters */
	
	@Override
	public void setTypeAttributes(){
		switch(this.getType()){
		case 0:
			this.setLevel(1);
			this.setMaxHitPoints(10);
			this.setHitPonts(10);
			this.setAttack(2);
			this.setArmor(0);
			this.setEnergy(0);
			this.setGain(1);
			this.setExp(0);
			break;
		default:
			break;
		}
	}
	
	public void setDepth(int set)		{this.depth = set;}
	public void setOrders(int set)		{this.orders = set;}
	
	/* Actions */
	
	@Override
	public boolean act(RogueGame rg){
		if(this.orders != PlayingState.WAIT && getEnergy() >= 1){ // only act if the action is not wait and there is enough energy to act
			setNextTile(getOrders());
			this.orders = PlayingState.WAIT;
			// rest
			if(this.getNextTile().equals(this.getTilePosition())){
				// TODO make rest regen health
				this.setHitPonts(this.getHitPoints() + 0.25f);
				this.consumeEnergy();
				this.setTurn(false);
				return true;
			}
			// if the player indicates a direction that is not an obstacle, enemy, or object, move into the open tile.
			else if (!isBlocked(rg.blocked) && !isOccupied(rg.occupied)){
				move();
				this.consumeEnergy();
				return true;
			}
			// if the indicated direction is an enemy, attack it.
			else if (isOccupied(rg.occupied)){
				for(Actor a : rg.actors){
					if(a.getTilePosition().equals(this.getNextTile())){
						attackActor(a);
						this.consumeEnergy();
						setNextTile(PlayingState.WAIT);
						this.setTurn(false);
					}
				}
					// TODO add 
					//			// if the indicated direction is an object, interact with it.
					//			else if (rg.objects[(int)this.getNextTile().getX()][(int)this.getNextTile().getY()] != null){
					//				interact(rg.objects[(int)this.getNextTile().getX()][(int)this.getNextTile().getY()]);
					//			}
				return true;
			}
			else{
				setNextTile(PlayingState.WAIT); // go nowhere
				return false;
			}
		} else if (getEnergy() < 1){
			return false;
		}
		return false;
	}

	
	
}
