/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DBConnection.DBConnection;
import Model.BookModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Y`inza
 */
public class bookController {

    public static boolean addBook(BookModel bModel) throws ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        String sql = "INSERT INTO orders(BID,BOOKNAME,AUTHOR,PRICE,OSBN,DESCRIPTION,AVAILABILITY) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        prepareStatement.setString(1, bModel.getbId());
        prepareStatement.setString(2, bModel.getBookName());
        prepareStatement.setString(3, bModel.getAuthor());
        prepareStatement.setString(4, bModel.getPrice());
        prepareStatement.setString(5, bModel.getIsbn());
        prepareStatement.setString(6, bModel.getDescription());
        prepareStatement.setString(7, bModel.getAvailability());

        return prepareStatement.executeUpdate() > 0;

    }

}
