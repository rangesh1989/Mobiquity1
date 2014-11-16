
import java.sql.*;
public class Countries{
public static void main (String args[])
{
String url = "jdbc:oracle:thin:@localhost:1521:XE";
String username="system";	
String password="TEE!$!wine027";
Connection con;
Statement stmt;
String query="SELECT * FROM COUNTRIES";
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
	
	String s = rs.getString("COUNTRY_ID");

	String s1 = rs.getString("COUNTRY_NAME");

	String s2 = rs.getString("REGION_ID");

	String s3 = rs.getString("CREATION_DATE");

	String s4 = rs.getString("CREATED_BY");

	String s5 = rs.getString("LAST_UPDATE_DATE");
	
	System.out.println(s+" "+s1+" "+s2+" "+s3+" "+s4+" "+s5);
	
}

stmt.close();
con.close();
} catch(SQLException ex) {
System.err.println("SQLException: " + ex.getMessage());
}
}
}