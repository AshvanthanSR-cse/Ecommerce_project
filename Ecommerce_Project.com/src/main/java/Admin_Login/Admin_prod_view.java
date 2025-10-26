package Admin_Login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Admin_prod_view {
	Connection con;
	String mail_id;
	int u_id;
	Admin_prod_view(Connection con,String mail_id){
		this.con=con;
		this.mail_id=mail_id;
		c_user c1=new c_user(con);
		try {
			this.u_id=c1.user_id_retrival(mail_id);
		}
		catch(SQLException e){
			System.out.println("Error Occured okay");
		}
	}
	public void retrive_inserted_product() throws SQLException {
		String str="Select *from product_cat where a_user_key=?";
		PreparedStatement ps= con.prepareStatement(str);
		ps.setInt(1,u_id);
		ResultSet rs=ps.executeQuery();
		System.out.println("\n--- Your Inserted Products ---");
		while (rs.next()) {
	        int productId = rs.getInt("product_id");
	        String pname = rs.getString("product_name");
	        String pdesc = rs.getString("product_desc");
	        double price = rs.getDouble("product_price");
	        int stock = rs.getInt("product_count");

	        System.out.println("Product ID: " + productId);
	        System.out.println("Name: " + pname);
	        System.out.println("Description: " + pdesc);
	        System.out.println("Price: " + price);
	        System.out.println("Stock: " + stock);
	        System.out.println("-----------------------------");
	    }
	    rs.close();
	    ps.close();
	}
}/*
create table product_cat(
product_name varchar(50),
product_desc text,
product_id int primary key,
product_price int,
product_discounts int,
product_count int,
product_img varchar(500),
a_user_key int 


);*/