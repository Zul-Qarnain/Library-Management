import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BookStoreApp {

    // Variables for GUI components and data handling
    private JFrame frame; // Main application window
    private JTable bookTable; // Table to display books
    private DefaultTableModel tableModel; // Model to handle table data
    private JTextField searchField; // Input field for book search
    private List<Book> books; // List to store books
    private List<CartItem> cartItems; // List to store items added to cart

    private static final String BOOK_DATA_FILE = "Bookdata.txt"; // File name for loading book data

    public static void main(String[] args) {
        // Runs the application on the event dispatch thread (EDT)
        EventQueue.invokeLater(() -> new BookStoreApp());
    }

    // Constructor to load book data and initialize the user interface
    public BookStoreApp() {
        books = loadBooksFromFile(BOOK_DATA_FILE); // Load books from file
        cartItems = new ArrayList<>(); // Initialize empty cart
        initializeUI(); // Set up the UI
    }

    // Method to initialize the User Interface
    private void initializeUI() {
        frame = new JFrame("Book Store"); // Create the main window
        frame.setBounds(100, 100, 800, 600); // Set the size and position
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close app on window close
        frame.getContentPane().setLayout(new BorderLayout(0, 0)); // Set layout manager

        // Create a search panel at the top
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel lblSearch = new JLabel("Search:");
        searchField = new JTextField(15); // Search input
        JButton btnSearch = new JButton("Search"); // Search button
        btnSearch.addActionListener(e -> searchBooks()); // Search button action listener

        // Add search components to search panel
        searchPanel.add(lblSearch);
        searchPanel.add(searchField);
        searchPanel.add(btnSearch);
        frame.getContentPane().add(searchPanel, BorderLayout.NORTH); // Add search panel to the frame

        // Table setup with columns for book details and action buttons
        tableModel = new DefaultTableModel(new Object[][]{}, new String[]{
                "Title", "Author", "Quantity", "Price", "Action"
        }) {
            // Make only the 'Action' column editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        // Create table and add custom renderers/editors for the "Action" buttons
        bookTable = new JTable(tableModel);
        bookTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        bookTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));
        JScrollPane scrollPane = new JScrollPane(bookTable); // Add scroll bar
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER); // Add table to the frame

        updateTable(); // Load books into the table

        // Button to view the shopping cart
        JButton btnViewCart = new JButton("View Cart");
        btnViewCart.addActionListener(e -> viewCart()); // Cart button action listener
        frame.getContentPane().add(btnViewCart, BorderLayout.SOUTH); // Add cart button to the frame
        frame.setVisible(true); // Show the window
    }

    // Method to load books from a file
    private List<Book> loadBooksFromFile(String filename) {
        List<Book> loadedBooks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            // Read each line and parse book data
            while ((line = reader.readLine()) != null) {
                String[] bookData = line.split(",");
                if (bookData.length == 4) {
                    String title = bookData[0].trim();
                    String author = bookData[1].trim();
                    int quantity = Integer.parseInt(bookData[2].trim());
                    double price = Double.parseDouble(bookData[3].trim());
                    loadedBooks.add(new Book(title, author, quantity, price)); // Add each book to the list
                }
            }
        } catch (IOException ex) {
            // Show error message if file loading fails
            JOptionPane.showMessageDialog(frame, "Error loading books from file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return loadedBooks; // Return the loaded list of books
    }

    // Method to search for books based on user input
    private void searchBooks() {
        String searchTerm = searchField.getText().toLowerCase(); // Get the search text
        DefaultTableModel searchModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Title", "Author", "Quantity", "Price", "Action"}
        ) {
            // Make only the 'Action' column editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        // Filter books by title or author based on the search term
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(searchTerm)
                    || book.getAuthor().toLowerCase().contains(searchTerm)) {
                // Add matching books to the table
                searchModel.addRow(new Object[]{
                        book.getTitle(), book.getAuthor(), book.getQuantity(), book.getPrice(), "Add to Cart"
                });
            }
        }

        // Update the table with the search results
        bookTable.setModel(searchModel);
        bookTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        bookTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    // Method to populate the table with the list of books
    private void updateTable() {
        tableModel.setRowCount(0); // Clear existing table rows
        for (Book book : books) {
            // Add each book to the table
            tableModel.addRow(new Object[]{
                    book.getTitle(), book.getAuthor(), book.getQuantity(), book.getPrice(), "Add to Cart"
            });
        }
    }

    // Method to add a book to the shopping cart
    private void addToCart(Book book, int quantity) {
        // Check if the requested quantity is available
        if (quantity > book.getQuantity()) {
            JOptionPane.showMessageDialog(frame, "Requested quantity is not available", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        cartItems.add(new CartItem(book, quantity)); // Add the book to the cart
        JOptionPane.showMessageDialog(frame, "Book added to cart!"); // Show confirmation
    }

    // Method to view the shopping cart
    private void viewCart() {
        // Show a message if the cart is empty
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Your cart is empty.");
            return;
        }

        // Create a modal dialog to display the cart contents
        JDialog cartDialog = new JDialog(frame, "Shopping Cart", true);
        cartDialog.setSize(400, 300);
        cartDialog.setLayout(new BorderLayout());

        // Table to display cart items with a column for totals
        DefaultTableModel cartTableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Title", "Author", "Quantity", "Price", "Total"}
        );
        JTable cartTable = new JTable(cartTableModel);
        JScrollPane cartScrollPane = new JScrollPane(cartTable); // Add scroll bar
        cartDialog.add(cartScrollPane, BorderLayout.CENTER);

        double totalPrice = 0; // Variable to calculate total price
        for (CartItem item : cartItems) {
            // Calculate the total for each item and add it to the cart table
            double itemTotal = item.getBook().getPrice() * item.getQuantity();
            cartTableModel.addRow(new Object[]{
                    item.getBook().getTitle(), item.getBook().getAuthor(),
                    item.getQuantity(), item.getBook().getPrice(), itemTotal
            });
            totalPrice += itemTotal; // Accumulate total price
        }

        // Panel at the bottom showing the total price and checkout button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JLabel totalPriceLabel = new JLabel("Total: $" + String.format("%.2f", totalPrice));
        bottomPanel.add(totalPriceLabel, BorderLayout.WEST); // Show total price

        // Checkout button with action listener
        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(e -> {
            checkout(); // Call the checkout method
            cartDialog.dispose(); // Close the cart dialog after checkout
        });
        bottomPanel.add(checkoutButton, BorderLayout.EAST);

        cartDialog.add(bottomPanel, BorderLayout.SOUTH);
        cartDialog.setVisible(true); // Show the cart dialog
    }

    // Method to handle checkout process
    private void checkout() {
        // Show a message if the cart is empty
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Your cart is empty.");
            return;
        }

        saveCartItemsToFile("cart.txt", cartItems); // Save cart items to a file

        new Payment(cartItems); // Proceed to payment (assumed to be implemented elsewhere)
    }

    // Method to save cart items to a file
    private void saveCartItemsToFile(String filename, List<CartItem> items) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Write each cart item to the file
            for (CartItem item : items) {
                Book book = item.getBook();
                writer.write(book.getTitle() + "," + book.getAuthor() + "," + item.getQuantity() + "," + book.getPrice());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print the error
            JOptionPane.showMessageDialog(frame, "Error saving cart to file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Inner classes for ButtonRenderer and ButtonEditor
    // Custom renderer and editor to add buttons in the table for adding books to the cart
    static class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true); // Make the button non-transparent
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString()); // Set button text
            return this; // Return the button as a renderer component
        }
    }

    // Custom cell editor to handle button clicks in the table
    class ButtonEditor extends DefaultCellEditor {

        protected JButton button; // Button component for the editor
        private String label; // Label for the button
        private boolean isPushed; // Flag to track if the button is clicked

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton(); // Initialize button
            button.setOpaque(true); // Make the button non-transparent
            button.addActionListener(e -> fireEditingStopped()); // Stop editing when clicked
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString(); // Set button label
            button.setText(label); // Set the button text
            isPushed = true; // Track button state
            return button; // Return the button as an editor component
        }

        @Override
        public Object getCellEditorValue() {
            // Handle the click event for the button
            if (isPushed) {
                int selectedRow = bookTable.getSelectedRow(); // Get the selected row
                if (selectedRow != -1) {
                    Book selectedBook = books.get(selectedRow); // Get the selected book
                    String input = JOptionPane.showInputDialog(frame, "Enter quantity:"); // Prompt for quantity
                    try {
                        int quantity = Integer.parseInt(input); // Parse quantity input
                        addToCart(selectedBook, quantity); // Add book to cart
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid quantity", "Error", JOptionPane.ERROR_MESSAGE); // Show error for invalid input
                    }
                }
            }
            isPushed = false; // Reset button state
            return label; // Return the button label
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false; // Reset button state when editing stops
            return super.stopCellEditing();
        }
    }
}
