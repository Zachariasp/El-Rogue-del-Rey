package rogueproject;

import java.util.ArrayList;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;

import org.newdawn.slick.util.ResourceLoader;
/* All this to write text. */
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import java.awt.Font;
import java.awt.Color;
import java.io.InputStream;

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
	
	public static final String ACTOR_PLAYER0_IMG_RSC = "rogueproject/resource/DawnLike_3/Characters/Player0.png";
	public static final String ACTOR_PLAYER1_IMG_RSC = "rogueproject/resource/DawnLike_3/Characters/Player1.png";
	public static final String ACTOR_UNDEAD0_IMG_RSC = "rogueproject/resource/DawnLike_3/Characters/Undead0.png";
	public static final String ACTOR_UNDEAD1_IMG_RSC = "rogueproject/resource/DawnLike_3/Characters/Undead1.png";
	public static final String ACTOR_PEST0_IMG_RSC = "rogueproject/resource/DawnLike_3/Characters/Pest0.png";
	public static final String ACTOR_PEST1_IMG_RSC = "rogueproject/resource/DawnLike_3/Characters/Pest1.png";
	
	public static final String ALAGARD_FONT_RSC =  "rogueproject/resource/fonts/alagard_by_pix3m-d6awiwp.ttf";
	
	// Font from Joel
	Font awtFont;
	TrueTypeFont courierBOLD12;
	TrueTypeFont custom12;
	
	public static final int WARRIOR = 0;
	Player player;
	ArrayList<Actor> actors;
	boolean[][] blocked;
	boolean[][] occupied; // for collision detection with actors
	NodeMap pathmap;
	
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
		ResourceManager.loadImage(ACTOR_PEST0_IMG_RSC);
		ResourceManager.loadImage(ACTOR_PEST1_IMG_RSC);


		player = new Player(WARRIOR);
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
