package com.rogueworld.actions.actions;

import com.rogueworld.actions.statuseffects.StTrigger;
import com.rogueworld.gui.gamescreen.Console;
import com.rogueworld.application.Main;
import com.rogueworld.entities.components.AIC;
import com.rogueworld.entities.components.GraphicC;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.components.StatusEffectsC;
import com.rogueworld.entities.components.VisionC;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;
import com.rogueworld.world.tile.Tile;

import javafx.scene.paint.Color;

public abstract class Die {
	
	public static void execute(Entity actor) {
		Tile tile = actor.get(PositionC.class).getTile();
		tile.remove(Type.ACTOR);
		
		if(Main.player.get(VisionC.class).visionMap.contains(tile)) {
			Console.addMessage("The -" + actor.name + "- dies.\n", Color.WHITE, Color.RED, Color.WHITE);
		}
		
		actor.get(AIC.class).isActive = false;
		actor.type = Type.CORPSE;
		actor.name += " corpse";
		actor.get(GraphicC.class).ASCII = "%";
		
		tile.put(actor);
		
		actor.get(StatusEffectsC.class).triggerStatus(StTrigger.DIE);
	}
	
}
