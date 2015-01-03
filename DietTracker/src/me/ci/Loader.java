package me.ci;

import javax.swing.JFrame;
import javax.swing.UIManager;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

@SuppressWarnings({"serial", "unused"})
public class Loader extends JFrame{
	private CurrentStats currentStats;
	private Toolbar toolbar;
	private FoodList foodList;
	private int x, y, w, h;
	private static ResourceLoader resourceLoader;
	public static boolean POP_UP_OPEN = false;
	public static JFrame POP_UP;
	private static Loader LOADER;
	private Loader(){
		init();
		addComponents();
		setVisible(true);
	}
	private void init(){
		setTitle("Diet Tracker");
		setSize(900, 480);
		setLocationRelativeTo(null);
		getContentPane().setBackground(Color.DARK_GRAY);
		setMinimumSize(new Dimension(900, 480));
		setUndecorated(true);
		ComponentResizer cr = new ComponentResizer();
		cr.registerComponent(this);
		cr.setMinimumSize(new Dimension(900, 480));
		addWindowFocusListener(new WindowFocusListener(){
			public void windowGainedFocus(WindowEvent e){
				if(Loader.POP_UP_OPEN){
					Toolkit.getDefaultToolkit().beep();
					Loader.POP_UP.requestFocus();
				}
			}
			public void windowLostFocus(WindowEvent e){}
		});
	}
	private void addComponents(){
		getContentPane().add(toolbar=new Toolbar(this), BorderLayout.NORTH);
		getContentPane().add(foodList=new FoodList(), BorderLayout.WEST);
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		getContentPane().add(currentStats=new CurrentStats(), BorderLayout.EAST);
		FlowLayout flowLayout = (FlowLayout)currentStats.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		panel.add(currentStats, BorderLayout.CENTER);
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.SOUTH);
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.WEST);
	}
	public void maximize(){
		x=getX();
		y=getY();
		w=getWidth();
		h=getHeight();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	public void normalize(){
		setExtendedState(JFrame.NORMAL);
		setBounds(x, y, w, h);
	}
	public void minimize(){ setExtendedState(JFrame.ICONIFIED); }
	public void setPosition(int x, int y){ setLocation(x, y); }
	public FoodList getFoodList(){ return foodList; }
	public CurrentStats getCurrentStats(){ return currentStats; }
	public static void main(String[] args){
		resourceLoader=new ResourceLoader();
		try{ UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		}catch(Throwable exception){ exception.printStackTrace(); }
		LOADER=new Loader();
	}
	public static ResourceLoader getResourceLoader(){ return resourceLoader; }
	public static Loader getInstance(){ return LOADER; }
}