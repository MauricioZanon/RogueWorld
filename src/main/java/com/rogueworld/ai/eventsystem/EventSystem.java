package com.rogueworld.ai.eventsystem;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import com.rogueworld.application.Main;
import com.rogueworld.entities.components.AIC;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;
import com.rogueworld.utils.shadowcasting.ShadowCasting;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;

public class EventSystem {
	
	private static float gameTurn = 0;
	private static PriorityQueue<Entity> entities = new PriorityQueue<>(createComparator());
	
	private static final SimpleBooleanProperty isPlayersTurnProperty = new SimpleBooleanProperty(false);
	
	/** Las luces que hay que re calcular se guardan aca, y se calculan antes de que empieze el turno del personaje, 
	 * es para no calcular una luz mas de una vez por turno */
	private static Set<Entity> lights = new HashSet<>();
	
	private EventSystem() {}
	
	public static void setTimedEntities(Set<Entity> e) {
		entities.clear();
		if(!e.isEmpty()) {
			e.removeIf(a -> !a.get(AIC.class).isActive);
			entities.addAll(e);
		}
	}
	
	public static void update() {
		while(!isPlayersTurnProperty.getValue()) {
			if(!entities.contains(Main.player)) {
				entities.add(Main.player);
			}
			Entity entity = entities.remove();
			
			AIC AI = entity.get(AIC.class);
			
			if(!AI.isActive) {
				continue;
			}
			
			float entityTurn = AI.nextTurn;
			if(entityTurn < gameTurn) { 
				AI.nextTurn = gameTurn+1;
			}else {
				gameTurn = entityTurn;
			}
			AI.update();
			
			if(entity.type == Type.PLAYER) break;
			
			entities.add(entity);
		}
	}
	
	private static Comparator<Entity> createComparator(){
		return new Comparator<Entity>() {
			@Override
			public int compare(Entity a, Entity b) {
				float turnA = a.get(AIC.class).nextTurn;
				float turnB = b.get(AIC.class).nextTurn;
				
				if(turnA > turnB) return 1;
				else if(turnA < turnB) return -1;
				else return 0;
			}
		};
	}

	public static PriorityQueue<Entity> getEntities() {
		return entities;
	}
	
	public static void setPlayerTurn(boolean newValue) {
		if(newValue) {
			recalculateLights();
		}
		else {
			entities.add(Main.player);
		}
		Platform.runLater(() -> isPlayersTurnProperty.set(newValue));
	}
	
	public static boolean isPlayersTurn() {
		return isPlayersTurnProperty.get();
	}

	public static SimpleBooleanProperty getIsPlayersTurnProperty() {
		return isPlayersTurnProperty;
	}
	
	public static void addLight(Entity light) {
		lights.add(light);
	}
	
	private static void recalculateLights() {
		lights.forEach(l -> {
			ShadowCasting.calculateIllumination(l, true);
		});
		lights.clear();
	}
	
}