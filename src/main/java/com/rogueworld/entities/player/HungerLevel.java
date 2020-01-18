package com.rogueworld.entities.player;

import javafx.scene.paint.Color;

public enum HungerLevel{
	BLOATED(new Color(0.08, 0.47, 0.16, 1)),
	FULL(new Color(0.12, 0.75, 0.25, 1)),
	SATIATED(Color.BLACK),
	HUNGRY(new Color(0.87, 0.65, 0.46, 1)),
	VERY_HUNGRY(new Color(0.8, 0.47, 0.2, 1)),
	STARVING(new Color(0.87, 0.31, 0.07, 1));
	
	public Color color;
	
	HungerLevel(Color c) {
		color = c;
	}
	
	public static HungerLevel getLevel(float hunger) {
		if(hunger < -500) return BLOATED;
		if(hunger < -200) return FULL;
		if(hunger < 200) return SATIATED;
		if(hunger < 500) return HUNGRY;
		if(hunger < 750) return VERY_HUNGRY;
		return STARVING;
	}
}
