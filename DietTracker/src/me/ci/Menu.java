package me.ci;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JPanel;
import wraith.library.MiscUtil.StringUtil;

@SuppressWarnings("serial")
public class Menu extends JPanel{
	private Font font, font1;
	private ArrayList<String> items = new ArrayList<>();
	private Color darkGray;
	private static final int WORD_WRAP_LENGTH = 45;
	public Menu(){
		font=new Font("Tahoma", Font.PLAIN, 12);
		font1=new Font("Tahoma", Font.BOLD, 15);
		darkGray=new Color(0.1f, 0.1f, 0.1f);
		setPreferredSize(new Dimension(300, 300));
		reload();
	}
	public void reload(){
		HashMap<FoodEntry,Integer> itemCounts = new HashMap<>();
		for(FoodEntry f : Loader.getResourceLoader().getMenu()){
			if(itemCounts.containsKey(f))itemCounts.put(f, itemCounts.get(f)+1);
			else itemCounts.put(f, 1);
		}
		items.clear();
		for(FoodEntry s : itemCounts.keySet()){
			String a = "";
			for(char c : StringUtil.wrap("x"+itemCounts.get(s)+" "+s.getName(), WORD_WRAP_LENGTH, true).toCharArray()){
				if(c=='\n'){
					items.add(a);
					a="     ";
				}else a+=c;
			}
			items.add(a);
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