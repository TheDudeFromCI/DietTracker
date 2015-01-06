package me.ci;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;


@SuppressWarnings("serial")
public class FoodList extends JPanel{
	private BufferedImage foodlistTitle, foodlistAdd, foodlistAddHover, eat, eatHover, scrollbar, remove, removeHover, edit, editHover;
	private boolean hover;
	private Font font1, font2;
	private ArrayList<FoodEntry> foods;
	private int scrollPos;
	private BufferedImage scrollBuf;
	private int scrollWidth;
	private int scrollHeight;
	private Graphics2D scrollBufGraphics;
	private Color scrollMenuColor;
	private int mouseX, mouseY;
	private int foodHoverElement = -1;
	private int foodHoverIcon = -1;
	private boolean scrollingBar = false;
	private int mouseDragY;
	private int startingScrollPosition;
	private static final int TITLE_SIZE = 20;
	private static final int STATS_SIZE = 15;
	private static final int ENTRY_SIZE = TITLE_SIZE+(DietNumbers.SIZE+1)*STATS_SIZE;
	public FoodList(){
		addMouseWheelListener(new MouseWheelListener(){
			public void mouseWheelMoved(MouseWheelEvent e){
				if(Loader.POP_UP_OPEN){
					Toolkit.getDefaultToolkit().beep();
					Loader.POP_UP.requestFocus();
					return;
				}
				if(scrollingBar)return;
				scrollPos+=e.getUnitsToScroll()*5;
				scrollPos=Math.max(scrollPos, 0);
				scrollPos=Math.min(scrollPos, Math.max(ENTRY_SIZE*foods.size()-scrollHeight+10, 0));
				repaint();
			}
		});
		font1=new Font("Tahoma", Font.ITALIC|Font.BOLD, 15);
		font2=new Font("Tahoma", Font.PLAIN, 10);
		scrollMenuColor=new Color(0.2f, 0.2f, 0.2f);
		foods=Loader.getResourceLoader().loadFoodList();
		setPreferredSize(new Dimension(223, 200));
		addMouseListener(new MouseAdapter(){
			@Override public void mousePressed(MouseEvent e){
				if(Loader.POP_UP_OPEN){
					Toolkit.getDefaultToolkit().beep();
					Loader.POP_UP.requestFocus();
					return;
				}
				int maxScroll = Math.max(ENTRY_SIZE*foods.size()-scrollHeight+10, 0);
				if(maxScroll>0){
					float percent = scrollPos/(float)maxScroll;
					if(e.getX()>=200&&e.getY()>=(int)(percent*(getHeight()-130)+25)&&e.getY()<(int)(percent*(getHeight()-130)+25)+100)scrollingBar=true;
					else return;
					startingScrollPosition=scrollPos;
					mouseDragY=e.getY();
				}
			}
			@Override public void mouseEntered(MouseEvent e){
				updateHover(e.getPoint());
				scrollingBar=false;
			}
			@Override public void mouseReleased(MouseEvent e){ scrollingBar=false; }
			@Override public void mouseExited(MouseEvent e){ updateHover(e.getPoint()); }
			@Override public void mouseClicked(MouseEvent e){ click(e.getPoint()); }
		});
		addMouseMotionListener(new MouseMotionAdapter(){
			@Override public void mouseDragged(MouseEvent e){
				if(!scrollingBar)return;
				float percentChange = (e.getY()-mouseDragY)/(getHeight()-130f);
				float toScrollUnits = ENTRY_SIZE*foods.size()-scrollHeight+10;
				scrollPos=(int)(toScrollUnits*percentChange+startingScrollPosition);
				scrollPos=Math.max(scrollPos, 0);
				scrollPos=Math.min(scrollPos, Math.max(ENTRY_SIZE*foods.size()-scrollHeight+10, 0));
				repaint();
			}
			@Override public void mouseMoved(MouseEvent e){ updateHover(e.getPoint()); }
		});
		try{
			foodlistTitle=ImageIO.read(getClass().getResource("Food List Title.png"));
			foodlistAdd=ImageIO.read(getClass().getResource("Food List Add.png"));
			foodlistAddHover=ImageIO.read(getClass().getResource("Food List Add Hover.png"));
			eat=ImageIO.read(getClass().getResource("Eat.png"));
			eatHover=ImageIO.read(getClass().getResource("Eat Hover.png"));
			scrollbar=ImageIO.read(getClass().getResource("Scrollbar.png"));
			remove=ImageIO.read(getClass().getResource("Remove.png"));
			removeHover=ImageIO.read(getClass().getResource("Remove Hover.png"));
			edit=ImageIO.read(getClass().getResource("Edit.png"));
			editHover=ImageIO.read(getClass().getResource("Edit Hover.png"));
		}catch(Exception exception){
			exception.printStackTrace();
			System.exit(1);
		}
	}
	@Override public void paint(Graphics g1){
		Graphics2D g = (Graphics2D)g1;
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(foodlistTitle, 0, 0, null);
		if(hover)g.drawImage(foodlistAddHover, 175, 0, null);
		else g.drawImage(foodlistAdd, 175, 0, null);
		if(getWidth()!=scrollWidth||getHeight()-25!=scrollHeight){
			scrollBuf=new BufferedImage(scrollWidth=getWidth(), scrollHeight=(getHeight()-25), BufferedImage.TYPE_INT_RGB);
			scrollBufGraphics=scrollBuf.createGraphics();
		}
		scrollBufGraphics.setColor(scrollMenuColor);
		scrollBufGraphics.fillRect(0, 0, scrollWidth, scrollHeight);
		FoodEntry food;
		foodHoverElement=-1;
		DietNumbers currentStats = Loader.getResourceLoader().loadTodaysStats();
		DietNumbers maxStats = Loader.getResourceLoader().loadMaxDiet();
		for(int i = 0; i<foods.size(); i++){
			if(i*ENTRY_SIZE+ENTRY_SIZE<=scrollPos)continue;
			if(i*ENTRY_SIZE>scrollPos+scrollHeight)continue;
			food=foods.get(i);
			int y = i*ENTRY_SIZE-scrollPos;
			scrollBufGraphics.setFont(font1);
			boolean overKill = false;
			for(int a = 0; a<DietNumbers.SIZE; a++){
				if(food.getStats().stats[a]>0&&food.getStats().stats[a]+currentStats.stats[a]>maxStats.stats[a]){
					overKill=true;
					break;
				}
			}
			if(overKill)scrollBufGraphics.setColor(Color.RED);
			else scrollBufGraphics.setColor(Color.WHITE);
			scrollBufGraphics.drawString(food.getName(), 5, y+TITLE_SIZE);
			scrollBufGraphics.setFont(font2);
			if(overKill)scrollBufGraphics.setColor(Color.RED);
			else scrollBufGraphics.setColor(Color.LIGHT_GRAY);
			for(int j = 0; j<DietNumbers.SIZE+1; j++){
				if(j==DietNumbers.SIZE)scrollBufGraphics.drawString("---------------------------------------------", 12, y+TITLE_SIZE+STATS_SIZE*(j+1));
				else scrollBufGraphics.drawString(DietNumbers.NAMES[j]+": "+food.getStats().stats[j], 12, y+TITLE_SIZE+STATS_SIZE*(j+1));
			}
			int bufImageStartPos = (int)(y+ENTRY_SIZE/2f-27.5f);
			int realY = bufImageStartPos+25;
			if(mouseX>=170&&mouseX<195&&mouseY>=Math.max(realY, 25)&&mouseY<realY+25){
				scrollBufGraphics.drawImage(eatHover, 170, bufImageStartPos, null);
				foodHoverElement=i;
				foodHoverIcon=0;
			}else scrollBufGraphics.drawImage(eat, 170, bufImageStartPos, null);
			if(mouseX>=140&&mouseX<165&&mouseY>=Math.max(realY, 25)&&mouseY<realY+25){
				scrollBufGraphics.drawImage(removeHover, 140, bufImageStartPos, null);
				foodHoverElement=i;
				foodHoverIcon=1;
			}else scrollBufGraphics.drawImage(remove, 140, bufImageStartPos, null);
			if(mouseX>=140&&mouseX<165&&mouseY>=Math.max(realY+30, 25)&&mouseY<realY+55){
				scrollBufGraphics.drawImage(editHover, 140, bufImageStartPos+30, null);
				foodHoverElement=i;
				foodHoverIcon=2;
			}else scrollBufGraphics.drawImage(edit, 140, bufImageStartPos+30, null);
		}
		g.drawImage(scrollBuf, 0, 25, null);
		int maxScroll = Math.max(ENTRY_SIZE*foods.size()-scrollHeight+10, 0);
		if(maxScroll>0){
			float percent = scrollPos/(float)maxScroll;
			g.drawImage(scrollbar, 200, (int)(percent*(getHeight()-130)+25), null);
		}
		g.dispose();
	}
	private void updateHover(Point p){
		if(p==null){
			hover=false;
			mouseX=mouseY=-1;
			return;
		}
		hover=p.y>=0&&p.y<25&&p.x>=175&&p.x<200;
		mouseX=p.x;
		mouseY=p.y;
		repaint();
	}
	private void click(Point p){
		if(p.y>=0&&p.y<25&&p.x>=175&&p.x<200)new AddNewFood();
		else if(foodHoverElement>-1){
			if(foodHoverIcon==0){
				final int index = foodHoverElement;
				new ConfirmPanel("Are you sure you want to add this item to your menu?", new Runnable(){
					public void run(){ Loader.getInstance().getCurrentStats().addFoodEntry(foods.get(index)); }
				}, null);
			}else if(foodHoverIcon==1){
				final int index = foodHoverElement;
				new ConfirmPanel("Are you sure you want to remove this item?", new Runnable(){
					public void run(){
						foods.remove(index);
						Loader.getResourceLoader().save();
						repaint();
					}
				}, null);
			}else new EditFood(foods.get(foodHoverElement));
		}
	}
	public void addFoodEntry(FoodEntry foodEntry){
		foods.add(foodEntry);
		Loader.getResourceLoader().save();
		repaint();
	}
}