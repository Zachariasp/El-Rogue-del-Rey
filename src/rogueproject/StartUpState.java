package rogueproject;

import jig.ResourceManager;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
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
public class StartUpState extends BasicGameState {
	RogueGame rg;
	
	/* List of startup buttons */
	Button startButton;
	Button exitButton;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		container.setSoundOn(false);
		rg = (RogueGame)game;
		
		// Create Start Menu Buttons
		startButton = new Button("Start Game", 
				(rg.ScreenWidth * 0.5f), (rg.ScreenHeight * 0.5f), Button.MENU_LARGE);
		exitButton = new Button("Exit", 
				rg.ScreenWidth * 0.5f, (rg.ScreenHeight * 0.5f) + 50, Button.MENU_LARGE);

	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		
		// Draw Title
		UnicodeFont alagardFont = new UnicodeFont(
				new java.awt.Font("Alagard", Font.BOLD, 48));
		alagardFont.getEffects().add(new ColorEffect(java.awt.Color.white));
		alagardFont.addNeheGlyphs();
		alagardFont.loadGlyphs();
		g.setFont(alagardFont);
		g.drawString("El Rogue del Rey",
			(rg.ScreenWidth * 0.5f) - (alagardFont.getWidth("EL Rogue Del Rey")/2),
			(rg.ScreenHeight * 0.2f));
		
		// Render Buttons
		startButton.render(g);
		exitButton.render(g);
		startButton.drawText(g);
		exitButton.drawText(g);
		
		// SpriteSheet() for tiles and entities
		// render objects, ie: bg.player.render(g);
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {

		//Input input = container.getInput();
		
	}
	
	@Override
	public int getID() {
		return RogueGame.STARTUPSTATE;
	}
	
}
