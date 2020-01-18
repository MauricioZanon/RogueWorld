package com.rogueworld.utils.shadowcasting;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.rogueworld.entities.components.LightSourceC;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.components.VisionC;
import com.rogueworld.entities.main.Att;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;
import com.rogueworld.utils.observerpattern.Notification;
import com.rogueworld.world.map.Map;
import com.rogueworld.world.tile.Tile;
import com.rogueworld.world.world.Direction;

public class ShadowCasting {
	
	/** Guarda la distancia desde un tile origen hasta los tiles cercanos para acelerar el shadow casting  */
	private static float[][] distancesChart = createDistanceChart();
	private static HashMap<Direction, int[]> octantsByDirection = createOctantsByDirections();
	
	private ShadowCasting() {}

	/*
	 * OCTANTES:	RECORRIDO (desde el centro hacia el extremo)
	 *  \2 1/		  \> </
	 *  3\ /0		  V\ /V
	 * 	4/ \7		  ^/ \^
	 *  /5 6\		  /> <\
	 */
	private static HashMap<Direction, int[]> createOctantsByDirections() {
		HashMap<Direction, int[]> map = new HashMap<>();
		map.put(Direction.N, new int[] {7, 0, 1, 2, 3, 4});
		map.put(Direction.NW, new int[] {0, 1, 2, 3, 4, 5});
		map.put(Direction.W, new int[] {1, 2, 3, 4, 5, 6});
		map.put(Direction.SW, new int[] {2, 3, 4, 5, 6, 7});
		map.put(Direction.S, new int[] {3, 4, 5, 6, 7, 0});
		map.put(Direction.SE, new int[] {4, 5, 6, 7, 0, 1});
		map.put(Direction.E, new int[] {5, 6, 7, 0, 1, 2});
		map.put(Direction.NE, new int[] {6, 7, 0, 1, 2, 3});
		
		return map;
	}

	//TODO hacer que los NPC solo calculen la vision de los tiles con alguna entidad importante
	public static void calculateFOV(Entity entity) {
		VisionC vc = entity.get(VisionC.class);
		
		Predicate<Tile> isTranslucent = t -> t.isTranslucent();
		Consumer<Tile> addToVisionMap = t -> {
			if(t.getLightLevel() <= 0) {
				return;
			}
			vc.visionMap.add(t);
		};
		if(entity.type == Type.PLAYER) {
			vc.visionMap.forEach(t -> t.setViewed(true));
			addToVisionMap = addToVisionMap.andThen(t -> {
				if(t.has(Type.NPC)) {
					vc.enemyTiles.add(t);
				}
			});
		}else {
			addToVisionMap = addToVisionMap.andThen(t -> {
				if(t.has(Type.ACTOR) && t.get(Type.ACTOR).type == Type.PLAYER) {
					vc.enemyTiles.add(t);
				}
			});
		}
		
		vc.clear();
		Tile origin = entity.get(PositionC.class).getTile();
		addToVisionMap.accept(origin);
		
		int[] octants = octantsByDirection.get(vc.faceDir);
		for(int i = 0; i < octants.length; i++) {
			compute(octants[i], origin, vc.sightRange, 1, new Slope(1, 1), new Slope(1, 0), isTranslucent, addToVisionMap);
		}
	}
	
