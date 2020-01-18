package com.rogueworld.ai.states;

import java.util.Set;

import com.rogueworld.actions.actions.FollowPath;
import com.rogueworld.ai.pathfind.AStar;
import com.rogueworld.ai.pathfind.Path;
import com.rogueworld.entities.components.AIC;
import com.rogueworld.entities.components.MovementC;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.world.map.Map;
import com.rogueworld.world.tile.Tile;

public class WanderingState extends State{
	
	public WanderingState() {}
	
	public WanderingState(Entity actor) {
		this.actor = actor;
	}
	
	public void update() {
		super.update();
		
		if(enemySighted()) {
			actor.get(AIC.class).setState(StateType.COMBAT);
			return;
		}
		
		Path path = actor.get(MovementC.class).path;
		if(path == null || path.isEnded()) {
			Set<Tile> nearbyArea = Map.getCircundatingAreaAsSet(5, actor.get(PositionC.class).getTile(), false);
			Tile destination = rng.getRandom(nearbyArea, t -> t.isTransitable(actor.get(MovementC.class).movementType));
			Path newPath = AStar.findPath(actor.get(PositionC.class), destination.pos, actor);
			actor.get(MovementC.class).path = newPath;
		}
		
		FollowPath.execute(actor);
		
	}
	
	@Override
	public void enterState() {
	}
	
	@Override
	public void exitState() {
		actor.get(MovementC.class).path = null;
	}

}
