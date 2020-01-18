package com.rogueworld.world.cave;

import java.util.HashSet;
import java.util.Set;

import com.rogueworld.entities.components.MovementC.MovementType;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.factories.EntityFactory;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;
import com.rogueworld.utils.rng.RNG;
import com.rogueworld.world.main.MultiLevelLocation;
import com.rogueworld.world.map.Map;
import com.rogueworld.world.tile.Tile;
import com.rogueworld.world.world.WorldBuilder;

public class Cave extends MultiLevelLocation{
	
	private static RNG rng = RNG.getInstance();
	
	/**Hecho con random walks*/
	public Cave(PositionC entranceStairPos, CaveSize size) {
		placeInitialStairs(entranceStairPos);
		
		PositionC initialPos = entranceStairPos.clone();
		initialPos.coord[2]++;
		dig(initialPos.getTile(), size.floorTiles);
		
		putWalls();
		
		Walker.floorTiles.clear();
		
		WorldBuilder.getLocations().add(this);
	}
	
	private void placeInitialStairs(PositionC entranceStairPos) {
		entranceStairPos.getTile().put(EntityFactory.create("down stair"));

		PositionC exitStairPos = entranceStairPos.clone();
		exitStairPos.coord[2] += 1;
		exitStairPos.getTile().put(EntityFactory.create("up stair"));
	}
	
	private void dig(Tile startingTile, int floorTilesAmount) {
		Set<Walker> miners = new HashSet<>();
		miners.add(new Walker(startingTile));
		
		while(Walker.getDiggedTiles() < floorTilesAmount) {
			Set<Walker> newMiners = new HashSet<>();
			Set<Walker> deactivatedMiners = new HashSet<>();
			
			for(Walker miner : miners) {
				if(miner.activated) {
					miner.dig();
					if(rng.nextFloat() < 0.01) { // Ver como afecta a la eficiencia esta chance de reproducirse
						Walker newMiner = miner.reproduce();
						if(newMiner != null) {
							newMiners.add(newMiner);
						}
					}
				}
				else {
					deactivatedMiners.add(miner);
				}
			}
			miners.addAll(newMiners);
			miners.removeAll(deactivatedMiners);
			if(miners.isEmpty()) {
				miners.add(new Walker(rng.getRandom(Walker.floorTiles, t -> Map.isOrthogonallyAdjacent(t, ti -> ti.get(Type.TERRAIN) == null))));
			}
		}
	}
	
	private void putWalls() {
		Entity dirtWall = EntityFactory.create("dirt wall");
		Entity dirtFloor = EntityFactory.create("dirt floor");
		Entity stoneWall = EntityFactory.create("stone wall");
		Entity stoneFloor = EntityFactory.create("stone floor");
		
		for(Tile tile : Walker.floorTiles) {
			for(Tile emptyTile : Map.getAdjacentTiles(tile, t -> t.get(Type.TERRAIN) == null)) {
				if(Map.countOrthogonalAdjacency(emptyTile, t -> !t.isTransitable(MovementType.WALK)) != 0) {
					emptyTile.put(tile.get(Type.TERRAIN).name.contains("dirt") ? dirtWall : stoneWall);
				}
				else {
					emptyTile.put(tile.get(Type.TERRAIN).name.contains("dirt") ? dirtFloor : stoneFloor);
				}
				if(emptyTile.get(Type.TERRAIN).name.contains("wall") && rng.nextFloat() < 0.005) {
					emptyTile.put(EntityFactory.create("iron vein"));
				}
				else if(emptyTile.get(Type.TERRAIN).name.contains("floor")) {
					emptyTile.put(EntityFactory.create("luminescent mushroom"));
				}
			}
		}
	}
	
	public enum CaveSize{
		TINY(100),
		SMALL(200),
		MEDIUM(400),
		BIG(800),
		HUGE(1600);
		
		private int floorTiles;
		
		CaveSize(int tiles) {
			floorTiles = rng.nextGaussian(tiles, tiles/33);
		}

		public int getFloorTiles() {
			return floorTiles;
		}
	}
	
}
