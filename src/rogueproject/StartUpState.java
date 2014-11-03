package rogueproject;




import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ResourceLoader;


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
		
		// Render Title
		title.render(g);
		// Render Buttons
		startButton.render(g);
		exitButton.render(g);
	
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {

		Input input = container.getInput();
		
		if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)
				&& input.getAbsoluteMouseX() <= startButton.getCoarseGrainedMaxX()
				&& input.getAbsoluteMouseX() >= startButton.getCoarseGrainedMinX()
				&& input.getAbsoluteMouseY() <= startButton.getCoarseGrainedMaxY()
				&& input.getAbsoluteMouseY() >= startButton.getCoarseGrainedMinY()){

			rg.enterState(RogueGame.PLAYINGSTATE);	
			//System.out.print("mouse pressed\n");
		}
		if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)
				&& input.getAbsoluteMouseX() <= exitButton.getCoarseGrainedMaxX()
				&& input.getAbsoluteMouseX() >= exitButton.getCoarseGrainedMinX()
				&& input.getAbsoluteMouseY() <= exitButton.getCoarseGrainedMaxY()
				&& input.getAbsoluteMouseY() >= exitButton.getCoarseGrainedMinY()){
			System.out.println("exit!!");
			container.exit();
		}
	}
	
	@Override
	public int getID() {
		return RogueGame.STARTUPSTATE;
	}
	
}
