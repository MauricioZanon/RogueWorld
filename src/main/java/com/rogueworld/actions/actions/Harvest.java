package com.rogueworld.actions.actions;

import java.util.ArrayDeque;

import com.rogueworld.entities.components.ContainerC;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Flag;
import com.rogueworld.world.tile.Tile;

public abstract class Harvest {
	
	public static void execute(Entity harvester, Entity harvestable) {
		Tile harvesterTile = harvester.get(PositionC.class).getTile();
		ArrayDeque<Entity> items = harvestable.get(ContainerC.class).removeAll(e -> e.is(Flag.EDIBLE));
		harvesterTile.put(items);
		
		EndTurn.execute(harvester, ActionType.USE_ITEM);
	}

}
