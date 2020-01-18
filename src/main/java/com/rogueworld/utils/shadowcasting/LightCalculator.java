// package shadowCasting;
//package FOV;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import main.Att;
//import main.Entity;
//import map.Map;
//import tile.Tile;
//
//public class LightCalculator {
//
//	private static Set<Tile> evaluatedTiles;
//	
//	/**
//	 * Ilumina el area al rededor de una entidad con el flag LIGHT_SOURCE
//	 * @param origin el origen de la luz
//	 * @param add true para cuando se agrega una fuente de luz, false para cuando se remueve
//	 */
//	public static void calculate(Tile origin, Entity lightSource, boolean add) {
//		evaluatedTiles = new HashSet<>();
//		float lightIntensity = lightSource.get(Att.LIGHT_INTENSITY);
//		int lightRange = (int) (lightIntensity / 0.15f);
//		
//		for (int octant = 0; octant < 8; octant++) {
//			compute(octant, origin, lightRange, 1, new Slope(1, 1), new Slope(0, 1), lightIntensity, add);
//		}
//		
//		origin.changeLightLevel(add ? lightIntensity : -lightIntensity);
//	}
//	
//	private static void compute(int octant, Tile origin, int lightRange, int x, Slope top, Slope bottom, float lightIntensity, boolean add) {
//		int addMod = add ? 1 : -1;
//		for (; x <= lightRange; x++) {
//			int topY = top.X == 1 ? x : ((x * 2 + 1) * top.Y + top.X - 1) / (top.X * 2);
//			int bottomY = bottom.Y == 0 ? 0 : ((x * 2 - 1) * bottom.Y + bottom.X) / (bottom.X * 2);
//
//			int wasOpaque = -1; // 0:false, 1:true, -1:not applicable
//			for (int y = topY; y >= bottomY; y--) {
//				int tx = origin.COORD[0], ty = origin.COORD[1];
//				switch (octant) {
//				case 0:
//					tx += x;
//					ty -= y;
//					break;
//				case 1:
//					tx += y;
//					ty -= x;
//					break;
//				case 2:
//					tx -= y;
//					ty -= x;
//					break;
//				case 3:
//					tx -= x;
//					ty -= y;
//					break;
//				case 4:
//					tx -= x;
//					ty += y;
//					break;
//				case 5:
//					tx -= y;
//					ty += x;
//					break;
//				case 6:
//					tx += y;
//					ty += x;
//					break;
//				case 7:
//					tx += x;
//					ty += y;
//					break;
//				}
//
//				Tile evaluatedTile = Map.getTile(tx, ty, origin.COORD[2]);
//				if(!evaluatedTiles.contains(evaluatedTile)){
//					double distance = Map.getDistance(origin.getPos(), evaluatedTile.getPos());
//					float intensity = (float) ((lightIntensity - (0.15f * distance))*addMod);
//					if((add && intensity > 0) || (!add && intensity < 0))
//						evaluatedTile.changeLightLevel(intensity);
//				}
//				evaluatedTiles.add(evaluatedTile);
//				
//
//				if (x != lightRange) {
//					boolean isOpaque = !evaluatedTile.isTranslucent();
//					if (isOpaque) {
//						if (wasOpaque == 0) { 
//							// if we found a transition from clear to opaque, this sector is done in this column, so
//							// adjust the bottom vector upwards and continue processing it in the next column.
//							Slope newBottom = new Slope(y * 2 + 1, x * 2 - 1);
//							boolean inRange = Map.isInRange(origin.getPos(), evaluatedTile.getPos(), lightRange);
//							if (!inRange || y == bottomY) {
//								bottom = newBottom;
//								break;
//							}
//							else {
//								compute(octant, origin, lightRange, x + 1, top, newBottom, lightIntensity, add);
//							}
//						}
//						wasOpaque = 1;
//					} else {
//						if (wasOpaque > 0)
//							top = new Slope(y * 2 + 1, x * 2 + 1);
//						wasOpaque = 0;
//					}
//				}
//			}
//
//			if (wasOpaque != 0) {
//				break; // if the column ended in a clear tile, continue processing the current sector
//			}
//		}
//	}
//
//}
