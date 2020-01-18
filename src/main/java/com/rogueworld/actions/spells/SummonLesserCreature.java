package com.rogueworld.actions.spells;

import com.rogueworld.entities.components.SkillsC.Skill;
import com.rogueworld.entities.factories.EntityFactory;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;
import com.rogueworld.world.tile.Tile;

public class SummonLesserCreature extends Spell{
	
	public static SummonLesserCreature instance = null;
	
	private SummonLesserCreature() {
		name = "Summon lesser creature";
		description = "Summons a weak creature to aid you.";
		usedSkill = Skill.SUMMONING;
		range = 3;
		area = 1;
		isProjectile = false;
	}

	@Override
	public void cast(Entity caster, Tile target) {
		Entity summon = EntityFactory.createRandom(Type.NPC);
		target.put(summon);
	}
	
	public static SummonLesserCreature getInstance() {
		if(instance == null) {
			instance = new SummonLesserCreature();
		}
		return instance;
	}
	
}
