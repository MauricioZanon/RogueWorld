package com.rogueworld.entities.main;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map.Entry;

import com.rogueworld.entities.components.AIC;
import com.rogueworld.entities.components.BodyC;
import com.rogueworld.entities.components.Component;
import com.rogueworld.utils.observerpattern.Notification;
import com.rogueworld.utils.observerpattern.Observer;

public class Entity implements Cloneable, Observer{
	
	private HashMap<Class<? extends Component>, Component> components = new HashMap<>();
	private EnumMap<Att, Float> attributes = new EnumMap<>(Att.class);
	private EnumSet<Flag> flags = EnumSet.noneOf(Flag.class);
	public Type type;
	public final int ID;
	public String name = "no name";
	public String description = "no desc";
	
	public Entity(Type type, int id, String name) {
		this.type = type;
		this.ID = id;
		this.name = name;
	}
	
	public void addComponent(Component c) {
		if (c != null) {
			components.put(c.getClass(), c);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Component> T removeComponent (Class<T> componentClass) {
		return (T)components.remove(componentClass);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Component> T get(Class<T> componentClass) {
		return (T)components.get(componentClass);
	}

	public boolean has(Class<? extends Component> componentClass) {
		return components.containsKey(componentClass);
	}
	
	/**
	 * Devuelve el valor del atributo teniendo en cuenta los modificadores
	 */
	public float get(Att attribute) {
		float value = attributes.containsKey(attribute) ? attributes.get(attribute) : 0;
		if(has(BodyC.class)) {
			for(Entity equipment : get(BodyC.class).getEquipment()) {
				value += equipment.getBase(attribute);
			}
		}
		return value;
	}
	
	/**
	 * Devuelve el valor del atributo sin tomar en cuenta los modificadores
	 */
	public float getBase(Att attribute) {
		return attributes.containsKey(attribute) ? attributes.get(attribute) : 0;
	}
	
	/**
	 * Si este atributo ya existía se reemplaza su valor por value
	 */
	public void setAttribute(Att attribute, float value) {
		attributes.put(attribute, value);
	}
	
	/**
	 *  Se le suma value al atributo, si no existe se agrega
	 */
	public void changeAttribute(Att attribute, float value) {
		if(!attributes.containsKey(attribute)) {
			attributes.put(attribute, value);
		}
		else {
			attributes.put(attribute, attributes.get(attribute) + value);
		}
	}
	
	public EnumMap<Att, Float> getAttributes() {
		return attributes;
	}
	
	public EnumSet<Flag> getFlags() {
		return flags;
	}

	public void addFlag(Flag flag) {
		flags.add(flag);
	}
	
	public void removeFlag(Flag flag) {
		flags.remove(flag);
	}
	
	public boolean is(Flag flag) {
		return flags.contains(flag);
	}

	public HashMap<Class<? extends Component>, Component> getComponents() {
		return components;
	}
	
	@Override //TODO test
	public Entity clone() {
		Entity e = new Entity(type, ID, name);
		e.description = description;
		
		components.values().forEach(c -> e.addComponent(c.isShared() ? c : c.clone()));
		attributes.forEach((k, v) -> e.setAttribute(k, v));
		flags.forEach(f -> e.addFlag(f));
		
		if(e.has(AIC.class)) {
			e.get(AIC.class).setOwner(e);
		}
		
		return e;
	}
	
	public boolean equals(Entity e) {
		if(e == null) return false;
		if(ID != e.ID) return false;
		
		for(Entry<Class<? extends Component>, Component> entry : components.entrySet()) {
			if(!entry.getValue().equals(e.get(entry.getKey()))) {
				System.err.println(entry.getKey());
				return false;
			}else {
				System.out.println(entry.getKey());
			}
		}
		return ID == e.ID && e.components.equals(e.components);
	}
	
	@Override
	public void notify(Notification not) {
		not.resolve(this);
	}
	
}
