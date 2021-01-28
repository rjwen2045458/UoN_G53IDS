package dengueSpreadModelConstantDailyRoutine;

public class House extends Building{
	
	private static int currentIndex = 0;
	
	private int HouseIndex = 0;
	
	public House(int maxVisitor){
		super(maxVisitor);
		currentIndex++;
		HouseIndex = currentIndex;
	}

	public int getIndex() {
		return HouseIndex;
	}

	public String toString() {
		return "House "+ HouseIndex;
	}
}
