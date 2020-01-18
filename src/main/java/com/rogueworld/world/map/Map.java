package com.rogueworld.world.map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;

import com.rogueworld.ai.eventsystem.EventSystem;
import com.rogueworld.application.Main;
import com.rogueworld.entities.components.AIC;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.utils.persistency.StateLoader;
import com.rogueworld.utils.persistency.StateSaver;
import com.rogueworld.world.chunk.Chunk;
import com.rogueworld.world.tile.Tile;
import com.rogueworld.world.tile.TilePool;
import com.rogueworld.world.world.Direction;
import com.rogueworld.world.world.WorldBuilder;

/**
 * Guarda los últimos chunks que se usaron y tiene varias formas de encontrar tiles
 */
public abstract class Map {
	
	public static final int MAX_NUMBER_OF_MAPS = TilePool.MAX_TILES / (Chunk.SIZE^2);
	
	private static TreeMap<String, Chunk> chunksInMemory = new TreeMap<>();
	private static LinkedHashSet<String> lastUsedChunks = new LinkedHashSet<>();
	
	private static int chunkSize = Chunk.SIZE;
	private static Chunk[][] mapInChunks = new Chunk[3][3];
	private static Tile[][] mapInTiles = new Tile[chunkSize*3][chunkSize*3];
	private static int currentZLevel = 0;
	
	public static void refresh(){
		Chunk center = mapInChunks[1][1];
		PositionC playerPos = Main.player.get(PositionC.class);
		int gx0 = playerPos.getGx();
		int gy0 = playerPos.getGy();
		int gz0 = playerPos.getGz();
		
		if(center == null || gx0 != center.getCoord()[0] || gy0 != center.getCoord()[1] || gz0 != currentZLevel) {
			
			currentZLevel = gz0;
			
			Set<Entity> npcs = new HashSet<>();
			for(int gx = 0; gx < mapInChunks.length; gx++) {
				for(int gy = 0; gy < mapInChunks[0].length; gy++) {
					int chunkX = gx0 - 1 + gx;
					int chunkY = gy0 - 1 + gy;
					Chunk chunk = getChunk(chunkX, chunkY, gz0);
					mapInChunks[gx][gy] = chunk;
					
					Tile[][] chunkMap = chunk.getMap();
					int tileMapX0 = gx*chunkSize;
					int tileMapY0 = gy*chunkSize;
					for(int lx = 0; lx < chunkSize; lx++) {
						for(int ly = 0; ly < chunkSize; ly++) {
							Tile tile = chunkMap[lx][ly];
							if(tile != null) {
								mapInTiles[lx + tileMapX0][ly + tileMapY0] = tile;
								npcs.addAll(tile.getEntities(e -> e.has(AIC.class)));
							}
						}
					}
				}
			}
			npcs.remove(Main.player);
			EventSystem.setTimedEntities(npcs);
		}
	}
	
	public static Chunk getChunk(int x, int y, int z){
		String posString = x + ":" + y + ":" + z;
		Chunk chunk = chunksInMemory.get(posString);
		if(chunk == null) {
			chunk = StateLoader.getInstance().loadChunk(posString);
			chunksInMemory.put(posString, chunk);
			while(chunksInMemory.size() > MAX_NUMBER_OF_MAPS && !WorldBuilder.isBuilding) {
				String chunkPosToRemove = lastUsedChunks.iterator().next();
				lastUsedChunks.remove(chunkPosToRemove);
				StateSaver.getInstance().addChunkToSaveList(chunksInMemory.remove(chunkPosToRemove));
			}
		}
		lastUsedChunks.remove(posString);
		lastUsedChunks.add(posString);
		return chunk;
	}
	
	public static Chunk getChunk(Tile tile) {
		int[] coord = tile.pos.coord;
		int gx = coord[0] >= 0 ? coord[0]/Chunk.SIZE : coord[0]/Chunk.SIZE - 1;
		int gy = coord[1] >= 0 ? coord[1]/Chunk.SIZE : coord[1]/Chunk.SIZE - 1;
		int gz = coord[2] >= 0 ? coord[2] : coord[2] - 1;
		return getChunk(gx, gy, gz);
	}
	
	public static Tile getTile(int x, int y, int z) {
		if(z == currentZLevel) {
			try {
				int x0 = mapInTiles[0][0].pos.coord[0];
				int y0 = mapInTiles[0][0].pos.coord[1];
				return mapInTiles[x - x0][y - y0];
			}catch(ArrayIndexOutOfBoundsException | NullPointerException e) {}
		}
		
		int gx;
		int gy;
		int lx;
		int ly;
		if(x >= 0) {
			gx = x/chunkSize;
			lx = x%chunkSize;
		}else {
			gx = x/chunkSize - 1;
			lx = chunkSize - Math.abs(x%chunkSize);
			if(lx % chunkSize == 0) {
				gx++;
				lx = 0;
			}
		}
		if(y >= 0) {
			gy = y/chunkSize;
			ly = y%chunkSize;
		}else {
			gy = y/chunkSize - 1;
			ly = chunkSize - Math.abs(y%chunkSize);
			if(ly % chunkSize == 0) {
				gy++;
				ly = 0;
			}
		}
		Chunk chunk = getChunk(gx, gy, z);
		Tile tile = chunk.getMap()[lx][ly];
		if(tile == null) {
			tile = TilePool.get(x, y, z);
			chunk.getMap()[lx][ly] = tile;
		}
		return tile;
	}
	
