package com.rogueworld.actions.spells;

import java.util.Set;

import com.rogueworld.actions.effects.Effects;
import com.rogueworld.entities.components.ContainerC;
import com.rogueworld.entities.components.MovementC;
import com.rogueworld.entities.components.MovementC.MovementType;
import com.rogueworld.entities.components.SkillsC.Skill;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.world.map.Map;
import com.rogueworld.world.tile.Tile;

public class TeleportSelf extends Spell{
	
	public static TeleportSelf instance = null;
	
	private TeleportSelf() {
		name = "Teleport self";
		description = "Teleports you to a random location. You need some slime goo to make this work";
		usedSkill = Skill.ILLUSION;
		range = 0;
		area = 1;
		isProjectile = false;
		
		condition = e -> e.get(ContainerC.class).contains("slime goo");
	}

	@Override
	public void cast(Entity caster, Tile target) {
		caster.get(ContainerC.class).remove("slime goo", 1);
		Set<Tile> area = Map.getCircundatingAreaAsSet(15, target, true);
		MovementType movType = caster.get(MovementC.class).movementType;
		Tile selectedTile = rng.getRandom(area, t-> t.isTransitable(movType));
		Effects.move(caster, selectedTile);
	}
	
	public static TeleportSelf getInstance() {
		if(instance == null) {
			instance = new TeleportSelf();
		}
		return instance;
	}
	
}
