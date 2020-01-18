package com.rogueworld.actions.actions;

import com.rogueworld.actions.effects.Effects;
import com.rogueworld.gui.gamescreen.Console;
import com.rogueworld.application.Main;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.main.Att;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;
import com.rogueworld.world.tile.Tile;
import com.rogueworld.world.world.Direction;

public abstract class Kick {
	
	public static void execute(Entity actor, Tile kickedTile) {
		if(kickedTile.pos.equals(actor.get(PositionC.class))) {
			return;
		}
		if(kickedTile.get(Type.ACTOR) != null) {
			Entity kicked = kickedTile.get(Type.ACTOR);
			int kickerSTR = (int) actor.get(Att.STR);
			int kickedCON = (int) kicked.get(Att.CON);
			
			if(kickerSTR > kickedCON) {
				Console.addMessage("You pushed the " + kicked.name + ".\n");
				Effects.push(kicked, kickerSTR - kickedCON, Direction.get(actor.get(PositionC.class), kickedTile.pos));
			}
			else {
				Console.addMessage("The " + kicked.name + " resists your push attempt.\n");
			}
		}
		
		if(kickedTile.get(Type.DOOR) != null) {
			Console.addMessage("You kick the door open\n");
			Effects.shatter(kickedTile.get(Type.DOOR), kickedTile);
		}
		EndTurn.execute(Main.player, ActionType.ATTACK);
	}

}
