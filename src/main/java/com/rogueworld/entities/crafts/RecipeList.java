package com.rogueworld.entities.crafts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.rogueworld.entities.factories.EntityFactory;
import com.rogueworld.entities.main.Type;

public class RecipeList {

	public static HashMap<String, List<CraftRecipe>> craftRecipes = new HashMap<>();
	public static HashMap<String, List<BuildRecipe>> buildRecipes = new HashMap<>();

	private static Connection connect() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:resources/Data/Entities.db");
		} catch (SQLException e) {
			System.out.println("load connection failed " + e.getMessage());
		}
		return conn;
	}

	private static void close(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {
		}
	}

	private static ResultSet executeQuery(String query, Connection con) {
		try {
			return con.prepareStatement(query).executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void loadRecipes() {
		Connection con = connect();
		ResultSet RS = executeQuery("SELECT * FROM Recipes;", con);
		try {
			while (RS.next()) {
				String itemType = RS.getString(3);
				String recipeCategory = getRecipeCategory(itemType);

				if (!craftRecipes.containsKey(recipeCategory)) {
					craftRecipes.put(recipeCategory, new ArrayList<>());
				}

				craftRecipes.get(recipeCategory).add(new CraftRecipe(RS.getString(2), RS.getString(4), RS.getString(5),
						RS.getString(6), RS.getString(7), RS.getInt(8), RS.getInt(9)));
			}

			RS.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		RS = executeQuery("SELECT * FROM BuildRecipes;", con);
		try {
			while (RS.next()) {
				String buildType = getBuildRecipeCategory(EntityFactory.create(RS.getString(2)).type);

				if (!buildRecipes.containsKey(buildType)) {
					buildRecipes.put(buildType, new ArrayList<>());
				}
				buildRecipes.get(buildType).add(new BuildRecipe(RS.getString(2), RS.getString(3), RS.getString(5),
						RS.getString(4), RS.getString(6), RS.getString(7), RS.getInt(8)));
			}

			RS.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		close(con);
	}

	private static String getBuildRecipeCategory(Type type) {
		switch (type) {
		case WALL:
			return "Walls";
		case FLOOR:
			return "Floors";
		case FEATURE:
			return "Features";
		case TRAP:
			return "Traps";
		default:
			return "Walls";
		}

	}

	// TODO aca se tiene que usar un enum de categorias de crafteo
	private static String getRecipeCategory(String itemType) {
		Type type = Type.valueOf(itemType);

		if (type.is(Type.WEAPON)) {
			return "Weapon";
		}
		if (type.is(Type.ARMOR)) {
			return "Armor";
		}
		if (type.is(Type.CLOTHES)) {
			return "Clothes";
		}
		// if(type.is(Type.FOOD) || type.is(Type.DRINK)) {
		// return "Food";
		// }
		if (type.is(Type.JEWELRY)) {
			return "Jewelry";
		}
		return "Misc";
	}

}
