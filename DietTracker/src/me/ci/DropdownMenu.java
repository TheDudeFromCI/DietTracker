package me.ci;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class DropdownMenu{
	private BufferedImage img;
	private Graphics2D g;
	private int[][] fontCenters;
	private String[] entries;
	private int index;
	private boolean open;
	private ArrayList<FoodEntry> temp = new ArrayList<>();
	private static BufferedImage scrollbarClosed, scrollbarOpenTop, scrollbarOpenEntry, scrollbarOpenBottom;
	private static final Font FONT = new Font("Tahoma", Font.PLAIN, 25);
	static{
		try{
			scrollbarClosed=ImageIO.read(DropdownMenu.class.getResource("Scrollbar Closed.png"));
			scrollbarOpenTop=ImageIO.read(DropdownMenu.class.getResource("Scrollbar Top Open.png"));
			scrollbarOpenEntry=ImageIO.read(DropdownMenu.class.getResource("Scrollbar Open Entry.png"));
			scrollbarOpenBottom=ImageIO.read(DropdownMenu.class.getResource("Scrollbar Open Bottom.png"));
		}catch(Exception exception){ exception.printStackTrace(); }
	}
	public BufferedImage render(){
		g.dispose();
		img=new BufferedImage(250, (entries.length+1)*25, BufferedImage.TYPE_INT_ARGB);
		g=img.createGraphics();
		g.setFont(FONT);
		g.setColor(Color.BLACK);
		if(open){
			for(int i = -1; i<entries.length; i++){
				if(i==-1){
					g.drawImage(scrollbarOpenTop, 0, 0, null);
					g.drawString(entries[index], fontCenters[index][0], fontCenters[index][1]);
				}else{
					g.drawImage(i==entries.length-1?scrollbarOpenBottom:scrollbarOpenEntry, 0, (i+1)*25, null);
					g.drawString(entries[i], fontCenters[i][0], fontCenters[i][1]+(i+1)*25);
				}
			}
		}else{
			g.drawImage(scrollbarClosed, 0, 0, null);
			g.drawString(entries[index], fontCenters[index][0], fontCenters[index][1]);
		}
		return img;
	}
	public boolean overlaps(int offsetX, int x, int y){
		if(x>=offsetX&&x<offsetX+250){
			if(open)return y>=0&&y<img.getHeight();
			return y>=0&&y<25;
		}
		return false;
	}
	public int indexAt(int y){
		if(y<25)return index;
		return (y-25)/25;
	}
	public void rebuild(String[] entries){
		this.entries=entries;
		img=new BufferedImage(250, (entries.length+1)*25, BufferedImage.TYPE_INT_ARGB);
		g=img.createGraphics();
		g.setFont(FONT);
		fontCenters=new int[entries.length][2];
		FontMetrics fm = g.getFontMetrics();
		for(int i = 0; i<entries.length; i++){
			fontCenters[i][0]=(250-fm.stringWidth(entries[i]))/2;
			fontCenters[i][1]=(fm.getAscent()+(25-(fm.getAscent()+fm.getDescent()))/2);
		}
	}
	public ArrayList<FoodEntry> getFilteredList(ArrayList<FoodEntry> foods, boolean filterReds){
		temp.clear();
		for(FoodEntry food : foods)if((index==0||food.getCategory().equals(entries[index]))&&(!filterReds||food.getRemaining(Loader.getResourceLoader().loadTodaysStats(), Loader.getResourceLoader().loadMaxDiet())!=0))temp.add(food);
		return temp;
	}
	public DropdownMenu(String[] entries){ rebuild(entries); }
	public void setOpen(boolean open){ this.open=open; }
	public boolean isOpen(){ return open; }
	public void setIndex(int index){ this.index=index; }
	public int getIndex(){ return index; }
	public int w(){ return img.getWidth(); }
	public int h(){ return img.getHeight(); }
}