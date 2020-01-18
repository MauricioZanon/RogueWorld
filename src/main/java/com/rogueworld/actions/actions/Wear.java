package com.rogueworld.actions.actions;

import java.util.Set;

import com.rogueworld.gui.gamescreen.Console;
import com.rogueworld.entities.components.BodyC;
import com.rogueworld.entities.components.BodyC.BodyPart;
import com.rogueworld.entities.components.OccupiesC;
import com.rogueworld.entities.main.Entity;

import javafx.scene.paint.Color;

public abstract class Wear {
	
	public static void execute(Entity actor, Entity armor) {
		Set<BodyPart> freePartsNeeded = armor.get(OccupiesC.class).occupies;
		
		if(actor.get(BodyC.class).isOccupied(freePartsNeeded)) {
			createFailMessage(actor.get(BodyC.class).getConflictingEquipment(freePartsNeeded));
		}else {
			Console.addMessage("You wear the -" + armor.name + "-.\n", Color.WHITE, Color.CADETBLUE, Color.WHITE);
		}
		EndTurn.execute(actor, ActionType.EQUIP);
	}
	
	private static void createFailMessage(Set<Entity> confEquipment) {
		StringBuilder sb = new StringBuilder();
		sb.append("First you need to take off your -");
		confEquipment.forEach(e -> sb.append(e.name + " "));
		Console.addMessage(sb.toString(), Color.WHITE, Color.CADETBLUE, Color.WHITE);
	}
	
}
