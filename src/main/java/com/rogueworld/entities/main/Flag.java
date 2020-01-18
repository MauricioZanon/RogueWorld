package com.rogueworld.entities.main;

public enum Flag {
	
	/** Un item con este flag tiene efectos adversos al ser usado */
	CURSED,
	/** Se puede agarrar y ser arrastrado */
	DRAGGABLE,
	/** Se puede tomar */
	DRINKABLE,
	/** Se puede comer */
	EDIBLE,
	/** Se rompe al ser lanzado o al recibir cualquier cantidad de daño */
	FRAGILE,
	/** Baja un nivel */
	GOES_DOWN,
	/** Sube un nivel */
	GOES_UP,
	/** No se lo puede destruir */
	INDESTRUCTIBLE,
	/** Puede prenderse fuego */
	INFLAMMABLE,
	/** Emite luz */
	LIGHT_SOURCE,
	/** Puede contener líquidos */
	LIQUID_CONTAINER,
	/** Bloquea la visión */
	OPAQUE,
	/** Se puede levantar y guardar en el inventario */
	PICKUPABLE,
	/** No deja que el fuego se extienda */
	SUPRESS_FIRE,
	/** Protege de la lluvia */
	SHELTER,
	/** Se puede usar como ropa o armadura */
	WEARABLE,
	
	
}
