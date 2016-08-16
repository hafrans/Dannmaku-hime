package gui;

import java.awt.AWTException;
import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.Popup;

import beans.SettingValuesBeans;
import utils.ProgramUtils;

public class Tray {
	private SettingValuesBeans bean = MainWindow.bean;
	private MainWindow window = null;
	private static SystemTray tray = null;
	public static TrayIcon icon = null;
	private URL imgDefault = this.getClass().getResource("/img/appbar.png");
	private URL imgActive = this.getClass().getResource("/img/appbar.png");
	private PopupMenu popup;
	private MenuItem clear;
	private MenuItem start;
	private MenuItem showMain;
	private Image imageDefault;
	private Image imageActive;
	private boolean isIconSet = false;

	public Tray(MainWindow window) {
		if (tray != null)
			return;
		if (!SystemTray.isSupported()) {
			System.err.println("系统不支持托盘！");
		}
		if (tray == null) {
			tray = SystemTray.getSystemTray();
		}
		if (window == null)
			throw new NullPointerException();
		this.window = window;

		try {
			setTray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		addIcon();
		// window.setVisible(false);
	}

	private void setTray() throws Exception {

		imageActive = new ImageIcon(imgActive).getImage();
		imageDefault = new ImageIcon(imgDefault).getImage();
		
		popup = new PopupMenu();

		showMain = new MenuItem("显示主菜单");
		start = new MenuItem("开始弹幕");
		clear = new MenuItem("清空弹幕");
		MenuItem exit = new MenuItem("退出");
		exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ProgramUtils.exit(0, bean);
			}
		});

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
		popup.addSeparator();
		popup.add(exit);
		popup.setFont(new Font("黑体",Font.PLAIN,18));
		
		
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Tray.this.stopOrStart();
			}
		});
		clear.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Tray.this.clear();
			}
		});
		//�۲��߳�
		Thread mThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				String status= null;
				while(true){
					status = start.getLabel();
					if (window.isLaunched) {
						setStart();
					} else if(window.isLaunched == false && "停止弹幕".equals(status)) {
						setStop();
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		});
		mThread.setDaemon(true);
		mThread.start();
		

		icon = new TrayIcon(imageActive, "弹幕姬在这儿~", popup);
		
		
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				icon.displayMessage("来自弹幕姬的问候", "您的弹幕姬现在就在托盘里呢~~", MessageType.INFO);
			}
		}, 500);
		if(window.isLaunched){
			setRunIcon();
		}
	}

	private boolean addIcon() {
		if (tray == null || tray.getTrayIcons().length > 0)
			return false;
		else {
			try {
				tray.add(icon);
			} catch (AWTException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

	}

	private void stopOrStart() {
		window.runActionPerformed(null);
		if (window.isLaunched) {
			setStart();
		} else {
			setStop();
		}
	}
	private void clear(){
		if (window.isLaunched) {
			window.runActionPerformed(null);
			window.runActionPerformed(null);
		} 
		
	}

	private void setRunIcon() {
		if(!window.isLaunched || isIconSet)
			return ;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				isIconSet = true;
				while(window.isLaunched){
					try {
						Thread.sleep(500);
						icon.setImage(new ImageIcon("/ll.png").getImage());
						Thread.sleep(500);
						icon.setImage(imageActive);
						
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}).start();
	}

	private void setStopIcon() {
		if(window.isLaunched)
			return;
		isIconSet = false;
		icon.setImage(imageDefault);
	}
	private synchronized void setStart(){
		if (window.isLaunched) {
			start.setLabel("停止弹幕");
			setRunIcon();
		}
	}
	private synchronized void setStop(){
		if (!window.isLaunched) {
			start.setLabel("开始弹幕");
			setStopIcon();
		}
		
	}

}
