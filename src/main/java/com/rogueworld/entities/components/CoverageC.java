package com.rogueworld.entities.components;

import java.util.EnumSet;

import com.rogueworld.entities.components.BodyC.BodyPart;

/** Componente usado en las entidades equipables, guarda información sobre que parte del cuerpo cubre u ocupa */
public class CoverageC extends Component{
	
	/** A una parte del cuerpo cubierta todavía se le puede ocupar algo */
	public EnumSet<BodyPart> covers = EnumSet.noneOf(BodyPart.class);
	
	public CoverageC() {
		isShared = true;
	}

	@Override
	public Component clone() {
		CoverageC comp = new CoverageC();
		comp.covers.addAll(covers);
		return comp;
	}

	@Override
	public void serialize(StringBuilder sb) {}
	
	@Override
	public void deserialize(String info) {}

	@Override
	public boolean equals(Component comp) {
		return covers.equals(((CoverageC) comp).covers);
	}

}
