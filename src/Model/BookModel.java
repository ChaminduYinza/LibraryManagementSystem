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
    
    
    String bId ;
    String bookName;
    String author;
    String price;
    String isbn;
    String description;
    String availability;

    public BookModel(String BID, String BOOKNAME, String AUTHOR, String PRICE, String ISBN, String DESCRIPTION, String AVAILABILITY) {
        this.bId = BID;
        this.bookName = BOOKNAME;
        this.author = AUTHOR;
        this.price = PRICE;
        this.isbn = ISBN;
        this.description = DESCRIPTION;
        this.availability = AVAILABILITY;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
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
