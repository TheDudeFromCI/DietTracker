package me.ci;

import java.awt.Color;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class InfoPanel extends JFrame{
	private String msg;
	private Runnable onClose;
	public InfoPanel(String msg, Runnable onClose){
		Loader.POP_UP_OPEN=true;
		Loader.POP_UP=this;
		this.msg=msg;
		this.onClose=onClose;
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
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(Loader.POP_UP_OPEN&&Loader.POP_UP!=InfoPanel.this){
					Toolkit.getDefaultToolkit().beep();
					Loader.POP_UP.requestFocus();
					return;
				}
				Loader.POP_UP_OPEN=false;
				Loader.POP_UP=null;
				dispose();
				if(onClose!=null)onClose.run();
			}
		});
		btnOk.setBackground(Color.GRAY);
		btnOk.setForeground(Color.WHITE);
		panel.add(btnOk);
		JLabel label = new JLabel(msg);
		label.setForeground(Color.WHITE);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(label, BorderLayout.CENTER);
	}
}