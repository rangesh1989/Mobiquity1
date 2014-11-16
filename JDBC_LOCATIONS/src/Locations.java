
import java.sql.*;
public class Locations{
public static void main (String args[])
{
String url = "jdbc:oracle:thin:@localhost:1521:XE";
String username="system";	
String password="TEE!$!wine027";
Connection con;
Statement stmt;
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
String a[] = null;
while (rs.next()) 
{
//	for(i =1;i<columnCount+1;i++)
//	{
//		String name = rsmd.getColumnName(i);
//		String s= rs.getString(name);
//		//String s2 = rs.getString("COUNTRY_ID");
//		//String s3 = rs.getString("REGION_ID");
//		//int i=rs.getInt("region_id");
//		System.out.println(s);
//	}
//System.out.println("\n");
	
	String s = rs.getString("LOCATION_ID");

	String s1 = rs.getString("STREET_ADDRESS");

	String s2 = rs.getString("POSTAL_CODE");

	String s3 = rs.getString("CITY");

	String s4 = rs.getString("STATE_PROVINCE");

	String s5 = rs.getString("COUNTRY_ID");

	String s6 = rs.getString("CREATED_BY");

	String s7 = rs.getString("CREATION_DATE");

	String s8 = rs.getString("LAST_UPDATE_DATE");
	
	System.out.println(s+" "+s1+" "+s2+" "+s3+" "+s4+" "+s5+" "+s6+" "+s7+" "+s8);
	
}

stmt.close();
con.close();
} catch(SQLException ex) {
System.err.println("SQLException: " + ex.getMessage());
}
}
}