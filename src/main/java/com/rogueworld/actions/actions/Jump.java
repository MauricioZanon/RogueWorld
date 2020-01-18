package com.rogueworld.actions.actions;

import com.rogueworld.actions.effects.Effects;
import com.rogueworld.entities.components.SkillsC;
import com.rogueworld.entities.components.SkillsC.Skill;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.world.tile.Tile;

public abstract class Jump {
	
	public static void execute(Entity actor, Tile target) {
		Effects.move(actor, target);
		actor.get(SkillsC.class).change(Skill.ACROBATICS, 0.1f);
		EndTurn.execute(actor, ActionType.WALK);
	}
	
}
