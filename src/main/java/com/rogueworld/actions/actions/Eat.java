package com.rogueworld.actions.actions;

import com.rogueworld.application.Main;
import com.rogueworld.entities.components.ContainerC;
import com.rogueworld.entities.main.Att;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.player.PlayerInfo;

public abstract class Eat {

	public static void execute(Entity food) {
		PlayerInfo.HUNGER.set(PlayerInfo.HUNGER.floatValue() - food.get(Att.NUTRITION));
		Main.player.get(ContainerC.class).remove(food.name, 1);
		EndTurn.execute(Main.player, ActionType.USE_ITEM);
	}
	

}
