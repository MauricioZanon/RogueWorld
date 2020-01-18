package com.rogueworld.utils.observerpattern;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

public interface Observable {
	
	EnumMap<Notification, Set<Observer>> observers = new EnumMap<>(Notification.class);
	
	public default void addObserver(Notification not, Observer obs) {
		if(!observers.containsKey(not)) {
			observers.put(not, new HashSet<Observer>());
		}
		observers.get(not).add(obs);
	}
	
	public default void removeObserver(Notification not, Observer obs) {
		if(observers.containsKey(not)) {
			observers.get(not).remove(obs);
			if(observers.get(not).isEmpty()) {
				observers.remove(not);
			}
		}
	}
	
	public default void notifyObservers(Notification not) {
		if(observers.containsKey(not)) {
			observers.get(not).forEach(o -> o.notify(not));
		}
	}

}
