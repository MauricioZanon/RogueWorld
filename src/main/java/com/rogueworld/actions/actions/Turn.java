package com.rogueworld.actions.actions;

import com.rogueworld.gui.gamescreen.DrawUtils;
import com.rogueworld.entities.components.AIC;
import com.rogueworld.entities.components.VisionC;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;
import com.rogueworld.world.world.Direction;

public abstract class Turn {
	
	public static void freeExecute(Entity actor, Direction dir) {
		actor.get(VisionC.class).faceDir = dir;
		float elapsedTime = EndTurn.calculateTime(actor, ActionType.TURN);
		actor.get(AIC.class).nextTurn += elapsedTime;
		if(actor.type == Type.PLAYER) {
			DrawUtils.fullRedraw = true;
		}
	}
	
	public static void execute(Entity actor, Direction dir) {
		actor.get(VisionC.class).faceDir = dir;
		EndTurn.execute(actor, ActionType.WAIT);
		if(actor.type == Type.PLAYER) {
			DrawUtils.fullRedraw = true;
		}
	}

}
