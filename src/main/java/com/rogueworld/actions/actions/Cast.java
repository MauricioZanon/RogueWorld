package com.rogueworld.actions.actions;

import com.rogueworld.actions.spells.Spell;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.world.tile.Tile;

public abstract class Cast {

	public static Spell castedSpell = null;
	
	public static void execute(Entity caster, Spell spell, Tile target) {
		castedSpell = spell;
		execute(caster, target);
	}
	
	public static void execute(Entity caster, Tile target) {
		castedSpell.cast(caster, target);
		EndTurn.execute(caster, ActionType.CAST_SPELL);
	}
	
}
