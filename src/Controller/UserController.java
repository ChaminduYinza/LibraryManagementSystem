/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.PaymentModel;
import Util.DBConnection;
import Model.UserModel;
import Model.WarnedListModel;
import Util.Config;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;

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
    public static boolean addUser(UserModel uModel) throws ClassNotFoundException, SQLException, IOException {

        String sql = "INSERT INTO USERS(UID,USERTYPE,FIRSTNAME,LASTNAME,NIC,DOB,ADDRESS,CONTACT,EMAIL,PASSWORD,MAXIMUMBOOKCOUNT,BORROWEDBOOKCOUNT,REGDATE,EXPIRYDATE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection connection = DBConnection.getDBConnection().getConnection();
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
        preparestatement.setString(11, uModel.getMaxBook());
        preparestatement.setString(12, uModel.getBorrowedBook());
        preparestatement.setString(13, uModel.getRegDate());
        preparestatement.setString(14, uModel.getExpiryDate());

        return preparestatement.executeUpdate() > 0;
    }

    //this will return all the users in the system
    public static ArrayList getAllUsers() throws ClassNotFoundException, SQLException, IOException {

        ArrayList<UserModel> userList = new ArrayList<UserModel>();

        String sql = "SELECT * FROM USERS";
        Connection connection = DBConnection.getDBConnection().getConnection();
        Statement createstatement = connection.createStatement();
        ResultSet result = createstatement.executeQuery(sql);

        while (result.next()) {

            UserModel objUserModel = new UserModel(result.getString("uId"), result.getString("password"),
                    result.getString("firstName"), result.getString("lastName"),
                    result.getString("nic"), result.getString("dob"),
                    result.getString("address"), result.getString("contact"),
                    result.getString("email"), result.getString("userType"),
                    result.getString("MaximumBookCount"), result.getString("BorrowedBookCount"),
                    result.getString("regDate"), result.getString("expiryDate"));

            userList.add(objUserModel);

        }
        return userList;
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
    
    public static boolean isExpiredUser(String UID) throws ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        String sql = Config.getXMLData("query", "IsExpiredUser");
        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        prepareStatement.setString(1, UID);
        ResultSet result = prepareStatement.executeQuery();
        return result.next();

    }
    
     public static boolean deleteUser(String UID) throws ClassNotFoundException, SQLException, IOException {

        String sql = " DELETE  FROM USERS WHERE UID = ?";
        Connection connection = DBConnection.getDBConnection().getConnection();
        PreparedStatement preparestatement = connection.prepareStatement(sql);
        preparestatement.setString(1, UID);

        return preparestatement.executeUpdate() > 0;
    }

    public static String autoGenerateUserID() throws ClassNotFoundException, SQLException, IOException {

        String UID = null;
        String sql = "SELECT UID  FROM USERS ORDER BY UID DESC";
        Connection connection = DBConnection.getDBConnection().getConnection();
        Statement stmt = connection.createStatement();
        ResultSet userTable = stmt.executeQuery(sql);
        if (userTable.next() == true) {

            UID = userTable.getString("UID");

        }
        if (UID != null) {
            StringTokenizer st = new StringTokenizer(UID, "U");

            int num = Integer.parseInt(st.nextToken()) + 1;
            if (num > 999) {
                return "U" + Integer.toString(num + 10000).substring(1);
            } else if (num > 9999) {
                return "U" + Integer.toString(num + 100000).substring(1);
            } else {
                return "U" + Integer.toString(num + 1000).substring(1);
            }
        } else {
            return "U001";
        }

    }

    public static ArrayList getAllMembers() throws ClassNotFoundException, SQLException, IOException {

        ArrayList<UserModel> userList = new ArrayList<UserModel>();

        String sql = "SELECT * FROM USERS WHERE USERTYPE='Member'";
        Connection connection = DBConnection.getDBConnection().getConnection();
        Statement createstatement = connection.createStatement();
        ResultSet result = createstatement.executeQuery(sql);

        while (result.next()) {

            UserModel objUserModel = new UserModel(result.getString("uId"), result.getString("password"),
                    result.getString("firstName"), result.getString("lastName"),
                    result.getString("nic"), result.getString("dob"),
                    result.getString("address"), result.getString("contact"),
                    result.getString("email"), result.getString("userType"),
                    result.getString("MaximumBookCount"), result.getString("BorrowedBookCount"),
                    result.getString("regDate"), result.getString("expiryDate"));

            userList.add(objUserModel);

        }
        return userList;
    }

    public static boolean addWarnsToUsers(WarnedListModel wModel) throws ClassNotFoundException, SQLException, IOException {

        String sql = "INSERT INTO WARNEDLIST(WID,UID,FULLNAME,DESCRIPTION,WARNINGTYPE) VALUES (?,?,?,?,?)";
        Connection connection = DBConnection.getDBConnection().getConnection();
        PreparedStatement preparestatement = connection.prepareStatement(sql);
        preparestatement.setString(1, wModel.getWid());
        preparestatement.setString(2, wModel.getUid());
        preparestatement.setString(3, wModel.getFullName());
        preparestatement.setString(4, wModel.getDescription());
        preparestatement.setString(5, wModel.getWarningType());

        return preparestatement.executeUpdate() > 0;
    }

    public static boolean addPayments(PaymentModel pModel) throws ClassNotFoundException, SQLException, IOException {

        String sql = "INSERT INTO PAYMENT(PID,UID,PAYMENTTYPE,AMOUNT) VALUES (?,?,?,?)";
        Connection connection = DBConnection.getDBConnection().getConnection();
        PreparedStatement preparestatement = connection.prepareStatement(sql);
        preparestatement.setString(1, pModel.getpId());
        preparestatement.setString(2, pModel.getuId());
        preparestatement.setString(3, pModel.getPaymentType());
        preparestatement.setString(4, pModel.getAmount().toString());

        return preparestatement.executeUpdate() > 0;
    }

    public static String autoGenerateWarnID() throws ClassNotFoundException, SQLException, IOException {

        String WID = null;
        String sql = "SELECT WID  FROM WARNEDLIST ORDER BY WID DESC";
        Connection connection = DBConnection.getDBConnection().getConnection();
        Statement stmt = connection.createStatement();
        ResultSet userTable = stmt.executeQuery(sql);
        if (userTable.next() == true) {

            WID = userTable.getString("WID");

        }
        if (WID != null) {
            StringTokenizer st = new StringTokenizer(WID, "W");

            int num = Integer.parseInt(st.nextToken()) + 1;
            if (num > 999) {
                return "W" + Integer.toString(num + 10000).substring(1);
            } else if (num > 9999) {
                return "W" + Integer.toString(num + 100000).substring(1);
            } else {
                return "W" + Integer.toString(num + 1000).substring(1);
            }
        } else {
            return "U001";
        }

    }

    public static String autoGeneratePayID() throws ClassNotFoundException, SQLException, IOException {

        String WID = null;
        String sql = "SELECT PID  FROM PAYMENT ORDER BY PID DESC";
        Connection connection = DBConnection.getDBConnection().getConnection();
        Statement stmt = connection.createStatement();
        ResultSet userTable = stmt.executeQuery(sql);
        if (userTable.next() == true) {

            WID = userTable.getString("PID");

        }
        if (WID != null) {
            StringTokenizer st = new StringTokenizer(WID, "P");

            int num = Integer.parseInt(st.nextToken()) + 1;
            if (num > 999) {
                return "P" + Integer.toString(num + 10000).substring(1);
            } else if (num > 9999) {
                return "P" + Integer.toString(num + 100000).substring(1);
            } else {
                return "P" + Integer.toString(num + 1000).substring(1);
            }
        } else {
            return "P001";
        }

    }

    public static boolean updateExpireDate(String newDate, String uid) throws ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        String sql = "UPDATE  USERS SET EXPIRYDATE = ?"
                + " WHERE UID = ?";
        PreparedStatement prepareStatement = connection.prepareStatement(sql);

        prepareStatement.setString(1, newDate);
        prepareStatement.setString(2, uid);

        return prepareStatement.executeUpdate() > 0;

    }

    public static boolean updateLateFineStatus(String status, String uid) throws ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        String sql = "UPDATE  FINES SET ISPAID = ?"
                + " WHERE UID = ?";
        PreparedStatement prepareStatement = connection.prepareStatement(sql);

        prepareStatement.setString(1, status);
        prepareStatement.setString(2, uid);

        return prepareStatement.executeUpdate() > 0;

    }

    public static boolean updateUserStatus(String status, String uid) throws ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        String sql = "UPDATE  USERS SET STATUS = ?"
                + " WHERE UID = ?";
        PreparedStatement prepareStatement = connection.prepareStatement(sql);

        prepareStatement.setString(1, status);
        prepareStatement.setString(2, uid);

        return prepareStatement.executeUpdate() > 0;

    }

    public static ResultSet getAllPayUsers() throws ClassNotFoundException, SQLException, IOException {

        String sql = "SELECT  UID,FIRSTNAME,LASTNAME,USERTYPE,STATUS FROM USERS";
        Connection connection = DBConnection.getDBConnection().getConnection();
        Statement createstatement = connection.createStatement();
        ResultSet result = createstatement.executeQuery(sql);

        if (result != null) {
            return result;
        } else {
            return null;
        }

    }


}
