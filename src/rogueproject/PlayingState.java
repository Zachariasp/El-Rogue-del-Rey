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


public class PlayingState extends BasicGameState {
	
	private TiledMap map;
	
	// input direction
	public static final int WAIT = -1, N = 0, E = 1, S = 2, W = 3, NW = 4, NE = 5, SE = 6, SW = 7, REST = 8;
	
	public boolean energyGained = false;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) 
			throws SlickException {
		
		RogueGame rg = (RogueGame)game;
		
		// TODO select map by depth
		map = new TiledMap("rogueproject/resource/maps/largetestmap.tmx");
		
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
		
		// temp hard set to player starting position
		rg.player.setTilePosition(7, 6);
		
		// at play start set the actors starting position to occupied = true;
		rg.occupied[rg.player.getTileX()][rg.player.getTileY()] = true;
		
		// add enemies TODO: this should be done on a depth basis. Get depth from
		// player and place enemies based on which dungeon the player is in.
		
		rg.actors2d = new Actor[rg.ScreenWidth][rg.ScreenHeight];
		rg.actors2d[14][15] = new Actor(1, 5, 1, 1, 1, 1, 14, 15); // put the Actor in actors at it's position
		
		for(Actor[] arr : rg.actors2d){
			for(Actor a : arr){
				if (a != null)
				rg.occupied[a.getTileX()][a.getTileY()] = true;
			}
		}
		
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		RogueGame rg = (RogueGame)game;
		
		map.render(0, 0); // renders the map on screen at (x, y)	
		
		for(Actor[] arr : rg.actors2d){
			for(Actor a : arr){
				if(a != null){
					a.render(g);
					g.drawString("Enemy HP = " + a.getHitPoints(), 10, 500);
				}
			}
		}
				
		rg.player.render(g);	
		
		float drawx= 10, drawy = 50;
		g.drawString("Energy: " + rg.player.getEnergy(), drawx, drawy);
		drawy += 15;
		g.drawString("Hit Points: " + rg.player.getHitPoints(), drawx, drawy);
		drawy += 15;
		for(int i = 0; i < map.getWidth(); i++){
			for(int j = 0; j < map.getHeight(); j++){
				if(rg.occupied[i][j]){
					g.drawString("Tile (" + i + ", " + j + ") occupied. ", drawx, drawy);
					drawy += 15;
				}
			}
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		RogueGame rg = (RogueGame)game;
		
		Input input = container.getInput();
		
		// The player's turn 
		if(rg.player != null){
			if(rg.player.getTurn()){
				if(!energyGained){
					rg.player.gainEnergy();
					energyGained = true;
				}
				if(!rg.player.isMoving()){ // handle all user input in this block
					// Directional Keys
					//   Q   W   E
					//    \  |  /
					// A - restS - D
					//    /  |  \
					//   Z   X   C
					if 		(input.isKeyDown(Input.KEY_W)) 	{rg.player.setOrders(N);} 		// North
					else if (input.isKeyDown(Input.KEY_X)) 	{rg.player.setOrders(S);} 		// South
					else if (input.isKeyDown(Input.KEY_A)) 	{rg.player.setOrders(W);} 		// West
					else if (input.isKeyDown(Input.KEY_D)) 	{rg.player.setOrders(E);} 		// East
					else if (input.isKeyDown(Input.KEY_Q)) 	{rg.player.setOrders(NW);} 		// Northwest
					else if (input.isKeyDown(Input.KEY_E)) 	{rg.player.setOrders(NE);} 		// Northeast
					else if (input.isKeyDown(Input.KEY_Z)) 	{rg.player.setOrders(SW);} 		// Southwest
					else if (input.isKeyDown(Input.KEY_C)) 	{rg.player.setOrders(SE);} 		// Southeast
					else if (input.isKeyDown(Input.KEY_S)) 	{rg.player.setOrders(REST);} 	// Rest
					
					rg.player.act(rg);
				}
			}
		}
		
		//Actors turns
		if(!rg.player.getTurn()){
			for(Actor[] arr : rg.actors2d){
				for(Actor a : arr){
					if(a != null){
						a.gainEnergy();
						a.act(rg);
					}
				}
				rg.player.setTurn(true);
				energyGained = false;
			}
		}
		
		// update player position
		if(rg.player.getPosition().equals(rg.player.getNextTile().scale(RogueGame.TILE_SIZE))){
			if(rg.player.isMoving()){
				rg.player.setMoving(false);
				//rg.player.setTurn(false);
			}
		}else {
			rg.occupied[rg.player.getTileX()][rg.player.getTileY()] = false;
			rg.player.update(delta);
			rg.occupied[rg.player.getTileX()][rg.player.getTileY()] = true;
		}
		
		// remove dead enemies
		for(Actor[] arr : rg.actors2d){
			for(Actor a : arr){
				if(a != null && a.getHitPoints() <= 0){
					System.out.print("found one to delete!\n");
					rg.occupied[a.getTileX()][a.getTileY()] = false;
					a.remove();
					rg.actors2d[a.getTileX()][a.getTileY()] = null;
					a = null;
				}
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
	/*
	private boolean isBlocked(Actor a, int direction){
		Vector next = null;
		int xBlock, yBlock;
		switch(direction){
		case N:
			next = a.seeNextTile(N);
			xBlock = (int) next.getX() ;
	        yBlock = (int) next.getY() ;
	        return blocked[xBlock][yBlock];
		case E:
			next = a.seeNextTile(E);
			xBlock = (int) next.getX() ;
	        yBlock = (int) next.getY() ;
	        return blocked[xBlock][yBlock];
		case S:
			next = a.seeNextTile(S);
			xBlock = (int) next.getX() ;
	        yBlock = (int) next.getY() ;
	        return blocked[xBlock][yBlock];
		case W:
			next = a.seeNextTile(W);
			xBlock = (int) next.getX() ;
	        yBlock = (int) next.getY() ;
	        return blocked[xBlock][yBlock];
		case NW:
			next = a.seeNextTile(NW);
			xBlock = (int) next.getX() ;
	        yBlock = (int) next.getY() ;
	        return blocked[xBlock][yBlock];
		case NE:
			next = a.seeNextTile(NE);
			xBlock = (int) next.getX() ;
	        yBlock = (int) next.getY() ;
	        return blocked[xBlock][yBlock];
		case SW:
			next = a.seeNextTile(SW);
			xBlock = (int) next.getX() ;
	        yBlock = (int) next.getY() ;
	        return blocked[xBlock][yBlock];
		case SE:
			next = a.seeNextTile(SE);
			xBlock = (int) next.getX() ;
	        yBlock = (int) next.getY() ;
	        return blocked[xBlock][yBlock];
		default:
			return false;
		}
	}
 	*/
}
