package me.ci.tabs;

import java.awt.Color;
import java.awt.Font;
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
import me.ci.popups.LogWeightPopup;
import me.ci.util.LogFile;

@SuppressWarnings("serial")
public class SleepTracker extends JPanel{
	private int[] values;
	private int maxValue;
	private boolean updateWeightHover;
	private static BufferedImage updateWeightButton, updateWeightButtonHover;
	private static final int BOTTOM_BORDER_THICKNESS = 40;
	private static final Color DARK_GRAY = new Color(0.1f, 0.1f, 0.1f);
	private static final Color LIGHT_GRAY = new Color(0.2f, 0.2f, 0.2f);
	private static final Font INFO_FONT = new Font("Tahoma", Font.BOLD, 20);
	static{
		try{
			updateWeightButton = ImageIO.read(SleepTracker.class.getResource("/assets/Update Weight Button.png"));
			updateWeightButtonHover = ImageIO.read(SleepTracker.class.getResource("/assets/Update Weight Button Hover.png"));
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}
	public SleepTracker(){
		addMouseMotionListener(new MouseAdapter(){
			@Override
			public void mouseMoved(MouseEvent e){
				int x = e.getX();
				int y = e.getY();
				boolean before = updateWeightHover;
				updateWeightHover = y>=getHeight()-BOTTOM_BORDER_THICKNESS+3&&y<getHeight()-3&&x>=getWidth()-78&&x<getWidth()-3;
				if(updateWeightHover!=before){
					repaint();
				}
			}
		});
		addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				if(updateWeightHover){
					new LogWeightPopup();
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
				String list = (values[0]/10)+"."+(values[0]%10);
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
						String list = (values[i]/10)+"."+(values[i]%10);
						g.drawString(list, (int)(points*i+points/2-fm.getStringBounds(list, g).getWidth()/2),
							(int)(percent2*(getHeight()-BOTTOM_BORDER_THICKNESS))-25);
					}else{
						double percent = 1-values[i]/max;
						g.setColor(Color.WHITE);
						String list = (values[i]/10)+"."+(values[i]%10);
						g.drawString(list, (int)(points*i+points/2-fm.getStringBounds(list, g).getWidth()/2),
							(int)(percent*(getHeight()-BOTTOM_BORDER_THICKNESS))-25);
					}
				}
			}
		}
		g.drawImage(updateWeightHover?updateWeightButtonHover:updateWeightButton, getWidth()-78, getHeight()-BOTTOM_BORDER_THICKNESS+3, null);
		g.setFont(INFO_FONT);
		int num;
		if(values.length>0){
			num = values[0]-values[values.length-1];
		}else{
			num = 0;
		}
		g.drawString("Total Weight Lost: "+(num/10)+"."+(num%10), 5, getHeight()-10);
		g.dispose();
	}
	public void logWeight(int weight){
		values[values.length-1] = weight;
		maxValue = 0;
		for(int i = 0; i<values.length; i++){
			maxValue = Math.max(maxValue, values[i]);
		}
		repaint();
		Loader.getResourceLoader().setWeights(weight);
		Loader.getResourceLoader().save();
	}
	public void recalculateValues(){
		ArrayList<Integer> weights = new ArrayList(32);
		weights.add(Loader.getResourceLoader().getWeight());
		for(int i = Loader.getResourceLoader().getCurrentDay()-1; i>=0; i--){
			LogFile log = Loader.getResourceLoader().getLog(i, false);
			if(log==null||log.getWeight()==0){
				break;
			}
			weights.add(log.getWeight());
		}
		values = new int[weights.size()];
		maxValue = 0;
		for(int i = 0; i<values.length; i++){
			values[i] = weights.get(values.length-1-i);
			maxValue = Math.max(maxValue, values[i]);
		}
		repaint();
	}
}
