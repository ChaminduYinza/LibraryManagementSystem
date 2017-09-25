/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 *
 */
public class dbConnection {

    private static dbConnection dBConnection;
    private Connection connection;

    private dbConnection() throws ClassNotFoundException, SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/libraryManagementSystem", "root", "");
    }

    public static dbConnection getDBConnection() throws ClassNotFoundException, SQLException {
        if (dBConnection == null) {
            dBConnection = new dbConnection();
        }
        return dBConnection;
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     *
     * @param query String The query to be executed
     * @return a ResultSet object containing the results or null if not
     * available
     * @throws SQLException
     */
//    public ResultSet query(String query) throws SQLException{
//        statement = db.conn.createStatement();
//        ResultSet res = statement.executeQuery(query);
//        return res;
//    }
//    /**
//     * @desc Method to insert data to a table
//     * @param insertQuery String The Insert query
//     * @return boolean
//     * @throws SQLException
//     */
//    public int insert(String insertQuery) throws SQLException {
//        statement = db.conn.createStatement();
//        int result = statement.executeUpdate(insertQuery);
//        return result;
// 
//    }
}
