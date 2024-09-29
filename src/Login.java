import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Login {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Admin Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        JLabel welcomeLabel = new JLabel("Welcome back!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        addComponent(panel, welcomeLabel, 0, 0, 2, 1, GridBagConstraints.CENTER);

        JLabel subLabel = new JLabel("Please sign in to access your account");
        subLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        addComponent(panel, subLabel, 0, 1, 2, 1, GridBagConstraints.CENTER);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        addComponent(panel, usernameLabel, 0, 2, 1, 1, GridBagConstraints.WEST);

        JTextField usernameField = new JTextField(20);
        usernameField.putClientProperty("JTextField.placeholderText", "Enter your username or email");
        addComponent(panel, usernameField, 0, 3, 2, 1, GridBagConstraints.WEST);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        addComponent(panel, passwordLabel, 0, 4, 1, 1, GridBagConstraints.WEST);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.putClientProperty("JPasswordField.placeholderText", "Enter your password");
        addComponent(panel, passwordField, 0, 5, 2, 1, GridBagConstraints.WEST);

        JCheckBox rememberMeCheckbox = new JCheckBox("Remember me");
        rememberMeCheckbox.setFont(new Font("Arial", Font.PLAIN, 14));
        rememberMeCheckbox.setOpaque(false);
        addComponent(panel, rememberMeCheckbox, 0, 6, 1, 1, GridBagConstraints.WEST);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (authenticateAdmin(username, password)) {
                    JOptionPane.showMessageDialog(frame, "Login successful!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                    Admin.main(new String[]{});
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        addComponent(panel, loginButton, 0, 7, 2, 1, GridBagConstraints.CENTER);

        // Signup Link
        JLabel signupLabel = new JLabel("Don't have an account? ");
        signupLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        addComponent(panel, signupLabel, 0, 8, 1, 1, GridBagConstraints.EAST);

        JLabel signupLink = new JLabel("<html><a href='#'>Sign up</a></html>");
        signupLink.setFont(new Font("Arial", Font.PLAIN, 14));
        signupLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signupLink.setForeground(Color.BLUE);
        signupLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                frame.dispose();
                SignUp.main(new String[]{});
            }
        });
        addComponent(panel, signupLink, 1, 8, 1, 1, GridBagConstraints.WEST);

        frame.add(panel);
        frame.setVisible(true);
    }

    // Helper function (unchanged)
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

    // Admin authentication (IMPORTANT: Insecure, for demo only!)
    private static boolean authenticateAdmin(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("admininfo.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length == 2 &&
                        credentials[0].equals(username) &&
                        credentials[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading admin file: " + e.getMessage());
        }
        return false;
    }
}
