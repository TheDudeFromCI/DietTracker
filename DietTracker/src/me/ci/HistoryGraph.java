package me.ci;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class HistoryGraph extends JPanel{
	private float[] values = new float[7];
	private int usedStat = 0;
	private int hover = -1;
	private static BufferedImage checkboxFalse, checkboxTrue, checkboxHover;
	private static final int SIDEBAR_WIDTH = 150;
	private static final int MINIMUM_HEIGHT = 400;
	private static final int STAT_NAME_HEIGHT = 30;
	private static final int CHECKBOX_SIZE = 25;
	private static final Color DARK_GRAY = new Color(0.15f, 0.15f, 0.15f);
	private static final Color LIGHT_GRAY = new Color(0.17f, 0.17f, 0.17f);
	private static final Font NAME_FONT = new Font("Tahoma", Font.PLAIN, 20);
	private static final Font WARNING_FONT = new Font("Tahoma", Font.BOLD, 20);
	static{
		try{
			checkboxFalse=ImageIO.read(HistoryGraph.class.getResource("Checkbox False.png"));
			checkboxTrue=ImageIO.read(HistoryGraph.class.getResource("Checkbox True.png"));
			checkboxHover=ImageIO.read(HistoryGraph.class.getResource("Checkbox Hover.png"));
		}catch(Exception exception){
			exception.printStackTrace();
			System.exit(1);
		}
	}
	public HistoryGraph(){
		addMouseMotionListener(new MouseAdapter(){
			@Override public void mouseMoved(MouseEvent e){
				int x = e.getX();
				int y = e.getY();
				int w;
				for(int i = 0; i<DietNumbers.SIZE; i++){
					w=i*STAT_NAME_HEIGHT+(STAT_NAME_HEIGHT-CHECKBOX_SIZE)/2+7;
					if(y>=w&&y<w+CHECKBOX_SIZE&&x>=5&&x<5+CHECKBOX_SIZE){
						hover=i;
						repaint();
						return;
					}
				}
				hover=-1;
				repaint();
			}
		});
		addMouseListener(new MouseAdapter(){
			@Override public void mousePressed(MouseEvent e){
				if(hover!=-1){
					usedStat=hover;
					recalculateValues();
				}
			}
		});
		recalculateValues();
	}
	@Override public void paint(Graphics g1){
		Graphics2D g = (Graphics2D)g1;
		if(getHeight()<MINIMUM_HEIGHT){
			g.setColor(LIGHT_GRAY);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.WHITE);
			g.setFont(WARNING_FONT);
			FontMetrics fm = g.getFontMetrics();
			String s = "Please expand window to view.";
			int x = (getWidth()-fm.stringWidth(s))/2;
			int y = (fm.getAscent()+(getHeight()-(fm.getAscent()+fm.getDescent()))/2);
			g.drawString(s, x, y);
		}else{
			g.setColor(LIGHT_GRAY);
			g.fillRect(0, 0, SIDEBAR_WIDTH, getHeight());
			g.setColor(DARK_GRAY);
			g.fillRect(SIDEBAR_WIDTH, 0, getWidth()-SIDEBAR_WIDTH, getHeight());
			g.setColor(Color.WHITE);
			g.setFont(NAME_FONT);
			for(int i = 0; i<DietNumbers.SIZE; i++){
				g.drawImage(usedStat==i?checkboxTrue:checkboxFalse, 5, i*STAT_NAME_HEIGHT+(STAT_NAME_HEIGHT-CHECKBOX_SIZE)/2+7, null);
				if(hover==i)g.drawImage(checkboxHover, 5, i*STAT_NAME_HEIGHT+(STAT_NAME_HEIGHT-CHECKBOX_SIZE)/2+7, null);
				g.drawString(DietNumbers.NAMES[i], CHECKBOX_SIZE+10, (i+1)*STAT_NAME_HEIGHT);
			}
		}
		g.dispose();
	}
	private void recalculateValues(){
		for(int i = 0; i<values.length; i++)values[i]=Loader.getResourceLoader().getLog(Loader.getResourceLoader().getCurrentDay()-i).stats[usedStat];
		repaint();
	}
}