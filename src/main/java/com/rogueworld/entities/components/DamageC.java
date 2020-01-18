package com.rogueworld.entities.components;

public class DamageC extends Component {
	
	public DamageType type;
	public int amount = 10;
	
	public DamageC() {
		isShared = false;
	}

	@Override
	public Component clone() {
		DamageC c = new DamageC();
		c.type = type;
		c.amount = amount;
		return c;
	}

	@Override
	public void serialize(StringBuilder sb) {}
	
	@Override
	public void deserialize(String info) {}

	@Override
	public boolean equals(Component comp) {
		DamageC c = (DamageC) comp;
		if(c.type != type) return false;
		if(c.amount != amount) return false;
		return true;
	}
	
	public enum DamageType {
		PIERCING,
		BLUNT,
		CUTTING,
		
		FIRE,
		COLD,
		LIGHTNING,
		
	}


}
