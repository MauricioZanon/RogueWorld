package com.rogueworld.world.mountain;

import com.rogueworld.entities.factories.EntityFactory;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.utils.rng.Noise;
import com.rogueworld.world.chunk.Chunk;

public class MountainLevel extends Chunk{
	
	private static Entity dirtFloor;
	private static Entity stoneFloor;
	private static Entity stoneWall;
	
	public MountainLevel(int posX, int posY){
		dirtFloor = EntityFactory.create("dirt floor");
		stoneFloor = EntityFactory.create("stone floor");
		stoneWall = EntityFactory.create("stone wall");
		coord = new int[] {posX, posY, 0};
		fillLevel();
		buildLevel();
	}

	@Override
	protected void buildLevel() {
		float[][] noise = Noise.generatePerlinNoise(SIZE, SIZE, 6);
		for(int i = 0; i < noise.length; i++) {
			for(int j = 0; j < noise[0].length; j++) {
				if(noise[i][j] > 0.45) {
					chunkMap[i][j].put(dirtFloor);
				}else if(noise[i][j] > 0.25){
					chunkMap[i][j].put(stoneFloor);
				}else {
					chunkMap[i][j].put(stoneWall);
				}
			}
		}
	}

}
