package com.rogueworld.actions.actions;

import java.util.ArrayList;

import com.rogueworld.actions.effects.Effects;
import com.rogueworld.gui.gamescreen.Console;
import com.rogueworld.entities.components.ContainerC;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.components.SkillsC;
import com.rogueworld.entities.components.SkillsC.Skill;
import com.rogueworld.entities.main.Att;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;
import com.rogueworld.world.map.Map;
import com.rogueworld.world.tile.Tile;

public abstract class Throw {
	
	public static Entity thrownItem = null;
	
	private Throw() {}
	
	public static void execute(Entity actor, Tile target) {
		if(thrownItem == null) return;
		actor.get(ContainerC.class).remove(thrownItem.name, 1);
		
		ArrayList<Tile> trajectory = Map.getStraigthLine(actor.get(PositionC.class), target.pos);
		for(int i = 1; i < trajectory.size(); i++) {
			Tile t = trajectory.get(i);
			if(t.get(Type.ACTOR) != null) {
				t.put(thrownItem);
				float damage = calculateDamage(actor, thrownItem);
				Effects.receiveDamage(t.get(Type.ACTOR), damage);
				Console.addMessage("The " + thrownItem.name + " hits the " + t.get(Type.ACTOR).name);
				EndTurn.execute(actor, ActionType.WALK);
				return;
			}
		}
		trajectory.get(trajectory.size()-1).put(thrownItem);
		actor.get(SkillsC.class).change(Skill.THROWING, 0.1f);
		EndTurn.execute(actor, ActionType.WALK);
	}
	
	private static float calculateDamage(Entity actor, Entity thrownItem) {
		float skillMod = actor.get(SkillsC.class).get(Skill.THROWING) / 5;
		return thrownItem.get(Att.DAMAGE) * skillMod;
	}
	
}
