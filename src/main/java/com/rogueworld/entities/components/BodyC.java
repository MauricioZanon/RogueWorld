package com.rogueworld.entities.components;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import com.rogueworld.entities.factories.EntityFactory;
import com.rogueworld.entities.main.Entity;

public class BodyC extends Component{
	
	private EnumSet<BodyPart> body = EnumSet.noneOf(BodyPart.class);
	private Set<Entity> equipment = new HashSet<>();
	private Entity itemInRightHand = null;
	
	public BodyC() {
		isShared = false;
	}

	/** Equipa un item a menos que el slot esté ocupado */ 
	public void equip(Entity item) {
		OccupiesC c = item.get(OccupiesC.class);
		if(c != null && !isOccupied(c.occupies)) {
			equipment.add(item);
		}
	}
	
	public boolean isOccupied(Set<BodyPart> parts) {
		if(parts.isEmpty()) {
			return false;
		}
		for(Entity e : equipment) {
			if(!Collections.disjoint(e.get(OccupiesC.class).occupies, parts)) { // true si no hay elementos en comun
				return true;
			}
		}
		return false;
	}
	
	public Set<Entity> getConflictingEquipment(Set<BodyPart> parts) {
		Set<Entity> result = new HashSet<>();

		for(Entity e : equipment) {
			if(!Collections.disjoint(e.get(OccupiesC.class).occupies, parts)) {
				result.add(e);
			}
		}
		
		return result;
	}
	
	public void remove(Entity item) {
		equipment.remove(item);
	}
	
	public void add(BodyPart part) {
		body.add(part);
	}
	
	public void remove(BodyPart part) {
		body.remove(part);
	}
	
	public void setWeapon(Entity w) {
		itemInRightHand = w;
	}
	
	public Entity getWeapon() {
		return itemInRightHand;
	}
	
	public Set<Entity> getEquipment() {
		return equipment;
	}

	@Override
	public BodyC clone() {
		BodyC comp = new BodyC();
		comp.body.addAll(body);
		equipment.forEach(e -> {
			comp.equipment.add(e.clone());
		});
		if(itemInRightHand != null) {
			comp.equip(itemInRightHand.clone());
		}
		return comp;
	}

	@Override
	public void serialize(StringBuilder sb) {
		sb.append("BOD:");
		body.forEach(b -> sb.append(b + "&"));
		
		if(!equipment.isEmpty()) {
			sb.append(" ");
			equipment.forEach(e -> sb.append(e.ID + "&"));
			if(itemInRightHand != null) {
				sb.append(itemInRightHand.ID);
			}
		}
	}
	

	@Override
	public void deserialize(String info) {
		String[]infoArray = info.split(" ");
		
		String[] bodyArray = infoArray[0].split("&");
		for(int i = 0; i < bodyArray.length; i++) {
			body.add(BodyPart.valueOf(bodyArray[i]));
		}
		
		if(infoArray.length > 1) {
			String[] equipmentArray = infoArray[1].split("&");
			for(int i = 0; i < equipmentArray.length; i++) {
				equip(EntityFactory.create(equipmentArray[i]));
			}
		}
		
	}

	@Override
	public boolean equals(Component comp) {
		BodyC c = (BodyC) comp;
		
		if(itemInRightHand != null && !itemInRightHand.equals(c.itemInRightHand)) return false;
		//FIXME tira false aunque contengan las mismas entidades
//		if(!c.equipment.equals(equipment)) return false;
		if(!c.body.equals(body)) return false;
		
		return true;
	}


	public enum BodyPart{
		HEAD,
		R_EYE,
		L_EYE,
		R_EAR,
		L_EAR,
		NOSE,
		MOUTH,
		NECK,
		TORSO,
		R_ARM,
		L_ARM,
		LOWER_BODY,
		R_LEG,
		L_LEG,
		R_HAND,
		L_HAND,
		R_FOOT,
		L_FOOT,
		
		BL_LEG,
		BR_LEG,
		FL_LEG,
		FR_LEG,
		BL_PAW,
		BR_PAW,
		FL_PAW,
		FR_PAW,
		BL_CLAW,
		BR_CLAW,
		FL_CLAW,
		FR_CLAW,
		TAIL,
	}
	
}
