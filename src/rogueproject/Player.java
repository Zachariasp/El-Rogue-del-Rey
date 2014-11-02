package rogueproject;

import jig.ResourceManager;

import org.newdawn.slick.Animation;

public class Player extends Actor{

	private int depth; // track the player's depth in the dungeon
	private int orders = PlayingState.WAIT; // store the input here for acting
	private int classtype;
	private boolean turn = true;
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
	
	public int getDepth()		{return this.depth;}
	public int getOrders()		{return this.orders;}
	public boolean getTurn()	{return this.turn;}
	
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
			this.setEnergy(0);
			this.setGain(1);
			break;
		default:
			break;
		}
	}
	
	public void setDepth(int set)		{this.depth = set;}
	public void setOrders(int set)		{this.orders = set;}
	public void setTurn(boolean set)	{this.turn = set;}
	
	/* Actions */
	
	@Override
	public boolean act(RogueGame rg){
		// TODO: set players turn to false for anything that doesn't involve moving, otherwise, let the update portion handle it.
		// deplete energy for all actions, even rest. Energy depletion should be handled when the action is performed.
		// Return false if the action is illegal, like walking into a wall.
		if(this.orders != PlayingState.WAIT && getEnergy() >= 1){ // only act if the action is not wait and there is enough energy to act
			if(this.orders != PlayingState.REST){
				setNextTile(getOrders());
				this.orders = PlayingState.WAIT;
				// if the player indicates a direction that is not an obstacle, enemy, or object, move into the open tile.
				if (!isBlocked(rg.blocked) && !isOccupied(rg.occupied)){
					move();
					this.consumeEnergy();
					return true;
				}
				// if the indicated direction is an enemy, attack it.
				else if (isOccupied(rg.occupied)){
					if(rg.actors2d[(int)this.getNextTile().getX()][(int)this.getNextTile().getY()] != null){
						attack(rg.actors2d[(int)this.getNextTile().getX()][(int)this.getNextTile().getY()]);
						this.consumeEnergy();
						setNextTile(PlayingState.WAIT);
						try{
							Thread.sleep(250);
						} catch (InterruptedException e){
							System.err.println("sleep error: " + e.getMessage());
						}
						//rg.hits.add(new Damage(this.getNextTile().getX(), this.getNextTile().getY(),0));
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
			} else {
				//TODO rest
				this.consumeEnergy();
				return true;
			}
		} else if (getEnergy() < 1){
			this.turn = false;
			return false;
		}
		return false;
	}
	
	public boolean isBlocked(boolean[][] blocked){
		return blocked[(int)this.getNextTile().getX()][(int)this.getNextTile().getY()];
	}
	
	public boolean isOccupied(boolean[][] occupied){
		return occupied[(int)this.getNextTile().getX()][(int)this.getNextTile().getY()];
	}
	
	public void move(){
		setMoving(true);
	}
	
	public void attack(Actor enemy){
		// damage done to enemy is player's attack minus enemies armor. if that is less than 0, do 0 damage instead.
		enemy.setHitPonts(enemy.getHitPoints() - Math.max(getAttack() - enemy.getArmor(), 0));
		System.out.println("Player attack = " + this.getAttack() + 
				", Enemy armor = " + enemy.getArmor() + 
				", damage = " + Math.max(getAttack() - enemy.getArmor(), 0));
	}
	
}
