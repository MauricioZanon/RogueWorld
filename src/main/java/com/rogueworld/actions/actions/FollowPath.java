package com.rogueworld.actions.actions;

import com.rogueworld.ai.pathfind.AStar;
import com.rogueworld.ai.pathfind.Path;
import com.rogueworld.application.Main;
import com.rogueworld.entities.components.MovementC;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.world.tile.Tile;
import com.rogueworld.world.world.Direction;

//TODO test
public abstract class FollowPath {
	
	public static void execute(Entity actor) {
		Path path = actor.get(MovementC.class).path;
		if(path != null && !path.isEnded()) {
			PositionC nextPos = path.getNext();
			path.advance();
			PositionC actualPos = actor.get(PositionC.class);
			Bump.execute(actualPos, Direction.get(actualPos, nextPos), true);
		}
		else {
			EndTurn.execute(actor, ActionType.WAIT);
		}
	}
	
	/** Primero crea el Path desde el actor hasta el tile destino y luego lo hace avanzar */
	public static void execute(Entity actor, Tile destiny) {
		PositionC pos = Main.player.get(PositionC.class);
		Path path = AStar.findPath(pos, destiny.pos, Main.player);
		if(path.getLength() > 0) {
			Main.player.get(MovementC.class).path = path;
			execute(Main.player);
		}
	}
	

}
