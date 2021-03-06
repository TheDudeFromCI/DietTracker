package me.ci;

import java.util.ArrayList;
import wraith.library.File.BinaryFile.CompactBinaryFile;

public class ResourceLoader{
	private ArrayList<FoodEntry> foods = new ArrayList<>();
	private ArrayList<FoodEntry> menu = new ArrayList<>();
	private DietNumbers maxDietNumbers = new DietNumbers();
	private DietNumbers currentDietNumbers = new DietNumbers();
	private CompactBinaryFile file;
	private short dayNumber = 0;
	private int[] weights = new int[0];
	public static final byte FILE_VERSION = -126;
	public ResourceLoader(){
		file=new CompactBinaryFile("Config.dat");
		if(!file.exists()){
			try{ file.createNewFile();
			}catch(Exception exception){ exception.printStackTrace(); }
		}
		file.read();
		if(!file.hasFinished()){
			byte version = (byte)file.getNumber(8);
			if(version==-128)loadFileVersion2(file);
			else if(version==-127)loadFileVersion3(file);
			else if(version==-126)loadFileVersion4(file);
		}
		System.out.println(dayNumber);
		file.stopReading();
		recountTodaysStats();
	}
	private void loadFileVersion4(CompactBinaryFile file){
		dayNumber=(short)file.getNumber(9);
		int entries = (int)file.getNumber(16);
		for(int a = 0; a<entries; a++){
			FoodEntry f = new FoodEntry(file.getString(16));
			f.setCetegory(file.getString(8));
			for(int b = 0; b<15; b++)f.getStats().stats[b]=(int)file.getNumber(16);
			foods.add(f);
		}
		entries=(int)file.getNumber(16);
		for(int a = 0; a<entries; a++)menu.add(foods.get((int)file.getNumber(16)));
		for(int b = 0; b<15; b++)maxDietNumbers.stats[b]=(int)file.getNumber(16);
		weights=new int[(int)file.getNumber(16)];
		for(int i = 0; i<weights.length; i++)weights[i]=(int)file.getNumber(14);
	}
	private void loadFileVersion3(CompactBinaryFile file){
		dayNumber=(short)file.getNumber(9);
		int entries = (int)file.getNumber(16);
		for(int a = 0; a<entries; a++){
			FoodEntry f = new FoodEntry(file.getString(16));
			f.setCetegory(file.getString(8));
			for(int b = 0; b<15; b++)f.getStats().stats[b]=(int)file.getNumber(16);
			foods.add(f);
		}
		entries=(int)file.getNumber(16);
		for(int a = 0; a<entries; a++)menu.add(foods.get((int)file.getNumber(16)));
		for(int b = 0; b<15; b++)maxDietNumbers.stats[b]=(int)file.getNumber(16);
		weights=new int[0];
	}
	private void loadFileVersion2(CompactBinaryFile file){
		dayNumber=(short)file.getNumber(9);
		int entries = (int)file.getNumber(16);
		for(int a = 0; a<entries; a++){
			FoodEntry f = new FoodEntry(file.getString(16));
			f.setCetegory(file.getString(8));
			f.getStats().stats[3]=(int)file.getNumber(16);
			f.getStats().stats[4]=(int)file.getNumber(16);
			f.getStats().stats[6]=(int)file.getNumber(16);
			f.getStats().stats[7]=(int)file.getNumber(16);
			f.getStats().stats[0]=(int)file.getNumber(16);
			f.getStats().stats[2]=(int)file.getNumber(16);
			f.getStats().stats[5]=(int)file.getNumber(16);
			f.getStats().stats[11]=(int)file.getNumber(16);
			f.getStats().stats[1]=(int)file.getNumber(16);
			f.getStats().stats[8]=(int)file.getNumber(16);
			f.getStats().stats[10]=(int)file.getNumber(16);
			f.getStats().stats[9]=(int)file.getNumber(16);
			f.getStats().stats[12]=(int)file.getNumber(16);
			f.getStats().stats[13]=(int)file.getNumber(16);
			f.getStats().stats[14]=(int)file.getNumber(16);
			foods.add(f);
		}
		entries=(int)file.getNumber(16);
		for(int a = 0; a<entries; a++)menu.add(foods.get((int)file.getNumber(16)));
		maxDietNumbers.stats[3]=(int)file.getNumber(16);
		maxDietNumbers.stats[4]=(int)file.getNumber(16);
		maxDietNumbers.stats[6]=(int)file.getNumber(16);
		maxDietNumbers.stats[7]=(int)file.getNumber(16);
		maxDietNumbers.stats[0]=(int)file.getNumber(16);
		maxDietNumbers.stats[2]=(int)file.getNumber(16);
		maxDietNumbers.stats[5]=(int)file.getNumber(16);
		maxDietNumbers.stats[11]=(int)file.getNumber(16);
		maxDietNumbers.stats[1]=(int)file.getNumber(16);
		maxDietNumbers.stats[8]=(int)file.getNumber(16);
		maxDietNumbers.stats[10]=(int)file.getNumber(16);
		maxDietNumbers.stats[9]=(int)file.getNumber(16);
		maxDietNumbers.stats[12]=(int)file.getNumber(16);
		maxDietNumbers.stats[13]=(int)file.getNumber(16);
		maxDietNumbers.stats[14]=(int)file.getNumber(16);
		weights=new int[0];
	}
	public void save(){
		file.write();
		file.addNumber(FILE_VERSION, 8);
		file.addNumber(dayNumber, 9);
		file.addNumber(foods.size(), 16);
		for(FoodEntry f : foods){
			file.addString(f.getName(), 16);
			file.addString(f.getCategory(), 8);
			for(int b = 0; b<DietNumbers.SIZE; b++)file.addNumber(f.getStats().stats[b], 16);
		}
		file.addNumber(menu.size(), 16);
		for(FoodEntry f : menu)file.addNumber(foods.indexOf(f), 16);
		for(int b = 0; b<DietNumbers.SIZE; b++)file.addNumber(maxDietNumbers.stats[b], 16);
		file.addNumber(weights.length, 16);
		for(int i = 0; i<weights.length; i++)file.addNumber(weights[i], 14);
		file.stopWriting();
		logDay();
	}
	private void logDay(){
		CompactBinaryFile file = new CompactBinaryFile("Log-"+dayNumber+".dat");
		file.ensureExistance();
		file.write();
		file.addNumber(FILE_VERSION, 8);
		for(int a = 0; a<DietNumbers.SIZE; a++)file.addNumber(currentDietNumbers.stats[a], 16);
		file.stopWriting();
	}
	public void newDay(){
		save();
		menu.clear();
		currentDietNumbers.clear();
		dayNumber=(short)((dayNumber+1)%512);
	}
	public void recountTodaysStats(){
		for(int b = 0; b<DietNumbers.SIZE; b++)currentDietNumbers.stats[b]=0;
		for(FoodEntry f : menu)for(int b = 0; b<DietNumbers.SIZE; b++)currentDietNumbers.stats[b]+=f.getStats().stats[b];
	}
	public DietNumbers getLog(int day){
		if(day==dayNumber)return currentDietNumbers;
		DietNumbers diet = new DietNumbers();
		if(day<0)return diet;
		CompactBinaryFile file = new CompactBinaryFile("Log-"+day+".dat");
		if(!file.exists())return diet;
		file.read();
		if(file.hasFinished())return diet;
		byte fileVersion = (byte)file.getNumber(8);
		if(fileVersion==-128){
			diet.stats[3]=(int)file.getNumber(16);
			diet.stats[4]=(int)file.getNumber(16);
			diet.stats[6]=(int)file.getNumber(16);
			diet.stats[7]=(int)file.getNumber(16);
			diet.stats[0]=(int)file.getNumber(16);
			diet.stats[2]=(int)file.getNumber(16);
			diet.stats[5]=(int)file.getNumber(16);
			diet.stats[11]=(int)file.getNumber(16);
			diet.stats[1]=(int)file.getNumber(16);
			diet.stats[8]=(int)file.getNumber(16);
			diet.stats[10]=(int)file.getNumber(16);
			diet.stats[9]=(int)file.getNumber(16);
			diet.stats[12]=(int)file.getNumber(16);
			diet.stats[13]=(int)file.getNumber(16);
			diet.stats[14]=(int)file.getNumber(16);
		}else if(fileVersion==-127){
			for(int i = 0; i<15; i++)diet.stats[i]=(int)file.getNumber(16);
		}else if(fileVersion==-126)for(int i = 0; i<15; i++)diet.stats[i]=(int)file.getNumber(16);
		file.stopReading();
		return diet;
	}
	public int getCurrentDay(){ return dayNumber; }
	public ArrayList<FoodEntry> loadFoodList(){ return foods; }
	public DietNumbers loadMaxDiet(){ return maxDietNumbers; }
	public DietNumbers loadTodaysStats(){ return currentDietNumbers; }
	public ArrayList<FoodEntry> getMenu(){ return menu; }
	public int[] getWeights(){ return weights; }
	public void setWeights(int[] weights){ this.weights=weights; }
}