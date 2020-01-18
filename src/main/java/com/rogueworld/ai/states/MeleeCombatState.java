package com.rogueworld.ai.states;

import com.rogueworld.ai.pathfind.AStar;
import com.rogueworld.actions.actions.FollowPath;
import com.rogueworld.entities.components.AIC;
import com.rogueworld.entities.components.MovementC;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.components.VisionC;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;
import com.rogueworld.world.map.Map;
import com.rogueworld.world.tile.Tile;

public class MeleeCombatState extends State{
	
	private Entity target = null;
	
	public MeleeCombatState() {}
	
	/** Con este estado la entidad se mueve hasta estar adyacente a la entidad enemiga y entonces la ataca */
	public MeleeCombatState(Entity actor) {
		this.actor = actor;
	}

	public void update() {
		super.update();
		if(!target.get(AIC.class).isActive) {
			actor.get(AIC.class).setState(StateType.IDLE);
		}
		else {
			refreshPath();
			if(actor.get(MovementC.class).path.isEnded()) {
				actor.get(AIC.class).setState(StateType.IDLE);
			}else {
				FollowPath.execute(actor);
			}
		}
	}
	
	@Override
	public void enterState() {
		Tile actorTile = actor.get(PositionC.class).getTile();
		Tile targetTile = Map.getClosestTile(actorTile, actor.get(VisionC.class).enemyTiles);
		target = targetTile.get(Type.ACTOR);
	}
	
	@Override
	public void exitState() {
		target = null;
		actor.get(MovementC.class).path = null;
	}
	
	private void refreshPath() {
		PositionC targetPos = target.get(PositionC.class);
		if(actor.get(VisionC.class).visionMap.contains(targetPos.getTile())) {
			actor.get(MovementC.class).path = AStar.findPath(actor.get(PositionC.class), targetPos, actor);
		}
	}

}
