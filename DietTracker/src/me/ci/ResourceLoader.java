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
	public static final byte FILE_VERSION = -128;
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
			else loadFileVersion1(file);
		}
		file.stopReading();
		recountTodaysStats();
	}
	private void loadFileVersion2(CompactBinaryFile file){
		dayNumber=(short)file.getNumber(9);
		int entries = (int)file.getNumber(16);
		for(int a = 0; a<entries; a++){
			FoodEntry f = new FoodEntry(file.getString(16));
			for(int b = 0; b<DietNumbers.SIZE; b++)f.getStats().stats[b]=(int)file.getNumber(16);
			foods.add(f);
		}
		entries=(int)file.getNumber(16);
		for(int a = 0; a<entries; a++)menu.add(foods.get((int)file.getNumber(16)));
		for(int b = 0; b<DietNumbers.SIZE; b++)maxDietNumbers.stats[b]=(int)file.getNumber(16);
	}
	private void loadFileVersion1(CompactBinaryFile file){
		file.resetIterator();
		int entries = (int)file.getNumber(16);
		for(int a = 0; a<entries; a++){
			FoodEntry f = new FoodEntry(file.getString(16));
			for(int b = 0; b<8; b++)f.getStats().stats[b]=(int)file.getNumber(16);
			foods.add(f);
		}
		entries=(int)file.getNumber(16);
		for(int a = 0; a<entries; a++)menu.add(foods.get((int)file.getNumber(16)));
		for(int b = 0; b<8; b++)maxDietNumbers.stats[b]=(int)file.getNumber(16);
	}
	public void save(){
		file.write();
		file.addNumber(FILE_VERSION, 8);
		file.addNumber(dayNumber, 9);
		file.addNumber(foods.size(), 16);
		for(FoodEntry f : foods){
			file.addString(f.getName(), 16);
			for(int b = 0; b<DietNumbers.SIZE; b++)file.addNumber(f.getStats().stats[b], 16);
		}
		file.addNumber(menu.size(), 16);
		for(FoodEntry f : menu)file.addNumber(foods.indexOf(f), 16);
		for(int b = 0; b<DietNumbers.SIZE; b++)file.addNumber(maxDietNumbers.stats[b], 16);
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
		byte fileVersion = (byte)file.getNumber(8);
		if(fileVersion==-128)for(int i = 0; i<16; i++)diet.stats[i]=(int)file.getNumber(16);
		else{
			//Idk how to read this...
		}
		file.stopReading();
		return diet;
	}
	public int getCurrentDay(){ return dayNumber; }
	public ArrayList<FoodEntry> loadFoodList(){ return foods; }
	public DietNumbers loadMaxDiet(){ return maxDietNumbers; }
	public DietNumbers loadTodaysStats(){ return currentDietNumbers; }
	public ArrayList<FoodEntry> getMenu(){ return menu; }
}