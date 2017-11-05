/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import static Controller.UserController.connection;
import Util.DBConnection;
import Model.BookFeedbackModel;
import Util.Config;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

/**
 *
 * @author Sameera
 */
public class FeedbackController {

    public static final Logger LOG = Config.getLogger(FeedbackController.class);
    
//    To add feedback this method will be called
    public static boolean addFeedback(BookFeedbackModel bookfeedbackModel)
            throws ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();

        String sql = Config.getXMLData("query", "AddFeedbackInsert");
        PreparedStatement preparestatement = connection.prepareStatement(sql);
        preparestatement.setString(1, bookfeedbackModel.getfId());
        preparestatement.setString(2, bookfeedbackModel.getbId());
        preparestatement.setString(3, bookfeedbackModel.getFeedback());
        preparestatement.setString(4, "0");
        preparestatement.setString(5, bookfeedbackModel.getFullName());

        return preparestatement.executeUpdate() > 0;
    }

    //This method will generate feedback ID by adding one to existing ID
    public static String autoGenerateFeedbackID()
            throws ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();

        String sqlGet = Config.getXMLData("query", "FeedbackAutoGenIDSelect");;
        Statement createStatement = connection.createStatement();
        ResultSet rst = createStatement.executeQuery(sqlGet);
        String fID = null;

        if (rst.next() == true) {
            fID = rst.getString("FID");
        }
        if (fID != null) {
            StringTokenizer st = new StringTokenizer(fID, "F");

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

    public static BookFeedbackModel getAllFeedBacks() throws ClassNotFoundException, SQLException {

        String sql = Config.getXMLData("query", "GetAllFeedBacks");
        Statement createstatement = connection.createStatement();
        ResultSet result = createstatement.executeQuery(sql);

        if (result.next()) {

            BookFeedbackModel objUserModel = new BookFeedbackModel(result.getString("FID"), result.getString("BID"),
                    result.getString("FEDDBACK"), result.getString("FULLNAME"));

            return objUserModel;
        } else {
            LOG.info("No feedbacks available");
            return null;
        }

    }

    public static boolean deleteFeedBack(String FID) throws ClassNotFoundException, SQLException {

        String sql = Config.getXMLData("query", "DeleteFeedBackByFID");
        PreparedStatement preparestatement = connection.prepareStatement(sql);
        preparestatement.setString(1, FID);

        return preparestatement.executeUpdate() > 0;
    }
}
