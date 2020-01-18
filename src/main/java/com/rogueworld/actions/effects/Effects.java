package com.rogueworld.actions.effects;

import java.util.Set;

import com.rogueworld.actions.actions.Die;
import com.rogueworld.actions.statuseffects.StTrigger;
import com.rogueworld.gui.gamescreen.Console;
import com.rogueworld.gui.gamescreen.DrawUtils;
import com.rogueworld.entities.components.BreakC;
import com.rogueworld.entities.components.HealthC;
import com.rogueworld.entities.components.MovementC;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.components.StatusEffectsC;
import com.rogueworld.entities.factories.ItemFactory;
import com.rogueworld.entities.main.Att;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;
import com.rogueworld.entities.player.PlayerInfo;
import com.rogueworld.world.map.Map;
import com.rogueworld.world.tile.Tile;
import com.rogueworld.world.world.Direction;

import javafx.scene.paint.Color;

public abstract class Effects {
	
	public static void receiveDamage(Entity actor, float damage) {
		//TODO agregar modificador de hardness por el material de la armadura
		HealthC hp = actor.get(HealthC.class);
		float totalDamage = damage - actor.get(Att.DEFENSE);
		if(totalDamage > 0) {
			hp.changeCurHP(-damage);
			actor.get(StatusEffectsC.class).triggerStatus(StTrigger.RECEIVE_DAMAGE);
		}
		if(actor.type == Type.PLAYER) {
			PlayerInfo.CUR_HP.set(hp.getCurHP());
		}
		else if(hp.getCurHP() <= 0) {
			Die.execute(actor);
		}
	}
	
	public static void heal(Entity entity, float value) {
		HealthC hp = entity .get(HealthC.class);
		hp.changeCurHP(value);
		if(entity.type == Type.PLAYER) {
			PlayerInfo.CUR_HP.set(hp.getCurHP());
		}
	}
	
	public static void move(Entity actor, Tile newTile) { //TODO test
		Tile oldTile = actor.get(PositionC.class).getTile();
		oldTile.remove(Type.ACTOR);
		newTile.put(actor);
		
		if(actor.type == Type.PLAYER) {
			if(newTile.has(Type.ITEM)) {
				Entity item = newTile.get(Type.ITEM);
				Console.addMessage("There is a- " + item.name + "- on the ground.\n", Color.WHITE, Color.CADETBLUE, Color.WHITE);
			}
			Map.refresh();
			DrawUtils.fullRedraw = true;
		}
	}
	
	public static void push(Entity entity, int distance, Direction dir) {
		for(int i = 0; i < distance; i++) {
			PositionC entityPos = entity.get(PositionC.class);
			Tile nextTile = Map.getPosition(entityPos, dir).getTile();
			if(nextTile.isTransitable(entity.get(MovementC.class).movementType)) {
				move(entity, nextTile);
			}else {
				break;
			}
		}
	}
	
	public static void shatter(Entity entity, Tile tile) {
		tile.remove(entity);
		if(entity.has(BreakC.class)) {
			Set<Entity> items = ItemFactory.getItems(entity.get(BreakC.class).items);
			items.forEach(i -> tile.put(i));
		}
	}

}
