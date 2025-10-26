package PG_Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Insertion {
	public void insert(String mail_id,String username,String password,Long aadhar_number,Connection con) throws SQLException {
		String stm = "Insert into admin_page(mail_id,username,password,aadhar_number)values(?,?,?,?)";
		PreparedStatement ps = con.prepareStatement(stm);
		ps.setString(1, mail_id);
		ps.setString(2, username);
		ps.setString(3 ,password);
		ps.setLong(4,aadhar_number);
		int rows = ps.executeUpdate();
		if(rows>0) {
		    System.out.println("New User Created");
		    stm="delete from admin_isolated where mail_id=?";
			PreparedStatement ps1 = con.prepareStatement(stm);
			ps1.setString(1, mail_id);
			ps1.executeUpdate();
			ps1.close();
		}
	    ps.close();
	}
}

