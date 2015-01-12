package me.ci;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

@SuppressWarnings("serial")
public class Toolbar extends JPanel{
	private boolean[] hover = new boolean[3];
	private Loader loader;
	private boolean normalized = true;
	private BufferedImage close, closeHover, minimize, minimizeHover, maximize, maximizeHover, normalize, normalizeHover, tabDark, tabLight;
	private int mouseDragX, mouseDragY;
	private int currentIndex;
	private Font font;
	private boolean dragging = false;
	private static final String[] TABS = {
		"Main",
		"Recipe Builder"
	};
	public static final int TAB_WIDTH = 100;
	public Toolbar(Loader loader, int currentIndex){
		this.currentIndex=currentIndex;
		this.loader=loader;
		font=new Font("Tahoma", Font.BOLD, 12);
		addMouseListener(new MouseAdapter(){
			@Override public void mousePressed(MouseEvent e){
				mouseDragX=e.getPoint().x;
				mouseDragY=e.getPoint().y;
				dragging=mouseDragX>=TABS.length*TAB_WIDTH&&mouseDragX<=getWidth()-105;
			}
			@Override public void mouseReleased(MouseEvent arg0){ dragging=false; }
			@Override public void mouseEntered(MouseEvent e){ updateHovers(e.getPoint()); }
			@Override public void mouseExited(MouseEvent e){ updateHovers(e.getPoint()); }
			@Override public void mouseClicked(MouseEvent e){ click(e.getPoint()); }
		});
		addMouseMotionListener(new MouseMotionAdapter(){
			@Override public void mouseMoved(MouseEvent e){ updateHovers(e.getPoint()); }
			@Override public void mouseDragged(MouseEvent e){ if(normalized&&dragging)Toolbar.this.loader.setPosition(e.getXOnScreen()-mouseDragX-getLocation().x, e.getYOnScreen()-mouseDragY-getLocation().y); }
		});
		setMinimumSize(new Dimension(105, 25));
		setPreferredSize(new Dimension(105, 25));
		try{
			close=ImageIO.read(getClass().getResource("Close.png"));
			closeHover=ImageIO.read(getClass().getResource("Close Hover.png"));
			minimize=ImageIO.read(getClass().getResource("Minimize.png"));
			minimizeHover=ImageIO.read(getClass().getResource("Minimize Hover.png"));
			maximize=ImageIO.read(getClass().getResource("Maximize.png"));
			maximizeHover=ImageIO.read(getClass().getResource("Maximize Hover.png"));
			normalize=ImageIO.read(getClass().getResource("Normalize.png"));
			normalizeHover=ImageIO.read(getClass().getResource("Normalize Hover.png"));
			tabDark=ImageIO.read(getClass().getResource("Tab Dark.png"));
			tabLight=ImageIO.read(getClass().getResource("Tab Light.png"));
		}catch(Exception exception){
			exception.printStackTrace();
			System.exit(1);
		}
	}
	@Override public void paint(Graphics g1){
		Graphics2D g = (Graphics2D)g1;
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		if(hover[0])g.drawImage(closeHover, getWidth()-35, 0, null);
		else g.drawImage(close, getWidth()-35, 0, null);
		if(hover[1]){
			if(normalized)g.drawImage(maximizeHover, getWidth()-70, 0, null);
			else g.drawImage(normalizeHover, getWidth()-70, 0, null);
		}else{
			if(normalized)g.drawImage(maximize, getWidth()-70, 0, null);
			else g.drawImage(normalize, getWidth()-70, 0, null);
		}
		if(hover[2])g.drawImage(minimizeHover, getWidth()-105, 0, null);
		else g.drawImage(minimize, getWidth()-105, 0, null);
		g.setColor(Color.WHITE);
		g.setFont(font);
		for(int i = TABS.length-1; i>=0; i--){
			if(i!=currentIndex){
				g.drawImage(tabDark, i*TAB_WIDTH, 0, null);
				g.drawString(TABS[i], i*TAB_WIDTH+4, getHeight()-8);
			}
		}
		g.drawImage(tabLight, currentIndex*TAB_WIDTH, 0, null);
		g.drawString(TABS[currentIndex], currentIndex*TAB_WIDTH+4, getHeight()-8);
		g.dispose();
	}
	private void updateHovers(Point p){
		hover[0]=p.y>=0&&p.y<getHeight()&&p.x<getWidth()&&p.x>=getWidth()-35;
		hover[1]=p.y>=0&&p.y<getHeight()&&p.x<getWidth()-35&&p.x>=getWidth()-70;
		hover[2]=p.y>=0&&p.y<getHeight()&&p.x<getWidth()-70&&p.x>=getWidth()-105;
		repaint();
	}
	private void click(Point p){
		if(p.x<getWidth()&&p.x>=getWidth()-105){
			if(p.x<getWidth()&&p.x>=getWidth()-35)System.exit(0);
			else if(p.x<getWidth()-35&&p.x>=getWidth()-70){
				if(normalized)loader.maximize();
				else loader.normalize();
				normalized=!normalized;
			}else loader.minimize();
		}else if(p.x<TABS.length*TAB_WIDTH){
			int clickedTab = p.x/TAB_WIDTH;
			if(clickedTab!=currentIndex){
				if(clickedTab==0)Loader.getInstance().buildMainTab();
				if(clickedTab==1)Loader.getInstance().buildRecipeBuilderTab();
			}
		}
	}
}