/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 *
 */
public class DBConnection {

    public static final Logger LOG = Config.getLogger(DBConnection.class);
    
    private static DBConnection dBConnection;
    private Connection connection;

    private DBConnection() throws ClassNotFoundException, SQLException, ClassNotFoundException, IOException {
        Properties prop = Config.getPropValues();
        Class.forName(prop.getProperty("driver"));
        connection = DriverManager.getConnection(prop.getProperty("url") + prop.getProperty("database"),
                prop.getProperty("user"), prop.getProperty("password"));

    }

    public static DBConnection getDBConnection() throws ClassNotFoundException, SQLException, IOException {
        if (dBConnection == null) {
            dBConnection = new DBConnection();
            LOG.info("------Initializing database connection------");
        }
        return dBConnection;
    }

    public Connection getConnection() {
        return connection;
    }

}
