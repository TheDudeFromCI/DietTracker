package me.ci;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CurrentStats extends JPanel{
	private final DietNumbers dietNumbers;
	private final DietNumbers tempDietNumbers;
	private final DietNumbers maxDietNumbers;
	private final Color[] warningStage;
	private final boolean[] broken;
	private final Font font1, font2, font3;
	private final Color darkerGray;
	private Timer t;
	public CurrentStats(){
		setPreferredSize(new Dimension(250, 26*DietNumbers.SIZE));
		setMinimumSize(new Dimension(100, 100));
		font1 = new Font("Tahoma", Font.BOLD, 20);
		font2 = new Font("Tahoma", Font.ITALIC, 20);
		font3 = new Font("Tahoma", Font.ITALIC|Font.BOLD, 20);
		warningStage = new Color[DietNumbers.SIZE];
		broken = new boolean[DietNumbers.SIZE];
		darkerGray = new Color(0.1f, 0.1f, 0.1f);
		dietNumbers = Loader.getResourceLoader().loadTodaysStats();
		maxDietNumbers = Loader.getResourceLoader().loadMaxDiet();
		tempDietNumbers = new DietNumbers();
		reload();
	}
	public void reload(){
		if(Loader.getInstance()!=null){
			Loader.getInstance().getFoodList().repaint();
			Loader.getInstance().getHistoryGraph().recalculateValues();
		}
		if(t!=null){
			t.cancel();
		}
		t = new Timer();
		t.scheduleAtFixedRate(new TimerTask(){
			public void run(){
				int checks = 0;
				for(int i = 0; i<DietNumbers.SIZE; i++){
					if(dietNumbers.stats[i]>tempDietNumbers.stats[i]){
						tempDietNumbers.stats[i] += Math.max(Math.round(0.05f*Math.abs(dietNumbers.stats[i]-tempDietNumbers.stats[i])), 1);
						if(tempDietNumbers.stats[i]>dietNumbers.stats[i]){
							tempDietNumbers.stats[i] = dietNumbers.stats[i];
							checks++;
						}
						float percent = tempDietNumbers.stats[i]/(float)maxDietNumbers.stats[i];
						if(percent<=1){
							warningStage[i] = new Color(percent, 1-percent, 0);
							broken[i] = false;
						}else{
							warningStage[i] = new Color(Math.max(1.1f-percent, 0.9f), 0, 0);
							broken[i] = true;
						}
					}else if(dietNumbers.stats[i]<tempDietNumbers.stats[i]){
						tempDietNumbers.stats[i] -= Math.max(Math.round(0.05f*Math.abs(dietNumbers.stats[i]-tempDietNumbers.stats[i])), 1);
						if(tempDietNumbers.stats[i]<dietNumbers.stats[i]){
							tempDietNumbers.stats[i] = dietNumbers.stats[i];
							checks++;
						}
						float percent = tempDietNumbers.stats[i]/(float)maxDietNumbers.stats[i];
						if(percent<=1){
							warningStage[i] = new Color(percent, 1-percent, 0);
							broken[i] = false;
						}else{
							warningStage[i] = new Color(Math.max(1.1f-percent, 0.9f), 0, 0);
							broken[i] = true;
						}
					}else{
						if(tempDietNumbers.stats[i]==0){
							warningStage[i] = new Color(0, 1f, 0);
						}
						checks++;
					}
				}
				repaint();
				if(checks==DietNumbers.SIZE){
					cancel();
				}
			}
		}, 50, 50);
	}
	@Override
	public void paint(Graphics g1){
		Graphics2D g = (Graphics2D)g1;
		g.setColor(darkerGray);
		FontMetrics fontMetrics = g.getFontMetrics();
		g.setFont(font1);
		int fontHeight = fontMetrics.getHeight();
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.WHITE);
		for(int i = 0; i<DietNumbers.SIZE; i++){
			int y = fontHeight*(i+1);
			g.drawString(DietNumbers.NAMES[i]+":", 1, y);
		}
		for(int i = 0; i<DietNumbers.SIZE; i++){
			if(broken[i]){
				g.setFont(font3);
			}else{
				g.setFont(font2);
			}
			fontMetrics = g.getFontMetrics();
			g.setColor(warningStage[i]);
			int y = fontHeight*(i+1);
			int x = (int)(getWidth()-(fontMetrics.getStringBounds(tempDietNumbers.stats[i]+"/"+maxDietNumbers.stats[i], g).getWidth()+10));
			g.drawString(tempDietNumbers.stats[i]+"/"+maxDietNumbers.stats[i], x, y);
		}
		g.dispose();
	}
	public void addFoodEntry(FoodEntry foodEntry){
		Loader.getResourceLoader().getMenu().add(foodEntry);
		Loader.getResourceLoader().save();
		for(int a = 0; a<DietNumbers.SIZE; a++){
			dietNumbers.stats[a] += foodEntry.getStats().stats[a];
		}
		reload();
		Loader.getInstance().getMenu().reload();
	}
	public DietNumbers getMaxStats(){
		return maxDietNumbers;
	}
	public DietNumbers getTempStats(){
		return tempDietNumbers;
	}
}
