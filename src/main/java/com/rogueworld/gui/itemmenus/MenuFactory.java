package com.rogueworld.gui.itemmenus;

import com.rogueworld.actions.actions.Drop;
import com.rogueworld.actions.actions.Eat;
import com.rogueworld.actions.actions.Quaff;
import com.rogueworld.actions.actions.Throw;
import com.rogueworld.actions.actions.Use;
import com.rogueworld.actions.actions.Wear;
import com.rogueworld.actions.actions.Wield;
import com.rogueworld.gui.gamescreen.InputConfig;
import com.rogueworld.gui.system.RenderSystem;
import com.rogueworld.application.Main;
import com.rogueworld.entities.components.ContainerC;
import com.rogueworld.entities.main.Flag;
import com.rogueworld.entities.main.Type;

/**
 * Esta clase est� solo para pasarle la informacion necesaria a MenuDataHolder y cargar el FXML
 */
public class MenuFactory {
	
	private MenuFactory() {}

	public static void openInventory() {
		MenuDataHolder.reset();
		MenuDataHolder.containers = new ContainerC[1];
		MenuDataHolder.containers[0] = Main.player.get(ContainerC.class);
		MenuDataHolder.closeOnAction = true;
		RenderSystem.getInstance().changeScene("Inventory.fxml", true);
	}
	
	public static void openDropMenu() {
		MenuDataHolder.reset();
		MenuDataHolder.title = "Drop";
		MenuDataHolder.action = i -> Drop.execute(Main.player, i);
		MenuDataHolder.containers = new ContainerC[1];
		MenuDataHolder.containers[0] = Main.player.get(ContainerC.class);
		MenuDataHolder.closeOnAction = false;
		RenderSystem.getInstance().changeScene("ItemSelectionMenu.fxml", true);
	}
	
	public static void openQuaffMenu() {
		MenuDataHolder.reset();
		MenuDataHolder.title = "Quaff";
		MenuDataHolder.action = p -> Quaff.execute(Main.player, p);
		MenuDataHolder.filter = i -> i.type.is(Type.POTION);
		MenuDataHolder.containers = new ContainerC[1];
		MenuDataHolder.containers[0] = Main.player.get(ContainerC.class);
		MenuDataHolder.closeOnAction = true;
		RenderSystem.getInstance().changeScene("ItemSelectionMenu.fxml", true);
	}
	
	public static void openThrowMenu() {
		MenuDataHolder.reset();
		MenuDataHolder.title = "Throw";
		MenuDataHolder.action = i -> {
			Throw.thrownItem = i;
			InputConfig.setThrowInput();
		};
		MenuDataHolder.containers = new ContainerC[1];
		MenuDataHolder.containers[0] = Main.player.get(ContainerC.class);
		MenuDataHolder.closeOnAction = true;
		RenderSystem.getInstance().changeScene("ItemSelectionMenu.fxml", true);
	}

	public static void openWearMenu() {
		MenuDataHolder.reset();
		MenuDataHolder.title = "Wear";
		MenuDataHolder.action = a -> Wear.execute(Main.player, a); // TODO si esta equipado sacarlo
		MenuDataHolder.filter = i -> i.type.is(Type.ARMOR);
		MenuDataHolder.containers = new ContainerC[1];
		MenuDataHolder.containers[0] = Main.player.get(ContainerC.class);
		MenuDataHolder.closeOnAction = true;
		RenderSystem.getInstance().changeScene("ItemSelectionMenu.fxml", true);
	}
	
	public static void openWieldMenu() {
		MenuDataHolder.reset();
		MenuDataHolder.title = "Wield";
		MenuDataHolder.action = w -> Wield.execute(Main.player, w); // TODO si esta equipado sacarlo
		MenuDataHolder.filter = i -> i.type.is(Type.WEAPON);
		MenuDataHolder.containers = new ContainerC[1];
		MenuDataHolder.containers[0] = Main.player.get(ContainerC.class);
		MenuDataHolder.closeOnAction = true;
		RenderSystem.getInstance().changeScene("ItemSelectionMenu.fxml", true);
	}

	public static void openGetItemsMenu() {
		MenuDataHolder.reset();
		MenuDataHolder.title = "Get items";
		MenuDataHolder.action = i -> {
			Main.player.get(ContainerC.class).add(i);
			Use.usedEntity.get(ContainerC.class).remove(i.name);
		};
		MenuDataHolder.containers = new ContainerC[2];
		MenuDataHolder.containers[0] = Use.usedEntity.get(ContainerC.class);
		MenuDataHolder.closeOnAction = false;
		RenderSystem.getInstance().changeScene("ItemSelectionMenu.fxml", true);
	}
	
	public static void openEatMenu() {
		MenuDataHolder.reset();
		MenuDataHolder.title = "Eat";
		MenuDataHolder.action = f -> Eat.execute(f);
		MenuDataHolder.filter = i -> i.is(Flag.EDIBLE);
		MenuDataHolder.containers = new ContainerC[1];
		MenuDataHolder.containers[0] = Main.player.get(ContainerC.class);
		MenuDataHolder.closeOnAction = true;
		RenderSystem.getInstance().changeScene("ItemSelectionMenu.fxml", true);
	}
	

}
