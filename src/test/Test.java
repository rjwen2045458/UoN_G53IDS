package test;

import sim.util.*;
import ec.util.MersenneTwisterFast;
import sim.engine.*;
import sim.field.network.*;

public class Test extends SimState{
	
	public Test(long seed) {
		super(seed);
		// TODO Auto-generated constructor stub
	}

	public Bag bagSelect(Bag inputBag, int num) {
		Bag input = new Bag(inputBag);
		Bag result = new Bag();
		if(num < 1) return result;
		if(num > inputBag.size()) return input;
		
		for(int i = 0; i < num; i++) {
			result.add(input.remove(random.nextInt(input.size())));
		}
		
		for(int i = 0; i<95; i++) {
			System.out.println(input.get(i));
		}
		return result;
	}
	
	private Bag schedule(int place, int timePeriod, MersenneTwisterFast random) {
		Bag schedule = new Bag();
		schedule.add(timePeriod);
		
		for(int i = 0; i < place-1; i++) {
			int t = (int) schedule.remove(0);
			while(t <= 1) {
				int tp = t;
				schedule.add(tp);
				t = (int) schedule.remove(0);
			}
			
			int t1 = random.nextInt(t-1)+1;
			int t2 = t - t1;
			schedule.add(t1);
			schedule.add(t2);	
		}
		
		return schedule;
	}
	
	private void sf(Bag x,MersenneTwisterFast random) {
		Bag b = new Bag(x);
		b.shuffle(random);
	}
	
	private Bag bagRemoveHelper(Bag input) {
		Bag b = new Bag();
		for(int i = 1; i < input.size(); i++) {
			b.add(input.get(i));
		}
		b.add(input.get(0));
		return b;
	}
	public static void main(String[] args) {
		Test t = new Test(System.currentTimeMillis());
		Bag b = new Bag();
		for(int i = 0; i<6; i++) {
			b.add(i);
		}
		
//		Bag r = t.bagSelect(b, 5);
//		Bag r = t.schedule(5,15,t.random);
//		t.sf(b,t.random);
		Bag r = t.bagRemoveHelper(b);
		
		for(int i = 0; i<r.size(); i++) {
			System.out.println(r.get(i));
		}
//		System.out.println(t.random.nextInt());
	}

}
