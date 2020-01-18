package com.rogueworld.ai.states;

import com.rogueworld.actions.actions.ActionType;
import com.rogueworld.actions.actions.EndTurn;
import com.rogueworld.actions.actions.Shoot;
import com.rogueworld.entities.components.AIC;
import com.rogueworld.entities.components.BodyC;
import com.rogueworld.entities.components.MovementC;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.components.VisionC;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;
import com.rogueworld.world.map.Map;
import com.rogueworld.world.tile.Tile;

public class RangedCombatState extends State{
	
	private Entity target = null;

	public RangedCombatState() {}
	
	public RangedCombatState(Entity actor) {
		this.actor = actor;
	}

	public void update() {
		super.update();
		if(!target.get(AIC.class).isActive) {
			actor.get(AIC.class).setState(StateType.IDLE);
			EndTurn.execute(actor, ActionType.WAIT);
		}
		Entity weapon = actor.get(BodyC.class).getWeapon();
		Tile targetTile = target.get(PositionC.class).getTile();
		if(actor.get(VisionC.class).visionMap.contains(targetTile)) {
			actor.get(AIC.class).setState(StateType.IDLE);
			EndTurn.execute(actor, ActionType.WAIT);
		}
		if(weapon == null || !weapon.type.is(Type.RANGED)) {
//			fleeState
			EndTurn.execute(actor, ActionType.WAIT);
		}
		else if(weapon.type == Type.BOW) {
			Shoot.execute(actor, targetTile);
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
	
}
