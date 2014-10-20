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
		
		actors.add(new Actor(1, 10, 2, 2, 0, 30, 25)); // The player will always be actors.get(0)
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		RogueGame rg = (RogueGame)game;
		
		map.render(0, 0); // renders the map on screen at (x, y)
		
		for(Actor a : actors){
			a.render(g);
			//System.out.print("(" + a.getX() + ", " + a.getY() + ")\n");
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		Input input = container.getInput();
		if (input.isKeyDown(Input.KEY_W)) { // up
			
		}
        else if (input.isKeyDown(Input.KEY_S)) { // down
        	
        }
        else if (input.isKeyDown(Input.KEY_A)) { // left
        	
        }
        else if (input.isKeyDown(Input.KEY_D)) { // right
        	
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
