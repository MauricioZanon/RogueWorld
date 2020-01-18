package com.rogueworld.actions.actions;

import com.rogueworld.actions.effects.Effects;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.world.tile.Tile;

public abstract class Walk {
	
	public static void execute(Entity actor, Tile nextTile){
		Effects.move(actor, nextTile);
		EndTurn.execute(actor, ActionType.WALK);
	}

}
