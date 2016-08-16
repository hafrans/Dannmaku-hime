package utils;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;

import beans.SettingValuesBeans;

public class ProgramUtils {

	private ProgramUtils() {
	}

	public static void exit(int code,SettingValuesBeans bean) {
		try {
			Class<?> clazz = Class.forName("main");
			Object obj = clazz.newInstance();
			Field refFilename = clazz.getDeclaredField("filename");
			refFilename.setAccessible(true);
			File filename = (File) refFilename.get(obj);
			FileOutputStream fos = new FileOutputStream(filename);
			bean.storeToXML(fos);
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (code <= 100) {
			System.exit(code);
		} else {
			System.err.println("保存");
		}

	}
	/**
	 * 让JTextField只输入数字
	 * a JTextField Component input number only. 
	 * @author Hafrans
	 *
	 */
	public static class NumberInputValidator extends KeyAdapter{

		@Override
		public void keyTyped(KeyEvent e) {
			if(e.getKeyChar() >= '0' && e.getKeyChar() <= '9'){
				super.keyReleased(e);
			}else{
				e.consume();
			}
			
		}
		
	}

}
