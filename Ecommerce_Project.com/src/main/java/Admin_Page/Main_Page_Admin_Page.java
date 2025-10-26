package Admin_Page;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import User_Login.Password_checker;
import Connection_db.Connection_db;
import Admin_Login.c_user;
public class Main_Page_Admin_Page {
	 
	public static void main(String args[]) throws SQLException {
		Connection con;
		String url="jdbc:postgresql://localhost:5432/Ecommerce";
		String name="postgres";
		String password="Achu2006$";
		Connection_db c1=new Connection_db(url,name,password);
		con=c1.getConnection();
		if(con!=null) {
			System.out.println("Connection Created");
		}
		else {
			System.out.println("Connection Not Created");
		}
		int id = 0;
		Scanner sc=new Scanner(System.in);
		System.out.println("Do you want to insert Product? (true/false):");
		String b1Input = sc.nextLine();
		boolean b1 = Boolean.parseBoolean(b1Input);
		System.out.println("Insert any Electronics from the list(Laptop,Mobile,Acessories)");
		if(b1==true) {
			String item=sc.nextLine();
			if(item.equals("Laptop")) {
				id=12;
			}
			else if(item.equals("Mobile")) {
				id=13;
			}
			else if(item.equals("Acessories")) {
				id=14;
			}
			c_user c2=new c_user(con);
			
			System.out.println("Input the user mail id");
			String mail_id=sc.nextLine();
			int n=c2.user_id_retrival(mail_id);
			System.out.println(n);
			System.out.println("Input the user name");
			String uname=sc.nextLine();
			Insert_user_cat s1=new Insert_user_cat(con, mail_id, uname,n);
			System.out.println("Enter the Product Name");
			String  p_name=sc.nextLine();
			System.out.println("Enter the Product Description");
			String p_desc=sc.nextLine();
			System.out.println("Enter the Product Image URL");
			String p_img=sc.nextLine();
			sc.nextLine();
			System.out.println("Enter the Product Price");
			int p_price=sc.nextInt();
			System.out.println("Enter the discount percentage");
			int p_dis=sc.nextInt();
			System.out.println("Enter the Product quantity in Stock");
			int p_amount_stock=sc.nextInt();
			s1.insert_product(id,p_name,p_desc,p_img,p_price,p_dis,p_amount_stock);
		}
	}
}
