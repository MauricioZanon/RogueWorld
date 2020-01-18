package com.rogueworld.world.dungeon;

import com.rogueworld.world.main.MultiLevelLocation;
import com.rogueworld.world.world.WorldBuilder;

public class Dungeon extends MultiLevelLocation{
	
	private DungeonLevel[] levels;
	
	public Dungeon(DungeonLevel[] levels){
		this.levels = levels;
		WorldBuilder.getLocations().add(this);
	}

	public DungeonLevel[] getLevels() {
		return levels;
	}
	
}
