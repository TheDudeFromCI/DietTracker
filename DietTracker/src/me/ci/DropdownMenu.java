package me.ci;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class DropdownMenu{
	private BufferedImage img;
	private Graphics2D g;
	private int[][] fontCenters;
	private String[] entries;
	private int index;
	private boolean open;
	private static BufferedImage scrollbarClosed, scrollbarOpenTop, scrollbarOpenEntry, scrollbarOpenBottom, scrollbarOpenEntryHover;
	private static final Font FONT = new Font("Tahoma", Font.PLAIN, 25);
	private static final Color BLANK = new Color(0, 0, 0, 0);
	static{
		try{
			scrollbarClosed=ImageIO.read(DropdownMenu.class.getResource("Scrollbar Closed.png"));
			scrollbarOpenTop=ImageIO.read(DropdownMenu.class.getResource("Scrollbar Open Top.png"));
			scrollbarOpenEntry=ImageIO.read(DropdownMenu.class.getResource("Scrollbar Open Entry.png"));
			scrollbarOpenBottom=ImageIO.read(DropdownMenu.class.getResource("Scrollbar Open Bottom.png"));
			scrollbarOpenEntryHover=ImageIO.read(DropdownMenu.class.getResource("Scrollbar Open Entry Hover.png"));
		}catch(Exception exception){ exception.printStackTrace(); }
	}
	public DropdownMenu(String[] entries){
		this.entries=entries;
		img=new BufferedImage(250, (entries.length+1)*25, BufferedImage.TYPE_INT_ARGB);
		g=img.createGraphics();
		g.setFont(FONT);
		fontCenters=new int[entries.length][2];
		FontMetrics fm = g.getFontMetrics();
		for(int i = 0; i<entries.length; i++){
			fontCenters[i][0]=(250-fm.stringWidth(entries[i]))/2;
			fontCenters[i][1]=(fm.getAscent()+(25-(fm.getAscent()+fm.getDescent()))/2);
		}
	}
	public BufferedImage render(){
		g.setColor(BLANK);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		g.setColor(Color.BLACK);
		if(open){
			for(int i = -1; i<entries.length; i++){
				if(i==-1){
					g.drawImage(scrollbarOpenTop, 0, 0, null);
					g.drawString(entries[index], fontCenters[index][0], fontCenters[index][1]);
				}else{
					g.drawImage(i==entries.length-1?scrollbarOpenBottom:scrollbarOpenEntry, 0, (i+1)*25, null);
					g.drawString(entries[i], fontCenters[i][0], fontCenters[i][1]+(i+1)*25);
				}
			}
		}else{
			g.drawImage(scrollbarClosed, 0, 0, null);
			g.drawString(entries[index], fontCenters[index][0], fontCenters[index][1]);
		}
		return img;
	}
}