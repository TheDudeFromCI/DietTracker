package me.ci;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class WeightTracker extends JPanel{
	private int[] values;
	private int maxValue;
	private boolean updateWeightHover;
	private static BufferedImage updateWeightButton, updateWeightButtonHover;
	private static final int BOTTOM_BORDER_THICKNESS = 40;
	private static final Color DARK_GRAY = new Color(0.1f, 0.1f, 0.1f);
	private static final Color LIGHT_GRAY = new Color(0.2f, 0.2f, 0.2f);
	static{
		try{
			updateWeightButton=ImageIO.read(WeightTracker.class.getResource("Update Weight Button.png"));
			updateWeightButtonHover=ImageIO.read(WeightTracker.class.getResource("Update Weight Button Hover.png"));
		}catch(Exception exception){ exception.printStackTrace(); }
	}
	public WeightTracker(){
		addMouseMotionListener(new MouseAdapter(){
			@Override public void mouseMoved(MouseEvent e){
				int x = e.getX();
				int y = e.getY();
				boolean before = updateWeightHover;
				updateWeightHover=y>=getHeight()-BOTTOM_BORDER_THICKNESS+3&&y<getHeight()-3&&x>=getWidth()-78&&x<getWidth()-3;
				if(updateWeightHover!=before)repaint();
			}
		});
		recalculateValues();
	}
	@Override public void paint(Graphics g1){
		Graphics2D g = (Graphics2D)g1;
		g.setColor(DARK_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight()-BOTTOM_BORDER_THICKNESS);
		g.setColor(LIGHT_GRAY);
		g.fillRect(0, getHeight()-BOTTOM_BORDER_THICKNESS, getWidth(), BOTTOM_BORDER_THICKNESS);
		double points = getWidth()/(double)values.length;
		double max = maxValue*1.2;
		g.setColor(Color.DARK_GRAY);
		double pointsV = (getHeight()-BOTTOM_BORDER_THICKNESS)/15.0;
		for(int i = 1; i<=15; i++)g.drawLine(0, (int)(pointsV*i), getWidth(), (int)(pointsV*i));
		for(int i = 1; i<values.length; i++){
			double percent1 = 1-values[i-1]/max;
			double percent2 = 1-values[i]/max;
			g.setColor(Color.DARK_GRAY);
			g.drawLine((int)(points*i+points/2), 0, (int)(points*i+points/2), getHeight()-BOTTOM_BORDER_THICKNESS);
			g.setColor(Color.GREEN);
			g.drawLine((int)(points*(i-1)+points/2), (int)(percent1*(getHeight()-BOTTOM_BORDER_THICKNESS)), (int)(points*i+points/2), (int)(percent2*(getHeight()-BOTTOM_BORDER_THICKNESS)));
		}
		g.setColor(Color.WHITE);
		for(int i = 0; i<15; i++)g.drawString(i+"", 4, (int)(pointsV*i)+12);
		g.drawImage(updateWeightHover?updateWeightButtonHover:updateWeightButton, getWidth()-78, getHeight()-BOTTOM_BORDER_THICKNESS+3, null);
		g.dispose();
	}
	public void recalculateValues(){
		values=new int[]{10, 20, 30, 40, 50, 35, 10, 12, 34, 61, 30, 48};
		maxValue=61;
		repaint();
	}
}