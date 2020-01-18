package com.rogueworld.gui.gamescreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.rogueworld.actions.actions.Cast;
import com.rogueworld.actions.actions.Throw;
import com.rogueworld.actions.actions.Use;
import com.rogueworld.actions.spells.Spell;
import com.rogueworld.application.Main;
import com.rogueworld.entities.components.BodyC;
import com.rogueworld.entities.components.ContainerC;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.components.SkillsC;
import com.rogueworld.entities.components.SkillsC.Skill;
import com.rogueworld.entities.components.UsesC;
import com.rogueworld.entities.main.Att;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;
import com.rogueworld.world.map.Map;
import com.rogueworld.world.tile.Tile;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.paint.Color;

public class InputConfig {
	
	private InputConfig() {}
	
	//TODO mouse action deberia ser un enum
	/** Cuando cambia el valor de MOUSE_ACTION a algo distinto de "go to" se focusea el selectionLayer */
	
	private static final SimpleObjectProperty<MouseAction> MOUSE_ACTION = new SimpleObjectProperty<>(MouseAction.GO_TO);
	private static final SimpleBooleanProperty IS_PROJECTILE = new SimpleBooleanProperty(false);
	private static final SimpleIntegerProperty MAX_DISTANCE = new SimpleIntegerProperty(Integer.MAX_VALUE);
	private static final SimpleIntegerProperty AFFECTED_RADIUS = new SimpleIntegerProperty(1);
	
	public static void setGoToInput() {
		IS_PROJECTILE.set(false);
		MAX_DISTANCE.set(Integer.MAX_VALUE);
		AFFECTED_RADIUS.set(1);
		MOUSE_ACTION.set(MouseAction.GO_TO);
	}
	
	public static void setThrowInput() {
		Entity thrownItem = Throw.thrownItem;
		float itemWeight = thrownItem.get(Att.WEIGHT);
		
		int maxDistance = (int) ((Main.player.get(Att.STR) / itemWeight) * 10);
		if(maxDistance > 15) maxDistance = 15;
		else if(maxDistance < 1) maxDistance = 1;
		
		IS_PROJECTILE.set(true);
		MAX_DISTANCE.set(maxDistance);
		AFFECTED_RADIUS.set(1);
		MOUSE_ACTION.set(MouseAction.THROW);
	}
	
	public static void setShootInput() {
		Entity weapon = Main.player.get(BodyC.class).getWeapon();
		if(weapon == null) {
			Console.addMessage("You don't have anything to fire with.\n");
		}else if(!weapon.type.is(Type.BOW)) {
			Console.addMessage("You can't fire with your- " + weapon.name + "-.\n", Color.WHITE, Color.CADETBLUE, Color.WHITE);
		}else if(Main.player.get(ContainerC.class).get(Type.ARROW).size() == 0) {
			Console.addMessage("You have no more arrows.\n");
		}
		else {
			IS_PROJECTILE.set(true);
			int maxDistance = (int) (Main.player.get(Att.STR) / 10 + weapon.get(Att.RANGE));
			MAX_DISTANCE.set(maxDistance);
			AFFECTED_RADIUS.set(1);
			MOUSE_ACTION.set(MouseAction.SHOOT);
		}
	}
	
	public static void setJumpInput() {
		IS_PROJECTILE.set(true);
		int maxDistance = Main.player.get(SkillsC.class).get(Skill.ACROBATICS) + 1;
		MAX_DISTANCE.set(maxDistance);
		AFFECTED_RADIUS.set(1);
		MOUSE_ACTION.set(MouseAction.JUMP);
	}
	
	public static void setKickInput() {
		IS_PROJECTILE.set(true);
		MAX_DISTANCE.set(0);
		AFFECTED_RADIUS.set(1);
		MOUSE_ACTION.set(MouseAction.KICK);
	}
	
	public static void setCastInput() {
		Spell spell = Cast.castedSpell;
		IS_PROJECTILE.set(spell.isProjectile());
		MAX_DISTANCE.set(spell.getRange());
		AFFECTED_RADIUS.set(spell.getArea());
		MOUSE_ACTION.set(MouseAction.CAST);
	}

	public static void setExamineInput() {
		IS_PROJECTILE.set(false);
		MAX_DISTANCE.set(0);
		AFFECTED_RADIUS.set(1);
		MOUSE_ACTION.set(MouseAction.EXAMINE);
	}

	public static void setQuickUseInput() {
		Set<Tile> adjacentTiles = Map.getAdjacentTiles(Main.player.get(PositionC.class).getTile());
		List<Entity> usableEntities = new ArrayList<>();
		adjacentTiles.forEach(t -> {
			t.getEntities().forEach(e -> {
				if(e.has(UsesC.class) && e.get(UsesC.class).quickUse != null) {
					usableEntities.add(e);
				}
			});
		});
		if(usableEntities.isEmpty()) {
			Console.addMessage("There is nothing you can use here.\n");
		}
		else if(usableEntities.size() == 1) {
			Entity usedEntity = usableEntities.get(0);
			Use.execute(usedEntity, Main.player, usedEntity.get(UsesC.class).quickUse);
		}
		else {
			IS_PROJECTILE.set(false);
			MAX_DISTANCE.set(0);
			AFFECTED_RADIUS.set(1);
			MOUSE_ACTION.set(MouseAction.QUICK_USE);
		}
	}
	
	public static void setBuildInput() {
		IS_PROJECTILE.set(false);
		MAX_DISTANCE.set(0);
		AFFECTED_RADIUS.set(1);
		MOUSE_ACTION.set(MouseAction.BUILD);
	}

	protected static MouseAction getMouseAction() {
		return MOUSE_ACTION.get();
	}
	
	protected static void setMouseAction(MouseAction action) {
		MOUSE_ACTION.set(action);
	}
	
	protected static boolean isProjectile() {
		return IS_PROJECTILE.get();
	}
	
	protected static int getMaxDistance() {
		return MAX_DISTANCE.get();
	}
	
	protected static int getAffectedRadius() {
		return AFFECTED_RADIUS.get();
	}

	protected static void setChangeListener(ChangeListener<MouseAction> listener) {
		MOUSE_ACTION.addListener(listener);
	}

}
