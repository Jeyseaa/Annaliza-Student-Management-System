package Customer;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;


public class DBHandler {

	private static String login;

	private static String password;

	
	static String databaseUrl;

	
	private final static String studentsTable;

	
	private final static String coursesTable;

	
	private final static String facultiesTable;

	
	public DBHandler() {

	}


	static {
		login = "root";
password = "qwerty123";
		databaseUrl = "jdbc:mysql://localhost:3306/studentsdb";

		studentsTable = "students";
		coursesTable = "courses";
		facultiesTable = "faculties";
	}

	
	public static String getLogin() {
		return login;
	}


	public static void setLogin(final String login) {
		DBHandler.login = login;
	}

	
	public static String getPassword() {
		return password;
	}

	
	public static void setPassword(final String password) {
		DBHandler.password = password;
	}

	
	public static void setDatabaseUrl(final String databaseUrl) {
		DBHandler.databaseUrl = databaseUrl;
	}

	
	public static String getDatabaseUrl() {
		return databaseUrl;
	}

	
	public static String getStudentsTable() {
		return studentsTable;
	}

	public static String getFacultiesTable() {
		return facultiesTable;
	}

	public static String getCoursesTable() {
		return coursesTable;
	}


	public static boolean checkIfTableExists(final String tableName) {
		try {
			Connection connection = DriverManager.getConnection(databaseUrl, login, password);

			
			DatabaseMetaData dbmData = connection.getMetaData();
			ResultSet resultSet = dbmData.getTables(null, null, tableName, null);
			while (resultSet.next()) {
				if (resultSet.getString(3).equals(tableName)) {
					
					return true;
				}
			}

			connection.close();
			resultSet.close();

			return false;
		} catch (SQLException e) {
			e.printStackTrace();

			
			return false;
		}
	}

	
	public static boolean createTables() {
		try {
			Connection connection = DriverManager.getConnection(databaseUrl, login, password);
			Statement statement = connection.createStatement();

			if (!checkIfTableExists(studentsTable)) {
				
				statement.executeUpdate("create table " + studentsTable + "(ID INTEGER not NULL AUTO_INCREMENT, "
						+ " Name varchar(50), " + "Surname varchar(50), " + "Age INTEGER, " + "Gender varchar(6), "
						+ "Course varchar(50), " + "Started varchar(25),  " + "Graduation varchar(25), "
						+ "PRIMARY KEY ( id ))");
			}

			if (!checkIfTableExists(coursesTable)) {
				
				statement.executeUpdate("create table " + coursesTable + "(ID INTEGER not NULL AUTO_INCREMENT, "
						+ " Name varchar(50), " + "Faculty varchar(50), " + "Duration INTEGER, " + "Attendees INTEGER, "
						+ "PRIMARY KEY ( id ))");
			}

			if (!checkIfTableExists(facultiesTable)) {
				
				statement.executeUpdate("create table " + facultiesTable + "(ID INTEGER not NULL AUTO_INCREMENT, "
						+ " Name varchar(50), " + "Courses INTEGER, " + "Attendees INTEGER, " + "PRIMARY KEY ( id ))");
			}

			connection.close();
			statement.close();

			
			return true;

		} catch (SQLException e) {
			e.printStackTrace();

			return false;
		}
	}

	
	public static boolean addStudent() {
		try {
			Connection connection = DriverManager.getConnection(databaseUrl, login, password);
			PreparedStatement preparedStatement = connection.prepareStatement("insert into " + studentsTable
					+ " (Name, Surname, Age, Gender, Course, Started, Graduation) values " + "(?, ?, ?, ?, ?, ?, ?)");

			
			PreparedStatement preparedStatement2 = connection
					.prepareStatement("select Duration from Courses where Name = " + "\""
							+ ManagementView.courseSelectionBox.getSelectedItem().toString() + "\"");
			ResultSet resultSet = preparedStatement2.executeQuery();
			resultSet.next();
			final int courseDuration = resultSet.getInt("Duration");

			preparedStatement.setString(1, ManagementView.nameField.getText());
			preparedStatement.setString(2, ManagementView.surnameField.getText());
			preparedStatement.setInt(3, Integer.parseInt(ManagementView.ageField.getText()));
			preparedStatement.setString(4, ManagementView.genderSelectionBox.getSelectedItem().toString());
			preparedStatement.setString(5, ManagementView.courseSelectionBox.getSelectedItem().toString());

			final String inputDate = ManagementView.startedDateField.getText();
			LocalDate startedDate = LocalDate.of(Integer.parseInt(inputDate.substring(0, 4)),
					Integer.parseInt(inputDate.substring(5, 7)), Integer.parseInt(inputDate.substring(8, 10)));
			preparedStatement.setString(6, startedDate.toString());

			LocalDate graduationDate = startedDate.plusMonths(courseDuration);
			preparedStatement.setString(7, graduationDate.toString());

			preparedStatement.executeUpdate();

			connection.close();
			preparedStatement.close();

			updateStudents();

			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();

			
			return false;
		} catch (Exception e) {
			e.printStackTrace();

			
			return false;
		}
	}

	
	public static boolean updateStudents() {
		int howManyColumns = 0, currentColumn = 0;

		try {
			Connection connection = DriverManager.getConnection(databaseUrl, login, password);
			PreparedStatement preparedStatement = connection.prepareStatement("select * from " + studentsTable);

		
			ResultSet resultSet = preparedStatement.executeQuery();
			ResultSetMetaData rsmData = resultSet.getMetaData();

			howManyColumns = rsmData.getColumnCount();

			DefaultTableModel recordTable = (DefaultTableModel) ManagementView.table.getModel();
			recordTable.setRowCount(0);

			while (resultSet.next()) {
				Vector columnData = new Vector();

				for (currentColumn = 1; currentColumn <= howManyColumns; currentColumn++) {
					columnData.add(resultSet.getString("ID"));
					columnData.add(resultSet.getString("Name"));
					columnData.add(resultSet.getString("Surname"));
					columnData.add(resultSet.getString("Age"));
					columnData.add(resultSet.getString("Gender"));
					columnData.add(resultSet.getString("Course"));
					columnData.add(resultSet.getString("Started"));
					columnData.add(resultSet.getString("Graduation"));
				}

				recordTable.addRow(columnData);
			}

			updateAttendees();

			connection.close();
			preparedStatement.close();
			resultSet.close();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();

			return false;
		}
	}

	
	public static boolean deleteStudent() {
		
		DefaultTableModel recordTable = (DefaultTableModel) ManagementView.table.getModel();
		int selectedRow = ManagementView.table.getSelectedRow();
		ManagementView.table.clearSelection();

		try {
			
			final int ID = Integer.parseInt(recordTable.getValueAt(selectedRow, 0).toString());

			Connection connection = DriverManager.getConnection(databaseUrl, login, password);
			PreparedStatement preparedStatement = connection
					.prepareStatement("delete from " + studentsTable + " where id = ?");

			preparedStatement.setInt(1, ID);
			preparedStatement.executeUpdate();

			connection.close();
			preparedStatement.close();

			updateStudents();

			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();

			
			return false;
		}
	}


