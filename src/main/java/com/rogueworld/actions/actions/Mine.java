package com.rogueworld.actions.actions;

import com.rogueworld.gui.gamescreen.Console;
import com.rogueworld.application.Main;
import com.rogueworld.entities.components.ContainerC;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.main.Entity;

public abstract class Mine {
	
	public static void execute(Entity mined) {
		//TODO if has pickaxe...
		Main.player.get(ContainerC.class).addAll(mined.get(ContainerC.class).remove(i -> i.name.contains("ore"), 1));
		Console.addMessage("You extract the ore from this vein\n");
		
		if(mined.get(ContainerC.class).isEmpty()) {
			Console.addMessage("This " + mined.name + " has no more ore.\n");
			mined.get(PositionC.class).getTile().remove(mined);
		}
		
		EndTurn.execute(Main.player, ActionType.USE_ITEM);
	}

}
