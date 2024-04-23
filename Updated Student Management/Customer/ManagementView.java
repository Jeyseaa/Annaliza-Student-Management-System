package Customer;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class ManagementView {

	static JFrame managementFrame;
	static JTable table;
	static JTextField nameField;
	static JTextField surnameField;
	static JTextField ageField;
	static JTextField startedDateField;
	static JComboBox genderSelectionBox;
	static JComboBox courseSelectionBox;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ManagementView window = new ManagementView();
					window.managementFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ManagementView() {
		initialize();
		table.clearSelection();
		managementFrame.setVisible(true);
		DBHandler.updateStudents();
	}

	private void updateCourses() {
		DefaultComboBoxModel courses = new DefaultComboBoxModel(DBHandler.getCourses());
		courseSelectionBox.setModel(courses);
	}

	private void initialize() {
		managementFrame = new JFrame();
		managementFrame.setBounds(100, 100, 860, 540);
		managementFrame.setResizable(false);
		managementFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		managementFrame.setTitle("SMS");
		managementFrame.getContentPane().setLayout(null);

		JPanel tablePanel = new JPanel();
		tablePanel.setBorder(new LineBorder(SystemColor.textHighlight, 5));
		tablePanel.setBounds(260, 10, 575, 395);
		managementFrame.getContentPane().add(tablePanel);
		tablePanel.setLayout(null);

		JScrollPane tableScrollPane = new JScrollPane();
		tableScrollPane.setBounds(10, 10, 555, 375);
		tablePanel.add(tableScrollPane);

		table = new JTable();
		tableScrollPane.setViewportView(table);
		table.setColumnSelectionAllowed(true);
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "ID", "Name", "Surname", "Age", "Gender", "Course", "Started", "Graduation" }) {
			boolean[] columnEditables = new boolean[] { false, true, true, true, true, false, false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});

		TableRowSorter tableSorter = new TableRowSorter(table.getModel());
		table.setRowSorter(tableSorter);

		table.getModel().addTableModelListener(new TableModelListener() {

			public void tableChanged(TableModelEvent e) {
				if (!DBHandler.updateDatabase()) {
					JOptionPane.showMessageDialog(managementFrame, "Check input", "SMS", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBorder(new LineBorder(new Color(0, 120, 215), 5));
		buttonsPanel.setBackground(UIManager.getColor("Button.background"));
		buttonsPanel.setBounds(10, 415, 825, 80);
		managementFrame.getContentPane().add(buttonsPanel);

		JButton deleteButton = new JButton("Delete");
		deleteButton.setName("deleteButton");

		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(managementFrame, "No student selected", "SMS",
							JOptionPane.ERROR_MESSAGE);
				} else {
					if (JOptionPane.showConfirmDialog(managementFrame, "Warning: Delete student?", "SMS",
							JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						if (DBHandler.deleteStudent()) {
							JOptionPane.showMessageDialog(managementFrame, "Student successfully deleted", "SMS",
									JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(managementFrame, "Something went wrong", "SMS",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		});

		deleteButton.setFont(new Font("Tahoma", Font.PLAIN, 16));

		JButton addButton = new JButton("Add");
		addButton.setName("addButton");

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				table.clearSelection();

				if (DBHandler.getFaculties().length == 0) {
					JOptionPane.showMessageDialog(managementFrame, "Cannot add student", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (nameField.getText().equals("") || surnameField.getText().equals("")
						|| ageField.getText().equals("") || startedDateField.getText().equals("")) {

					JOptionPane.showMessageDialog(managementFrame, "Fill empty fields", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						format.setLenient(false);
						format.parse(startedDateField.getText());
					} catch (ParseException ex) {
						ex.printStackTrace();

						JOptionPane.showMessageDialog(managementFrame, "Date format error", "Error",
								JOptionPane.ERROR_MESSAGE);

						return;
					}

					if (DBHandler.addStudent()) {
						JOptionPane.showMessageDialog(managementFrame, "Student successfully added", "Success",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(managementFrame, "Something went wrong", "Error",
								JOptionPane.ERROR_MESSAGE);
					}

				}
			}
		});
		buttonsPanel.setLayout(new GridLayout(0, 5, 0, 0));

		addButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		buttonsPanel.add(addButton);

		JButton updateButton = new JButton("Update");

		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				table.clearSelection();
				DBHandler.updateStudents();
			}
		});

		updateButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		buttonsPanel.add(updateButton);
		buttonsPanel.add(deleteButton);

		JButton exitButton = new JButton("Exit");
		exitButton.setName("exitButton");

		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(managementFrame, "Are you sure you want to exit?", "SMS",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					managementFrame.dispose();
					System.exit(0);
				}
			}
		});

		JButton disconnectButton = new JButton("Disconnect");
		disconnectButton.setName("disconnectButton");

		disconnectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(managementFrame, "Are you sure you want to disconnect?", "SMS",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    managementFrame.dispose(); // Close the current frame
                    new Homepage(); // Instantiate and display the Homepage frame
                }
            }
        });

		disconnectButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		buttonsPanel.add(disconnectButton);

		exitButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		buttonsPanel.add(exitButton);

		JPanel studentPanel = new JPanel();
		studentPanel.setBorder(new LineBorder(SystemColor.textHighlight, 5));
		studentPanel.setBounds(10, 10, 240, 395);
		managementFrame.getContentPane().add(studentPanel);
		studentPanel.setLayout(null);

		JLabel nameText = new JLabel("Name");
		nameText.setFont(new Font("Tahoma", Font.PLAIN, 16));
		nameText.setBounds(10, 22, 67, 19);
		studentPanel.add(nameText);

		nameField = new JTextField();
		nameField.setName("nameField");
		nameField.setBounds(85, 23, 143, 22);
		studentPanel.add(nameField);
		nameField.setColumns(10);

		JLabel surnameText = new JLabel("Surname");
		surnameText.setFont(new Font("Tahoma", Font.PLAIN, 16));
		surnameText.setBounds(10, 54, 67, 19);
		studentPanel.add(surnameText);

		surnameField = new JTextField();
		surnameField.setName("surnameField");
		surnameField.setColumns(10);
		surnameField.setBounds(85, 51, 143, 22);
		studentPanel.add(surnameField);

		JLabel ageText = new JLabel("Age");
		ageText.setFont(new Font("Tahoma", Font.PLAIN, 16));
		ageText.setBounds(10, 86, 67, 19);
		studentPanel.add(ageText);

		ageField = new JTextField();
		ageField.setName("ageField");
		ageField.setColumns(10);
		ageField.setBounds(85, 83, 143, 22);
		studentPanel.add(ageField);

		JLabel courseText = new JLabel("Course");
		courseText.setFont(new Font("Tahoma", Font.PLAIN, 16));
		courseText.setBounds(10, 156, 67, 19);
		studentPanel.add(courseText);

		JLabel startedDateText = new JLabel("Started");
		startedDateText.setFont(new Font("Tahoma", Font.PLAIN, 16));
		startedDateText.setBounds(10, 188, 67, 19);
		studentPanel.add(startedDateText);

		startedDateField = new JTextField();
		startedDateField.setName("startedDateField");
		startedDateField.setColumns(10);
		startedDateField.setBounds(85, 185, 143, 22);
		startedDateField.setText("yyyy-MM-dd");
		studentPanel.add(startedDateField);

		JLabel genderText = new JLabel("Gender");
		genderText.setFont(new Font("Tahoma", Font.PLAIN, 16));
		genderText.setBounds(10, 120, 67, 19);
		studentPanel.add(genderText);

		genderSelectionBox = new JComboBox();
		genderSelectionBox.setName("genderSelectionBox");
		genderSelectionBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		genderSelectionBox.setModel(new DefaultComboBoxModel(Gender.values()));
		genderSelectionBox.setBounds(85, 120, 143, 22);
		studentPanel.add(genderSelectionBox);

		JButton addFacultyButton = new JButton("Add Faculty");
		addFacultyButton.setName("addFacultyButton");

		addFacultyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String facultyName = "";

				facultyName = JOptionPane.showInputDialog(managementFrame, "Type name of faculty");

				if (facultyName == null || facultyName.equals("")) {
					JOptionPane.showMessageDialog(managementFrame, "Empty name", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					if (DBHandler.checkIfElementExists(DBHandler.getFacultiesTable(), facultyName)) {
						JOptionPane.showMessageDialog(managementFrame, "Faculty already exists", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						if (DBHandler.addFaculty(facultyName)) {
							JOptionPane.showMessageDialog(managementFrame, "Faculty successfully added", "Success",
									JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(managementFrame, "Faculty not added", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		});

		addFacultyButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		addFacultyButton.setBounds(10, 220, 220, 30);
		studentPanel.add(addFacultyButton);

		JButton addCourseButton = new JButton("Add Course");
		addCourseButton.setName("addCourseButton");
		addCourseButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (DBHandler.getFaculties().length == 0) {
					JOptionPane.showMessageDialog(managementFrame, "Cannot add course", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				String courseName = "", faculty = "";
				int duration = 0;

				courseName = JOptionPane.showInputDialog(managementFrame, "Type name of course");

				if (courseName == null || courseName.equals("")) {
					JOptionPane.showMessageDialog(managementFrame, "Empty name", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					String[] faculties = DBHandler.getFaculties();
					faculty = (String) JOptionPane.showInputDialog(null, "Choose faculty", "SMS",
							JOptionPane.QUESTION_MESSAGE, null, faculties, faculties[0]);

					if (faculty == null || faculty.equals("")) {
						JOptionPane.showMessageDialog(managementFrame, "Course not added: no faculty", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					} else {
						try {
							duration = Integer.parseInt(JOptionPane.showInputDialog(managementFrame, "Type duration"));
						} catch (NumberFormatException ex) {
							ex.printStackTrace();

							JOptionPane.showMessageDialog(managementFrame, "Course not added: no duration", "Error",
									JOptionPane.ERROR_MESSAGE);

							return;
						}

						if (DBHandler.checkIfElementExists(DBHandler.getCoursesTable(), courseName)) {
							JOptionPane.showMessageDialog(managementFrame, "Course already exists", "Error",
									JOptionPane.ERROR_MESSAGE);
						} else {
							if (DBHandler.addCourse(courseName, faculty, duration)) {
								JOptionPane.showMessageDialog(managementFrame, "Course successfully added", "Success",
										JOptionPane.INFORMATION_MESSAGE);

								updateCourses();
							} else {
								JOptionPane.showMessageDialog(managementFrame, "Course not added", "Error",
										JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				}
			}
		});

		addCourseButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		addCourseButton.setBounds(10, 260, 220, 30);
		studentPanel.add(addCourseButton);

		courseSelectionBox = new JComboBox();
		courseSelectionBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		courseSelectionBox.setBounds(85, 154, 143, 22);
		updateCourses();
		studentPanel.add(courseSelectionBox);

		JButton deleteFacultyButton = new JButton("Delete Faculty");
		deleteFacultyButton.setName("deleteFacultyButton");

		deleteFacultyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				table.clearSelection();

				String faculty = (String) JOptionPane.showInputDialog(null, "Choose faculty", "Delete Faculty",
						JOptionPane.QUESTION_MESSAGE, null, DBHandler.getFaculties(), DBHandler.getFaculties()[0]);

				if (faculty == null) {
					return;
				}

				if (DBHandler.getNumberOfCourses(faculty) > 0) {
					if (JOptionPane.showConfirmDialog(managementFrame, "Delete faculty with courses?", "SMS",
							JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						if (DBHandler.deleteFacultyCourses(faculty)) {
							JOptionPane.showMessageDialog(managementFrame, "Courses from faculty successfully deleted",
									"Success", JOptionPane.INFORMATION_MESSAGE);

							if (DBHandler.deleteFaculty(faculty)) {
								JOptionPane.showMessageDialog(managementFrame, "Faculty deleted", "Success",
										JOptionPane.INFORMATION_MESSAGE);
							} else {
								JOptionPane.showMessageDialog(managementFrame, "Something went wrong", "Error",
										JOptionPane.ERROR_MESSAGE);
							}

						} else {
							JOptionPane.showMessageDialog(managementFrame, "Something went wrong", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				} else {
					if (DBHandler.deleteFaculty(faculty)) {
						JOptionPane.showMessageDialog(managementFrame, "Faculty deleted", "Success",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(managementFrame, "Something went wrong", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
				updateCourses();
			}
		});

		deleteFacultyButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		deleteFacultyButton.setBounds(10, 300, 220, 30);
		studentPanel.add(deleteFacultyButton);

		JButton deleteCourseButton = new JButton("Delete Course");
		deleteCourseButton.setName("deleteCourseButton");

		deleteCourseButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				table.clearSelection();

				String course = (String) JOptionPane.showInputDialog(null, "Choose course", "Delete Course",
						JOptionPane.QUESTION_MESSAGE, null, DBHandler.getCourses(), DBHandler.getCourses()[0]);

				if (course == null) {
					return;
				}

				if (DBHandler.getNumberOfAttendees(DBHandler.getCoursesTable(), course) > 0) {
					if (JOptionPane.showConfirmDialog(managementFrame, "Delete course with students?", "Delete Course",
							JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						if (DBHandler.deleteCourseAttendees(course)) {
							JOptionPane.showMessageDialog(managementFrame, "Students attending successfully deleted",
									"Success", JOptionPane.INFORMATION_MESSAGE);

							if (DBHandler.deleteCourse(course)) {
								JOptionPane.showMessageDialog(managementFrame, "Course deleted", "Success",
										JOptionPane.INFORMATION_MESSAGE);
							} else {
								JOptionPane.showMessageDialog(managementFrame, "Something went wrong", "Error",
										JOptionPane.ERROR_MESSAGE);
							}
						} else {
							JOptionPane.showMessageDialog(managementFrame, "Something went wrong", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				} else {
					if (DBHandler.deleteCourse(course)) {
						JOptionPane.showMessageDialog(managementFrame, "Course deleted", "Success",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(managementFrame, "Something went wrong", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
				updateCourses();
			}
		});

		deleteCourseButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		deleteCourseButton.setBounds(10, 340, 220, 30);
		studentPanel.add(deleteCourseButton);
	}
}
