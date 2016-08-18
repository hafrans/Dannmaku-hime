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
import javax.swing.event.*;
import com.jgoodies.forms.factories.*;

import beans.SettingValuesBeans;
import utils.ProgramUtils;


public class MainWindow implements Saveable {

	private static final long serialVersionUID = 154518528548548L;
	public boolean isLaunched = false;
	public Barrage _obj = null;
	public JFrame _window = null;
	private String version = "0.9.7 Beta";
	public static SettingValuesBeans bean = new SettingValuesBeans();
	/**
	 * 
	 * @author Hafrans
	 */
	public MainWindow() {
		initComponents();
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
		initOtherComponents();
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
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				heartBeat.setText(bean.getHeartbeat());
				cbFont.setSelectedItem(bean.getFontFace());
				cbSize.setSelectedItem(bean.getFontSize());
				textServer.setText(bean.getServerAddress());
				testText.setForeground(Color.decode(bean.getColor()));
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
		int state = JOptionPane.showConfirmDialog(e.getComponent(), "即将退出系统" + '\n' + "是否加入系统托盘？", "提示",
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
		save();
	}

	private void TCAMouseClicked(MouseEvent e) {
		JOptionPane.showMessageDialog(mainWindow, "(c) 2016 TCA"+"\n"+"Dannmaku hime"+"\n"+"弹幕姬"+"\n"+"弾幕(だんまく)姫（ひめ）", "声明", JOptionPane.PLAIN_MESSAGE);
	}

	public void runActionPerformed(ActionEvent e) {
	

		if (this.isLaunched == false) {
			
			
			
			
			if(cbDebug.isSelected()){
				try{
					_obj = new Barrage(Integer.parseInt(bean.getQueueLength()), Integer.parseInt(bean.getStep()),
							Integer.parseInt(bean.getFontSize()),0,
							Integer.parseInt(bean.getStepTime()),Integer.parseInt(tfPushTime.getText().trim()),
							(String) cbFont.getSelectedItem());
				}catch (Exception e1) {
					JOptionPane.showMessageDialog(_window, "请检查的您的参数配置是否有问题");
					e1.printStackTrace();
					return;
				}
				System.out.println("Debug start");
			}else{
				try{
					_obj = new Barrage(bean,this);
				}catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(_window, "请检查的您的参数配置是否有问题");
					e1.printStackTrace();
				}catch (Exception e2) {
					JOptionPane.showMessageDialog(_window, "未知异常");
					e2.printStackTrace();
				}
			}
			
			
			
			
			
			
			this.isLaunched = true;
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
				"弹幕姬 "+version+ "\n\n" + "(c) 2016 T.C.A" + '\n' + "济南大学图灵电脑协会" + '\n' + "turing.com" + "\n\n"
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

	private void cbDebugActionPerformed(ActionEvent e) {
		JCheckBoxMenuItem cb = (JCheckBoxMenuItem) e.getSource();
		if(cb.getState()){
			debugPanel.setVisible(true);
		}else{
			debugPanel.setVisible(false);
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		mainWindow = new JFrame();
		menuBar1 = new JMenuBar();
		menu1 = new JMenu();
		setting = new JMenuItem();
		cbDebug = new JCheckBoxMenuItem();
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
		debugPanel = new JPanel();
		label7 = new JLabel();
		tfPushTime = new JTextField();

		//======== mainWindow ========
		{
			mainWindow.setTitle("\u5f39\u5e55\u59ec");
			//mainWindow.setVisible(true);
			mainWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			mainWindow.setForeground(Color.darkGray);
			mainWindow.setIconImage(((ImageIcon) UIManager.getIcon("FileView.computerIcon")).getImage());
			mainWindow.setMinimumSize(new Dimension(4, 4));
			mainWindow.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
			mainWindow.setResizable(false);
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
				menuBar1.setAutoscrolls(true);
				menuBar1.setMinimumSize(new Dimension(2, 1000));

				//======== menu1 ========
				{
					menu1.setText("\u7a0b\u5e8f");

					//---- setting ----
					setting.setText("\u9ad8\u7ea7\u8bbe\u7f6e");
					setting.addActionListener(e -> settingActionPerformed(e));
					menu1.add(setting);

					//---- cbDebug ----
					cbDebug.setText("\u8c03\u8bd5\u5f39\u5e55");
					cbDebug.addActionListener(e -> cbDebugActionPerformed(e));
					menu1.add(cbDebug);
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
					menu2.addSeparator();

					//---- openSource ----
					openSource.setText("\u5f00\u6e90\u9879\u76ee");
					openSource.addActionListener(e -> openSourceActionPerformed(e));
					menu2.add(openSource);
				}
				menuBar1.add(menu2);
			}
			mainWindowContentPane.add(menuBar1);
			menuBar1.setBounds(0, 0, 554, 26);

			//---- label1 ----
			label1.setText("\u670d\u52a1\u5668\u5730\u5740");
			mainWindowContentPane.add(label1);
			label1.setBounds(30, 55, 75, 18);

			//---- textServer ----
			textServer.setText("http://api.hafrans.com/dannmaku/getmsg.php");
			mainWindowContentPane.add(textServer);
			textServer.setBounds(115, 50, 385, 24);

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
			fontSetting.setBounds(25, 140, 490, 190);

			//---- label4 ----
			label4.setText("\u6293\u53d6\u9891\u7387");
			mainWindowContentPane.add(label4);
			label4.setBounds(45, 95, 75, 18);

			//---- heartBeat ----
			heartBeat.setText("3");
			mainWindowContentPane.add(heartBeat);
			heartBeat.setBounds(115, 90, 100, 24);

			//---- label5 ----
			label5.setText("\u79d2");
			mainWindowContentPane.add(label5);
			label5.setBounds(225, 95, 30, 18);

			//---- button1 ----
			button1.setText("\u4fdd\u5b58");
			button1.addActionListener(e -> saveActionPerformed(e));
			mainWindowContentPane.add(button1);
			button1.setBounds(340, 380, 65, 27);

			//---- button2 ----
			button2.setText("\u6d4b\u8bd5");
			button2.addActionListener(e -> testActionPerformed(e));
			mainWindowContentPane.add(button2);
			button2.setBounds(435, 90, 65, 27);

			//---- run ----
			run.setText("\u5f00\u542f\u5f39\u5e55");
			run.addActionListener(e -> runActionPerformed(e));
			mainWindowContentPane.add(run);
			run.setBounds(420, 380, 95, 27);

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
			label6.setBounds(10, 390, 112, 18);

			//======== debugPanel ========
			{
				debugPanel.setVisible(false);
				debugPanel.setLayout(null);

				//---- label7 ----
				label7.setText("\u63a8\u9001\u95f4\u9694(s)");
				debugPanel.add(label7);
				label7.setBounds(0, 5, 84, 18);

				//---- tfPushTime ----
				tfPushTime.setText("1000");
				debugPanel.add(tfPushTime);
				tfPushTime.setBounds(90, 0, 105, 24);
			}
			mainWindowContentPane.add(debugPanel);
			debugPanel.setBounds(55, 351, 200, 32);

			mainWindowContentPane.setPreferredSize(new Dimension(555, 435));
			mainWindow.pack();
			mainWindow.setLocationRelativeTo(null);
		}
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	private JFrame mainWindow;
	private JMenuBar menuBar1;
	private JMenu menu1;
	private JMenuItem setting;
	private JCheckBoxMenuItem cbDebug;
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
	private JPanel debugPanel;
	private JLabel label7;
	private JTextField tfPushTime;
	// JFormDesigner - End of variables declaration //GEN-END:variables
	@Override
	public void save() {
		if (bean == null) {
			throw new NullPointerException("NULL BEANS");
		}
		if ( heartBeat.getText().trim().length() <=0) {
			JOptionPane.showMessageDialog(_window, "缺项！");
		}
		try{
			bean.setFontFace(cbFont.getSelectedItem());
			bean.setFontSize(cbSize.getSelectedItem());
			bean.setServerAddress(textServer.getText().trim());
			bean.setHeartbeat(heartBeat.getText().trim());
		}catch(Exception e){
			JOptionPane.showMessageDialog(_window, "检查您输入的信息是否正确！");
		}
		ProgramUtils.exit(111, bean);
		JOptionPane.showMessageDialog(_window, "保存成功");
	}

	@Override
	public void saveFlush() {
		initOtherComponents();
		
	}


}