	/**
	 * Ilumina el area al rededor de una entidad
	 * @param origin el origen de la luz
	 * @param addingLight true para cuando se agrega una fuente de luz, false para cuando se remueve
	 */
	public static void calculateIllumination(Entity entity, boolean addingLight) {
		Tile origin = entity.get(PositionC.class).getTile();
		if(origin == null) return;
//		if(WorldBuilder.isBuilding) return;
		float lightIntensity = entity.get(Att.LIGHT_INTENSITY);
		int lightRange = (int) (lightIntensity / 0.15f);
		Set<Tile> illuminatedTiles = entity.get(LightSourceC.class).illuminatedTiles;
		
		Predicate<Tile> isTranslucent = t -> t.isTranslucent();
		
		
		if(addingLight) {
			Consumer<Tile> illuminate = t -> {
				if(!illuminatedTiles.contains(t)){
					double distance = getDistance(origin, t);
					float intensity = (float) ((lightIntensity - (0.15f * distance)));
					if(intensity > 0) {
						t.changeLightLevel(intensity);
						t.addObserver(Notification.RECALCULATE_LIGHT, entity);
					}
					illuminatedTiles.add(t);
				}
			};
			illuminate.accept(origin);
			
			for (int octant = 0; octant < 8; octant++) {
				compute(octant, origin, lightRange, 1, new Slope(1, 1), new Slope(1, 0), isTranslucent, illuminate);
			}
		}
		else {
			illuminatedTiles.forEach(t -> {
				if(illuminatedTiles.contains(t)){
					double distance = getDistance(origin, t);
					float intensity = (float) -((lightIntensity - (0.15f * distance)));
					if(intensity < 0) {
						t.changeLightLevel(intensity);
						t.removeObserver(Notification.RECALCULATE_LIGHT, entity);
					}
				}
			});
			illuminatedTiles.clear();
		}
	}
	
	private static void compute(int octant, Tile origin, int range, int x, Slope top, Slope bottom, Predicate<Tile> condition, Consumer<Tile> action) {
		for (; x <= range; x++) {
			int topY = top.X == 1 ? x : ((x * 2 + 1) * top.Y + top.X - 1) / (top.X * 2);
			int bottomY = bottom.Y == 0 ? 0 : ((x * 2 - 1) * bottom.Y + bottom.X) / (bottom.X * 2);

			int brokeCondition = -1; // 0:false, 1:true, -1:not applicable
			for (int y = topY; y >= bottomY; y--) {
				int tx = origin.pos.coord[0];
				int ty = origin.pos.coord[1];
				
				switch (octant) {
				case 0:
					tx += x;
					ty -= y;
					break;
				case 1:
					tx += y;
					ty -= x;
					break;
				case 2:
					tx -= y;
					ty -= x;
					break;
				case 3:
					tx -= x;
					ty -= y;
					break;
				case 4:
					tx -= x;
					ty += y;
					break;
				case 5:
					tx -= y;
					ty += x;
					break;
				case 6:
					tx += y;
					ty += x;
					break;
				case 7:
					tx += x;
					ty += y;
					break;
				}
				
				Tile evaluatedTile = Map.getTile(tx, ty, origin.pos.coord[2]);
				action.accept(evaluatedTile);

				if (x <= range) {
					if (!condition.test(evaluatedTile)) {
						if (brokeCondition == 0) { 
							// if we found a transition from clear to opaque, this sector is done in this column, so
							// adjust the bottom vector upwards and continue processing it in the next column.
							Slope newBottom = new Slope(x*2 + 1, y*2 + 1);
							if (y == bottomY) {
								bottom = newBottom;
								break;
							}
							else {
								compute(octant, origin, range, x+1, top, newBottom, condition, action);
							}
						}
						brokeCondition = 1;
					} else {
						if (brokeCondition == 1)
							top = new Slope(x*2 + 1, y*2 + 1);
						brokeCondition = 0;
					}
				}
			}

			if (brokeCondition != 0) {
				break; // if the column ended in a clear tile, continue processing the current sector
			}
		}
	}
	
	
	private static float[][] createDistanceChart() {
		float[][] chart = new float[40][40];
		
		for(int x = 0; x < chart.length; x++) {
			for(int y = 0; y < chart[0].length; y++) {
				chart[x][y] = (float) Math.sqrt(x*x + y*y);
			}
		}
		
		return chart;
	}
	
	private static float getDistance(Tile t1, Tile t2) {
		return distancesChart[Math.abs(t1.pos.coord[0] - t2.pos.coord[0])][Math.abs(t1.pos.coord[1] - t2.pos.coord[1])];
	}

}
