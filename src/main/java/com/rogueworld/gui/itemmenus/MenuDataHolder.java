package com.rogueworld.gui.itemmenus;

import java.util.function.Consumer;
import java.util.function.Predicate;

import com.rogueworld.entities.components.ContainerC;
import com.rogueworld.entities.main.Entity;

public class MenuDataHolder {
	
	protected static boolean closeOnAction;
	protected static ContainerC[] containers;
	protected static Consumer<Entity> action;
	protected static Predicate<Entity> filter;
	protected static String title;
	
	private MenuDataHolder() {}
	
	protected static void reset() {
		closeOnAction = false;
		containers = null;
		action = a -> {};
		filter = f -> true;
		title = "";
	}

}
