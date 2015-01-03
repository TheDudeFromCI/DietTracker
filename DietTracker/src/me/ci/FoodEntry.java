package me.ci;

public class FoodEntry{
	private String name;
	private DietNumbers stats;
	public FoodEntry(String name){
		this.name=name;
		stats=new DietNumbers();
	}
	public String getName(){ return name; }
	public DietNumbers getStats(){ return stats; }
}