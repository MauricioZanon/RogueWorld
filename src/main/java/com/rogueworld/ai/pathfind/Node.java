package com.rogueworld.ai.pathfind;

import com.rogueworld.entities.components.PositionC;

public class Node {
	
	public PositionC pos;
	public Node parent;
	public double f = 0, g = 0, h = 0;
	
	/**
	 * @param p = posision que representa
	 * @param par = tile anterior en el camino
	 * @param g = distancia desde el origen hasta el tile actual
	 * @param h = distancia desde el tile actual hasta el destino
	 */
	public Node(PositionC p, Node par, double g, double h){
		pos = p;
		parent = par;
		this.g = g;
		this.h = h;
		f = g + h;
	}
	
}
