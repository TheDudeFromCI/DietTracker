package me.ci;

import javax.swing.JFrame;
import java.awt.event.WindowEvent;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowFocusListener;

@SuppressWarnings("serial")
public class AddNewFood extends JFrame{
	private JTextField textField, textField_1;
	private JSpinner[] spinners;
	public AddNewFood(){
		addWindowFocusListener(new WindowFocusListener(){
			public void windowGainedFocus(WindowEvent e){
				if(Loader.POP_UP_OPEN&&Loader.POP_UP!=AddNewFood.this){
					Toolkit.getDefaultToolkit().beep();
					Loader.POP_UP.requestFocus();
				}
			}
			public void windowLostFocus(WindowEvent e){}
		});
		Loader.POP_UP_OPEN=true;
		Loader.POP_UP=this;
		init();
		addComponents();
		setVisible(true);
	}
	private void init(){
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setSize(320, 640);
		setLocationRelativeTo(null);
		setTitle("New Food Entry");
		getContentPane().setBackground(Color.DARK_GRAY);
		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
	}
	private void addComponents(){
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		getContentPane().add(panel);
		panel.setLayout(new MigLayout("", "[center][40px:n][grow]", "[grow,center]"));
		JLabel lblName = new JLabel("Name:");
		lblName.setForeground(Color.LIGHT_GRAY);
		panel.add(lblName, "cell 0 0,alignx left,aligny center");
		textField = new JTextField();
		textField.setFont(new Font("Dialog", Font.BOLD, 15));
		textField.setForeground(Color.BLACK);
		textField.setBackground(Color.GRAY);
		panel.add(textField, "cell 2 0,growx");
		textField.setColumns(10);
		JPanel categoryPanel = new JPanel();
		categoryPanel.setBackground(Color.DARK_GRAY);
		getContentPane().add(categoryPanel);
		categoryPanel.setLayout(new MigLayout("", "[][::40px,grow][grow]", "[grow]"));
		JLabel categoryLbl = new JLabel("Category");
		categoryLbl.setForeground(Color.LIGHT_GRAY);
		categoryPanel.add(categoryLbl, "cell 0 0,alignx left,aligny center");
		textField_1=new JTextField();
		textField_1.setFont(new Font("Dialog", Font.BOLD, 15));
		textField_1.setForeground(Color.BLACK);
		textField_1.setBackground(Color.GRAY);
		categoryPanel.add(textField_1, "cell 2 0,grow");
		int index = 0;
		spinners=new JSpinner[DietNumbers.SIZE];
		for(String types : DietNumbers.NAMES){
			JPanel panel_1 = new JPanel();
			panel_1.setBackground(Color.DARK_GRAY);
			getContentPane().add(panel_1);
			panel_1.setLayout(new MigLayout("", "[][grow][::100px,grow]", "[grow]"));
			JLabel lblCholesterol = new JLabel(types+":");
			lblCholesterol.setForeground(Color.LIGHT_GRAY);
			panel_1.add(lblCholesterol, "cell 0 0,alignx left,aligny center");
			spinners[index]=new JSpinner();
			spinners[index].setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
			panel_1.add(spinners[index], "cell 2 0,grow");
			index++;
		}
		JPanel panel_5 = new JPanel();
		panel_5.setBackground(Color.DARK_GRAY);
		FlowLayout flowLayout = (FlowLayout) panel_5.getLayout();
		flowLayout.setAlignment(FlowLayout.TRAILING);
		getContentPane().add(panel_5);
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(Loader.POP_UP_OPEN&&Loader.POP_UP!=AddNewFood.this){
					Toolkit.getDefaultToolkit().beep();
					Loader.POP_UP.requestFocus();
					return;
				}
				if(textField.getText().isEmpty()){
					Toolkit.getDefaultToolkit().beep();
					new InfoPanel("Name cannot be empty!", new Runnable(){
						public void run(){
							Loader.POP_UP_OPEN=true;
							Loader.POP_UP=AddNewFood.this;
						}
					});
					return;
				}
				if(textField_1.getText().isEmpty()){
					Toolkit.getDefaultToolkit().beep();
					new InfoPanel("Category cannot be empty!", new Runnable(){
						public void run(){
							Loader.POP_UP_OPEN=true;
							Loader.POP_UP=AddNewFood.this;
						}
					});
					return;
				}
				FoodEntry foodEntry = new FoodEntry(textField.getText());
				foodEntry.setCetegory(textField_1.getText());
				for(int i = 0; i<DietNumbers.SIZE; i++)foodEntry.getStats().stats[i]=(int)spinners[i].getValue();
				Loader.getInstance().getFoodList().addFoodEntry(foodEntry);
				Loader.POP_UP_OPEN=false;
				Loader.POP_UP=null;
				dispose();
			}
		});
		btnOk.setForeground(Color.WHITE);
		btnOk.setBackground(Color.GRAY);
		panel_5.add(btnOk);
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Loader.POP_UP_OPEN=false;
				Loader.POP_UP=null;
				dispose();
			}
		});
		btnCancel.setForeground(Color.WHITE);
		btnCancel.setBackground(Color.GRAY);
		panel_5.add(btnCancel);
	}
}