package com.rogueworld.ai.states;

import com.rogueworld.actions.statuseffects.StTrigger;
import com.rogueworld.entities.components.HealthC;
import com.rogueworld.entities.components.StatusEffectsC;
import com.rogueworld.entities.components.VisionC;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.utils.rng.RNG;
import com.rogueworld.utils.shadowcasting.ShadowCasting;

public abstract class State {
	
	protected Entity actor;
	protected static RNG rng = RNG.getInstance();
	
	public void update() {
		ShadowCasting.calculateFOV(actor);
		actor.get(StatusEffectsC.class).triggerStatus(StTrigger.START_TURN);
		actor.get(HealthC.class).regenerate();
	}
	
	public abstract void enterState();
	public abstract void exitState();
	
	protected boolean enemySighted() {
		return !actor.get(VisionC.class).enemyTiles.isEmpty();
	}
	
	public void setOwner(Entity actor) {
		this.actor = actor;
	}

}
