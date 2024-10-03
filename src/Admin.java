import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Class representing the Admin panel
public class Admin {

    private JFrame frame; // The main frame for the admin panel
    private JTable bookTable; // Table to display the books
    private DefaultTableModel tableModel; // Model for the book table data
    private JTextField searchField; // Field for searching books
    private List<Book> books; // List to store the Book objects

    private static final String BOOK_DATA_FILE = "Bookdata.txt"; // File path for book data
    private static final String TRANSACTION_DATA_FILE = "transactions.txt"; // File path for transaction data

    // Main method to run the Admin panel
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new Admin());
    }

    // Constructor to initialize the Admin panel
    public Admin() {
        books = loadBooksFromFile(BOOK_DATA_FILE); // Load book data from file
        initializeUI(); // Initialize the user interface
    }

    // Method to initialize the user interface components
    private void initializeUI() {
        frame = new JFrame("Book Store Admin"); // Create the main frame
        frame.setBounds(100, 100, 800, 600); // Set the frame bounds
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit on close
        frame.getContentPane().setLayout(new BorderLayout(0, 0)); // Set border layout

        // Create a top panel for buttons and the logout button
        JPanel topPanel = new JPanel(new BorderLayout());

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JButton btnAddBook = new JButton("Add Book"); // Button to add a new book
        btnAddBook.addActionListener(e -> addBook()); // Add action listener to open add book dialog
        buttonPanel.add(btnAddBook); // Add button to the panel

        JLabel lblSearch = new JLabel("Search:"); // Label for the search field
        searchField = new JTextField(15); // Text field for searching
        JButton btnSearch = new JButton("Search"); // Button to trigger the search
        btnSearch.addActionListener(e -> searchBooks()); // Add action listener to perform search
        buttonPanel.add(lblSearch); // Add search label to panel
        buttonPanel.add(searchField); // Add search field to panel
        buttonPanel.add(btnSearch); // Add search button to panel

        // Add button panel to the WEST of topPanel
        topPanel.add(buttonPanel, BorderLayout.WEST);

        // Logout button
        JButton btnLogout = new JButton("Logout"); // Create logout button
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the admin panel
                Welcome.main(new String[]{}); // Go back to the welcome screen
            }
        });
        // Add logout button to the EAST of topPanel
        topPanel.add(btnLogout, BorderLayout.EAST);
        // Add topPanel to the NORTH of the frame
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);

        // Table model for displaying book data
        tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Title", "Author", "Quantity", "Price", "Edit", "Remove"} // Column headers
        ) {
            // Only allow editing in "Edit" and "Remove" columns
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 5;
            }
        };
        bookTable = new JTable(tableModel); // Create table with the data model
        // Set renderer and editor for "Edit" column buttons
        bookTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        bookTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), "Edit"));
        // Set renderer and editor for "Remove" column buttons
        bookTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        bookTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox(), "Remove"));

        JScrollPane scrollPane = new JScrollPane(bookTable); // Add table to a scroll pane
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER); // Add scroll pane to center

        updateTable(); // Update table data with loaded books

        // Button to view sales data
        JButton btnViewSales = new JButton("View Sales"); 
        btnViewSales.addActionListener(e -> viewSales(TRANSACTION_DATA_FILE));
        frame.getContentPane().add(btnViewSales, BorderLayout.SOUTH); // Add button to the south

        frame.setVisible(true); // Set the frame to visible
    }

    // Method to add a new book to the inventory
    private void addBook() {
        // Create input fields for book details
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField priceField = new JTextField();

        // Create an input dialog for adding a new book
        Object[] message = {
                "Title:", titleField,
                "Author:", authorField,
                "Quantity:", quantityField,
                "Price:", priceField
        };

        // Display the input dialog and get the user's response
        int option = JOptionPane.showConfirmDialog(frame, message, "Add Book", JOptionPane.OK_CANCEL_OPTION);

        // If the user clicked "OK"
        if (option == JOptionPane.OK_OPTION) {
            // Try to parse the input values
            try {
                String title = titleField.getText();
                String author = authorField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                double price = Double.parseDouble(priceField.getText());

                // Create a new Book object and add it to the list of books
                Book newBook = new Book(title, author, quantity, price);
                books.add(newBook);

                // Save the updated book list to the file
                saveBooksToFile();

                // Update the table to display the new book
                updateTable();
            } catch (NumberFormatException ex) {
                // If there is a parsing error, display an error message
                JOptionPane.showMessageDialog(frame, "Invalid quantity or price format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method to search for books based on title or author
    private void searchBooks() {
        String searchTerm = searchField.getText().toLowerCase(); // Get the search term and convert to lowercase

        // Create a new table model to display the search results
        DefaultTableModel searchModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Title", "Author", "Quantity", "Price", "Edit", "Remove"}
        ) {
            // Only allow editing in the "Edit" and "Remove" columns
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 5;
            }
        };

        // Iterate through the list of books
        for (Book book : books) {
            // If the book title or author contains the search term (case-insensitive)
            if (book.getTitle().toLowerCase().contains(searchTerm) || book.getAuthor().toLowerCase().contains(searchTerm)) {
                // Add the book to the search results table model
                searchModel.addRow(new Object[]{book.getTitle(), book.getAuthor(), book.getQuantity(), book.getPrice(), "Edit", "Remove"});
            }
        }

        // Set the table model to the search results model to display the results
        bookTable.setModel(searchModel);

        // Set the cell renderer and editor for the "Edit" and "Remove" buttons in the search results table
        bookTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        bookTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), "Edit"));
        bookTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        bookTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox(), "Remove"));
    }


    // Method to update the table with the data from the 'books' list
    private void updateTable() {
        tableModel.setRowCount(0); // Clear the existing rows in the table model

        // Iterate through the list of books
        for (Book book : books) {
            // Add a new row to the table model for each book
            tableModel.addRow(new Object[]{book.getTitle(), book.getAuthor(), book.getQuantity(), book.getPrice(), "Edit", "Remove"});
        }
    }

    // Method to load book data from the specified file
    private List<Book> loadBooksFromFile(String filename) {
        List<Book> loadedBooks = new ArrayList<>(); // Create a new list to store the loaded books

        // Try to open the file for reading
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line; // Variable to hold each line read from the file
            // Read the file line by line until the end of the file is reached
            while ((line = reader.readLine()) != null) {
                String[] bookData = line.split(","); // Split the line into an array of strings, delimited by a comma

                // Check if the bookData array has the expected number of elements
                if (bookData.length == 4) {
                    // Parse the book details from the array
                    String title = bookData[0].trim();
                    String author = bookData[1].trim();
                    int quantity = Integer.parseInt(bookData[2].trim());
                    double price = Double.parseDouble(bookData[3].trim());

                    // Create a new Book object and add it to the list of loaded books
                    loadedBooks.add(new Book(title, author, quantity, price));
                }
            }
        } catch (IOException ex) {
            // If an IOException occurs while reading the file, display an error message to the user
            JOptionPane.showMessageDialog(frame, "Error loading books from file.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Return the list of loaded books
        return loadedBooks;
    }

    // Method to save the current book data to the specified file
    private void saveBooksToFile() {
        // Try to open the file for writing
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOK_DATA_FILE))) {
            // Iterate over the list of books
            for (Book book : books) {
                // Write the book details to the file in CSV format
                writer.write(book.getTitle() + "," + book.getAuthor() + "," + book.getQuantity() + "," + book.getPrice());
                // Move to the next line for the next book
                writer.newLine();
            }
        } catch (IOException ex) {
            // If an IOException occurs while writing to the file, display an error message to the user
            JOptionPane.showMessageDialog(frame, "Error saving books to file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to edit the details of an existing book
    private void editBook(Book book) {
        // Create text fields for each book attribute, pre-populated with existing data
        JTextField titleField = new JTextField(book.getTitle());
        JTextField authorField = new JTextField(book.getAuthor());
        JTextField quantityField = new JTextField(String.valueOf(book.getQuantity()));
        JTextField priceField = new JTextField(String.valueOf(book.getPrice()));

        // Create an object array to hold the input fields and their labels for the dialog
        Object[] message = {
                "Title:", titleField,
                "Author:", authorField,
                "Quantity:", quantityField,
                "Price:", priceField
        };

        // Show a confirmation dialog to the user with the input fields
        int option = JOptionPane.showConfirmDialog(frame, message, "Edit Book", JOptionPane.OK_CANCEL_OPTION);

        // If the user clicks "OK"
        if (option == JOptionPane.OK_OPTION) {
            // Try to parse the updated values from the input fields
            try {
                String title = titleField.getText();
                String author = authorField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                double price = Double.parseDouble(priceField.getText());

                // Update the book object with the new values
                book.setTitle(title);
                book.setAuthor(author);
                book.setQuantity(quantity);
                book.setPrice(price);

                // Save the updated book data to the file
                saveBooksToFile();

                // Update the table to reflect the changes
                updateTable();

            } catch (NumberFormatException ex) {
                // If there is a parsing error, display an error message to the user
                JOptionPane.showMessageDialog(frame, "Invalid quantity or price format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method to remove a book from the inventory
    private void removeBook(Book book) {
        // Remove the book from the list of books
        books.remove(book);
        // Save the updated book data to the file
        saveBooksToFile();
        // Update the table to reflect the removal of the book
        updateTable();
    }

    // Method to display sales data from the specified file
    private void viewSales(String filename) {
        // Try to open the sales data file for reading
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            StringBuilder salesData = new StringBuilder();
            String line;

            // Read the file line by line and append each line to the salesData StringBuilder
            while ((line = reader.readLine()) != null) {
                salesData.append(line).append("\n");
            }

            // Create a non-editable JTextArea to display the sales data
            JTextArea textArea = new JTextArea(salesData.toString());
            textArea.setEditable(false);

            // Embed the JTextArea in a JScrollPane for scrollability
            JScrollPane scrollPane = new JScrollPane(textArea);

            // Display the sales data in a message dialog
            JOptionPane.showMessageDialog(frame, scrollPane, "Sales Data", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            // If an IOException occurs while reading the file, display an error message to the user
            JOptionPane.showMessageDialog(frame, "Error reading sales data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Inner classes for ButtonRenderer and ButtonEditor
    // These classes are used to render and handle button clicks within the JTable

    // Class to render a button in a table cell
    static class ButtonRenderer extends JButton implements TableCellRenderer {

        // Constructor for ButtonRenderer, sets the button to be opaque
        public ButtonRenderer() {
            setOpaque(true);
        }

        // Method to get the component to be rendered for the cell, returns the button itself
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString()); // Sets the text of the button to the value of the cell
            return this; // Returns the button component
        }
    }

    // Class to handle editing a button in a table cell, extends DefaultCellEditor
    class ButtonEditor extends DefaultCellEditor {

        protected JButton button; // The JButton that will be used as the editor
        private String label; // The label for the button
        private boolean isPushed; // Flag to track if the button has been pressed
        private String action; // The action to be performed by the button ("Edit" or "Remove")

        // Constructor for ButtonEditor
        public ButtonEditor(JCheckBox checkBox, String action) {
            // Call the superclass constructor with a JCheckBox, which is not actually used
            super(checkBox);
            this.action = action; // Store the action to be performed
            button = new JButton(); // Create a new JButton
            button.setOpaque(true); // Make the button opaque
            // Add an ActionListener to the button to handle button presses
            button.addActionListener(e -> {
                fireEditingStopped(); // Stop editing the cell when the button is pressed
                int selectedRow = bookTable.getSelectedRow(); // Get the index of the currently selected row
                // If a row is selected
                if (selectedRow != -1) {
                    Book selectedBook = books.get(selectedRow); // Get the Book object corresponding to the selected row
                    if (action.equals("Edit")) { // If the action is "Edit"
                        editBook(selectedBook); // Call the editBook method with the selected Book
                    } else if (action.equals("Remove")) { // If the action is "Remove"
                        removeBook(selectedBook); // Call the removeBook method with the selected Book
                    }
                }
            });
        }

        // Method to get the component that should be placed in the cell for editing
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString(); // Set the label to the string value of the cell
            button.setText(label); // Set the text of the button to the label
            isPushed = true; // Set the isPushed flag to true, indicating the button has been pressed
            return button; // Return the button component to be placed in the cell
        }

        // Method to get the value that should be stored in the cell
        @Override
        public Object getCellEditorValue() {
            isPushed = false; // Set the isPushed flag to false, indicating the editing is finished
            return label; // Return the label, which represents the value to be stored in the cell
        }

        // Method to stop editing and store the current value
        @Override
        public boolean stopCellEditing() {
            isPushed = false; // Set the isPushed flag to false, indicating editing is finished
            return super.stopCellEditing(); // Call the superclass method to stop editing
        }
    }
}
