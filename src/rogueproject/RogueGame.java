package rogueproject;

import java.util.ArrayList;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;

/* All this to write text. */
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import java.awt.Font;
import java.awt.Color;

/**
 * 
 * @author Zacharias Shufflebarger
 *
 */
public class RogueGame extends StateBasedGame{

	public static final int STARTUPSTATE = 0;
	public static final int PLAYINGSTATE = 1;
	public static final int GAMEOVERSTATE = 2;
	// other possible states: CharacterSelect, Inventory, Pause, Menu, Settings
	
	// create image, animation, and sound macros
	// ie: public static final String GAMEOVER_BANNER_RSC = "rogueproject/resource/gameover.png";
	
	public final int ScreenWidth;
	public final int ScreenHeight;
	public static final int TILE_SIZE = 16; //DawnLike uses 16x16 tiles. This will mainly help readability.
	
	//public final UnicodeFont alagardFont = new UnicodeFont(
	//		new java.awt.Font("Alagard", Font.BOLD, 20));
	
	public static final String GOLDGUI_IMG_RSC = "rogueproject/resource/goldui_big_pieces_0.png"; 
	public static final String GUI_MENULARGE_IMG_RSC = "rogueproject/resource/menu_large.png";
	public static final String HIT_REDNUMBERS0_IMG_RSC = "rogueproject/resource/boxy_bold_fat/gradient/boxy_bold_fat_gradient_red.png";
	public static final String HIT_REDNUMBERS1_IMG_RSC = "rogueproject/resource/boxy_bold_fat/edged/boxy_bold_fat_edge_red.png";
	
	public static final String ACTOR_PLAYER0_IMG_RSC = "rogueproject/resource/DawnLike_3/Characters/Player0.png";
	public static final String ACTOR_PLAYER1_IMG_RSC = "rogueproject/resource/DawnLike_3/Characters/Player1.png";
	public static final String ACTOR_UNDEAD0_IMG_RSC = "rogueproject/resource/DawnLike_3/Characters/Undead0.png";
	public static final String ACTOR_UNDEAD1_IMG_RSC = "rogueproject/resource/DawnLike_3/Characters/Undead1.png";
	
	public static final String ALAGARD_FONT_RSC =  "rogueproject/resource/fonts/alagard_by_pix3m-d6awiwp.ttf";
	
	public static final int WARRIOR = 0;
	Player player;
	Actor[][] actors2d; // for collision detection
	boolean[][] blocked;
	boolean[][] occupied; // for collision detection with actors
	NodeMap pathmap;
	ArrayList<Damage> hits;
	
	//TODO ArrayList<Objects> objects;
	
	public RogueGame(String title, int width, int height) {
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;
		
		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
				
	}
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new StartUpState());
		//addState(new GameOverState());
		addState(new PlayingState());
		
		// preload resources here
		// images: ResourceManager.loadImage(IMG_RSC);
		// sounds: ResourceManager.loadSound(SND_RSC);
		ResourceManager.loadImage(GOLDGUI_IMG_RSC);
		ResourceManager.loadImage(GUI_MENULARGE_IMG_RSC);
		ResourceManager.loadImage(ACTOR_PLAYER0_IMG_RSC);
		ResourceManager.loadImage(ACTOR_PLAYER1_IMG_RSC);
		ResourceManager.loadImage(ACTOR_UNDEAD0_IMG_RSC);
		ResourceManager.loadImage(ACTOR_UNDEAD1_IMG_RSC);
		ResourceManager.loadImage(HIT_REDNUMBERS0_IMG_RSC);
		ResourceManager.loadImage(HIT_REDNUMBERS1_IMG_RSC);

		player = new Player(WARRIOR);
		hits = new ArrayList<Damage>(10);
	}
	
	public static void main(String[] args) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new RogueGame("El Rogue del Rey", 1024, 720));
			app.setDisplayMode(1024, 720, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}
	
}
