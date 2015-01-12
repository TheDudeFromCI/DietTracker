package me.ci;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.TRAILING);
		JButton btnClearTodaysMenu = new JButton("Clear Today's Menu");
		btnClearTodaysMenu.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Loader.getResourceLoader().getMenu().clear();
				Loader.getResourceLoader().loadTodaysStats().clear();
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
			spinner.setModel(new SpinnerNumberModel(new Integer(Loader.getResourceLoader().loadMaxDiet().stats[a]), new Integer(0), null, new Integer(1)));
			namePanel.add(spinner);
			panel.add(namePanel);
			spinner.addChangeListener(new ChangeListener(){
				public void stateChanged(ChangeEvent e){
					Loader.getInstance().getCurrentStats().getMaxStats().stats[a]=(int)spinner.getValue();
					Loader.getInstance().getCurrentStats().getTempStats().stats[a]=0;
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