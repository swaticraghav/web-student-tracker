package com.luv2code.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDBUtil {

	// Set up the reference to the data source.
	private DataSource dataSource;

	// set up the constructor.
	public StudentDBUtil(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	// create the method to list the student.
	public List<Student> getStudents() throws SQLException {

		// create an empty list
		List<Student> students = new ArrayList<Student>();

		// put the data coming from DB into the list

		// set JDBC object and set it to null
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		try {

			// get the connection
			myConn = dataSource.getConnection();

			// create SQL statement
			String sql = "Select * from student order by last_name";
			myStmt = myConn.createStatement();

			// execute the query
			myRs = myStmt.executeQuery(sql);

			// process the result set
			while (myRs.next()) {

				// retrieve data from result set row
				int id = myRs.getInt("id");
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");

				// create a new student object
				Student tempStudent = new Student(id, firstName, lastName, email);

				// add it to the list of students
				students.add(tempStudent);
			}

			// return the list
			return students;
		} finally {

			// close JDBC ojects
			close(myConn, myStmt, myRs);

		}

	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {

		try {

			if (myConn != null) {
				myConn.close();
			}
			if (myStmt != null) {
				myStmt.close();
			}
			if (myRs != null) {
				myRs.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void addStudent(Student theStudent) throws SQLException {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {

			// get the DB connection
			myConn = dataSource.getConnection();

			// create sql for create
			String sql = "insert into student " + "(first_name, last_name, email)" + "values (?, ?, ?)";
			myStmt = myConn.prepareStatement(sql);

			// set the param values for the students
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());

			// execute SQl
			myStmt.execute();
		} finally {

			// clean up JDBC objects
			close(myConn, myStmt, null);
		}

	}

	public Student getStudent(String theStudentId) throws Exception {
		
		Student theStudent = null;
		Connection myConn= null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int studentId;
		
		try {
			
			// convert student_id to int
			studentId = Integer.parseInt(theStudentId);
			
			// get connection to DB
			myConn = dataSource.getConnection();
			
			// create SQL
			String sql = "select * from student where id=?";
			
			// create prepared statement
			myStmt = myConn.prepareStatement(sql);
			
			// set params
			myStmt.setInt(1, studentId);
			
			// execute sql
			myRs = myStmt.executeQuery();
			
			// get the data back from DB
			if(myRs.next()) {
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				theStudent = new Student(studentId, firstName, lastName, email);
			}else {
				throw new Exception("Cound not find student ID: " + studentId);
			}
			
			return theStudent;
		} finally {
			
			close(myConn, myStmt, null);
		}
		
		
	}

	public void updateStudent(Student theStudent) throws SQLException {
		
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			
			// get the DB connection
			myConn = dataSource.getConnection();

			// create sql for create
			String sql = "update student "
					+ "set first_name=?, last_name=?, email=? "
					+ "where id=?";
			myStmt = myConn.prepareStatement(sql);

			// set the param values for the students
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			myStmt.setInt(4, theStudent.getId());

			// execute SQl
			myStmt.execute();
			
		} finally {
			close(myConn, myStmt, null);
		}
		
		
	}

	public void deleteStudent(int theStudentId) throws SQLException {

		Connection myConn= null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {

			// get connection to DB
			myConn = dataSource.getConnection();
			
			// create SQL
			String sql = "delete from student where id=?";
			
			// create prepared statement
			myStmt = myConn.prepareStatement(sql);
			
			// set params
			myStmt.setInt(1, theStudentId);
			
			// execute sql
			myStmt.execute();
						
		} finally {
			
			close(myConn, myStmt, null);
		}
		
	}
}
