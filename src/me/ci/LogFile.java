/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose Tools | Templates and open the
 * template in the editor.
 */
package me.ci;

import java.util.ArrayList;

/**
 * @author thedudefromci
 */
public class LogFile{
	private final ArrayList<String> foodsEaten = new ArrayList(8);
	private final DietNumbers numbers;
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
}