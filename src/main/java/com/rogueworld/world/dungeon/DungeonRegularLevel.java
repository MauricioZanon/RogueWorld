package com.rogueworld.world.dungeon;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.factories.EntityFactory;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;
import com.rogueworld.world.dungeon.DungeonBuilder.DungeonSize;
import com.rogueworld.world.main.Blueprint;
import com.rogueworld.world.main.Room;
import com.rogueworld.world.main.RoomFactory;
import com.rogueworld.world.map.Map;
import com.rogueworld.world.tile.Tile;
import com.rogueworld.world.world.Direction;

public class DungeonRegularLevel extends DungeonLevel{
	
	public DungeonRegularLevel(PositionC exitStairPos, DungeonSize size) {
		int requestedRooms = size.roomQuantity;
		
		while(rooms.isEmpty()) {
			createFirstRoom(exitStairPos);
		}
		
		while(rooms.size() < requestedRooms) {
			if(availableAnchors.isEmpty()) {
				validLevel = false;
				return;
			}
			availableAnchors.removeIf(a -> a == null);
			PositionC anchorPos = rng.getRandom(availableAnchors).pos;
			createRoom(anchorPos);
		}
		putDoors();
		putStairs();
		putEnemies();
		putItems();
	}

	/**
	 * Crea la primer habitación del nivel
	 * @param exitStairPos: El lugar en el que debe ir la escalera al nivel superior
	 */
	private void createFirstRoom(PositionC exitStairPos) {
		
		Blueprint bp = RoomFactory.createRoom("Dungeon starting rooms");
		int[] startingPosCorrection = bp.getStairsAnchor();
		PositionC startingPos = exitStairPos.clone();
		startingPos.coord[0] -= startingPosCorrection[0];
		startingPos.coord[1] -= startingPosCorrection[1];
		
		buildRoom(startingPos, null, bp);
	}
	
	/**
	 * Crea una habitación genérica en el nivel
	 * @param anchorPos: Es la posición en el nivel a la que estará unida la nueva habitación
	 */
	private void createRoom(PositionC anchorPos) {
		Tile emptyTile = rng.getRandom(Map.getOrthogonalTiles(anchorPos.getTile(), t -> t.get(Type.TERRAIN) == null));
		if(emptyTile == null) return;
		Direction bpDirection = Direction.get(anchorPos, emptyTile.pos);
		Blueprint bp = RoomFactory.createRoom("Dungeon rooms", bpDirection);
		List<Integer[]> posibleAnchors = bp.getAnchors(bpDirection);
		Integer[] bpAnchor = rng.getRandom(posibleAnchors);
		
		PositionC startingPos = anchorPos.clone();
		startingPos.coord[0] -= bpAnchor[0];
		startingPos.coord[1] -= bpAnchor[1];
		
		buildRoom(startingPos, anchorPos.getTile(), bp);
	}


	private void buildRoom(PositionC startingPos, Tile entranceTile, Blueprint bp) {
		Set<Tile> roomTiles = new HashSet<>();
		Set<Tile> doorTiles = new HashSet<>();
		Tile upStairTile = null;
		Tile downStairTile = null;
		Set<Tile> newAnchorTiles = new HashSet<>();
		
		char[][] bpArray = bp.getArray();
		for(int i = 0; i < bpArray.length; i++) {
			for(int j = 0; j < bpArray[0].length; j++) {
				Tile tile = Map.getTile(startingPos.coord[0] + i, startingPos.coord[1] + j, startingPos.coord[2]);
				char symbol = bpArray[i][j];
				switch(symbol) {
				case '.':
					if(!isValidTile(tile)) return;
					roomTiles.add(tile);
					break;
				case 'u':
					newAnchorTiles.add(tile);
					break;
				case '+':
					roomTiles.add(tile);
					doorTiles.add(tile);
					break;
				case '>':
					if(!isValidTile(tile)) return;
					roomTiles.add(tile);
					downStairTile = tile;
					break;
				case '<':
					if(!isValidTile(tile)) return;
					roomTiles.add(tile);
					upStairTile = tile;
					break;
				}
			}
		}
		
		if(entranceTile != null) {
			roomTiles.add(entranceTile);
			doors.add(entranceTile);
		}
		buildRoom(roomTiles);
		rooms.add(new Room(roomTiles));
		doors.addAll(doorTiles);
		upStair = upStairTile == null ? upStair: upStairTile.pos;
		downStair = downStairTile == null ? downStair : downStairTile.pos;
		availableAnchors.addAll(newAnchorTiles);
		availableAnchors.remove(entranceTile);
	}

	private void buildRoom(Set<Tile> roomTiles) {
		roomTiles.forEach(t -> t.put(FLOOR));
		
		for(Tile floorTile : roomTiles) {
			Map.getAdjacentTiles(floorTile, t -> t.get(Type.TERRAIN) == null).forEach(t -> t.put(WALL));
		}
	}
	
	private void putEnemies() {
		int quantity = rng.nextGaussian(rooms.size()/2, rooms.size()/3);
		Set<Tile> availableTiles = new HashSet<>();
		rooms.forEach(r -> availableTiles.addAll(r.getFloorTiles()));
		while(quantity > 0) {
			Entity npc = EntityFactory.createRandom(Type.NPC);
			Tile tile = rng.getRandom(availableTiles, t -> t.get(Type.FEATURE) == null);
			npc.addComponent(tile.pos.clone());
			tile.put(npc);
			quantity--;
		}
	}
	
	private void putItems() {
		int quantity = rng.nextGaussian(rooms.size(), rooms.size()/3);
		Set<Tile> availableTiles = new HashSet<>();
		rooms.forEach(r -> availableTiles.addAll(r.getFloorTiles()));
		while(quantity > 0) {
			//FIXME decidir la rareza de cada item del dungeon aca
			Entity item = EntityFactory.createRandom(Type.ITEM);
			Tile tile = rng.getRandom(availableTiles, t -> t.get(Type.FEATURE) == null);
			tile.put(item);
			quantity--;
		}
	}
}
