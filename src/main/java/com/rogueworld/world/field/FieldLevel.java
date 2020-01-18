package com.rogueworld.world.field;

import com.rogueworld.entities.factories.EntityFactory;
import com.rogueworld.utils.rng.Noise;
import com.rogueworld.world.chunk.Chunk;

public class FieldLevel extends Chunk{
	
	public FieldLevel(int posX, int posY){
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
	}

}
