package com.rogueworld.entities.components;

import java.util.EnumMap;
import java.util.Map.Entry;

import com.rogueworld.entities.components.MovementC.MovementType;

/**
 * No es un flag porque el que sea transitable o no depende del tipo de movimiento usado
 * TODO: implementar los distintos tipos de movimiento y sacar este comentario
 * TODO: quitar toda la lógica
 */

public class TransitableC extends Component{
	
	private EnumMap<MovementType, Float> acceptedMovementTypes = new EnumMap<>(MovementType.class);
	
	public TransitableC() {
		isShared = true;
	}
	
	public boolean isTransitable(MovementType movType) {
		return acceptedMovementTypes.containsKey(movType);
	}
	
	public float getMovCost(MovementType movType) {
		if(acceptedMovementTypes.containsKey(movType)) {
			return acceptedMovementTypes.get(movType);
		}
		else {
			return 0;
		}
	}
	
	public void add(MovementType type, float cost) {
		acceptedMovementTypes.put(type, cost);
	}

	@Override
	public TransitableC clone() {
		TransitableC c = new TransitableC();
		for(Entry<MovementType, Float> e : acceptedMovementTypes.entrySet()) {
			c.acceptedMovementTypes.put(e.getKey(), e.getValue());
		}
		return c;
	}

	@Override
	public void serialize(StringBuilder sb) {}
	
	@Override
	public void deserialize(String info) {}

	@Override
	public boolean equals(Component comp) {
		return acceptedMovementTypes.equals(((TransitableC) comp).acceptedMovementTypes);
	}

}
