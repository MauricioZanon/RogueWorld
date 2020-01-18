package com.rogueworld.actions.actions;

import java.util.Set;

import com.rogueworld.actions.effects.Effects;
import com.rogueworld.entities.components.ContainerC;
import com.rogueworld.entities.components.MovementC;
import com.rogueworld.entities.components.MovementC.MovementType;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.utils.rng.RNG;
import com.rogueworld.world.map.Map;
import com.rogueworld.world.tile.Tile;

public abstract class Quaff {
	
	private static RNG rng = RNG.getInstance();

	public static void execute(Entity actor, Entity item) {
		switch(item.name) {
		case "healing potion":
			Effects.heal(actor, 20);
			break;
		case "teleportation potion":
			Set<Tile> area = Map.getCircundatingAreaAsSet(15, actor.get(PositionC.class).getTile(), true);
			MovementType movType = actor.get(MovementC.class).movementType;
			Tile selectedTile = rng.getRandom(area, t-> t.isTransitable(movType));
			Effects.move(actor, selectedTile);
			break;
		default:
			break;
		}
		actor.get(ContainerC.class).remove(item.name, 1);
		EndTurn.execute(actor, ActionType.WALK);
	}
	
}
