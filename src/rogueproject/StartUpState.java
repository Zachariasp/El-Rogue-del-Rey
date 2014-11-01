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
		
		if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)
				&& input.getAbsoluteMouseX() <= startButton.getCoarseGrainedMaxX()
				&& input.getAbsoluteMouseX() >= startButton.getCoarseGrainedMinX()
				&& input.getAbsoluteMouseY() <= startButton.getCoarseGrainedMaxY()
				&& input.getAbsoluteMouseY() >= startButton.getCoarseGrainedMinY()){

			rg.enterState(RogueGame.PLAYINGSTATE);	
			//System.out.print("mouse pressed\n");
		}
	}
	
	@Override
	public int getID() {
		return RogueGame.STARTUPSTATE;
	}
	
}
