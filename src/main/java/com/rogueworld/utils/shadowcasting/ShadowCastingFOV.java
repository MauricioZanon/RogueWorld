// package shadowCasting;
//package FOV;
//
//import components.PositionComponent;
//import components.VisionComponent;
//import main.Entity;
//import main.Type;
//import map.Map;
//import player.PlayerInfo;
//import tile.Tile;
//
///**
// * http://www.adammil.net/blog/v125_Roguelike_Vision_Algorithms.html#shadowcode
// */
//public class ShadowCastingFOV{
//	
//	private static ShadowCastingFOV instance = new ShadowCastingFOV();
//	
//	private ShadowCastingFOV() {}
//	
//	public static ShadowCastingFOV getInstance() {
//		return instance;
//	}
//
//	public void calculateVision(Entity actor) {
////		long time = System.currentTimeMillis();
//		VisionComponent vc = actor.get(VisionComponent.class);
//		resetVisionComponent(vc);
//		
//		//FIXME los tiles en los bordes de los octantes se calculan dos veces
//		Tile origin = actor.get(PositionComponent.class).getTile();
//		for (int octant = 0; octant < 8; octant++) {
//			compute(octant, origin, vc.sightRange, 1, new Slope(1, 1), new Slope(0, 1), vc, actor.TYPE == Type.PLAYER);
//		}
//		
//		vc.visionMap.add(origin);
//		
//		if(actor.TYPE == Type.PLAYER) {
//			PlayerInfo.viewedTiles.addAll(vc.visionMap);
//		}
////		System.out.println(System.currentTimeMillis() - time);
//	}
//
//	private void compute(int octant, Tile origin, int rangeLimit, int x, Slope top, Slope bottom, VisionComponent vc, boolean isPlayer) {
//		for (; x <= rangeLimit; x++) {
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
//				if(evaluatedTile.getLightLevel() > 0) {
//					if(enemyFound(isPlayer, evaluatedTile)) {
//						vc.visionMap.add(evaluatedTile);
//						vc.enemyTiles.add(evaluatedTile);
//					}else if(isPlayer) {
//						vc.visionMap.add(evaluatedTile);
//					}
//				}
//					
//				if (x != rangeLimit) {
//					boolean isOpaque = !evaluatedTile.isTranslucent();
//					if (isOpaque) {
//						if (wasOpaque == 0) { 
//							// if we found a transition from clear to opaque, this sector is done in this column, so
//							// adjust the bottom vector upwards and continue processing it in the next column.
//							Slope newBottom = new Slope(y * 2 + 1, x * 2 - 1);
//							boolean inRange = Map.isInRange(origin.getPos(), evaluatedTile.getPos(), rangeLimit);
//							if (!inRange || y == bottomY) {
//								bottom = newBottom;
//								break;
//							}
//							else {
//								compute(octant, origin, rangeLimit, x + 1, top, newBottom, vc, isPlayer);
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
//	private boolean enemyFound(boolean isPlayer, Tile tile) {
//		Entity foundActor = tile.get(Type.ACTOR);
//		
//		if(foundActor == null) {
//			return false;
//		}
//		if(!isPlayer && foundActor.TYPE == Type.PLAYER) {
//			return true;
//		}
//		if(isPlayer && foundActor.TYPE != Type.PLAYER) {
//			return true;
//		}
//		
//		return false;
//	}
//	
//	private void resetVisionComponent(VisionComponent vc) {
//		vc.enemyTiles.clear();
//		vc.visionMap.clear();
//	}
//}