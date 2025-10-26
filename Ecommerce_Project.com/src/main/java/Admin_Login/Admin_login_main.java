package Admin_Login;
import User_Login.Password_checker;
import User_Login.Login_createnewuser;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import Connection_db.Connection_db;
public class Admin_login_main {
	public static void main(String[]arg) throws SQLException {
		Scanner sc=new Scanner(System.in);
		Connection con;
		String url="jdbc:postgresql://localhost:5432/Ecommerce";
		String name="postgres";
		String password="Achu2006$";
		Connection_db c1=new Connection_db(url,name,password);
		con=c1.getConnection();
		if(con!=null) {
			System.out.println("Connection Created");
		}
		System.out.println("Do you have a account");
		boolean b1 = sc.nextBoolean();
		sc.nextLine();
		String us_ma;
		if(b1) {
			System.out.println("Enter User name or Main ID");
			us_ma=sc.nextLine();
			Login_createnewuser l1=new Login_createnewuser(con);
			boolean result=l1.searchuser(us_ma,"admin_page");
			if(result) {
				System.out.println("User Found");
				while(true) {
					String password1=sc.nextLine();
					Password_checker p1=new Password_checker(password1,con); 
					boolean pas=p1.verifier(us_ma, "admin_page");
					if(pas) {
						break;
					}
					System.out.println("Password not Found");
				}
			}
		}
		else {
			System.out.println("No user found. Let's create a new admin account.");
		    System.out.print("Enter Mail ID: ");
		    us_ma = sc.nextLine();
		    System.out.print("Enter Username: ");
		    String u_name = sc.nextLine();
		    System.out.print("Enter Password: ");
		    String pass = sc.nextLine();
		    System.out.print("Enter Aadhar Number: ");
		    Long aad_num = sc.nextLong();
		    sc.nextLine(); 
		    c_user c2 = new c_user(con);
		    c2.create_newuser(us_ma, u_name, pass, aad_num);
		}
		System.out.println("Do you want to view your inserted product(true/false)");
		boolean sc1 = sc.nextBoolean();
		sc.nextLine();
		if(sc1) {
			Admin_prod_view pro1=new Admin_prod_view(con,us_ma);
			pro1.retrive_inserted_product(); 
		}
		else {
			System.out.println("Thank You For Using Our Platform");
		}
	}	
}
/*
--Admin login page:
create table Admin_page(
mail_id varchar(50),
username varchar(50),
password varchar(15),
A_user_key int primary key,
aadhar_number int unique not null
);*/
