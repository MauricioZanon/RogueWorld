package com.rogueworld.actions.actions;

import java.util.ArrayDeque;

import com.rogueworld.gui.gamescreen.Console;
import com.rogueworld.gui.system.RenderSystem;
import com.rogueworld.application.Main;
import com.rogueworld.entities.components.UsesC;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.world.tile.Tile;

/** Examina un tile adyacente y, de ser posible, usa la entidad encontrada */
public abstract class Examine {
	
	public static Tile examinedTile = null;
	
	private Examine() {}
	
	public static void execute(Tile tile) {
		ArrayDeque<Entity> entities = tile.getEntities(e -> e.has(UsesC.class));
		if(entities.isEmpty()) {
			Console.addMessage("There is nothing there that you can use.\n");
			EndTurn.execute(Main.player, ActionType.USE_ITEM);
		}
		else if(entities.size() == 1 && entities.getFirst().get(UsesC.class).uses.size() == 1) {
			Use.execute(entities.getFirst(), Main.player, entities.getFirst().get(UsesC.class).uses.get(0));
		}
		else {
			examinedTile = tile;
			RenderSystem.getInstance().changeScene("ExamineMenu.fxml", true);
		}
	}

}

