package com.luv2code.web.jdbc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// set up the reference to the helper util class
	private StudentDBUtil studentDBUtil;

	// set up the resource annotation for connection pool
	@Resource(name = "jdbc/web_student_tracker")
	private DataSource dataSource;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		try {
			studentDBUtil = new StudentDBUtil(dataSource);
		} catch (Exception e) {
			throw new ServletException();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			// read the command parameter
			String theCommand = request.getParameter("command");

			// if the command is missing, then the default command will be list method
			if (theCommand == null) {
				theCommand = "LIST";
			}
			// route to the appropriate method
			switch (theCommand) {
			case "LIST":
				listStudents(request, response);
				break;

			case "ADD":
				addStudent(request, response);
				break;

			case "LOAD":
				loadStudent(request, response);
				break;

			case "UPDATE":
				updateStudent(request, response);
				break;
				
			case "DELETE":
				deleteStudent(request, response);
				break;
				
			default:
				listStudents(request, response);
				break;
			}

			listStudents(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new ServletException();
		}
	}

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// read the student id from the form
		int theStudentId = Integer.parseInt(request.getParameter("studentId"));
		
		// delete student from DB
		studentDBUtil.deleteStudent(theStudentId);

		// send data back to main page
		listStudents(request, response);

		
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// read data from the form
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");

		// create a new student object with that
		Student theStudent = new Student(id, firstName, lastName, email);

		// perform update on DB
		studentDBUtil.updateStudent(theStudent);

		// send the list to the "list-students.jsp"
		listStudents(request, response);

	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// read the student-id from the form data
		String theStudentId = request.getParameter("studentId");

		// get the student data from DB using that form data
		Student theStudent = studentDBUtil.getStudent(theStudentId);

		// add this student data to the request object
		request.setAttribute("THE_STUDENT", theStudent);

		// send this data to the update-student-form.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// read the new student info from the form
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");

		// create a new student object
		Student theStudent = new Student(firstName, lastName, email);

		// add the student to the database
		studentDBUtil.addStudent(theStudent);

		// send data back to main page
		listStudents(request, response);

	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {

		List<Student> students = studentDBUtil.getStudents();
		request.setAttribute("STUDENTS_LIST", students);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
	}

}
