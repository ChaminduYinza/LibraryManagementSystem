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

    String rid;
    String uid;
    String bid;
    String bookName;

    public RequestedBookListModel(String rId, String uId, String bId, String bookName) {
        this.rid = rId;
        this.uid = uId;
        this.bid = bId;
        this.bookName = bookName;
    }

    public String getrId() {
        return rid;
    }

    public String getuId() {
        return uid;
    }

    public String getbId() {
        return bid;
    }

    public String getBookName() {
        return bookName;
    }

    public void setrId(String rId) {
        this.rid = rId;
    }

    public void setuId(String uId) {
        this.uid = uId;
    }

    public void setbId(String bId) {
        this.bid = bId;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

}
