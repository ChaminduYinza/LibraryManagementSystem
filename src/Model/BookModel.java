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

    public BookModel(String BID, String BOOKNAME, String AUTHOR, String PRICE, String ISBN, String DESCRIPTION, String AVAILABILITY) {
        this.bid = BID;
        this.bookName = BOOKNAME;
        this.author = AUTHOR;
        this.price = PRICE;
        this.isbn = ISBN;
        this.description = DESCRIPTION;
        this.availability = AVAILABILITY;
    }

    public String getBID() {
        return bid;
    }

    public void setBID(String BID) {
        this.bid = BID;
    }

    public String getBOOKNAME() {
        return bookName;
    }

    public void setBOOKNAME(String BOOKNAME) {
        this.bookName = BOOKNAME;
    }

    public String getAUTHOR() {
        return author;
    }

    public void setAUTHOR(String AUTHOR) {
        this.author = AUTHOR;
    }

    public String getPRICE() {
        return price;
    }

    public void setPRICE(String PRICE) {
        this.price = PRICE;
    }

    public String getISBN() {
        return isbn;
    }

    public void setISBN(String ISBN) {
        this.isbn = ISBN;
    }

    public String getDESCRIPTION() {
        return description;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.description = DESCRIPTION;
    }

    public String getAVAILABILITY() {
        return availability;
    }

    public void setAVAILABILITY(String AVAILABILITY) {
        this.availability = AVAILABILITY;
    }
    
}
