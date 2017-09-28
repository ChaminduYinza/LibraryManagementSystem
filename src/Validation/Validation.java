/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Validation;

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
}
