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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.naming.spi.DirStateFactory;

/**
 *
 * @author Sameera
 */
public class UserController {

    static Connection connection;
    
    

    public UserController(){


    }

    public static String login(UserModel userModel) throws
            ClassNotFoundException, SQLException {

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

    public static boolean addUser(UserModel uModel) throws ClassNotFoundException, SQLException {

        String sql = "INSERT INTO USERS(UID,USERTYPE,FIRSTNAME,LASTNAME,NIC,DOB,ADDRESS,CONTACT,EMAIL,PASSWORD,REGDATE,EXPIRYDATE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparestatement = connection.prepareStatement(sql);
        preparestatement.setString(1, uModel.getUid());
        preparestatement.setString(2, uModel.getUserType());
        preparestatement.setString(3, uModel.getFirstName());
        preparestatement.setString(4, uModel.getLastName());
        preparestatement.setString(5, uModel.getNic());
        preparestatement.setString(6, uModel.getDob());
        preparestatement.setString(7, uModel.getAddress());
        preparestatement.setString(8, uModel.getContact());
        preparestatement.setString(9, uModel.getEmail());
        preparestatement.setString(10, uModel.getPassword());
        preparestatement.setString(11, uModel.getRegDate());
        preparestatement.setString(12, uModel.getExpiryDate());

        return preparestatement.executeUpdate() > 0;
    }

    public static UserModel getAllUsers() throws ClassNotFoundException, SQLException {

        String sql = "SELECT * FROM USERS";
        Statement createstatement = connection.createStatement();
        ResultSet result = createstatement.executeQuery(sql);

        if (result.next()) {

            UserModel objUserModel = new UserModel(result.getString("uId"), result.getString("password"),
                    result.getString("firstName"), result.getString("lastName"),
                    result.getString("nic"), result.getString("dob"),
                    result.getString("address"), result.getString("contact"),
                    result.getString("email"), result.getString("userType"),
                    result.getString("regDate"), result.getString("expiryDate"));

            return objUserModel;
        } else {
            return null;
        }

    }

    public static boolean deleteUser(String UID) throws ClassNotFoundException, SQLException {

        String sql = "SELECT * FROM USERS WHERE UID = ?";
        PreparedStatement preparestatement = connection.prepareStatement(sql);
        preparestatement.setString(1, UID);

        return preparestatement.executeUpdate() > 0;
    }

}
