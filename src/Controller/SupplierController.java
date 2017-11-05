/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.SupplierModel;
import Util.Config;
import Util.DBConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

/**
 *
 * @author Y`inza
 */
public class SupplierController {

    static PreparedStatement PreparedStatement = null;

    public static boolean addSupplier(SupplierModel supplier) throws ClassNotFoundException, SQLException, IOException {

        Connection connection = DBConnection.getDBConnection().getConnection();

        String sql = Config.getXMLData("query", "AddSupplier");

        PreparedStatement = connection.prepareStatement(sql);
        PreparedStatement.setString(1, supplier.getSid());
        PreparedStatement.setString(2, supplier.getCompany());
        PreparedStatement.setString(3, supplier.getAddress());
        PreparedStatement.setInt(4, supplier.getMobile());

        return (PreparedStatement.executeUpdate() > 0);

    }

    public static SupplierModel getSupplierBySID(String SID) throws ClassNotFoundException, SQLException, IOException {

        Connection connection = DBConnection.getDBConnection().getConnection();

        String sql = Config.getXMLData("query", "GetSupplierBySID");

        PreparedStatement = connection.prepareStatement(sql);
        PreparedStatement.setString(1, SID);
        ResultSet rst = PreparedStatement.executeQuery();
        if (rst.next()) {
            SupplierModel supplier = new SupplierModel(rst.getString("SID"), rst.getString("COMPANY"), rst.getString("ADDRESS"), Integer.parseInt(rst.getString("MOBILE")));
            return supplier;
        }

        return null;

    }

    public static boolean deleteSupplier(String SID) throws ClassNotFoundException, SQLException, IOException {

        if (getSupplierBySID(SID) == null) {
            return false;
        } else {
            Connection connection = DBConnection.getDBConnection().getConnection();

            String sql = Config.getXMLData("query", "DeleteSupplier");

            PreparedStatement = connection.prepareStatement(sql);
            PreparedStatement.setString(1, SID);

            return (PreparedStatement.executeUpdate() > 0);

        }
    }

    public static boolean editSupplier(SupplierModel Supplier) throws ClassNotFoundException, SQLException, IOException {

        if (getSupplierBySID(Supplier.getSid()) == null) {
            return false;
        } else {
            Connection connection = DBConnection.getDBConnection().getConnection();

            String sql = Config.getXMLData("query", "EditSupplier");

            PreparedStatement = connection.prepareStatement(sql);
            PreparedStatement.setString(1, Supplier.getCompany());
            PreparedStatement.setString(2, Supplier.getAddress());
            PreparedStatement.setString(3, Integer.toString(Supplier.getMobile()));
            PreparedStatement.setString(4, Supplier.getSid());

            return (PreparedStatement.executeUpdate() > 0);

        }
    }

    public static String autoGenerateSupplierID() throws ClassNotFoundException, SQLException, IOException {

        String SID = null;
        String sql = Config.getXMLData("query", "AutoGenerateSupplierID");
        Connection connection = DBConnection.getDBConnection().getConnection();
        Statement stmt = connection.createStatement();
        ResultSet bookTable = stmt.executeQuery(sql);
        if (bookTable.next() == true) {

            SID = bookTable.getString("SID");

        }
        if (SID != null) {
            StringTokenizer st = new StringTokenizer(SID, "S");

            int num = Integer.parseInt(st.nextToken()) + 1;
            if (num > 999) {
                return "S" + Integer.toString(num + 10000).substring(1);
            } else if (num > 9999) {
                return "S" + Integer.toString(num + 100000).substring(1);
            } else {
                return "S" + Integer.toString(num + 1000).substring(1);
            }
        } else {
            return "S001";
        }

    }

}
