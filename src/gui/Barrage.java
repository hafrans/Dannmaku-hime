package gui;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import utils.ProgramUtils;
import utils.ScreenUtils;

public class Barrage extends JWindow {
	private Dimension screenSize = ScreenUtils.getScreenSize();
	private double screenPercent = 0.5; //屏占比
	private final ConcurrentLinkedQueue<Integer> clq = new ConcurrentLinkedQueue<Integer>();
	private int queueMaxSize = 3;
	private int step = 3;
	private int textSize = 48;
	private int refreshTime = 200;
	private int stepTime = 20;
	private int pushTime = 1000;
	private String fontface = "黑体";
	private static Barrage obj = null;

	public Barrage(int queueMaxSize, int step) {
		if (step * queueMaxSize == 0)
			throw new IllegalArgumentException("参数有误！");
		this.queueMaxSize = queueMaxSize;
		this.step = step;
		mBarrage();

	}

	/**
	 * Just For Debug
	 * @param queueMaxSize 5
	 * @param step  8 6
	 * @param textSize 48 52
	 * @param refreshTime 
	 * @param stepTime 32 36
	 * @param pushTime
	 */
	public Barrage(int queueMaxSize, int step, int textSize, int refreshTime, int stepTime,int pushTime,String fontface) {
		if (step * queueMaxSize == 0)
			throw new IllegalArgumentException("参数有误！");
		this.queueMaxSize = queueMaxSize;
		this.step = step;
		this.textSize = textSize;
		this.refreshTime = refreshTime;
		this.stepTime = stepTime;
		this.pushTime = pushTime;
		this.fontface = fontface;
		mBarrage();
		this.setVisible(true);

	}

	public Barrage() {
		mBarrage();
	}

	public void mBarrage() {
		//this.setUndecorated(true);
		this.setBackground(new Color(0, 0, 0, 0));
		this.setLayout(null);
		this.setSize(screenSize.width, (int) (screenSize.height * screenPercent));
		this.setFocusable(false);
		//this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
						if (clq.size() > queueMaxSize*0.8) {
							clq.poll();
							clq.poll();
							/**	
							 * 弹幕炸锅了
							 */
							if (clq.size() > queueMaxSize*2) {
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
		write();

	}

	private void write() {
		
		
		
		
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				if (Barrage.this.isVisible()) {
					writeBarrage("测试文本--用于压力测试", fontface, String.valueOf(textSize));
					System.out.println("测试");
				}
			}
		}, 500, pushTime);

	}

	private void writeBarrage(final String text, final String fontFace, final String size) {
		if(!Barrage.this.isVisible())
			return;
		new Thread(new Runnable() {
			@Override
			public void run() {
				JLabel label = new JLabel(text);
				label.setForeground(Color.red);
				label.setFont(new Font(fontFace, Font.PLAIN, Integer.parseInt(size)));
				label.setSize(Integer.parseInt(size) * text.length() + 1, (int) (Integer.parseInt(size)*1.2));
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
							if(!set && label.getLocation().x < screenSize.getWidth() - (Integer.parseInt(size) * text.length() + 10)){
								clq.remove(label.getLocation().y);
								set = true;
							}
						}
						//clq.remove(label.getLocation().y);
					}
				}).start();

			}
		}).start();
	}

	private int getRandomScreenYPosition(String size) {
		int numSize = (int) (Integer.parseInt(size)*1.2);
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
