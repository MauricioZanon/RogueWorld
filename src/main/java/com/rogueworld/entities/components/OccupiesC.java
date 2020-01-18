package com.rogueworld.entities.components;

import java.util.EnumSet;

import com.rogueworld.entities.components.BodyC.BodyPart;

public class OccupiesC extends Component {
	
	/** Si una parte del cuerpo está ocupada significa que no se le puede equipar nada que intente ocuparla, un breastplate ocuparía 
	 *  el torso, evitando que se equipe otro breastplate, pero cosas como capas o tapados no, por lo que se pueden equipar varias a la vez 
	 *  Este componente también puede usarse para inabilitar partes del cuerpo*/
	public EnumSet<BodyPart> occupies = EnumSet.noneOf(BodyPart.class);
	
	public OccupiesC() {
		isShared = true;
	}

	@Override
	public OccupiesC clone() {
		OccupiesC c = new OccupiesC();
		c.occupies.addAll(occupies);
		return c;
	}

	@Override
	public void serialize(StringBuilder sb) {}
	
	@Override
	public void deserialize(String info) {}

	@Override
	public boolean equals(Component comp) {
		return occupies.equals(((OccupiesC) comp).occupies);
	}

}
