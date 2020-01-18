package com.rogueworld.entities.components;

import java.util.Arrays;

import com.rogueworld.world.chunk.Chunk;
import com.rogueworld.world.map.Map;
import com.rogueworld.world.tile.Tile;

/**Guarda las coordenadas globales de la entidad*/
public class PositionC extends Component{
	
	public int[] coord = new int[3];
	
	public PositionC() {
		isShared = false;
	}
	
	public int getGx() {
		if(coord[0] < 0) return coord[0] / Chunk.SIZE - 1;
		else return coord[0] / Chunk.SIZE;
	}
	public int getLx() {
		return coord[0] % Chunk.SIZE;
	}
	public int getGy() {
		if(coord[1] < 0) return coord[1] / Chunk.SIZE - 1;
		else return coord[1] / Chunk.SIZE;
	}
	public int getLy() {
		return coord[1] % Chunk.SIZE;
	}
	public int getGz() {
		return coord[2];
	}
	
	public Tile getTile(){
		return Map.getTile(coord[0], coord[1], coord[2]);
	}
	
	@Override
	public String toString(){
		return coord[0] + ":" + coord[1] + ":" + coord[2];
	}
	
	@Override
	public boolean equals(Object o) {
		int[] otherCoords = ((PositionC)o).coord;
		return coord[0] == otherCoords[0] && coord[1] == otherCoords[1] && coord[2] == otherCoords[2];
	}
	
	@Override
	public PositionC clone() {
		PositionC c = new PositionC();
		c.coord = new int[]{coord[0], coord[1], coord[2]};
		return c;
	}
	
	@Override
	public void serialize(StringBuilder sb) {}
	
	@Override
	public void deserialize(String info) {}

	@Override
	public boolean equals(Component comp) {
		PositionC c = (PositionC) comp;
		return Arrays.equals(coord, c.coord);
	}

}
