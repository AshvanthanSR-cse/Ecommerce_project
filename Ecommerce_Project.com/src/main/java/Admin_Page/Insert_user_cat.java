package Admin_Page;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class Insert_user_cat {
	Connection con;
	String mail_id;
	String username;
	int u_id;
	public Insert_user_cat(Connection con,String mail_id,String username,int User_id){
		this.con=con;
		this.mail_id=mail_id;
		this.username=username;
		this.u_id=User_id;
	}
	public void insert_product(int id, String p_name, String p_desc, String p_img,
            int p_price, int p_dis, int p_amount_stock) throws SQLException {
		p_price = (int) (p_price - (p_price * p_dis / 100.0));
		String str = "INSERT INTO product_cat (product_name, product_desc, product_id, product_price, product_discounts, product_count, product_img,a_user_key) "
		           + "VALUES (?, ?, ?, ?, ?, ?, ?,?)";

		PreparedStatement pst = con.prepareStatement(str);
		pst.setString(1, p_name);
		pst.setString(2, p_desc);
		pst.setInt(3, id);
		pst.setInt(4, p_price);
		pst.setInt(5, p_dis);
		pst.setInt(6, p_amount_stock);
		pst.setString(7, p_img);
		pst.setInt(8, u_id);
		int rows = pst.executeUpdate();
		if(rows>0) {
			System.out.print("Inserted into Table");
		}
		
	}
}
/*create table product_cat(
product_name varchar(50),
product_desc text,
product_id int primary key,
product_price int,
product_discounts int,
product_count int,
product_img varchar(500),
a_user_key int 
);*/