	public static PositionC getPosition(PositionC oldPos, Direction dir) {
		int[] coord = oldPos.coord;
		return getTile(coord[0] + dir.movX, coord[1] + dir.movY, coord[2]).pos;
	}
	
	public static Tile getTile(Tile oldTile, Direction dir) {
		int[] coord = oldTile.pos.coord;
		return getTile(coord[0] + dir.movX, coord[1] + dir.movY, coord[2]);
	}
	
	public static Set<Tile> getOrthogonalTiles(Tile tile, Predicate<Tile> cond) {
		Set<Tile> tiles = getOrthogonalTiles(tile);
		tiles.removeIf(cond.negate());
		
		return tiles;
	}
	
	public static Set<Tile> getOrthogonalTiles(Tile tile) {
		int x = tile.pos.coord[0];
		int y = tile.pos.coord[1];
		int z = tile.pos.coord[2];
		
		Set<Tile> tiles = new HashSet<Tile>();
		tiles.add(getTile(x + 1, y, z));
		tiles.add(getTile(x - 1, y, z));
		tiles.add(getTile(x, y + 1, z));
		tiles.add(getTile(x, y - 1, z));
		
		return tiles;
	}

	public static Set<Tile> getDiagonalTiles(Tile tile, Predicate<Tile> cond) {
		Set<Tile> tiles = getDiagonalTiles(tile);
		tiles.removeIf(cond.negate());
		
		return tiles;
	}
	
	public static Set<Tile> getDiagonalTiles(Tile tile) {
		int x = tile.pos.coord[0];
		int y = tile.pos.coord[1];
		int z = tile.pos.coord[2];
		
		Set<Tile> tiles = new HashSet<Tile>();
		tiles.add(getTile(x + 1, y + 1, z));
		tiles.add(getTile(x - 1, y - 1, z));
		tiles.add(getTile(x - 1, y + 1, z));
		tiles.add(getTile(x + 1, y - 1, z));
		
		return tiles;
	}

	public static Set<Tile> getAdjacentTiles(Tile tile, Predicate<Tile> cond) {
		Set<Tile> tiles = getOrthogonalTiles(tile, cond);
		tiles.addAll(getDiagonalTiles(tile, cond));
		return tiles;
	}
	
	public static Set<Tile> getAdjacentTiles(Tile tile) {
		Set<Tile> tiles = getOrthogonalTiles(tile);
		tiles.addAll(getDiagonalTiles(tile));
		return tiles;
	}

	public static Set<Tile> getCircundatingAreaAsSet(int radius, Tile tile, boolean isRound){
		int x = tile.pos.coord[0];
		int y = tile.pos.coord[1];
		int z = tile.pos.coord[2];
		HashSet<Tile> list = new HashSet<Tile>();
		for(int i = -radius; i <= radius; i++){
			for (int j = -radius; j <= radius; j++){
				try {
					PositionC evalPos = new PositionC();
					evalPos.coord = new int[]{x+i, y+j, z};
					if(!isRound || getDistance(tile.pos, evalPos) <= radius){
						Tile t = getTile(x+i, y+j, z);
						list.add(t);
					}
				} catch (ArrayIndexOutOfBoundsException e) {}
			}
		}
		return list;
	}

