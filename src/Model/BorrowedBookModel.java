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
public class BorrowedBookModel {

    String bbIdl;
    String uId;
    String bId;
    String issueDate;
    String renewDate;

    public BorrowedBookModel(String bbIdl, String uId, String bId, String issueDate, String renewDate) {
        this.bbIdl = bbIdl;
        this.uId = uId;
        this.bId = bId;
        this.issueDate = issueDate;
        this.renewDate = renewDate;
    }

    public String getBbIdl() {
        return bbIdl;
    }

    public String getuId() {
        return uId;
    }

    public String getbId() {
        return bId;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public String getRenewDate() {
        return renewDate;
    }

    public void setBbIdl(String bbIdl) {
        this.bbIdl = bbIdl;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public void setRenewDate(String renewDate) {
        this.renewDate = renewDate;
    }

}
