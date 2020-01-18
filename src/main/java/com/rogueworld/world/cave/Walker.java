package com.rogueworld.world.cave;

import java.util.HashSet;
import java.util.Set;

import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.factories.EntityFactory;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;
import com.rogueworld.utils.rng.RNG;
import com.rogueworld.world.map.Map;
import com.rogueworld.world.tile.Tile;

public class Walker {
	
	private PositionC position;
	private static final Entity DIRT_FLOOR = EntityFactory.create("dirt floor");
	private static final Entity STONE_FLOOR = EntityFactory.create("stone floor");
	protected static Set<Tile> floorTiles = new HashSet<>();
	
	protected boolean activated = true;
	
	private static RNG rng = RNG.getInstance();
	
	protected Walker(Tile startingTile) {
		position = startingTile.pos;
		digFloor(startingTile);
	}
	
	protected void dig() {
		Set<Tile> emptyTiles = Map.getOrthogonalTiles(position.getTile(), t -> t.get(Type.TERRAIN) == null);
		if(emptyTiles.isEmpty()) {
			activated = false;
		}
		else {
			Tile tile = rng.getRandom(emptyTiles);
			position = tile.pos;
			if(tile.get(Type.TERRAIN) == null) {
				digFloor(tile);
				if(rng.nextFloat() < 0.005) {
					digDown(tile);
				}
			}
		}
	}
	
	private void digFloor(Tile t) {
		t.put(floorTiles.size() < 200 ? DIRT_FLOOR : STONE_FLOOR);
		floorTiles.add(t);
	}
	
	private void digDown(Tile t) {
		t.put(EntityFactory.create("down stair"));
		
		position = position.clone();
		position.coord[2]++;
		
		t = position.getTile();
		digFloor(t);
		t.put(EntityFactory.create("up stair"));
	}
	
	protected Walker reproduce() {
		Tile tile = rng.getRandom(floorTiles, t -> Map.isOrthogonallyAdjacent(t, ti -> ti.get(Type.TERRAIN) == null));
		return tile != null ? null : new Walker(tile);
	}

	protected PositionC getPosition() {
		return position;
	}

	protected static int getDiggedTiles() {
		return floorTiles.size();
	}

}
