/*
 * Created by JFormDesigner on Fri Aug 12 16:09:44 CST 2016
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.Properties;

import javax.swing.*;
import javax.swing.border.*;

import beans.SettingValuesBeans;

/**
 * @author Hafrans
 */
public class AdvancedSetting {
	private MainWindow window = null;
	private SettingValuesBeans bean = MainWindow.bean;
	
	public AdvancedSetting(MainWindow window) {
		this.window = window;
		initComponents();
		initOtherComponents();
	}

	private void initOtherComponents() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				cbFont.setModel(new DefaultComboBoxModel<>(
						GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
			}
		});
		
		
		
	}

	private void cbFontItemStateChanged(ItemEvent e) {
		
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		adset = new JFrame();
		button1 = new JButton();
		button2 = new JButton();
		tabbedPane1 = new JTabbedPane();
		panel3 = new JPanel();
		fontSetting = new JPanel();
		label5 = new JLabel();
		cbFont = new JComboBox<>();
		label6 = new JLabel();
		cbSize = new JComboBox<>();
		scrollPane1 = new JScrollPane();
		panel4 = new JPanel();
		testText = new JLabel();
		panel6 = new JPanel();
		panel1 = new JPanel();
		panel5 = new JPanel();
		tfStep = new JTextField();
		tfStepTime = new JTextField();
		label3 = new JLabel();
		label1 = new JLabel();
		label2 = new JLabel();
		tfQueueLength = new JTextField();
		panel2 = new JPanel();
		label4 = new JLabel();
		comboBox1 = new JComboBox<>();

		//======== adset ========
		{
			adset.setTitle("\u9ad8\u7ea7\u8bbe\u7f6e");
			adset.setAlwaysOnTop(true);
			adset.setVisible(true);
			Container adsetContentPane = adset.getContentPane();
			adsetContentPane.setLayout(null);

			//---- button1 ----
			button1.setText("\u4fdd\u5b58");
			adsetContentPane.add(button1);
			button1.setBounds(new Rectangle(new Point(305, 400), button1.getPreferredSize()));

			//---- button2 ----
			button2.setText("\u6062\u590d\u9ed8\u8ba4");
			adsetContentPane.add(button2);
			button2.setBounds(new Rectangle(new Point(400, 400), button2.getPreferredSize()));

			//======== tabbedPane1 ========
			{

				//======== panel3 ========
				{
					panel3.setLayout(null);

					//======== fontSetting ========
					{
						fontSetting.setBorder(new TitledBorder("\u5b57\u4f53\u8bbe\u7f6e"));
						fontSetting.setLayout(null);

						//---- label5 ----
						label5.setText("\u5b57\u4f53");
						fontSetting.add(label5);
						label5.setBounds(new Rectangle(new Point(35, 35), label5.getPreferredSize()));

						//---- cbFont ----
						cbFont.setModel(new DefaultComboBoxModel<>(new String[] { "2", "2", "6", "4", "4" }));
						cbFont.addItemListener(e -> cbFontItemStateChanged(e));
						fontSetting.add(cbFont);
						cbFont.setBounds(70, 30, 185, 29);

						//---- label6 ----
						label6.setText("\u5b57\u53f7");
						fontSetting.add(label6);
						label6.setBounds(new Rectangle(new Point(260, 35), label6.getPreferredSize()));

						//---- cbSize ----
						cbSize.setModel(new DefaultComboBoxModel<>(
								new String[] { "28", "32", "36", "40", "46", "48", "52", "56", "64", "72", "88" }));
						cbSize.setSelectedIndex(5);
						cbSize.addItemListener(e -> cbFontItemStateChanged(e));
						fontSetting.add(cbSize);
						cbSize.setBounds(300, 30, 125, 29);
					}
					panel3.add(fontSetting);
					fontSetting.setBounds(10, 20, 480, 80);

					//======== scrollPane1 ========
					{

						//======== panel4 ========
						{
							panel4.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));

							//---- testText ----
							testText.setText("TCA \u5f39\u5e55\u6d4b\u8bd5");
							panel4.add(testText);
						}
						scrollPane1.setViewportView(panel4);
					}
					panel3.add(scrollPane1);
					scrollPane1.setBounds(15, 230, 470, 100);

					//======== panel6 ========
					{
						panel6.setBorder(new TitledBorder("\u989c\u8272\u8bbe\u7f6e"));
						panel6.setLayout(null);
					}
					panel3.add(panel6);
					panel6.setBounds(15, 120, 470, 85);

					{ // compute preferred size
						Dimension preferredSize = new Dimension();
						for (int i = 0; i < panel3.getComponentCount(); i++) {
							Rectangle bounds = panel3.getComponent(i).getBounds();
							preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
							preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
						}
						Insets insets = panel3.getInsets();
						preferredSize.width += insets.right;
						preferredSize.height += insets.bottom;
						panel3.setMinimumSize(preferredSize);
						panel3.setPreferredSize(preferredSize);
					}
				}
				tabbedPane1.addTab("\u5f39\u5e55\u6837\u5f0f", panel3);

				//======== panel1 ========
				{
					panel1.setLayout(null);

					//======== panel5 ========
					{
						panel5.setBorder(new BevelBorder(BevelBorder.LOWERED));
						panel5.setLayout(null);

						//---- tfStep ----
						tfStep.setText("6");
						panel5.add(tfStep);
						tfStep.setBounds(80, 15, 90, 24);

						//---- tfStepTime ----
						tfStepTime.setText("36");
						panel5.add(tfStepTime);
						tfStepTime.setBounds(80, 65, 90, 24);

						//---- label3 ----
						label3.setText("\u6b65\u8fdb\u5ef6\u65f6");
						panel5.add(label3);
						label3.setBounds(10, 70, 60, 18);

						//---- label1 ----
						label1.setText("\u79fb\u52a8\u6b65\u957f");
						panel5.add(label1);
						label1.setBounds(10, 20, 60, 18);

						//---- label2 ----
						label2.setText("\u9632\u5806\u53e0\u961f\u5217\u957f\u5ea6");
						panel5.add(label2);
						label2.setBounds(185, 20, 105, 18);

						//---- tfQueueLength ----
						tfQueueLength.setText("6");
						panel5.add(tfQueueLength);
						tfQueueLength.setBounds(305, 15, 105, 25);
					}
					panel1.add(panel5);
					panel5.setBounds(20, 15, 460, 120);

					{ // compute preferred size
						Dimension preferredSize = new Dimension();
						for (int i = 0; i < panel1.getComponentCount(); i++) {
							Rectangle bounds = panel1.getComponent(i).getBounds();
							preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
							preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
						}
						Insets insets = panel1.getInsets();
						preferredSize.width += insets.right;
						preferredSize.height += insets.bottom;
						panel1.setMinimumSize(preferredSize);
						panel1.setPreferredSize(preferredSize);
					}
				}
				tabbedPane1.addTab("\u5f39\u5e55\u6027\u80fd", panel1);

				//======== panel2 ========
				{
					panel2.setLayout(null);

					//---- label4 ----
					label4.setText("\u4f20\u8f93\u534f\u8bae");
					panel2.add(label4);
					label4.setBounds(new Rectangle(new Point(30, 25), label4.getPreferredSize()));

					//---- comboBox1 ----
					comboBox1.setModel(new DefaultComboBoxModel<>(
							new String[] { "\u56fe\u7075\u5fae\u4fe1\u534f\u8bae", "HSDPv1.0" }));
					comboBox1.setEnabled(false);
					panel2.add(comboBox1);
					comboBox1.setBounds(95, 20, 185, comboBox1.getPreferredSize().height);

					{ // compute preferred size
						Dimension preferredSize = new Dimension();
						for (int i = 0; i < panel2.getComponentCount(); i++) {
							Rectangle bounds = panel2.getComponent(i).getBounds();
							preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
							preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
						}
						Insets insets = panel2.getInsets();
						preferredSize.width += insets.right;
						preferredSize.height += insets.bottom;
						panel2.setMinimumSize(preferredSize);
						panel2.setPreferredSize(preferredSize);
					}
				}
				tabbedPane1.addTab("\u7f51\u7edc", panel2);
			}
			adsetContentPane.add(tabbedPane1);
			tabbedPane1.setBounds(10, 5, 485, 380);

			{ // compute preferred size
				Dimension preferredSize = new Dimension();
				for (int i = 0; i < adsetContentPane.getComponentCount(); i++) {
					Rectangle bounds = adsetContentPane.getComponent(i).getBounds();
					preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
					preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
				}
				Insets insets = adsetContentPane.getInsets();
				preferredSize.width += insets.right;
				preferredSize.height += insets.bottom;
				adsetContentPane.setMinimumSize(preferredSize);
				adsetContentPane.setPreferredSize(preferredSize);
			}
			adset.setSize(520, 485);
			adset.setLocationRelativeTo(adset.getOwner());
		}
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JFrame adset;
	private JButton button1;
	private JButton button2;
	private JTabbedPane tabbedPane1;
	private JPanel panel3;
	private JPanel fontSetting;
	private JLabel label5;
	private JComboBox<String> cbFont;
	private JLabel label6;
	private JComboBox<String> cbSize;
	private JScrollPane scrollPane1;
	private JPanel panel4;
	private JLabel testText;
	private JPanel panel6;
	private JPanel panel1;
	private JPanel panel5;
	private JTextField tfStep;
	private JTextField tfStepTime;
	private JLabel label3;
	private JLabel label1;
	private JLabel label2;
	private JTextField tfQueueLength;
	private JPanel panel2;
	private JLabel label4;
	private JComboBox<String> comboBox1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
