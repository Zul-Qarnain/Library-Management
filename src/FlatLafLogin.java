import javax.swing.*; // Correct
import java.awt.*;    // Correct
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
public class FlatLafLogin {
    public static void main(String[] args) {
        // Use FlatLaf look and feel
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to initialize LaF");
        }

        JFrame frame = new JFrame("FlatLaf Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null); // Center the frame

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.decode("#212121")); // Dark background

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome back!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        GridBagConstraints welcomeConstraints = new GridBagConstraints();
        welcomeConstraints.gridx = 0;
        welcomeConstraints.gridy = 0;
        welcomeConstraints.gridwidth = 2;
        welcomeConstraints.insets = new Insets(20, 0, 20, 0);
        panel.add(welcomeLabel, welcomeConstraints);

        JLabel subLabel = new JLabel("Please sign in to access your account");
        subLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subLabel.setForeground(Color.LIGHT_GRAY);
        GridBagConstraints subConstraints = new GridBagConstraints();
        subConstraints.gridx = 0;
        subConstraints.gridy = 1;
        subConstraints.gridwidth = 2;
        subConstraints.insets = new Insets(0, 0, 20, 0);
        panel.add(subLabel, subConstraints);

        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setForeground(Color.WHITE);
        GridBagConstraints usernameLabelConstraints = new GridBagConstraints();
        usernameLabelConstraints.gridx = 0;
        usernameLabelConstraints.gridy = 2;
        usernameLabelConstraints.anchor = GridBagConstraints.WEST;
        usernameLabelConstraints.insets = new Insets(5, 0, 5, 0);
        panel.add(usernameLabel, usernameLabelConstraints);

        JTextField usernameField = new JTextField(20);
        usernameField.putClientProperty("JTextField.placeholderText", "Enter your username or email");
        GridBagConstraints usernameFieldConstraints = new GridBagConstraints();
        usernameFieldConstraints.gridx = 0;
        usernameFieldConstraints.gridy = 3;
        usernameFieldConstraints.gridwidth = 2;
        usernameFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        usernameFieldConstraints.insets = new Insets(0, 0, 15, 0);
        panel.add(usernameField, usernameFieldConstraints);

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(Color.WHITE);
        GridBagConstraints passwordLabelConstraints = new GridBagConstraints();
        passwordLabelConstraints.gridx = 0;
        passwordLabelConstraints.gridy = 4;
        passwordLabelConstraints.anchor = GridBagConstraints.WEST;
        passwordLabelConstraints.insets = new Insets(5, 0, 5, 0);
        panel.add(passwordLabel, passwordLabelConstraints);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.putClientProperty("JPasswordField.placeholderText", "Enter your password");
        GridBagConstraints passwordFieldConstraints = new GridBagConstraints();
        passwordFieldConstraints.gridx = 0;
        passwordFieldConstraints.gridy = 5;
        passwordFieldConstraints.gridwidth = 2;
        passwordFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        passwordFieldConstraints.insets = new Insets(0, 0, 15, 0);
        panel.add(passwordField, passwordFieldConstraints);

        // Remember me checkbox
        JCheckBox rememberMeCheckbox = new JCheckBox("Remember me");
        rememberMeCheckbox.setFont(new Font("Arial", Font.PLAIN, 14));
        rememberMeCheckbox.setForeground(Color.WHITE);
        rememberMeCheckbox.setOpaque(false); // Make checkbox transparent
        GridBagConstraints rememberMeConstraints = new GridBagConstraints();
        rememberMeConstraints.gridx = 0;
        rememberMeConstraints.gridy = 6;
        rememberMeConstraints.anchor = GridBagConstraints.WEST;
        panel.add(rememberMeCheckbox, rememberMeConstraints);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle login logic here
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                System.out.println("Username: " + username);
                System.out.println("Password: " + password);
            }
        });
        GridBagConstraints loginButtonConstraints = new GridBagConstraints();
        loginButtonConstraints.gridx = 0;
        loginButtonConstraints.gridy = 7;
        loginButtonConstraints.gridwidth = 2;
        loginButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        loginButtonConstraints.insets = new Insets(10, 0, 10, 0);
        panel.add(loginButton, loginButtonConstraints);

        // Signup link
        JLabel signupLabel = new JLabel("Don't have an account? ");
        signupLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        signupLabel.setForeground(Color.LIGHT_GRAY);
        GridBagConstraints signupLabelConstraints = new GridBagConstraints();
        signupLabelConstraints.gridx = 0;
        signupLabelConstraints.gridy = 8;
        signupLabelConstraints.anchor = GridBagConstraints.EAST;
        panel.add(signupLabel, signupLabelConstraints);

        JLabel signupLink = new JLabel("<html><a href='#'>Sign up</a></html>");
        signupLink.setFont(new Font("Arial", Font.PLAIN, 14));
        signupLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signupLink.setForeground(Color.decode("#5865F2")); // Discord blue
        signupLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Open the signup page
                frame.dispose(); // Close the login frame
                SignUp.main(new String[]{}); // Open the signup frame
            }
        });
        GridBagConstraints signupLinkConstraints = new GridBagConstraints();
        signupLinkConstraints.gridx = 1;
        signupLinkConstraints.gridy = 8;
        signupLinkConstraints.anchor = GridBagConstraints.WEST;
        panel.add(signupLink, signupLinkConstraints);

        // Add panel to frame
        frame.add(panel);
        frame.setVisible(true);
    }}