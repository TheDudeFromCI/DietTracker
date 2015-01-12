package me.ci;

public class FoodEntry{
	private String name;
	private DietNumbers stats;
	public FoodEntry(String name){
		this.name=name;
		stats=new DietNumbers();
	}
	public int getRemaining(DietNumbers current, DietNumbers max){
		if(stats.max()==0)return -1;
		int c = Integer.MAX_VALUE;
		for(int a = 0; a<DietNumbers.SIZE; a++){
			if(stats.stats[a]==0)continue;
			int p = (max.stats[a]-current.stats[a])/stats.stats[a];
			if(p<c)c=p;
		}
		return c;
	}
	@Override public boolean equals(Object o){ return o instanceof FoodEntry&&((FoodEntry)o).stats.equals(stats); }
	public String getName(){ return name; }
	public DietNumbers getStats(){ return stats; }
	public void setName(String name){ this.name=name; }
}