package me.ci;

import java.util.ArrayList;
import wraith.library.BinaryFile.CompactBinaryFile;

public class ResourceLoader{
	private ArrayList<FoodEntry> foods = new ArrayList<>();
	private ArrayList<FoodEntry> menu = new ArrayList<>();
	private DietNumbers maxDietNumbers = new DietNumbers();
	private DietNumbers currentDietNumbers = new DietNumbers();
	private CompactBinaryFile file;
	public ResourceLoader(){
		file=new CompactBinaryFile("Config.dat");
		file.read();
		if(!file.hasFinished()){
			int entries = (int)file.getNumber(16);
			for(int a = 0; a<entries; a++){
				FoodEntry f = new FoodEntry(file.getString(9));
				for(int b = 0; b<DietNumbers.SIZE; b++)f.getStats().stats[b]=(int)file.getNumber(16);
				foods.add(f);
			}
			entries=(int)file.getNumber(16);
			for(int a = 0; a<entries; a++)menu.add(foods.get((int)file.getNumber(16)));
			for(int b = 0; b<DietNumbers.SIZE; b++)maxDietNumbers.stats[b]=(int)file.getNumber(16);
		}
		file.stopReading();
		recountTodaysStats();
	}
	public void save(){
		file.write();
		file.addNumber(foods.size(), 16);
		for(FoodEntry f : foods){
			file.addString(f.getName(), 9);
			for(int b = 0; b<DietNumbers.SIZE; b++)file.addNumber(f.getStats().stats[b], 16);
		}
		file.addNumber(menu.size(), 16);
		for(FoodEntry f : menu)file.addNumber(foods.indexOf(f), 16);
		for(int b = 0; b<DietNumbers.SIZE; b++)file.addNumber(maxDietNumbers.stats[b], 16);
		file.stopWriting();
	}
	public void recountTodaysStats(){
		for(int b = 0; b<DietNumbers.SIZE; b++)currentDietNumbers.stats[b]=0;
		for(FoodEntry f : menu)for(int b = 0; b<DietNumbers.SIZE; b++)currentDietNumbers.stats[b]+=f.getStats().stats[b];
	}
	public ArrayList<FoodEntry> loadFoodList(){ return foods; }
	public DietNumbers loadMaxDiet(){ return maxDietNumbers; }
	public DietNumbers loadTodaysStats(){ return currentDietNumbers; }
	public ArrayList<FoodEntry> getMenu(){ return menu; }
}