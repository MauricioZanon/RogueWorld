package com.rogueworld.entities.factories;

import com.rogueworld.entities.main.Entity;

public class FeatureFactory extends EntityFactory{
	
	protected static Entity[] featuresByID = null;
	
	private FeatureFactory() {}

	protected static Entity createFeature(int ID) {
		int correctedID = ID - 2000;
		if(correctedID > featuresByID.length-1) {
			System.out.println("ID de feature incorrecta, el máximo es " + (featuresByID.length-1) + " y se pidió " + correctedID);
			return null;
		}else {
			return featuresByID[correctedID].clone();
		}
	}
	
	protected static Entity get(int ID) {
		int correctedID = ID - 2000;
		if(correctedID > featuresByID.length-1) {
			System.out.println("ID de feature incorrecta, el máximo es " + (featuresByID.length-1) + " y se pidió " + correctedID);
			return null;
		}else {
			return featuresByID[correctedID];
		}
	}
	
	
}
