package rogueproject;

import jig.Entity;
import jig.ResourceManager;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
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
	Button title;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		container.setSoundOn(false);
		rg = (RogueGame)game;
		
		// Create Title
		title = new Button("El Rogue del Rey",
				(rg.ScreenWidth * 0.5f), (rg.ScreenHeight * 0.2f), 48);
		
		// Create Start Menu Buttons
		startButton = new Button("Start Game", 
				(rg.ScreenWidth * 0.5f), (rg.ScreenHeight * 0.5f), Button.MENU_LARGE, 20);
		exitButton = new Button("Exit", 
				rg.ScreenWidth * 0.5f, (rg.ScreenHeight * 0.5f) + 50, Button.MENU_LARGE, 20);
		

	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		
		Input input = container.getInput();
		//g.drawString("x: " + input.getAbsoluteMouseX() + ", y: " + input.getAbsoluteMouseY()
		//		, input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
		
		// Render Title
		title.render(g);
		// Render Buttons
		startButton.render(g);
		exitButton.render(g);
		
		// SpriteSheet() for tiles and entities
		// render objects, ie: bg.player.render(g);
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {

		Input input = container.getInput();
		//Input mouse = new Input(rg.ScreenHeight).addMouseListener(listener);
		
		if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)
				&& input.getAbsoluteMouseX() <= startButton.getCoarseGrainedMaxX()
				&& input.getAbsoluteMouseX() >= startButton.getCoarseGrainedMinX()
				&& input.getAbsoluteMouseY() <= startButton.getCoarseGrainedMaxY()
				&& input.getAbsoluteMouseY() >= startButton.getCoarseGrainedMinY()){
			//startButton.setText("Game Starting!");
			rg.enterState(RogueGame.PLAYINGSTATE);	
			//System.out.print("mouse pressed\n");
		}
	}
	
	@Override
	public int getID() {
		return RogueGame.STARTUPSTATE;
	}
	
}
