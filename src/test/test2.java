package test;

public class test2 {

	public static void main(String[] args) {
		java.util.Random random = new java.util.Random();
		double u = 0.0; // u = 0
		double v = 6; // v = 1
		for(int i = 0; i<10; i++) {
			System.out.println(Math.sqrt(v)*random.nextGaussian()+u);
		}
	}
}
