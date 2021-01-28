package dengueSpreadModelRebuildDailyRoutine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import sim.engine.*;
import sim.field.network.*;
import sim.util.*;
import sim.util.distribution.*;

public class DengueSpreadModelRebuildDailyRountine extends SimState implements ModelConfig{
	
	public Bag people = new Bag();
	public Bag houses = new Bag();
	public Bag workplaces = new Bag();
	public Bag commercials = new Bag();
	public Network socialNetwork = new Network(true);
	
	public int Exposed_People = 0;
	public int Infectious_People = 0;
	public int Reviving_People = 0;
	public int Resisted_People = 0;
	public int Infectious_Places = 0; 

	int initialPatient = 10;
	
	public DengueSpreadModelRebuildDailyRountine(long seed) {
		super(seed);
	}

	public void start() {
		super.start();
		
		// clear the bags and the network
		people.clear();
		houses.clear();
		workplaces.clear();
		commercials.clear();
		socialNetwork.clear();

		// create people agents
		for (int i = 0; i < People; i++) 
			// the number of places should larger than 2 (a home and an other place)
			people.add(new Person((int) Distributions.nextWeibull(Shape/2, Scale*2, random)+2));
			
		// assign people to their home
		Bag peopleToHome  = new Bag(people);
		Poisson PD = new Poisson(1, random); // poission distribution
		
		while (!peopleToHome.isEmpty()) {
			int maxPeopleInHome = PD.nextInt();
			House h = new House(maxPeopleInHome);
			if(maxPeopleInHome > peopleToHome.size()) maxPeopleInHome = peopleToHome.size();
			for (int i = 0; i < maxPeopleInHome;i++) {
				Person p = (Person) peopleToHome.remove(random.nextInt(peopleToHome.size()));
				p.setHomeLocation(h);
			}
			houses.add(h);
		}
		
		// the number of different types of building
		int numHouses = (int) (houses.size()*HousesCoefficient);
		int numWorkplaces = (int) (numHouses*WorkplacesCoefficient);
		int numCommercials = (int) (numHouses*CommercialsCoefficient);
				
		// create workplaces and commercials
		for (int i = 0; i < numWorkplaces; i++)
			workplaces.add(new Workplace((int) Distributions.nextPowLaw(PLCoefficient, PLCoefficient, random) + 1));
		for (int i = 0; i < numCommercials; i++)
			commercials.add(new Commercial((int) Distributions.nextPowLaw(PLCoefficient, PLCoefficient, random) + 1));
		
		// add agents and building into network
		for (int i = 0; i < People; i++) { 
			socialNetwork.addNode(people.get(i));
			schedule.scheduleRepeating((Steppable) people.get(i));
		}
		for (int i = 0; i < numHouses; i++) {
			socialNetwork.addNode(houses.get(i));
			schedule.scheduleRepeating((Steppable) houses.get(i));
		}
		for (int i = 0; i < numWorkplaces; i++) {
			socialNetwork.addNode(workplaces.get(i));
			schedule.scheduleRepeating((Steppable) workplaces.get(i));
		}
		for (int i = 0; i < numCommercials; i++) {
			socialNetwork.addNode(commercials.get(i));
			schedule.scheduleRepeating((Steppable) commercials.get(i));
		}
		
		// assign buildings to people // info 0 is visitor
		Bag buildings = new Bag();
		buildings.addAll(houses);
		buildings.addAll(workplaces);
		buildings.addAll(commercials);
		
		for (int i = 0; i < People; i++) {
			// the person and the places
			Person P = (Person) people.get(i);
			Building home = P.getHomeLocation();
			
			int numOfPlacesToGo = 20;
			Bag places = new Bag();
			Bag buildingToChoose = new Bag(buildings);
			buildingToChoose.remove(home);// ensure a person will not have same location in its routine
			for (int j = 0; j < numOfPlacesToGo-1; j++)
				places.add((Building) buildingToChoose.remove(random.nextInt(buildingToChoose.size())));
			
			P.setPlaces(places);
		}
		
		//printSocialNetwork();
		
		Monitor m = new Monitor();
		schedule.scheduleRepeating(1.0, 1, m);
		Bag peopleCopy = new Bag(people);
		for (int i = 0; i < initialPatient; i++ ) {
			Person firstPatient = (Person) peopleCopy.get(random.nextInt(peopleCopy.size()));
			firstPatient.theFirstPatient();
		}
	}
	
	public void finish() {
		super.finish();
		
		double a = Resisted_People;
		double b = People;
//		System.out.println(a);
//		System.out.println(b);
		System.out.println(String.format("%.6f", a/b));
		
		socialNetwork.clear();
		people.clear();
		houses.clear();
		workplaces.clear();
		commercials.clear();
		
		Exposed_People = 0;
		Infectious_People = 0;
		Reviving_People = 0;
		Resisted_People = 0;
		Infectious_Places = 0; 
	}
	
	public static void main(String[] args) {
		int jobs = 1; // let's do 100 runs
		SimState state = new DengueSpreadModelRebuildDailyRountine(System.currentTimeMillis()); // MyModel is our SimState subclass
		System.out.println(((DengueSpreadModelRebuildDailyRountine)state).InfectiousCoefficientLinear);
		System.out.println(((DengueSpreadModelRebuildDailyRountine)state).initialPatient);
		for(int job = 0; job < jobs; job++) {
			state.setJob(job);
			state.start();
			do {
				if (!state.schedule.step(state)) break;
			}
			while(state.schedule.getSteps() <= 180);
			state.finish();
		}
		System.exit(0);
	}
	
