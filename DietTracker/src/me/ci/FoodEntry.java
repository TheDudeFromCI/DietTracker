package me.ci;

public class FoodEntry{
	private String name;
	private DietNumbers stats;
	public FoodEntry(String name){
		this.name=name;
		stats=new DietNumbers();
	}
	@Override public boolean equals(Object o){ return o instanceof FoodEntry&&((FoodEntry)o).stats.equals(stats); }
	public String getName(){ return name; }
	public DietNumbers getStats(){ return stats; }
	public void setName(String name){ this.name=name; }
}