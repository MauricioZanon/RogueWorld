package com.rogueworld.world.tile;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.function.Predicate;

import com.rogueworld.gui.gamescreen.DrawUtils;
import com.rogueworld.entities.components.BackColorC;
import com.rogueworld.entities.components.GraphicC;
import com.rogueworld.entities.components.MovementC.MovementType;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.components.TransitableC;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Flag;
import com.rogueworld.entities.main.Type;
import com.rogueworld.utils.observerpattern.Notification;
import com.rogueworld.utils.observerpattern.Observable;
import com.rogueworld.utils.rng.RNG;
import com.rogueworld.utils.shadowcasting.ShadowCasting;
import com.rogueworld.world.time.Clock;

import javafx.scene.paint.Color;

public class Tile implements Observable{
	
	private Entity actor;
	private ArrayDeque<Entity> features = new ArrayDeque<>();
	private ArrayDeque<Entity> items = new ArrayDeque<>();
	private Entity terrain;
	
	private Color backColor = Color.BLACK;
	private Color frontColor = Color.BLACK;
	private String ASCII = "";
	
	public PositionC pos;
	private float lightLevel = 0;
	private boolean viewed = false;
	
	private static RNG rng = RNG.getInstance();

	protected Tile(PositionC pos) {
		this.pos = pos;
	}
	
	public void put(Entity entity) {
		if(entity == null) return;
		
		boolean wasTranslucent = isTranslucent();
		entity.addComponent(pos);
		place(entity);

		refreshGraphics();
		if(wasTranslucent != isTranslucent()) {
			notifyObservers(Notification.RECALCULATE_LIGHT);
		}
	}
	
	public void put(Collection<Entity> entities) {
		if(entities.isEmpty()) return;
		
		boolean wasTranslucent = isTranslucent();
		entities.forEach(e -> {
			e.addComponent(pos);
			place(e);
		});
		
		refreshGraphics();
		if(wasTranslucent != isTranslucent()) {
			notifyObservers(Notification.RECALCULATE_LIGHT);
		}
	}
	
	private void place(Entity e) {
		switch(e.type.getSuperType()) {
		case ACTOR:
			actor = e;
			break;
		case ITEM:
			items.add(e);
			break;
		case FEATURE:
			features.add(e);
			break;
		case TERRAIN:
			terrain = e;
			break;
		default:
			break;
		}
		
		if(e.is(Flag.LIGHT_SOURCE)) {
			ShadowCasting.calculateIllumination(e, true);
		}
		refreshGraphics();
	}
	
	public void remove(Entity entity) {
		if(entity == null) return;
		
		boolean wasTranslucent = isTranslucent();
		switch(entity.type.getSuperType()) {
		case ACTOR:
			if(actor.name.equals(entity.name))
				actor = null;
			break;
		case ITEM:
			items.remove(entity);
			break;
		case FEATURE:
			features.remove(entity);
			break;
		case TERRAIN:
			if(terrain.name.equals(entity.name))
				terrain = null;
			break;
		default:
			break;
		}
		refreshGraphics();
		
		if(wasTranslucent != isTranslucent()) {
			notifyObservers(Notification.RECALCULATE_LIGHT);
		}
		if(entity.is(Flag.LIGHT_SOURCE)) {
			ShadowCasting.calculateIllumination(entity, false);
		}
	}
	
	public Entity remove(Type type) {
		boolean wasTranslucent = isTranslucent();
		Entity removedEntity = null;
		switch(type.getSuperType()) {
		case ACTOR:
			if(actor.type.is(type));{
				removedEntity = actor;
				actor = null;
			}
			break;
		case ITEM:
			for(Entity i : items) {
				if(i.type.is(type)) {
					removedEntity = i;
					items.remove(i);
					break;
				}
			}
			break;
		case FEATURE:
			for(Entity f : features) {
				if(f.type.is(type)) {
					removedEntity = f;
					features.remove(f);
					break;
				}
			}
			break;
		case TERRAIN:
			if(terrain.type.is(type)) {
				removedEntity = terrain;
				terrain = null;
			}
			break;
		default:
			break;
		}
		
		if(removedEntity == null) return null;
		
		refreshGraphics();
		if(wasTranslucent != isTranslucent()) {
			notifyObservers(Notification.RECALCULATE_LIGHT);
		}
		if(removedEntity.is(Flag.LIGHT_SOURCE)) {
			ShadowCasting.calculateIllumination(removedEntity, false);
		}
		return removedEntity;
	}
	
	public Entity get(Type type) {
		switch(type.getSuperType()) {
		case ACTOR:
			if(actor != null && actor.type.is(type)){
				return actor;
			}
			break;
		case ITEM:
			for(Entity i : items) {
				if(i.type.is(type)) {
					return i;
				}
			}
			break;
		case FEATURE:
			for(Entity f : features) {
				if(f.type.is(type)) {
					return f;
				}
			}
			break;
		case TERRAIN:
			if(terrain != null && terrain.type.is(type)) {
				return terrain;
			}
			break;
		default:
			break;
		}
		return null;
	}
	
	/**
	 * @param flag
	 * @return una entidad que tenga el flag con esta prioridad:
	 * 		ACTOR > FEATURE > ITEM > TERRAIN
	 */
	public Entity get(Flag flag) {
		if(actor != null && actor.is(flag)){
			return actor;
		}
		
		for(Entity f : features) {
			if(f.is(flag)) return f;
		}
	
		for(Entity i : getEntities(e -> e.type.is(Type.ITEM))) {
			if(i.is(flag))
				return i;
		}
		
		if(terrain.is(flag)){
			return terrain;
		}
		
		return null;
	}
	
