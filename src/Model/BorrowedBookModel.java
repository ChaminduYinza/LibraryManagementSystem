/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Date;

/**
 *
 * @author Sameera
 */
public class BorrowedBookModel {

    String bbid;
    String uid;
    String bid;
    String issueDate;
    Date renewDate;

    public BorrowedBookModel(String bbIdl, String uId, String bId, String issueDate, Date renewDate) {
        this.bbid = bbIdl;
        this.uid = uId;
        this.bid = bId;
        this.issueDate = issueDate;
        this.renewDate = renewDate;
    }

    public String getBbIdl() {
        return bbid;
    }

    public String getuId() {
        return uid;
    }

    public String getbId() {
        return bid;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public Date getRenewDate() {
        return renewDate;
    }

    public void setBbIdl(String bbIdl) {
        this.bbid = bbIdl;
    }

    public void setuId(String uId) {
        this.uid = uId;
    }

    public void setbId(String bId) {
        this.bid = bId;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public void setRenewDate(Date renewDate) {
        this.renewDate = renewDate;
    }

}
