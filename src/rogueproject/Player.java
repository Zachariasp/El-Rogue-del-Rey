package rogueproject;

import jig.ResourceManager;

import org.newdawn.slick.Animation;

public class Player extends Actor{

	private int depth; // track the player's depth in the dungeon
	private int orders; // store the input here for acting
	private int classtype;
	private Animation anim;
	
	public Player(int charClass){
		super();
		classtype = charClass;
		depth = 1;
		setTypeAttributes();
		getTypeImage();
		addAnimation(anim);
		anim.setLooping(true);
	}
	/* Getters */
	
	public int getDepth(){
		return this.depth;
	}
	
	public int getOrders(){
		return this.orders;
	}
	
	@Override
	public void getTypeImage(){
		switch(this.classtype){
		case 0:			
			this.anim = new Animation(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_PLAYER0_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					, 0, 0, 0, 0, true, 300, true);
			this.anim.addFrame(ResourceManager.getSpriteSheet(
					RogueGame.ACTOR_PLAYER1_IMG_RSC, RogueGame.TILE_SIZE, RogueGame.TILE_SIZE)
					.getSprite(0, 0), 300);
			break;
		default:
			break;
		}
	}
	
	/* Setters */
	
	public void setTypeAttributes(){
		switch(this.getType()){
		case 0:
			this.setLevel(1);
			this.setHitPonts(10);
			this.setAttack(2);
			this.setArmor(2);
			this.setEnergy(1);
			this.setGain(1);
			break;
		default:
			break;
		}
	}
	
	public void setDepth(int set){
		this.depth = set;
	}
	
	public void setOrders(int set){
		this.orders = set;
	}
	
	/* Actions */
	
	public void act(){
		if(orders != PlayingState.REST){
			
		} else {
			//TODO rest
		}
	}
	
}
