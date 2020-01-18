package com.rogueworld.entities.player;

import javafx.beans.property.SimpleFloatProperty;

public class PlayerInfo {
	
	// TODO: guardar el player aca y sacarlo de la clase Main
	public static final SimpleFloatProperty MAX_HP = new SimpleFloatProperty(0);
	public static final SimpleFloatProperty CUR_HP = new SimpleFloatProperty(0);
	
	public static final SimpleFloatProperty HUNGER = new SimpleFloatProperty();
	public static final SimpleFloatProperty THIRST = new SimpleFloatProperty();
	
	private PlayerInfo() {}
	
}
