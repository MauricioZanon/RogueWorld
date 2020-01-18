package com.rogueworld.utils.observerpattern;

import java.util.function.Consumer;

import com.rogueworld.ai.eventsystem.EventSystem;
import com.rogueworld.entities.main.Entity;

public enum Notification {

	RECALCULATE_LIGHT(e -> {
		EventSystem.addLight(e);
	});
	
	private Consumer<Entity> action;
	
	Notification(Consumer<Entity> a) {
		action = a;
	}
	
	public void resolve(Entity e) {
		action.accept(e);
	}
	
}
