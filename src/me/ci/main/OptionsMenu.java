package me.ci.main;

import me.ci.popups.InfoPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import me.ci.util.DietNumbers;
import me.ci.Loader;

@SuppressWarnings("serial")
public class OptionsMenu extends JPanel{
	public OptionsMenu(){
		setBackground(Color.DARK_GRAY);
		setLayout(new BorderLayout(0, 0));
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.DARK_GRAY);
		add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));
		JPanel panel = new JPanel();
		panel_2.add(panel, BorderLayout.NORTH);
		panel.setBackground(Color.DARK_GRAY);
		panel.setLayout(new GridLayout(2, 0, 10, 0));
		JPanel panel_1 = new JPanel();
		panel_2.add(panel_1, BorderLayout.SOUTH);
		panel_1.setBackground(Color.DARK_GRAY);
		FlowLayout flowLayout = (FlowLayout)panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.TRAILING);
		JButton btnClearTodaysMenu = new JButton("Clear Today's Menu");
		btnClearTodaysMenu.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				// First check to make sure this is a good idea.
				if(Loader.getResourceLoader().getWeight()==0){
					new InfoPanel("Please enter today's weight before clearing today's stats.", null);
					return;
				}
				Loader.getResourceLoader().newDay();
				Loader.getInstance().getCurrentStats().reload();
				Loader.getInstance().getMenu().reload();
				Loader.getResourceLoader().save();
			}
		});
		btnClearTodaysMenu.setBackground(Color.GRAY);
		btnClearTodaysMenu.setForeground(Color.WHITE);
		panel_1.add(btnClearTodaysMenu);
		int i = 0;
		for(String s : DietNumbers.NAMES){
			final int a = i;
			i++;
			JPanel namePanel = new JPanel();
			namePanel.setLayout(new GridLayout(2, 0, 0, 0));
			namePanel.setBackground(Color.DARK_GRAY);
			JLabel lblMaxSugar = new JLabel(s);
			lblMaxSugar.setHorizontalAlignment(SwingConstants.CENTER);
			lblMaxSugar.setForeground(Color.WHITE);
			namePanel.add(lblMaxSugar);
			final JSpinner spinner = new JSpinner();
			spinner.setModel(new SpinnerNumberModel(Loader.getResourceLoader().loadMaxDiet().stats[a], 0, null, 1));
			namePanel.add(spinner);
			panel.add(namePanel);
			spinner.addChangeListener(new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e){
					Loader.getInstance().getCurrentStats().getMaxStats().stats[a] = (int)spinner.getValue();
					Loader.getInstance().getCurrentStats().getTempStats().stats[a] = 0;
					Loader.getInstance().getCurrentStats().reload();
					Loader.getInstance().getFoodList().repaint();
					Loader.getResourceLoader().save();
				}
			});
		}
		JLabel lblDailyMaxValues = new JLabel("Daily Max Values");
		lblDailyMaxValues.setForeground(new Color(0.7f, 0.7f, 0.7f));
		lblDailyMaxValues.setFont(new Font("Dialog", Font.BOLD|Font.ITALIC, 20));
		lblDailyMaxValues.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblDailyMaxValues, BorderLayout.NORTH);
	}
}
