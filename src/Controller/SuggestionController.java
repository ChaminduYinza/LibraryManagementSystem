/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Util.DBConnection;
import Model.SuggestionsModel;
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
public class SuggestionController {
    
    public static String autoGenerateSuggestionID() throws ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        
        String sqlGet = Config.getXMLData("query", "SelectSuggestionUotoGenID");
        Statement createStatement = connection.createStatement();
        ResultSet rst = createStatement.executeQuery(sqlGet);
        String fID = null;
        
        if (rst.next() == true) {
            fID = rst.getString("SID");
        }
        if (fID != null) {
            StringTokenizer st = new StringTokenizer(fID, "S");

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
    
    public static boolean addSuggestion(SuggestionsModel suggestionsModel) 
            throws ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        
        String sql = Config.getXMLData("query", "InsertSuggestions");
        PreparedStatement preparestatement = connection.prepareStatement(sql);
        preparestatement.setString(1, suggestionsModel.getsId());
        preparestatement.setString(2, suggestionsModel.getuId());
        preparestatement.setString(3, suggestionsModel.getSuggestion());
        return preparestatement.executeUpdate() > 0;
    }
}
