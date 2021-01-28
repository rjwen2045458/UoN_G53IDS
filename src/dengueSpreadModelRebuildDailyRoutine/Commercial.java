package dengueSpreadModelRebuildDailyRoutine;

public class Commercial extends Building{

	private static int currentIndex = 0;
	
	private int CommercialIndex = 0;
	
	public Commercial(int maxVisitor){
		super(maxVisitor);
		currentIndex++;
		CommercialIndex = currentIndex;
	}

	public int getIndex() {
		return CommercialIndex;
	}

	public String toString() {
		return "Commercial "+ CommercialIndex;
	}
}
