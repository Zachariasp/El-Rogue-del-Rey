package rogueproject;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Image;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import java.awt.Font;
import java.awt.Color;

/**
 * 
 * @author Zacharias Shufflebarger
 *
 * Basic menu buttons centered at (x, y) with a width, text message, and visual style.
 * Buttons depress when selected.
 */
public class Button extends Entity {
	
	public static final int MENU_LARGE = 0;
	
	private java.lang.String text;
	private int textSize;
	private float width;
	private float height;
	private int style;
	
	private Animation select; // Perhaps we will use this to animate the buttons... Maybe.
	
	/* Constructor */
	
	public Button(java.lang.String string, float x, float y, int bstyle, int tsize)
			throws SlickException{
		super(x, y);
		text = string;
		textSize = tsize;
		style = bstyle;	
		getStyleImage();
		
	}
	
	public Button(java.lang.String string, float x, float y, int tsize)
			throws SlickException{
		super(x, y);
		text = string;	
		textSize = tsize;
		style = -1;
		getStyleImage();
		
	}
	
	public Button(java.lang.String string, float x, float y)
			throws SlickException{
		super(x, y);
		text = string;	
		style = -1;
		getStyleImage();
		
	}
	
	/* Getters */
	public String getText(){
		return text;
	}
	
	public int getTextSize(){
		return textSize;
	}
	
	public int getStyle(){
		return style;
	}
	
	public float getWidth(){
		return width;
	}
	
	public float getHeight(){
		return height;
	}
	
	public void getStyleImage(){
		//Image gui = ResourceManager.getImage(RogueGame.GOLDGUI_IMG_RSC);
		switch(style){
		case 0:
			addImageWithBoundingBox(ResourceManager.getImage(RogueGame.GUI_MENULARGE_IMG_RSC));
		default:
			break;
		}
	}
	
	/* Static Getter for Style*/
	public static float getHeightfromStyle(int style){
		switch(style){
		case 0:
			return 40;
		default:
			return 0;
		}
	}
	
	/* Setters */
	public void setText(java.lang.String set){
		text = set;
	}
	
	public void setTextSize(int set){
		textSize = set;
	}
	
	public void setStyle(int set){
		style = set;
	}
	
	public void setWidth(float set){
		width = set;
	}
	
	
	/**
	*/
	@Override
	public void render(Graphics g){
		super.render(g);
		
		UnicodeFont alagardFont = new UnicodeFont(
				new java.awt.Font("Alagard", Font.BOLD, textSize));
		alagardFont.getEffects().add(new ColorEffect(java.awt.Color.white));
		alagardFont.addNeheGlyphs();
		try {
			alagardFont.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		g.setFont(alagardFont);
		g.drawString(text,
			getX() - (alagardFont.getWidth(text)/2),
			getY() - (alagardFont.getHeight(text)/2));		
	}
	
	/* Action */
	// TODO: clickButton(), toggleButton()
}
