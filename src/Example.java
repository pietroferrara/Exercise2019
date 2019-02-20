import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public class Example extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
	
		String param = "";
		if (request.getHeader("BenchmarkTest00205") != null) {
			param = request.getHeader("BenchmarkTest00205");
		}
		
		// URL Decode the header value since req.getHeader() doesn't. Unlike req.getParameter().
		param = java.net.URLDecoder.decode(param, "UTF-8");
		
		
		String bar = "alsosafe";
		if (param != null) {
			java.util.List<String> valuesList = new java.util.ArrayList<String>( );
			valuesList.add("safe");
			valuesList.add( param );
			valuesList.add( "moresafe" );
			
			valuesList.remove(0); // remove the 1st safe value
			
			bar = valuesList.get(0); // get the last 'safe' value
		}
		
		
		String sql = "INSERT INTO users (username, password) VALUES ('foo','"+ bar + "')";
				
		try {
			java.sql.Statement statement = getSqlStatement();
			int count = statement.executeUpdate( sql, new String[] {"USERNAME","PASSWORD"} );
		} catch (java.sql.SQLException e) {
        	response.getWriter().println("Error processing request.");
        		return;
		}
	}
	
	

	private static Statement stmt;
	private static Connection conn;
	
	private static java.sql.Statement getSqlStatement() {
		if (conn == null) {
			getSqlConnection();
		}

		if (stmt == null) {
			try {
				stmt = conn.createStatement();
			} catch (SQLException e) {
				System.out.println("Problem with database init.");
			}
		}

		return stmt;
	}
	
	private static java.sql.Connection getSqlConnection() {
		if (conn == null) {
			try {
				InitialContext ctx = new InitialContext();
				DataSource datasource = (DataSource)ctx.lookup("java:comp/env/jdbc/BenchmarkDB");
				conn = datasource.getConnection();
				conn.setAutoCommit(false);
			} catch (SQLException | NamingException e) {
				System.out.println("Problem with getSqlConnection.");
				e.printStackTrace();
			}
		}
		return conn;
	}
}
