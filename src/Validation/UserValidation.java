/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Validation;

import Util.DBConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Rushan
 */
public class UserValidation {

    static Date SystemDate = new Date();

    public UserValidation() {

    }

    public static boolean vefName(String name) {
        boolean staticvef = false;

        for (int i = 0; i < name.length(); i++) {
            if (Character.isLetter(name.charAt(i))) {
                staticvef = true;

            } else {

                staticvef = false;
                break;
            }
        }
        if (staticvef == false) {
            JOptionPane.showMessageDialog(null, "Invalid Name", "Warning!", JOptionPane.ERROR_MESSAGE);
        }
        return staticvef;
    }

    public static boolean vefNIC(String nic) {
        boolean staticvef = false;
        if (nic.length() == 10) {

            if (nic.charAt(9) == 'V' || nic.charAt(9) == 'v') {
                staticvef = true;

                for (int i = 0; i < (nic.length() - 1); i++) {
                    if (Character.isDigit(nic.charAt(i))) {
                        staticvef = true;
                    } else {
                        staticvef = false;
                        break;
                    }
                }
            } else {
                staticvef = false;

            }

        }
        if (staticvef == false) {
            JOptionPane.showMessageDialog(null, "Invalid NIC", "Warning!", JOptionPane.ERROR_MESSAGE);
        }

        return staticvef;

    }

    public static boolean checkNIC(String nic) throws ClassNotFoundException, SQLException, IOException {

        String sql = "SELECT * FROM USERS WHERE NIC = '" + nic + "'";
        Connection connection = DBConnection.getDBConnection().getConnection();
        Statement createstatement = connection.createStatement();
        ResultSet result = createstatement.executeQuery(sql);

        if (result.next()) {
            return true;
        } else {

            return false;
        }
    }

    public static boolean checkEmail(String email) throws ClassNotFoundException, SQLException, IOException {

        String sql = "SELECT * FROM USERS WHERE EMAIL = '" + email + "'";
        Connection connection = DBConnection.getDBConnection().getConnection();
        Statement createstatement = connection.createStatement();
        ResultSet result = createstatement.executeQuery(sql);

        if (result.next()) {
            return true;
        } else {

            return false;
        }

    }
    
    public static boolean checkPhone(String phone) throws ClassNotFoundException, SQLException, IOException {

        String sql = "SELECT * FROM USERS WHERE CONTACT = '" + phone + "'";
        Connection connection = DBConnection.getDBConnection().getConnection();
        Statement createstatement = connection.createStatement();
        ResultSet result = createstatement.executeQuery(sql);

        if (result.next()) {
            return true;
        } else {

            return false;
        }

    }

    public static boolean vefPhone(String mphone) {
        boolean staticvef = false;
        if (mphone.length() == 10) {
            for (int i = 0; i < mphone.length(); i++) {
                if (Character.isDigit(mphone.charAt(i))) {
                    staticvef = true;
                } else {
                    staticvef = false;
                    JOptionPane.showMessageDialog(null, "Invalid Phone Number", "Warning!", JOptionPane.ERROR_MESSAGE);
                    break;
                }
            }

        } else {
            JOptionPane.showMessageDialog(null, "Invalid Phone NumberMan", "Warning!", JOptionPane.ERROR_MESSAGE);
            staticvef = false;

        }
        return staticvef;
    }

    public static boolean vefAddress(String address) {
        boolean staticvef = false;
        if (address.length() != 0 || Character.isDigit(address.charAt(0))) {
            staticvef = true;
        } else {
            JOptionPane.showMessageDialog(null, "Invalid Address", "Warning!", JOptionPane.ERROR_MESSAGE);

        }

        return staticvef;
    }

    public static boolean vefDOB(Date dob) {
        boolean staticvef = false;
        //if (dob != null && dob.compareTo(SystemDate) < 0) {
        if (dob != null) {
            if ((SystemDate.getYear() - dob.getYear()) >= 18) {
                if ((SystemDate.getYear() - dob.getYear()) <= 55) {
                    staticvef = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Employer must be younger than 55 years old", "Warning!", JOptionPane.ERROR_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(null, "Employer must be elder than 18 years old", "Warning!", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(null, "Invalid Birthdate", "Warning!", JOptionPane.ERROR_MESSAGE);

        }

        return staticvef;
    }

    public static boolean vefEndDate(Date date, Date startDate, Date dob) {
        boolean staticvef = false;
        if (date != null && (date.getDate() - startDate.getDate()) >= 1) {
            if (date.getYear() - dob.getYear() <= 55) {
                staticvef = true;
            } else {
                JOptionPane.showMessageDialog(null, "Invalid End Date \nEmployee will be elder than 55", "Warning!", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(null, "Invalid End Date", "Warning!", JOptionPane.ERROR_MESSAGE);

        }

        return staticvef;
    }

    public static boolean vefPassword(String pw1, String pw2) {
        boolean staticvef = false;
        boolean chkDigit = false;
        boolean chkLetter = false;

        if (pw1.length() >= 6) {
            if (pw1.equals(pw2)) {
                for (int i = 0; i < pw1.length(); i++) {
                    if (Character.isDigit(pw1.charAt(i))) {
                        chkDigit = true;
                    }
                    if (Character.isLetter(pw1.charAt(i))) {
                        chkLetter = true;
                    }
                }
                if (chkDigit == true && chkLetter == true) {
                    staticvef = true;

                } else {
                    JOptionPane.showMessageDialog(null, "Password must contain both letters and digits", "Warning!", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Password doesn't match", "Warning!", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Password must contain atleast 6 characters", "Warning!", JOptionPane.ERROR_MESSAGE);

        }

        return staticvef;
    }

    public static boolean validateEmail(String Email) {
        boolean at = false, dot = false, emailCheck;
        if (Email.length() > 5) {
            if (!Character.isDigit(Email.charAt(1))) {
                for (int i = 0; i < Email.length(); i++) {
                    if (Email.charAt(i) == '@') {
                        at = true;
                    } else if (Email.charAt(i) == '.') {
                        dot = true;
                    }

                }
            }
            emailCheck = false;
        } else {
            emailCheck = false;
        }

        if (dot == true) {
            if (at == true) {
                emailCheck = true;
                return emailCheck;
            } else {
                emailCheck = false;
                JOptionPane.showMessageDialog(null, "Invalid Email", "Warning!", JOptionPane.ERROR_MESSAGE);
                return emailCheck;
            }
        } else {
            emailCheck = false;
            JOptionPane.showMessageDialog(null, "Invalid Email", "Warning!", JOptionPane.ERROR_MESSAGE);
            return emailCheck;
        }
    }

}
