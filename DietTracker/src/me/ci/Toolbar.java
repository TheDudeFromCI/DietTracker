package me.ci;

import java.awt.Color;
import java.awt.Dimension;
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
	private BufferedImage close, closeHover, minimize, minimizeHover, maximize, maximizeHover, normalize, normalizeHover;
	private int mouseDragX, mouseDragY;
	public Toolbar(Loader loader){
		this.loader=loader;
		addMouseListener(new MouseAdapter(){
			@Override public void mousePressed(MouseEvent e){
				mouseDragX=e.getPoint().x;
				mouseDragY=e.getPoint().y;
			}
			@Override public void mouseEntered(MouseEvent e){ updateHovers(e.getPoint()); }
			@Override public void mouseExited(MouseEvent e){ updateHovers(e.getPoint()); }
			@Override public void mouseClicked(MouseEvent e){ click(e.getPoint()); }
		});
		addMouseMotionListener(new MouseMotionAdapter(){
			@Override public void mouseMoved(MouseEvent e){ updateHovers(e.getPoint()); }
			@Override public void mouseDragged(MouseEvent e){ if(normalized)Toolbar.this.loader.setPosition(e.getXOnScreen()-mouseDragX-getLocation().x, e.getYOnScreen()-mouseDragY-getLocation().y); }
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
		g.dispose();
	}
	private void updateHovers(Point p){
		hover[0]=p.y>=0&&p.y<getHeight()&&p.x<getWidth()&&p.x>=getWidth()-35;
		hover[1]=p.y>=0&&p.y<getHeight()&&p.x<getWidth()-35&&p.x>=getWidth()-70;
		hover[2]=p.y>=0&&p.y<getHeight()&&p.x<getWidth()-70&&p.x>=getWidth()-105;
		repaint();
	}
	private void click(Point p){
		if(p.y<getHeight()&&p.y>=0){
			if(p.x<getWidth()&&p.x>=getWidth()-105){
				if(p.x<getWidth()&&p.x>=getWidth()-35)System.exit(0);
				else if(p.x<getWidth()-35&&p.x>=getWidth()-70){
					if(normalized)loader.maximize();
					else loader.normalize();
					normalized=!normalized;
				}else loader.minimize();
			}
		}
	}
}