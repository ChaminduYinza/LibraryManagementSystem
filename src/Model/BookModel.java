/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Y`inza
 */
public class BookModel {
    
    
    String bid ;
    String bookName;
    String author;
    String price;
    String isbn;
    String description;
    String availability;
    String version;
    String quantity;

    public BookModel(String bid, String bookName, String author, String price, String isbn, String description, String availability, String version, String quantity) {
        this.bid = bid;
        this.bookName = bookName;
        this.author = author;
        this.price = price;
        this.isbn = isbn;
        this.description = description;
        this.availability = availability;
        this.version = version;
        this.quantity = quantity;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

   
    
    public String getbId() {
        return bid;
    }

    public void setbId(String bId) {
        this.bid = bId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
    
}
