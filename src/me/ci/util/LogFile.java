/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose Tools | Templates and open the
 * template in the editor.
 */
package me.ci.util;

import java.util.ArrayList;

/**
 * @author thedudefromci
 */
public class LogFile{
	private final ArrayList<String> foodsEaten = new ArrayList(8);
	private final DietNumbers numbers;
	private int weight;
	private int sleep;
	private int water;
	public LogFile(DietNumbers numbers){
		this.numbers = numbers;
	}
	public LogFile(){
		numbers = new DietNumbers();
	}
	public DietNumbers getNumbers(){
		return numbers;
	}
	public ArrayList<String> getFoodsEaten(){
		return foodsEaten;
	}
	public void setWeight(int weight){
		this.weight = weight;
	}
	public int getWeight(){
		return weight;
	}
	public void setSleep(int sleep){
		this.sleep = sleep;
	}
	public int getSleep(){
		return sleep;
	}
	public void setWater(int water){
		this.water = water;
	}
	public int getWater(){
		return water;
	}
}
