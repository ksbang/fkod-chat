package global;

import java.sql.Connection;
import java.sql.DriverManager;
/**
 * @file_name : Database.java 
 * @author    : chanhok61@daum.net
 * @date      : 2015. 10. 13.
 * @story     : 
 */
public class DatabaseImpl implements Database {
	
	private String driver, url, userid, password;
	private Connection connection;
	
	public DatabaseImpl(String driver, String url, String userid, String password) {
		this.driver = driver;
		this.url = url;
		this.userid = userid;
		this.password = password;
	}
	
	public Connection getConnection() {
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, userid, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return connection;
	}
}