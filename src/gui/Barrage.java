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
import javax.swing.SwingUtilities;
import utils.ProgramUtils;
import utils.ScreenUtils;

public class Barrage extends JFrame {
	private Dimension screenSize = ScreenUtils.getScreenSize();
	private double screenPercent = 0.75;
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
	 * @param queueMaxSize
	 * @param step
	 * @param textSize
	 * @param refreshTime
	 * @param stepTime
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

	}

	public Barrage() {
		mBarrage();
	}

	public void mBarrage() {
		this.setUndecorated(true);
		this.setBackground(new Color(0, 0, 0, 0));
		this.setLayout(null);
		this.setSize(screenSize.width, (int) (screenSize.height * screenPercent));
		this.setFocusable(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setLocation(0, 25);
//		this.addKeyListener(new KeyAdapter() {
//
//			@Override
//			public void keyReleased(KeyEvent e) {
//				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
//					Barrage.this.dispose();
//					if (e.isShiftDown()) {
//						System.err.println("强制退出");
//						System.exit(-2);
//					}
//				}
//				super.keyReleased(e);
//			}
//
//		});

		this.setVisible(true);
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
			
			@Override
			public void eventDispatched(AWTEvent event) {
					KeyEvent keyevent = (KeyEvent) event;
					if(keyevent.getID()  == KeyEvent.KEY_RELEASED){
						System.out.println("松开"+System.currentTimeMillis());
					}
				
			}
		}, AWTEvent.KEY_EVENT_MASK);

		// 后台刷新线程
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (Barrage.this.isVisible()) {
						TimeUnit.MILLISECONDS.sleep(refreshTime);
						Barrage.this.repaint(1, 0, 0, Barrage.this.getWidth(), Barrage.this.getHeight());
						// Barrage.this.setVisible(true);
						// System.out.println("刷新");
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
						if (clq.size() > queueMaxSize) {
							clq.poll();
						} else {
							TimeUnit.SECONDS.sleep(1);
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
					//System.out.println("测试");
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
						}
						clq.remove(label.getLocation().y);
					}
				}).start();

			}
		}).start();
	}

	private int getRandomScreenYPosition(String size) {
		int numSize = (int) (Integer.parseInt(size)*1.2);
		int numLine = (int) (screenSize.height * screenPercent * 0.9) / numSize;
		int random = 0;
		do {
			random = new Random(System.currentTimeMillis()).nextInt(numLine) * numSize + 8;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} while (clq.contains(random) && Barrage.this.isVisible());
		clq.add(random);

		return random;

	}

}
