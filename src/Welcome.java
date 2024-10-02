import javax.swing.*;
import java.awt.*;

public class Welcome {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bokamari");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        JLabel titleLabel = new JLabel("Welcome To");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        addComponent(panel, titleLabel, 0, 0, 1, 1, GridBagConstraints.CENTER);

        JLabel subtitleLabel = new JLabel("Bokamari.");
        subtitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        addComponent(panel, subtitleLabel, 0, 1, 1, 1, GridBagConstraints.CENTER);

        JButton buybook = new JButton("Buy Book");
        buybook.setFont(new Font("Arial", Font.BOLD, 16));
        buybook.addActionListener(e -> {
            frame.dispose();
            BookStoreApp.main(new String[]{});
        });
        addComponent(panel, buybook, 0, 2, 1, 1, GridBagConstraints.CENTER);

        JButton loginButton = new JButton("AdminPanel");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.addActionListener(e -> {
            frame.dispose();
            Login.main(new String[]{});
        });
        addComponent(panel, loginButton, 0, 3, 1, 1, GridBagConstraints.CENTER);

        JLabel termsLabel = new JLabel("By signing up, you agree to our ");
        termsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        addComponent(panel, termsLabel, 0, 4, 1, 1, GridBagConstraints.CENTER);

        JLabel termsLink = createLinkLabel("<html><a href='#'>Terms</a> & <a href='#'>Privacy</a></html>");
        addComponent(panel, termsLink, 0, 5, 1, 1, GridBagConstraints.CENTER);

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
        constraints.insets = new Insets(10, 20, 10, 20);
        constraints.anchor = anchor;
        panel.add(component, constraints);
    }

    private static JLabel createLinkLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.setForeground(Color.BLUE);
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                System.out.println("Terms or Privacy link clicked");
            }
        });
        return label;
    }
}
