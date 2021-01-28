package dengueSpreadModelConstantDailyRoutine;

public class Workplace extends Building{

	private static int currentIndex = 0;
	
	private int WorkplaceIndex = 0;
	
	public Workplace(int maxVisitor){
		super(maxVisitor);
		currentIndex++;
		WorkplaceIndex = currentIndex;
	}

	public int getIndex() {
		return WorkplaceIndex;
	}

	public String toString() {
		return "Workplace "+ WorkplaceIndex;
	}
}
