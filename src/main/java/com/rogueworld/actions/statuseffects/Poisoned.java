package com.rogueworld.actions.statuseffects;

import com.rogueworld.actions.effects.Effects;
import com.rogueworld.gui.gamescreen.Console;
import com.rogueworld.entities.components.HealthC;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;

//TODO esta status es solo para probar, hay que borrarlo y hacer otros
public class Poisoned extends Status{
	
	public Poisoned(int duration) {
		trigger = StTrigger.START_TURN;
		name = "poisoned";
		this.duration = duration;
	}

	@Override
	public void makeEffect(Entity affected) {
		HealthC hp = affected.get(HealthC.class);
		float damage = hp.getCurHP() / 5;
		
		Effects.receiveDamage(affected, damage);
		
		if(affected.type == Type.PLAYER) {
			Console.addMessage("You suffer from poison\n");
		}
	}

}
