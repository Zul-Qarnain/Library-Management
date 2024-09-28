import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Admin {
    private JFrame frame;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private List<Book> books;
    private static final String FILE_NAME = "Bookdata.txt";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Admin window = new Admin();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Admin() {
        initialize();
        books = new ArrayList<>();
        loadBooksFromFile();
    }

    private void initialize() {
        frame = new JFrame("Book Store Admin");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.NORTH);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        JButton btnAddBook = new JButton("Add Book");
        btnAddBook.addActionListener(e -> addBook());
        panel.add(btnAddBook);

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
                new Object[][]{}, // Empty table to start
                new String[]{"Title", "Author", "Quantity", "Price", "Edit", "Remove"} // Columns for book data
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 5; // Only "Edit" and "Remove" columns are editable
            }
        };
        bookTable.setModel(tableModel);
        scrollPane.setViewportView(bookTable);

        // Set custom cell renderer and editor for the "Edit" and "Remove" columns
        bookTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        bookTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), "Edit"));

        bookTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        bookTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox(), "Remove"));

        JButton btnViewSales = new JButton("View Sales");
        btnViewSales.addActionListener(e -> viewSales());
        frame.getContentPane().add(btnViewSales, BorderLayout.SOUTH);
    }

    private void addBook() {
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField priceField = new JTextField();

        Object[] message = {
                "Title:", titleField,
                "Author:", authorField,
                "Quantity:", quantityField,
                "Price:", priceField
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Add Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String title = titleField.getText();
                String author = authorField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                double price = Double.parseDouble(priceField.getText());

                Book newBook = new Book(title, author, quantity, price);
                books.add(newBook);
                saveBooksToFile();
                updateTable();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid quantity or price format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchBooks() {
        String searchTerm = searchField.getText().toLowerCase();
        DefaultTableModel searchModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Title", "Author", "Quantity", "Price", "Edit", "Remove"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 5; // Only "Edit" and "Remove" columns are editable
            }
        };

        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(searchTerm) || book.getAuthor().toLowerCase().contains(searchTerm)) {
                searchModel.addRow(new Object[]{book.getTitle(), book.getAuthor(), book.getQuantity(), book.getPrice(), "Edit", "Remove"});
            }
        }

        bookTable.setModel(searchModel);
        bookTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        bookTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), "Edit"));
        bookTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        bookTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox(), "Remove"));
    }

    private void updateTable() {
        tableModel.setRowCount(0); // Clear existing data

        for (Book book : books) {
            tableModel.addRow(new Object[]{book.getTitle(), book.getAuthor(), book.getQuantity(), book.getPrice(), "Edit", "Remove"});
        }
    }

    private void loadBooksFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bookData = line.split(",");
                if (bookData.length == 4) {
                    String title = bookData[0];
                    String author = bookData[1];
                    int quantity = Integer.parseInt(bookData[2]);
                    double price = Double.parseDouble(bookData[3]);
                    books.add(new Book(title, author, quantity, price));
                }
            }
            updateTable();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error loading books from file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveBooksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Book book : books) {
                writer.write(book.getTitle() + "," + book.getAuthor() + "," + book.getQuantity() + "," + book.getPrice());
                writer.newLine();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error saving books to file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editBook(Book book) {
        JTextField titleField = new JTextField(book.getTitle());
        JTextField authorField = new JTextField(book.getAuthor());
        JTextField quantityField = new JTextField(String.valueOf(book.getQuantity()));
        JTextField priceField = new JTextField(String.valueOf(book.getPrice()));

        Object[] message = {
                "Title:", titleField,
                "Author:", authorField,
                "Quantity:", quantityField,
                "Price:", priceField
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Edit Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String title = titleField.getText();
                String author = authorField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                double price = Double.parseDouble(priceField.getText());

                book.setTitle(title);
                book.setAuthor(author);
                book.setQuantity(quantity);
                book.setPrice(price);
                saveBooksToFile();
                updateTable();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid quantity or price format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removeBook(Book book) {
        books.remove(book);
        saveBooksToFile();
        updateTable();
    }

    private void viewSales() {
        JDialog salesDialog = new JDialog(frame, "Sales Report", true);
        salesDialog.setSize(600, 400);
        salesDialog.setLayout(new BorderLayout());

        DefaultTableModel salesTableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Transaction ID", "Phone Number", "Buyer's address", "Total Price"}
        );
        JTable salesTable = new JTable(salesTableModel);
        JScrollPane scrollPane = new JScrollPane(salesTable);
        salesDialog.add(scrollPane, BorderLayout.CENTER);

        try (BufferedReader reader = new BufferedReader(new FileReader("cart.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] transactionData = line.split(",");
                if (transactionData.length >= 4) {
                    String transactionId = transactionData[1];
                    String phoneNumber = transactionData[0];
                    String buyerAddress = transactionData[3];
                    String totalPrice = transactionData[4];

                    salesTableModel.addRow(new Object[]{transactionId, phoneNumber, buyerAddress, totalPrice});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error loading sales data.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> salesDialog.dispose());
        salesDialog.add(closeButton, BorderLayout.SOUTH);

        salesDialog.setVisible(true);
    }

    static class Book {
        private String title;
        private String author;
        private int quantity;
        private double price;

        public Book(String title, String author, int quantity, double price) {
            this.title = title;
            this.author = author;
            this.quantity = quantity;
            this.price = price;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
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
        private String action;

        public ButtonEditor(JCheckBox checkBox, String action) {
            super(checkBox);
            this.action = action;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                fireEditingStopped(); // Ensure the editing is stopped before taking action
                int selectedRow = bookTable.getSelectedRow();
                if (selectedRow != -1) {
                    Book selectedBook = books.get(selectedRow);
                    if (action.equals("Edit")) {
                        editBook(selectedBook);
                    } else if (action.equals("Remove")) {
                        removeBook(selectedBook);
                    }
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
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}
