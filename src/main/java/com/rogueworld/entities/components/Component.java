package com.rogueworld.entities.components;

public abstract class Component implements Cloneable{

	/** Si esto es true entonces el componente no se copia al crearse una nueva entidad que lo use, sino que es compartido
	 * entre todas las entidades del mismo tipo */
	protected boolean isShared;
	
	public abstract Component clone();
	public abstract void serialize(StringBuilder sb);
	public abstract void deserialize(String info);
	public abstract boolean equals(Component comp);
	
	public boolean isShared() {
		return isShared;
	}
	
}
