// Class representing a Book object
public class Book {
    private String title; // Title of the book
    private String author; // Author of the book
    private int quantity; // Quantity of the book in stock
    private double price; // Price of the book

    // Constructor to initialize a new Book object
    public Book(String title, String author, int quantity, double price) {
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and setters for the Book object's attributes

    // Getter for the title
    public String getTitle() {
        return title;
    }

    // Setter for the title
    public void setTitle(String title) {
        this.title = title;
    }

    // Getter for the author
    public String getAuthor() {
        return author;
    }

    // Setter for the author
    public void setAuthor(String author) {
        this.author = author;
    }

    // Getter for the quantity
    public int getQuantity() {
        return quantity;
    }

    // Setter for the quantity
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Getter for the price
    public double getPrice() {
        return price;
    }

    // Setter for the price
    public void setPrice(double price) {
        this.price = price;
    }
}
