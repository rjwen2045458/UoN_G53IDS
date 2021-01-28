package dengueSpreadModelConstantDailyRoutine;

import java.util.ArrayList;

import sim.engine.*;
import sim.util.*;
import sim.util.distribution.Exponential;
import sim.field.network.*;

public class Person implements Steppable, ModelConfig{
	
	static int currentIndex = 0;
	
	private int PersonIndex = 0;
	
	private House homeLocation = null;
	
	private int maxDailyPlaces = 0;
	
	private int routinePlaces = 0;
	
	private int infectiousTimeline = 0;
	
	private Bag Places = null;
	
	public Person(int maxPlaces) {
		super();
		this.maxDailyPlaces = maxPlaces;
		currentIndex++;
		PersonIndex = currentIndex;
	}
		
	public void step(SimState state) {
		DengueSpreadModelConstantDailyRountine peopleM = (DengueSpreadModelConstantDailyRountine) state;
		
		// update the infectious status schedule
		if(infectiousTimeline > 0) infectiousTimeline ++;
		
		// whether the building is infected
				Bag PlaceVisited = peopleM.socialNetwork.getEdgesOut(this);
				boolean infected = false;
				for (int i = 0; i < PlaceVisited.size(); i++) {
					if(((Building)((Edge)PlaceVisited.get(i)).getTo()).isInfectious()) {
						infected = true;
						break;
					}
				}
		
		// if infected
		if(infected && infectiousTimeline == 0) infectiousTimeline ++;
		
	}
	
	public void theFirstPatient() {
		infectiousTimeline ++;
	}
	
	public boolean isInfectious() {
		if(infectiousTimeline >= 5 && infectiousTimeline <= 8) return true;
		else return false;
	}
	
	public boolean isExposed() {
		if(infectiousTimeline >= 1 && infectiousTimeline <= 4) return true;
		else return false;
	}
	
	public boolean isReviving() {
		if(infectiousTimeline >= 9 && infectiousTimeline <= 16) return true;
		else return false;
	}
	
	public boolean isResisted() {
		if(infectiousTimeline >= 17) return true;
		else return false;
	}
	public House getHomeLocation() {
		return homeLocation;
	}
	
	public void setHomeLocation(House h) {
		homeLocation = h;
	}
	
	public int getMaxPlaces() {
		return maxDailyPlaces;
	}
	
	public void setMaxPlaces(int number) {
		maxDailyPlaces = number;
	}
	
	public void setPlaces(Bag places) {
		this.Places = places;
	}
	
	public int getRoutinePlaces() {
		return routinePlaces;
	}
	
	public void setRoutinePlaces(int number) {
		routinePlaces = number;
	}
	
	public int getPersonIndex() {
		return PersonIndex;
	}
}
