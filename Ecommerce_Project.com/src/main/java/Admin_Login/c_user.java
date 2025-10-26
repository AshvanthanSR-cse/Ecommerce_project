package Admin_Login;
import Admin_Page.Main_Page_Admin_Page;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class c_user {
	Connection con;
	public c_user(Connection con){
		this.con=con;
	}
	public void create_newuser(String username,String mail_is,String password,long a_no) throws SQLException{
		String stm = "Insert into admin_isolated(mail_id,username,password,aadhar_number)values(?,?,?,?)";
		PreparedStatement ps = con.prepareStatement(stm);
		ps.setString(1, mail_is);
		ps.setString(2, username);
		ps.setString(3 ,password);
		ps.setLong(4,a_no);
		int rows = ps.executeUpdate();
		if(rows>0) {
		    System.out.println("New User Created");
		}
	    ps.close();
	}
	 public int user_id_retrival(String mail_or_username_input) throws SQLException {
	        String stm="SELECT a_user_key FROM admin_page WHERE mail_id = ? OR username = ?";

	        try (PreparedStatement ps = con.prepareStatement(stm)) {
	            ps.setString(1, mail_or_username_input);
	            ps.setString(2, mail_or_username_input);

	            try (ResultSet rs = ps.executeQuery()) {
	                int n = -1;
	                if(rs.next()) {
	                    n = rs.getInt("a_user_key");
	                }
	                return n;
	            }
	        }
	 }
}
