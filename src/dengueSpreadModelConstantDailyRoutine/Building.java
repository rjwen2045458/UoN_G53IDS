package dengueSpreadModelConstantDailyRoutine;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.network.Edge;
import sim.util.Bag;

public class Building implements Steppable, ModelConfig{
	
	private int maxVisitor = 0;
	
	// dengue spread model
	Bag E = new Bag();
	int I = 0;
	
	Building(int maxVisitor){
		this.maxVisitor = maxVisitor;
	}
	
	public int getMaxVisitor() {
		return maxVisitor;
	}

	@Override
	public void step(SimState state) {
		DengueSpreadModelConstantDailyRountine model = (DengueSpreadModelConstantDailyRountine) state;
		
		OneDayPass();
		
		switch(InfectiousPlan) {
		case "Default":
			InfectedDefault(model);
			break;
		case "Linear":
			InfectedLinear(model);
			break;
		case "Polynominal":
			InfectedPolynominal(model);
			break;
		}
		
		
	}
	
	// update infectious status after one day passed
	public void OneDayPass() {
		if(I > 0) I -= 1;
		if(!E.isEmpty()) {
			for(int i = 0; i < E.size(); i++) {
				int left = (int) E.get(i)-1;
				E.remove(i);
				E.add(left);
			}
			for(int i = 0; i < E.size(); i++) {
				int value = (int)E.get(i);
				if(value == 0) {
					I = PlaceI;
					E.remove(i);
					break;
				}
			}
		}
	}
	
	// update infectious status after infected (default plan)
	public void InfectedDefault(DengueSpreadModelConstantDailyRountine model) {
		// whether the building is infected
		Bag PeopleVisited = model.socialNetwork.getEdgesIn(this);
		boolean infected = false;
		for (int i = 0; i < PeopleVisited.size(); i++) {
			if(((Person)((Edge)PeopleVisited.get(i)).getFrom()).isInfectious()) {
				infected = true;
				break;
			}
		}
				
		// if infected
		if(infected) E.add(PlaceE);
	}
	
	// update infectious status after infected (linear plan)
	public void InfectedLinear(DengueSpreadModelConstantDailyRountine model) {
		// whether the building is infected
		Bag PeopleVisited = model.socialNetwork.getEdgesIn(this);
		boolean infected = false;
		for (int i = 0; i < PeopleVisited.size(); i++) {
			Edge e = (Edge)PeopleVisited.get(i);
			int period = (int) e.getInfo();
			double randomPercentage = model.random.nextDouble();
			// only when the person is infectious and probability test is passed
			if(((Person)e.getFrom()).isInfectious() && randomPercentage < period*InfectiousCoefficientLinear) {
				infected = true;
				break;
			}
		}
		
		// if infected
		if(infected) E.add(PlaceE);
	}
	
	// update infectious status after infected (polynominal plan)
	public void InfectedPolynominal(DengueSpreadModelConstantDailyRountine model) {
		// whether the building is infected
		Bag PeopleVisited = model.socialNetwork.getEdgesIn(this);
		boolean infected = false;
		for (int i = 0; i < PeopleVisited.size(); i++) {
			Edge e = (Edge)PeopleVisited.get(i);
			int period = (int) e.getInfo();
			double randomPercentage = model.random.nextDouble();
			// only when the person is infectious and probability test is passed
			if(((Person)e.getFrom()).isInfectious() && randomPercentage < Math.pow(1+InfectiousCoefficientPolynominal, period)-1) {
				infected = true;
				break;
			}
		}
		
		// if infected
		if(infected) E.add(PlaceE);
	}
	
	public boolean isInfectious() {
		if(I > 0) return true;
		else return false;
	} 
}