package me.ci;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

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
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getContentPane().setBackground(Color.DARK_GRAY);
	}
	private void addComponents(){
		JPanel root = new JPanel();
		root.setLayout(new BorderLayout());
		root.setBackground(Color.darkGray);
		root.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.gray, Color.black));
		getContentPane().add(root, BorderLayout.CENTER);
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		root.add(panel, BorderLayout.SOUTH);
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
		root.add(label, BorderLayout.CENTER);
		setSize(new Dimension(label.getPreferredSize().width+50, 100));
		setLocationRelativeTo(null);
	}
}
