package me.ci;

public class DietNumbers{
	public final int[] stats = new int[NAMES.length];
	public static final String[] NAMES = new String[]{
		"Cholesterol",
		"Sugar",
		"Sodium",
		"Calories",
		"Fat",
		"Carbs",
		"Sat. Fat",
		"Trans Fat",
		"Protein",
		"Iron",
		"Calcium",
		"Fiber",
		"Vitamin A",
		"Vitamin C",
		"Vitamin D"
	};
	public static final int SIZE = NAMES.length;
	public int max(){
		int high = 0;
		for(int i = 0; i<SIZE; i++)high=Math.max(stats[i], high);
		return high;
	}
	@Override public boolean equals(Object o){
		if(o instanceof DietNumbers){
			for(int i = 0; i<SIZE; i++)if(stats[i]!=((DietNumbers)o).stats[i])return false;
			return true;
		}
		return false;
	}
	public void clear(){ for(int a = 0; a<SIZE; a++)stats[a]=0; }
}