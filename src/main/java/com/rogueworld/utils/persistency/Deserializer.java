package com.rogueworld.utils.persistency;

import com.rogueworld.entities.components.AbilitiesC;
import com.rogueworld.entities.components.BodyC;
import com.rogueworld.entities.components.Component;
import com.rogueworld.entities.components.ContainerC;
import com.rogueworld.entities.components.HealthC;
import com.rogueworld.entities.components.MaterialC;
import com.rogueworld.entities.components.MovementC;
import com.rogueworld.entities.components.SkillsC;
import com.rogueworld.entities.components.StatusEffectsC;
import com.rogueworld.entities.factories.EntityFactory;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.world.chunk.Chunk;
import com.rogueworld.world.tile.Tile;

public class Deserializer {

	public static Chunk deserialize(String chunkPos, String chunkString) {
		Chunk chunk = new Chunk(chunkPos);

		int size = Chunk.SIZE;
		Tile[][] chunkMap = chunk.getMap();

		String[] tileStrings = chunkString.split("/");
		for (int i = 0; i < tileStrings.length; i++) {
			String[] entitiesStrings = tileStrings[i].split(",");
			int tileX = i / size;
			int tileY = i % size;
			Tile t = chunkMap[tileX][tileY];
			if (entitiesStrings.length > 1) {
				t.setViewed(Boolean.parseBoolean(entitiesStrings[0]));
				for (int j = 1; j < entitiesStrings.length; j++) {
					if (!entitiesStrings[j].equals("")) {
						t.put(deserializeEntity(entitiesStrings[j]));
					}
				}
			}
		}

		return chunk;
	}

	private static Entity deserializeEntity(String entityString) {
		String[] components = entityString.split("-");

		Entity e = EntityFactory.create(Integer.parseInt(components[0]));

		for (int i = 1; i < components.length; i++) {
			deserializeComponent(components[i], e);
		}

		return e;
	}

	private static void deserializeComponent(String componentString, Entity e) {
		String[] componentInfo = componentString.split(":");

		Component c = null;

		switch (componentInfo[0]) {
		case "ABI":
			c = new AbilitiesC();
			break;
		case "BOD":
			c = new BodyC();
			break;
		case "CON":
			c = new ContainerC();
			break;
		case "HEA":
			c = new HealthC();
			break;
		case "MAT":
			c = new MaterialC();
			break;
		case "MOV":
			c = new MovementC();
			break;
		case "SKI":
			c = new SkillsC();
			break;
		case "STA":
			c = new StatusEffectsC();
			break;
		default:
			c = null;
			break;
		}

		if (c != null) {
			c.deserialize(componentInfo[1]);
			e.addComponent(c);
		} else {
			System.out.println("Se intent� deserializar un string de component inv�lido:\n\t" + componentString);
		}
	}

}
