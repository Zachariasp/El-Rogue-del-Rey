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
	private ArrayList<Actor> actors = new ArrayList<Actor>(1);
	private boolean[][] blocked;	
	private boolean[][] occupied; // for collision detection with actors
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) 
			throws SlickException {
		map = new TiledMap("rogueproject/resource/maps/largetestmap.tmx");
		//System.out.print("map width = " + map.getWidth() + " and height = " + map.getHeight() + ".\n");
		blocked = new boolean[map.getWidth()][map.getHeight()];
		occupied = new boolean[map.getWidth()][map.getHeight()]; // TODO: make this a thing
		// Build collision detection for map tiles
		for (int i = 0; i < map.getWidth(); i++){
			for (int j = 0; j < map.getHeight(); j++){
				int tileID = map.getTileId(i, j, 0);
				String value = map.getTileProperty(tileID, "blocked", "false");
				//System.out.print("tile property: " + value + ".\n");
				if ("true".equals(value)){
					blocked[i][j] = true;
					//System.out.print("Tile (" + i + ", " + j + ") is blocked.\n");
				}
			}
		}
		
		// The player will always be actors.get(0)
		actors.add(new Actor(1, 10, 2, 2, 0, 1, 30, 30)); 
		System.out.print("Screen pos: " + actors.get(0).getPosition() + "\nTile pos: " + actors.get(0).getTilePosition() + "\n");
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		RogueGame rg = (RogueGame)game;
		
		map.render(0, 0); // renders the map on screen at (x, y)
		
		for(Actor a : actors){
			a.render(g);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		Input input = container.getInput();
		// if the player has energy for an action and isn't currently moving
		// TODO: make action loop efficient and able to handle all other creatures.
		if(actors.get(0).getEnergy() >= 1){ 
			//System.out.print("delta: " + delta + "\n");
			if(!actors.get(0).isMoving()){ // handle all user input in this block
				// TODO: collision detection against walls.
				// Directional Keys
				//   Q   W   E
				//    \  |  /
				// A - restS - D
				//    /  |  \
				//   Z   X   C
				if (input.isKeyDown(Input.KEY_W)) { // up
					//System.out.print("actor position before move up: " + actors.get(0).getTilePosition() + "\n");

					if (!isBlocked(actors.get(0), "n")){
						//System.out.print("moving up\n");
						actors.get(0).setNextTile("n");
						actors.get(0).setMoving(true);
					}
				}
				else if (input.isKeyDown(Input.KEY_X)) { // down
					//System.out.print("actor position before move down: " + actors.get(0).getTilePosition() + "\n");

					if (!isBlocked(actors.get(0), "s")){
						actors.get(0).setNextTile("s");
						actors.get(0).setMoving(true);
					}
				}
				else if (input.isKeyDown(Input.KEY_A)) { // left
					//System.out.print("actor position before move left: " + actors.get(0).getTilePosition() + "\n");

					if (!isBlocked(actors.get(0), "w")){
						actors.get(0).setNextTile("w");
						actors.get(0).setMoving(true);
					}
				}
				else if (input.isKeyDown(Input.KEY_D)) { // right
					//System.out.print("actor position before move right: " + actors.get(0).getTilePosition() + "\n");

					if (!isBlocked(actors.get(0), "e")){				
						actors.get(0).setNextTile("e");
						actors.get(0).setMoving(true);
					}
				}
				else if (input.isKeyDown(Input.KEY_Q)) { // up and left
					if (!isBlocked(actors.get(0), "nw")){				
						actors.get(0).setNextTile("nw");
						actors.get(0).setMoving(true);
					}
				}
				else if (input.isKeyDown(Input.KEY_E)) { // up and right
					if (!isBlocked(actors.get(0), "ne")){				
						actors.get(0).setNextTile("ne");
						actors.get(0).setMoving(true);
					}
				}
				else if (input.isKeyDown(Input.KEY_Z)) { // down and left
					if (!isBlocked(actors.get(0), "sw")){				
						actors.get(0).setNextTile("sw");
						actors.get(0).setMoving(true);
					}
				}
				else if (input.isKeyDown(Input.KEY_C)) { // down and right
					if (!isBlocked(actors.get(0), "se")){				
						actors.get(0).setNextTile("se");
						actors.get(0).setMoving(true);
					}
				}
				else if (input.isKeyDown(Input.KEY_S)){
					// TODO: implement rest
				}
			} else { // actor is moving
				//System.out.print("checking position.\n");
				if(actors.get(0).getPosition().equals(actors.get(0).getNextTile().scale(RogueGame.TILE_SIZE))){
					//System.out.print("stopped at tile\n");
					actors.get(0).gainEnergy();
					actors.get(0).setMoving(false);
				}else {
					//System.out.print("updating.\n");
					actors.get(0).update(delta);				
				}
			}
		}
		
	}

	@Override
	public int getID() {
		return RogueGame.PLAYINGSTATE;
	}
	
	private boolean isBlocked(Actor a, java.lang.String direction){
		Vector next = null;
		int xBlock, yBlock;
		switch(direction){
		case "n":
			next = a.seeNextTile("n");
			xBlock = (int) next.getX() ;
	        yBlock = (int) next.getY() ;
	        //System.out.print("next x and Y: " + xBlock + ", " + yBlock + "\n");
	        //System.out.print("position: " + a.getTilePosition() + "\nnext: " + next + "\n");
	        return blocked[xBlock][yBlock];
		case "e":
			next = a.seeNextTile("e");
			xBlock = (int) next.getX() ;
	        yBlock = (int) next.getY() ;
	        return blocked[xBlock][yBlock];
		case "s":
			next = a.seeNextTile("s");
			xBlock = (int) next.getX() ;
	        yBlock = (int) next.getY() ;
	        return blocked[xBlock][yBlock];
		case "w":
			next = a.seeNextTile("w");
			xBlock = (int) next.getX() ;
	        yBlock = (int) next.getY() ;
	        return blocked[xBlock][yBlock];
		case "nw":
			next = a.seeNextTile("nw");
			xBlock = (int) next.getX() ;
	        yBlock = (int) next.getY() ;
	        return blocked[xBlock][yBlock];
		case "ne":
			next = a.seeNextTile("ne");
			xBlock = (int) next.getX() ;
	        yBlock = (int) next.getY() ;
	        return blocked[xBlock][yBlock];
		case "sw":
			next = a.seeNextTile("sw");
			xBlock = (int) next.getX() ;
	        yBlock = (int) next.getY() ;
	        return blocked[xBlock][yBlock];
		case "se":
			next = a.seeNextTile("se");
			xBlock = (int) next.getX() ;
	        yBlock = (int) next.getY() ;
	        return blocked[xBlock][yBlock];
		default:
			return false;
		}
	}

}
