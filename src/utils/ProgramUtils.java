package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class ProgramUtils {

	private ProgramUtils() {
	}

	public static void exit(int code, Properties pro) {
		pro.setProperty("updated", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		try {
			Class<?> clazz = Class.forName("main");
			Object obj = clazz.newInstance();
			Field refFilename = clazz.getDeclaredField("filename");
			refFilename.setAccessible(true);
			File filename = (File) refFilename.get(obj);
			FileOutputStream fos = new FileOutputStream(filename);
			pro.storeToXML(fos, "updated on " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (code <= 100) {
			System.exit(code);
		} else {
			System.err.println("保存数据操作");
		}

	}

}
