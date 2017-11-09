/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.IOException;
import static java.lang.Thread.sleep;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.regex.PatternSyntaxException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import net.proteanit.sql.DbUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Sameera
 */
public class Utility {

    public static final Logger LOG = Config.getLogger(Utility.class);

    public static final String PROPERTY_FILE_PATH = System.getProperty("user.dir") + "\\src\\util\\Application.properties";

    public static final String QUERY_FILE_PATH = System.getProperty("user.dir") + "\\src\\util\\DatabaseQueries.xml";

    public static void filter(JTable table, String searchKey) throws PatternSyntaxException {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        TableRowSorter<DefaultTableModel> tableSorter = new TableRowSorter<>(model);
        table.setRowSorter(tableSorter);
        tableSorter.setRowFilter(RowFilter.regexFilter(searchKey));
    }

    public static void loadTable(JTable table, String Query) throws ClassNotFoundException, SQLException, IOException {

        Connection connection = DBConnection.getDBConnection().getConnection();
        Statement stmt = connection.createStatement();
        ResultSet bookTable = stmt.executeQuery(Query);

        if (bookTable != null) {
            table.setModel(DbUtils.resultSetToTableModel(bookTable));
        } else {
            LOG.error("Failed to load data to the table");
        }
    }

    public static void clock(JLabel label) {
        Thread clock = new Thread() {
            public void run() {
                try {
                    for (;;) {
                        Calendar cal = new GregorianCalendar();
                        label.setText(cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE)
                                + ":" + cal.get(Calendar.SECOND) + " / " + cal.get(Calendar.YEAR)
                                + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DATE));
                        sleep(1000);
                    }
                } catch (Exception e) {

                }
            }
        };
        clock.start();
    }

    public static boolean sendEmail(String senderMail, String subject) throws IOException {

        Properties prop = Config.getPropValues();
        final String username = prop.getProperty("EmailAddress");
        final String password = prop.getProperty("EmailPassword");

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        
        String body = "";
        
        if(subject== "passwordchange"){
            subject = prop.getProperty("PasswordChangeSubject");
            body = prop.getProperty("PasswordChangeBody");
        }
        else if(subject == "Register"){
            subject = prop.getProperty("RegisterSubject");
            body = prop.getProperty("RegisterBody");
        }

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(prop.getProperty("EmailAddress")));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(senderMail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            LOG.info("Email sent to the " + senderMail + " Successfully");
            return true;

        } catch (MessagingException e) {
            LOG.info("Fail to sent Email to the " + senderMail);
            LOG.error(e);
        }

        return false;
    }

}
