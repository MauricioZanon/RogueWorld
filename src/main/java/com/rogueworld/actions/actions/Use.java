package com.rogueworld.actions.actions;

import java.util.ArrayDeque;

import com.rogueworld.gui.gamescreen.Console;
import com.rogueworld.gui.itemmenus.MenuFactory;
import com.rogueworld.application.Main;
import com.rogueworld.entities.components.UsesC;
import com.rogueworld.entities.components.UsesC.UseType;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.world.tile.Tile;

public abstract class Use {

	public static Entity usedEntity;

	public static void execute(Entity used, Entity user, UseType use) {
		usedEntity = used;
		switch (use) {
		case ACTIVATE: {
		}
			break;
		case CHOP: {
		}
			break;
		case CLOSE:
			Close.execute(user, usedEntity);
			break;
		case CUT_BRANCH:
			CutBranch.execute(usedEntity);
			break;
		case FISH: {
		}
			break;
		case GET_BARK: {
		}
			break;
		case HARVEST:
			Harvest.execute(user, usedEntity);
			break;
		case LOCK: {
		}
			break;
		case MINE:
			Mine.execute(usedEntity);
			break;
		case OPEN:
			Open.execute(user, usedEntity);
			break;
		case PEEK: {
		}
			break;
		case REFILL_CONTAINER: {
		}
			break;
		case SEE_CONTENT:
			MenuFactory.openGetItemsMenu();
			break;
		case TRUNK: {
		}
			break;
		case UNLOCK: {
		}
			break;
		}
		;

	}

	public static void execute(Tile tile) {
		ArrayDeque<Entity> usables = tile.getEntities(e -> e.has(UsesC.class));
		if (usables.isEmpty()) {
			Console.addMessage("There is nothing you can use here.\n");
		} else if (usables.size() == 1) {
			execute(usables.getFirst(), Main.player, usables.getFirst().get(UsesC.class).quickUse);
		} else {
			Examine.execute(tile);
		}
	}

}