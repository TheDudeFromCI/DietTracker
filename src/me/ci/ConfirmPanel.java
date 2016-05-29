package me.ci;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class ConfirmPanel extends JFrame{
	private final String msg;
	private final Runnable yes, no;
	public ConfirmPanel(String msg, Runnable yes, Runnable no){
		Loader.POP_UP_OPEN = true;
		Loader.POP_UP = this;
		this.msg = msg;
		this.yes = yes;
		this.no = no;
		init();
		addComponents();
		setVisible(true);
	}
	private void init(){
		setTitle("Confirm");
		setResizable(false);
		setSize(320, 100);
		setLocationRelativeTo(null);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getContentPane().setBackground(Color.DARK_GRAY);
	}
	private void addComponents(){
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		JButton btnYes = new JButton("Yes");
		btnYes.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				Loader.POP_UP_OPEN = false;
				Loader.POP_UP = null;
				dispose();
				if(yes!=null){
					yes.run();
				}
			}
		});
		btnYes.setBackground(Color.GRAY);
		btnYes.setForeground(Color.WHITE);
		panel.add(btnYes);
		JButton btnNo = new JButton("No");
		btnNo.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				Loader.POP_UP_OPEN = false;
				Loader.POP_UP = null;
				dispose();
				if(no!=null){
					no.run();
				}
			}
		});
		btnNo.setForeground(Color.WHITE);
		btnNo.setBackground(Color.GRAY);
		panel.add(btnNo);
		JLabel label = new JLabel(msg);
		label.setForeground(Color.WHITE);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(label, BorderLayout.CENTER);
	}
}
