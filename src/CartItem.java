// Class representing an item in the shopping cart
public class CartItem {
    private Book book; // Book object in the cart
    private int quantity; // Quantity of the book

    // Constructor to create a cart item with a book and its quantity
    public CartItem(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    // Getter method to retrieve the book
    public Book getBook() {
        return book;
    }

    // Getter method to retrieve the quantity
    public int getQuantity() {
        return quantity;
    }
}
