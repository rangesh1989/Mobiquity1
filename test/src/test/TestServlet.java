package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/test/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String url = "jdbc:oracle:thin://@localhost:1521:XE";
		String username="system";	
		String password="TEE!$!wine027";
		Connection con;
		Statement stmt;
		System.out.println("Entering database");
		String query="SELECT * FROM LOCATIONS";
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
		stmt= con.createStatement();
		ResultSet rs= stmt.executeQuery(query);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		int i;

		//html code starts here
		response.setContentType("text/html");
		PrintWriter pr = response.getWriter();
		pr.println("<!DOCTYPE HTML><html lang=\"en\">");
		pr.println("<head><title>SAMPLE SE</title></head>");
		pr.println("<body>");
		pr.println("<table border=\"1\">");
		
		while (rs.next()) 
		{
//			for(i =1;i<columnCount+1;i++)
//			{
//				String name = rsmd.getColumnName(i);
//				String s= rs.getString(name);
//				//String s2 = rs.getString("COUNTRY_ID");
//				//String s3 = rs.getString("REGION_ID");
//				//int i=rs.getInt("region_id");
//				System.out.println(s);
//			}
		//System.out.println("\n");

			
			String s = rs.getString("LOCATION_ID");

			String s1 = rs.getString("STREET_ADDRESS");

			String s2 = rs.getString("POSTAL_CODE");

			String s3 = rs.getString("CITY");

			String s4 = rs.getString("STATE_PROVINCE");

			String s5 = rs.getString("COUNTRY_ID");

			String s6 = rs.getString("CREATION_DATE");

			String s7 = rs.getString("CREATED_BY");

			String s8 = rs.getString("LAST_UPDATE_DATE");
			
			//System.out.println(s+" "+s1+" "+s2+" "+s3+" "+s4+" "+s5+" "+s6+" "+s7+" "+s8);
		
					pr.println("<tr >"
							+ "<td width:15px,height:10px>"
							+ s
							+ "</td>"
							+ "<td>"
							+ s1
							+ "</td>"
							+ "<td>"
							+ s2
							+ "</td>"
							+ "<td>"
							+ s3
							+ "</td>"
							+ "<td>"
							+ s4
							+ "</td>"
							+"<td>"
							+ s5
							+ "</td>"
							+"<td>"
							+ s7
							+ "</td>"
							+"<td>"
							+ s6
							+ "</td>"
							+ "</tr>");
				

		}

		pr.println("</table>");
		pr.println("</body>");
		pr.println("</html>");

		//html ends here
		stmt.close();
		con.close();
		
		
		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
			}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
