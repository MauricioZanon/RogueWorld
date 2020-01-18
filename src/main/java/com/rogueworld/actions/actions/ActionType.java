package com.rogueworld.actions.actions;

import com.rogueworld.entities.main.Att;

/**
 * Los tipos de acción que pueden terminar un turno
 * TODO Cada acción tiene que tener un delay base (6 segundos para caminar por ejemplo) que se reduce o aumenta dependiendo del stat
 * 		asociado
 */
public enum ActionType{
	ATTACK(Att.ATTACK_SPEED, 1),
	CAST_SPELL(Att.CAST_SPEED, 5),
	CRAFT(Att.MOV_SPEED, 1),
	EQUIP(Att.MOV_SPEED, 100),
	PICK_UP(Att.MOV_SPEED, 4),
	THROW(Att.ATTACK_SPEED, 2),
	TURN(Att.MOV_SPEED, 0.01f),
	USE_ITEM(Att.MOV_SPEED, 10),
	WAIT(Att.MOV_SPEED, 0.1f),
	WALK(Att.MOV_SPEED, 2),
	WIELD(Att.MOV_SPEED, 10);
	
	/** Stat que se usa como modificador para calcular el tiempo total */
	public Att asociatedStat;
	/** Tiempo en segundos que requiere este tipo de acci�n */
	public float timeNeeded;
	
	ActionType(Att as, float time){
		asociatedStat = as;
		timeNeeded = time;
	}
}
