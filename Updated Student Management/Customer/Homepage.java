package Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Homepage extends JFrame implements ActionListener {
    JButton register, login;

    Homepage() {
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/Register.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1358, 700, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(0, 0, 1358, 700);
        add(l3);

        register = new JButton("REGISTER");
        register.setBounds(100, 200, 150, 35);
        register.setBackground(new Color(255, 255, 255));
        register.setForeground(Color.BLACK);
        register.addActionListener(this);
        l3.add(register);

        login= new JButton("MY ACCOUNT");
        login.setBounds(570, 210, 160, 35);
        login.setBackground(new Color(255, 255, 255));
        login.setForeground(Color.BLACK);
        login.addActionListener(this);
        l3.add(login);

        setLayout(null);
        setSize(1550, 1080);
        setLocation(0, 0);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == register) {
            new Registration();
            this.dispose();
        } else if (e.getSource() == login) {
            int dialogResult = JOptionPane.showConfirmDialog(this, "Do you have an account?", "Confirmation", JOptionPane.YES_NO_OPTION);

            if (dialogResult == JOptionPane.YES_OPTION) {
                new Login();
                this.dispose();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Homepage::new);
    }
}