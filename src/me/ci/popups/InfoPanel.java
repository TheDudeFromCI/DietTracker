package me.ci.popups;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import me.ci.Loader;

@SuppressWarnings("serial")
public class InfoPanel extends JFrame{
	private final String msg;
	private final Runnable onClose;
	public InfoPanel(String msg, Runnable onClose){
		Loader.POP_UP_OPEN = true;
		Loader.POP_UP = this;
		this.msg = msg;
		this.onClose = onClose;
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
		root.setBackground(Color.DARK_GRAY);
		root.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.gray, Color.black));
		getContentPane().add(root, BorderLayout.CENTER);
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		root.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				if(Loader.POP_UP_OPEN&&Loader.POP_UP!=InfoPanel.this){
					Toolkit.getDefaultToolkit().beep();
					Loader.POP_UP.requestFocus();
					return;
				}
				Loader.POP_UP_OPEN = false;
				Loader.POP_UP = null;
				dispose();
				if(onClose!=null){
					onClose.run();
				}
			}
		});
		btnOk.setBackground(Color.GRAY);
		btnOk.setForeground(Color.WHITE);
		panel.add(btnOk);
		JLabel label = new JLabel(msg);
		label.setForeground(Color.WHITE);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		setSize(new Dimension(label.getPreferredSize().width+50, 100));
		setLocationRelativeTo(null);
		root.add(label, BorderLayout.CENTER);
	}
}
