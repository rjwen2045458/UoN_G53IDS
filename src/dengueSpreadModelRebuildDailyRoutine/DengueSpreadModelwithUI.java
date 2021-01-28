package dengueSpreadModelRebuildDailyRoutine;

import sim.engine.*;
import sim.display.*;
import sim.util.media.chart.TimeSeriesAttributes;
import sim.util.media.chart.TimeSeriesChartGenerator;
import sim.portrayal.*;

public class DengueSpreadModelwithUI extends GUIState
{
	public TimeSeriesAttributes exposedPeople;
	public TimeSeriesAttributes infectiousPeople;
	public TimeSeriesAttributes revivingPeople;
	public TimeSeriesAttributes resistedPeople;
	public TimeSeriesAttributes infectiousPlaces;
	public TimeSeriesChartGenerator myChart;
	
	public static void main(String[] args)
	{
		DengueSpreadModelwithUI vid = new DengueSpreadModelwithUI();
		Console c = new Console(vid);
		c.setVisible(true);
	}

	public DengueSpreadModelwithUI() { super(new DengueSpreadModelRebuildDailyRountine( System.currentTimeMillis())); }

	public DengueSpreadModelwithUI(SimState state) { super(state); }

	public static String getName() { return "Dengue Spread Model";}

	public Object getSimulationInspectedObject() { return state; }

	public Inspector getInspector(){
		Inspector i = super.getInspector();
		i.setVolatile(true);
		return i;
	}

	public void start()
	{
		super.start();
		myChart.clearAllSeries(); 
		updateData();
	}
	
	public void load(SimState state)
	{
		super.load(state);
		updateData();
	}
	
	public void updateData() {
		ChartUtilities.scheduleSeries(this, exposedPeople, new sim.util.Valuable(){
			public double doubleValue(){
				return ((DengueSpreadModelRebuildDailyRountine)state).Exposed_People;
			}
		});
		ChartUtilities.scheduleSeries(this, infectiousPeople, new sim.util.Valuable(){
			public double doubleValue(){
				return ((DengueSpreadModelRebuildDailyRountine)state).Infectious_People;
			}
		});
		ChartUtilities.scheduleSeries(this, revivingPeople, new sim.util.Valuable(){
			public double doubleValue(){
				return ((DengueSpreadModelRebuildDailyRountine)state).Reviving_People;
			}
		});
		ChartUtilities.scheduleSeries(this, resistedPeople, new sim.util.Valuable(){
			public double doubleValue(){
				return ((DengueSpreadModelRebuildDailyRountine)state).Resisted_People;
			}
		});
		ChartUtilities.scheduleSeries(this, infectiousPlaces, new sim.util.Valuable(){
			public double doubleValue(){
				return ((DengueSpreadModelRebuildDailyRountine)state).Infectious_Places;
			}
		});
	}
	
	public void init(Controller c)
	{
		super.init(c);
		myChart = ChartUtilities.buildTimeSeriesChartGenerator(this, "Dengue Spread Simulation", "Timeline");
		exposedPeople = ChartUtilities.addSeries(myChart, "Exposed People");
		infectiousPeople = ChartUtilities.addSeries(myChart, "Infectious P eople");
		revivingPeople = ChartUtilities.addSeries(myChart, "Reviving People");
		resistedPeople = ChartUtilities.addSeries(myChart, "Resisted People");
		infectiousPlaces = ChartUtilities.addSeries(myChart, "Infectious Place");
	}
	
	public void quit()
	{
		super.quit();
	}
}