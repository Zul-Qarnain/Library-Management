import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
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

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                BookStoreApp window = new BookStoreApp();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public BookStoreApp() {
        initialize();
        books = new ArrayList<>();
        cartItems = new ArrayList<>();
        loadBooksFromFile();
        // Preload some books into the store for customer to see
    }

    private void initialize() {
        frame = new JFrame("Book Store");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.NORTH);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        JLabel lblSearch = new JLabel("Search:");
        panel.add(lblSearch);

        searchField = new JTextField();
        panel.add(searchField);
        searchField.setColumns(10);

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> searchBooks());
        panel.add(btnSearch);

        JScrollPane scrollPane = new JScrollPane();
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        bookTable = new JTable();
        tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Title", "Quantity", "Price","Author","Action"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only allow editing the "Action" column (Add to Cart button)
            }
        };
        bookTable.setModel(tableModel);
        scrollPane.setViewportView(bookTable);

        // Set custom cell renderer and editor for the "Action" column
        bookTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        bookTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));

        JButton btnViewCart = new JButton("View Cart");
        btnViewCart.addActionListener(e -> viewCart());
        frame.getContentPane().add(btnViewCart, BorderLayout.SOUTH);
    }

    private void loadBooksFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Bookdata.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bookData = line.split(",");
                if (bookData.length == 4) {
                    String title = bookData[0];
                    String author = bookData[1];
                    int quantity = Integer.parseInt(bookData[2]);
                    double price = Double.parseDouble(bookData[3]);
                    books.add(new BookStoreApp.Book(title, author, quantity, price));
                }
            }
            updateTable();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error loading books from file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchBooks() {
        String searchTerm = searchField.getText().toLowerCase();
        DefaultTableModel searchModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Title", "Quantity", "Price", "Action"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only allow editing the "Action" column (Add to Cart button)
            }
        };

        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(searchTerm)) {
                searchModel.addRow(new Object[]{book.getTitle(),book.getAuthor(), book.getQuantity(), book.getPrice(), "Add to Cart"});
            }
        }

        bookTable.setModel(searchModel);
        bookTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        bookTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    private void updateTable() {
        tableModel.setRowCount(0); // Clear existing data

        for (Book book : books) {
            tableModel.addRow(new Object[]{book.getTitle(),book.getAuthor(), book.getQuantity(), book.getPrice(), "Add to Cart"});
        }
    }

    private void addToCart(Book book) {
        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, book.getQuantity(), 1);
        JSpinner quantitySpinner = new JSpinner(model);

        Object[] message = {
                "Quantity:", quantitySpinner
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Add to Cart", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            int quantity = (int) quantitySpinner.getValue();
            cartItems.add(new CartItem(book, quantity));
            JOptionPane.showMessageDialog(frame, "Book added to cart!");
        }
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
                new String[]{"Title", "Quantity","Author","Price", "Total"}
        );
        JTable cartTable = new JTable(cartTableModel);
        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartDialog.add(cartScrollPane, BorderLayout.CENTER);

        double totalPrice = 0;
        for (CartItem item : cartItems) {
            double itemTotal = item.getBook().getPrice() * item.getQuantity();
            cartTableModel.addRow(new Object[]{item.getBook().getTitle(),item.getBook().getAuthor(), item.getQuantity(), item.getBook().getPrice(), itemTotal});
            totalPrice += itemTotal;
        }

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JLabel totalPriceLabel = new JLabel("Total: $" + String.format("%.2f", totalPrice));
        bottomPanel.add(totalPriceLabel, BorderLayout.WEST);

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(e -> {
            checkout();
            cartDialog.dispose();
        });
        bottomPanel.add(checkoutButton, BorderLayout.EAST);

        cartDialog.add(bottomPanel, BorderLayout.SOUTH);
        cartDialog.setVisible(true);
    }

    private void checkout() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("cart.txt"))) {
            for (CartItem item : cartItems) {
                writer.write(item.getBook().getTitle() + "," +item.getBook().getAuthor()+","+ item.getQuantity() + "," + item.getBook().getPrice());
                writer.newLine();
            }
            JOptionPane.showMessageDialog(frame, "Checkout successful! Cart items saved to cart.txt");
            cartItems.clear(); // Clear the cart after checkout
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error saving cart items to file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    static class Book {
        private String title;
        private String author;
        private int quantity;
        private double price;

        public Book(String title,String author, int quantity, double price) {
            this.title = title;
            this.author = author;
            this.quantity = quantity;
            this.price = price;
        }

        public String getTitle() {
            return title;
        }
        public String getAuthor() {
            return author;
        }
        public int getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }
    }

    static class CartItem {
        private Book book;
        private int quantity;

        public CartItem(Book book, int quantity) {
            this.book = book;
            this.quantity = quantity;
        }

        public Book getBook() {
            return book;
        }

        public int getQuantity() {
            return quantity;
        }
    }

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
            button.addActionListener(e -> {
                fireEditingStopped(); // Ensure the editing is stopped before taking action
                int selectedRow = bookTable.getSelectedRow();
                if (selectedRow != -1) { // Check if a row is selected
                    Book selectedBook = books.get(selectedRow);
                    addToCart(selectedBook);
                }
            });
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
            isPushed = false;
            return label; // Return the label as the cell value
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}
