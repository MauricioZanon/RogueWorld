package com.rogueworld.actions.actions;

import com.rogueworld.actions.effects.Effects;
import com.rogueworld.gui.gamescreen.Console;
import com.rogueworld.entities.components.BodyC;
import com.rogueworld.entities.components.MaterialC;
import com.rogueworld.entities.components.SkillsC;
import com.rogueworld.entities.components.SkillsC.Skill;
import com.rogueworld.entities.main.Att;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;
import com.rogueworld.utils.rng.RNG;

import javafx.scene.paint.Color;

//TODO TEST
public abstract class Attack {
	
	private static RNG rng = RNG.getInstance();
	
	public static void execute(Entity attacker, Entity receiver) {
		if(attackLanded(attacker, receiver)) {
			if(attacker.type == Type.PLAYER) {
				Console.addMessage("You attack the -" + receiver.name + "-.\n", Color.WHITE, Color.CRIMSON, Color.WHITE);
			}
			else if(receiver.type == Type.PLAYER) {
				Console.addMessage("The -" + attacker.name + "- attacks you.\n", Color.WHITE, Color.CRIMSON, Color.WHITE);
			}

			float damage = calculateDamage(attacker);
			Effects.receiveDamage(receiver, damage);
		}
		else {
			receiver.get(SkillsC.class).change(Skill.DODGE, 0.1f);
			
			if(attacker.type == Type.PLAYER) {
				Console.addMessage("The -" + receiver.name + "- dodges your attack.\n", Color.WHITE, Color.CRIMSON, Color.WHITE);
			}
			else if(receiver.type == Type.PLAYER) {
				Console.addMessage("You dodge the attack.\n");
			}
		}
		
		EndTurn.execute(attacker, ActionType.ATTACK);
	}
	
	private static boolean attackLanded(Entity attacker, Entity receiver) {
		float attackerDexMod = attacker.get(Att.DEX) / 10;
		float attackerSkillMod = attacker.get(SkillsC.class).get(getSkillUsed(attacker)) * attacker.get(SkillsC.class).get(Skill.MELEE);
		float acc = 100 * attackerDexMod * attackerSkillMod;
		
		float receiverDexMod = receiver.get(Att.DEX) / 10;
		float receiverSkillMod = receiver.get(SkillsC.class).get(Skill.DODGE);
		float evasion = 100 * receiverDexMod * receiverSkillMod;
		
		return rng.nextFloat(acc) >= rng.nextFloat(evasion);
	}

	private static float calculateDamage(Entity attacker) {
		Skill skillUsed = getSkillUsed(attacker);
		SkillsC skills = attacker.get(SkillsC.class);
		
		float baseDamage = attacker.get(Att.DAMAGE);
		float strMod = attacker.get(Att.STR) / 10;
		float skillMod = skills.get(skillUsed);
		float materialMod = 1;
		
		Entity weaponUsed = attacker.get(BodyC.class).getWeapon();
		if(weaponUsed != null && weaponUsed.has(MaterialC.class)) {
			materialMod = weaponUsed.get(MaterialC.class).material.hardness;
		}
		
		float totalDamage = baseDamage * strMod * skillMod * materialMod;
		
		skills.change(skillUsed, 0.1f);
		
		return totalDamage;
	}
	
	private static Skill getSkillUsed(Entity attacker) {
		BodyC equipment = attacker.get(BodyC.class);
		if(equipment != null) {
			Entity weapon = equipment.getWeapon();
			if(weapon != null) {
				switch(weapon.type) {
				case AXE:
					return Skill.AXES;
				case SWORD:
					return Skill.SWORDS;
				case MACE:
					return Skill.MACES;
				case DAGGER:
					return Skill.SHORT_BLADES;
				default:
					break;
				}
			}
		}
		return Skill.UNNARMED_COMBAT;
	}
}
