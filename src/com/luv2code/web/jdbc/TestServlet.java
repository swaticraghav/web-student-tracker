package com.luv2code.web.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// define the connection pool/data source for resource injection
	@Resource(name = "jdbc/web_student_tracker")
	private DataSource datasource;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// get the printWriter object
		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");
		
		// set up the connection to the dataBase
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			
			connection = datasource.getConnection();
			// create SQL statement
			String sql = "Select * from student";
			statement = connection.createStatement();
			
			// Execute the SQL statement
			resultSet = statement.executeQuery(sql);
			
			// process the result
			while(resultSet.next()) {
				String email = resultSet.getString("email");
				out.println(email);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
