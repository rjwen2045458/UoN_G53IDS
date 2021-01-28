package dengueSpreadModelConstantDailyRoutine;

import sim.engine.SimState;
import sim.util.distribution.*;
import sim.util.media.chart.ChartGenerator;
import sim.util.media.chart.TimeSeriesChartGenerator;
import org.jfree.data.xy.XYSeries;

public class testtest extends SimState{

	public testtest(long seed) {
		super(seed);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		testtest t = new testtest(System.currentTimeMillis());
		for (int i = 0; i<100; i++) {
			double x = Distributions.nextPowLaw(2.8, 2.8, t.random);
			//System.out.println(x);
		}
		double sum1 = 0;
		for (int i = 0; i<100; i++) {
			double x = (int)Distributions.nextWeibull(1.7*2, 6.5*2, t.random);
			x += 2;
			sum1+=x;
			System.out.println(x);
		}
		System.out.println("\n" + sum1/100);
		
		Poisson p = new Poisson(6,t.random);
		for (int i = 0; i<100; i++) {
			double x = p.nextInt();
			//System.out.println(x);
		}
		Exponential e = new Exponential( 1, t.random);
		double sum = 0;
		for (int i = 0; i<100; i++) {
			double x = (double)(int)e.nextDouble()+1;
			sum += x;
			//System.out.println(x);
			
		}
		for (int i = 0; i<100; i++) {
			double x = t.random.nextDouble();
			//System.out.println(x);
		}
		
		//System.out.println("\n" + sum/100);
		 XYSeries firefox = new XYSeries( "Firefox" );
	      firefox.add( 1.0 , 1.0 );
	      firefox.add( 2.0 , 4.0 );
	      firefox.add( 3.0 , 3.0 );
	      XYSeries chrome = new XYSeries( "Chrome" );
	      chrome.add( 1.0 , 4.0 );
	      chrome.add( 2.0 , 5.0 );
	      chrome.add( 3.0 , 6.0 );
	      XYSeries iexplorer = new XYSeries( "InternetExplorer" );
	      iexplorer.add( 3.0 , 4.0 );
	      iexplorer.add( 4.0 , 5.0 );
	      iexplorer.add( 5.0 , 4.0 );
		
		
		TimeSeriesChartGenerator c = new TimeSeriesChartGenerator();
		c.addSeries(iexplorer, null);
		c.addSeries(chrome, null);
		c.addSeries(firefox, null);
		//RefineryUtilities.centerFrameOnScreen( c );          
	      c.setVisible( true );
	}

}
