package me.ci.tabs;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import me.ci.Loader;
import me.ci.popups.LogValuePopup;
import me.ci.util.LogFile;

@SuppressWarnings("serial")
public class WaterTracker extends JPanel{
	private int[] values;
	private int maxValue;
	private boolean updateHover;
	private static BufferedImage button, buttonHover;
	private static final int BOTTOM_BORDER_THICKNESS = 40;
	private static final Color DARK_GRAY = new Color(0.1f, 0.1f, 0.1f);
	private static final Color LIGHT_GRAY = new Color(0.2f, 0.2f, 0.2f);
	static{
		try{
			button = ImageIO.read(WaterTracker.class.getResource("/assets/Update Weight Button.png"));
			buttonHover = ImageIO.read(WaterTracker.class.getResource("/assets/Update Weight Button Hover.png"));
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}
	public WaterTracker(){
		addMouseMotionListener(new MouseAdapter(){
			@Override
			public void mouseMoved(MouseEvent e){
				int x = e.getX();
				int y = e.getY();
				boolean before = updateHover;
				updateHover = y>=getHeight()-BOTTOM_BORDER_THICKNESS+3&&y<getHeight()-3&&x>=getWidth()-78&&x<getWidth()-3;
				if(updateHover!=before){
					repaint();
				}
			}
		});
		addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				if(updateHover){
					new LogValuePopup("Enter Today's Water", "Today's Water (oz)", 32, 4);
				}
			}
		});
		recalculateValues();
	}
	@Override
	public void paint(Graphics g1){
		Graphics2D g = (Graphics2D)g1;
		g.setColor(DARK_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight()-BOTTOM_BORDER_THICKNESS);
		g.setColor(LIGHT_GRAY);
		g.fillRect(0, getHeight()-BOTTOM_BORDER_THICKNESS, getWidth(), BOTTOM_BORDER_THICKNESS);
		if(values.length>0){
			double points = getWidth()/(double)values.length;
			double max = maxValue*1.2;
			g.setColor(Color.DARK_GRAY);
			double pointsV = (getHeight()-BOTTOM_BORDER_THICKNESS)/15.0;
			for(int i = 1; i<=15; i++){
				g.drawLine(0, (int)(pointsV*i), getWidth(), (int)(pointsV*i));
			}
			if(values.length==1){
				double percent = 1-values[0]/max;
				g.setColor(Color.GREEN);
				g.drawOval(getWidth()/2-3, (int)(percent*(getHeight()-BOTTOM_BORDER_THICKNESS))-3, 6, 6);
				FontMetrics fm = g.getFontMetrics();
				g.setColor(Color.WHITE);
				String list = values[0]+"";
				g.drawString(list, (int)(getWidth()/2-fm.getStringBounds(list, g).getWidth()/2),
					(int)(percent*(getHeight()-BOTTOM_BORDER_THICKNESS))-25);
			}else{
				FontMetrics fm = g.getFontMetrics();
				for(int i = 0; i<values.length; i++){
					g.setColor(Color.DARK_GRAY);
					g.drawLine((int)(points*i+points/2), 0, (int)(points*i+points/2), getHeight()-BOTTOM_BORDER_THICKNESS);
					if(i>0){
						double percent1 = 1-values[i-1]/max;
						double percent2 = 1-values[i]/max;
						g.setColor(Color.GREEN);
						g.drawLine((int)(points*(i-1)+points/2), (int)(percent1*(getHeight()-BOTTOM_BORDER_THICKNESS)), (int)(points*i+points/2),
							(int)(percent2*(getHeight()-BOTTOM_BORDER_THICKNESS)));
						g.setColor(Color.WHITE);
						String list = values[0]+"";
						g.drawString(list, (int)(points*i+points/2-fm.getStringBounds(list, g).getWidth()/2),
							(int)(percent2*(getHeight()-BOTTOM_BORDER_THICKNESS))-25);
					}else{
						double percent = 1-values[i]/max;
						g.setColor(Color.WHITE);
						String list = values[0]+"";
						g.drawString(list, (int)(points*i+points/2-fm.getStringBounds(list, g).getWidth()/2),
							(int)(percent*(getHeight()-BOTTOM_BORDER_THICKNESS))-25);
					}
				}
			}
		}
		g.drawImage(updateHover?buttonHover:button, getWidth()-78, getHeight()-BOTTOM_BORDER_THICKNESS+3, null);
		g.dispose();
	}
	public void logWater(int water){
		values[values.length-1] = water;
		maxValue = 0;
		for(int i = 0; i<values.length; i++){
			maxValue = Math.max(maxValue, values[i]);
		}
		repaint();
		Loader.getResourceLoader().setWater(water);
		Loader.getResourceLoader().save();
	}
	public void recalculateValues(){
		ArrayList<Integer> valueList = new ArrayList(7);
		valueList.add(Loader.getResourceLoader().getWater());
		int day = Loader.getResourceLoader().getCurrentDay()-1;
		int back = day-7;
		for(int i = day; i>=back; i--){
			LogFile log = Loader.getResourceLoader().getLog(i, false);
			if(log==null||log.getWeight()==0){
				break;
			}
			valueList.add(log.getWater());
		}
		values = new int[valueList.size()];
		maxValue = 0;
		for(int i = 0; i<values.length; i++){
			values[i] = valueList.get(values.length-1-i);
			maxValue = Math.max(maxValue, values[i]);
		}
		repaint();
	}
}
