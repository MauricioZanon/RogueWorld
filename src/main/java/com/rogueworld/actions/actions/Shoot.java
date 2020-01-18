package com.rogueworld.actions.actions;

import java.util.ArrayList;

import com.rogueworld.actions.effects.Effects;
import com.rogueworld.gui.gamescreen.Console;
import com.rogueworld.entities.components.BodyC;
import com.rogueworld.entities.components.ContainerC;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.components.SkillsC;
import com.rogueworld.entities.components.SkillsC.Skill;
import com.rogueworld.entities.main.Att;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;
import com.rogueworld.world.map.Map;
import com.rogueworld.world.tile.Tile;

import javafx.scene.paint.Color;

public abstract class Shoot {
	
	private Shoot() {}
	
	public static void execute(Entity actor, Tile target) {
		ArrayList<Tile> trajectory = Map.getStraigthLine(actor.get(PositionC.class), target.pos);
		Entity ammunition = actor.get(ContainerC.class).remove("wooden arrow", 1).getFirst();
		
		for(int i = 1; i < trajectory.size(); i++) {
			Tile t = trajectory.get(i);
			if(t.get(Type.ACTOR) != null) {
				Entity targetEntity = t.get(Type.ACTOR);
				t.put(ammunition);
				float damage = calculateDamage(actor, ammunition);
				Effects.receiveDamage(targetEntity, damage);
				createMessage(actor, targetEntity);
				
				actor.get(SkillsC.class).change(Skill.ARCHERY, 0.1f);
				EndTurn.execute(actor, ActionType.WALK);
				return;
			}
		}
		trajectory.get(trajectory.size()-1).put(ammunition);
		EndTurn.execute(actor, ActionType.ATTACK);
	}
	
	private static float calculateDamage(Entity actor, Entity ammunition) {
		float skillMod = actor.get(SkillsC.class).get(Skill.ARCHERY) / 5f;
		float bowDmg = actor.get(BodyC.class).getWeapon().get(Att.DAMAGE);
		return (bowDmg + ammunition.get(Att.DAMAGE)) * skillMod;
	}
	
	private static void createMessage(Entity shooter, Entity target) {
		if(shooter.type == Type.PLAYER) {
			Console.addMessage("You shoot at the- " + shooter.name + "-.\n", Color.WHITE, Color.CRIMSON, Color.WHITE);
		}
		else if(target.type == Type.PLAYER) {
			Console.addMessage("The - " + shooter.name + "- shoots you.\n", Color.WHITE, Color.CRIMSON, Color.WHITE);
		}
		else {
			Console.addMessage("The - " + shooter.name + "- shoots the -" + target.name + "-.\n", Color.WHITE, Color.CRIMSON, Color.WHITE, Color.CRIMSON);
		}
	}

}
