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
public class PaymentModel {

    String pid;
    String uid;
    String paymentType;
    Double amount;
    int noOfDays;

    public PaymentModel(String pId, String uId, String paymentType, Double amount) {
        this.pid = pId;
        this.uid = uId;
        this.paymentType = paymentType;
        this.amount = amount;
//        this.noOfDays = noOfDays;
    }

    public String getpId() {
        return pid;
    }

    public String getuId() {
        return uid;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public Double getAmount() {
        return amount;
    }

//    public int getNoOfDays() {
//        return noOfDays;
//    }

    public void setpId(String pId) {
        this.pid = pId;
    }

    public void setuId(String uId) {
        this.uid = uId;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

//    public void setNoOfDays(int noOfDays) {
//        this.noOfDays = noOfDays;
//    }

}
