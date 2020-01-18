package com.rogueworld.actions.actions;

import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.factories.EntityFactory;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.world.tile.Tile;

public abstract class Open {
	
	public static void execute(Entity actor, Entity opened) {
		open(opened);
		EndTurn.execute(actor, ActionType.USE_ITEM);
	}
	
	public static void open(Entity oepenedEntity) {
		Tile t = oepenedEntity.get(PositionC.class).getTile();
		t.remove(oepenedEntity);
		t.put(EntityFactory.create("open door"));
	}

}
