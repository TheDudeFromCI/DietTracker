package me.ci;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class Loader extends JFrame{
	private CurrentStats currentStats;
	private FoodList foodList;
	private HistoryGraph historyGraph;
	private WeightTracker weightTracker;
	private int x, y, w, h;
	private static ResourceLoader resourceLoader;
	public static boolean POP_UP_OPEN = false;
	public static JFrame POP_UP;
	private static Loader LOADER;
	private Menu menu;
	private Loader(){
		init();
		buildMainTab();
		setVisible(true);
		new LogList();
	}
	private void init(){
		setTitle("Diet Tracker");
		setSize(1100, 640);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.DARK_GRAY);
		setMinimumSize(new Dimension(1100, 640));
		setUndecorated(true);
		addWindowFocusListener(new WindowFocusListener(){
			@Override
			public void windowGainedFocus(WindowEvent e){
				if(Loader.POP_UP_OPEN){
					Toolkit.getDefaultToolkit().beep();
					Loader.POP_UP.requestFocus();
				}
			}
			@Override
			public void windowLostFocus(WindowEvent e){}
		});
	}
	public void maximize(){
		x = getX();
		y = getY();
		w = getWidth();
		h = getHeight();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	public void buildMainTab(){
		getContentPane().removeAll();
		Toolbar toolbar = new Toolbar(this, 0);
		getContentPane().add(toolbar, BorderLayout.NORTH);
		getContentPane().add(foodList = new FoodList(), BorderLayout.WEST);
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		OptionsMenu optionsMenu = new OptionsMenu();
		panel.add(optionsMenu, BorderLayout.SOUTH);
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		panel_1.add(currentStats = new CurrentStats(), BorderLayout.EAST);
		panel_1.add(menu = new Menu(), BorderLayout.CENTER);
		panel.add(historyGraph = new HistoryGraph(), BorderLayout.CENTER);
		validate();
		repaint();
	}
	public void buildWeightTrackerTab(){
		getContentPane().removeAll();
		Toolbar toolbar = new Toolbar(this, 1);
		getContentPane().add(toolbar, BorderLayout.NORTH);
		getContentPane().add(weightTracker = new WeightTracker(), BorderLayout.CENTER);
		validate();
		repaint();
	}
	public void normalize(){
		setExtendedState(JFrame.NORMAL);
		setBounds(x, y, w, h);
	}
	public void minimize(){
		setExtendedState(JFrame.ICONIFIED);
	}
	public void setPosition(int x, int y){
		setLocation(x, y);
	}
	public FoodList getFoodList(){
		return foodList;
	}
	public CurrentStats getCurrentStats(){
		return currentStats;
	}
	public Menu getMenu(){
		return menu;
	}
	public HistoryGraph getHistoryGraph(){
		return historyGraph;
	}
	public WeightTracker getWeightTracker(){
		return weightTracker;
	}
	public static void main(String[] args){
		resourceLoader = new ResourceLoader();
		try{
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		}catch(Throwable exception){
			exception.printStackTrace();
		}
		LOADER = new Loader();
	}
	public static ResourceLoader getResourceLoader(){
		return resourceLoader;
	}
	public static Loader getInstance(){
		return LOADER;
	}
}
