import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Payment extends JFrame {

    private JFrame frame;
    private List<CartItem> cartItems;
    private JTextField addressField;
    private static final String BOOK_DATA_FILE = "Bookdata.txt";
    private static final String TRANSACTION_DATA_FILE = "transactions.txt";

    public Payment(List<CartItem> cartItems) {
        // Create a copy to avoid directly modifying the original cartItems
        this.cartItems = new ArrayList<>(cartItems);
        initializePaymentGUI();
    }

    private void initializePaymentGUI() {
        frame = new JFrame("Book Purchase Payment");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null); // Center the frame

        JPanel mainPanel = new JPanel(new GridLayout(7, 1, 5, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        double totalCost = calculateTotalCost();
        JLabel costLabel = new JLabel("Total Cost: " + totalCost + " TK");
        mainPanel.add(costLabel);

        JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        paymentPanel.add(new JLabel("Select Payment Method:"));
        JRadioButton bKashButton = new JRadioButton("Akash");
        JRadioButton nagadButton = new JRadioButton("Baki");
        ButtonGroup paymentGroup = new ButtonGroup();
        paymentGroup.add(bKashButton);
        paymentGroup.add(nagadButton);
        paymentPanel.add(bKashButton);
        paymentPanel.add(nagadButton);
        mainPanel.add(paymentPanel);

        JPanel phoneNumberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        phoneNumberPanel.add(new JLabel("Enter Phone Number:"));
        JTextField phoneNumberField = new JTextField(15);
        phoneNumberPanel.add(phoneNumberField);
        mainPanel.add(phoneNumberPanel);

        JPanel transactionIdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        transactionIdPanel.add(new JLabel("Enter Transaction ID:"));
        JTextField transactionIdField = new JTextField(15);
        transactionIdPanel.add(transactionIdField);
        mainPanel.add(transactionIdPanel);

        JPanel addressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addressPanel.add(new JLabel("Enter Address:"));
        addressField = new JTextField(25);
        addressPanel.add(addressField);
        mainPanel.add(addressPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton purchaseButton = new JButton("Purchase");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(purchaseButton);
        buttonPanel.add(cancelButton);

        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

        purchaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String phoneNumber = phoneNumberField.getText();
                String transactionId = transactionIdField.getText();
                String address = addressField.getText();

                if (phoneNumber.isEmpty() || transactionId.isEmpty() || address.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all fields.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                saveTransactionToFile(TRANSACTION_DATA_FILE, phoneNumber, transactionId,
                        totalCost, address);
                reduceBookQuantities(BOOK_DATA_FILE);
                showPurchasedBooks();
                clearCartFile("cart.txt");
                frame.dispose();
            }
        });

        cancelButton.addActionListener(e -> frame.dispose());
    }

    private void clearCartFile(String filename) {
        // Overwrites the file with an empty string, effectively clearing it
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("");
        } catch (IOException e) {
            System.err.println("Error clearing cart file: " + e.getMessage());
        }
    }

    private double calculateTotalCost() {
        double totalCost = 0;
        for (CartItem item : cartItems) {
            totalCost += item.getBook().getPrice() * item.getQuantity();
        }
        return totalCost;
    }

    private void saveTransactionToFile(String filename, String phoneNumber, String transactionId,
                                       double totalCost, String address) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write("Phone Number: " + phoneNumber);
            writer.newLine();
            writer.write("Transaction ID: " + transactionId);
            writer.newLine();
            writer.write("Total Cost: " + totalCost + " TK");
            writer.newLine();
            writer.write("Address: " + address);
            writer.newLine();
            writer.newLine();

            JOptionPane.showMessageDialog(frame, "Purchase Successful!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error saving transaction details.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void reduceBookQuantities(String filename) {
        List<Book> updatedBooks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bookData = line.split(",");
                if (bookData.length == 4) {
                    String title = bookData[0].trim();
                    String author = bookData[1].trim();
                    int quantity = Integer.parseInt(bookData[2].trim());
                    double price = Double.parseDouble(bookData[3].trim());

                    for (CartItem item : cartItems) {
                        if (item.getBook().getTitle().equalsIgnoreCase(title)
                                && item.getBook().getAuthor().equalsIgnoreCase(author)) {
                            quantity -= item.getQuantity();
                            break;
                        }
                    }
                    updatedBooks.add(new Book(title, author, quantity, price));
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                for (Book book : updatedBooks) {
                    writer.write(book.getTitle() + "," + book.getAuthor() + ","
                            + book.getQuantity() + "," + book.getPrice());
                    writer.newLine();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error updating book quantities.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error reading book data.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void showPurchasedBooks() {
        StringBuilder message = new StringBuilder("Books Purchased:\n\n");
        for (CartItem item : cartItems) {
            message.append("- ").append(item.getBook().getTitle())
                    .append(" by ").append(item.getBook().getAuthor())
                    .append(" (Quantity: ").append(item.getQuantity()).append(")\n");
        }
        JOptionPane.showMessageDialog(frame, message.toString(), "Purchase Details",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
