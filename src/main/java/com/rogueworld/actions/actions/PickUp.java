package com.rogueworld.actions.actions;

import com.rogueworld.gui.gamescreen.Console;
import com.rogueworld.entities.components.ContainerC;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;
import com.rogueworld.world.map.Map;
import com.rogueworld.world.tile.Tile;
import com.rogueworld.world.world.Direction;

import javafx.scene.paint.Color;

public abstract class PickUp {
	
	public static void execute(Entity actor, Direction dir) {
		PositionC actorPos = actor.get(PositionC.class);
		Tile itemTile = Map.getPosition(actorPos, dir).getTile();
		
		pickUp(actor, itemTile);
	}
	
	public static void execute(Entity actor) {
		pickUp(actor, actor.get(PositionC.class).getTile());
	}
	
	private static void pickUp(Entity actor, Tile tile) {
		Entity item = tile.remove(Type.ITEM);
		if(item != null) {
			actor.get(ContainerC.class).add(item);
			Console.addMessage("You pick up a -" + item.name + " -.\n", Color.WHITE, Color.CADETBLUE, Color.WHITE);
		}else {
			Console.addMessage("There is nothing to pick up here.\n");
		}
	}
}
