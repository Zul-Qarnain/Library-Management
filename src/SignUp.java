import javax.swing.*; // Remove the extra '.'
import java.awt.*;    // Remove the extra '.'
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
public class SignUp {
    public static void main(String[] args) {
        // Use FlatLaf look and feel
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to initialize LaF");
        }

        JFrame frame = new JFrame("FlatLaf Sign Up");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.decode("#212121"));

        // Welcome message
        JLabel welcomeLabel = new JLabel("Create an account");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        GridBagConstraints welcomeConstraints = new GridBagConstraints();
        welcomeConstraints.gridx = 0;
        welcomeConstraints.gridy = 0;
        welcomeConstraints.gridwidth = 2;
        welcomeConstraints.insets = new Insets(20, 0, 20, 0);
        panel.add(welcomeLabel, welcomeConstraints);

        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setForeground(Color.WHITE);
        GridBagConstraints usernameLabelConstraints = new GridBagConstraints();
        usernameLabelConstraints.gridx = 0;
        usernameLabelConstraints.gridy = 1;
        usernameLabelConstraints.anchor = GridBagConstraints.WEST;
        usernameLabelConstraints.insets = new Insets(5, 0, 5, 0);
        panel.add(usernameLabel, usernameLabelConstraints);

        JTextField usernameField = new JTextField(20);
        usernameField.putClientProperty("JTextField.placeholderText", "Enter your username");
        GridBagConstraints usernameFieldConstraints = new GridBagConstraints();
        usernameFieldConstraints.gridx = 0;
        usernameFieldConstraints.gridy = 2;
        usernameFieldConstraints.gridwidth = 2;
        usernameFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        usernameFieldConstraints.insets = new Insets(0, 0, 15, 0);
        panel.add(usernameField, usernameFieldConstraints);

        // Email field
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setForeground(Color.WHITE);
        GridBagConstraints emailLabelConstraints = new GridBagConstraints();
        emailLabelConstraints.gridx = 0;
        emailLabelConstraints.gridy = 3;
        emailLabelConstraints.anchor = GridBagConstraints.WEST;
        emailLabelConstraints.insets = new Insets(5, 0, 5, 0);
        panel.add(emailLabel, emailLabelConstraints);

        JTextField emailField = new JTextField(20);
        emailField.putClientProperty("JTextField.placeholderText", "Enter your email");
        GridBagConstraints emailFieldConstraints = new GridBagConstraints();
        emailFieldConstraints.gridx = 0;
        emailFieldConstraints.gridy = 4;
        emailFieldConstraints.gridwidth = 2;
        emailFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        emailFieldConstraints.insets = new Insets(0, 0, 15, 0);
        panel.add(emailField, emailFieldConstraints);

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(Color.WHITE);
        GridBagConstraints passwordLabelConstraints = new GridBagConstraints();
        passwordLabelConstraints.gridx = 0;
        passwordLabelConstraints.gridy = 5;
        passwordLabelConstraints.anchor = GridBagConstraints.WEST;
        passwordLabelConstraints.insets = new Insets(5, 0, 5, 0);
        panel.add(passwordLabel, passwordLabelConstraints);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.putClientProperty("JPasswordField.placeholderText", "Enter your password");
        GridBagConstraints passwordFieldConstraints = new GridBagConstraints();
        passwordFieldConstraints.gridx = 0;
        passwordFieldConstraints.gridy = 6;
        passwordFieldConstraints.gridwidth = 2;
        passwordFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        passwordFieldConstraints.insets = new Insets(0, 0, 15, 0);
        panel.add(passwordField, passwordFieldConstraints);

        // Confirm password field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        confirmPasswordLabel.setForeground(Color.WHITE);
        GridBagConstraints confirmPasswordLabelConstraints = new GridBagConstraints();
        confirmPasswordLabelConstraints.gridx = 0;
        confirmPasswordLabelConstraints.gridy = 7;
        confirmPasswordLabelConstraints.anchor = GridBagConstraints.WEST;
        confirmPasswordLabelConstraints.insets = new Insets(5, 0, 5, 0);
        panel.add(confirmPasswordLabel, confirmPasswordLabelConstraints);

        JPasswordField confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.putClientProperty("JPasswordField.placeholderText", "Confirm your password");
        GridBagConstraints confirmPasswordFieldConstraints = new GridBagConstraints();
        confirmPasswordFieldConstraints.gridx = 0;
        confirmPasswordFieldConstraints.gridy = 8;
        confirmPasswordFieldConstraints.gridwidth = 2;
        confirmPasswordFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        confirmPasswordFieldConstraints.insets = new Insets(0, 0, 15, 0);
        panel.add(confirmPasswordField, confirmPasswordFieldConstraints);

        // Signup button
        JButton signupButton = new JButton("Sign Up");
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle signup logic here
                String username = usernameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                // ... (Validate and process signup data)
            }
        });
        GridBagConstraints signupButtonConstraints = new GridBagConstraints();
        signupButtonConstraints.gridx = 0;
        signupButtonConstraints.gridy = 9;
        signupButtonConstraints.gridwidth = 2;
        signupButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        signupButtonConstraints.insets = new Insets(10, 0, 10, 0);
        panel.add(signupButton, signupButtonConstraints);

        // Login link
        JLabel loginLabel = new JLabel("Already have an account? ");
        loginLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        loginLabel.setForeground(Color.LIGHT_GRAY);
        GridBagConstraints loginLabelConstraints = new GridBagConstraints();
        loginLabelConstraints.gridx = 0;
        loginLabelConstraints.gridy = 10;
        loginLabelConstraints.anchor = GridBagConstraints.EAST;
        panel.add(loginLabel, loginLabelConstraints);

        JLabel loginLink = new JLabel("<html><a href='#'>Log in</a></html>");
        loginLink.setFont(new Font("Arial", Font.PLAIN, 14));
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLink.setForeground(Color.decode("#5865F2")); // Discord blue
        loginLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Switch back to the login page
                frame.dispose(); // Close the signup frame
                FlatLafLogin.main(new String[]{}); // Open the login frame
            }
        });
        GridBagConstraints loginLinkConstraints = new GridBagConstraints();
        loginLinkConstraints.gridx = 1;
        loginLinkConstraints.gridy = 10;
        loginLinkConstraints.anchor = GridBagConstraints.WEST;
        panel.add(loginLink, loginLinkConstraints);

        frame.add(panel);
        frame.setVisible(true);
    }}