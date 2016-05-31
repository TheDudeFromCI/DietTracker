package me.ci;

import java.util.ArrayList;
import me.ci.util.CompactBinaryFile;
import me.ci.util.DietNumbers;
import me.ci.util.LogFile;
import me.ci.util.comps.FoodEntry;

public class ResourceLoader{
	public static final byte FILE_VERSION = -125;
	private ArrayList<FoodEntry> foods = new ArrayList<>(16);
	private ArrayList<FoodEntry> menu = new ArrayList<>(16);
	private DietNumbers maxDietNumbers = new DietNumbers();
	private DietNumbers currentDietNumbers = new DietNumbers();
	private CompactBinaryFile file;
	private short dayNumber = 0;
	private int weight = -1;
	private int sleep = -1;
	private int water = -1;
	public ResourceLoader(){
		file = new CompactBinaryFile("Config.dat");
		if(!file.exists()){
			try{
				file.createNewFile();
			}catch(Exception exception){
				exception.printStackTrace();
			}
		}
		file.read();
		if(!file.hasFinished()){
			byte version = (byte)file.getNumber(8);
			System.out.println("Loading file version: "+(version+130));
			switch(version){
				case -128:
				case -127:
					throw new RuntimeException("Unsupported file version!");
				case -126:
					loadFileVersion4(file);
					break;
				case -125:
					loadFileVersion5(file);
					break;
				default:
					throw new RuntimeException("Unabled to file file!");
			}
		}
		System.out.println(dayNumber);
		file.stopReading();
		recountTodaysStats();
	}
	private void loadFileVersion5(CompactBinaryFile file){
		dayNumber = (short)file.getNumber(9);
		int entries = (int)file.getNumber(16);
		for(int a = 0; a<entries; a++){
			String uuid = file.getString(5);
			FoodEntry f = new FoodEntry(file.getString(16), uuid);
			f.setCetegory(file.getString(8));
			for(int b = 0; b<17; b++){
				f.getStats().stats[b] = (int)file.getNumber(16);
			}
			foods.add(f);
		}
		entries = (int)file.getNumber(16);
		for(int a = 0; a<entries; a++){
			menu.add(foods.get((int)file.getNumber(16)));
		}
		for(int b = 0; b<17; b++){
			maxDietNumbers.stats[b] = (int)file.getNumber(16);
		}
		weight = (int)file.getNumber(16);
		sleep = (int)file.getNumber(8);
		water = (int)file.getNumber(16);
	}
	private void loadFileVersion4(CompactBinaryFile file){
		dayNumber = (short)file.getNumber(9);
		int entries = (int)file.getNumber(16);
		for(int a = 0; a<entries; a++){
			FoodEntry f = new FoodEntry(file.getString(16), null);
			f.setCetegory(file.getString(8));
			f.getStats().stats[0] = (int)file.getNumber(16);
			f.getStats().stats[1] = (int)file.getNumber(16);
			f.getStats().stats[2] = (int)file.getNumber(16);
			f.getStats().stats[3] = (int)file.getNumber(16);
			f.getStats().stats[6] = (int)file.getNumber(16);
			f.getStats().stats[7] = (int)file.getNumber(16);
			f.getStats().stats[8] = (int)file.getNumber(16);
			f.getStats().stats[9] = (int)file.getNumber(16);
			f.getStats().stats[10] = (int)file.getNumber(16);
			f.getStats().stats[11] = (int)file.getNumber(16);
			f.getStats().stats[12] = (int)file.getNumber(16);
			f.getStats().stats[13] = (int)file.getNumber(16);
			f.getStats().stats[14] = (int)file.getNumber(16);
			f.getStats().stats[15] = (int)file.getNumber(16);
			f.getStats().stats[16] = (int)file.getNumber(16);
			foods.add(f);
		}
		entries = (int)file.getNumber(16);
		for(int a = 0; a<entries; a++){
			menu.add(foods.get((int)file.getNumber(16)));
		}
		maxDietNumbers.stats[0] = (int)file.getNumber(16);
		maxDietNumbers.stats[1] = (int)file.getNumber(16);
		maxDietNumbers.stats[2] = (int)file.getNumber(16);
		maxDietNumbers.stats[3] = (int)file.getNumber(16);
		maxDietNumbers.stats[6] = (int)file.getNumber(16);
		maxDietNumbers.stats[7] = (int)file.getNumber(16);
		maxDietNumbers.stats[8] = (int)file.getNumber(16);
		maxDietNumbers.stats[9] = (int)file.getNumber(16);
		maxDietNumbers.stats[10] = (int)file.getNumber(16);
		maxDietNumbers.stats[11] = (int)file.getNumber(16);
		maxDietNumbers.stats[12] = (int)file.getNumber(16);
		maxDietNumbers.stats[13] = (int)file.getNumber(16);
		maxDietNumbers.stats[14] = (int)file.getNumber(16);
		maxDietNumbers.stats[15] = (int)file.getNumber(16);
		maxDietNumbers.stats[16] = (int)file.getNumber(16);
	}
	public void save(){
		file.write();
		file.addNumber(FILE_VERSION, 8);
		file.addNumber(dayNumber, 9);
		file.addNumber(foods.size(), 16);
		for(FoodEntry f : foods){
			file.addString(f.getUUID(), 5);
			file.addString(f.getName(), 16);
			file.addString(f.getCategory(), 8);
			for(int b = 0; b<DietNumbers.SIZE; b++){
				file.addNumber(f.getStats().stats[b], 16);
			}
		}
		file.addNumber(menu.size(), 16);
		for(FoodEntry f : menu){
			file.addNumber(foods.indexOf(f), 16);
		}
		for(int b = 0; b<DietNumbers.SIZE; b++){
			file.addNumber(maxDietNumbers.stats[b], 16);
		}
		file.addNumber(weight, 16); // Current weight.
		file.addNumber(sleep, 8); // Current sleep.
		file.addNumber(water, 16); // Current weight.
		file.stopWriting();
		logDay();
	}
	private void logDay(){
		CompactBinaryFile bin = new CompactBinaryFile("Log-"+dayNumber+".dat");
		bin.ensureExistance();
		bin.write();
		bin.addNumber(FILE_VERSION, 8);
		for(int a = 0; a<DietNumbers.SIZE; a++){
			bin.addNumber(currentDietNumbers.stats[a], 16);
		}
		bin.addNumber(menu.size(), 14);
		for(FoodEntry food : menu){
			bin.addString(food.getUUID(), 5);
		}
		bin.addNumber(weight, 16);
		bin.addNumber(sleep, 8);
		bin.addNumber(water, 16);
		bin.stopWriting();
	}
	public void newDay(){
		save();
		menu.clear();
		currentDietNumbers.clear();
		dayNumber = (short)(dayNumber+1);
		weight = -1;
		sleep = -1;
		water = -1;
		save();
	}
	public void recountTodaysStats(){
		for(int b = 0; b<DietNumbers.SIZE; b++){
			currentDietNumbers.stats[b] = 0;
		}
		for(FoodEntry f : menu){
			for(int b = 0; b<DietNumbers.SIZE; b++){
				currentDietNumbers.stats[b] += f.getStats().stats[b];
			}
		}
	}
	public LogFile getLog(int day, boolean loadBlank){
		if(day==dayNumber){
			LogFile log = new LogFile(currentDietNumbers);
			return log;
		}
		DietNumbers diet = new DietNumbers();
		LogFile log = new LogFile(diet);
		if(day<0){
			return loadBlank?log:null;
		}
		CompactBinaryFile bin = new CompactBinaryFile("Log-"+day+".dat");
		if(!bin.exists()){
			return loadBlank?log:null;
		}
		bin.read();
		if(bin.hasFinished()){
			return loadBlank?log:null;
		}
		byte fileVersion = (byte)bin.getNumber(8);
		switch(fileVersion){
			case -128:
				diet.stats[3] = (int)bin.getNumber(16);
				diet.stats[6] = (int)bin.getNumber(16);
				diet.stats[8] = (int)bin.getNumber(16);
				diet.stats[9] = (int)bin.getNumber(16);
				diet.stats[0] = (int)bin.getNumber(16);
				diet.stats[2] = (int)bin.getNumber(16);
				diet.stats[7] = (int)bin.getNumber(16);
				diet.stats[13] = (int)bin.getNumber(16);
				diet.stats[1] = (int)bin.getNumber(16);
				diet.stats[10] = (int)bin.getNumber(16);
				diet.stats[12] = (int)bin.getNumber(16);
				diet.stats[11] = (int)bin.getNumber(16);
				diet.stats[14] = (int)bin.getNumber(16);
				diet.stats[15] = (int)bin.getNumber(16);
				diet.stats[16] = (int)bin.getNumber(16);
				break;
			case -127:
			case -126:
				diet.stats[0] = (int)bin.getNumber(16);
				diet.stats[1] = (int)bin.getNumber(16);
				diet.stats[2] = (int)bin.getNumber(16);
				diet.stats[3] = (int)bin.getNumber(16);
				diet.stats[6] = (int)bin.getNumber(16);
				diet.stats[7] = (int)bin.getNumber(16);
				diet.stats[8] = (int)bin.getNumber(16);
				diet.stats[9] = (int)bin.getNumber(16);
				diet.stats[10] = (int)bin.getNumber(16);
				diet.stats[11] = (int)bin.getNumber(16);
				diet.stats[12] = (int)bin.getNumber(16);
				diet.stats[13] = (int)bin.getNumber(16);
				diet.stats[14] = (int)bin.getNumber(16);
				diet.stats[15] = (int)bin.getNumber(16);
				diet.stats[16] = (int)bin.getNumber(16);
				break;
			case -125:
				for(int i = 0; i<17; i++){
					diet.stats[i] = (int)bin.getNumber(16);
				}
				int i = (int)bin.getNumber(14);
				for(int a = 0; a<i; a++){
					log.getFoodsEaten().add(bin.getString(5));
				}
				log.setWeight((int)bin.getNumber(16));
				log.setSleep((int)bin.getNumber(8));
				log.setWater((int)bin.getNumber(16));
				break;
			default:
				throw new RuntimeException("Unknown file version!");
		}
		bin.stopReading();
		return log;
	}
	public int getCurrentDay(){
		return dayNumber;
	}
	public ArrayList<FoodEntry> loadFoodList(){
		return foods;
	}
	public DietNumbers loadMaxDiet(){
		return maxDietNumbers;
	}
	public DietNumbers loadTodaysStats(){
		return currentDietNumbers;
	}
	public ArrayList<FoodEntry> getMenu(){
		return menu;
	}
	public int getWeight(){
		return weight;
	}
	public void setWeight(int weight){
		this.weight = weight;
	}
	public int getSleep(){
		return sleep;
	}
	public int getWater(){
		return water;
	}
	public void setSleep(int sleep){
		this.sleep = sleep;
	}
	public void setWater(int water){
		this.water = water;
	}
}
