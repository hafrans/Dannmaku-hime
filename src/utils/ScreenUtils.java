package utils;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;



public class ScreenUtils {

	public static Point getCenterPosition(int width, int height) {
		return new Point((Toolkit.getDefaultToolkit().getScreenSize().width - width) / 2,
				(Toolkit.getDefaultToolkit().getScreenSize().height - height) / 2);
	}
	public static Dimension getScreenSize(){
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

}
