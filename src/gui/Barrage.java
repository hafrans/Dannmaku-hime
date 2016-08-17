package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.TrayIcon.MessageType;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import org.json.JSONException;

import beans.SettingValuesBeans;
import net.HSMTP;
import net.HSMTPException;
import utils.ScreenUtils;

@SuppressWarnings("serial")
public class Barrage extends JWindow {
	private MainWindow window = null;
	private Dimension screenSize = ScreenUtils.getScreenSize();
	private double screenPercent = 0.5; // 屏占比
	private final ConcurrentLinkedQueue<Integer> clq = new ConcurrentLinkedQueue<Integer>();
	private int queueMaxSize = 3;
	private int step = 3;
	private int textSize = 48;
	private int refreshTime = 200;
	private int stepTime = 20;
	private int pushTime = 1000;
	private String fontface = "黑体";
	private Color color = new Color(255,0, 0);
	private String server = null;
	private static Barrage obj = null;
	private boolean isDebug = false;
	private HSMTP hsmtp = null;
	private int heartBeat = 3;

	public Barrage(int queueMaxSize, int step) {
		if (step * queueMaxSize == 0)
			throw new IllegalArgumentException("参数有误！");
		this.queueMaxSize = queueMaxSize;
		this.step = step;
		mBarrage();

	}

	/**
	 * Just For Debug
	 * 
	 * @param queueMaxSize
	 *            5
	 * @param step
	 *            8 6
	 * @param textSize
	 *            48 52
	 * @param refreshTime
	 * @param stepTime
	 *            32 36
	 * @param pushTime
	 */
	public Barrage(int queueMaxSize, 
				   int step, 
				   int textSize, 
				   int refreshTime, 
				   int stepTime, 
				   int pushTime,
				   String fontface) {
		if (step * queueMaxSize == 0)
			throw new IllegalArgumentException("参数有误！");
		this.queueMaxSize = queueMaxSize;
		this.step = step;
		this.textSize = textSize;
		this.refreshTime = refreshTime;
		this.stepTime = stepTime;
		this.pushTime = pushTime;
		this.fontface = fontface;
		this.isDebug = true;
		mBarrage();
		

	}
	public Barrage(SettingValuesBeans bean,MainWindow window){
		if(bean == null){
			throw new NullPointerException(" no input beans");
		}
		this.window = window;
		try{
			this.queueMaxSize = Integer.parseInt(bean.getQueueLength());
			this.step = Integer.parseInt(bean.getStep());
			this.stepTime = Integer.parseInt(bean.getStepTime());
			this.fontface = bean.getFontFace();
			this.textSize = Integer.parseInt(bean.getFontSize());
			this.color = Color.decode(bean.getColor());
			this.server = bean.getServerAddress();
			this.heartBeat = Integer.parseInt(bean.getHeartbeat());
			hsmtp = new HSMTP(server, "DAMA", false);
			
		}catch (NumberFormatException e) {
			throw new NumberFormatException("set is invalid");
		}
		mBarrage();
	}
		

	public void mBarrage() {
		// this.setUndecorated(true);
		this.setBackground(new Color(0, 0, 0, 0));
		this.setLayout(null);
		this.setSize(screenSize.width, (int) (screenSize.height * screenPercent));
		this.setFocusable(false);
		// this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setLocation(0, 25);

		// 后台刷新线程
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (Barrage.this.isVisible()) {
						TimeUnit.SECONDS.sleep(10);
						Barrage.this.repaint(1, 0, 0, Barrage.this.getWidth(), Barrage.this.getHeight());
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

		// 队列处理线程
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (Barrage.this.isVisible()) {
						if (clq.size() > queueMaxSize * 0.8) {
							clq.poll();
							clq.poll();
							/**
							 * 弹幕炸锅了
							 */
							if (clq.size() > queueMaxSize * 1.5) {
								clq.clear();
								System.out.println("clear");
							}
						} else {

							TimeUnit.MILLISECONDS.sleep(1000);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		this.setVisible(true);
		write();

	}

	private void write() {

		if(isDebug){
			new Timer().schedule(new TimerTask() {

				@Override
				public void run() {
					if (Barrage.this.isVisible()) {
						writeBarrage("**您处于DEBUG模式**这是测试弹幕**[弾幕姫だんまくひめ]", fontface, String.valueOf(textSize),color);
						System.out.println("测试");
					}
				}
			}, 500, pushTime);
		}else{
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					if(!Barrage.this.isVisible())
						timer.cancel();
					try {
						List<String> list = hsmtp.openConnection();
						
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								for(String obj : list){
									if (Barrage.this.isVisible()) {
										writeBarrage(obj, fontface, String.valueOf(textSize),color);
										System.out.println(obj);
									}
									try {
										Thread.sleep(150);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							}
						}).start();
						
					} catch (JSONException e) {
						if(Tray.icon != null)
							Tray.icon.displayMessage("弹幕姬的提示", "您的弹幕姬在解析上出现了问题！", MessageType.ERROR);
						timer.cancel();
						e.printStackTrace();
					} catch (IOException e) {
						if(Tray.icon != null)
							Tray.icon.displayMessage("弹幕姬的提示", "您的弹幕姬出现了故障！", MessageType.ERROR);
						timer.cancel();
						e.printStackTrace();
					} catch (HSMTPException e) {
						if(Tray.icon != null)
							Tray.icon.displayMessage("弹幕姬的提示", "服务器故障："+e.getMessage(), MessageType.ERROR);
						timer.cancel();
						e.printStackTrace();
					}
				} 
			}, 500, heartBeat*1000);
		}

	}

	private void writeBarrage(final String text, final String fontFace, final String size,final Color color) {
		if (!Barrage.this.isVisible())
			return;
		new Thread(new Runnable() {
			@Override
			public void run() {
				JLabel label = new JLabel(text);
				label.setForeground(color);
				label.setFont(new Font(fontFace, Font.PLAIN, Integer.parseInt(size)));
				label.setSize(Integer.parseInt(size) * text.length() + 1, (int) (Integer.parseInt(size) * 1.2));
				label.setLocation(screenSize.width + 1, getRandomScreenYPosition(size));
				Barrage.this.add(label);

				new Thread(new Runnable() {

					@Override
					public void run() {
						boolean set = false;
						while (label.getLocation().getX() > -label.getWidth() && Barrage.this.isVisible()) {
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {

									label.setLocation(label.getLocation().x - step, label.getLocation().y);

								}
							});
							try {
								Thread.sleep(stepTime);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							if (!set && label.getLocation().x < screenSize.getWidth()
									- (Integer.parseInt(size) * text.length() + 10)) {
								clq.remove(label.getLocation().y);
								set = true;
							}
						}
						// clq.remove(label.getLocation().y);
					}
				}).start();

			}
		}).start();
	}

	private int getRandomScreenYPosition(String size) {
		int numSize = (int) (Integer.parseInt(size) * 1.2);
		int numLine = (int) (screenSize.height * screenPercent) / numSize;
		int random = 0;

		do {
			random = new Random(System.currentTimeMillis()).nextInt(numLine) * numSize + 8;
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} while (clq.contains(random) && Barrage.this.isVisible());
		clq.add(random);

		return random;

	}

}
