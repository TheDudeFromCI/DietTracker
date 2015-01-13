package me.ci;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class FoodEntry{
	private String name;
	private DietNumbers stats;
	private BufferedImage img;
	private Graphics2D g;
	private Font font;
	private static Color darkGray, lightGray;
	private static Color[] colors;
	public static final int BAR_MAX_WIDTH = 200;
	public static final int POST_BAR_BUFFER = 50;
	static{
		darkGray=new Color(0.1f, 0.1f, 0.1f);
		lightGray=new Color(0.2f, 0.2f, 0.2f);
		colors=new Color[DietNumbers.SIZE];
		for(int i = 0; i<colors.length; i++)colors[i]=new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
	}
	public FoodEntry(String name){
		this.name=name;
		stats=new DietNumbers();
		img=new BufferedImage(BAR_MAX_WIDTH+POST_BAR_BUFFER, 15*DietNumbers.SIZE, BufferedImage.TYPE_INT_ARGB);
		g=img.createGraphics();
		font=new Font("Tahoma", Font.ITALIC|Font.BOLD, 13);
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
	public BufferedImage graph(){
		paint();
		return img;
	}
	private void paint(){
		g.setFont(font);
		g.setColor(darkGray);
		g.fillRect(0, 0, BAR_MAX_WIDTH, img.getHeight());
		g.setColor(lightGray);
		g.fillRect(BAR_MAX_WIDTH, 0, POST_BAR_BUFFER, img.getHeight());
		double width = img.getHeight()/(double)DietNumbers.SIZE;
		for(int i = 0; i<DietNumbers.SIZE; i++){
			g.setColor(colors[i]);
			float percent = percent(i);
			if(Float.isInfinite(percent))percent=0;
			int round = Math.round(percent*100);
			if(round>0){
				g.fillRect(0, (int)(i*width), (int)(BAR_MAX_WIDTH*Math.min(percent, 1)), (int)width);
				g.setColor(Color.WHITE);
				if(percent>0)g.drawString(round+"%", (int)(BAR_MAX_WIDTH*Math.min(percent, 1))+3, (int)((i+1)*width-3));
			}
		}
	}
	private float percent(int index){ return stats.stats[index]/(float)Loader.getResourceLoader().loadMaxDiet().stats[index]; }
	@Override public boolean equals(Object o){ return o instanceof FoodEntry&&((FoodEntry)o).stats.equals(stats); }
	public String getName(){ return name; }
	public DietNumbers getStats(){ return stats; }
	public void setName(String name){ this.name=name; }
}