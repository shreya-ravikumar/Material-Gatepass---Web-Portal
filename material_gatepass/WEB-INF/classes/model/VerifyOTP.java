package model;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class VerifyOTP
 */
public class VerifyOTP extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VerifyOTP() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
 {
		// TODO Auto-generated method stub
	    ObjectOutputStream out = new ObjectOutputStream(response.getOutputStream()); 
		Connection conn = null;
		PreparedStatement st = null;
		Enumeration paramNames = request.getParameterNames();
        String params[]= new String[3];
		int i=0;
        while(paramNames.hasMoreElements()) {
            String paramName = (String)paramNames.nextElement();
            System.out.println(paramName );
            String[] paramValues = request.getParameterValues(paramName);
            params[i] = paramValues[0];
            System.out.println(params[i]);
            i++;
        }
		 
	try{
		 Class.forName("com.mysql.jdbc.Driver");
         System.out.println("Set connection : URL : jdbc:mysql://172.16.50.70:3306/material_gatepass");
         conn = DriverManager.getConnection("jdbc:mysql://172.16.50.70:3306/material_gatepass","root","Walnut01");
         System.out.println("Create statement");
         String otpstr = request.getParameter("otp");
         System.out.println(otpstr);
         st = conn.prepareStatement("select * from users where userid = '"+params[0]+"'");
	     ResultSet rs = st.executeQuery();
	     ResultSetMetaData rsnd = rs.getMetaData();
         String result = "";
		 while(rs.next()) {
		 result += rsnd.getColumnName(4) + ":" + rs.getString(4) + "\n";		
		 }
		 System.out.println(result);
         String key = result;
         System.out.println(key);
		 long otp = Long.parseLong(otpstr);  
	     long t = System.currentTimeMillis();  
	     AuthenticatorLEOS la = new AuthenticatorLEOS();  
	     la.setWindowSize(5);  // Provide 5 * 30 seconds of grace...  
	     long l = la.check_code(key,otp,t); 
	     if(l == 0 ) {  
	     System.out.println("Fail");
	     out.writeObject("Wrong OTP!");
	     } 
	     else {  
	     System.out.println("Okay, Matched OTP is: " + l);
	     out.writeObject("Verification Successful");  
	     }
	   } 
	catch(Exception e){
        e.printStackTrace();
    }
  }
}

