package rogueproject;

import java.awt.Font;
import java.util.ArrayList;

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


public class PlayingState extends BasicGameState {
	
	private TiledMap map;
	//private ArrayList<Actor> actors = new ArrayList<Actor>(1);
	//private Player player;
	public boolean[][] blocked;	
	public boolean[][] occupied; // for collision detection with actors
	
	// input direction
	public static final int N = 0, E = 1, S = 2, W = 3, NW = 4, NE = 5, SE = 6, SW = 7, REST = 8;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) 
			throws SlickException {
		
		RogueGame rg = (RogueGame)game;
		
		map = new TiledMap("rogueproject/resource/maps/largetestmap.tmx");
		blocked = new boolean[map.getWidth()][map.getHeight()];
		occupied = new boolean[map.getWidth()][map.getHeight()]; // TODO: make this a thing
		// Build collision detection for map tiles, and fill occupied with false values.
		for (int i = 0; i < map.getWidth(); i++){
			for (int j = 0; j < map.getHeight(); j++){
				occupied[i][j] = false;
				int tileID = map.getTileId(i, j, 0);
				String value = map.getTileProperty(tileID, "blocked", "false");
				if ("true".equals(value)){
					blocked[i][j] = true;
				}
			}
		}
		
		// temp hard set to player starting position
		rg.player.setTilePosition(7, 6);
		System.out.print(rg.player.getPosition() + " " + rg.player.getType() + "/n");
		occupied[rg.player.getTileX()][rg.player.getTileY()] = true;
		// add enemies TODO: this should be done on a depth basis. Get depth from
		// player and place enemies based on which dungeon the player is in.
		rg.actors.add(new Actor(1, 5, 1, 1, 1, 1, 14, 15));
		for(Actor a : rg.actors){
			occupied[a.getTileX()][a.getTileY()] = true;
		}
		
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		RogueGame rg = (RogueGame)game;
		
		map.render(0, 0); // renders the map on screen at (x, y)
		
		rg.player.render(g);
		
		for(Actor a : rg.actors){
			a.render(g);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		RogueGame rg = (RogueGame)game;
		
		Input input = container.getInput();
		// if the player has energy for an action and isn't currently moving
		// TODO: make action loop efficient and able to handle all other creatures.
		if(rg.player.getEnergy() >= 1){ 
			if(!rg.player.isMoving()){ // handle all user input in this block
				// Directional Keys
				//   Q   W   E
				//    \  |  /
				// A - restS - D
				//    /  |  \
				//   Z   X   C
				if (input.isKeyDown(Input.KEY_W)) { // North
					rg.player.setOrders(N);
					if (!isBlocked(rg.player, N)){
						rg.player.setNextTile(N);
						rg.player.setMoving(true);
					}
				}
				else if (input.isKeyDown(Input.KEY_X)) { // South
					rg.player.setOrders(S);
					if (!isBlocked(rg.player, S)){
						rg.player.setNextTile(S);
						rg.player.setMoving(true);
					}
				}
				else if (input.isKeyDown(Input.KEY_A)) { // West
					rg.player.setOrders(W);
					if (!isBlocked(rg.player, W)){
						rg.player.setNextTile(W);
						rg.player.setMoving(true);
					}
				}
				else if (input.isKeyDown(Input.KEY_D)) { // East
					rg.player.setOrders(E);
					if (!isBlocked(rg.player, E)){				
						rg.player.setNextTile(E);
						rg.player.setMoving(true);
					}
				}
				else if (input.isKeyDown(Input.KEY_Q)) { // Northwest
					rg.player.setOrders(NW);
					if (!isBlocked(rg.player, NW)){				
						rg.player.setNextTile(NW);
						rg.player.setMoving(true);
					}
				}
				else if (input.isKeyDown(Input.KEY_E)) { // Northeast
					rg.player.setOrders(NE);
					if (!isBlocked(rg.player, NE)){				
						rg.player.setNextTile(NE);
						rg.player.setMoving(true);
					}
				}
				else if (input.isKeyDown(Input.KEY_Z)) { // Southwest
					rg.player.setOrders(SW);
					if (!isBlocked(rg.player, SW)){				
						rg.player.setNextTile(SW);
						rg.player.setMoving(true);
					}
				}
				else if (input.isKeyDown(Input.KEY_C)) { // Southeast
					rg.player.setOrders(SE);
					if (!isBlocked(rg.player, SE)){				
						rg.player.setNextTile(SE);
						rg.player.setMoving(true);
					}
				}
				else if (input.isKeyDown(Input.KEY_S)){ // Rest
					// TODO: implement rest
					rg.player.setOrders(REST);
				}
			} else { // actor is moving
				if(rg.player.getPosition().equals(rg.player.getNextTile().scale(RogueGame.TILE_SIZE))){
					rg.player.gainEnergy();
					rg.player.setMoving(false);
				}else {
					rg.player.update(delta);				
				}
			}
		}
		
	}

	@Override
	public int getID() {
		return RogueGame.PLAYINGSTATE;
	}
	
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

}
