/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Util.DBConnection;
import Model.FineModel;
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
public class FineController {

    public static boolean addToFine(FineModel fine) throws SQLException, ClassNotFoundException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        String insertQuery = Config.getXMLData("query", "AddToFine");
        PreparedStatement prepareStatement = connection.prepareStatement(insertQuery);
        prepareStatement.setString(1, fine.getFid());
        prepareStatement.setString(2, fine.getUid());
        prepareStatement.setString(3, fine.getReason());
        prepareStatement.setDouble(4, fine.getFine());
        prepareStatement.setInt(5, fine.getIsPaid());

        return (prepareStatement.executeUpdate() > 0);

    }

    public static String autoGenerateFineID() throws ClassNotFoundException, SQLException, IOException {

        String FID = null;
        String sql = Config.getXMLData("query", "AutoGenerateFineID");
        Connection connection = DBConnection.getDBConnection().getConnection();
        Statement stmt = connection.createStatement();
        ResultSet bookTable = stmt.executeQuery(sql);
        if (bookTable.next() == true) {

            FID = bookTable.getString("FID");

        }
        if (FID != null) {
            StringTokenizer st = new StringTokenizer(FID, "F");

            int num = Integer.parseInt(st.nextToken()) + 1;
            if (num > 999) {
                return "F" + Integer.toString(num + 10000).substring(1);
            } else if (num > 9999) {
                return "F" + Integer.toString(num + 100000).substring(1);
            } else {
                return "F" + Integer.toString(num + 1000).substring(1);
            }
        } else {
            return "F001";
        }

    }

}
