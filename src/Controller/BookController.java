/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Util.DBConnection;
import Model.BookModel;
import Model.BorrowedBookModel;
import Util.Config;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

/**
 *
 * @author Y`inza
 */
public class BookController {

    static PreparedStatement PreparedStatement = null;

    static SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
    static Date date = new Date();
    static java.sql.Date currentDate = new java.sql.Date(date.getTime());

    //Add book to the database
    public static boolean addBook(BookModel bModel) throws ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();

        //READING XML AND GET QUERY FROM IT
        String selectQTYByISBN = Config.getXMLData("query", "SelectQTYByISBN");
        PreparedStatement = connection.prepareStatement(selectQTYByISBN);
        PreparedStatement.setString(1, bModel.getIsbn());
        ResultSet rst = PreparedStatement.executeQuery();
        //If Resultset from the table is not empty then return book object otherwise return null
        if (rst.next()) {
            Double QTY = Double.parseDouble(rst.getString("QUANTITY")) + Double.parseDouble(bModel.getQuantity());

            String updateBooks = Config.getXMLData("query", "UpdateBooks");
            PreparedStatement = connection.prepareStatement(updateBooks);
            PreparedStatement.setString(1, QTY.toString());
            PreparedStatement.setString(2, bModel.getIsbn());
            return PreparedStatement.executeUpdate() > 0;

        } else {
            //READING XML AND GET QUERY FROM IT
            String insertIntoBooks = Config.getXMLData("query", "InsertIntoBooks");

            PreparedStatement = connection.prepareStatement(insertIntoBooks);
            PreparedStatement.setString(1, bModel.getbId());
            PreparedStatement.setString(2, bModel.getBookName());
            PreparedStatement.setString(3, bModel.getAuthor());
            PreparedStatement.setString(4, bModel.getPrice());
            PreparedStatement.setString(5, bModel.getIsbn());
            PreparedStatement.setString(6, bModel.getDescription());
            PreparedStatement.setString(7, bModel.getAvailability());
            PreparedStatement.setString(8, bModel.getVersion());
            PreparedStatement.setString(9, bModel.getQuantity());

            return PreparedStatement.executeUpdate() > 0;
        }
    }

    public static BookModel searchBookByBID(String BID) throws ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        //READING XML AND GET QUERY FROM IT
        String searchBookByBID = Config.getXMLData("query", "SearchBookByBID");
        PreparedStatement = connection.prepareStatement(searchBookByBID);
        PreparedStatement.setString(1, BID);
        ResultSet rst = PreparedStatement.executeQuery();

        //If Resultset fromt he table is not empty then return book object otherwise return null
        if (rst.next()) {
            BookModel objBookModel = new BookModel(rst.getString("BID"),
                    rst.getString("BOOKNAME"), rst.getString("AUTHOR"),
                    rst.getString("PRICE"), rst.getString("ISBN"),
                    rst.getString("DESCRIPTION"), rst.getString("AVAILABILITY"),
                    rst.getString("VERSION"), rst.getString("QUANTITY"));

            return objBookModel;
        } else {
            return null;
        }
    }

    public static boolean deleteBookByISBN(String ISBN) throws ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();

        String updateBookAvailability = Config.getXMLData("query", "UpdateBookAvailability");

        PreparedStatement prepareStatementUpdate = connection.prepareStatement(updateBookAvailability);
        prepareStatementUpdate.setString(1, ISBN);
        return (prepareStatementUpdate.executeUpdate() > 0);
    }

    public static boolean deleteBorrowedByBBID(String BBID, String BID, String UID) throws ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        //READING XML AND GET QUERY FROM IT
        String deleteBorrowedByBBID = Config.getXMLData("query", "DeleteBorrowedByBBID");
        String updateBooksByBID = Config.getXMLData("query", "UpdateBooksByBID");
        String selectBorrowedCount = Config.getXMLData("query", "SelectBorrowedCount");
        String updateBorrowedCount = Config.getXMLData("query", "UpdateBorrowedCount");

        PreparedStatement prepareStatementSelecteUser = connection.prepareStatement(selectBorrowedCount);
        prepareStatementSelecteUser.setString(1, UID);
        ResultSet rst = prepareStatementSelecteUser.executeQuery();
        //Fetching current borrowed count of the user, if result set is empty then reutrn false (No such user)
        if (rst.next()) {

            PreparedStatement prepareStatementUpdateUser = connection.prepareStatement(updateBorrowedCount);
            prepareStatementUpdateUser.setString(1, Integer.toString(Integer.parseInt(rst.getString("BORROWEDBOOKCOUNT")) - 1));
            prepareStatementUpdateUser.setString(2, UID);
            prepareStatementUpdateUser.executeUpdate();

            BookModel book = searchBookByBID(BID);
            int qty = Integer.parseInt(book.getQuantity()) + 1;

            PreparedStatement prepareStatementUpdate = connection.prepareStatement(updateBooksByBID);
            prepareStatementUpdate.setString(1, Integer.toString(qty));
            prepareStatementUpdate.setString(2, BID);
            //Adding +1 to the borrowedcount of the user since the issued book record is deleting from the history
            if (prepareStatementUpdate.executeUpdate() > 0) {
                PreparedStatement prepareStatementDelete = connection.prepareStatement(deleteBorrowedByBBID);
                prepareStatementDelete.setString(1, BBID);

                return (prepareStatementDelete.executeUpdate() > 0);
            } else {
                return false;
            }

        } else {
            return false;
        }

    }

    //Updating book by ISBN 
    public static boolean updateBookByISBN(BookModel bModel) throws ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        //READING XML AND GET QUERY FROM IT
        String updateBookByISBN = Config.getXMLData("query", "UpdateBookByISBN");
        PreparedStatement = connection.prepareStatement(updateBookByISBN);

        PreparedStatement.setString(1, bModel.getBookName());
        PreparedStatement.setString(2, bModel.getAuthor());
        PreparedStatement.setString(3, bModel.getPrice());
        PreparedStatement.setString(4, bModel.getDescription());
        PreparedStatement.setString(5, bModel.getAvailability());
        PreparedStatement.setString(6, bModel.getQuantity());
        PreparedStatement.setString(7, bModel.getVersion());
        PreparedStatement.setString(8, bModel.getIsbn());

        return PreparedStatement.executeUpdate() > 0;

    }

    //Auto generating BID
    public static String autoGenerateBookID() throws ClassNotFoundException, SQLException, IOException {

        String BID = null;
        //READING XML AND GET QUERY FROM IT
        String sql = Config.getXMLData("query", "AutoGenerateBookID");
        Connection connection = DBConnection.getDBConnection().getConnection();
        Statement stmt = connection.createStatement();
        //Fetching BID From the table
        ResultSet bookTable = stmt.executeQuery(sql);

        //Take bid from the resultset
        if (bookTable.next() == true) {

            BID = bookTable.getString("BID");

        }
        //If bid is not null ( That means there is already a bid in the table) then add +1 to the current bid and reutrn other wise return fresh BID
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

    public static ResultSet loadTable(String Query) throws ClassNotFoundException, SQLException, IOException {

        Connection connection = DBConnection.getDBConnection().getConnection();
        Statement stmt = connection.createStatement();
        ResultSet bookTable = stmt.executeQuery(Query);
        if (bookTable != null) {
            return bookTable;
        } else {
            return null;
        }

    }

    public static String autoGenerateBorrowedID() throws ClassNotFoundException, SQLException, IOException {

        String BBID = null;
        //READING XML AND GET QUERY FROM IT
        String sql = Config.getXMLData("query", "AutoGenerateBorrowedID");
        Connection connection = DBConnection.getDBConnection().getConnection();
        Statement stmt = connection.createStatement();
        //Fetching BBID from the table
        ResultSet bookTable = stmt.executeQuery(sql);
        if (bookTable.next() == true) {

            BBID = bookTable.getString("BBID");

        }
        //If bbid is not null ( That means there is already a bbid in the table) then add +1 to the current bbid and reutrn other wise return fresh BID
        if (BBID != null) {
            StringTokenizer st = new StringTokenizer(BBID, "B");

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

    public static String issueBook(BorrowedBookModel bModel, boolean isReserved) throws ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        //READING XML AND GET QUERY FROM IT
        String selectUsersByUID = Config.getXMLData("query", "SelectUsersByUID");
        String updateUserBorrowedCount = Config.getXMLData("query", "UpdateUserBorrowedCount");

        //Fetch user details from the table
        PreparedStatement = connection.prepareStatement(selectUsersByUID);
        PreparedStatement.setString(1, bModel.getuId());
        ResultSet rst = PreparedStatement.executeQuery();
        if (rst.next()) {

            //Checking whether the user has exceed maximumbook count a user can have is exceeded or not
            Boolean Available = (Integer.parseInt(rst.getString("MAXIMUMBOOKCOUNT"))
                    - Integer.parseInt(rst.getString("BORROWEDBOOKCOUNT")) > 0);

            if (Available) {
                //READING XML AND GET QUERY FROM IT
                String insertBorrowedBooks = Config.getXMLData("query", "InsertBorrowedBooks");
                String updateBookQuantity = Config.getXMLData("query", "UpdateBookQuantity");

                //Fetching book details from the table
                BookModel book = searchBookByBID(bModel.getbId());
                int qty;

                //If this book is issuing from a reserved request that means the book count is already deduceted from the database
                //there for no need to deduct again.
                //If normal book issue process then deduct book count from the book table
                if (isReserved) {
                    qty = Integer.parseInt(book.getQuantity());
                } else {
                    qty = Integer.parseInt(book.getQuantity()) - 1;
                }

                java.sql.Date renewDate = new java.sql.Date(bModel.getRenewDate().getTime());

                PreparedStatement = connection.prepareStatement(updateBookQuantity);
                PreparedStatement.setString(1, Integer.toString(qty));
                PreparedStatement.setString(2, bModel.getbId());
                //Adding record to the database
                if (PreparedStatement.executeUpdate() > 0) {
                    PreparedStatement = connection.prepareStatement(insertBorrowedBooks);
                    PreparedStatement.setString(1, bModel.getBbIdl());
                    PreparedStatement.setString(2, bModel.getuId());
                    PreparedStatement.setString(3, bModel.getbId());
                    PreparedStatement.setDate(4, renewDate);
                    PreparedStatement.setDate(5, currentDate);
                    if (PreparedStatement.executeUpdate() > 0) {
                        PreparedStatement = connection.prepareStatement(updateUserBorrowedCount);
                        PreparedStatement.setString(1, Integer.toString(Integer.parseInt(rst.getString("BORROWEDBOOKCOUNT")) + 1));
                        if (PreparedStatement.executeUpdate() > 0) {
                            return "Successfully added";
                        } else {
                            return "Error occured while inserting";
                        }

                    } else {
                        return "Error occured while inserting";
                    }

                } else {
                    return "Error occured while inserting";
                }

            } else {
                return "User has exceed the maximum borrowed book count";
            }

        }
        return "No such user";
    }

    public static boolean deleteBookFeedbacksByFID(String FID) throws ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        //READING XML AND GET QUERY FROM IT
        String updateQuery = Config.getXMLData("query", "DeleteBookFeedbacksByFID");

        PreparedStatement = connection.prepareStatement(updateQuery);
        PreparedStatement.setString(1, FID);
        return (PreparedStatement.executeUpdate() > 0);
    }

    public static boolean approveBookFeedBack(String FID) throws ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        //READING XML AND GET QUERY FROM IT
        String updateQuery = Config.getXMLData("query", "ApproveBookFeedBack");

        PreparedStatement = connection.prepareStatement(updateQuery);
        PreparedStatement.setString(1, FID);
        return (PreparedStatement.executeUpdate() > 0);
    }

    public static boolean updateBorrowedBook(String BBID, Date renewDate, int statusFlag) throws ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        //READING XML AND GET QUERY FROM IT
        String updateQuery = Config.getXMLData("query", "UpdateBorrowedBook");
        java.sql.Date sqlRenewDate = new java.sql.Date(renewDate.getTime());
        PreparedStatement = connection.prepareStatement(updateQuery);
        PreparedStatement.setDate(1, sqlRenewDate);
        PreparedStatement.setString(2, Integer.toString(statusFlag));
        PreparedStatement.setString(3, BBID);
        return (PreparedStatement.executeUpdate() > 0);
    }

}
