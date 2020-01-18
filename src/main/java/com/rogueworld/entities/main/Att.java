package com.rogueworld.entities.main;

/**
 * Los atributos que puede tener una entidad
 */
public enum Att {
	
	/** Daño de meele, requerimiento para usar armaduras o armas efectivamente, cargar mas items, arrastrar objetos, etc */
	STR,
	/** Hp y stamina, resistencia a enfermedades */
	CON,
	/** Evasión */
	DEX,
	/** Daño de ataque ranged y crit chance */
	CUN,
	/** Daño de hechizos y facilidad de casteo y crafteo */
	INT,
	/** Defensa mágica e identificación de items */
	WIS,
	/** Ayuda a encontrar cosas ocultas */
	PER,
	
	/** Velocidad de movimiento */
	MOV_SPEED,
	/** Velocidad de ataque */
	ATTACK_SPEED,
	/** Velocidad de casteo */
	CAST_SPEED,
	
	/** Daño al atacar */
	DAMAGE,
	/** Defensa al daño físico */
	DEFENSE,
	/** La intensidad de la luz emitida */
	LIGHT_INTENSITY,
	/** Rango de ataque */
	RANGE,
	
	/** Nutrición dada por la comida */
	NUTRITION,
	/** Peso de la entidad */
	WEIGHT,
}
