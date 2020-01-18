package com.rogueworld.world.dungeon;

import java.util.HashSet;
import java.util.Set;

import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.factories.EntityFactory;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;
import com.rogueworld.utils.rng.RNG;
import com.rogueworld.world.main.Room;
import com.rogueworld.world.tile.Tile;

public abstract class DungeonLevel {
	
	protected Set<Room> rooms = new HashSet<>();
	protected Set<Tile> doors = new HashSet<>();
	protected PositionC upStair = null;
	protected PositionC downStair = null;
	protected Set<Tile> availableAnchors = new HashSet<>();
	
	protected final Entity FLOOR = EntityFactory.create("concrete floor");
	protected final Entity WALL = EntityFactory.create("concrete wall");
	
	protected boolean validLevel = true;
	
	protected static RNG rng = RNG.getInstance();
	
	protected void putDoors() {
		for(Tile tile : doors) {
			tile.put(EntityFactory.create("closed door"));
		}
	}
	
	protected void putStairs() {
		if(upStair != null) {
			Entity stair = EntityFactory.create("up stair");
			stair.addComponent(upStair);
			upStair.getTile().put(stair);
		}
		Room room = rng.getRandom(rooms, r -> r.getDoorTiles().size() == 1);
		downStair = rng.getRandom(room.getFloorTiles()).pos;
		Entity stair = EntityFactory.create("down stair");
		stair.addComponent(downStair);
		downStair.getTile().put(stair);
	}
	
	protected boolean isValidTile(Tile tile) {
		try{
			return tile.get(Type.TERRAIN) == null;
		}catch(NullPointerException e) {
			return false;
		}
	}
	
	public Set<Room> getRooms() {
		return rooms;
	}

	public Set<Tile> getDoors() {
		return doors;
	}

	public PositionC getUpStair() {
		return upStair;
	}

	public PositionC getDownStair() {
		return downStair;
	}

	public boolean isValidLevel() {
		return validLevel;
	}

}
