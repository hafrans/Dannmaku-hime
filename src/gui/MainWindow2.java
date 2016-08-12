package gui;

import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import utils.ScreenUtils;

public class MainWindow2 extends JFrame {

	private int width = 650;
	private int height = 500;
	private Point point = ScreenUtils.getCenterPosition(width, height);
	private GridBagLayout gb = null;
	private JTextField jtf_url = null;
	private JComboBox<String> jcb_font;
	private JComboBox<Integer> jcb_size;

	public MainWindow2() {
		this.setBounds(point.x, point.y, width, height);
		this.setTitle("TCA弹幕系统");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setFocusable(true);
		gb = new GridBagLayout();
		this.setLayout(gb);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 0;
		gbc.gridheight = 1;
		gbc.ipady = 15;
		gbc.ipadx = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;
		TitledBorder tba = BorderFactory.createTitledBorder("弹幕服务器地址");
		jtf_url = new JTextField("http://hafrans.com", 256);
		jtf_url.setBorder(tba);
		this.add(jtf_url);
		gb.setConstraints(jtf_url, gbc);
		gbc.ipady = 15;

		// gbc.gridheight = 0;
		JPanel jp_font = new JPanel();
		jp_font.setBorder(BorderFactory.createTitledBorder("字体设置"));
		this.add(jp_font);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gb.setConstraints(jp_font, gbc);

		JPanel jp_barrage = new JPanel();
		jp_barrage.setBorder(BorderFactory.createTitledBorder("弹幕设置"));
		this.add(jp_barrage);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gb.setConstraints(jp_barrage, gbc);

		//字体设置的Panel
		jp_font.setLayout(new FlowLayout());
		JLabel jbInputFont = new JLabel("字体:");
		JLabel jbInputSize = new JLabel("大小");
		jcb_font = new JComboBox<String>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()); 
		jcb_font.setSelectedItem("黑体");
		jp_font.add(jbInputFont);
		jp_font.add(jcb_font);
		
		jcb_size = new JComboBox<Integer>(new Integer[]{12,24,36,48,60,72});
		jcb_size.setSelectedItem(36);
		jp_font.add(jbInputSize);
		jp_font.add(jcb_size);
		
		JLabel font_show = new JLabel("TCA. 图灵电脑协会");
		jp_font.add(font_show);
		// this.addMouseListener(new MouseAdapter() {
		//
		// @Override
		// public void mouseReleased(MouseEvent e) {
		//
		// if(e.getButton() == MouseEvent.BUTTON3){
		// JPopupMenu pop = new JPopupMenu("Hafrans");
		// pop.add(new JMenuItem("Connect"));
		// pop.addSeparator();
		// pop.add(new JMenuItem("Text"));
		// pop.add(new JMenuItem("Text"));
		// pop.add(new JMenuItem("Text"));
		// pop.add(new JMenuItem("Text"));
		// pop.show(e.getComponent(), e.getX(), e.getY());
		//// MainWindow.this.getContentPane().add(pop);
		//// if(SystemTray.isSupported())
		//// {
		//// SystemTray tray = SystemTray.getSystemTray();
		//// ImageIcon iconp = new ImageIcon("D:\\1234.png");
		//// final TrayIcon icon = new TrayIcon(iconp.getImage(),"Test", pop);
		//// try {
		//// tray.add(icon);
		//// } catch (AWTException e1) {
		//// // TODO Auto-generated catch block
		//// e1.printStackTrace();
		//// }
		//// Timer tm = new Timer();
		//// tm.schedule(new TimerTask() {
		////
		//// @Override
		//// public void run() {
		//// // TODO Auto-generated method stub
		//// icon.displayMessage("服务端","自动上传失败", MessageType.ERROR);
		//// }
		//// }, 1000, 1000);
		//// }
		// return;
		// }
		// super.mouseReleased(e);
		//
		// }
		//
		// });

		this.setVisible(true);

	}

	public void save() {

	}

}
