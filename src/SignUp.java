import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SignUp {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Create an account");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        JLabel welcomeLabel = new JLabel("Create an account");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        addComponent(panel, welcomeLabel, 0, 0, 2, 1, GridBagConstraints.CENTER);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        addComponent(panel, usernameLabel, 0, 1, 1, 1, GridBagConstraints.WEST);

        JTextField usernameField = new JTextField(20);
        addComponent(panel, usernameField, 0, 2, 2, 1, GridBagConstraints.WEST);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        addComponent(panel, emailLabel, 0, 3, 1, 1, GridBagConstraints.WEST);

        JTextField emailField = new JTextField(20);
        addComponent(panel, emailField, 0, 4, 2, 1, GridBagConstraints.WEST);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        addComponent(panel, passwordLabel, 0, 5, 1, 1, GridBagConstraints.WEST);

        JPasswordField passwordField = new JPasswordField(20);
        addComponent(panel, passwordField, 0, 6, 2, 1, GridBagConstraints.WEST);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        addComponent(panel, confirmPasswordLabel, 0, 7, 1, 1, GridBagConstraints.WEST);

        JPasswordField confirmPasswordField = new JPasswordField(20);
        addComponent(panel, confirmPasswordField, 0, 8, 2, 1, GridBagConstraints.WEST);

        JButton signupButton = new JButton("Sign Up");
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(frame, "Passwords do not match!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all fields!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // In a real application, hash the password securely before storing!
                // For this example, we are simply writing it to the file.

                try (BufferedWriter writer = new BufferedWriter(new FileWriter("admininfo.txt", true))) {
                    writer.write(username + "," + password);
                    writer.newLine();
                    JOptionPane.showMessageDialog(frame, "Signup successful!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                    Login.main(new String[]{});
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error during signup: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        addComponent(panel, signupButton, 0, 9, 2, 1, GridBagConstraints.CENTER);

        JLabel loginLabel = new JLabel("Already have an account? ");
        loginLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        addComponent(panel, loginLabel, 0, 10, 1, 1, GridBagConstraints.EAST);

        JLabel loginLink = new JLabel("<html><a href='#'>Log in</a></html>");
        loginLink.setFont(new Font("Arial", Font.PLAIN, 14));
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLink.setForeground(Color.BLUE);
        loginLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                frame.dispose();
                Login.main(new String[]{});
            }
        });
        addComponent(panel, loginLink, 1, 10, 1, 1, GridBagConstraints.WEST);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void addComponent(JPanel panel, JComponent component, int gridx, int gridy,
                                     int gridwidth, int gridheight, int anchor) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = gridwidth;
        constraints.gridheight = gridheight;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 20, 5, 20);
        constraints.anchor = anchor;
        panel.add(component, constraints);
    }
}
