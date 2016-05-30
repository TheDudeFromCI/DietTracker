/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose Tools | Templates and open the
 * template in the editor.
 */
package me.ci;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * @author thedudefromci
 */
public class LogList extends JFrame{
	private static final int BUTTON_HEIGHT = 50;
	private static final int BUTTON_SPACE_VERTICAL = 10;
	private static final int BUTTON_SPACE_HORIZONTAL = 25;
	private static final int BUTTON_CURVE = 20;
	private static final int COMP_TEXT_HEIGHT = 12;
	private static final Font COMP_FONT = new Font("Tahoma", Font.BOLD, 10);
	private static final String COMP_SEPERATOR =
		"-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
	private static class LogListPaint extends JPanel{
		private final ArrayList<LogListComp> list;
		private int daysBack = 0;
		private boolean overButton;
		private LogListPaint(){
			list = new ArrayList(4);
			MouseAdapter mouse = new MouseAdapter(){
				@Override
				public void mouseMoved(MouseEvent e){
					int x = e.getX();
					int y = e.getY();
					int h = getPreferredSize().height;
					int w = getWidth();
					overButton =
						x>=BUTTON_SPACE_HORIZONTAL&&x<w-BUTTON_SPACE_HORIZONTAL&&y>=h-BUTTON_HEIGHT+BUTTON_SPACE_VERTICAL&&y<h-BUTTON_SPACE_VERTICAL;
					repaint();
				}
				@Override
				public void mouseDragged(MouseEvent e){
					mouseMoved(e);
				}
				@Override
				public void mouseExited(MouseEvent e){
					overButton = false;
					repaint();
				}
				@Override
				public void mouseEntered(MouseEvent e){
					mouseMoved(e);
				}
				@Override
				public void mouseReleased(MouseEvent e){
					if(overButton){
						loadPrevious();
					}
				}
			};
			addMouseMotionListener(mouse);
			addMouseListener(mouse);
			updatePrefSize();
		}
		@Override
		public void paint(Graphics g1){
			Graphics2D g = (Graphics2D)g1;
			g.setColor(Color.darkGray);
			int width = getWidth();
			int height = getHeight();
			g.fillRect(0, 0, width, height);
			g.setFont(COMP_FONT);
			FontMetrics fm = g.getFontMetrics();
			g.setColor(Color.white);
			int y = 0;
			// Draw comps
			for(LogListComp comp : list){
				y = comp.draw(g, fm, width, y);
			}
			// Draw button
			g.setColor(overButton?Color.lightGray:Color.gray);
			g.fillRoundRect(BUTTON_SPACE_HORIZONTAL, BUTTON_SPACE_VERTICAL+y, width-BUTTON_SPACE_HORIZONTAL*2, BUTTON_HEIGHT-BUTTON_SPACE_VERTICAL*2,
				BUTTON_CURVE, BUTTON_CURVE);
			g.setColor(Color.black);
			g.drawRoundRect(BUTTON_SPACE_HORIZONTAL, BUTTON_SPACE_VERTICAL+y, width-BUTTON_SPACE_HORIZONTAL*2, BUTTON_HEIGHT-BUTTON_SPACE_VERTICAL*2,
				BUTTON_CURVE, BUTTON_CURVE);
			g.setColor(Color.white);
			String buttonText = "Load More";
			g.drawString(buttonText, (width-fm.stringWidth(buttonText))/2, y+(BUTTON_HEIGHT-fm.getHeight())/2+fm.getAscent());
			g.dispose();
		}
		private void updatePrefSize(){
			int height = BUTTON_HEIGHT;
			for(LogListComp comp : list){
				height += comp.height();
			}
			setPreferredSize(new Dimension(340, height));
		}
		private void loadPrevious(){
			daysBack++;
			LogFile log = Loader.getResourceLoader().getLog(Loader.getResourceLoader().getCurrentDay()-daysBack, false);
			if(log==null){
				return;
			}
			LogListComp comp = new LogListComp(log, daysBack);
			list.add(comp);
			updatePrefSize();
			repaint();
		}
	}
	private static class LogListComp{
		private final ArrayList<String> names;
		private final ArrayList<String> categorys;
		private final int daysBack;
		private final int height;
		private LogListComp(LogFile log, int daysBack){
			this.daysBack = daysBack;
			names = new ArrayList(log.getFoodsEaten().size());
			categorys = new ArrayList(log.getFoodsEaten().size());
			for(String f : log.getFoodsEaten()){
				FoodEntry ref = getFoodRef(f);
				if(ref==null){
					names.add("Unknown Food");
					categorys.add("Unknown Category");
				}else{
					names.add(ref.getName());
					categorys.add(ref.getCategory());
				}
			}
			int h = COMP_TEXT_HEIGHT;
			h += names.size()*COMP_TEXT_HEIGHT*2;
			height = h;
		}
		private FoodEntry getFoodRef(String uuid){
			for(FoodEntry food : Loader.getResourceLoader().loadFoodList()){
				if(food.getUUID().equals(uuid)){
					return food;
				}
			}
			return null;
		}
		private int draw(Graphics2D g, FontMetrics fm, int width, int y){
			for(int i = 0; i<names.size(); i++){
				String name = "Name: "+names.get(i);
				String cat = "Cat: "+categorys.get(i);
				y += COMP_TEXT_HEIGHT;
				g.drawString(name, (width-fm.stringWidth(name))/2f, y-5);
				y += COMP_TEXT_HEIGHT;
				g.drawString(cat, (width-fm.stringWidth(cat))/2f, y-5);
			}
			y += COMP_TEXT_HEIGHT;
			g.drawString(COMP_SEPERATOR, (width-fm.stringWidth(COMP_SEPERATOR))/2f, y-5);
			return y;
		}
		private int height(){
			return height;
		}
	}
	public LogList(){
		addWindowFocusListener(new WindowFocusListener(){
			@Override
			public void windowGainedFocus(WindowEvent e){
				if(Loader.POP_UP_OPEN&&Loader.POP_UP!=LogList.this){
					Toolkit.getDefaultToolkit().beep();
					Loader.POP_UP.requestFocus();
				}
			}
			@Override
			public void windowLostFocus(WindowEvent e){}
		});
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				close();
			}
		});
		Loader.POP_UP_OPEN = true;
		Loader.POP_UP = this;
		init();
		addComponents();
		setVisible(true);
	}
	private void init(){
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle("Menu Logs");
		setMinimumSize(new Dimension(400, 400));
		getContentPane().setBackground(Color.DARK_GRAY);
		getContentPane().setLayout(new BorderLayout(0, 0));
	}
	private void addComponents(){
		LogListPaint paint = new LogListPaint();
		// Load last three logs.
		paint.loadPrevious();
		paint.loadPrevious();
		paint.loadPrevious();
		// Finish build.
		JScrollPane scrollPane = new JScrollPane(paint);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		pack();
	}
	private void close(){
		Loader.POP_UP_OPEN = false;
		Loader.POP_UP = null;
		dispose();
	}
}
