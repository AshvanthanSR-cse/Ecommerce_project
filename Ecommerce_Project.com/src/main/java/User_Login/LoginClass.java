package User_Login;
import Connection_db.Connection_db;
import Home_Page.ProductCatalogManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
public class LoginClass {
	public static void main(String[] a) throws SQLException {
		Scanner sc=new Scanner(System.in);
		String name="postgres";
		String password="Achu2006$";
		String url="jdbc:postgresql://localhost:5432/Ecommerce";
		Connection_db l1=new Connection_db(url,name,password);
		Connection con = l1.getConnection();
		Login_createnewuser cl1=new Login_createnewuser(con);
		boolean case1 = sc.nextBoolean(); 
		sc.nextLine();
		String username;
		String mail_is;
		if(case1==true) {
		System.out.println("Enter User Name or Mail ID:-");
		username=sc.nextLine();
		boolean n = cl1.searchuser(username, "acess");
		if(n==true) {
			System.out.println("User Found");
			while(true) {
			String password1=sc.nextLine();
			Password_checker p1 = new Password_checker(password1, con);
			boolean correct = p1.verifier(username,"acess");
			if(!correct) {
			    System.out.println("Access denied! Incorrect password.");
			}
			else {
				System.out.println("Access granted");
				ProductCatalogManager o1=new ProductCatalogManager(con);
				break;
			}
			}
		}
		else {
			System.out.println("User not found, Creating new user!");
			System.out.println("Create a user name");
			username=sc.nextLine();
			System.out.println("Enter your mail id");
			mail_is=sc.nextLine();
			System.out.println("Create a new Password here");
			String password1=sc.nextLine();
			System.out.println("Enter your data of birth");
			String dateof_birth = sc.nextLine();
			LocalDate dob = LocalDate.parse(dateof_birth);
			LocalDate currentDate = LocalDate.now();
			int age = Period.between(dob, currentDate).getYears();
			System.out.println("Enter your Phone Number");
			long ph_no=sc.nextLong();
			sc.nextLine();
			cl1.create_newuser(username,mail_is,age,dateof_birth,password1,ph_no);
		}
		}
		
		else {
			System.out.println("User not found, Creating new user!");
			System.out.println("Create a user name");
			username=sc.nextLine();
			System.out.println("Enter your mail id");
			mail_is=sc.nextLine();
			System.out.println("Create a new Password here");
			String password1=sc.nextLine();
			System.out.println("Enter your data of birth");
			String dateof_birth = sc.nextLine();
			LocalDate dob = LocalDate.parse(dateof_birth);
			LocalDate currentDate = LocalDate.now();
			int age = Period.between(dob, currentDate).getYears();
			System.out.println("Enter your Phone Number");
			long ph_no=sc.nextLong();
			sc.nextLine();
			cl1.create_newuser(username,mail_is,age,dateof_birth,password1,ph_no);
		}
	}
}
