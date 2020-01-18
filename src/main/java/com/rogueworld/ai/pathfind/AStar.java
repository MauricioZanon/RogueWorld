package com.rogueworld.ai.pathfind;

import java.util.ArrayList;
import java.util.Comparator;

import com.rogueworld.entities.components.MovementC;
import com.rogueworld.entities.components.MovementC.MovementType;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.world.chunk.Chunk;
import com.rogueworld.world.map.Map;
import com.rogueworld.world.tile.Tile;

public class AStar implements PathFinder{ //TODO: test
	
	private static final int MAX_DISTANCE = Chunk.SIZE;
	private static Comparator<Node> comparador = new Comparator<Node>(){
		@Override
		public int compare(Node n1, Node n2) {
			if(n1.f < n2.f) return -1;
			else if(n1.f > n2.f) return 1;
			else return 0;
		}
	};
	
	/**
	 * Busca un camino desde la primer posición hasta la segunda, si no es posible devuelve un path vacío
	 * TODO: Hacer que los tiles sean transitables o no dependiendo de el tipo de movimiento del actor
	 */
	public static Path findPath(PositionC start, PositionC end, Entity actor){
		MovementType movType = actor.get(MovementC.class).movementType;
		if(!end.getTile().isTransitable(movType)){
			return new Path();
		}
		
		ArrayList<Node> open = new ArrayList<Node>();
		ArrayList<Node> closed = new ArrayList<Node>();
		
		Node currentNode = new Node(start, null, 0, Map.getDistance(start, end));
		open.add(currentNode);
		
		while(!open.isEmpty()){
			open.sort(comparador);
			currentNode = open.remove(0);
			closed.add(currentNode);
			
			if(currentNode.pos.equals(end)){
				return createPath(currentNode);
			}
			
			// Evaluando tiles adyacentes
			for(Tile tile : Map.getOrthogonalTiles(currentNode.pos.getTile(), t -> t.isTransitable(movType))){
				PositionC neighborPos = tile.pos;
				double g = Map.getDistance(start, neighborPos);
				if(g > MAX_DISTANCE){
					continue;
				}
				double h = currentNode.h + Map.getDistance(currentNode.pos, end);
				Node neighborNode = new Node(neighborPos, currentNode, g, h);
				
				if(!nodePresent(closed, neighborNode) && (!nodePresent(open, neighborNode) || h < getNode(open, neighborNode).h)){
					open.add(neighborNode);
				}
			}
			for(Tile tile : Map.getDiagonalTiles(currentNode.pos.getTile(), t -> t.isTransitable(movType))){
				PositionC neighborPos = tile.pos;
				double g = Map.getDistance(start, neighborPos);
				if(g > MAX_DISTANCE){
					continue;
				}
				double h = currentNode.h + Map.getDistance(currentNode.pos, end);
				Node neighborNode = new Node(neighborPos, currentNode, g, h);
				
				if(!nodePresent(closed, neighborNode) && (!nodePresent(open, neighborNode) || h < getNode(open, neighborNode).h)){
					open.add(neighborNode);
				}
			}
		}
		return new Path();
	}
	
	private static Path createPath(Node currentNode){
		Path path = new Path();
		while(currentNode.parent != null){
			path.addFirst(currentNode.pos);
			currentNode = currentNode.parent;
		}
		return path;
	}
	
	private static boolean nodePresent(ArrayList<Node> lista, Node n){
		for(Node node : lista){
			if(node.pos.equals(n.pos)){
				return true;
			}
		}
		return false;
	}
	
	private static Node getNode(ArrayList<Node> list, Node n){
		for(Node node : list){
			if(node.pos.equals(n.pos)){
				return node;
			}
		}
		return null;
	}

	public int getWalkableDistance(Tile start, Tile end) {
		Path path = findPath(start.pos, end.pos, null);
		return path == null ? -1 : path.getLength();
	}
	

}
	