package com.rogueworld.entities.player;

import javafx.scene.paint.Color;

public enum ThirstLevel{
	SATIATED(Color.BLACK),
	THIRSTY(new Color(0.87, 0.65, 0.46, 1)),
	VERY_THIRSTY(new Color(0.8, 0.47, 0.2, 1)),
	PARCHED(new Color(0.87, 0.31, 0.07, 1));
	
	public Color color;
	
	ThirstLevel(Color c){
		color = c;
	}
	
	public static ThirstLevel getLevel(float hunger) {
		if(hunger < 200) return SATIATED;
		if(hunger < 500) return THIRSTY;
		if(hunger < 750) return VERY_THIRSTY;
		return PARCHED;
	}
}
