package test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import test.ModelSportStop;

/**
 * Servlet implementation class SignUp
 */
@WebServlet("/SportsStoreTest/SignUp")
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUp() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		

//		String username = "rangesh";
//		String password = "rangesh";
		String userid = request.getParameter("username");
		String u_pwd = request.getParameter("password");
		
		ModelSportStop model= new ModelSportStop();
		String url = "jdbc:oracle:thin://@localhost:1521:XE";
		String username= "rs";
		String password ="rangesh";
		
		Connection con = null;
		ResultSet resultsetlogin = null;
		PreparedStatement login1_statement = null;
		System.out.println("Entering Database");
		System.out.println("Entering Database"+userid);
		System.out.println("Entering Database"+u_pwd);
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
			login1_statement = con.prepareStatement("SELECT * FROM "
					+ "SSTP_USERS WHERE USER_NAME= ? AND USER_PASSWORD= ?");
			
			
//			Statement sales_statement = con.createStatement();
			login1_statement.setString(1,userid);
			login1_statement.setString(2,u_pwd);
			resultsetlogin=login1_statement.executeQuery();
			
//			System.out.println("RESULT SET VALUE"+resultsetlogin.next());
			
			if(resultsetlogin.next())
			{
				String sval = resultsetlogin.getString("SUBSCIBER_LEVEL");
				String uval = resultsetlogin.getString("USER_NAME");
				model.setsUsername(uval);
				if(sval.equals("1"))
				{
					response.sendRedirect("admin-home.html");
				}
				else{
					response.sendRedirect("index-home.html");
				}
				
			}		
				else{
					response.sendRedirect("SignInError.jsp");
			}
		
		}
	
		catch (Exception e) {
		      e.printStackTrace();
//		      response.sendRedirect("SignInError.jsp");
		    } finally {
		      try {
		    	 // result.close();
		    	  login1_statement.close();
					con.close();
		   
		      } catch (SQLException e) {
//		    	  response.sendRedirect("SignInError.jsp");
		        e.printStackTrace();
		      }
		    }

		
		/*if(unname!="" && pwd!="")
		{
		if(unname.equals(username) && pwd.equals(password))
		{
			unname="";
			pwd= "";
			response.sendRedirect("index-home.html");
		}
		else if(unname.equals("admin") && pwd.equals("admin"))
		{
			unname="";
			pwd= "";
			response.sendRedirect("admin-home.html");
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
*/

			}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
			}

}
