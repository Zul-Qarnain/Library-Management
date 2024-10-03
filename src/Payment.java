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
        // Create the main frame for the payment screen
        frame = new JFrame("Book Purchase Payment");
        // Dispose the frame on close
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Set the size of the frame
        frame.setSize(400, 400);
        // Center the frame on the screen
        frame.setLocationRelativeTo(null);

        // Create a JPanel with GridLayout for main content
        JPanel mainPanel = new JPanel(new GridLayout(7, 1, 5, 10));
        // Add padding around the main panel
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Calculate and display the total cost
        double totalCost = calculateTotalCost();
        JLabel costLabel = new JLabel("Total Cost: " + totalCost + " TK");
        mainPanel.add(costLabel);

        // Create a panel for selecting payment method
        JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        paymentPanel.add(new JLabel("Select Payment Method:"));

        // Add radio buttons for bKash and Nagad
        JRadioButton bKashButton = new JRadioButton("bKash");
        JRadioButton nagadButton = new JRadioButton("Nagad");
        ButtonGroup paymentGroup = new ButtonGroup();
        paymentGroup.add(bKashButton);
        paymentGroup.add(nagadButton);
        paymentPanel.add(bKashButton);
        paymentPanel.add(nagadButton);
        mainPanel.add(paymentPanel);

        // Create a panel for entering phone number
        JPanel phoneNumberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        phoneNumberPanel.add(new JLabel("Enter Phone Number:"));
        JTextField phoneNumberField = new JTextField(15);
        phoneNumberPanel.add(phoneNumberField);
        mainPanel.add(phoneNumberPanel);

        // Create a panel for entering transaction ID
        JPanel transactionIdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        transactionIdPanel.add(new JLabel("Enter Transaction ID:"));
        JTextField transactionIdField = new JTextField(15);
        transactionIdPanel.add(transactionIdField);
        mainPanel.add(transactionIdPanel);

        // Create a panel for entering address
        JPanel addressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addressPanel.add(new JLabel("Enter Address:"));
        addressField = new JTextField(25);
        addressPanel.add(addressField);
        mainPanel.add(addressPanel);

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton purchaseButton = new JButton("Purchase");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(purchaseButton);
        buttonPanel.add(cancelButton);

        // Add main panel and button panel to the frame
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

        // Add action listener for Purchase button
        purchaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get values from input fields
                String phoneNumber = phoneNumberField.getText();
                String transactionId = transactionIdField.getText();
                String address = addressField.getText();

                // Check if any field is empty
                if (phoneNumber.isEmpty() || transactionId.isEmpty() || address.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all fields.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Save transaction details, reduce book quantities,
                // show purchased books, clear the cart, and close the payment frame
                saveTransactionToFile(TRANSACTION_DATA_FILE, phoneNumber, transactionId,
                        totalCost, address);
                reduceBookQuantities(BOOK_DATA_FILE);
                showPurchasedBooks();
                clearCartFile("cart.txt");
                frame.dispose();
            }
        });

        // Add action listener for Cancel button to dispose the frame
        cancelButton.addActionListener(e -> frame.dispose());
    }

    // Method to clear the cart file
    private void clearCartFile(String filename) {
        // Overwrites the file with an empty string, effectively clearing it
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("");
        } catch (IOException e) {
            System.err.println("Error clearing cart file: " + e.getMessage());
        }
    }

    // Method to calculate the total cost of items in the cart
    private double calculateTotalCost() {
        double totalCost = 0;
        for (CartItem item : cartItems) {
            totalCost += item.getBook().getPrice() * item.getQuantity();
        }
        return totalCost;
    }

    // Method to save transaction details to a file
    private void saveTransactionToFile(String filename, String phoneNumber, String transactionId,
                                       double totalCost, String address) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            // Write transaction details to the file
            writer.write("Phone Number: " + phoneNumber);
            writer.newLine();
            writer.write("Transaction ID: " + transactionId);
            writer.newLine();
            writer.write("Total Cost: " + totalCost + " TK");
            writer.newLine();
            writer.write("Address: " + address);
            writer.newLine();
            writer.newLine();

            // Show success message
            JOptionPane.showMessageDialog(frame, "Purchase Successful!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            // Show error message if saving transaction fails
            JOptionPane.showMessageDialog(frame, "Error saving transaction details.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Method to reduce book quantities after a purchase
    private void reduceBookQuantities(String filename) {
        List<Book> updatedBooks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            // Read the book data file line by line
            while ((line = reader.readLine()) != null) {
                String[] bookData = line.split(",");
                if (bookData.length == 4) {
                    // Extract book details
                    String title = bookData[0].trim();
                    String author = bookData[1].trim();
                    int quantity = Integer.parseInt(bookData[2].trim());
                    double price = Double.parseDouble(bookData[3].trim());

                    // Reduce quantity based on purchased items
                    for (CartItem item : cartItems) {
                        if (item.getBook().getTitle().equalsIgnoreCase(title)
                                && item.getBook().getAuthor().equalsIgnoreCase(author)) {
                            quantity -= item.getQuantity();
                            break;
                        }
                    }
                    // Add the updated book to the list
                    updatedBooks.add(new Book(title, author, quantity, price));
                }
            }

            // Write the updated book list back to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                for (Book book : updatedBooks) {
                    writer.write(book.getTitle() + "," + book.getAuthor() + ","
                            + book.getQuantity() + "," + book.getPrice());
                    writer.newLine();
                }
            } catch (IOException ex) {
                // Show error message if updating quantities fails
                JOptionPane.showMessageDialog(frame, "Error updating book quantities.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }

        } catch (IOException ex) {
            // Show error message if reading book data fails
            JOptionPane.showMessageDialog(frame, "Error reading book data.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Method to display a list of purchased books
    private void showPurchasedBooks() {
        StringBuilder message = new StringBuilder("Books Purchased:\n\n");
        for (CartItem item : cartItems) {
            message.append("- ").append(item.getBook().getTitle())
                    .append(" by ").append(item.getBook().getAuthor())
                    .append(" (Quantity: ").append(item.getQuantity()).append(")\n");
        }
        JOptionPane.showMessageDialog(frame, message.toString(), "Purchase Details",
                JOptionPane.INFORMATION_MESSAGE);
    }}
