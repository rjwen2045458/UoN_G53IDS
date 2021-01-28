package dengueSpreadModelConstantDailyRoutine;

public interface ModelConfig {
	
	// Initial agents number setting
	public final int People = 100000;
	
	public final double HousesCoefficient = 1;
	
	public final double WorkplacesCoefficient = 0.21;
	
	public final double CommercialsCoefficient = 0.09;
	
	
	// default alogrithm parameter setting

	public final int DayLength = 16;
		
	// the number of places a person will visit
	// Weibull distribution
	
	public final double Scale = 6.5;
	
	public final double Shape = 1.7;
	
	// the number of people visiting a single place
	// Power Law distribution
	
	public final double PLCoefficient = 2.8;
	
	// home location 
	// Poisson distribution
	public final int mean = 6;
	
	// place assignment algorithm parameter setting (PP: per person)
	
	public final int HousePP = 5;
	
	public final int WorkplacePP = 4;
	
	public final int CommercialPP = 3;
	
	
	// day schedual algorithm parameter setting
	
	public final int MaxPlaces = 9;
	
	public final int MinPlaces = 3;
	
	// dengue spread model design
	
	public final int PeopleE = 4; // Exposed period
	
	public final int PeopleI = 5; // Ingectious period
	
	public final int PeopleR = 7; // reviving period After this period the person is resisted
	
	public final int PlaceE = 7; // Exposed period
	
	public final int PlaceI = 14; // infectious period 
	
	// infectious plan 
	
	public final String InfectiousPlan = "Linear"; // "Default", "Linear", "Polynominal"
	
	public final double InfectiousCoefficientLinear = 0.005; // infectious coefficient for linear plan
	
	public final double InfectiousCoefficientPolynominal = 0.2; // infectious coefficient for polynominal plan
	
}
