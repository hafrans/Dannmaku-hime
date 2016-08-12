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
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import com.jgoodies.forms.factories.*;

import utils.ProgramUtils;

/**
 * 设置主界面
 * 
 * @author Hafrans
 */
public class MainWindow {
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 154518528548548L;
	public boolean isLaunched = false;
	public Barrage _obj = null;
	public JFrame _window = null;

	/**
	 * 获取实例
	 * 
	 * @author Hafrans
	 */
	public MainWindow() {
		initComponents();
		initOtherComponents();
		// TODO 完成工程后将唯一显示放置此处 jfm 处自行删除
		this._window = mainWindow;
		mainWindow.setVisible(true);
	}

	/**
	 * 初试化其他组件
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
			pro = (Properties) clazz.getField("pro").get(clazz.newInstance());
			if (pro == null) {
				throw new IllegalAccessException("获取不到资源");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(mainWindow, "无法获取系统资源！");
			System.exit(-1);
		}
		final Properties _pro = pro;
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				heartBeat.setText(_pro.getProperty("heartbeat"));
				cbFont.setSelectedItem(_pro.getProperty("font-face"));
				cbSize.setSelectedItem(_pro.getProperty("text-size"));
			}
		});

	}

	/**
	 * 窗口关闭的事件处理
	 * 
	 * @param e
	 *            WindowEvent
	 */
	private void mainWindowWindowClosing(WindowEvent e) {
		int state = JOptionPane.showConfirmDialog(e.getComponent(), "是否将程序加入系统托盘？" + '\n' + "不加入系统托盘将退出该程序", "退出提示",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		switch (state) {
		case 0:
			System.out.println("Yes");
			new Tray(this);
			// TODO 用WINAPI with JNI 注册全局键盘监听
			mainWindow.setVisible(false);
			break;
		case 1:
			ProgramUtils.exit(0, MainWindow.pro);
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
			JOptionPane.showMessageDialog(mainWindow, "URL 格式不正确" + '\n' + "请输入正确的URL地址", "警示",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		System.out.println(url.getHost());
		Inet4Address addr = null;
		try {
			addr = (Inet4Address) InetAddress.getByName(url.getHost());
		} catch (UnknownHostException e1) {
			JOptionPane.showMessageDialog(mainWindow, "URL 有错误" + '\n' + "请输入正确的URL地址", "警示",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		final Inet4Address myaddr = addr;
		JDialog jdi = new JDialog(mainWindow);
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
		jdi.setTitle("测试");
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
					jba.setText("未知的测试失败");
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
		MainWindow.pro.setProperty("server-address", textServer.getText());
		MainWindow.pro.setProperty("font-face", cbFont.getSelectedItem().toString());
		MainWindow.pro.setProperty("text-size", cbSize.getSelectedItem().toString());
		MainWindow.pro.setProperty("heartbeat", heartBeat.getText());
		ProgramUtils.exit(111, pro);
		JOptionPane.showMessageDialog(mainWindow, "保存成功");
	}

	private void TCAMouseClicked(MouseEvent e) {
		JOptionPane.showMessageDialog(mainWindow, "(c) 济南大学图灵电脑协会", "版权信息", JOptionPane.PLAIN_MESSAGE);
	}

	private void runActionPerformed(ActionEvent e) {
		if (Integer.parseInt(tfQueueLength.getText()) <= 0 || Integer.parseInt(tfStep.getText()) <= 0
				|| Integer.parseInt((String) cbSize.getSelectedItem()) <= 0
				|| Integer.parseInt(tfRefreshTime.getText()) <= 0 || Integer.parseInt(tfStepTime.getText()) <= 0
				|| Integer.parseInt(tfPushTime.getText()) <= 0) {
			JOptionPane.showMessageDialog(mainWindow, "设置故障！");
			return;
		}
		if (Integer.parseInt(tfPushTime.getText()) < 200) {
			if (JOptionPane.showConfirmDialog(mainWindow, "你的值设置的有点极端" + '\n' + "您确定要继续？", "警告",
					JOptionPane.OK_CANCEL_OPTION) != 0) {
				return;
			}
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

	/**
	 * 软件 -- 关于界面
	 * 
	 * @param e
	 */
	private void aboutActionPerformed(ActionEvent e) {
		JOptionPane
				.showMessageDialog(_window,
						"弹幕姬 0.4 Alpha" + "\n\n" + "(c) 2016 T.C.A" + '\n' + "济南大学图灵电脑协会" + '\n' + "turing.com" + "\n\n" + "Client :" + '\n'
								+ "(c) 2009-2016 Hafrans Stu." + '\n' + "hafrans.com",
						"关于", JOptionPane.DEFAULT_OPTION);
	}

	private void updateActionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(_window, "当前为最新版本！");
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		mainWindow = new JFrame();
		menuBar1 = new JMenuBar();
		menu1 = new JMenu();
		menu2 = new JMenu();
		menuItemHelp = new JMenuItem();
		menuItemUpdate = new JMenuItem();
		menuItemAbout = new JMenuItem();
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
					menu1.setText("\u9ad8\u7ea7\u8bbe\u7f6e");
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
				cbFont.setModel(new DefaultComboBoxModel<>(new String[] {
					"2",
					"2",
					"6",
					"4",
					"4"
				}));
				cbFont.addItemListener(e -> cbFontItemStateChanged(e));
				fontSetting.add(cbFont);
				cbFont.setBounds(70, 30, 185, 29);

				//---- label3 ----
				label3.setText("\u5b57\u53f7");
				fontSetting.add(label3);
				label3.setBounds(new Rectangle(new Point(260, 35), label3.getPreferredSize()));

				//---- cbSize ----
				cbSize.setModel(new DefaultComboBoxModel<>(new String[] {
					"20",
					"24",
					"28",
					"32",
					"36",
					"40",
					"46",
					"48",
					"52",
					"56",
					"64",
					"72",
					"88"
				}));
				cbSize.setSelectedIndex(4);
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
				panel2.setBorder(new TitledBorder(null, "\u538b\u529b\u6d4b\u8bd5", TitledBorder.RIGHT, TitledBorder.DEFAULT_POSITION, null, Color.red));
				panel2.setLayout(null);

				//---- label7 ----
				label7.setText("\u6b65\u8fdb");
				panel2.add(label7);
				label7.setBounds(45, 25, 35, label7.getPreferredSize().height);

				//---- tfStep ----
				tfStep.setText("2");
				panel2.add(tfStep);
				tfStep.setBounds(95, 20, 70, tfStep.getPreferredSize().height);

				//---- label8 ----
				label8.setText("\u961f\u5217\u957f\u5ea6");
				panel2.add(label8);
				label8.setBounds(new Rectangle(new Point(200, 25), label8.getPreferredSize()));

				//---- tfQueueLength ----
				tfQueueLength.setText("5");
				panel2.add(tfQueueLength);
				tfQueueLength.setBounds(270, 20, 90, tfQueueLength.getPreferredSize().height);

				//---- label9 ----
				label9.setText("\u6b65\u8fdb\u95f4\u9694");
				panel2.add(label9);
				label9.setBounds(new Rectangle(new Point(20, 60), label9.getPreferredSize()));

				//---- tfStepTime ----
				tfStepTime.setText("20");
				panel2.add(tfStepTime);
				tfStepTime.setBounds(95, 55, 75, tfStepTime.getPreferredSize().height);

				//---- label10 ----
				label10.setText("\u5237\u65b0\u95f4\u9694");
				panel2.add(label10);
				label10.setBounds(new Rectangle(new Point(200, 60), label10.getPreferredSize()));

				//---- tfRefreshTime ----
				tfRefreshTime.setText("200");
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
			panel2.setBounds(70, 325, 430, 125);

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
	private JMenu menu2;
	private JMenuItem menuItemHelp;
	private JMenuItem menuItemUpdate;
	private JMenuItem menuItemAbout;
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
	public static Properties pro = null;

}
