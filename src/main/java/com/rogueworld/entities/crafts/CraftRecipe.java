package com.rogueworld.entities.crafts;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.rogueworld.application.Main;
import com.rogueworld.entities.components.ContainerC;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.components.SkillsC;
import com.rogueworld.entities.components.SkillsC.Skill;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;
import com.rogueworld.world.map.Map;
import com.rogueworld.world.tile.Tile;

public class CraftRecipe {
	
	public final String name;
	public final String materials;
	public final String skills;
	public final String workStations;
	public final String tools;
	public final int craftTime;
	public final int difficulty;
	
	public CraftRecipe(String name, String skills, String materials, String workStations, String tools, int craftTime, int difficulty) {
		this.name = name;
		this.materials = materials;
		this.skills = skills;
		this.workStations = workStations;
		this.tools = tools;
		this.craftTime = craftTime;
		this.difficulty = difficulty;
	}

	public boolean isCraftable() {
		return workStationsNearby() && hasSkills() && hasNecessaryItems();
	}
	
	//TODO debe buscar en un radio cercano
	private boolean hasNecessaryItems() {
		ContainerC inv = Main.player.get(ContainerC.class);
		String[] neededMats = materials.split("&");
		for(int i = 0; i < neededMats.length; i++) {
			String[] interchangeableMats = neededMats[i].split("\\|");
			for(int j = 0; j < interchangeableMats.length; j++) {
				String[] mat = interchangeableMats[j].split("_");
				if(!inv.contains(mat[1], Integer.parseInt(mat[0]))) {
					return false;
				}
			}
		}
		
		if(!tools.equals("")) {
			Set<String> neededTools = new HashSet<>(Arrays.asList(tools.split("-")));
			for(String tool : neededTools) {
				if(!inv.contains(tool)) return false;
			}
		}
		
		return true;
	}
	
	private boolean hasSkills() {
		SkillsC playerSkills = Main.player.get(SkillsC.class);
		String[] neededSkills = skills.split(" ");
		for(int i = 0; i < neededSkills.length; i++) {
			String[] skill = neededSkills[i].split(":");
			if(!skill[0].equals("") && (playerSkills.get(Skill.valueOf(skill[0])) < Integer.parseInt(skill[1]))) {
				return false;
			}
		}
		return true;
	}
	
	private boolean workStationsNearby() {
		if(workStations.equals("")) return true;
		Set<String> neededStations = new HashSet<>(Arrays.asList(workStations.split("-")));
		
		Set<Tile> area = Map.getCircundatingAreaAsSet(6, Main.player.get(PositionC.class).getTile(), false);
		
		for(Tile t : area) {
			Entity feature = t.get(Type.FEATURE);
			if(feature != null) {
				if(neededStations.contains(feature.name)) {
					neededStations.remove(feature.name);
				}
				if(neededStations.isEmpty()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void consumeMaterials() {
		ContainerC inv = Main.player.get(ContainerC.class);
		String[] neededMats = materials.split("&");
		for(int i = 0; i < neededMats.length; i++) {
			String[] interchangeableMats = neededMats[i].split("\\|");
			for(int j = 0; j < interchangeableMats.length; j++) {
				String[] mat = interchangeableMats[j].split(":");
				if(inv.contains(mat[0], Integer.parseInt(mat[1]))) {
					inv.remove(mat[0], Integer.parseInt(mat[1]));
					break;
				}
			}
		}
	}

}
