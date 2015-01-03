package me.ci;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.awt.Font;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Dimension;

@SuppressWarnings("serial")
public class CurrentStats extends JPanel{
	private DietNumbers dietNumbers;
	private DietNumbers tempDietNumbers;
	private DietNumbers maxDietNumbers;
	private Color[] warningStage;
	private boolean[] broken;
	private Font font1, font2, font3;
	private Color darkerGray;
	public CurrentStats(){
		setPreferredSize(new Dimension(400, 400));
		setMinimumSize(new Dimension(100, 100));
		font1=new Font("Tahoma", Font.BOLD, 40);
		font2=new Font("Tahoma", Font.ITALIC, 40);
		font3=new Font("Tahoma", Font.ITALIC|Font.BOLD, 40);
		warningStage=new Color[DietNumbers.SIZE];
		broken=new boolean[DietNumbers.SIZE];
		darkerGray=new Color(0.1f, 0.1f, 0.1f);
		reload();
	}
	public void reload(){
		tempDietNumbers=new DietNumbers();
		load();
		new Timer().scheduleAtFixedRate(new TimerTask(){
			public void run(){
				int checks = 0;
				for(int i = 0; i<DietNumbers.SIZE; i++){
					if(dietNumbers.stats[i]>tempDietNumbers.stats[i]){
						tempDietNumbers.stats[i]+=Math.max(Math.round(0.015f*dietNumbers.max()), 1);
						if(tempDietNumbers.stats[i]>dietNumbers.stats[i]){
							tempDietNumbers.stats[i]=dietNumbers.stats[i];
							checks++;
						}
						float percent = tempDietNumbers.stats[i]/(float)maxDietNumbers.stats[i];
						if(percent<=1){
							warningStage[i]=new Color(percent, 1-percent, 0);
							broken[i]=false;
						}else{
							warningStage[i]=new Color(Math.max(1.1f-percent, 0.9f), 0, 0);
							broken[i]=true;
						}
					}else checks++;
				}
				repaint();
				if(checks==DietNumbers.SIZE)cancel();
			}
		}, 50, 50);
	}
	private void load(){
		dietNumbers=Loader.getResourceLoader().loadTodaysStats();
		maxDietNumbers=Loader.getResourceLoader().loadMaxDiet();
	}
	@Override public void paint(Graphics g1){
		Graphics2D g = (Graphics2D)g1;
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(darkerGray);
		FontMetrics fontMetrics = g.getFontMetrics();
		g.setFont(font1);
		int fontHeight = g.getFontMetrics().getHeight();
		g.fillRect(0, (int)(getHeight()/2f-(fontHeight*DietNumbers.SIZE/2f)-fontHeight/2f), getWidth(), fontHeight*DietNumbers.SIZE);
		g.setColor(Color.WHITE);
		float startPos = getHeight()/2f+(DietNumbers.SIZE/2f*fontHeight-DietNumbers.SIZE*fontHeight)+(fontMetrics.getLeading()+fontMetrics.getAscent());
		for(int i = 0; i<DietNumbers.SIZE; i++){
			int y = (int)(startPos+fontHeight*i);
			g.drawString(DietNumbers.NAMES[i]+":", 10, y);
		}
		for(int i = 0; i<DietNumbers.SIZE; i++){
			if(broken[i])g.setFont(font3);
			else g.setFont(font2);
			fontMetrics=g.getFontMetrics();
			g.setColor(warningStage[i]);
			int y = (int)(startPos+fontHeight*i);
			int x = (int)(getWidth()-(fontMetrics.getStringBounds(tempDietNumbers.stats[i]+"/"+maxDietNumbers.stats[i], g).getWidth()+10));
			g.drawString(tempDietNumbers.stats[i]+"/"+maxDietNumbers.stats[i], x, y);
		}
		g.dispose();
	}
	public void addFoodEntry(FoodEntry foodEntry){
		//TODO Add diet numbers.
	}
}