	public static boolean addFaculty(final String facultyName) {
		try {
			Connection connection = DriverManager.getConnection(databaseUrl, login, password);
			PreparedStatement preparedStatement = connection.prepareStatement(
					"insert into " + facultiesTable + " (Name, Courses, Attendees) values " + "(?, ?, ?)");

			preparedStatement.setString(1, facultyName);
			preparedStatement.setInt(2, 0);
			preparedStatement.setInt(3, 0);

			preparedStatement.executeUpdate();

			connection.close();
			preparedStatement.close();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();

			
			return false;
		}
	}

	
	public static boolean addCourse(final String courseName, final String faculty, final int duration) {
		try {
			Connection connection = DriverManager.getConnection(databaseUrl, login, password);
			PreparedStatement preparedStatement = connection.prepareStatement(
					"insert into " + coursesTable + " (Name, Faculty, Duration, Attendees) values " + "(?, ?, ?, ?)");

			preparedStatement.setString(1, courseName);
			preparedStatement.setString(2, faculty);
			preparedStatement.setInt(3, duration);
			preparedStatement.setInt(4, 0);

			preparedStatement.executeUpdate();

			connection.close();
			preparedStatement.close();

			updateAttendees();

			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();

			
			return false;
		}
	}

	
	public static String[] getFaculties() {
		Vector<String> faculties = new Vector<String>();

		try {
			Connection connection = DriverManager.getConnection(databaseUrl, login, password);
			PreparedStatement preparedStatement = connection.prepareStatement("select Name from faculties");
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				faculties.add(resultSet.getString("Name"));
			}

			connection.close();
			preparedStatement.close();
			resultSet.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}


