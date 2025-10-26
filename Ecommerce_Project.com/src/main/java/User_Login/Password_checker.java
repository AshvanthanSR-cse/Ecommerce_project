package User_Login;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Password_checker {
	String password;
	Connection con;
	public Password_checker(String password,Connection con){
		this.password=password;
		this.con=con;
	}
	public boolean verifier(String mail_is,String tableName) throws SQLException {
		String sql = "SELECT password FROM "+tableName+ " WHERE mail_id =? or username=?";  
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, mail_is);
		ps.setString(2, mail_is);
		ResultSet rs = ps.executeQuery();

		if(rs.next()) {
		    String dbPassword = rs.getString("password");
		    if(dbPassword.equals(password)) {
		        System.out.println("Passwords match!");
		        rs.close();
		        ps.close();
		        return true;
		    } else {
		        System.out.println("Passwords do not match!");
		        rs.close();
		        ps.close();
		        return false;
		    }
		} else {
		    System.out.println("Email not found!");
		    rs.close();
		    ps.close();
		    return false;
		}

	}
}

