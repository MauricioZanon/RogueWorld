package com.rogueworld.entities.components;

import java.util.HashSet;

import com.rogueworld.world.tile.Tile;
import com.rogueworld.world.world.Direction;

public class VisionC extends Component{
	
	public Direction faceDir = Direction.N;
	public int sightRange = 26;
	public HashSet<Tile> visionMap = new HashSet<>();
	public HashSet<Tile> enemyTiles = new HashSet<>();
	
	public VisionC() {
		isShared = false;
	}

	public void clear() {
		visionMap.clear();
		enemyTiles.clear();
	}
	
	@Override
	public Component clone() {
		VisionC vc = new VisionC();
		vc.sightRange = sightRange;
		vc.visionMap.addAll(visionMap);
		vc.enemyTiles.addAll(enemyTiles);
		return vc;
	}

	@Override
	public void serialize(StringBuilder sb) {}
	
	@Override
	public void deserialize(String info) {}

	@Override
	public boolean equals(Component comp) {
		return sightRange == ((VisionC) comp).sightRange;
	}

}