	public static Tile[][] getCircundatingAreaAsArray(int radius, Tile tile, boolean isRound){
		int x = tile.pos.coord[0];
		int y = tile.pos.coord[1];
		int z = tile.pos.coord[2];
		
		Tile[][] area = new Tile[(radius*2)+1][(radius*2)+1];
		
		for (int i = -radius; i <= radius; i++){
			for (int j = -radius; j <= radius; j++){
				try {
					PositionC evalPos = new PositionC();
					evalPos.coord = new int[]{x+i, y+j, z};
					if(!isRound || getDistance(tile.pos, evalPos) <= radius){
						area[i+radius][j+radius] = getTile(x+i, y+j, z);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}
			}
		}
		return area;
	}
	
	public static Set<Tile> getSquareAreaAsSet(PositionC pos, int width, int height) {
		int x0 = pos.coord[0];
		int y0 = pos.coord[1];
		int z = pos.coord[2];
		
		Set<Tile> area = new HashSet<>();
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				area.add(getTile(x0+x, y0+y, z));
			}
		}
		return area;
	}
	
	public static Tile[][] getSquareAreaAsArray(PositionC pos, int width, int height) {
		int x0 = pos.coord[0];
		int y0 = pos.coord[1];
		int z = pos.coord[2];
		
		Tile[][] area = new Tile[width][height];
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				area[x][y] = getTile(x0+x, y0+y, z);
			}
		}
		return area;
	}
	
	public static Set<Tile> getAreaByFloodFill(Tile center, int radius, Predicate<Tile> cond){
		Set<Tile> area = getAdjacentTiles(center, cond);
		for(int i = 1; i < radius; i++) {
			Set<Tile> newArea = new HashSet<>();
			area.forEach(t -> newArea.addAll(getAdjacentTiles(t, cond)));
			newArea.removeIf(t -> getDistance(center.pos, t.pos) > radius);
			area.addAll(newArea);
		}
		return area;
	}
	
	public static int countOrthogonalAdjacency(Tile tile, Predicate<Tile> cond){
		return getOrthogonalTiles(tile, cond).size();
	}
	
	public static int countDiagonalAdjacency(Tile tile, Predicate<Tile> cond){
		return getDiagonalTiles(tile, cond).size();
	}
	
	public static int countAdjacency(Tile tile, Predicate<Tile> cond){
		return countOrthogonalAdjacency(tile, cond) + countDiagonalAdjacency(tile, cond);
	}
	
	public static boolean isAdjacent(Tile tile, Predicate<Tile> cond){
		return isOrthogonallyAdjacent(tile, cond) || isDiagonallyAdjacent(tile, cond);
	}
	
	public static boolean isOrthogonallyAdjacent(Tile tile, Predicate<Tile> cond){
		int x = tile.pos.coord[0];
		int y = tile.pos.coord[1];
		int z = tile.pos.coord[2];
		
		return cond.test(getTile(x + 1, y, z))
				|| cond.test(getTile(x - 1, y, z))
				|| cond.test(getTile(x, y + 1, z))
				|| cond.test(getTile(x, y - 1, z));
	}

	public static boolean isDiagonallyAdjacent(Tile tile, Predicate<Tile> cond){
		int x = tile.pos.coord[0];
		int y = tile.pos.coord[1];
		int z = tile.pos.coord[2];
		
		return cond.test(getTile(x + 1, y + 1, z))
				|| cond.test(getTile(x - 1, y - 1, z))
				|| cond.test(getTile(x - 1, y + 1, z))
				|| cond.test(getTile(x + 1, y - 1, z));
	}
	
	public static double getDistance(PositionC start, PositionC end){
		double dx = end.coord[0] - start.coord[0];
		double dy = start.coord[1] - end.coord[1];
		
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	public static boolean isInRange(PositionC start, PositionC end, double maxDistance) {
		double dx = end.coord[0] - start.coord[0];
		double dy = start.coord[1] - end.coord[1];
		return (dx*dx + dy*dy) <= maxDistance*maxDistance;
	}
	
	// Bresenham's line algorithm
	// https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
	public static ArrayList<Tile> getStraigthLine(PositionC start, PositionC end){
		return getStraigthLine(start, end, mapInTiles);
	}
	
	public static ArrayList<Tile> getStraigthLine(PositionC start, PositionC end, Tile[][] area){
		return getStraigthLine(start, end, area, t -> true);
	}
	
	public static ArrayList<Tile> getStraigthLine(PositionC start, PositionC end, Tile[][] area, Predicate<Tile> cond){
		ArrayList<Tile> line = new ArrayList<Tile>();
		if(start.equals(end)) {
			line.add(start.getTile());
			return line;
		}
		int dx = end.coord[0] - start.coord[0];
		int dy = start.coord[1] - end.coord[1];

		int sx = dx > 0 ? 1 : -1; // slope X
		int sy = dy < 0 ? 1 : -1; // slope Y
		
		dx = Math.abs(dx);
		dy = Math.abs(dy);
		
		int err = dx - dy;
		
		PositionC posInLine = start.clone();
		
		int[] startCoord = start.coord;
		int startXinArea;
		int startYinArea;
		if(area[0][0] == null) {
			startXinArea = area.length/2;
			startYinArea = area[0].length/2;
		}else {
			startXinArea = startCoord[0] - area[0][0].pos.coord[0];
			startYinArea = startCoord[1] - area[0][0].pos.coord[1];
		}
		double lineLength = getDistance(start, end);
		do {
			int[] coord = posInLine.coord;
			line.add(area[Math.abs(startXinArea - startCoord[0] + posInLine.coord[0])][Math.abs(startYinArea - startCoord[1] + posInLine.coord[1])]);
			 
			int e2 = 2 * err;
			
			if (e2 > -dy) {
				err -= dy;
				coord[0] += sx;
			}
			if (e2 < dx) {
				err += dx;
				coord[1] += sy;
			}
		} while (cond.test(posInLine.getTile()) && lineLength >= getDistance(start, posInLine));
		return line;
	}
	
	public static Tile getClosestTile(Tile origin, Set<Tile> tiles) {
		Tile closest = null;
		double shortestDistance = Double.MAX_VALUE;
		
		for(Tile tile : tiles) {
			double distance = getDistance(origin.pos, tile.pos);
			if(distance < shortestDistance) {
				closest = tile;
				shortestDistance = distance;
			}
		}
		return closest;
		
	}

	public static TreeMap<String, Chunk> getChunksInMemory() {
		return chunksInMemory;
	}
	
}
