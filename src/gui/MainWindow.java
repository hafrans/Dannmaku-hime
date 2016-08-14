/*
 * Created by JFormDesigner on Mon Aug 08 17:56:26 CST 2016
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import com.jgoodies.forms.factories.*;

import beans.SettingValuesBeans;
import utils.ProgramUtils;


public class MainWindow {
	/**
	 * ���л�
	 */
	private static final long serialVersionUID = 154518528548548L;
	public boolean isLaunched = false;
	public Barrage _obj = null;
	public JFrame _window = null;
	public static SettingValuesBeans bean = new SettingValuesBeans();
	/**
	 * 
	 * @author Hafrans
	 */
	public MainWindow() {
		initComponents();
		initOtherComponents();
		// TODO ��ɹ��̺�Ψһ��ʾ���ô˴� jfm ������ɾ��
		this._window = mainWindow;
		this._window.pack();
		mainWindow.setVisible(true);
	}

	/**
	 * initialize other Components
	 * 
	 * @author Hafrans
	 */
	private void initOtherComponents() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				cbFont.setModel(new DefaultComboBoxModel<>(
						GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
			}
		});
		@SuppressWarnings("rawtypes")
		Class clazz = null;
		try {
			clazz = Class.forName("main");
			bean = (SettingValuesBeans) clazz.getField("bean").get(clazz.newInstance());
			if (bean == null) {
				throw new IllegalAccessException("Bean is not Found");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(mainWindow, "参数错误");
			System.exit(-1);
		}
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				heartBeat.setText(bean.getHeartbeat());
				cbFont.setSelectedItem(bean.getFontFace());
				cbSize.setSelectedItem(bean.getFontSize());
				textServer.setText(bean.getServerAddress());
				tfQueueLength.setText(bean.getQueueLength());
				tfStepTime.setText(bean.getStepTime());
				tfStep.setText(bean.getStep());
			}
		});

	}

	/**
	 * 
	 * 
	 * @param e
	 *            WindowEvent
	 */
	private void mainWindowWindowClosing(WindowEvent e) {
		int state = JOptionPane.showConfirmDialog(e.getComponent(), "即将退出系统" + '\n' + "是否加入系统托盘？", "ishi",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		switch (state) {
		case 0:
			System.out.println("Yes");
			new Tray(this);
			mainWindow.setVisible(false);
			break;
		case 1:
			ProgramUtils.exit(0, MainWindow.bean);
			break;
		case 2:
			return;
		}
		return;

	}

	private void cbFontItemStateChanged(ItemEvent e) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				testText.setFont(new Font((String) cbFont.getSelectedItem(), Font.PLAIN,
						Integer.parseInt((String) cbSize.getSelectedItem())));
			}
		});
	}

	private void testActionPerformed(ActionEvent e) {
		URL url = null;
		try {
			url = new URL(textServer.getText());
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(mainWindow, "URL 格式不正确" + '\n' + "请检查您的URL", "提示",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		System.out.println(url.getHost());
		Inet4Address addr = null;
		try {
			addr = (Inet4Address) InetAddress.getByName(url.getHost());
		} catch (UnknownHostException e1) {
			JOptionPane.showMessageDialog(mainWindow, "此URL找不到目标主机" + '\n' + "请检查您的URL", "提示",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		final Inet4Address myaddr = addr;
		JDialog jdi = new JDialog(mainWindow);
		jdi.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		jdi.setAlwaysOnTop(true);
		Container con = jdi.getContentPane();
		con.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
		button2.setEnabled(false);
		final JLabel jba = new JLabel("正在测试……");
		con.add(jba);
		final JButton jbtn = new JButton("确定");
		con.add(jbtn);
		jdi.setSize(200, 150);
		jdi.setLocationRelativeTo(mainWindow);
		jdi.setTitle("提示");
		jdi.setVisible(true);

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					if (myaddr.isReachable(5000)) {
						jba.setText("测试成功");
						// jdi.setVisible(true);
						System.out.println("success");
					} else {
						jba.setText("测试失败");
						// jdi.setVisible(true);
						System.out.println("failed");
					}
				} catch (IOException e) {
					jba.setText("测试错误");
					e.printStackTrace();
				}
				jbtn.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						jdi.dispose();
						button2.setEnabled(true);
					}
				});
			}

		}).start();

	}

	private void saveActionPerformed(ActionEvent e) {
		bean.setFontFace(cbFont.getSelectedItem());
		bean.setFontSize(cbSize.getSelectedItem());
		bean.setHeartbeat(heartBeat.getText());
		bean.setStep(tfStep.getText());
		bean.setQueueLength(tfQueueLength.getText());
		bean.setServerAddress(textServer.getText());
		bean.setStepTime(tfStepTime.getText());
		ProgramUtils.exit(111, bean);
		JOptionPane.showMessageDialog(mainWindow, "保存成功");
	}

	private void TCAMouseClicked(MouseEvent e) {
		JOptionPane.showMessageDialog(mainWindow, "(c) 2016 TCA"+"\n"+"Dannmaku hime"+"\n"+"弹幕姬"+"\n"+"弾幕(だんまく)姫（ひめ）", "声明", JOptionPane.PLAIN_MESSAGE);
	}

	public void runActionPerformed(ActionEvent e) {
		if (Integer.parseInt(tfQueueLength.getText()) <= 0 || Integer.parseInt(tfStep.getText()) <= 0
				|| Integer.parseInt((String) cbSize.getSelectedItem()) <= 0
				|| Integer.parseInt(tfRefreshTime.getText()) <= 0 || Integer.parseInt(tfStepTime.getText()) <= 0
				|| Integer.parseInt(tfPushTime.getText()) <= 0) {
			JOptionPane.showMessageDialog(mainWindow, "参数有误");
			return;
		}
		if (this.isLaunched == false) {
			this.isLaunched = true;
			_obj = new Barrage(Integer.parseInt(tfQueueLength.getText()), Integer.parseInt(tfStep.getText()),
					Integer.parseInt((String) cbSize.getSelectedItem()), Integer.parseInt(tfRefreshTime.getText()),
					Integer.parseInt(tfStepTime.getText()), Integer.parseInt(tfPushTime.getText()),
					(String) cbFont.getSelectedItem());
			System.out.println(_obj);
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					run.setText("停止弹幕");
					new Tray(MainWindow.this);
				}
			});
		} else {
			this.isLaunched = false;
			_obj.dispose();
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					run.setText("开始弹幕");
				}
			});

		}
	}

	private void aboutActionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(_window,
				"弹幕姬 0.4 Alpha" + "\n\n" + "(c) 2016 T.C.A" + '\n' + "济南大学图灵电脑协会" + '\n' + "turing.com" + "\n\n"
						+ "Client :" + '\n' + "(c) 2009-2016 Hafrans Stu." + '\n' + "hafrans.com",
				"关于", JOptionPane.DEFAULT_OPTION);
	}

	private void updateActionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(_window, "您的版本目前最新");
	}

	private void openSourceActionPerformed(ActionEvent e) {
		Desktop desk = Desktop.getDesktop();
		URI url;
		try {
			url = new URI("https://github.com/hafrans/Dannmaku-hime");
			desk.browse(url);
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	private void settingActionPerformed(ActionEvent e) {
		new AdvancedSetting(this);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		mainWindow = new JFrame();
		menuBar1 = new JMenuBar();
		menu1 = new JMenu();
		setting = new JMenuItem();
		menu2 = new JMenu();
		menuItemHelp = new JMenuItem();
		menuItemUpdate = new JMenuItem();
		menuItemAbout = new JMenuItem();
		openSource = new JMenuItem();
		label1 = new JLabel();
		textServer = new JTextField();
		fontSetting = new JPanel();
		label2 = new JLabel();
		cbFont = new JComboBox<>();
		label3 = new JLabel();
		cbSize = new JComboBox<>();
		scrollPane1 = new JScrollPane();
		panel1 = new JPanel();
		testText = new JLabel();
		label4 = new JLabel();
		heartBeat = new JTextField();
		label5 = new JLabel();
		button1 = new JButton();
		button2 = new JButton();
		run = new JButton();
		label6 = new JLabel();
		panel2 = new JPanel();
		label7 = new JLabel();
		tfStep = new JTextField();
		label8 = new JLabel();
		tfQueueLength = new JTextField();
		label9 = new JLabel();
		tfStepTime = new JTextField();
		label10 = new JLabel();
		tfRefreshTime = new JTextField();
		label11 = new JLabel();
		tfPushTime = new JTextField();

		//======== mainWindow ========
		{
			mainWindow.setTitle("\u5f39\u5e55\u59ec");
			mainWindow.setVisible(true);
			mainWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			mainWindow.setForeground(Color.darkGray);
			mainWindow.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					mainWindowWindowClosing(e);
				}
			});
			Container mainWindowContentPane = mainWindow.getContentPane();
			mainWindowContentPane.setLayout(null);

			//======== menuBar1 ========
			{

				//======== menu1 ========
				{
					menu1.setText("\u7a0b\u5e8f");

					//---- setting ----
					setting.setText("\u9ad8\u7ea7\u8bbe\u7f6e");
					setting.addActionListener(e -> settingActionPerformed(e));
					menu1.add(setting);
				}
				menuBar1.add(menu1);

				//======== menu2 ========
				{
					menu2.setText("\u5e2e\u52a9");

					//---- menuItemHelp ----
					menuItemHelp.setText("\u4f7f\u7528\u5e2e\u52a9");
					menu2.add(menuItemHelp);

					//---- menuItemUpdate ----
					menuItemUpdate.setText("\u8f6f\u4ef6\u66f4\u65b0");
					menuItemUpdate.addActionListener(e -> updateActionPerformed(e));
					menu2.add(menuItemUpdate);

					//---- menuItemAbout ----
					menuItemAbout.setText("\u5173\u4e8e");
					menuItemAbout.addActionListener(e -> aboutActionPerformed(e));
					menu2.add(menuItemAbout);

					//---- openSource ----
					openSource.setText("\u5f00\u6e90\u9879\u76ee");
					openSource.addActionListener(e -> openSourceActionPerformed(e));
					menu2.add(openSource);
				}
				menuBar1.add(menu2);
			}
			mainWindow.setJMenuBar(menuBar1);

			//---- label1 ----
			label1.setText("\u670d\u52a1\u5668\u5730\u5740");
			mainWindowContentPane.add(label1);
			label1.setBounds(new Rectangle(new Point(45, 30), label1.getPreferredSize()));

			//---- textServer ----
			textServer.setText("http://api.hafrans.com/barrage/api.php?action=gettext&privilege=public");
			mainWindowContentPane.add(textServer);
			textServer.setBounds(135, 25, 385, textServer.getPreferredSize().height);

			//======== fontSetting ========
			{
				fontSetting.setBorder(new TitledBorder("\u5b57\u4f53\u8bbe\u7f6e"));
				fontSetting.setLayout(null);

				//---- label2 ----
				label2.setText("\u5b57\u4f53");
				fontSetting.add(label2);
				label2.setBounds(new Rectangle(new Point(35, 35), label2.getPreferredSize()));

				//---- cbFont ----
				cbFont.setModel(new DefaultComboBoxModel<>(new String[] { "2", "2", "6", "4", "4" }));
				cbFont.addItemListener(e -> cbFontItemStateChanged(e));
				fontSetting.add(cbFont);
				cbFont.setBounds(70, 30, 185, 29);

				//---- label3 ----
				label3.setText("\u5b57\u53f7");
				fontSetting.add(label3);
				label3.setBounds(new Rectangle(new Point(260, 35), label3.getPreferredSize()));

				//---- cbSize ----
				cbSize.setModel(new DefaultComboBoxModel<>(
						new String[] { "28", "32", "36", "40", "46", "48", "52", "56", "64", "72", "88" }));
				cbSize.setSelectedIndex(5);
				cbSize.addItemListener(e -> cbFontItemStateChanged(e));
				fontSetting.add(cbSize);
				cbSize.setBounds(300, 30, 125, 29);

				//======== scrollPane1 ========
				{

					//======== panel1 ========
					{
						panel1.setBorder(new MatteBorder(1, 1, 1, 1, Color.black));
						panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));

						//---- testText ----
						testText.setText("TCA \u5f39\u5e55\u6d4b\u8bd5");
						panel1.add(testText);
					}
					scrollPane1.setViewportView(panel1);
				}
				fontSetting.add(scrollPane1);
				scrollPane1.setBounds(35, 80, 440, 100);
			}
			mainWindowContentPane.add(fontSetting);
			fontSetting.setBounds(35, 115, 490, 190);

			//---- label4 ----
			label4.setText("\u6293\u53d6\u9891\u7387");
			mainWindowContentPane.add(label4);
			label4.setBounds(55, 70, 75, 18);

			//---- heartBeat ----
			heartBeat.setText("3");
			mainWindowContentPane.add(heartBeat);
			heartBeat.setBounds(135, 65, 100, 24);

			//---- label5 ----
			label5.setText("\u79d2");
			mainWindowContentPane.add(label5);
			label5.setBounds(240, 70, 30, 18);

			//---- button1 ----
			button1.setText("\u4fdd\u5b58");
			button1.addActionListener(e -> saveActionPerformed(e));
			mainWindowContentPane.add(button1);
			button1.setBounds(new Rectangle(new Point(335, 485), button1.getPreferredSize()));

			//---- button2 ----
			button2.setText("\u6d4b\u8bd5");
			button2.addActionListener(e -> testActionPerformed(e));
			mainWindowContentPane.add(button2);
			button2.setBounds(new Rectangle(new Point(450, 65), button2.getPreferredSize()));

			//---- run ----
			run.setText("\u5f00\u542f\u5f39\u5e55");
			run.addActionListener(e -> runActionPerformed(e));
			mainWindowContentPane.add(run);
			run.setBounds(new Rectangle(new Point(430, 485), run.getPreferredSize()));

			//---- label6 ----
			label6.setText("(c) 2016 - TCA");
			label6.setForeground(SystemColor.windowBorder);
			label6.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					TCAMouseClicked(e);
				}
			});
			mainWindowContentPane.add(label6);
			label6.setBounds(new Rectangle(new Point(420, 525), label6.getPreferredSize()));

			//======== panel2 ========
			{
				panel2.setBorder(new TitledBorder(null, "\u538b\u529b\u6d4b\u8bd5", TitledBorder.RIGHT,
						TitledBorder.DEFAULT_POSITION, null, Color.red));
				panel2.setLayout(null);

				//---- label7 ----
				label7.setText("\u6b65\u8fdb");
				panel2.add(label7);
				label7.setBounds(45, 25, 35, label7.getPreferredSize().height);

				//---- tfStep ----
				tfStep.setText("6");
				panel2.add(tfStep);
				tfStep.setBounds(95, 20, 70, tfStep.getPreferredSize().height);

				//---- label8 ----
				label8.setText("\u961f\u5217\u957f\u5ea6");
				panel2.add(label8);
				label8.setBounds(new Rectangle(new Point(200, 25), label8.getPreferredSize()));

				//---- tfQueueLength ----
				tfQueueLength.setText("6");
				panel2.add(tfQueueLength);
				tfQueueLength.setBounds(270, 20, 90, tfQueueLength.getPreferredSize().height);

				//---- label9 ----
				label9.setText("\u6b65\u8fdb\u95f4\u9694");
				panel2.add(label9);
				label9.setBounds(new Rectangle(new Point(20, 60), label9.getPreferredSize()));

				//---- tfStepTime ----
				tfStepTime.setText("36");
				panel2.add(tfStepTime);
				tfStepTime.setBounds(95, 55, 75, tfStepTime.getPreferredSize().height);

				//---- label10 ----
				label10.setText("\u5237\u65b0\u95f4\u9694");
				panel2.add(label10);
				label10.setBounds(new Rectangle(new Point(200, 60), label10.getPreferredSize()));

				//---- tfRefreshTime ----
				tfRefreshTime.setText("200000");
				panel2.add(tfRefreshTime);
				tfRefreshTime.setBounds(270, 55, 90, tfRefreshTime.getPreferredSize().height);

				//---- label11 ----
				label11.setText("\u5f39\u5e55\u4ea7\u751f\u901f\u5ea6");
				panel2.add(label11);
				label11.setBounds(new Rectangle(new Point(20, 90), label11.getPreferredSize()));

				//---- tfPushTime ----
				tfPushTime.setText("1000");
				panel2.add(tfPushTime);
				tfPushTime.setBounds(120, 85, 240, tfPushTime.getPreferredSize().height);
			}
			mainWindowContentPane.add(panel2);
			panel2.setBounds(40, 325, 485, 140);

			mainWindowContentPane.setPreferredSize(new Dimension(570, 580));
			mainWindow.pack();
			mainWindow.setLocationRelativeTo(mainWindow.getOwner());
		}
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	private JFrame mainWindow;
	private JMenuBar menuBar1;
	private JMenu menu1;
	private JMenuItem setting;
	private JMenu menu2;
	private JMenuItem menuItemHelp;
	private JMenuItem menuItemUpdate;
	private JMenuItem menuItemAbout;
	private JMenuItem openSource;
	private JLabel label1;
	private JTextField textServer;
	private JPanel fontSetting;
	private JLabel label2;
	private JComboBox<String> cbFont;
	private JLabel label3;
	private JComboBox<String> cbSize;
	private JScrollPane scrollPane1;
	private JPanel panel1;
	private JLabel testText;
	private JLabel label4;
	private JTextField heartBeat;
	private JLabel label5;
	private JButton button1;
	private JButton button2;
	private JButton run;
	private JLabel label6;
	private JPanel panel2;
	private JLabel label7;
	private JTextField tfStep;
	private JLabel label8;
	private JTextField tfQueueLength;
	private JLabel label9;
	private JTextField tfStepTime;
	private JLabel label10;
	private JTextField tfRefreshTime;
	private JLabel label11;
	private JTextField tfPushTime;
	// JFormDesigner - End of variables declaration //GEN-END:variables


}
