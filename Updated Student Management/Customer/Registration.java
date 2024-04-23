package Customer;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends JFrame implements ActionListener {
    JButton submit, back, eye, eye1, eye3, eye4, eye5, eye6;
    JTextField textName, textEmail;
    JPasswordField textPassword, textConfirmPassword, textPin;

    Registration() {
        JLabel labelName = new JLabel("Name:");
        labelName.setFont(new Font("Raleway", Font.BOLD, 14));
        labelName.setBounds(400, 160, 100, 30);
        add(labelName);

        textName = new JTextField();
        textName.setFont(new Font("Raleway", Font.PLAIN, 14));
        textName.setBounds(500, 160, 500, 30);
        add(textName);

        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setFont(new Font("Raleway", Font.BOLD, 14));
        labelEmail.setBounds(400, 210, 100, 30);
        add(labelEmail);

        textEmail = new JTextField();
        textEmail.setFont(new Font("Raleway", Font.PLAIN, 14));
        textEmail.setBounds(500, 210, 400, 30);
        add(textEmail);

        JLabel labelPassword = new JLabel("Password:");
        labelPassword.setFont(new Font("Raleway", Font.BOLD, 14));
        labelPassword.setBounds(400, 260, 100, 30);
        add(labelPassword);

        textPassword = new JPasswordField();
        textPassword.setFont(new Font("Raleway", Font.PLAIN, 14));
        textPassword.setBounds(500, 260, 400, 30);
        add(textPassword);

        ImageIcon buttonIcon = new ImageIcon(ClassLoader.getSystemResource("icon/itachi.png"));
        eye = new JButton(buttonIcon);
        eye.setText(null);
        eye.setBackground(Color.lightGray);
        eye.setForeground(Color.black);
        eye.setBounds(865, 255, 113, 50);
        eye.addActionListener(this);
        eye.setOpaque(false);
        eye.setContentAreaFilled(false);
        eye.setBorderPainted(false);
        add(eye);

        ImageIcon buttonIcon1 = new ImageIcon(ClassLoader.getSystemResource("icon/itachi1.png"));
        eye1 = new JButton(buttonIcon1);
        eye1.setText(null);
        eye1.setBackground(Color.lightGray);
        eye1.setForeground(Color.black);
        eye1.setBounds(865, 260, 113, 50);
        eye1.addActionListener(this);
        eye1.setOpaque(false);
        eye1.setContentAreaFilled(false);
        eye1.setBorderPainted(false);
        add(eye1);
        eye1.setVisible(false);

        JLabel labelConfirmPassword = new JLabel("Confirm Password:");
        labelConfirmPassword.setFont(new Font("Raleway", Font.BOLD, 14));
        labelConfirmPassword.setBounds(400, 310, 150, 30);
        add(labelConfirmPassword);

        textConfirmPassword = new JPasswordField();
        textConfirmPassword.setFont(new Font("Raleway", Font.PLAIN, 14));
        textConfirmPassword.setBounds(550, 310, 350, 30);
        add(textConfirmPassword);

        JLabel labelPin = new JLabel("PIN:");
        labelPin.setFont(new Font("Raleway", Font.BOLD, 14));
        labelPin.setBounds(400, 360, 100, 30);
        add(labelPin);

        textPin = new JPasswordField();
        textPin.setFont(new Font("Raleway", Font.PLAIN, 14));
        textPin.setBounds(500, 360, 100, 30);
        add(textPin);


        ((AbstractDocument) textPin.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length()) <= 4) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() + text.length() - length) <= 4) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        ImageIcon buttonIcon3 = new ImageIcon(ClassLoader.getSystemResource("icon/itachi.png"));
        eye3 = new JButton(buttonIcon3);
        eye3.setText(null);
        eye3.setBackground(Color.lightGray);
        eye3.setForeground(Color.black);
        eye3.setBounds(570, 355, 113, 50);
        eye3.addActionListener(this);
        eye3.setOpaque(false);
        eye3.setContentAreaFilled(false);
        eye3.setBorderPainted(false);
        add(eye3);

        ImageIcon buttonIcon4 = new ImageIcon(ClassLoader.getSystemResource("icon/itachi1.png"));
        eye4 = new JButton(buttonIcon4);
        eye4.setText(null);
        eye4.setBackground(Color.lightGray);
        eye4.setForeground(Color.black);
        eye4.setBounds(570, 360, 113, 50);
        eye4.addActionListener(this);
        eye4.setOpaque(false);
        eye4.setContentAreaFilled(false);
        eye4.setBorderPainted(false);
        add(eye4);
        eye4.setVisible(false);

        ImageIcon buttonIcon5 = new ImageIcon(ClassLoader.getSystemResource("icon/itachi.png"));
        eye5 = new JButton(buttonIcon5);
        eye5.setText(null);
        eye5.setBackground(Color.lightGray);
        eye5.setForeground(Color.black);
        eye5.setBounds(870, 300, 113, 50);
        eye5.addActionListener(this);
        eye5.setOpaque(false);
        eye5.setContentAreaFilled(false);
        eye5.setBorderPainted(false);
        add(eye5);

        ImageIcon buttonIcon6 = new ImageIcon(ClassLoader.getSystemResource("icon/itachi1.png"));
        eye6 = new JButton(buttonIcon6);
        eye6.setText(null);
        eye6.setBackground(Color.lightGray);
        eye6.setForeground(Color.black);
        eye6.setBounds(905, 310, 113, 50);
        eye6.addActionListener(this);
        eye6.setOpaque(false);
        eye6.setContentAreaFilled(false);
        eye6.setBorderPainted(false);
        add(eye6);
        eye6.setVisible(false);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/typing.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1400, 700, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(0, 0, 1400, 700);
        add(l3);

        submit = new JButton("SUBMIT");
        submit.setFont(new Font("Raleway", Font.BOLD, 14));
        submit.setBackground(Color.white);
        submit.setForeground(Color.black);
        submit.setBounds(800, 356, 100, 40);
        submit.addActionListener(this);
        l3.add(submit);

        back = new JButton("BACK");
        back.setFont(new Font("Raleway", Font.BOLD, 14));
        back.setBackground(Color.white);
        back.setForeground(Color.black);
        back.setBounds(905, 356, 100, 40);
        back.addActionListener(this);
        l3.add(back);

        getContentPane().setBackground(new Color(135, 206, 250));
        setLayout(null);
        setSize(1550, 1080);
        setLocation(0, 0);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submit) {
            if (validateInput()) {
                saveToDatabase();
                JOptionPane.showMessageDialog(this, "REGISTRATION SUCCESSFUL", "Success", JOptionPane.INFORMATION_MESSAGE);

            }
        } else if (e.getSource() == back) {
            new Homepage();
            this.dispose();
        } else if (e.getSource() == eye || e.getSource() == eye1) {
            togglePasswordVisibility(textPassword, eye, eye1);
        } else if (e.getSource() == eye3 || e.getSource() == eye4) {
            togglePasswordVisibility(textPin, eye3, eye4);
        } else if (e.getSource() == eye5 || e.getSource() == eye6) {
            togglePasswordVisibility(textConfirmPassword, eye5, eye6);
        }
    }

    private void togglePasswordVisibility(JPasswordField passwordField, JButton eyeButton, JButton eye1Button) {
        if (passwordField.getEchoChar() == '•') {
            passwordField.setEchoChar((char) 0);
            eyeButton.setVisible(false);
            eye1Button.setVisible(true);
        } else {
            passwordField.setEchoChar('•');
            eyeButton.setVisible(true);
            eye1Button.setVisible(false);
        }
    }

    private boolean validateInput() {

        String name = textName.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!isAlphabetic(name)) {
            JOptionPane.showMessageDialog(this, "Name should contain only letters.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }


        String email = textEmail.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email format.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        char[] password = textPassword.getPassword();
        char[] confirmPassword = textConfirmPassword.getPassword();
        if (password.length == 0 || confirmPassword.length == 0) {
            JOptionPane.showMessageDialog(this, "Password fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!isStrongPassword(password)) {
            JOptionPane.showMessageDialog(this, "Weak password. Make it stronger.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!Arrays.equals(password, confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }


        char[] pin = textPin.getPassword();
        if (pin.length != 4) {
            JOptionPane.showMessageDialog(this, "PIN must be 4 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!isNumeric(pin)) {
            JOptionPane.showMessageDialog(this, "PIN must contain only numeric characters.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }


        return true;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isStrongPassword(char[] password) {
        String passwordString = new String(password);


        if (passwordString.replaceAll("[^A-Z]", "").length() != 1) {
            JOptionPane.showMessageDialog(this, "Password should contain exactly 1 capital letter.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (passwordString.replaceAll("[^a-z]", "").length() < 5) {
            JOptionPane.showMessageDialog(this, "Password should contain at least 5 small letters.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }


        if (!passwordString.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            JOptionPane.showMessageDialog(this, "Password should contain at least 1 special character.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }


        if (passwordString.replaceAll("[^0-9]", "").length() < 1) {
            JOptionPane.showMessageDialog(this, "Password should contain at least 1 number.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }


        if (password.length < 9) {
            JOptionPane.showMessageDialog(this, "Password should be at least 9 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }


        return true;
    }

    private boolean isAlphabetic(String input) {
        return input.matches("[a-zA-Z ]+");
    }

    private boolean isNumeric(char[] input) {
        for (char c : input) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private void saveToDatabase() {
        String databaseUrl = "jdbc:mysql://localhost:3306/Authentication";
        String login = "root";
        String password = "qwerty123";

        String sql = "INSERT INTO users (name, email, password, pin) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(databaseUrl, login, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, textName.getText().trim());
            preparedStatement.setString(2, textEmail.getText().trim());
            preparedStatement.setString(3, new String(textPassword.getPassword()));
            preparedStatement.setString(4, new String(textPin.getPassword()));

            preparedStatement.executeUpdate();

            openHomepage();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving to database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openHomepage() {
        new Homepage();
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Registration::new);
    }
}
