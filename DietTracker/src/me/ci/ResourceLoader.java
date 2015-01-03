package me.ci;

import java.io.File;
import java.util.ArrayList;

public class ResourceLoader{
	private ArrayList<FoodEntry> foods = new ArrayList<>();
	private ArrayList<FoodEntry> menu = new ArrayList<>();
	private DietNumbers maxDietNumbers = new DietNumbers();
	private DietNumbers currentDietNumbers = new DietNumbers();
	private File file;
	public ResourceLoader(){
		file=new File("Config.dat");
		if(file.exists()){
			byte[] b = BinaryFileUtil.readFile(file);
			int[] lengths = getLengths(b);
			int q = 8;
			byte[] buf = new byte[4];
			buf[0]=b[8];
			buf[1]=b[9];
			buf[2]=b[10];
			buf[3]=b[11];
			int foodEntries = BinaryFileUtil.byteArrayToInteger(buf);
			for(int i = 0; i<foodEntries; i++){
				buf[0]=b[q];
				buf[1]=b[q+1];
				buf[2]=b[q+2];
				buf[3]=b[q+3];
				q+=4;
				byte[] name = new byte[BinaryFileUtil.byteArrayToInteger(buf)];
				for(int a = 0; a<name.length; a++){
					name[a]=b[q];
					q++;
				}
				FoodEntry foodEntry = new FoodEntry(new String(name));
				getDietStats(b, q, foodEntry.getStats().stats);
				q+=DietNumbers.SIZE*4;
			}
			for(int i = 0; i<lengths[1]; i++){
				//menu
				q++;
			}
			for(int i = 0; i<DietNumbers.SIZE*4; i++){
				//max
				q++;
			}
			FoodEntry foodEntry;
			for(int i = 0; i<menu.size(); i++){
				foodEntry=menu.get(i);
				for(int j = 0; j<DietNumbers.SIZE; j++)currentDietNumbers.stats[j]+=foodEntry.getStats().stats[j];
			}
		}else{
			try{ file.createNewFile();
			}catch(Exception exception){ exception.printStackTrace(); }
		}
	}
	private int[] getLengths(byte[] b){
		int[] a = new int[2];
		byte[] buf = new byte[4];
		buf[0]=b[0];
		buf[1]=b[1];
		buf[2]=b[2];
		buf[3]=b[3];
		a[0]=BinaryFileUtil.byteArrayToInteger(buf);
		buf[0]=b[4];
		buf[1]=b[5];
		buf[2]=b[6];
		buf[3]=b[7];
		a[1]=BinaryFileUtil.byteArrayToInteger(buf);
		return a;
	}
	private void getDietStats(byte[] b, int pos, int[] a){
		byte[] buf = new byte[4];
		for(int c = 0; c<a.length; c++){
			buf[0]=b[c*4+pos];
			buf[1]=b[c*4+1+pos];
			buf[2]=b[c*4+2+pos];
			buf[3]=b[c*4+3+pos];
			a[c]=BinaryFileUtil.byteArrayToInteger(buf);
		}
	}
	public void save(){
		//TODO Save.
	}
	public ArrayList<FoodEntry> loadFoodList(){ return foods; }
	public DietNumbers loadMaxDiet(){ return maxDietNumbers; }
	public DietNumbers loadTodaysStats(){ return currentDietNumbers; }
}