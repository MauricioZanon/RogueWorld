package com.rogueworld.ai.states;

import com.rogueworld.actions.actions.ActionType;
import com.rogueworld.actions.actions.Bump;
import com.rogueworld.actions.actions.EndTurn;
import com.rogueworld.entities.components.AIC;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.world.world.Direction;

public class IdleState extends State{ //TODO test
	
	public IdleState() {}
	
	/** Con este estado la entidad se mantiene en el lugar o se mueve a un tile adyacente */
	public IdleState(Entity actor) {
		this.actor = actor;
	}
	
	public void update() {
		super.update();
		
		if(enemySighted()) {
			actor.get(AIC.class).setState(StateType.COMBAT);
		}else {
			if(rng.nextInt(100) > 60)
				Bump.execute(actor.get(PositionC.class), rng.getRandom(Direction.values()), true);
			else
				EndTurn.execute(actor, ActionType.WAIT);
		}
	}

	@Override
	public void enterState() {}

	@Override
	public void exitState() {}

	
}
