package com.rogueworld.entities.components;

public class BreakC extends Component{
	
	public String items;
	
	public BreakC() {
		isShared = true;
	}

	@Override
	public Component clone() {
		BreakC c = new BreakC();
		c.items = items;
		return c;
	}

	@Override
	public void serialize(StringBuilder sb) {}
	
	@Override
	public void deserialize(String info) {}

	@Override
	public boolean equals(Component comp) {
		return items.equals(((BreakC) comp).items);
	}

}
