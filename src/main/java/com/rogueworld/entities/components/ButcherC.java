package com.rogueworld.entities.components;

import java.util.EnumMap;
import java.util.EnumSet;

import com.rogueworld.entities.components.SkillsC.Skill;
import com.rogueworld.entities.components.ToolC.Tool;

public class ButcherC extends Component{
	
	public String items;
	public EnumMap<Skill, Integer> neededSkills = new EnumMap<>(Skill.class);
	public EnumSet<Tool> neededTools = EnumSet.noneOf(Tool.class);
	
	public ButcherC() {
		isShared = true;
	}
	
	@Override
	public ButcherC clone() {
		ButcherC c = new ButcherC();
		c.items = items;
		c.neededSkills.entrySet().addAll(neededSkills.entrySet());
		c.neededTools.addAll(neededTools);
		return c;
	}

	@Override
	public void serialize(StringBuilder sb) {}
	
	@Override
	public void deserialize(String info) {}

	@Override
	public boolean equals(Component comp) {
		return true;
	}

}
