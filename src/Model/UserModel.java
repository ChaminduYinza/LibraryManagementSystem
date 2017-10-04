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
public class UserModel {
    
    String uId,password,firstName,lastName,nic,dob,address,contact,email,userType,regDate,expiryDate;

    public UserModel(String Uid, String password, String firstName, String lastName, String nic, String dob, String address, String contact, String email, String userType, String regDate, String expiryDate) {
        this.uId = Uid;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nic = nic;
        this.dob = dob;
        this.address = address;
        this.contact = contact;
        this.email = email;
        this.userType = userType;
        this.regDate = regDate;
        this.expiryDate = expiryDate;
    }

    public UserModel() {
        
    }

    public String getUid() {
        return uId;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNic() {
        return nic;
    }

    public String getDob() {
        return dob;
    }

    public String getAddress() {
        return address;
    }

    public String getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }

    public String getUserType() {
        return userType;
    }

    public String getRegDate() {
        return regDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setUid(String Uid) {
        this.uId = Uid;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    
    
}
