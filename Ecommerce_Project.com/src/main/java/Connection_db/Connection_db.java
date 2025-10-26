package Connection_db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection_db {
	private static String  url;
	private static String name;
	private static String password;
	private static Connection con;
	public Connection_db (String url,String name, String password){
		this.url=url;
		this.name =name ;
		this.password =password;
	}
	 public static Connection getConnection() throws SQLException {
	        if (con == null || con.isClosed()) {
	            con = DriverManager.getConnection(url, name, password);
	        }
	        System.out.println("Connection Created");
	        return con;
	    }
	 public void closeConnection() throws SQLException {
	        if (con != null && !con.isClosed()) {
	            con.close();
	        }
	    }
}
