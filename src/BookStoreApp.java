import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BookStoreApp {

    private JFrame frame;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private List<Book> books;
    private List<CartItem> cartItems;

    private static final String BOOK_DATA_FILE = "Bookdata.txt";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new BookStoreApp());
    }

    public BookStoreApp() {
        books = loadBooksFromFile(BOOK_DATA_FILE);
        cartItems = new ArrayList<>();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Book Store");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel lblSearch = new JLabel("Search:");
        searchField = new JTextField(15);
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> searchBooks());

        searchPanel.add(lblSearch);
        searchPanel.add(searchField);
        searchPanel.add(btnSearch);
        frame.getContentPane().add(searchPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[][]{}, new String[]{
                "Title", "Author", "Quantity", "Price", "Action"
        }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        bookTable = new JTable(tableModel);
        bookTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        bookTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));
        JScrollPane scrollPane = new JScrollPane(bookTable);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        updateTable();

        JButton btnViewCart = new JButton("View Cart");
        btnViewCart.addActionListener(e -> viewCart());
        frame.getContentPane().add(btnViewCart, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private List<Book> loadBooksFromFile(String filename) {
        List<Book> loadedBooks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bookData = line.split(",");
                if (bookData.length == 4) {
                    String title = bookData[0].trim();
                    String author = bookData[1].trim();
                    int quantity = Integer.parseInt(bookData[2].trim());
                    double price = Double.parseDouble(bookData[3].trim());
                    loadedBooks.add(new Book(title, author, quantity, price));
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error loading books from file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return loadedBooks;
    }

    private void searchBooks() {
        String searchTerm = searchField.getText().toLowerCase();
        DefaultTableModel searchModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Title", "Author", "Quantity", "Price", "Action"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(searchTerm)
                    || book.getAuthor().toLowerCase().contains(searchTerm)) {
                searchModel.addRow(new Object[]{
                        book.getTitle(), book.getAuthor(), book.getQuantity(), book.getPrice(), "Add to Cart"
                });
            }
        }

        bookTable.setModel(searchModel);
        bookTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        bookTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (Book book : books) {
            tableModel.addRow(new Object[]{
                    book.getTitle(), book.getAuthor(), book.getQuantity(), book.getPrice(), "Add to Cart"
            });
        }
    }

    private void addToCart(Book book, int quantity) {
        if (quantity > book.getQuantity()) {
            JOptionPane.showMessageDialog(frame, "Requested quantity is not available", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        cartItems.add(new CartItem(book, quantity));
        JOptionPane.showMessageDialog(frame, "Book added to cart!");
    }

    private void viewCart() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Your cart is empty.");
            return;
        }

        JDialog cartDialog = new JDialog(frame, "Shopping Cart", true);
        cartDialog.setSize(400, 300);
        cartDialog.setLayout(new BorderLayout());

        DefaultTableModel cartTableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Title", "Author", "Quantity", "Price", "Total"}
        );
        JTable cartTable = new JTable(cartTableModel);
        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartDialog.add(cartScrollPane, BorderLayout.CENTER);

        double totalPrice = 0;
        for (CartItem item : cartItems) {
            double itemTotal = item.getBook().getPrice() * item.getQuantity();
            cartTableModel.addRow(new Object[]{
                    item.getBook().getTitle(), item.getBook().getAuthor(),
                    item.getQuantity(), item.getBook().getPrice(), itemTotal
            });
            totalPrice += itemTotal;
        }

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JLabel totalPriceLabel = new JLabel("Total: $" + String.format("%.2f", totalPrice));
        bottomPanel.add(totalPriceLabel, BorderLayout.WEST);

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(e -> {
            checkout(); // Call the checkout method
            cartDialog.dispose();
        });
        bottomPanel.add(checkoutButton, BorderLayout.EAST);

        cartDialog.add(bottomPanel, BorderLayout.SOUTH);
        cartDialog.setVisible(true);
    }

    private void checkout() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Your cart is empty.");
            return;
        }

        saveCartItemsToFile("cart.txt", cartItems);

        new Payment(cartItems);
    }

    private void saveCartItemsToFile(String filename, List<CartItem> items) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (CartItem item : items) {
                Book book = item.getBook();
                writer.write(book.getTitle() + "," + book.getAuthor() + "," + item.getQuantity() + "," + book.getPrice());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error saving cart to file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Inner classes for ButtonRenderer and ButtonEditor
    // (These can remain the same as your original code)

    static class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {

        protected JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int selectedRow = bookTable.getSelectedRow();
                if (selectedRow != -1) {
                    Book selectedBook = books.get(selectedRow);
                    String input = JOptionPane.showInputDialog(frame, "Enter quantity:");
                    try {
                        int quantity = Integer.parseInt(input);
                        addToCart(selectedBook, quantity);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid quantity", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}
