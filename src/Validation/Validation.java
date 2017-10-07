/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author rpa02
 */
public class Validation {

    final static String DOUBLE_REGEXP = "[0-9]+([.][0-9]{1,2})?";

    public static boolean isLetters(String Paramter) {

        if (!Paramter.isEmpty()) {
            for (int i = 0; i < Paramter.length(); i++) {
                if (!Character.isLetter(Paramter.charAt(i))) {
                    return false;

                }
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNumbers(String Paramter) {

        if (!Paramter.isEmpty()) {
            for (int i = 0; i < Paramter.length(); i++) {
                if (!Character.isDigit(Paramter.charAt(i))) {
                    return false;

                }
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNIC(String NIC) {

        return NIC.length() == 10 && isNumbers(NIC.substring(0, NIC.length() - 1)) && "V".equals(NIC.substring(9).toUpperCase());

    }

    public static boolean isISBN(String ISBN) {

        return (!ISBN.isEmpty() && (ISBN.length() == 13 || ISBN.length() == 10)) && isNumbers(ISBN);
    }

    public static boolean isDouble(String value) {

        return (!value.isEmpty() && value.matches(DOUBLE_REGEXP));

    }
    
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX
            = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

}
