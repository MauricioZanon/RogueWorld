package com.rogueworld.actions.actions;

import java.util.ArrayDeque;

import com.rogueworld.gui.gamescreen.Console;
import com.rogueworld.application.Main;
import com.rogueworld.entities.components.ContainerC;
import com.rogueworld.entities.main.Entity;

public abstract class CutBranch {
	
	public static void execute(Entity usedEntity) {
		ArrayDeque<Entity> items = usedEntity.get(ContainerC.class).removeAll(e -> e.name.contains("branch"));
		if(items.isEmpty()) {
			Console.addMessage("The " + usedEntity.name + " has no more usable branches\n");
		}
		else {
			Main.player.get(ContainerC.class).addAll(items);
			Console.addMessage("You cut some branches from the " + usedEntity.name + "\n");
		}

		EndTurn.execute(Main.player, ActionType.USE_ITEM);
	}

}
