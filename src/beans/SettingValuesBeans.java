package beans;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class SettingValuesBeans {
	private Properties pro = null;
	private String serverAddress = "http://api.hafrans.com/dannmaku/getmsg.php";
	private String heartbeat = "3";
	private String fontFace = "黑体";
	private String fontSize = "48";
	private String color    = "#FF0000";
	private String step = "6";
	private String stepTime = "36";
	private String queueLength = "6";
	
	public SettingValuesBeans(Properties pro) {
		loadProperties(pro);
		this.pro = (Properties) pro.clone();
	}
	public SettingValuesBeans(){
		storeProperties();
	}
	public void storeToXML(OutputStream fos) throws IOException{
		storeProperties();
		pro.storeToXML(fos, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
	}
	public void loadFromXML(InputStream fis) throws IOException{
		pro.loadFromXML(fis);
		loadProperties(pro);
	}
	private void loadProperties(Properties pro) {
		serverAddress = pro.getProperty("server-address",serverAddress);
		heartbeat = pro.getProperty("heart", heartbeat);
		fontFace = pro.getProperty("font-face", fontFace);
		fontSize = pro.getProperty("font-size", fontSize);
		color = pro.getProperty("color",color);
		step = pro.getProperty("step", step);
		stepTime = pro.getProperty("step-time", stepTime);
		queueLength = pro.getProperty("queue-length", queueLength);
	}
	private void storeProperties(){
		if(this.pro == null){
			pro = new Properties();
		}
		pro.setProperty("server-address",serverAddress);
		pro.setProperty("heart", heartbeat);
		pro.setProperty("font-face", fontFace);
		pro.setProperty("font-size", fontSize);
		pro.setProperty("color",color);
		pro.setProperty("step", step);
		pro.setProperty("step-time", stepTime);
		pro.setProperty("queue-length", queueLength);
	}

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
	public void setFontFace(Object fontFace) {
		this.fontFace = (String) fontFace;
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
	public void setFontSize(Object fontSize) {
		this.fontSize = (String) fontSize;
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


}
