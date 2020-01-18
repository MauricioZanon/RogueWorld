package com.rogueworld.world.world;

import com.rogueworld.entities.components.PositionC;
import com.rogueworld.world.tile.Tile;

public enum Direction {
	
	/** (0;-1) */
	N(0, -1),
	/** (1;-1) */
	NE(1, -1),
	/** (1;0) */
	E(1, 0),
	/** (1;1) */
	SE(1, 1),
	/** (0;1) */
	S(0, 1),
	/** (-1;1) */
	SW(-1, 1),
	/** (-1;0) */
	W(-1, 0),
	/** (-1;-1) */
	NW(-1, -1);
	
	public int movX;
	public int movY;
	
	private static Direction[][] grid = {{NW, W, SW},{N, null, S},{NE, E, SE}};
	
	Direction(int x, int y){
		movX = x;
		movY = y;
	}

	public static Direction get(Tile t1, Tile t2){
		return get(t1.pos, t2.pos);
	}
			
	public static Direction get(PositionC p1, PositionC p2){
		int[] coord1 = p1.coord;
		int[] coord2 = p2.coord;
		
		int dx = (int) Math.signum(coord2[0] - coord1[0]);
		int dy = (int) Math.signum(coord2[1] - coord1[1]);
		
		return grid[dx+1][dy+1];
	}
	
	public static Direction getOpossite(Direction dir){
		return grid[-dir.movX + 1][-dir.movY + 1];
	}
	
	public static Direction[] getOrthogonal() {
		return new Direction[]{S, E, N, W};
	}
}
