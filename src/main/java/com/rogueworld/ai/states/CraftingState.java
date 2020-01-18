package com.rogueworld.ai.states;

import com.rogueworld.ai.eventsystem.EventSystem;
import com.rogueworld.actions.actions.ActionType;
import com.rogueworld.actions.actions.EndTurn;
import com.rogueworld.gui.gamescreen.Console;
import com.rogueworld.application.Main;
import com.rogueworld.entities.components.AIC;
import com.rogueworld.entities.components.ContainerC;
import com.rogueworld.entities.crafts.CraftRecipe;
import com.rogueworld.entities.factories.EntityFactory;

public class CraftingState extends State{
	
	private int duration;
	private CraftRecipe recipe;
	
	public CraftingState(CraftRecipe recipe) {
		actor = Main.player;
		duration = recipe.craftTime;
		this.recipe = recipe;
	}
	
	/* TODO: las cosas que se craftean tiene que tardar el tiempo que dice en craftTime, 6000 serian 6000 segundos, pero tarda mucho
	 * porque cada segundo que avanza se tiene que renderizar la pantalla de nuevo y calcular el FOV, capaz fraccionando el tiempo
	 * e implementando otro metodo EndTurn que reciva un float con el tiempo hasta el siguiente turno se haga mas facil
	 */
	public void update() {
		super.update();
		EventSystem.setPlayerTurn(true);
		duration -= 50;
		EndTurn.execute(Main.player, ActionType.CRAFT);
		
		if(duration <= 0) {
			Main.player.get(ContainerC.class).add(EntityFactory.create(recipe.name));
			recipe.consumeMaterials();
			Console.addMessage("You crafted a " + recipe.name);
			Main.player.get(AIC.class).setState(StateType.IDLE);
		}
	}

	@Override
	public void enterState() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitState() {
		// TODO Auto-generated method stub
		
	}

}
