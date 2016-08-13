package beans;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class SettingValuesBeans {
	private String serverAddress = "http://api.hafrans.com/api.php?action=barrage";
	private String heartbeat = "3";
	private String fontFace = "ºÚÌå";
	private String fontSize = "48";
	private String color    = "RED";
	private String step = "6";
	private String stepTime = "36";
	private String queueLength = "6";

	/**
	 * @return the serverAddress
	 */
	public String getServerAddress() {
		return serverAddress;
	}
	/**
	 * @param serverAddress the serverAddress to set
	 */
	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}
	/**
	 * @return the heartbeat
	 */
	public String getHeartbeat() {
		return heartbeat;
	}
	/**
	 * @param heartbeat the heartbeat to set
	 */
	public void setHeartbeat(String heartbeat) {
		this.heartbeat = heartbeat;
	}
	/**
	 * @return the fontFace
	 */
	public String getFontFace() {
		return fontFace;
	}
	/**
	 * @param fontFace the fontFace to set
	 */
	public void setFontFace(String fontFace) {
		this.fontFace = fontFace;
	}
	/**
	 * @return the fontSize
	 */
	public String getFontSize() {
		return fontSize;
	}
	/**
	 * @param fontSize the fontSize to set
	 */
	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}
	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}
	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}
	/**
	 * @return the step
	 */
	public String getStep() {
		return step;
	}
	/**
	 * @param step the step to set
	 */
	public void setStep(String step) {
		this.step = step;
	}
	/**
	 * @return the stepTime
	 */
	public String getStepTime() {
		return stepTime;
	}
	/**
	 * @param stepTime the stepTime to set
	 */
	public void setStepTime(String stepTime) {
		this.stepTime = stepTime;
	}
	/**
	 * @return the queueLength
	 */
	public String getQueueLength() {
		return queueLength;
	}
	/**
	 * @param queueLength the queueLength to set
	 */
	public void setQueueLength(String queueLength) {
		this.queueLength = queueLength;
	}
	public SettingValuesBeans(Properties pro) {
//		pro.setProperty("server-address", "http://api.hafrans.com/api.php?action=barrage");
//		pro.setProperty("heartbeat", "3");
//		pro.setProperty("font-face", "ºÚÌå");
//		pro.setProperty("text-size", "48");
//		pro.setProperty("step", "6");
//		pro.setProperty("step-time", "36");
//		pro.setProperty("queue-length", "5");
//		pro.setProperty("random-speed", "0");
//		pro.setProperty("updated", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
//		pro.storeToXML(fos, "created on "+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
//		fos.close();
		serverAddress = pro.getProperty("server-address","http://api.hafrans.com/?api=null");
		heartbeat = pro.getProperty("heart", "3");
		fontFace = pro.getProperty("font-face", "ºÚÌå");
		fontSize = pro.getProperty("font-suze", "48");
	}
	public SettingValuesBeans(){
		
	}

}
