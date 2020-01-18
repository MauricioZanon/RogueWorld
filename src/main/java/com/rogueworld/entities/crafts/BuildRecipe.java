package com.rogueworld.entities.crafts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.rogueworld.application.Main;
import com.rogueworld.entities.components.ContainerC;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.components.SkillsC;
import com.rogueworld.entities.components.SkillsC.Skill;
import com.rogueworld.entities.components.ToolC;
import com.rogueworld.entities.components.ToolC.Tool;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;
import com.rogueworld.world.map.Map;
import com.rogueworld.world.tile.Tile;

public class BuildRecipe {
	
	public final String name;
	public final String materials;
	public final String skills;
	public final String tools;
	public final String[] onTerrain;
	public final String[] nextToTerrain;
	public final int buildTime;
	
	public BuildRecipe(String name, String materials, String skills, String tools, String onTerrain, String nextToTerrain, int buildTime) {
		this.name = name;
		this.materials = materials;
		this.skills = skills;
		this.tools = tools;
		this.onTerrain = onTerrain.equals("") ? new String[0] : onTerrain.split(" ");
		this.nextToTerrain = nextToTerrain.equals("") ? new String[0] : nextToTerrain.split(" ");
		this.buildTime = buildTime;
	}
	
	public boolean isBuildable() {
		return validTerrainNearby() && hasSkills() && hasMaterials();
	}
	
	//TODO debe buscar en un radio cercano
	private boolean hasMaterials() {
		ContainerC inv = Main.player.get(ContainerC.class);
		String[] neededMats = materials.split("&");
		for(int i = 0; i < neededMats.length; i++) {
			String[] mat = neededMats[i].split("_");
			if(!inv.contains(mat[1], Integer.parseInt(mat[0]))) {
				return false;
			}
		}

		if(!tools.contentEquals("")) {
			Set<String> neededTools = new HashSet<>(Arrays.asList(tools.split(" ")));
			for(String tool : neededTools) {
				String[] toolInfo = tool.split("-");
				Tool prop = Tool.valueOf(toolInfo[0]);
				boolean found = false;
				for(Entity item : inv.getAll()) {
					ToolC comp = item.get(ToolC.class);
					if(comp != null && comp.properties.containsKey(prop) && comp.properties.get(prop) >= Integer.parseInt(toolInfo[1])) {
						found = true;
						break;
					}
				}
				if(!found) return false;
			}
		}
		
		return true;
	}
	
	private boolean hasSkills() {
		SkillsC playerSkills = Main.player.get(SkillsC.class);
		String[] neededSkills = skills.split(" ");
		for(int i = 0; i < neededSkills.length; i++) {
			String[] skill = neededSkills[i].split("-");
			if((playerSkills.get(Skill.valueOf(skill[0])) < Integer.parseInt(skill[1]))) {
				return false;
			}
		}
		return true;
	}
	
	private boolean validTerrainNearby() {
		List<Type> on = new ArrayList<>();
		for(int i = 0; i < onTerrain.length; i++) {
			on.add(Type.valueOf(onTerrain[i]));
		}
		List<Type> nextTo = new ArrayList<>();
		for(int i = 0; i < nextToTerrain.length; i++) {
			nextTo.add(Type.valueOf(nextToTerrain[i]));
		}
		
		for(Tile t : Map.getAdjacentTiles(Main.player.get(PositionC.class).getTile())) {
			if(on.contains(t.get(Type.TERRAIN).type)) {
				if(nextTo.size() == 0) {
					return true;
				}
				for(Tile at : Map.getAdjacentTiles(t)) {
					if(nextTo.contains(at.get(Type.TERRAIN).type)) {
						return true;
					}
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
				String[] mat = interchangeableMats[j].split("_");
				if(inv.contains(mat[1], Integer.parseInt(mat[0]))) {
					inv.remove(mat[1], Integer.parseInt(mat[0]));
					break;
				}
			}
		}
	}
	
	public boolean canBeBuiltIn(Tile t) {
		List<Type> on = new ArrayList<>();
		for(int i = 0; i < onTerrain.length; i++) {
			on.add(Type.valueOf(onTerrain[i]));
		}
		List<Type> nextTo = new ArrayList<>();
		for(int i = 0; i < nextToTerrain.length; i++) {
			nextTo.add(Type.valueOf(nextToTerrain[i]));
		}
		
		if(on.contains(t.get(Type.TERRAIN).type)) {
			if(nextTo.size() == 0) {
				return true;
			}
			for(Tile at : Map.getAdjacentTiles(t)) {
				if(nextTo.contains(at.get(Type.TERRAIN).type)) {
					return true;
				}
			}
		}
		return false;
	}
	
	

}
