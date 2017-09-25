/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DBConnection.DBConnection;
import Model.UserModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.naming.spi.DirStateFactory;

/**
 *
 * @author Sameera
 */
public class UserController {

    public static String login(UserModel userModel) throws
            ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        String sql = "SELECT * FROM USERS WHERE email = '" + userModel.getEmail()
                + "' AND password = '" + userModel.getPassword() + "'";

        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        ResultSet result = prepareStatement.executeQuery();
        String email = "email";
        String password = "password";
        String userType = "userType";

        if (result.next()) {
            if (userModel.getEmail().equals(result.getString(email))
                    && userModel.getPassword().equals(result.getString(password))) {
//                if(result.getString(userType) == "Librarian"){
//                    //view librarian's UI
//                }
//                else if(result.getString(userType) == "Manager"){
//                    //view Manager's UI
//                }
//                else if(result.getString(userType) == "Member"){
//                    //view Member's UI
//                }
                return result.getString(userType);
            }
        }

        return "";
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX
            = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}
