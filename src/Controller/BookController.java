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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Y`inza
 */
public class BookController {

    public static boolean addBook(BookModel bModel) throws ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        String sql = "INSERT INTO BOOKS(BID,BOOKNAME,AUTHOR,PRICE,ISBN,DESCRIPTION,AVAILABILITY) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement preparestatement = connection.prepareStatement(sql);
        preparestatement.setString(1, bModel.getBID());
        preparestatement.setString(2, bModel.getBOOKNAME());
        preparestatement.setString(3, bModel.getAUTHOR());
        preparestatement.setString(4, bModel.getPRICE());
        preparestatement.setString(5, bModel.getISBN());
        preparestatement.setString(6, bModel.getDESCRIPTION());
        preparestatement.setString(7, bModel.getAVAILABILITY());

        return preparestatement.executeUpdate() > 0;

    }

    public static boolean updateBook(BookModel bModel) throws ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        String sql = "UPDATE BOOKS SET BOOKNAME = ?, AUTHOR = ?, PRICE = ? , ISBN = ? , DESCRIPTION = ?, AVAILABILITY = ? WHERE BID = ?";
        PreparedStatement preparestatement = connection.prepareStatement(sql);

        preparestatement.setString(1, bModel.getBOOKNAME());
        preparestatement.setString(2, bModel.getAUTHOR());
        preparestatement.setString(3, bModel.getPRICE());
        preparestatement.setString(4, bModel.getISBN());
        preparestatement.setString(5, bModel.getDESCRIPTION());
        preparestatement.setString(6, bModel.getAVAILABILITY());
        preparestatement.setString(7, bModel.getBID());

        return preparestatement.executeUpdate() > 0;

    }

    public static BookModel searchBookByISBN(String ISBN) throws ClassNotFoundException, SQLException {
        Connection connecton = DBConnection.getDBConnection().getConnection();
        String sql = "SELECT * FROM BOOKS where ISBN = '" + ISBN + "'";
        Statement createStatement = connecton.createStatement();
        ResultSet rst = createStatement.executeQuery(sql);
        if (rst.next()) {
            BookModel bModel = new BookModel(rst.getString("BID"), rst.getString("BOOKNAME"),
                    rst.getString("AUTHOR"), rst.getString("PRICE"), rst.getString("ISBN"),
                    rst.getString("DESCRIPTION"), rst.getString("AVAILABILITY"));
            return bModel;
        } else {
            return null;
        }
    }

    public static BookModel getAllBookDetails() throws ClassNotFoundException, SQLException {
        Connection connecton = DBConnection.getDBConnection().getConnection();
        String sql = "SELECT * FROM BOOKS";
        Statement createStatement = connecton.createStatement();
        ResultSet rst = createStatement.executeQuery(sql);
        if (rst.next()) {
            BookModel bModel = new BookModel(rst.getString("BID"), rst.getString("BOOKNAME"),
                    rst.getString("AUTHOR"), rst.getString("PRICE"), rst.getString("ISBN"),
                    rst.getString("DESCRIPTION"), rst.getString("AVAILABILITY"));
            return bModel;
        } else {
            return null;
        }
    }

}
