package com.rogueworld.actions.actions;

import com.rogueworld.gui.gamescreen.Console;
import com.rogueworld.application.Main;
import com.rogueworld.entities.crafts.BuildRecipe;
import com.rogueworld.entities.factories.EntityFactory;
import com.rogueworld.world.tile.Tile;

public abstract class Build {
	
	public static BuildRecipe recipe = null;

	public static void execute(Tile tile) {
		if(recipe.canBeBuiltIn(tile)) {
			tile.put(EntityFactory.create(recipe.name));
			recipe.consumeMaterials();
			EndTurn.execute(Main.player, ActionType.CRAFT);
		}else {
			Console.addMessage("You can't build there");
			EndTurn.execute(Main.player, 0);
		}
		
	}
	
}
