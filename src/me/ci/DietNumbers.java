package me.ci;

import java.util.Arrays;

public class DietNumbers{
	public static final String[] NAMES = new String[]{
		"Calories", "Fat", "Sat. Fat", "Trans Fat", "Poly", "Mono", "Cholesterol", "Sodium", "Carbs", "Fiber", "Sugar", "Protein", "Calcium", "Iron",
		"Vitamin A", "Vitamin C", "Vitamin D"
	};
	public static final int SIZE = NAMES.length;
	public final int[] stats = new int[NAMES.length];
	public int max(){
		int high = 0;
		for(int i = 0; i<SIZE; i++){
			high = Math.max(stats[i], high);
		}
		return high;
	}
	@Override
	public int hashCode(){
		int hash = 3;
		hash = 67*hash+Arrays.hashCode(this.stats);
		return hash;
	}
	@Override
	public boolean equals(Object o){
		if(o instanceof DietNumbers){
			for(int i = 0; i<SIZE; i++){
				if(stats[i]!=((DietNumbers)o).stats[i]){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	public void clear(){
		for(int a = 0; a<SIZE; a++){
			stats[a] = 0;
		}
	}
}
