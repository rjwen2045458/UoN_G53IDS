package dengueSpreadModelConstantDailyRoutine;

import sim.engine.*;
import sim.field.continuous.*;
import sim.util.*;
import sim.field.network.*;

public class Monitor implements Steppable, ModelConfig{

	@Override
	public void step(SimState state) {
		
		DengueSpreadModelConstantDailyRountine model = (DengueSpreadModelConstantDailyRountine) state;
		
		int exposedPeople = 0;
		int infectiousPeople = 0;
		int revivingPeople = 0;
		int resistedPeople = 0;
		int infectiousPlaces = 0;
		
		for(int i = 0; i < model.people.size(); i++) {
			boolean exposed = ((Person) model.people.get(i)).isExposed();
			if(exposed) exposedPeople ++;
		}
		
		for(int i = 0; i < model.people.size(); i++) {
			boolean infected = ((Person) model.people.get(i)).isInfectious();
			if(infected) infectiousPeople ++;
		}
		
		for(int i = 0; i < model.people.size(); i++) {
			boolean reviving = ((Person) model.people.get(i)).isReviving();
			if(reviving) revivingPeople ++;
		}
		
		for(int i = 0; i < model.people.size(); i++) {
			boolean resisted = ((Person) model.people.get(i)).isResisted();
			if(resisted) resistedPeople ++;
		}
		
		for(int i = 0; i < model.houses.size(); i++) {
			boolean infected = ((Building) model.houses.get(i)).isInfectious();
			if(infected) infectiousPlaces ++;
		}
		
		for(int i = 0; i < model.workplaces.size(); i++) {
			boolean infected = ((Building) model.workplaces.get(i)).isInfectious();
			if(infected) infectiousPlaces ++;
		}
		
		for(int i = 0; i < model.commercials.size(); i++) {
			boolean infected = ((Building) model.commercials.get(i)).isInfectious();
			if(infected) infectiousPlaces ++;
		}
		
		model.Exposed_People = exposedPeople;
		model.Infectious_People = infectiousPeople;
		model.Reviving_People = revivingPeople;
		model.Resisted_People = resistedPeople;
		model.Infectious_Places = infectiousPlaces;
				
		System.out.println("Day" + model.schedule.getSteps()+" ExposedPeople: "+exposedPeople+" InfectiousPeople: "
		+infectiousPeople+" RevivingPeople: "+revivingPeople+" ResistedPeople: "+resistedPeople+" infectiousPlaces: "+infectiousPlaces);
	}
	
}
