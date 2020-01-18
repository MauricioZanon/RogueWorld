package com.rogueworld.actions.actions;

import com.rogueworld.application.Main;
import com.rogueworld.entities.components.ContainerC;
import com.rogueworld.entities.crafts.CraftRecipe;
import com.rogueworld.entities.factories.EntityFactory;

public abstract class Craft {
	
	public static void execute(CraftRecipe recipe) {
		Main.player.get(ContainerC.class).add(EntityFactory.create(recipe.name));
		EndTurn.execute(Main.player, ActionType.CRAFT);
	}

}
