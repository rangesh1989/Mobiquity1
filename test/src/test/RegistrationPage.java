package test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegistrationPage
 */
@WebServlet("/SportsStoreTest/RegistrationPage")
public class RegistrationPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistrationPage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String ufirstname = request.getParameter("firstname");
		String ulastname = request.getParameter("lastname");
		String uname = request.getParameter("username");
		String userconfPass = request.getParameter("userconfPass");
		String uemail = request.getParameter("userEmail");
		String upass = request.getParameter("userPass");
		
		if(uname!="" && uemail!="" && upass!="" && userconfPass!="")
		{
			if(upass.equals(userconfPass))
			{
			String url = "jdbc:oracle:thin://@localhost:1521:XE";
			String username= "rs";
			String password ="rangesh";
			
			Connection con = null;
			ResultSet rs = null;
			PreparedStatement ppmt = null;
			System.out.println("Entering Database");
			try 
			{
				Class.forName("oracle.jdbc.OracleDriver");
			}
			catch(java.lang.ClassNotFoundException e){
				System.err.println("ClassNotFoundException:");
				System.err.println(e.getMessage());
				}
	
			try {
				con = DriverManager.getConnection(url,username,password);
				System.out.println("Entering 2nd try");
				ppmt = con.prepareStatement("INSERT INTO SSTP_USERS(USER_NAME,USER_PASSWORD,"
						+ "EMAIL_ADD,FIRST_NAME,LAST_NAME,SUBSCIBER_LEVEL) VALUES(?, ?, ?, ?, ?,?)");
			
	//			Statement sales_statement = con.createStatement();
				
				ppmt.setString(1,uname);
				ppmt.setString(2,upass);
				
				ppmt.setString(3,uemail);
				ppmt.setString(4,ufirstname);
				ppmt.setString(5,ulastname);
				ppmt.setString(6,"0");
				
				ppmt.executeUpdate();
				con.commit();
				response.sendRedirect("RegistrationSuccessScreen.html");
		}
			catch (Exception e) {
			      e.printStackTrace();
			      response.sendRedirect("SignInError.jsp");
			    } finally {
			      try {
			    	 // result.close();
			    	  ppmt.close();
			        con.close();
			      } catch (SQLException e) {
			        e.printStackTrace();
			      }
			    }
			}
			else
			{
		response.sendRedirect("SignInError.jsp");
			}
		}
		else
		{
			response.sendRedirect("SignInError.jsp");
		}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
