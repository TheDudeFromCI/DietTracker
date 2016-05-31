package me.ci.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import me.ci.util.DietNumbers;
import me.ci.util.comps.DropdownMenu;
import me.ci.Loader;

@SuppressWarnings("serial")
public class HistoryGraph extends JPanel{
	private int[] values = new int[7];
	private int highestValue;
	private int spinnerHover = 0;
	private DropdownMenu dropdownMenu;
	private static BufferedImage spinner, spinnerLeft, spinnerRight;
	private static final int SIDEBAR_WIDTH = 150;
	private static final int STAT_NAME_HEIGHT = 25;
	private static final int CHECKBOX_SIZE = 25;
	private static final int LINE_GRAPH_TOP_BUFFER = 50;
	private static final int LINE_GRAPH_BOTTOM_BUFFER = 10;
	private static final Color DARK_GRAY = new Color(0.15f, 0.15f, 0.15f);
	private static final Color LIGHT_GRAY = new Color(0.17f, 0.17f, 0.17f);
	private static final Font SPINNER_FONT = new Font("Tahoma", Font.BOLD, 15);
	private static final Font DAY_COUNT_FONT = new Font("Tahoma", Font.PLAIN, 13);
	static{
		try{
			spinner = ImageIO.read(HistoryGraph.class.getResource("/assets/Spinner.png"));
			spinnerLeft = ImageIO.read(HistoryGraph.class.getResource("/assets/Spinner Left.png"));
			spinnerRight = ImageIO.read(HistoryGraph.class.getResource("/assets/Spinner Right.png"));
		}catch(Exception exception){
			exception.printStackTrace();
			System.exit(1);
		}
	}
	public HistoryGraph(){
		addMouseMotionListener(new MouseAdapter(){
			@Override
			public void mouseMoved(MouseEvent e){
				int x = e.getX();
				int y = e.getY();
				int w;
				for(int i = 0; i<DietNumbers.SIZE; i++){
					w = i*STAT_NAME_HEIGHT+(STAT_NAME_HEIGHT-CHECKBOX_SIZE)/2+7;
					if(y>=w&&y<w+CHECKBOX_SIZE&&x>=5&&x<5+CHECKBOX_SIZE){
						spinnerHover = 0;
						repaint();
						return;
					}
				}
				if(y>=getHeight()-30&&y<getHeight()-5){
					int offset = (SIDEBAR_WIDTH-75)/2;
					if(x>=offset&&x<offset+17){
						spinnerHover = -1;
						repaint();
						return;
					}else if(x>=offset+58&&x<offset+75){
						spinnerHover = 1;
						repaint();
						return;
					}
				}
				spinnerHover = 0;
				repaint();
			}
		});
		addMouseListener(new MouseAdapter(){
			private Timer t;
			@Override
			public void mousePressed(MouseEvent e){
				if(dropdownMenu.overlaps(0, e.getX(), e.getY())){
					return;
				}
				r();
				t = new Timer();
				t.scheduleAtFixedRate(new TimerTask(){
					private int skip = 7;
					@Override
					public void run(){
						if(skip>0){
							skip--;
							return;
						}
						r();
					}
				}, 50, 50);
			}
			private void r(){
				if(spinnerHover!=0){
					if(spinnerHover==-1){
						if(values.length>5){
							values = new int[values.length-1];
							recalculateValues();
						}
					}else if(spinnerHover==1){
						if(values.length<511){
							values = new int[values.length+1];
							recalculateValues();
						}
					}
				}
			}
			@Override
			public void mouseReleased(MouseEvent e){
				if(t!=null){
					t.cancel();
					t = null;
				}
			}
			@Override
			public void mouseClicked(MouseEvent e){
				int x = e.getX();
				int y = e.getY();
				if(x>=0&&x<150){
					if(dropdownMenu.isOpen()){
						if(y>=0&&y<(int)(dropdownMenu.h()*0.6)){
							y /= 0.6;
							dropdownMenu.setIndex(dropdownMenu.indexAt(y));
							dropdownMenu.setOpen(false);
							recalculateValues();
						}
					}else if(y>=0&&y<15){
						dropdownMenu.setOpen(true);
						repaint();
					}
				}
			}
		});
		dropdownMenu = new DropdownMenu(DietNumbers.NAMES);
		recalculateValues();
	}
	@Override
	public void paint(Graphics g1){
		Graphics2D g = (Graphics2D)g1;
		g.setColor(LIGHT_GRAY);
		g.fillRect(0, 0, SIDEBAR_WIDTH, getHeight());
		g.setColor(DARK_GRAY);
		g.fillRect(SIDEBAR_WIDTH, 0, getWidth()-SIDEBAR_WIDTH, getHeight());
		g.setColor(Color.WHITE);
		double columWidth = (getWidth()-SIDEBAR_WIDTH)/(double)values.length;
		int graphHeight = getHeight()-LINE_GRAPH_TOP_BUFFER-LINE_GRAPH_BOTTOM_BUFFER;
		String daysAgo = "Days Ago";
		g.setFont(DAY_COUNT_FONT);
		for(int i = 0; i<11; i++){
			g.setColor(Color.DARK_GRAY);
			int y = (int)((getHeight()-LINE_GRAPH_TOP_BUFFER-LINE_GRAPH_BOTTOM_BUFFER)/10f*i)+LINE_GRAPH_TOP_BUFFER;
			g.drawLine(SIDEBAR_WIDTH, y, getWidth(), y);
			if(i<10){
				g.setColor(Color.WHITE);
				g.drawString(String.valueOf((int)(highestValue/10f*(10-i))), SIDEBAR_WIDTH, y+15);
			}
		}
		for(int i = 0; i<values.length; i++){
			if(i%(values.length/10+1)==0){
				g.setColor(Color.WHITE);
				String count = String.valueOf(values.length-1-i);
				int x = (int)(columWidth*i+columWidth/2)+SIDEBAR_WIDTH;
				g.drawString(count, x, LINE_GRAPH_TOP_BUFFER-30);
				g.drawString(daysAgo, x, LINE_GRAPH_TOP_BUFFER-15);
				g.setColor(Color.DARK_GRAY);
				g.drawLine(x, LINE_GRAPH_TOP_BUFFER, x, getHeight()-LINE_GRAPH_BOTTOM_BUFFER);
			}
			if(i>0){
				g.setColor(Color.green);
				g.drawLine((int)(columWidth*(i-1)+columWidth/2)+SIDEBAR_WIDTH,
					highestValue==0?graphHeight+LINE_GRAPH_TOP_BUFFER
						:(int)((1-values[values.length-1-(i-1)]/(float)highestValue)*graphHeight+LINE_GRAPH_TOP_BUFFER),
					(int)(columWidth*i+columWidth/2)+SIDEBAR_WIDTH, highestValue==0?graphHeight+LINE_GRAPH_TOP_BUFFER
						:(int)((1-values[values.length-1-i]/(float)highestValue)*graphHeight+LINE_GRAPH_TOP_BUFFER));
			}
		}
		for(int i = 0; i<10; i++){
			g.setColor(Color.WHITE);
			int y = (int)((getHeight()-LINE_GRAPH_TOP_BUFFER-LINE_GRAPH_BOTTOM_BUFFER)/10f*i)+LINE_GRAPH_TOP_BUFFER+15;
			g.drawString(String.valueOf((int)(highestValue/10f*(10-i))), SIDEBAR_WIDTH, y);
		}
		g.setFont(SPINNER_FONT);
		g.setColor(Color.WHITE);
		FontMetrics fm = g.getFontMetrics();
		String daysShown = "Days Shown";
		String spinnerNumber = String.valueOf(values.length);
		int x = (SIDEBAR_WIDTH-fm.stringWidth(daysShown))/2;
		int x2 = (SIDEBAR_WIDTH-fm.stringWidth(spinnerNumber))/2;
		int y = (fm.getAscent()+(25-(fm.getAscent()+fm.getDescent()))/2);
		g.drawString(daysShown, x, getHeight()-35);
		g.setColor(Color.BLACK);
		switch(spinnerHover){
			case 1:
				g.drawImage(spinnerRight, (SIDEBAR_WIDTH-75)/2, getHeight()-30, null);
				break;
			case -1:
				g.drawImage(spinnerLeft, (SIDEBAR_WIDTH-75)/2, getHeight()-30, null);
				break;
			default:
				g.drawImage(spinner, (SIDEBAR_WIDTH-75)/2, getHeight()-30, null);
				break;
		}
		g.drawString(spinnerNumber, x2, getHeight()-30+y);
		g.drawImage(dropdownMenu.render(), 0, 0, (int)(dropdownMenu.w()*0.6), (int)(dropdownMenu.h()*0.6), 0, 0, dropdownMenu.w(), dropdownMenu.h(),
			null);
		g.dispose();
	}
	public void recalculateValues(){
		highestValue = 0;
		for(int i = 0; i<values.length; i++){
			values[i] =
				Loader.getResourceLoader().getLog(Loader.getResourceLoader().getCurrentDay()-i, true).getNumbers().stats[dropdownMenu.getIndex()];
			if(values[i]>highestValue){
				highestValue = values[i];
			}
		}
		repaint();
	}
}
