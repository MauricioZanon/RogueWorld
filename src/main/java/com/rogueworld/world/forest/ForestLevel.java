package com.rogueworld.world.forest;

import com.rogueworld.entities.components.MovementC.MovementType;
import com.rogueworld.entities.factories.EntityFactory;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;
import com.rogueworld.utils.rng.Noise;
import com.rogueworld.utils.rng.RNG;
import com.rogueworld.world.chunk.Chunk;
import com.rogueworld.world.tile.Tile;

public class ForestLevel extends Chunk{
	
	private static RNG rng = RNG.getInstance();
	
	public ForestLevel(int posX, int posY){
		coord = new int[] {posX, posY, 0};
		fillLevel();
		buildLevel();
	}
	
	@Override
	protected void buildLevel() {
		float[][] noise = Noise.generatePerlinNoise(SIZE, SIZE, 5);
		for(int i = 0; i < noise.length; i++) {
			for(int j = 0; j < noise[0].length; j++) {
				if(noise[i][j] > 0.25) {
					chunkMap[i][j].put(EntityFactory.create("grass floor"));
				}else {
					chunkMap[i][j].put(EntityFactory.create("dirt floor"));
				}
			}
		}
		
		placeTrees();
		
		for(int i = 0; i < rng.nextGaussian(SIZE / 2, SIZE / 10); i++){
			Tile tile = rng.getRandom(chunkMap, t -> t.isTransitable(MovementType.WALK) && t.get(Type.FEATURE) == null);
			Entity feature = EntityFactory.create("rock");
			tile.put(feature);
		}
	}
	
	private void placeTrees() {
		for(int i = 0; i < rng.nextGaussian(SIZE / 2, SIZE / 10); i++){
			Tile tile = rng.getRandom(chunkMap, t -> t.isTransitable(MovementType.WALK) && t.get(Type.FEATURE) == null);
			Entity feature = EntityFactory.create("apple tree");
			tile.put(feature);
		}
	}

}
