package com.rogueworld.entities.components;

import java.util.EnumSet;

import com.rogueworld.entities.main.Flag;

public class DisassembleC extends Component{
	
	public EnumSet<Flag> requirements = EnumSet.noneOf(Flag.class);
	public String items;
	
	public DisassembleC() {
		isShared = true;
	}

	@Override
	public Component clone() {
		DisassembleC c = new DisassembleC();
		c.requirements.addAll(requirements);
		c.items = items;
		return c;
	}

	@Override
	public void serialize(StringBuilder sb) {}
	
	@Override
	public void deserialize(String info) {}

	@Override
	public boolean equals(Component comp) {
		DisassembleC c = (DisassembleC) comp;
		if(!items.equals(c.items)) return false;
		return requirements.equals(c.requirements);
	}

}
