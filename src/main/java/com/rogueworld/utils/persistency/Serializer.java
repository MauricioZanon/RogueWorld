package com.rogueworld.utils.persistency;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import com.rogueworld.entities.components.AbilitiesC;
import com.rogueworld.entities.components.BodyC;
import com.rogueworld.entities.components.ButcherC;
import com.rogueworld.entities.components.Component;
import com.rogueworld.entities.components.ContainerC;
import com.rogueworld.entities.components.FieldDressC;
import com.rogueworld.entities.components.HealthC;
import com.rogueworld.entities.components.SkinC;
import com.rogueworld.entities.components.StatusEffectsC;
import com.rogueworld.entities.factories.EntityFactory;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;
import com.rogueworld.world.chunk.Chunk;
import com.rogueworld.world.tile.Tile;

//Ideas:
//1 - una lista de Component.class con todas las clases de componentes que se deben serializar apra cada tipo de entidad

public class Serializer {
	
	private static final StringBuilder sb = new StringBuilder();
	
	private static final List<Class<? extends Component>> actorComponents = new ArrayList<>();
	private static final List<Class<? extends Component>> featureComponents = new ArrayList<>();
	
	static {
		actorComponents.add(AbilitiesC.class);
		actorComponents.add(BodyC.class);
		actorComponents.add(ButcherC.class);
		actorComponents.add(ContainerC.class);
		actorComponents.add(FieldDressC.class);
		actorComponents.add(HealthC.class);
		actorComponents.add(SkinC.class);
		actorComponents.add(StatusEffectsC.class);
		
		featureComponents.add(ContainerC.class);
//		featureComponents.add(LockC.class);
//		featureComponents.add(UseC.class);
	}
	
	protected static String serialize(Chunk c) {
		sb.setLength(0);
		
		Tile[][] chunkMap = c.getMap();
		for(int x = 0; x < chunkMap.length; x++) {
			for(int y = 0; y < chunkMap[0].length; y++) {
				Tile tile = chunkMap[x][y];
				if(tile != null) {
					sb.append(tile.isViewed() + ",");
					serializeTile(chunkMap[x][y]);
					
				}
				sb.append("/");
			}
		}
		return sb.toString();
	}
	
	private static void serializeTile(Tile t) {
		ArrayDeque<Entity> entities = t.getEntities();
		while(!entities.isEmpty()) {
			serializeEntity(entities.removeFirst());
			sb.append(",");
		}
	}
	
	private static void serializeEntity(Entity e) {
		Type superType = e.type.getSuperType();
		
		sb.append(e.ID + "-");
		
		switch(superType) {
		case ACTOR:
			serializeActor(e);
			break;
		case TERRAIN:
			break;
		case FEATURE:
			serializeFeature(e);
			break;
		case ITEM:
			break;
		default:
			break;
		}
	}
	
//	TODO: para serializar tipos de entidades espcificas (actor, feature, etc) capaz sea mejor pasar como 
//	parametro la lista de componentes que se deberian serializar para no repetir tanto codigo

	private static void serializeActor(Entity actor) {
		Entity baseEntity = EntityFactory.getBase(actor.ID);
		actorComponents.forEach(comp -> {
			if(actor.has(comp) && !actor.get(comp).equals(baseEntity.get(comp))) {
				actor.get(comp).serialize(sb);
				sb.append("-");
			}
		});
	}
	
	private static void serializeFeature(Entity feature) {
		Entity baseEntity = EntityFactory.getBase(feature.ID);
		featureComponents.forEach(comp -> {
			if(feature.has(comp) && !feature.get(comp).equals(baseEntity.get(comp))) {
				feature.get(comp).serialize(sb);
				sb.append("-");
			}
		});
	}

}

