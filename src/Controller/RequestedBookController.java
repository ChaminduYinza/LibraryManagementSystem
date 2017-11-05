/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Util.DBConnection;
import Model.RequestedBookListModel;
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
 * @author Sameera
 */
public class RequestedBookController {
    
    //this method will add book request comming from the member
    public static boolean addBookRequest(RequestedBookListModel bRequestModel)
            throws ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();

        ResultSet rst;

        String sql = Config.getXMLData("query", "AddBookRequestByMember");

        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        prepareStatement.setString(1, bRequestModel.getrId());
        prepareStatement.setString(2, bRequestModel.getuId());
        prepareStatement.setString(3, bRequestModel.getbId());
        prepareStatement.setString(4, bRequestModel.getBookName());

        //this part will check for availability of the book and substract quantity
        //by 1 after requesting the book from the library
        if (prepareStatement.executeUpdate() > 0) {
            sql = Config.getXMLData("query", "SelectQuantityByMember");
            prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setString(1, bRequestModel.getbId());
            rst = prepareStatement.executeQuery();
            int quantity;
            String qty = "";

            if (rst.next() == true) {
                quantity = Integer.parseInt(rst.getString("QUANTITY")) - 1;
                qty = Integer.toString(quantity);
                sql = Config.getXMLData("query", "UpdateQuantityByMember");
                prepareStatement = connection.prepareStatement(sql);
                prepareStatement.setString(1, qty);
                prepareStatement.setString(2, bRequestModel.getbId());

                return prepareStatement.executeUpdate() > 0;
            }

        }
        return false;
    }

    //this method will generate requested book ID by adding 1 to the last ID
    public static String autoGenerateRequestedBookID() throws ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();

        String sqlGet = Config.getXMLData("query", "SelectAutoGenRequestedBookID");
        Statement createStatement = connection.createStatement();
        ResultSet rst = createStatement.executeQuery(sqlGet);
        String rID = null;

        if (rst.next() == true) {

            rID = rst.getString("RID");

        }
        if (rID != null) {
            StringTokenizer st = new StringTokenizer(rID, "R");

            int num = Integer.parseInt(st.nextToken()) + 1;
            if (num > 999) {
                return "R" + Integer.toString(num + 10000).substring(1);
            } else if (num > 9999) {
                return "R" + Integer.toString(num + 100000).substring(1);
            } else {
                return "R" + Integer.toString(num + 1000).substring(1);
            }
        } else {
            return "R001";
        }
    }

    //this method will cancel existing book request
    public static boolean cancelBookRequest(RequestedBookListModel requestedBookList)
            throws ClassNotFoundException, SQLException, IOException {

        Connection connection = DBConnection.getDBConnection().getConnection();

        ResultSet rst;

        String sql = Config.getXMLData("query", "DeleteBookRequestByMember");
        
        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        prepareStatement.setString(1, requestedBookList.getrId());
        prepareStatement.setString(2, requestedBookList.getuId());
        prepareStatement.setString(3, requestedBookList.getbId());

        if (prepareStatement.executeUpdate() > 0) {
            sql = Config.getXMLData("query", "SelectQuantityByMember");
            prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setString(1, requestedBookList.getbId());
            rst = prepareStatement.executeQuery();
            int quantity;
            String qty = "";

            if (rst.next() == true) {
                quantity = Integer.parseInt(rst.getString("QUANTITY")) + 1;
                qty = Integer.toString(quantity);
                sql = Config.getXMLData("query", "UpdateQuantityByMember");
                prepareStatement = connection.prepareStatement(sql);
                prepareStatement.setString(1, qty);
                prepareStatement.setString(2, requestedBookList.getbId());
                return prepareStatement.executeUpdate() > 0;
            }

        }
        return false;

    }
}
