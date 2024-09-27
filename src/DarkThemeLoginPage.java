import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DarkThemeLoginPage {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to initialize LaF");
        }

        JFrame frame = new JFrame("Dark Theme Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.decode("#212121"));

        JLabel titleLabel = new JLabel("Welcome To");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        GridBagConstraints titleConstraints = new GridBagConstraints();
        titleConstraints.gridx = 0;
        titleConstraints.gridy = 0;
        titleConstraints.insets = new Insets(20, 0, 5, 0);
        panel.add(titleLabel, titleConstraints);

        JLabel subtitleLabel = new JLabel("JPass.");
        subtitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        subtitleLabel.setForeground(Color.WHITE);
        GridBagConstraints subtitleConstraints = new GridBagConstraints();
        subtitleConstraints.gridx = 0;
        subtitleConstraints.gridy = 1;
        subtitleConstraints.insets = new Insets(0, 0, 40, 0);
        panel.add(subtitleLabel, subtitleConstraints);

        JButton signupButton = new JButton("Sign Up");
        signupButton.setBackground(Color.decode("#F24E1E")); // Orange color
        signupButton.setForeground(Color.WHITE);
        signupButton.setFont(new Font("Arial", Font.BOLD, 16));
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle signup button click here
                frame.dispose(); // Close the login frame
                SignUp.main(new String[]{});
                System.out.println("Sign up button clicked");
            }
        });
        GridBagConstraints signupButtonConstraints = new GridBagConstraints();
        signupButtonConstraints.gridx = 0;
        signupButtonConstraints.gridy = 2;
        signupButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        signupButtonConstraints.insets = new Insets(10, 0, 10, 0);
        panel.add(signupButton, signupButtonConstraints);

        JButton loginButton = new JButton("Log In");
        loginButton.setBackground(Color.decode("#36393F")); // Dark gray color
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle login button click here
                frame.dispose(); // Close the signup frame
                FlatLafLogin.main(new String[]{});
                System.out.println("Login button clicked");
            }
        });
        GridBagConstraints loginButtonConstraints = new GridBagConstraints();
        loginButtonConstraints.gridx = 0;
        loginButtonConstraints.gridy = 3;
        loginButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        loginButtonConstraints.insets = new Insets(0, 0, 20, 0);
        panel.add(loginButton, loginButtonConstraints);

        JLabel termsLabel = new JLabel("By signing up, you agree to our ");
        termsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        termsLabel.setForeground(Color.LIGHT_GRAY);
        GridBagConstraints termsLabelConstraints = new GridBagConstraints();
        termsLabelConstraints.gridx = 0;
        termsLabelConstraints.gridy = 4;
        termsLabelConstraints.anchor = GridBagConstraints.CENTER;
        panel.add(termsLabel, termsLabelConstraints);

        JLabel termsLink = new JLabel("<html><a href='#'>Terms</a> & <a href='#'>Privacy</a></html>");
        termsLink.setFont(new Font("Arial", Font.PLAIN, 12));
        termsLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        termsLink.setForeground(Color.decode("#5865F2")); // Discord blue
        termsLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Handle link click here
                System.out.println("Terms or Privacy link clicked");
            }
        });
        GridBagConstraints termsLinkConstraints = new GridBagConstraints();
        termsLinkConstraints.gridx = 0;
        termsLinkConstraints.gridy = 5;
        termsLinkConstraints.anchor = GridBagConstraints.CENTER;
        panel.add(termsLink, termsLinkConstraints);

        frame.add(panel);
        frame.setVisible(true);
    }
}