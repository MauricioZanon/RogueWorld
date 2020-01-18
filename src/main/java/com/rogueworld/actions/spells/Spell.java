package com.rogueworld.actions.spells;

import java.util.function.Predicate;

import com.rogueworld.entities.components.SkillsC.Skill;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.utils.rng.RNG;
import com.rogueworld.world.tile.Tile;

public abstract class Spell {
	
	protected Skill usedSkill;
	
	protected Predicate<Entity> condition = e -> true;
	
	protected String name;
	protected String description;
	
	protected int range;
	protected int area;
	
	protected boolean isProjectile;
	
	public abstract void cast(Entity caster, Tile target);
	
	protected static RNG rng = RNG.getInstance();
	
	public boolean canBeCasted(Entity caster) {
		return condition.test(caster);
	}

	public Skill getUsedSkill() {
		return usedSkill;
	}

	public Predicate<Entity> getCondition() {
		return condition;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	public int getRange() {
		return range;
	}

	public int getArea() {
		return area;
	}

	public boolean isProjectile() {
		return isProjectile;
	}

	@Override
	public String toString() {
		return name;
	}
	
	//TODO quitar el switch y hacer esto con Reflections
	public static Spell get(String spellName) {
		switch(spellName) {
		case "teleport self": return TeleportSelf.getInstance();	
		case "dig": return Dig.getInstance();
		case "Summon lesser creature": return SummonLesserCreature.getInstance();
		default: return null; 
		}
	}
}
