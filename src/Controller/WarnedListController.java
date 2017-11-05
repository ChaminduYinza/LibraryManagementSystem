/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Util.DBConnection;
import Model.WarnedListModel;
import Util.Config;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

/**
 *
 * @author rpa02
 */
public class WarnedListController {

    public static String autoGenerateWarnedListID() throws ClassNotFoundException, SQLException, IOException {

        String WID = null;
        String sql = Config.getXMLData("query", "AutoGenerateWarnedListID");
        Connection connection = DBConnection.getDBConnection().getConnection();
        Statement stmt = connection.createStatement();
        ResultSet bookTable = stmt.executeQuery(sql);
        if (bookTable.next() == true) {

            WID = bookTable.getString("WID");

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
            return "W001";
        }

    }

    // }
    public static boolean addToWarnedList(WarnedListModel warn) throws ClassNotFoundException, SQLException, IOException {

        Connection connection = DBConnection.getDBConnection().getConnection();
        String insertQuery = Config.getXMLData("query", "AddToWarnedList");
        PreparedStatement prepareStatement = connection.prepareStatement(insertQuery);
        prepareStatement.setString(1, warn.getWid());
        prepareStatement.setString(2, warn.getUid());
        prepareStatement.setString(3, warn.getFullName());
        prepareStatement.setString(4, warn.getDescription());
        prepareStatement.setString(5, warn.getWarningType());

        return (prepareStatement.executeUpdate() > 0);

    }

}
