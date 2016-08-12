package gui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.Popup;

public class Tray {
	public Properties pro = MainWindow.pro;
	private MainWindow window = null;
	private static SystemTray tray = null;
	private TrayIcon icon = null;
	private URL imgDefault = this.getClass().getResource("/img/chat.png");
	private URL imgActive  = this.getClass().getResource("/img/chat-active-alt-2.png");
	private PopupMenu popup;
	
	static {
		if(!SystemTray.isSupported()){
			   System.err.println("²»Ö§³ÖÍÐÅÌ£¡");
			}
			if(tray == null){
				tray = SystemTray.getSystemTray();
			}
	}
	
	public Tray(MainWindow window) {
		if(window == null)
			throw new NullPointerException();
		this.window = window;	
		
		try {
			setTray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		addIcon();
		//window.setVisible(false);
	}
	private void setTray() throws Exception{
		
		popup = new PopupMenu();
		
		MenuItem showMain = new MenuItem("ÏÔÊ¾Ö÷²Ëµ¥");
		MenuItem start	  = new MenuItem("Í£Ö¹µ¯Ä»");
		MenuItem clear     = new MenuItem("Çå³ýµ¯Ä»");
		
		
		showMain.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				window._window.setVisible(true);
			}
		});
		
		
		
		
		
		
		
		
		
		
		
		popup.add(showMain);
		popup.addSeparator();
		popup.add(start);
		popup.add(clear);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		Image image = new ImageIcon(imgActive).getImage();
		icon = new TrayIcon(image, "µ¯Ä»¼§", popup);
	}
	private boolean addIcon(){
		if(tray == null || tray.getTrayIcons().length > 0)
			return false;
		else{
			try {
				tray.add(icon);
			} catch (AWTException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
	}

}
