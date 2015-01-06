package me.ci;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Menu extends JPanel{
	private Font font, font1;
	private String[] items;
	private Color darkGray;
	public Menu(){
		font=new Font("Tahoma", Font.PLAIN, 12);
		font1=new Font("Tahoma", Font.BOLD, 15);
		darkGray=new Color(0.1f, 0.1f, 0.1f);
		setPreferredSize(new Dimension(200, 500));
		reload();
	}
	public void reload(){
		HashMap<String,Integer> itemCounts = new HashMap<>();
		for(FoodEntry f : Loader.getResourceLoader().getMenu()){
			if(itemCounts.containsKey(f.getName()))itemCounts.put(f.getName(), itemCounts.get(f.getName())+1);
			else itemCounts.put(f.getName(), 1);
		}
		items=new String[itemCounts.size()];
		int index = 0;
		int c;
		for(String s : itemCounts.keySet()){
			c=itemCounts.get(s);
			items[index]=s+(c==1?"":" x"+c);
			index++;
		}
		repaint();
	}
	@Override public void paint(Graphics g1){
		Graphics2D g = (Graphics2D)g1;
		g.setColor(darkGray);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.DARK_GRAY);
		g.fillRect(5, 5, getWidth()-10, getHeight()-10);
		g.setColor(Color.WHITE);
		g.setFont(font1);
		Rectangle2D bounds = g.getFontMetrics().getStringBounds("Today's Menu", g);
		g.drawString("Today's Menu", (int)(getWidth()/2f-bounds.getWidth()/2f), 25);
		g.drawLine(20, 28, getWidth()-20, 28);
		g.setFont(font);
		int i = 1;
		for(String s : items){
			g.drawString(s, 10, 12*i+30);
			i++;
		}
		g.dispose();
	}
}