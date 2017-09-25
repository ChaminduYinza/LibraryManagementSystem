/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Sameera
 */
public class RequestedBookListModel {

    String rId;
    String uId;
    String bId;
    String bookName;

    public RequestedBookListModel(String rId, String uId, String bId, String bookName) {
        this.rId = rId;
        this.uId = uId;
        this.bId = bId;
        this.bookName = bookName;
    }

    public String getrId() {
        return rId;
    }

    public String getuId() {
        return uId;
    }

    public String getbId() {
        return bId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

}
