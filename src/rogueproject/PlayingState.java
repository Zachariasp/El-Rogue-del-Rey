package rogueproject;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import jig.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.*;

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
 */
public class PlayingState extends BasicGameState {
	
	private TiledMap map;
	
	// input direction
	public static final int WAIT = -1, N = 0, E = 1, S = 2, W = 3, NW = 4, NE = 5, SE = 6, SW = 7, REST = 8;
	
	
	// collective boolean for of all actors turns
	public boolean actorsTurns = false; 
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) 
			throws SlickException {
		
		RogueGame rg = (RogueGame)game;
		rg.actors = new ArrayList<Actor>();
		setLevel(rg);
		
		rg.blocked = new boolean[map.getWidth()][map.getHeight()];
		rg.occupied = new boolean[map.getWidth()][map.getHeight()]; 
		rg.pathmap = new NodeMap(map);
				
		// Build collision detection for map tiles, and fill occupied with false values.
		for (int i = 0; i < map.getWidth(); i++){
			for (int j = 0; j < map.getHeight(); j++){
				rg.occupied[i][j] = false; // initialize occupied
				int tileID = map.getTileId(i, j, 0);
				String value = map.getTileProperty(tileID, "blocked", "false");
				if ("true".equals(value)){
					rg.blocked[i][j] = true;
				}
			}
		}
		// at play start set the actors starting position to occupied = true;
		rg.occupied[rg.player.getTileX()][rg.player.getTileY()] = true;

		for(Actor a : rg.actors){
			rg.occupied[a.getTileX()][a.getTileY()] = true;
		}
		
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		RogueGame rg = (RogueGame)game;
		
		if(rg.player == null){ // player died, don't render anything.
			rg.enterState(RogueGame.STARTUPSTATE);
		}
		if(map != null){
			map.render(0, 0); // renders the map on screen at (x, y)	
		} else {
			rg.enterState(RogueGame.PLAYINGSTATE);
		}
		if(rg.player != null){
			g.drawString("Level: " + (int)rg.player.getLevel() + 
					" Health: " + (int)rg.player.getHitPoints() + 
					"/" + (int)rg.player.getMaxHitPoints() +  
					" Attack: " + (int)rg.player.getAttack() + 
					" Armor: " + (int)rg.player.getArmor() + 
					" Experience: " + (int)rg.player.getExp()
					, 100 , 10);
			g.drawString("Dungeon Level: " + rg.player.getDepth(), 100, 25);
		}
		
		for(Actor a : rg.actors){
			a.render(g);
		}
		
		if(rg.player != null){
			rg.player.render(g);	
		}
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		RogueGame rg = (RogueGame)game;
		
		if(rg.actors.size()==0){
			rg.player.setDepth(rg.player.getDepth() + 1);
			rg.enterState(RogueGame.PLAYINGSTATE);
		}
		
		Input input = container.getInput();
		// The player's turn 
		if(rg.player != null){
			if(rg.player.getTurn()){
				if(!rg.player.getGained()){
					rg.player.gainEnergy();
					rg.player.setGained(true);
				}
				if(!rg.player.isMoving()){ // handle all user input in this block
					// Directional Keys
					//   Q   W   E
					//    \  |  /
					// A - restS - D
					//    /  |  \
					//   Z   X   C
					if 		(input.isKeyPressed(Input.KEY_W)) 	{rg.player.setOrders(N);} 		// North
					else if (input.isKeyPressed(Input.KEY_X)) 	{rg.player.setOrders(S);} 		// South
					else if (input.isKeyPressed(Input.KEY_A)) 	{rg.player.setOrders(W);} 		// West
					else if (input.isKeyPressed(Input.KEY_D)) 	{rg.player.setOrders(E);} 		// East
					else if (input.isKeyPressed(Input.KEY_Q)) 	{rg.player.setOrders(NW);} 		// Northwest
					else if (input.isKeyPressed(Input.KEY_E)) 	{rg.player.setOrders(NE);} 		// Northeast
					else if (input.isKeyPressed(Input.KEY_Z)) 	{rg.player.setOrders(SW);} 		// Southwest
					else if (input.isKeyPressed(Input.KEY_C)) 	{rg.player.setOrders(SE);} 		// Southeast
					else if (input.isKeyPressed(Input.KEY_S)) 	{rg.player.setOrders(REST);} 	// Rest
					else if (input.isKeyPressed(Input.KEY_ESCAPE)) {container.exit();}
					// Cheats:
					/* hard code modulo for the number of dungeons actually playable, 
					 * then add one, since dungeons start at level 1.
					 */
					else if (input.isKeyPressed(Input.KEY_I))	{
						rg.player.setDepth(1);
						map = null;
						//rg.enterState(RogueGame.PLAYINGSTATE);
					}
					else if (input.isKeyPressed(Input.KEY_O)) { 
						rg.player.setDepth(2); 
						map = null;
						//rg.enterState(RogueGame.PLAYINGSTATE);
						}
					rg.player.act(rg);
					if(!rg.player.getTurn()){ // not player's turn after taking action
						// set actors' turns
						for(Actor a : rg.actors){
							a.setTurn(true);
							a.setGained(false);
						}
					}
				}
			}
			// update player position
			if(rg.player.isMoving()){
				if(rg.player.getPosition().equals(rg.player.getNextTile().scale(RogueGame.TILE_SIZE))){
					//player reached destination.
					rg.player.setMoving(false);
					// end player's turn and begin actors' turns
					rg.player.setTurn(false);
					
					for(Actor a : rg.actors){
						a.setTurn(true);
						a.setGained(false);
					}
				}else {
					rg.occupied[rg.player.getTileX()][rg.player.getTileY()] = false;
					rg.player.update(delta);
					rg.occupied[rg.player.getTileX()][rg.player.getTileY()] = true;
				}
			}
		} 
			

		
		//Actors turns
		if(!rg.player.getTurn()){
			actorsTurns = false;
			for(Actor a : rg.actors){
				a.act(rg);
				if(rg.player == null){ // player died
					rg.enterState(RogueGame.STARTUPSTATE);
				}
				if(a.getTurn()) actorsTurns = true;
				// update actors
				if(a.isMoving()){
					if(a.getPosition().equals(a.getNextTile().scale(RogueGame.TILE_SIZE))){
						a.setMoving(false);
						a.setTurn(false);
					} else {
						//System.out.println("updating actor " + a.getTilePosition() + " to " + a.getNextTile());
						rg.occupied[a.getTileX()][a.getTileY()] = false;
						a.update(delta);
						rg.occupied[a.getTileX()][a.getTileY()] = true;
						if(a.getTurn()) actorsTurns = true;
					}
				}
			}
			// set players turn if all actors are done with theirs.
			if(!actorsTurns){
				rg.player.setTurn(true);
				rg.player.setGained(false);
			}
		}	
		
		// remove dead enemies	
		for(int i = rg.actors.size()-1; i >= 0; i--){
			Actor a = rg.actors.get(i);
			if(a.getHitPoints() <= 0){
				rg.occupied[a.getTileX()][a.getTileY()] = false;
				a.remove();
				rg.actors.remove(a);
			}
		}
		
		if(rg.player != null && rg.player.getHitPoints() <= 0){
			rg.player.remove();
			rg.player = null;
		}
	}

	@Override
	public int getID() {
		return RogueGame.PLAYINGSTATE;
	}
	
	public void setLevel(RogueGame rg) throws SlickException{
		switch(rg.player.getDepth()){
		case 1:
			rg.player.setTilePosition(7, 6);
			map = new TiledMap("rogueproject/resource/maps/largetestmap.tmx");
			//Little Zombies
			rg.actors.add( new Actor(0, 14, 15));
			rg.actors.add( new Actor(0, 10, 16));
			rg.actors.add( new Actor(0, 12, 28));
			rg.actors.add( new Actor(0, 6, 29));
			rg.actors.add( new Actor(0, 24, 38));
			rg.actors.add( new Actor(0, 25, 29));
			rg.actors.add( new Actor(0, 23, 7));
			rg.actors.add( new Actor(0, 40, 19));
			rg.actors.add( new Actor(0, 24, 37));
			rg.actors.add( new Actor(0, 26, 12));
			//Little Mummies
			rg.actors.add( new Actor(1, 31, 22));
			rg.actors.add( new Actor(1, 25, 21));
			rg.actors.add( new Actor(1, 23, 40));
			//Skeletons
			rg.actors.add( new Actor(2, 9, 30));
			rg.actors.add( new Actor(2, 45, 24));
			rg.actors.add( new Actor(2, 49, 26));
			rg.actors.add( new Actor(2, 37, 36));
			rg.actors.add( new Actor(2, 47, 38));
			//Large Zombies
			rg.actors.add( new Actor(3, 41, 9));
			rg.actors.add( new Actor(3, 43, 9));
			rg.actors.add( new Actor(3, 34, 36));
			rg.actors.add( new Actor(3, 34, 30));
			rg.actors.add( new Actor(3, 48, 18));
			rg.actors.add( new Actor(3, 56, 19));
			//Large Mummies
			rg.actors.add( new Actor(4, 55, 25));
			rg.actors.add( new Actor(4, 56, 24));
			rg.actors.add( new Actor(4, 54, 13));
			//Death
			rg.actors.add( new Actor(5, 48, 4)); 
			break;
		case 2:
			rg.player.setTilePosition(11, 31);
			map = new TiledMap("rogueproject/resource/maps/map2.tmx");
			// Little Spiders
			rg.actors.add( new Actor(6, 12, 19));
			rg.actors.add( new Actor(6, 7, 31));
			rg.actors.add( new Actor(6, 13, 34));
			rg.actors.add( new Actor(6, 6, 6));
			rg.actors.add( new Actor(6, 27, 16));
			rg.actors.add( new Actor(6, 37, 21));
			rg.actors.add( new Actor(6, 39, 31));
			rg.actors.add( new Actor(6, 51, 34));
			rg.actors.add( new Actor(6, 28, 5));
			rg.actors.add( new Actor(6, 59, 16));
			// Little Scorpions
			rg.actors.add( new Actor(7, 39, 29));
			rg.actors.add( new Actor(7, 34, 41));
			rg.actors.add( new Actor(7, 41, 29));
			rg.actors.add( new Actor(7, 34, 14));
			rg.actors.add( new Actor(7, 6, 5));
			// Slugs
			rg.actors.add( new Actor(8, 27, 39));
			rg.actors.add( new Actor(8, 37, 41));
			rg.actors.add( new Actor(8, 37, 42));
			// Large Spiders
			rg.actors.add( new Actor(9, 48, 10));
			rg.actors.add( new Actor(9, 22, 5));
			rg.actors.add( new Actor(9, 55, 19));
			// Large Scorpions
			rg.actors.add( new Actor(10, 46, 8));
			rg.actors.add( new Actor(10, 25, 6));
			rg.actors.add( new Actor(10, 54, 33));
			// Red Leech
			rg.actors.add( new Actor(11, 48, 8));
			break;
		default:
			break;
		}
	}

}
