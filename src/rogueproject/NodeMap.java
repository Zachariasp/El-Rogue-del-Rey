package rogueproject;

import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.*;

public class NodeMap implements TileBasedMap{

	private TiledMap map;
	
	public NodeMap(TiledMap map){
		this.map = map;
	}

	@Override
	public boolean blocked(PathFindingContext context, int x, int y) {
		// TODO Auto-generated method stub
		return map.getTileProperty(map.getTileId(x, y, 0), "blocked", "false").equals("true");
	}

	@Override
	public float getCost(PathFindingContext context, int x, int y) {
		// TODO Auto-generated method stub
		return 1.0f;
	}

	@Override
	public int getHeightInTiles() {
		// TODO Auto-generated method stub
		return this.map.getHeight();
	}

	@Override
	public int getWidthInTiles() {
		// TODO Auto-generated method stub
		return this.map.getWidth();
	}

	@Override
	public void pathFinderVisited(int x, int y) {
		// TODO Auto-generated method stub
		
	}
	
}
