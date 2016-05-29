package me.ci;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class LogWeightPopup extends JFrame{
	public LogWeightPopup(){
		addWindowFocusListener(new WindowFocusListener(){
			@Override
			public void windowGainedFocus(WindowEvent e){
				if(Loader.POP_UP_OPEN&&Loader.POP_UP!=LogWeightPopup.this){
					Toolkit.getDefaultToolkit().beep();
					Loader.POP_UP.requestFocus();
				}
			}
			@Override
			public void windowLostFocus(WindowEvent e){}
		});
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				close();
			}
		});
		Loader.POP_UP_OPEN = true;
		Loader.POP_UP = this;
		init();
		addComponents();
		setVisible(true);
	}
	private void init(){
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setSize(253, 171);
		setLocationRelativeTo(null);
		setTitle("Log Today's Weight");
		getContentPane().setBackground(Color.DARK_GRAY);
		getContentPane().setLayout(new BorderLayout(0, 0));
	}
	private void addComponents(){
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		JLabel lblPleaseEnterTodays = new JLabel("Please Enter");
		panel.add(lblPleaseEnterTodays);
		lblPleaseEnterTodays.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblPleaseEnterTodays.setForeground(Color.LIGHT_GRAY);
		lblPleaseEnterTodays.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel lblTodaysWeight = new JLabel("Today's Weight");
		lblTodaysWeight.setHorizontalAlignment(SwingConstants.CENTER);
		lblTodaysWeight.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblTodaysWeight.setForeground(Color.LIGHT_GRAY);
		panel.add(lblTodaysWeight);
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.DARK_GRAY);
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		final JSpinner spinner = new JSpinner();
		spinner.setForeground(Color.BLACK);
		spinner.setBackground(Color.LIGHT_GRAY);
		spinner.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		spinner.setPreferredSize(new Dimension(100, 25));
		spinner.setModel(new SpinnerNumberModel(200.0, 1.0, 800.0, 0.1));
		panel_1.add(spinner);
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				Loader.getInstance().getWeightTracker().logWeight((int)((double)spinner.getValue()*10));
				close();
			}
		});
		panel_1.add(btnOk);
	}
	private void close(){
		Loader.POP_UP_OPEN = false;
		Loader.POP_UP = null;
		dispose();
	}
}
