package PG_Admin;
import Connection_db.Connection_db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main_pg {
	public static void main(String[] a) throws SQLException {
		String url="jdbc:postgresql://localhost:5432/Ecommerce";
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Pg user name");
		String user_name=sc.nextLine();
		System.out.println("Enter pg user Passward");
		String password=sc.nextLine();
		
		Connection_db c1=new Connection_db(url,user_name,password);
		Connection con=c1.getConnection();
        
        con.setAutoCommit(false); 

		System.out.println("Do you want to approve a user(true/false)");
		boolean b1=sc.nextBoolean();
        sc.nextLine();

		if(b1) {
			System.out.println("---------------Users Pending For Approval---------------");
			String str="Select*from admin_isolated";
			
            try (PreparedStatement ps=con.prepareStatement(str);
                 ResultSet rs=ps.executeQuery()) {
                
                while(rs.next()) {
                    String mail_id=rs.getString("mail_id");
                    String username=rs.getString("username");
                    String password1 =rs.getString("password");
                    Long aadhar_number=rs.getLong("aadhar_number");
                    
                    System.out.println("Mail_id: "+mail_id);
                    System.out.println("User Name: "+username);
                    System.out.println("Aadhar Number: "+aadhar_number);
                    System.out.println("Do you want to approve this user");
                    
                    b1=sc.nextBoolean();
                    sc.nextLine();
                    
                    if(b1) {
                        Insertion in1=new Insertion();
                        in1.insert(mail_id,username,password1,aadhar_number,con);
                    }
                }
            } catch (SQLException e) {
                throw e; 
            }
		}
        
        if (con != null && !con.isClosed()) {
            con.close();
        }
        sc.close();
	}
}
