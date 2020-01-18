package com.rogueworld.ai.states;

import com.rogueworld.ai.eventsystem.EventSystem;
import com.rogueworld.actions.actions.FollowPath;
import com.rogueworld.entities.components.MovementC;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.player.PlayerInfo;

public class PlayerState extends State{
	
	public PlayerState(Entity actor) {
		this.actor = actor;
	}
	
	public void update() {
		super.update();
		EventSystem.setPlayerTurn(true);
		if(actor.get(MovementC.class).path != null && !actor.get(MovementC.class).path.isEnded()) {
			FollowPath.execute(actor);
		}
		
		PlayerInfo.HUNGER.set(PlayerInfo.HUNGER.floatValue()+1);
		PlayerInfo.THIRST.set(PlayerInfo.THIRST.floatValue()+1);
		
	}

	@Override
	public void enterState() {}

	@Override
	public void exitState() {}
	
}
