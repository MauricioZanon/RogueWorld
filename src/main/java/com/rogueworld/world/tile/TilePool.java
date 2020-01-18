package com.rogueworld.world.tile;

import java.util.ArrayDeque;

import com.rogueworld.entities.components.PositionC;

public class TilePool {
	
	public static final int MAX_TILES = 8192;
	private static ArrayDeque<Tile> pool = new ArrayDeque<>(MAX_TILES);
	
	private TilePool() {}
	
	public static Tile get(int x, int y, int z) {
		PositionC pos = new PositionC();
		pos.coord = new int[] {x, y, z};
		if(pool.isEmpty()) {
			return new Tile(pos);
		}else {
			Tile t = pool.pollFirst();
			t.pos = pos;
			return t;
		}
	}
	
	public static void returnTile(Tile t) {
		if(t != null) {
			t.clear();
			pool.add(t);
		}
	}

}
