package com.rogueworld.entities.components;

import com.rogueworld.ai.pathfind.Path;

public class MovementC extends Component {

	public MovementType movementType = MovementType.WALK;
	public Path path = null;
	
	public MovementC() {
		isShared = false;
	}
	
	@Override
	public MovementC clone() {
		MovementC comp = new MovementC();
		comp.movementType = movementType;
		return comp;
	}

	@Override
	public void serialize(StringBuilder sb) {
		sb.append("MOV:" + movementType);
	}
	
	@Override
	public void deserialize(String info) {
		movementType = MovementType.valueOf(info);
	}
	
	public enum MovementType{
		FLY,
		WALK,
		SWIM,
		AMPHIBIOUS,
	}

	@Override
	public boolean equals(Component comp) {
		return true;
	}

}
