package al.sda.LibraryManagement.entity;

import jakarta.persistence.*;

@Entity
public class Book {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String title;
        private String isbn;
        private int year;
        private int quantity;
        private int available;
        @ManyToOne
        @JoinColumn(name = "author_id")
        private Author author;
    
    public Book() {
    }
    
    public Book(Long id, String title, String isbn, int year, int quantity, int available, Author author) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.year = year;
        this.quantity = quantity;
        this.available = available;
        this.author = author;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public int getYear() {
        return year;
    }
    
    public void setYear(int year) {
        this.year = year;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public int getAvailable() {
        return available;
    }
    
    public void setAvailable(int available) {
        this.available = available;
    }
    
    public Author getAuthor() {
        return author;
    }
    
    public void setAuthor(Author author) {
        this.author = author;
    }
    
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", year=" + year +
                ", quantity=" + quantity +
                ", available=" + available +
                ", author=" + author +
                '}';
    }
}