	public void printSocialNetwork() {
		try{
			// write the file that People -> Place
		    File file1 = new File("SocialNetworkPeopleToPlace.txt");
		    if(file1.exists()){
		    	file1.delete();
		    	file1.createNewFile();
		    }
		    else {
		    	file1.createNewFile();
		    } 
		    FileWriter fw1 = new FileWriter(file1,true);
		    BufferedWriter bw1 = new BufferedWriter(fw1);
			
		    double sum = 0;
		    int SClimit = 8;
		    double SuperConnector = 0;
			for(int i = 0; i < People; i++) {
				Person P = (Person) people.get(i); 
				Bag places = socialNetwork.getEdgesOut(P); sum+=places.size(); if(places.size()>SClimit) SuperConnector++;
				//String outputLine = "Person " + String.valueOf(P.getPersonIndex()) + " : " + String.valueOf(places.size()) + " places.\n";
				for (int j = 0; j <places.size(); j++) {
					//outputLine = outputLine + ((Edge) places.get(j)).getTo().toString()+ ",";
					bw1.write(String.valueOf(P.getPersonIndex()) + "," + ((Edge) places.get(j)).getTo().toString()+"\n");
				}
				//outputLine = outputLine + "\n";
				//bw1.write(outputLine);
			}
			double average = sum/People;
			bw1.write("The average number of connected places with respect to one person is "+ average+"\n");
			System.out.print("The average number of connected places with respect to one person is "+ average+"\n");
			bw1.write("The number of people super connector is "+SuperConnector+" (larger than "+SClimit+")\n");
			bw1.write("The percent of super connector is "+SuperConnector*100/People+"%\n");
		    bw1.close();
		    
		    // write the file that Place -> People 
		    File file2 = new File("SocialNetworkPlaceToPeople.txt");
		    if(file2.exists()){
		    	file2.delete();
		    	file2.createNewFile();
		    }
		    else {
		    	file2.createNewFile();
		    }
		    FileWriter fw2 = new FileWriter(file2,true);
		    BufferedWriter bw2 = new BufferedWriter(fw2);
			
		    double sumP = 0;
		    int SClimitP = 18;
		    double SuperConnectorP = 0;
			for(int i = 0; i < houses.size(); i++) {
				House h = (House) houses.get(i); 
				Bag people = socialNetwork.getEdgesIn(h); sumP+=people.size(); if(people.size()>SClimitP) SuperConnectorP++;
				//String outputLine = h.toString() + " connects to " + String.valueOf(people.size()) + " people.\n";
				for (int j = 0; j <people.size(); j++) {
					//outputLine = outputLine + "Person "+String.valueOf(((Person) ((Edge) people.get(j)).getFrom()).getPersonIndex())+ " ";
					bw2.write(h.toString()+","+String.valueOf(((Person) ((Edge) people.get(j)).getFrom()).getPersonIndex())+"\n");
				}
				//outputLine = outputLine + "\n\n";
				//bw2.write(outputLine);
			}
			
			for(int i = 0; i < workplaces.size(); i++) {
				Workplace w = (Workplace) workplaces.get(i); 
				Bag people = socialNetwork.getEdgesIn(w); sumP+=people.size(); if(people.size()>SClimitP) SuperConnectorP++;
				//String outputLine = w.toString() + " connects to " + String.valueOf(people.size()) + " people.\n";
				for (int j = 0; j <people.size(); j++) {
					//outputLine = outputLine + "Person "+String.valueOf(((Person) ((Edge) people.get(j)).getFrom()).getPersonIndex())+ " ";
					bw2.write(w.toString()+","+String.valueOf(((Person) ((Edge) people.get(j)).getFrom()).getPersonIndex())+"\n");
				}
				//outputLine = outputLine + "\n\n";
				//bw2.write(outputLine);
			}
			
			for(int i = 0; i < commercials.size(); i++) {
				Commercial c = (Commercial) commercials.get(i); 
				Bag people = socialNetwork.getEdgesIn(c); sumP+=people.size(); if(people.size()>SClimitP) SuperConnectorP++;
				//String outputLine = c.toString() + " connects to " + String.valueOf(people.size()) + " people.\n";
				for (int j = 0; j <people.size(); j++) {
					//outputLine = outputLine + "Person "+String.valueOf(((Person) ((Edge) people.get(j)).getFrom()).getPersonIndex())+ " ";
					bw2.write(c.toString()+","+String.valueOf(((Person) ((Edge) people.get(j)).getFrom()).getPersonIndex())+"\n");
				}
				//outputLine = outputLine + "\n\n";
				//bw2.write(outputLine);
			}
			double averageP = sumP/(houses.size()+workplaces.size()+commercials.size());
			bw2.write("The average number of connected people with respect to one place is "+ averageP+"\n");
			System.out.print("The average number of connected people with respect to one place is "+ averageP+"\n");
			bw2.write("The number of place super connector is "+SuperConnectorP+" (larger than "+ SClimitP+")\n");
			bw2.write("The percent of super connector is "+SuperConnectorP*100/(houses.size()+workplaces.size()+commercials.size())+"%\n");
		    bw2.close();
		    
		}
		catch(IOException ioe){
		System.out.println("Exception occurred:");
		     ioe.printStackTrace();
		}
	}
	
}
