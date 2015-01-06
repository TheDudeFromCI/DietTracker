package me.ci;

import javax.swing.JFrame;
import javax.swing.UIManager;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import javax.swing.JPanel;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.GridLayout;

@SuppressWarnings("serial")
public class Loader extends JFrame{
	private CurrentStats currentStats;
	private FoodList foodList;
	private int x, y, w, h;
	private static ResourceLoader resourceLoader;
	public static boolean POP_UP_OPEN = false;
	public static JFrame POP_UP;
	private static Loader LOADER;
	private Menu menu;
	private Loader(){
		init();
		addComponents();
		setVisible(true);
	}
	private void init(){
		setTitle("Diet Tracker");
		setSize(900, 640);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.DARK_GRAY);
		setMinimumSize(new Dimension(900, 480));
		setUndecorated(true);
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
		Toolbar toolbar = new Toolbar(this);
		getContentPane().add(toolbar, BorderLayout.NORTH);
		getContentPane().add(foodList=new FoodList(), BorderLayout.WEST);
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.WEST);
		panel_2.setLayout(new GridLayout(0, 1, 0, 0));
		menu=new Menu();
		panel_2.add(menu);
		OptionsMenu optionsMenu = new OptionsMenu();
		panel.add(optionsMenu, BorderLayout.SOUTH);
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		getContentPane().add(currentStats=new CurrentStats(), BorderLayout.EAST);
		panel_1.add(currentStats, BorderLayout.NORTH);
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
	public Menu getMenu(){ return menu; }
	public static void main(String[] args){
		resourceLoader=new ResourceLoader();
		try{ UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		}catch(Throwable exception){ exception.printStackTrace(); }
		LOADER=new Loader();
	}
	public static ResourceLoader getResourceLoader(){ return resourceLoader; }
	public static Loader getInstance(){ return LOADER; }
}