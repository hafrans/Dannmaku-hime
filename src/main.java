import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import beans.SettingValuesBeans;
import gui.*;
import net.HSMTP;
import utils.ProgramUtils;


/**	
 * @author Hafrans
 * @since 1.0
 */
public class main {
	public static SettingValuesBeans bean = new SettingValuesBeans(); 
	private static File filename = new File("./config.xml");
	
	/**
	 * 
	 */
	static{
		try{
			if(!filename.exists()){
				FileOutputStream fos = new FileOutputStream(filename);
				bean.storeToXML(fos);
				fos.close();
			}else{
				FileInputStream fin = new FileInputStream(filename);
				bean.loadFromXML(fin);
				fin.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@SuppressWarnings("all")
	/**	
	 * 程序入口
	 * @author Hafrans 
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws UnsupportedLookAndFeelException
	 */
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
		new MainWindow();
//		LookAndFeelInfo[] infos=UIManager.getInstalledLookAndFeels();
//		 System.out.println("Skins:");
//		 for(LookAndFeelInfo info : infos){
//		 System.out.println(info.getName()+" "+info.getClassName());
//		 }
		HSMTP hsmtp = new HSMTP("http://api.hafrans.com/dannmaku/getmsg.php", "DAMA", false);
		try {
			System.err.println(hsmtp.openConnection());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
