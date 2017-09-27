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

    String pId;
    String uId;
    String paymentType;
    Double amount;
    int noOfDays;

    public PaymentModel(String pId, String uId, String paymentType, Double amount, int noOfDays) {
        this.pId = pId;
        this.uId = uId;
        this.paymentType = paymentType;
        this.amount = amount;
        this.noOfDays = noOfDays;
    }

    public String getpId() {
        return pId;
    }

    public String getuId() {
        return uId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public Double getAmount() {
        return amount;
    }

    public int getNoOfDays() {
        return noOfDays;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }

}
