package com.rogueworld.ai.pathfind;

import java.util.ArrayDeque;

import com.rogueworld.entities.components.PositionC;

public class Path {
	
	private ArrayDeque<PositionC> path = new ArrayDeque<>();
	
	/**
	 * Agrega una posición al principio del path, se usa solo cuando el path se está creando
	 * @param pos
	 */
	public void addFirst(PositionC pos){
		path.addFirst(pos);
	}
	
	/**
	 * @return La próxima posición del path
	 */
	public PositionC getNext(){
		return path.getFirst();
	}
	
	/**
	 * Saca la primera posicion del path
	 */
	public void advance(){
		path.removeFirst();
	}
	
	/**
	 * @return si ya se recorrió
	 */
	public boolean isEnded(){
		return path.isEmpty();
	}
	
	/**
	 * @return la distancia que recorre en tiles
	 */
	public int getLength(){
		return path.size();
	}
	
	/**
	 * @return la posición al final del path
	 */
	public PositionC getDestination() {
		return path.getLast();
	}

}
