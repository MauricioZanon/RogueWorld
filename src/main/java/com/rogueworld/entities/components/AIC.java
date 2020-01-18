package com.rogueworld.entities.components;

import java.lang.reflect.InvocationTargetException;
import java.util.EnumMap;
import java.util.Map.Entry;

import com.rogueworld.ai.states.State;
import com.rogueworld.ai.states.StateType;
import com.rogueworld.entities.main.Entity;

public class AIC extends Component { // TODO test

	public float nextTurn = 0;
	public boolean isActive = true;
	private EnumMap<StateType, State> states = new EnumMap<>(StateType.class);
	private State activeState = null;

	public AIC() {
		isShared = false;
	}

	public void update() {
		activeState.update();
	}

	public void addState(StateType type, State state) {
		states.put(type, state);
	}

	public State getState() {
		return activeState;
	}

	public void setState(StateType type) {
		if (activeState != null)
			activeState.exitState();
		activeState = states.get(type);
		activeState.enterState();
	}

	@Override
	public AIC clone() {
		AIC newAI = new AIC();
		newAI.nextTurn = nextTurn;
		newAI.isActive = isActive;
		for (Entry<StateType, State> entry : states.entrySet()) {
			try {
				newAI.states.put(entry.getKey(), entry.getValue().getClass().getDeclaredConstructor().newInstance());
			} catch (IllegalArgumentException | InvocationTargetException | SecurityException | InstantiationException
					| IllegalAccessException | NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		newAI.setState(StateType.IDLE);
		return newAI;
	}
	
	public void setOwner(Entity actor) {
		states.values().forEach(s -> s.setOwner(actor));
	}
	
	@Override
	public void serialize(StringBuilder sb) {}

	@Override
	public void deserialize(String info) {}
	
	@Override
	public boolean equals(Component comp) {
		AIC c = (AIC) comp;
		if(c.isActive != isActive) return false;
		return true;
		//FIXME tira siempre false cuando compara dos states iguales de entidades distintas
//		return states.values().equals(c.states.values());
		//TODO comparar active states
	}

}
