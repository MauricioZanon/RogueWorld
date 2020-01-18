package com.rogueworld.entities.main;

import java.util.EnumSet;

/**
 * El tipo de entidad
 * Cada tipo guarda un atributo (group) que es el tipo del cual desciende y un boolean que dice si es stackable en un tile
 */
public enum Type {
	
	ACTOR(null, false),
		PLAYER(ACTOR, false),
		NPC(ACTOR, false),
	
	TERRAIN(null, false),
		FLOOR(TERRAIN, false),
		WALL(TERRAIN, false),
		HOLE(TERRAIN, false),
		WATER(TERRAIN, false),
	
	ITEM(null, true),
		CORPSE(ITEM, true),
		EQUIPMENT(ITEM, true),
			WEAPON(EQUIPMENT, true),
				AXE(WEAPON, true),
				DAGGER(WEAPON, true),
				MACE(WEAPON, true),
				SWORD(WEAPON, true),
				RANGED(WEAPON, true),
					BOW(RANGED, true),
				MUNITION(WEAPON, true),
					ARROW(MUNITION, true),
			ARMOR(EQUIPMENT, true),
				HELMET(ARMOR, true),
				GAUNTLETS(ARMOR, true),
				BREASTPLATE(ARMOR, true),
				GREAVES(ARMOR, true),
				BOOTS(ARMOR, true),
			CLOTHES(EQUIPMENT, true),
				HAT(CLOTHES, true),
				GLOVES(CLOTHES, true),
				SHIRT(CLOTHES, true),
				PANTS(CLOTHES, true),
				SOCKS(CLOTHES, true),
			JEWELRY(EQUIPMENT, true),
				RING(JEWELRY, true),
				NECKLACE(JEWELRY, true),
		BOOK(ITEM, true),
		TOOL(ITEM, true),
		CONSUMABLE(ITEM, true),
			POTION(CONSUMABLE, true),
			SCROLL(CONSUMABLE, true),
		MATERIAL(ITEM, true),
		
	FEATURE(null, false),
		CONTAINER(FEATURE, false),
		DOOR(FEATURE, false),
		FURNITURE(FEATURE, false),
		MINERAL_VEIN(FEATURE, false),
		TRAP(FEATURE, false),
		TREE(FEATURE, false),
		VEGETATION(FEATURE, false),
		WORK_STATION(FEATURE, false);
	
	private Type superType = null;
	private boolean isTileStackable;
		
	private Type(Type group, boolean isTileStackable) {
	    this.superType = group;
	    this.isTileStackable = isTileStackable;
	}
	
	/** Devuelve true si es el mismo tipo o subtipo */
	public boolean is(Type other) {
	   if(this == other) return true;
	   else if(superType == null) return false;
	   else return superType.is(other);
	}
	
	public boolean isLeaf() {
		for(Type t : EnumSet.allOf(Type.class)) {
			if(t.superType == this) {
				return false;
			}
		}
		return true;
	}
	
	public Type getSuperType() {
		return superType == null ? this : superType.getSuperType();
	}

	public boolean isTileStackable() {
		return isTileStackable;
	}

}
