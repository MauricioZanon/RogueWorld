package com.rogueworld.entities.components;

import java.util.HashMap;
import java.util.Map;

import com.rogueworld.actions.spells.Spell;

public class AbilitiesC extends Component{
	
	private Map<String, Spell> spells = new HashMap<>();
	
	public AbilitiesC() {
		isShared = false;
	}

	public void addSpell(Spell spell) {
		if(spell != null) {
			spells.put(spell.getName(), spell);
		}
	}
	
	public void removeSpell(String spellName) {
		spells.remove(spellName);
	}
	
	public Spell getSpell(String spellName) {
		return spells.get(spellName);
	}
	
	public Map<String, Spell> getSpells(){
		return spells;
	}

	@Override
	public AbilitiesC clone() {
		AbilitiesC ac = new AbilitiesC();
		spells.values().forEach(s -> ac.addSpell(s));
		return ac;
	}

	@Override
	public void serialize(StringBuilder sb) {
		sb.append("ABI:");
		
		for(Spell spell : spells.values()) {
			sb.append(spell.getName() + "&");
		}
	}
	
	@Override
	public void deserialize(String info) {
		String[] spellNames = info.split("&");
		for(int i = 0; i < spellNames.length; i++) {
			addSpell(Spell.get(spellNames[i]));
		}
	}
	
	@Override
	public boolean equals(Component comp) {
		AbilitiesC c = (AbilitiesC) comp;
		return spells.equals(c.spells);
	}

}
