package com.rogueworld.entities.components;

import java.util.HashSet;
import java.util.Set;

import com.rogueworld.world.tile.Tile;

public class LightSourceC extends Component{
	
	public Set<Tile> illuminatedTiles = new HashSet<>();
	
	public LightSourceC() {
		isShared = false;
	}

	@Override
	public LightSourceC clone() {
		LightSourceC c = new LightSourceC();
		c.illuminatedTiles.addAll(illuminatedTiles);
		return c;
	}

	@Override
	public void serialize(StringBuilder sb) {}
	
	@Override
	public void deserialize(String info) {}

	@Override
	public boolean equals(Component comp) {
		LightSourceC c = (LightSourceC) comp;
		return illuminatedTiles.equals(c.illuminatedTiles);
	}

}