		return faculties.toArray(new String[0]);
	}

	
	public static String[] getCourses() {
		Vector<String> courses = new Vector<String>();

		try {
			Connection connection = DriverManager.getConnection(databaseUrl, login, password);
			PreparedStatement preparedStatement = connection.prepareStatement("select Name from courses");
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				courses.add(resultSet.getString("Name"));
			}

			connection.close();
			preparedStatement.close();
			resultSet.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return courses.toArray(new String[0]);
	}

	
	private static void updateAttendees() {
		updateCoursesAttendees();
		updateFacultiesAttendees();
	}

	
	private static boolean updateCoursesAttendees() {
		try {
			Connection connection = DriverManager.getConnection(databaseUrl, login, password);
			PreparedStatement preparedStatement = connection.prepareStatement("select Course from " + studentsTable);
			Statement statement = connection.createStatement();

			
			statement.executeUpdate("update " + getCoursesTable() + " set Attendees = 0");

			
			ResultSet resultSet = preparedStatement.executeQuery();
			HashMap<String, Integer> coursesAttendees = new HashMap<String, Integer>();

	
			MAINLOOP: while (resultSet.next()) {
				String currentCourse = resultSet.getString("Course");

				for (String key : coursesAttendees.keySet()) {
					
					if (currentCourse.equals(key)) {
						coursesAttendees.put(key, coursesAttendees.get(key) + 1);
						continue MAINLOOP;
					}
				}

				coursesAttendees.put(currentCourse, 1);
			}

			
			for (String key : coursesAttendees.keySet()) {
				statement.executeUpdate("update " + coursesTable + " set Attendees = " + coursesAttendees.get(key)
						+ " where Name = " + "\"" + key + "\"");
			}

			connection.close();
			preparedStatement.close();
			statement.close();
			resultSet.close();

			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();

			
			return false;
		}
	}

	private static boolean updateFacultiesAttendees() {
		try {
			Connection connection = DriverManager.getConnection(databaseUrl, login, password);
			PreparedStatement preparedStatement = null, preparedStatement2 = null;
			Statement statement = connection.createStatement();
			ResultSet resultSet = null, resultSet2 = null;

			statement.executeUpdate("update " + facultiesTable + " set Attendees = 0, Courses = 0");

			preparedStatement = connection.prepareStatement("select Faculty, Attendees from " + coursesTable);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				final String faculty = resultSet.getString("Faculty");
				final int courseAttendees = resultSet.getInt("Attendees");

				preparedStatement2 = connection.prepareStatement(
						"select Attendees, Courses from " + facultiesTable + " where Name = " + "\"" + faculty + "\"");
				resultSet2 = preparedStatement2.executeQuery();

				resultSet2.next();
				final int currentNumberOfAttendees = resultSet2.getInt("Attendees");
				final int currentNumberOfCourses = resultSet2.getInt("Courses");

				statement.executeUpdate("update " + facultiesTable + " set Attendees = "
						+ (courseAttendees + currentNumberOfAttendees) + " where Name = " + "\"" + faculty + "\"");

				statement.executeUpdate("update " + facultiesTable + " set Courses = " + (currentNumberOfCourses + 1)
						+ " where Name = " + "\"" + faculty + "\"");
			}

			connection.close();
			preparedStatement.close();
			if (preparedStatement2 != null)
				preparedStatement2.close();
			resultSet.close();
			if (resultSet2 != null)
				resultSet2.close();
			statement.close();

			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();

			return false;
		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}
	}

	
	public static boolean checkIfElementExists(final String tableName, final String name) {
		try {
			Connection connection = DriverManager.getConnection(databaseUrl, login, password);
			PreparedStatement preparedStatement = connection.prepareStatement("select Name from " + tableName);

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				if (resultSet.getString("Name").equals(name)) {
					
					return true;
				}
			}

			connection.close();
			preparedStatement.close();
			resultSet.close();

		
			return false;
		} catch (SQLException e) {
			e.printStackTrace();

			return false;
		}
	}


	public static int getNumberOfAttendees(final String tableName, final String element) {
		try {
			Connection connection = DriverManager.getConnection(databaseUrl, login, password);
			PreparedStatement preparedStatement = connection
					.prepareStatement("select Attendees from " + tableName + " where Name = " + "\"" + element + "\"");

			
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			int attendees = resultSet.getInt("Attendees");

			connection.close();
			preparedStatement.close();
			resultSet.close();

			return attendees;

		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}


	public static boolean deleteCourseAttendees(final String course) {
		try {
			Connection connection = DriverManager.getConnection(databaseUrl, login, password);
			Statement statement = connection.createStatement();

			statement.executeUpdate("delete from " + getStudentsTable() + " where Course = " + "\"" + course + "\"");

			updateStudents();

			connection.close();
			statement.close();

			return true;

		} catch (SQLException e) {
			e.printStackTrace();

			return false;
		}
	}

	
	public static boolean deleteCourse(final String course) {
		try {
			Connection connection = DriverManager.getConnection(databaseUrl, login, password);
			Statement statement = connection.createStatement();

			statement.executeUpdate("delete from " + getCoursesTable() + " where Name = " + "\"" + course + "\"");

			updateStudents();

			connection.close();
			statement.close();

			return true;

		} catch (SQLException e) {
			e.printStackTrace();

			return false;
		}
	}

	public static boolean deleteFaculty(final String faculty) {
		try {
			Connection connection = DriverManager.getConnection(databaseUrl, login, password);
			Statement statement = connection.createStatement();

			statement.executeUpdate("delete from " + getFacultiesTable() + " where Name = " + "\"" + faculty + "\"");

			updateStudents();

			connection.close();
			statement.close();

			return true;

		} catch (SQLException e) {
			e.printStackTrace();

			return false;
		}
	}

	
	public static boolean deleteFacultyCourses(final String faculty) {
		try {
			Connection connection = DriverManager.getConnection(databaseUrl, login, password);
			Statement statement = connection.createStatement();

			
			PreparedStatement preparedStatement = connection.prepareStatement(
					"select Name from " + getCoursesTable() + " where Faculty = " + "\"" + faculty + "\"");
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				deleteCourseAttendees(resultSet.getString("Name"));
			}

			
			statement.executeUpdate("delete from " + getCoursesTable() + " where Faculty = " + "\"" + faculty + "\"");

			updateStudents();

			connection.close();
			statement.close();
			preparedStatement.close();
			resultSet.close();

			return true;

		} catch (SQLException e) {
			e.printStackTrace();

			return false;
		}
	}


	public static int getNumberOfCourses(final String faculty) {
		try {
			Connection connection = DriverManager.getConnection(databaseUrl, login, password);
			PreparedStatement preparedStatement = connection.prepareStatement(
					"select Courses from " + getFacultiesTable() + " where Name = " + "\"" + faculty + "\"");

			
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			int courses = resultSet.getInt("Courses");

			connection.close();
			preparedStatement.close();

			return courses;

		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}


	public static boolean updateDatabase() {

		int selectedRow = ManagementView.table.getSelectedRow();
		int selectedColumn = ManagementView.table.getSelectedColumn();

		try {
			Connection connection = DriverManager.getConnection(databaseUrl, login, password);
			Statement statement = connection.createStatement();

			if (selectedRow > -1 && selectedColumn > -1) {
		
				if (selectedColumn == 1) {
					statement.executeUpdate("update " + studentsTable + " set Name = " + "\""
							+ ManagementView.table.getValueAt(selectedRow, selectedColumn).toString() + "\""
							+ " where id = "
							+ Integer.parseInt(ManagementView.table.getValueAt(selectedRow, 0).toString()));
				} else if (selectedColumn == 2) {
					statement.executeUpdate("update " + studentsTable + " set Surname = " + "\""
							+ ManagementView.table.getValueAt(selectedRow, selectedColumn).toString() + "\""
							+ " where id = "
							+ Integer.parseInt(ManagementView.table.getValueAt(selectedRow, 0).toString()));
				} else if (selectedColumn == 3) {
					statement.executeUpdate("update " + studentsTable + " set Age = "
							+ Integer.parseInt(ManagementView.table.getValueAt(selectedRow, selectedColumn).toString())
							+ " where id = "
							+ Integer.parseInt(ManagementView.table.getValueAt(selectedRow, 0).toString()));
				} else if (selectedColumn == 4) {
					statement.executeUpdate("update " + studentsTable + " set Gender = " + "\""
							+ ManagementView.table.getValueAt(selectedRow, selectedColumn).toString() + "\""
							+ " where id = "
							+ Integer.parseInt(ManagementView.table.getValueAt(selectedRow, 0).toString()));
				}
			}

			connection.close();
			statement.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();

			return false;
		} catch (Exception ex) {
			ex.printStackTrace();

			return false;
		}
	}
}
