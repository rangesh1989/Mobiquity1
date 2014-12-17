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

/**
 * Servlet implementation class EditSalesDataServlet
 */
@WebServlet("/SportsStoreTest/EditSalesDataServlet")
public class EditSalesDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditSalesDataServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String sales_id =request.getParameter("salesid");
		String sales_amount = request.getParameter("salesamount");
		String region_id = request.getParameter("regionid");
		String admin_user_name = request.getParameter("adminusername");
		
		ModelSportStop model =new ModelSportStop();
		String url = "jdbc:oracle:thin://@localhost:1521:XE";
		String username="rs";	
		String password="rangesh";
		Connection con = null;
		ResultSet region_name_resultset = null;
		PreparedStatement edit_sales_stat = null;
		PreparedStatement region_name_ppmt = null;
		System.out.println("Entering database");

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
		edit_sales_stat = con.prepareStatement("UPDATE SALES SET SALES_AMOUNT=?, REGION_ID=?,"
				+ " LAST_UPDATED_BY=?"
				+ " WHERE SALE_ID=? ");
		
		
//		Statement sales_statement = con.createStatement();
		
		edit_sales_stat.setString(1,sales_amount);

		edit_sales_stat.setString(2,region_id);

		edit_sales_stat.setString(3,admin_user_name);

		edit_sales_stat.setString(4,sales_id);
		edit_sales_stat.executeUpdate();
		
		con.commit();
		region_name_ppmt= con.prepareStatement("SELECT REGION_NAME FROM REGION WHERE REGION_ID=?");
		
		region_name_ppmt.setString(1, region_id);
		
		region_name_resultset = region_name_ppmt.executeQuery();
		System.out.println("RESULTSET VALUE"+region_name_resultset.next());
			
			model.setRegionName(region_name_resultset.getString("REGION_NAME"));
			
			response.sendRedirect("/test/SportsStoreTest/SearchSalesDataServlet");
		}
		catch (Exception e) {
		      e.printStackTrace();
		    } finally {
		      try {
		    	 // result.close();
		    	  region_name_ppmt.close();
		    	  edit_sales_stat.close();
		        con.close();
		      } catch (SQLException e) {
		    	  response.sendRedirect("SignInError.jsp");
		        e.printStackTrace();
		      }
		    }
		    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
