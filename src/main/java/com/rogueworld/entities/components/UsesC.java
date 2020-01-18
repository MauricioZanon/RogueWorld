package com.rogueworld.entities.components;

import java.util.ArrayList;
import java.util.List;

public class UsesC extends Component{
	
	public List<UseType> uses = new ArrayList<>();
	public UseType useOnBump = null;
	public UseType quickUse = null; // uso con la barra espaciadora
	
	public UsesC() {
		isShared = true;
	}

	@Override
	public UsesC clone() {
		UsesC c = new UsesC();
		c.uses.addAll(uses);
		return c;
	}

	@Override
	public void serialize(StringBuilder sb) {}
	
	@Override
	public void deserialize(String info) {}
	
	@Override
	public boolean equals(Component comp) {
		return uses.equals(((UsesC) comp).uses);
	}

	public enum UseType{
		ACTIVATE,
		CHOP,
		CUT_BRANCH,
		CLOSE,
		FISH,
		GET_BARK,
		HARVEST, // necesita ContainerC
		LOCK, // necesita LockC
		MINE,
		OPEN,
		PEEK,
		REFILL_CONTAINER, // Necesita LiquidContainerC
		SEE_CONTENT,
		TRUNK, // necesita LockC
		UNLOCK; // necesita LockC
	}
}
