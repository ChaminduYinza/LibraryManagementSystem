/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import static Controller.UserController.connection;
import Model.BookFeedbackModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author user
 */
public class FeedbackController {

    public static boolean addFeedback(BookFeedbackModel bookfeedbackModel) throws ClassNotFoundException, SQLException {

        String sql = "INSERT INTO bookfeedbacks(FID,BID,FEDDBACK,FULLNAME) VALUES (?,?,?,?,?)";
        PreparedStatement preparestatement = connection.prepareStatement(sql);
        preparestatement.setString(1, bookfeedbackModel.getfId());
        preparestatement.setString(2, bookfeedbackModel.getbId());
        preparestatement.setString(3, bookfeedbackModel.getFullName());

        return preparestatement.executeUpdate() > 0;
    }

    public static BookFeedbackModel getAllFeedBacks() throws ClassNotFoundException, SQLException {

        String sql = "SELECT * FROM bookfeedbacks";
        Statement createstatement = connection.createStatement();
        ResultSet result = createstatement.executeQuery(sql);

        if (result.next()) {

            BookFeedbackModel objUserModel = new BookFeedbackModel(result.getString("FID"), result.getString("BID"),
                    result.getString("FEDDBACK"),result.getString("FULLNAME"));

            return objUserModel;
        } else {
            return null;
        }

    }

    public static boolean deleteFeedBack(String FID) throws ClassNotFoundException, SQLException {

        String sql = "SELECT * FROM bookfeedbacks WHERE FID = ?";
        PreparedStatement preparestatement = connection.prepareStatement(sql);
        preparestatement.setString(1, FID);

        return preparestatement.executeUpdate() > 0;
    }

}
