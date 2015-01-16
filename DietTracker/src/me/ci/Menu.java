package me.ci;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import wraith.library.MiscUtil.StringUtil;

@SuppressWarnings("serial")
public class Menu extends JPanel{
	private Font font, font1;
	private ArrayList<String> items = new ArrayList<>();
	private Color darkGray;
	private boolean hover;
	private static BufferedImage undoButton, undoButtonHover;
	private static final int UNDO_BUTTON_EDGE_DISTANCE = 37;
	static{
		try{
			undoButton=ImageIO.read(Menu.class.getResource("Undo Button.png"));
			undoButtonHover=ImageIO.read(Menu.class.getResource("Undo Button Hover.png"));
		}catch(Exception exception){ exception.printStackTrace(); }
	}
	public Menu(){
		font=new Font("Tahoma", Font.PLAIN, 15);
		font1=new Font("Tahoma", Font.BOLD, 20);
		darkGray=new Color(0.1f, 0.1f, 0.1f);
		setPreferredSize(new Dimension(350, 300));
		addMouseMotionListener(new MouseAdapter(){
			@Override public void mouseMoved(MouseEvent e){
				boolean hoverBefore = hover;
				int x = e.getX();
				int y = e.getY();
				hover=x>=getWidth()-UNDO_BUTTON_EDGE_DISTANCE&&x<getWidth()-UNDO_BUTTON_EDGE_DISTANCE+30&&y>=getHeight()-UNDO_BUTTON_EDGE_DISTANCE&&y<getHeight()-UNDO_BUTTON_EDGE_DISTANCE+30;
				if(hover!=hoverBefore)repaint();
			}
		});
		addMouseListener(new MouseAdapter(){
			@Override public void mouseClicked(MouseEvent e){
				if(hover&&Loader.getResourceLoader().getMenu().size()>0){
					Loader.getResourceLoader().getMenu().remove(Loader.getResourceLoader().getMenu().size()-1);
					Loader.getResourceLoader().recountTodaysStats();
					Loader.getInstance().getCurrentStats().reload();
					Loader.getResourceLoader().save();
					reload();
				}
			}
		});
		addComponentListener(new ComponentListener(){
			public void componentShown(ComponentEvent paramComponentEvent){ reload(); }
			public void componentResized(ComponentEvent paramComponentEvent){ reload(); }
			public void componentMoved(ComponentEvent paramComponentEvent){ reload(); }
			public void componentHidden(ComponentEvent paramComponentEvent){ reload(); }
		});
		reload();
	}
	public void reload(){
		HashMap<FoodEntry,Integer> itemCounts = new HashMap<>();
		for(FoodEntry f : Loader.getResourceLoader().getMenu()){
			if(itemCounts.containsKey(f))itemCounts.put(f, itemCounts.get(f)+1);
			else itemCounts.put(f, 1);
		}
		items.clear();
		int wrapLength = (getWidth()-10)/9;
		for(FoodEntry s : itemCounts.keySet()){
			String a = "";
			for(char c : StringUtil.wrap("x"+itemCounts.get(s)+" "+s.getName(), wrapLength, true).toCharArray()){
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
		g.drawImage(hover?undoButtonHover:undoButton, getWidth()-UNDO_BUTTON_EDGE_DISTANCE, getHeight()-UNDO_BUTTON_EDGE_DISTANCE, null);
		g.dispose();
	}
}