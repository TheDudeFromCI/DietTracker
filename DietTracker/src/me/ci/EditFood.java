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
public class EditFood extends JFrame{
	private JTextField textField;
	private JSpinner spinner;
	private JSpinner spinner_1;
	private JSpinner spinner_2;
	private JSpinner spinner_3;
	private JSpinner spinner_4;
	private JSpinner spinner_5;
	private FoodEntry foodEntry;
	public EditFood(FoodEntry f){
		foodEntry=f;
		addWindowFocusListener(new WindowFocusListener(){
			public void windowGainedFocus(WindowEvent e){
				if(Loader.POP_UP!=EditFood.this){
					Toolkit.getDefaultToolkit().beep();
					requestFocus();
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
		setSize(320, 320);
		setLocationRelativeTo(null);
		setTitle("Edit Food Entry");
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
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.DARK_GRAY);
		getContentPane().add(panel_1);
		panel_1.setLayout(new MigLayout("", "[][grow][::100px,grow]", "[grow]"));
		JLabel lblCholesterol = new JLabel("Cholesterol:");
		lblCholesterol.setForeground(Color.LIGHT_GRAY);
		panel_1.add(lblCholesterol, "cell 0 0,alignx left,aligny center");
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(5)));
		panel_1.add(spinner, "cell 2 0,grow");
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.DARK_GRAY);
		getContentPane().add(panel_2);
		panel_2.setLayout(new MigLayout("", "[][grow][::100px,grow]", "[grow]"));
		JLabel lblSugar = new JLabel("Sugar:");
		lblSugar.setForeground(Color.LIGHT_GRAY);
		panel_2.add(lblSugar, "cell 0 0");
		spinner_1 = new JSpinner();
		spinner_1.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(5)));
		panel_2.add(spinner_1, "cell 2 0,grow");
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.DARK_GRAY);
		getContentPane().add(panel_3);
		panel_3.setLayout(new MigLayout("", "[][grow][::100px,grow]", "[grow]"));
		JLabel lblSodium = new JLabel("Sodium:");
		lblSodium.setForeground(Color.LIGHT_GRAY);
		panel_3.add(lblSodium, "cell 0 0");
		spinner_2 = new JSpinner();
		spinner_2.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(5)));
		panel_3.add(spinner_2, "cell 2 0,grow");
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(Color.DARK_GRAY);
		getContentPane().add(panel_4);
		panel_4.setLayout(new MigLayout("", "[][grow][::100px,grow]", "[grow]"));
		JLabel lblCalories = new JLabel("Calories:");
		lblCalories.setForeground(Color.LIGHT_GRAY);
		panel_4.add(lblCalories, "cell 0 0");
		spinner_3 = new JSpinner();
		spinner_3.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(5)));
		panel_4.add(spinner_3, "cell 2 0,grow");
		JPanel panel_6 = new JPanel();
		panel_6.setBackground(Color.DARK_GRAY);
		getContentPane().add(panel_6);
		panel_6.setLayout(new MigLayout("", "[][grow][::100px,grow]", "[grow]"));
		JLabel lblFat = new JLabel("Fat:");
		lblFat.setForeground(Color.LIGHT_GRAY);
		panel_6.add(lblFat, "cell 0 0");
		spinner_4 = new JSpinner();
		spinner_4.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(5)));
		panel_6.add(spinner_4, "cell 2 0,grow");
		JPanel panel_7 = new JPanel();
		panel_7.setBackground(Color.DARK_GRAY);
		getContentPane().add(panel_7);
		panel_7.setLayout(new MigLayout("", "[][grow][::100px,grow]", "[]"));
		JLabel lblCarbohydrates = new JLabel("Carbohydrates:");
		lblCarbohydrates.setForeground(Color.LIGHT_GRAY);
		panel_7.add(lblCarbohydrates, "cell 0 0");
		spinner_5 = new JSpinner();
		spinner_5.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(5)));
		panel_7.add(spinner_5, "cell 2 0,grow");
		JPanel panel_5 = new JPanel();
		panel_5.setBackground(Color.DARK_GRAY);
		FlowLayout flowLayout = (FlowLayout) panel_5.getLayout();
		flowLayout.setAlignment(FlowLayout.TRAILING);
		getContentPane().add(panel_5);
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(textField.getText().isEmpty()){
					Toolkit.getDefaultToolkit().beep();
					new InfoPanel("Name cannot be empty!", new Runnable(){
						public void run(){
							Loader.POP_UP_OPEN=true;
							Loader.POP_UP=EditFood.this;
						}
					});
					return;
				}
				foodEntry.setName(textField.getText());
				foodEntry.getStats().stats[0]=(int)spinner.getValue();
				foodEntry.getStats().stats[1]=(int)spinner_1.getValue();
				foodEntry.getStats().stats[2]=(int)spinner_2.getValue();
				foodEntry.getStats().stats[3]=(int)spinner_3.getValue();
				foodEntry.getStats().stats[4]=(int)spinner_4.getValue();
				foodEntry.getStats().stats[5]=(int)spinner_5.getValue();
				Loader.getResourceLoader().save();
				Loader.getResourceLoader().recountTodaysStats();
				Loader.getInstance().getCurrentStats().reload();
				Loader.getInstance().getFoodList().repaint();
				Loader.getInstance().getMenu().reload();
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
		textField.setText(foodEntry.getName());
		spinner.setValue(foodEntry.getStats().stats[0]);
		spinner_1.setValue(foodEntry.getStats().stats[1]);
		spinner_2.setValue(foodEntry.getStats().stats[2]);
		spinner_3.setValue(foodEntry.getStats().stats[3]);
		spinner_4.setValue(foodEntry.getStats().stats[4]);
		spinner_5.setValue(foodEntry.getStats().stats[5]);
	}
}