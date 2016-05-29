package me.ci;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class FoodList extends JPanel{
	private BufferedImage foodlistTitle, foodlistAdd, foodlistAddHover, eat, eatHover, scrollbar, remove, removeHover, edit, editHover, checkboxHover,
		checkboxFalse, checkboxTrue;
	private boolean hover;
	private Font font1, font2, font3, font4;
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
	private DropdownMenu dropdownMenu;
	private boolean checkboxMouseHover, filterReds;
	private static final int TITLE_SIZE = 20;
	private static final int STATS_SIZE = 15;
	private static final int ENTRY_SIZE = TITLE_SIZE+(DietNumbers.SIZE+1)*STATS_SIZE;
	private static final int COMP_WIDTH = 493;
	private static final int DROP_DOWN_MENU_OFFSET = 170;
	public FoodList(){
		addMouseWheelListener(new MouseWheelListener(){
			@Override
			public void mouseWheelMoved(MouseWheelEvent e){
				if(Loader.POP_UP_OPEN){
					Toolkit.getDefaultToolkit().beep();
					Loader.POP_UP.requestFocus();
					return;
				}
				if(scrollingBar){
					return;
				}
				scrollPos += e.getUnitsToScroll()*5;
				scrollPos = Math.max(scrollPos, 0);
				scrollPos = Math.min(scrollPos, Math.max(ENTRY_SIZE*dropdownMenu.getFilteredList(foods, filterReds).size()-scrollHeight+10, 0));
				repaint();
			}
		});
		font1 = new Font("Tahoma", Font.ITALIC|Font.BOLD, 15);
		font2 = new Font("Tahoma", Font.PLAIN, 15);
		font3 = new Font("Tahoma", Font.ITALIC, 30);
		font4 = new Font("Tahoma", Font.BOLD, 19);
		scrollMenuColor = new Color(0.2f, 0.2f, 0.2f);
		foods = Loader.getResourceLoader().loadFoodList();
		dropdownMenu = new DropdownMenu(findAllCategories());
		setPreferredSize(new Dimension(COMP_WIDTH, 200));
		addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e){
				if(Loader.POP_UP_OPEN){
					Toolkit.getDefaultToolkit().beep();
					Loader.POP_UP.requestFocus();
					return;
				}
				int x = e.getX();
				int y = e.getY();
				if(dropdownMenu.overlaps(DROP_DOWN_MENU_OFFSET, x, y)){
					if(!dropdownMenu.isOpen()){
						dropdownMenu.setOpen(true);
					}else{
						dropdownMenu.setIndex(dropdownMenu.indexAt(y));
						dropdownMenu.setOpen(false);
						if(y>=25){
							scrollPos = 0;
						}
					}
					repaint();
				}else{
					int maxScroll = Math.max(ENTRY_SIZE*dropdownMenu.getFilteredList(foods, filterReds).size()-scrollHeight+10, 0);
					if(maxScroll>0){
						float percent = scrollPos/(float)maxScroll;
						if(x>=COMP_WIDTH-23&&y>=(int)(percent*(getHeight()-130)+25)&&y<(int)(percent*(getHeight()-130)+25)+100){
							scrollingBar = true;
						}else{
							return;
						}
						startingScrollPosition = scrollPos;
						mouseDragY = e.getY();
					}
				}
			}
			@Override
			public void mouseEntered(MouseEvent e){
				updateHover(e.getPoint());
				scrollingBar = false;
			}
			@Override
			public void mouseReleased(MouseEvent e){
				scrollingBar = false;
			}
			@Override
			public void mouseExited(MouseEvent e){
				updateHover(e.getPoint());
			}
			@Override
			public void mouseClicked(MouseEvent e){
				if(Loader.POP_UP_OPEN){
					Loader.POP_UP.requestFocus();
					return;
				}
				click(e.getPoint());
			}
		});
		addMouseMotionListener(new MouseMotionAdapter(){
			@Override
			public void mouseDragged(MouseEvent e){
				if(!scrollingBar){
					return;
				}
				ArrayList<FoodEntry> tempFoods = dropdownMenu.getFilteredList(foods, filterReds);
				float percentChange = (e.getY()-mouseDragY)/(getHeight()-130f);
				float toScrollUnits = ENTRY_SIZE*tempFoods.size()-scrollHeight+10;
				scrollPos = (int)(toScrollUnits*percentChange+startingScrollPosition);
				scrollPos = Math.max(scrollPos, 0);
				scrollPos = Math.min(scrollPos, Math.max(ENTRY_SIZE*tempFoods.size()-scrollHeight+10, 0));
				repaint();
			}
			@Override
			public void mouseMoved(MouseEvent e){
				updateHover(e.getPoint());
			}
		});
		try{
			foodlistTitle = ImageIO.read(getClass().getResource("/assets/Food List Title.png"));
			foodlistAdd = ImageIO.read(getClass().getResource("/assets/Food List Add.png"));
			foodlistAddHover = ImageIO.read(getClass().getResource("/assets/Food List Add Hover.png"));
			eat = ImageIO.read(getClass().getResource("/assets/Eat.png"));
			eatHover = ImageIO.read(getClass().getResource("/assets/Eat Hover.png"));
			scrollbar = ImageIO.read(getClass().getResource("/assets/Scrollbar.png"));
			remove = ImageIO.read(getClass().getResource("/assets/Remove.png"));
			removeHover = ImageIO.read(getClass().getResource("/assets/Remove Hover.png"));
			edit = ImageIO.read(getClass().getResource("/assets/Edit.png"));
			editHover = ImageIO.read(getClass().getResource("/assets/Edit Hover.png"));
			checkboxHover = ImageIO.read(getClass().getResource("/assets/Checkbox Hover.png"));
			checkboxFalse = ImageIO.read(getClass().getResource("/assets/Checkbox False.png"));
			checkboxTrue = ImageIO.read(getClass().getResource("/assets/Checkbox True.png"));
		}catch(Exception exception){
			exception.printStackTrace();
			System.exit(1);
		}
	}
	@Override
	public void paint(Graphics g1){
		Graphics2D g = (Graphics2D)g1;
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(foodlistTitle, 0, 0, null);
		if(hover){
			g.drawImage(foodlistAddHover, COMP_WIDTH-25, 0, null);
		}else{
			g.drawImage(foodlistAdd, COMP_WIDTH-25, 0, null);
		}
		if(getWidth()!=scrollWidth||getHeight()-25!=scrollHeight){
			scrollBuf = new BufferedImage(scrollWidth = getWidth(), scrollHeight = (getHeight()-25), BufferedImage.TYPE_INT_RGB);
			scrollBufGraphics = scrollBuf.createGraphics();
		}
		scrollBufGraphics.setColor(scrollMenuColor);
		scrollBufGraphics.fillRect(0, 0, scrollWidth, scrollHeight);
		FoodEntry food;
		foodHoverElement = -1;
		DietNumbers currentStats = Loader.getResourceLoader().loadTodaysStats();
		DietNumbers maxStats = Loader.getResourceLoader().loadMaxDiet();
		ArrayList<FoodEntry> tempFoods = dropdownMenu.getFilteredList(foods, filterReds);
		for(int i = 0; i<tempFoods.size(); i++){
			if(i*ENTRY_SIZE+ENTRY_SIZE<=scrollPos){
				continue;
			}
			if(i*ENTRY_SIZE>scrollPos+scrollHeight){
				continue;
			}
			food = tempFoods.get(i);
			int y = i*ENTRY_SIZE-scrollPos;
			scrollBufGraphics.setFont(font1);
			int remainingBuys = food.getRemaining(currentStats, maxStats);
			boolean overKill = remainingBuys==0;
			if(overKill){
				scrollBufGraphics.setColor(Color.RED);
			}else{
				scrollBufGraphics.setColor(Color.WHITE);
			}
			scrollBufGraphics.drawString(food.getName(), 5, y+TITLE_SIZE);
			scrollBufGraphics.setFont(font2);
			if(overKill){
				scrollBufGraphics.setColor(Color.RED);
			}else{
				scrollBufGraphics.setColor(Color.LIGHT_GRAY);
			}
			for(int j = 0; j<=DietNumbers.SIZE; j++){
				if(j==DietNumbers.SIZE){
					scrollBufGraphics.drawString(
						"-----------------------------------------------------------------------------------------------------------------------------------------",
						0, y+TITLE_SIZE+STATS_SIZE*(j+1));
				}else{
					scrollBufGraphics.drawString(DietNumbers.NAMES[j]+": "+food.getStats().stats[j], 12, y+TITLE_SIZE+STATS_SIZE*(j+1));
				}
			}
			int bufImageStartPos = (int)(y+ENTRY_SIZE/2f-27.5f);
			int realY = bufImageStartPos+25;
			if(mouseX>=COMP_WIDTH-53&&mouseX<COMP_WIDTH-28&&mouseY>=Math.max(realY, 25)&&mouseY<realY+25){
				scrollBufGraphics.drawImage(eatHover, COMP_WIDTH-53, bufImageStartPos, null);
				foodHoverElement = i;
				foodHoverIcon = 0;
			}else{
				scrollBufGraphics.drawImage(eat, COMP_WIDTH-53, bufImageStartPos, null);
			}
			if(mouseX>=COMP_WIDTH-83&&mouseX<COMP_WIDTH-58&&mouseY>=Math.max(realY, 25)&&mouseY<realY+25){
				scrollBufGraphics.drawImage(removeHover, COMP_WIDTH-83, bufImageStartPos, null);
				foodHoverElement = i;
				foodHoverIcon = 1;
			}else{
				scrollBufGraphics.drawImage(remove, COMP_WIDTH-83, bufImageStartPos, null);
			}
			if(mouseX>=COMP_WIDTH-83&&mouseX<COMP_WIDTH-58&&mouseY>=Math.max(realY+30, 25)&&mouseY<realY+55){
				scrollBufGraphics.drawImage(editHover, COMP_WIDTH-83, bufImageStartPos+30, null);
				foodHoverElement = i;
				foodHoverIcon = 2;
			}else{
				scrollBufGraphics.drawImage(edit, COMP_WIDTH-83, bufImageStartPos+30, null);
			}
			scrollBufGraphics.setFont(font3);
			if(overKill){
				scrollBufGraphics.setColor(Color.RED);
			}else{
				scrollBufGraphics.setColor(Color.WHITE);
			}
			String left = remainingBuys==-1?"N/A":"x"+remainingBuys;
			FontMetrics fm = scrollBufGraphics.getFontMetrics();
			scrollBufGraphics.drawString(left, (int)(COMP_WIDTH-90-fm.getStringBounds(left, scrollBufGraphics).getWidth()), bufImageStartPos+54);
			scrollBufGraphics.drawImage(food.graph(), 120, y+TITLE_SIZE+1, null);
			scrollBufGraphics.setFont(font4);
			fm = scrollBufGraphics.getFontMetrics();
			scrollBufGraphics.drawString(food.getCategory(), (int)(getWidth()-fm.getStringBounds(food.getCategory(), g).getWidth()-25),
				y+TITLE_SIZE+STATS_SIZE*DietNumbers.SIZE);
		}
		g.drawImage(filterReds?checkboxTrue:checkboxFalse, DROP_DOWN_MENU_OFFSET-30, 0, null);
		if(checkboxMouseHover){
			g.drawImage(checkboxHover, DROP_DOWN_MENU_OFFSET-30, 0, null);
		}
		g.drawImage(scrollBuf, 0, 25, null);
		int maxScroll = Math.max(ENTRY_SIZE*tempFoods.size()-scrollHeight+10, 0);
		if(maxScroll>0){
			float percent = scrollPos/(float)maxScroll;
			g.drawImage(scrollbar, COMP_WIDTH-23, (int)(percent*(getHeight()-130)+25), null);
		}
		g.drawImage(dropdownMenu.render(), DROP_DOWN_MENU_OFFSET, 0, null);
		g.dispose();
	}
	private void updateHover(Point p){
		if(p==null){
			hover = false;
			mouseX = mouseY = -1;
			return;
		}
		hover = p.y>=0&&p.y<25&&p.x>=COMP_WIDTH-25&&p.x<COMP_WIDTH;
		checkboxMouseHover = p.y>=0&&p.y<25&&p.x>=DROP_DOWN_MENU_OFFSET-30&&p.x<DROP_DOWN_MENU_OFFSET-5;
		mouseX = p.x;
		mouseY = p.y;
		repaint();
	}
	private void click(Point p){
		if(p.y>=0&&p.y<25&&p.x>=COMP_WIDTH-25&&p.x<COMP_WIDTH){
			new AddNewFood();
		}else if(checkboxMouseHover){
			filterReds = !filterReds;
			scrollPos = 0;
			repaint();
		}else if(foodHoverElement>-1){
			switch(foodHoverIcon){
				case 0:{
					final int index = foodHoverElement;
					new ConfirmPanel("Are you sure you want to add this item to your menu?", new Runnable(){
						@Override
						public void run(){
							Loader.getInstance().getCurrentStats().addFoodEntry(dropdownMenu.getFilteredList(foods, filterReds).get(index));
						}
					}, null);
					break;
				}
				case 1:{
					final int index = foodHoverElement;
					new ConfirmPanel("Are you sure you want to remove this item?", new Runnable(){
						@Override
						public void run(){
							foods.remove(foods.indexOf(dropdownMenu.getFilteredList(foods, filterReds).get(index)));
							dropdownMenu.rebuild(findAllCategories());
							Loader.getResourceLoader().save();
							repaint();
						}
					}, null);
					break;
				}
				default:
					new EditFood(dropdownMenu.getFilteredList(foods, filterReds).get(foodHoverElement));
					break;
			}
		}
	}
	public void addFoodEntry(FoodEntry foodEntry){
		foods.add(foodEntry);
		dropdownMenu.rebuild(findAllCategories());
		Loader.getResourceLoader().save();
		repaint();
	}
	private String[] findAllCategories(){
		ArrayList<String> list = new ArrayList<>(16);
		list.add("All");
		for(FoodEntry f : foods){
			if(!list.contains(f.getCategory())){
				list.add(f.getCategory());
			}
		}
		return list.toArray(new String[list.size()]);
	}
	public void rebuildCategories(){
		dropdownMenu.rebuild(findAllCategories());
	}
}
