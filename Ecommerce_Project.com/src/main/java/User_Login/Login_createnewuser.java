package User_Login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login_createnewuser {
	Connection con;
	public Login_createnewuser(Connection con){
		this.con=con;
	}
	public boolean searchuser(String username,String table_name) throws SQLException {
		String stm = "SELECT * FROM "+table_name +" WHERE username = ? or mail_id = ?";
		PreparedStatement ps = con.prepareStatement(stm);
		ps.setString(1,username);
		ps.setString(2, username);
		ResultSet rs = ps.executeQuery();
        boolean exists = rs.next(); 	
        rs.close();
        ps.close();
        return exists;
	}
	public void create_newuser(String username,String mail_is,int age,String dateof_birth,String password,long phone_no) throws SQLException{
		String stm = "Insert into acess(username,mail_id,age,dateof_birth,password,phone_no)values(?,?,?,?,?,?)";
		PreparedStatement ps = con.prepareStatement(stm);
		ps.setString(1, username);
		ps.setString(2, mail_is);
		ps.setInt(3,age);
		ps.setDate(4, java.sql.Date.valueOf(dateof_birth));
		ps.setString(5 ,password);
		ps.setLong(6 ,phone_no);
		int rows = ps.executeUpdate();
	    ps.close();
	    System.out.println("New User Created");
	}
}