	public ArrayDeque<Entity> getEntities(){
		ArrayDeque<Entity> result = new ArrayDeque<>(features);
		result.addAll(items);
		if(actor != null) result.add(actor);
		if(terrain != null) result.add(terrain);
		return result;
	}
	
	public ArrayDeque<Entity> getEntities(Predicate<Entity> cond){
		ArrayDeque<Entity> result = new ArrayDeque<>();
		for(Entity e : getEntities()) {
			if(cond.test(e)) result.add(e);
		}
		return result;
	}
	
	public ArrayDeque<Entity> getEntities(Type type){
		ArrayDeque<Entity> result = new ArrayDeque<Entity>();
		switch(type.getSuperType()) {
		case ACTOR:
			if(actor.type.is(type)){
				result.add(actor);
			}
			break;
		case ITEM:
			for(Entity i : items) {
				if(i.type.is(type)) {
					result.add(i);
				}
			}
			break;
		case FEATURE:
			for(Entity f : features) {
				if(f.type.is(type)) {
					result.add(f);
				}
			}
			break;
		case TERRAIN:
			if(terrain.type.is(type)) {
				result.add(terrain);
			}
			break;
		default:
			break;
		}
		return result;
	}
	
	public boolean has(Type type) {
		switch(type.getSuperType()) {
		case ACTOR:
			if(actor != null && actor.type.is(type)){
				return true;
			}
			break;
		case ITEM:
			for(Entity i : items) {
				if(i.type.is(type)) {
					return true;
				}
			}
			break;
		case FEATURE:
			for(Entity f : features) {
				if(f.type.is(type)) {
					return true;
				}
			}
			break;
		case TERRAIN:
			if(terrain != null && terrain.type.is(type)) {
				return true;
			}
			break;
		default:
			break;
		}
		return false;
	}
	
	public boolean has(Flag flag) {
		for(Entity e : getEntities()){
			if(e.is(flag)) {
				return true;
			}
		}
		return false;
	}

	public Color getBackColor() {
		return backColor == null ? Color.BLACK : backColor;
	}

	public Color getFrontColor() {
		return frontColor;
	}

	public String getASCII() {
		return ASCII;
	}
	
	public boolean isTranslucent() {
		return !has(Flag.OPAQUE);
	}

	public boolean isTransitable(MovementType movType) {//TODO test
		if(terrain == null || !terrain.get(TransitableC.class).isTransitable(movType)) {
			return false;
		}
		if(!features.isEmpty()) {
			for(Entity f : features) {
				if(f.has(TransitableC.class) && !f.get(TransitableC.class).isTransitable(movType)) return false;
			}
		}
		return true;
	}
	
	public float getMovementCost(MovementType movType) {
		float cost = 1;
		
		if(terrain != null) {
			cost *= terrain.get(TransitableC.class).getMovCost(movType);
		}
		for(Entity f : features) {
			if(f.has(TransitableC.class)) {
				cost *= f.get(TransitableC.class).getMovCost(movType);
			}
		}
		
		return cost;
	}
	
	public void changeLightLevel(float v) {
		lightLevel += v;
		if(lightLevel < 0)
			lightLevel = 0;
	}
	
	public float getLightLevel() {
		if(pos.coord[2] == 0) {
			return Clock.getSurfaceLightLevel() + lightLevel;
		}else {
			return lightLevel;
		}
	}
	
	public boolean isViewed() {
		return viewed;
	}

	public void setViewed(boolean viewed) {
		this.viewed = viewed;
	}

	//FIXME si, por ejemplo, el personaje se para sobre un cofre, el color de fondo que 
	//		se dibuja no es el del cofre, sino el del terreno
	private void refreshGraphics() {
		DrawUtils.tilesToDraw.add(this);
		
		Entity visibleEntity = null;
		if(actor != null) {
			visibleEntity = actor;
		}else if(!features.isEmpty()) {
			visibleEntity = features.getLast();
		}else if(!items.isEmpty()) {
			visibleEntity = items.getFirst();
		}else {
			visibleEntity = terrain;
		}
		if(visibleEntity != null) {
			GraphicC graph = visibleEntity.get(GraphicC.class);
			frontColor = graph.color;
			ASCII = rng.getRandom(graph.ASCII.split(" "));
			if(visibleEntity.has(BackColorC.class)) {
				backColor = rng.getRandom(visibleEntity.get(BackColorC.class).colors);
			}else if(terrain != null) {
				backColor = rng.getRandom(terrain.get(BackColorC.class).colors);
			}else {
				backColor = Color.BLACK;
			}
		}
		else {
			ASCII = "";
			frontColor = Color.BLACK;
			backColor = Color.BLACK;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(pos.coord[0] + " " + pos.coord[1] + " " + pos.coord[2] + "\n");
		getEntities().forEach(e -> sb.append(e.name + "\n"));
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object t) {
		return pos.equals(((Tile)t).pos);
	}

	/**
	 * Remueve todos los elementos del tile para que pueda reusarse
	 */
	protected void clear() {
		actor = null;
		features.clear();
		items.clear();
		terrain = null;
		observers.clear();
		ASCII = "";
		backColor = Color.BLACK;
		frontColor = Color.BLACK;
		lightLevel = 0;
		pos = null;
	}

}
