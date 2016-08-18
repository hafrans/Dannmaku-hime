package net;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.json.*;

import utils.ProgramUtils;

/**
 * Hafrans Simplified Message Transfer Protocol
 * @author Hafrans
 * A solution to fetch String Messages from Remote Server
 * 
 * 
 * 
 * 
 * SERVER -----JSON----> ----HSMTP core---List---> Client Message
 */
public class HSMTP {
	private String serverAddress;
	private String key;
	private String enctyped;
	@SuppressWarnings("unused")
	private boolean ssl = false;

	public HSMTP(String serverAddress,String key,boolean ssl) {
		this.serverAddress = serverAddress;
		this.key = key;
		this.ssl = ssl;
	}
	
	public List<String> openConnection() throws IOException, JSONException, HSMTPException{
		
		URL url = new URL(serverAddress+"/getmsg.php");
		String timeStamp = String.valueOf(System.currentTimeMillis()/1000/10);
		try {
			enctyped = ProgramUtils.md5String(key+timeStamp);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		String menctyped = URLEncoder.encode(enctyped,"UTF-8");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(5000);
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Length", ""+menctyped.getBytes().length);
		conn.setRequestProperty("Content-Type", "x-www-form-urlencoded");
		conn.setRequestProperty("User-Agent", "HSMTP 1.0 / Lari Protocol is Submiter");
		
		//先发送
		
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(conn.getOutputStream()), true);
		pw.write(menctyped);
		pw.close();
		
		//后接受
		if(conn.getResponseCode() != 200){
			throw new HSMTPException("连接失败:"+conn.getResponseCode());
		}
		BufferedReader read = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		String a =  read.readLine();
		System.err.println("GET : "+a);
		if(a.startsWith("{")){
			JSONObject object = new JSONObject(a);
			object.getString("err_msg");
			System.err.println(object.getString("err_msg"));
			throw new HSMTPException(object.getString("err_msg"));
		}
		return HSMTP.parseJSON(a);		
	}
	/**
	 * JSON to List<String>
	 * @param text JSON String
	 * @return
	 */
	public static ArrayList<String> parseJSON(String text) {
		ArrayList<String> obj = new ArrayList<String>();
		JSONArray arr = new JSONArray(text);
		for (Object a : arr) {
			obj.add((String) a);
		}
		return obj;
	}
}
