/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Util.DBConnection;
import Model.UserModel;
import Util.Config;
import java.io.IOException;
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

    public UserController() {

    }

    //this method will validate ligin
    public static UserModel login(UserModel userModel) throws
            ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        String sql = Config.getXMLData("query", "SelectLoginUserDetails");
        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        prepareStatement.setString(1, userModel.getEmail());
        prepareStatement.setString(2, userModel.getPassword());

        ResultSet result = prepareStatement.executeQuery();
        String email = "email";
        String password = "password";
        UserModel userAndID = new UserModel();

        if (result.next()) {
            if (userModel.getEmail().equals(result.getString(email))
                    && userModel.getPassword().equals(result.getString(password))) {
                userAndID.setUserType(result.getString("USERTYPE"));
                userAndID.setUid(result.getString("UID"));
                return userAndID;
            }
        }

        return null;
    }

    //this method will check current password with entered password when changing the password
    public static boolean checkPasswordforReset(UserModel userModel) throws
            ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        String sql = Config.getXMLData("query", "SelectForPasswordChange");

        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        prepareStatement.setString(1, userModel.getUid());
        prepareStatement.setString(2, userModel.getPassword());

        ResultSet result = prepareStatement.executeQuery();
        String uID = "UID";
        String password = "PASSWORD";

        if (result.next()) {
            if (userModel.getUid().equals(result.getString(uID))
                    && userModel.getPassword().equals(result.getString(password))) {
                return true;
            }
        }

        return false;
    }
    
    //this method will update new password
    public static boolean updatePassword(String uID, String password) 
            throws ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        String sql = Config.getXMLData("query", "UpdateUserPassword");
        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        prepareStatement.setString(1, password);
        prepareStatement.setString(2, uID);
        return prepareStatement.executeUpdate() > 0;
    }

    //this method will add user to the system
    public static boolean addUser(UserModel uModel)
            throws ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        String sql = Config.getXMLData("query", "AddUser");
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

    //this will return all the users in the system
    public static UserModel getAllUsers() throws ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        String sql = Config.getXMLData("query", "SelectAllUsers");
        Statement createstatement = connection.createStatement();
        ResultSet result = createstatement.executeQuery(sql);

        if (result.next()) {

            UserModel objUserModel = new UserModel(result.getString("uId"), result.getString("password"),
                    result.getString("firstName"), result.getString("lastName"),
                    result.getString("nic"), result.getString("dob"),
                    result.getString("address"), result.getString("contact"),
                    result.getString("email"), result.getString("userType"),
                    result.getString("MaximumBookCount"), result.getString("BorrowedBookCount"),
                    result.getString("regDate"), result.getString("expiryDate"));

            return objUserModel;
        } else {
            return null;
        }

    }

    //this method will return single user by user ID
    public static UserModel getUser(String uID) throws ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        String sql = Config.getXMLData("query", "SelectSingleUser");
        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        prepareStatement.setString(1, uID);
        ResultSet result = prepareStatement.executeQuery();

        if (result.next()) {
            UserModel objUserModel = new UserModel(result.getString("UID"), result.getString("PASSWORD"),
                    result.getString("FIRSTNAME"), result.getString("LASTNAME"),
                    result.getString("NIC"), result.getString("DOB"),
                    result.getString("ADDRESS"), result.getString("CONTACT"),
                    result.getString("EMAIL"), result.getString("USERTYPE"),
                    result.getString("MaximumBookCount"), result.getString("BorrowedBookCount"),
                    result.getString("REGDATE"), result.getString("EXPIRYDATE"));

            return objUserModel;
        } else {
            return null;
        }

    }

    //this method will update user's contact number
    public static boolean updateUser(String uID, String contact) throws ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        String sql = Config.getXMLData("query", "UpdateUser");
        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        prepareStatement.setString(1, contact);
        prepareStatement.setString(2, uID);
        return prepareStatement.executeUpdate() > 0;
    }
    
    //this method will update user's max book count
    public static boolean updateMaxBookCount(String uID, String maxBookCount) throws ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        String sql = Config.getXMLData("query", "updateMaxBookCount");
        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        prepareStatement.setString(1, maxBookCount);
        prepareStatement.setString(2, uID);
        return prepareStatement.executeUpdate() > 0;
    }

}
