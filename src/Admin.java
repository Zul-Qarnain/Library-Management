import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Admin {

    private JFrame frame;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private List<Book> books;

    private static final String BOOK_DATA_FILE = "Bookdata.txt";
    private static final String TRANSACTION_DATA_FILE = "transactions.txt";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new Admin());
    }

    public Admin() {
        books = loadBooksFromFile(BOOK_DATA_FILE);
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Book Store Admin");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        // Create a top panel for buttons and the logout button
        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JButton btnAddBook = new JButton("Add Book");
        btnAddBook.addActionListener(e -> addBook());
        buttonPanel.add(btnAddBook);

        JLabel lblSearch = new JLabel("Search:");
        searchField = new JTextField(15);
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> searchBooks());

        buttonPanel.add(lblSearch);
        buttonPanel.add(searchField);
        buttonPanel.add(btnSearch);

        // Add button panel to the WEST of topPanel
        topPanel.add(buttonPanel, BorderLayout.WEST);

        // Logout button
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                Welcome.main(new String[]{});
            }
        });

        // Add logout button to the EAST of topPanel
        topPanel.add(btnLogout, BorderLayout.EAST);

        // Add topPanel to the NORTH of the frame
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Title", "Author", "Quantity", "Price", "Edit", "Remove"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 5;
            }
        };
        bookTable = new JTable(tableModel);
        bookTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        bookTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), "Edit"));

        bookTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        bookTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox(), "Remove"));

        JScrollPane scrollPane = new JScrollPane(bookTable);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        updateTable();

        JButton btnViewSales = new JButton("View Sales");
        btnViewSales.addActionListener(e -> viewSales(TRANSACTION_DATA_FILE));
        frame.getContentPane().add(btnViewSales, BorderLayout.SOUTH);

        frame.setVisible(true);
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
                return column == 4 || column == 5;
            }
        };

        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(searchTerm)
                    || book.getAuthor().toLowerCase().contains(searchTerm)) {
                searchModel.addRow(new Object[]{
                        book.getTitle(), book.getAuthor(), book.getQuantity(), book.getPrice(), "Edit", "Remove"
                });
            }
        }

        bookTable.setModel(searchModel);
        bookTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        bookTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), "Edit"));

        bookTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        bookTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox(), "Remove"));
    }

    private void updateTable() {
        tableModel.setRowCount(0);

        for (Book book : books) {
            tableModel.addRow(new Object[]{
                    book.getTitle(), book.getAuthor(), book.getQuantity(), book.getPrice(), "Edit", "Remove"
            });
        }
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

    private void saveBooksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOK_DATA_FILE))) {
            for (Book book : books) {
                writer.write(book.getTitle() + "," + book.getAuthor() + ","
                        + book.getQuantity() + "," + book.getPrice());
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

    private void viewSales(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            StringBuilder salesData = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                salesData.append(line).append("\n");
            }

            JTextArea textArea = new JTextArea(salesData.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            JOptionPane.showMessageDialog(frame, scrollPane, "Sales Data", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error reading sales data.", "Error", JOptionPane.ERROR_MESSAGE);
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
        private String action;

        public ButtonEditor(JCheckBox checkBox, String action) {
            super(checkBox);
            this.action = action;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                fireEditingStopped();
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
