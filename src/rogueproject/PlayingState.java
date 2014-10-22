package rogueproject;

import java.awt.Font;
import java.util.ArrayList;

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
		blocked = new boolean[map.getWidth()][map.getHeight()];
		occupied = new boolean[map.getWidth()][map.getHeight()]; // TODO: make this a thing
		// Build collision detection for map tiles
		for (int i = 0; i < map.getWidth(); i++){
			for (int j = 0; j < map.getHeight(); j++){
				int tileID = map.getTileId(i, j, 0);
				String value = map.getTileProperty(tileID, "blocked", "false");
				if ("true".equals(value)){
					blocked[i][j] = true;
				}
			}
		}
		
		// The player will always be actors.get(0)
		actors.add(new Actor(1, 10, 2, 2, 0, 1, 30, 25)); 
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
			if(!actors.get(0).isMoving()){ // handle all user input in this block
				// TODO: collision detection against walls.
				// Directional Keys
				//   Q   W   E
				//    \  |  /
				// A - restS - D
				//    /  |  \
				//   Z   X   C
				if (input.isKeyDown(Input.KEY_W)) { // up
					actors.get(0).setNextTile("n");
					actors.get(0).setMoving(true);
				}
				else if (input.isKeyDown(Input.KEY_X)) { // down
					actors.get(0).setNextTile("s");
					actors.get(0).setMoving(true);
				}
				else if (input.isKeyDown(Input.KEY_A)) { // left
					actors.get(0).setNextTile("w");
					actors.get(0).setMoving(true);
				}
				else if (input.isKeyDown(Input.KEY_D)) { // right
					actors.get(0).setNextTile("e");
					actors.get(0).setMoving(true);
				}
				else if (input.isKeyDown(Input.KEY_Q)) { // up and left
					actors.get(0).setNextTile("nw");
					actors.get(0).setMoving(true);
				}
				else if (input.isKeyDown(Input.KEY_E)) { // up and right
					actors.get(0).setNextTile("ne");
					actors.get(0).setMoving(true);
				}
				else if (input.isKeyDown(Input.KEY_Z)) { // down and left
					actors.get(0).setNextTile("sw");
					actors.get(0).setMoving(true);
				}
				else if (input.isKeyDown(Input.KEY_C)) { // down and right
					actors.get(0).setNextTile("se");
					actors.get(0).setMoving(true);
				}
				else if (input.isKeyDown(Input.KEY_S)){
					// TODO: implement rest
				}
			} else {
				if(actors.get(0).getTilePosition().equals(actors.get(0).getNextTile())){
					actors.get(0).gainEnergy();
					actors.get(0).setMoving(false);
				}else {
					actors.get(0).update(delta);				
				}
			}
		}
		
	}

	@Override
	public int getID() {
		return RogueGame.PLAYINGSTATE;
	}
	
	private boolean isBlocked(float x, float y)
    {
        int xBlock = (int)x / RogueGame.TILE_SIZE;
        int yBlock = (int)y / RogueGame.TILE_SIZE;
        return blocked[xBlock][yBlock];
    }

}
