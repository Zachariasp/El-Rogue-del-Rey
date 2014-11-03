package rogueproject;

import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.Animation;

public class Player extends Actor{

	private int depth; // track the player's depth in the dungeon
	private int orders = PlayingState.WAIT; // store the input here for acting
	private int classtype;
	//private boolean turn = true;
	private Animation anim;
	
	public Player(int charClass){
		super();
		classtype = charClass;
		depth = 1;
		setTypeAttributes();
		getTypeImage();
		addAnimation(anim);
		anim.setLooping(true);
		this.setTurn(true);
	}
	/* Getters */
	
	public int getDepth()		{return this.depth;}
	public int getOrders()		{return this.orders;}
	
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
			this.setHitPonts(50);
			this.setAttack(2);
			this.setArmor(0);
			this.setEnergy(0);
			this.setGain(1);
			break;
		default:
			break;
		}
	}
	
	public void setDepth(int set)		{this.depth = set;}
	public void setOrders(int set)		{this.orders = set;}
	
	/* Actions */
	
	@Override
	public boolean act(RogueGame rg){
		if(this.orders != PlayingState.WAIT && getEnergy() >= 1){ // only act if the action is not wait and there is enough energy to act
			setNextTile(getOrders());
			this.orders = PlayingState.WAIT;
			// rest
			if(this.getNextTile().equals(this.getTilePosition())){
				// TODO make rest regen health
				this.consumeEnergy();
				this.setTurn(false);
				return true;
			}
			// if the player indicates a direction that is not an obstacle, enemy, or object, move into the open tile.
			else if (!isBlocked(rg.blocked) && !isOccupied(rg.occupied)){
				move();
				this.consumeEnergy();
				return true;
			}
			// if the indicated direction is an enemy, attack it.
			else if (isOccupied(rg.occupied)){
				System.out.println("Tile " + this.getNextTile() + " is occupied.");
				for(Actor a : rg.actors){
					System.out.println("actor position = " + a.getTilePosition() + ", player next tile = " + this.getNextTile());
					if(a.getTilePosition().equals(this.getNextTile())){
						System.out.println("attacking actor at " + a.getTilePosition());
						attackActor(a);
						this.consumeEnergy();
						setNextTile(PlayingState.WAIT);
						this.setTurn(false);
						try{
							Thread.sleep(200);
						} catch (InterruptedException e){
							System.err.println("sleep error: " + e.getMessage());
						}
					}
				}
					// TODO add 
					//			// if the indicated direction is an object, interact with it.
					//			else if (rg.objects[(int)this.getNextTile().getX()][(int)this.getNextTile().getY()] != null){
					//				interact(rg.objects[(int)this.getNextTile().getX()][(int)this.getNextTile().getY()]);
					//			}
				return true;
			}
			else{
				setNextTile(PlayingState.WAIT); // go nowhere
				return false;
			}
		} else if (getEnergy() < 1){
			return false;
		}
		return false;
	}

	
	
}
