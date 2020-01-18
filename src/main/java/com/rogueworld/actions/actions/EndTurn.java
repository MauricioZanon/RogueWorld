package com.rogueworld.actions.actions;

import com.rogueworld.ai.eventsystem.EventSystem;
import com.rogueworld.entities.components.AIC;
import com.rogueworld.entities.components.MovementC;
import com.rogueworld.entities.components.MovementC.MovementType;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.components.StatusEffectsC;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;
import com.rogueworld.world.time.Clock;

//TODO TEST
public abstract class EndTurn {
	
	/** 
	 * @param time: el tiempo para el siguiente turno en milisegundos
	 */
	public static void execute(Entity actor, float time) {
		endTurn(actor, time);
	}
	
	public static void execute(Entity actor, ActionType type) {
		float elapsedTime = calculateTime(actor, type);
		endTurn(actor, elapsedTime);
		
	}
	
	public static float calculateTime(Entity actor, ActionType type) {
		float actorSpeed = actor.get(type.asociatedStat);
		if(actorSpeed <= 0) actorSpeed = 1;
		float elapsedTime = type.timeNeeded*10 / actorSpeed;
		
		if(type == ActionType.WALK) {
			MovementType movType = actor.get(MovementC.class).movementType;
			elapsedTime *= actor.get(PositionC.class).getTile().getMovementCost(movType);
		}
		
		return elapsedTime;
	}
	
	private static void endTurn(Entity actor, float time) {
		actor.get(AIC.class).nextTurn += time;
		if(actor.type == Type.PLAYER) {
			EventSystem.setPlayerTurn(false);
			Clock.advanceTime(time);
//			Clock.advanceTime(100);
		}
		
		actor.get(StatusEffectsC.class).diminishDurations();
	}
	
}
