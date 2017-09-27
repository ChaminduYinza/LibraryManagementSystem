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
import java.util.StringTokenizer;

/**
 *
 * @author Y`inza
 */
public class BookController {

    public static boolean addBook(BookModel bModel) throws ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        String sql = "INSERT INTO BOOKS(BID,BOOKNAME,AUTHOR,PRICE,ISBN,DESCRIPTION,AVAILABILITY) VALUES (?,?,?,?,?,?,?)";
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

    public static BookModel searchBookByISBN(String ISBN) throws ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        String sql = "SELECT * FROM BOOKS WHERE ISBN = '" + ISBN + "'";
        Statement createStatement = connection.createStatement();
        ResultSet rst = createStatement.executeQuery(sql);
        if (rst.next()) {
            BookModel objBookModel = new BookModel(rst.getString("BID"), rst.getString("BOOKNAME"),
                    rst.getString("AUTHOR"), rst.getString("PRICE"), rst.getString("ISBN"), rst.getString("DESCRIPTION"), rst.getString("AVAILABILITY"));
            return objBookModel;
        } else {
            return null;
        }
    }

    public static BookModel getAllBooks() throws ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        String sql = "SELECT * FROM BOOKS";
        Statement createStatement = connection.createStatement();
        ResultSet rst = createStatement.executeQuery(sql);
        if (rst.next()) {
            BookModel objBookModel = new BookModel(rst.getString("BID"), rst.getString("BOOKNAME"),
                    rst.getString("AUTHOR"), rst.getString("PRICE"), rst.getString("ISBN"), rst.getString("DESCRIPTION"), rst.getString("AVAILABILITY"));
            return objBookModel;
        } else {
            return null;
        }
    }

    public static String deleteBookByISBN(String ISBN) throws ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        String sql = "SELECT * FROM BOOKS WHERE ISBN = '" + ISBN + "'";
        Statement createstatement = connection.createStatement();
        ResultSet rst = createstatement.executeQuery(sql);
        if (rst.next()) {
            sql = "DELETE * FROM BOOKS WHERE ISBN = '" + ISBN + "'";
            createstatement.executeQuery(sql);
            createstatement.executeUpdate(sql);
            return "Successfully deleted the book";
        } else {
            return "No records found for the given ISBN";
        }
    }

    public static String updateBookByISBN(String ISBN) throws ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        String sql = "UPDATE0. INTO BOOKS(BID,BOOKNAME,AUTHOR,PRICE,ISBN,DESCRIPTION,AVAILABILITY) VALUES (?,?,?,?,?,?,?)";
        Statement createstatement = connection.createStatement();
        ResultSet rst = createstatement.executeQuery(sql);
        if (rst.next()) {
            sql = "DELETE * FROM BOOKS WHERE ISBN = '" + ISBN + "'";
            createstatement.executeQuery(sql);
            createstatement.executeUpdate(sql);
            return "Successfully deleted the book";
        } else {
            return "No records found for the given ISBN";
        }
    }

    public static String autoOrderId() throws ClassNotFoundException, SQLException {

        String BID = null;
        String sql = "SELECT BID  FROM BOOKS ORDER BY BID DESC";
        Connection connection = DBConnection.getDBConnection().getConnection();
        Statement stmt = connection.createStatement();
        ResultSet bookTable = stmt.executeQuery(sql);
        if (bookTable.next() == true) {

            BID = bookTable.getString("BID");

        }
        if (BID != null) {
            StringTokenizer st = new StringTokenizer(BID, "B");

            int num = Integer.parseInt(st.nextToken()) + 1;
            if (num > 999) {
                return "B" + Integer.toString(num + 10000).substring(1);
            } else if (num > 9999) {
                return "B" + Integer.toString(num + 100000).substring(1);
            } else {
                return "B" + Integer.toString(num + 1000).substring(1);
            }
        } else {
            return "B001";
        }

    }

}
