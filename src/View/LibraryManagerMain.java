/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.SupplierController;
import Controller.UserController;
import Model.PaymentModel;
import Model.SupplierModel;
import Model.UserModel;
import Model.WarnedListModel;
import Util.DBConnection;
import Util.MD5;
import Util.Utility;
import static Util.Utility.LOG;
import Validation.UserValidation;
import Validation.Validation;
import java.awt.Color;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Rushan
 */
public class LibraryManagerMain extends javax.swing.JFrame {

    /**
     * Creates new form LibraryMain
     */
    static String USER;
    static String UID;
    static boolean isReserved;
    public static final Logger log = Logger.getLogger(LibraryManagerMain.class.getName());
    static Calendar calendar = Calendar.getInstance();
    static Calendar regcalendar = Calendar.getInstance();
    static Date SystemDate = new Date();
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

    public LibraryManagerMain(String userName, String uid) throws IOException {
        initComponents();

        USER = userName;
        UID = uid;
        jLabelWelcomeUser.setText("Welcome " + USER);
        pnlIssueBook.setVisible(false);
        Utility.clock(jLableTime);
        pnlIssueBook.setVisible(false);
        jPanelRegisterdUsers.setVisible(false);
        jPanelUser.setVisible(false);
        jPanelDelete.setVisible(false);
        jPanelWarn.setVisible(false);
        jPanelReported.setVisible(false);
        jPanelEmail.setVisible(false);
        jPanelPayHistory.setVisible(false);
        jPanelSettings.setVisible(false);

        calendar.setTime(SystemDate);
        regcalendar.setTime(SystemDate);
        calendar.add(Calendar.DATE, 365);
        Date expireDate = calendar.getTime();
        tx_expdate.setText(formatDate.format(expireDate).toString());

        fetchUserDataToTable();
        fetchWarnUserDataToTable();
        fetchReportedUserDataToTable();
        fetchPayUsersDataToTable();
        fetchLateFinesDataToTable();
        fetchPaymentHistoryToTable();
        fetchUserDataToSendmailTable();

        try {
            tx_userid.setText(UserController.autoGenerateUserID());
            tx_wid.setText(UserController.autoGenerateWarnID());
            tx_pid.setText(UserController.autoGeneratePayID());

        } catch (ClassNotFoundException ex) {
            log.info(ex.getMessage());
        } catch (SQLException ex) {
            log.info(ex.getMessage());
        }

    }

    private void loadSupplierTable() throws IOException {
        try {
            Utility.loadTable(tblSupplier, "SELECT * FROM SUPPLIERS");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LibraryManagerMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void clearSupplierData() throws IOException {
        try {
            tfCompany.setText("");
            tfAddress.setText("");
            tfMobile.setText("");
            tfSupplerID.setText(SupplierController.autoGenerateSupplierID());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LibraryManagerMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void fetchUserDataToTable() {

        try {
            String sql = "SELECT u.UID AS USERID,u.FIRSTNAME,"
                    + "u.LASTNAME, u.CONTACT, u.EMAIL, u.EXPIRYDATE FROM USERS u";
            Utility.loadTable(tbl_regusers, sql);

        } catch (IOException | ClassNotFoundException | SQLException e) {

            log.info("Faild to load data" + e.getMessage());
        }

    }

    public void fetchUserDataToSendmailTable() {

        try {
            String sql = "SELECT u.UID AS USERID,u.FIRSTNAME,"
                    + "u.LASTNAME, u.CONTACT, u.EMAIL, u.EXPIRYDATE FROM USERS u";
            Utility.loadTable(tbl_sendmail, sql);

        } catch (IOException | ClassNotFoundException | SQLException e) {

            log.info("Faild to load data" + e.getMessage());
        }

    }

    public void fetchDeleteUserDataToTable() {

        try {
            String sql = "SELECT u.UID AS USERID,u.FIRSTNAME,"
                    + "u.LASTNAME, u.CONTACT, u.EMAIL, u.EXPIRYDATE FROM USERS u";
            Utility.loadTable(tbl_deleteusers, sql);

        } catch (IOException | ClassNotFoundException | SQLException e) {

            log.info("Faild to load data" + e.getMessage());
        }
    }

    public void fetchWarnUserDataToTable() {

        try {
            String sql = "SELECT u.UID AS USERID,u.FIRSTNAME,"
                    + "u.LASTNAME, u.CONTACT, u.EMAIL, u.EXPIRYDATE  FROM USERS u WHERE u.USERTYPE='Member'";
            Utility.loadTable(tbl_warnusers, sql);

        } catch (IOException | ClassNotFoundException | SQLException e) {

            log.info("Faild to load data" + e.getMessage());
        }

    }

    public void fetchReportedUserDataToTable() {

        try {
            String sql = "SELECT u.UID,w.FULLNAME,u.CONTACT,u.EMAIL,w.WARNINGTYPE,"
                    + "w.DESCRIPTION FROM warnedlist w LEFT JOIN USERS u ON u.UID=w.UID";
            Utility.loadTable(tbl_reportedusers, sql);

        } catch (IOException | ClassNotFoundException | SQLException e) {

            log.info("Faild to load data" + e.getMessage());
        }

    }

    public void fetchPaymentHistoryToTable() {

        try {
            String sql = "SELECT p.PID AS PAYMENTID,p.UID AS USERID,u.FIRSTNAME,u.LASTNAME, p.PAYMENTTYPE, p.AMOUNT FROM PAYMENT p,USERS u WHERE u.UID=p.UID";
            Utility.loadTable(tbl_payhistory, sql);

        } catch (Exception e) {

            log.info("Faild to load data" + e.getMessage());
        }

    }

    public void fetchPayUsersDataToTable() {

        try {
            String sql = "SELECT  UID,FIRSTNAME,LASTNAME,USERTYPE,EXPIRYDATE,STATUS FROM USERS";
            Utility.loadTable(tbl_payusers, sql);

        } catch (Exception e) {

            log.info("Faild to load data" + e.getMessage());
        }

    }

    public void fetchLateFinesDataToTable() {

        try {
            String sql = "SELECT  f.FID,u.UID,u.FIRSTNAME,U.LASTNAME,f.REASON,f.VALUE,"
                    + "f.ISPAID FROM USERS u, FINES f WHERE u.UID=f.UID";

            Utility.loadTable(tbl_latefines, sql);

        } catch (Exception e) {

            log.info("Faild to load data" + e.getMessage());
        }

    }

    public void refreshData() throws IOException {

        try {
            tx_userid.setText(UserController.autoGenerateUserID());
            tx_lname.setText("");
            tx_email.setText("");
            tx_fname.setText("");
            tx_cpsw.setText("");
            tx_psw.setText("");
            tx_dob.setCalendar(null);
            tx_address.setText("");
            tx_phone.setText("");
            tx_nic.setText("");
            tx_brrowcount.setText("");
            tx_maxbook.setText("");

        } catch (ClassNotFoundException ex) {
            log.info(ex.getMessage());
        } catch (SQLException ex) {
            log.info(ex.getMessage());
        }

    }

    public void demoData() throws IOException {

        tx_lname.setText("Bino");
        tx_email.setText("rushan.edirisuriya@gmail.com");
        tx_fname.setText("Tiran");
        tx_cpsw.setText("R@ndika123");
        tx_psw.setText("R@ndika123");
        tx_address.setText("74/2,Saman Mw,Rathmalana");
        tx_phone.setText("0754273900");
        tx_nic.setText("923104570v");
        tx_brrowcount.setText("5");
        tx_maxbook.setText("8");

    }

    public void expireStatus() {

        String today = formatDate.format(regcalendar.getTime()).toString();
        try {
            String sql = "SELECT  UID,FIRSTNAME,LASTNAME,USERTYPE,EXPIRYDATE,STATUS FROM USERS WHERE EXPIRYDATE < '" + today + "'";
            Connection connection = DBConnection.getDBConnection().getConnection();
            Statement createstatement = connection.createStatement();
            ResultSet result = createstatement.executeQuery(sql);
            tbl_payusers.setModel(DbUtils.resultSetToTableModel(result));

        } catch (Exception e) {

            log.info("Faild to load data" + e.getMessage());
        }

    }

    //this method call for show and hide toggle panels when button click
    private void togglePanels(String buttonName) {
        switch (buttonName) {
            case "ReserveBook":
                pnlIssueBook.setVisible(true);
                jPanelRegisterdUsers.setVisible(false);
                jPanelUser.setVisible(false);
                jPanelDelete.setVisible(false);
                jPanelWarn.setVisible(false);
                jPanelReported.setVisible(false);
                jPanelEmail.setVisible(false);
                jPanelPayHistory.setVisible(false);
                jPanelSettings.setVisible(false);
                break;
            case "USERS":
                pnlIssueBook.setVisible(false);
                jPanelRegisterdUsers.setVisible(true);
                jPanelUser.setVisible(false);
                jPanelDelete.setVisible(false);
                jPanelWarn.setVisible(false);
                jPanelReported.setVisible(false);
                jPanelEmail.setVisible(false);
                jPanelPayHistory.setVisible(false);
                jPanelSettings.setVisible(false);
                break;
            case "RequestedList":
                pnlIssueBook.setVisible(false);
                jPanelRegisterdUsers.setVisible(false);
                jPanelUser.setVisible(true);
                jPanelDelete.setVisible(false);
                jPanelWarn.setVisible(false);
                jPanelReported.setVisible(false);
                jPanelEmail.setVisible(false);
                jPanelPayHistory.setVisible(false);
                jPanelSettings.setVisible(false);
                break;
            case "PendingBooks":
                pnlIssueBook.setVisible(false);
                jPanelRegisterdUsers.setVisible(false);
                jPanelUser.setVisible(false);
                jPanelDelete.setVisible(true);
                jPanelWarn.setVisible(false);
                jPanelReported.setVisible(false);
                jPanelEmail.setVisible(false);
                jPanelPayHistory.setVisible(false);
                jPanelSettings.setVisible(false);
                break;
            case "History":
                pnlIssueBook.setVisible(false);
                jPanelRegisterdUsers.setVisible(false);
                jPanelUser.setVisible(false);
                jPanelDelete.setVisible(false);
                jPanelWarn.setVisible(true);
                jPanelReported.setVisible(false);
                jPanelEmail.setVisible(false);
                jPanelPayHistory.setVisible(false);
                jPanelSettings.setVisible(false);
                break;
            case "Feedbacks":
                pnlIssueBook.setVisible(false);
                jPanelRegisterdUsers.setVisible(false);
                jPanelUser.setVisible(false);
                jPanelDelete.setVisible(false);
                jPanelWarn.setVisible(false);
                jPanelReported.setVisible(true);
                jPanelEmail.setVisible(false);
                jPanelPayHistory.setVisible(false);
                jPanelSettings.setVisible(false);
                break;
            case "SendMessage":
                pnlIssueBook.setVisible(false);
                jPanelRegisterdUsers.setVisible(false);
                jPanelUser.setVisible(false);
                jPanelDelete.setVisible(false);
                jPanelWarn.setVisible(false);
                jPanelReported.setVisible(false);
                jPanelEmail.setVisible(true);
                jPanelPayHistory.setVisible(false);
                jPanelSettings.setVisible(false);
                break;
            case "AddPayment":
                pnlIssueBook.setVisible(false);
                jPanelRegisterdUsers.setVisible(false);
                jPanelUser.setVisible(false);
                jPanelDelete.setVisible(false);
                jPanelWarn.setVisible(false);
                jPanelReported.setVisible(false);
                jPanelEmail.setVisible(false);
                jPanelPayHistory.setVisible(true);
                jPanelSettings.setVisible(false);
                break;
            case "Settings":
                pnlIssueBook.setVisible(false);
                jPanelRegisterdUsers.setVisible(false);
                jPanelUser.setVisible(false);
                jPanelDelete.setVisible(false);
                jPanelWarn.setVisible(false);
                jPanelReported.setVisible(false);
                jPanelEmail.setVisible(false);
                jPanelPayHistory.setVisible(false);
                jPanelSettings.setVisible(true);
                break;

            default:
                break;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDesktopPaneLibraryMain = new javax.swing.JDesktopPane();
        jPanelHeader = new javax.swing.JPanel();
        jLabelLogo = new javax.swing.JLabel();
        jLabelWelcomeUser = new javax.swing.JLabel();
        jLableTime = new javax.swing.JLabel();
        jPanelButtons = new javax.swing.JPanel();
        btnIssueBookTab = new javax.swing.JButton();
        jButtonAddPayment = new javax.swing.JButton();
        jButtonSendMessage = new javax.swing.JButton();
        jButtonReportedUsers = new javax.swing.JButton();
        jButtonWarnusers = new javax.swing.JButton();
        jButtonDeleteButton = new javax.swing.JButton();
        jButtonAddUsers = new javax.swing.JButton();
        jButtonRegUsers = new javax.swing.JButton();
        jButtonLogout = new javax.swing.JButton();
        jButtonSettings = new javax.swing.JButton();
        jPanelToggle = new javax.swing.JPanel();
        pnlIssueBook = new javax.swing.JPanel();
        tbIssueBook = new javax.swing.JTabbedPane();
        jPanelSearchBookTab = new javax.swing.JPanel();
        tfSearchSupplier = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSupplier = new javax.swing.JTable();
        tfSupplerID = new javax.swing.JTextField();
        tfCompany = new javax.swing.JTextField();
        tfAddress = new javax.swing.JTextField();
        tfMobile = new javax.swing.JTextField();
        btnDelete = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        jPanelRegisterdUsers = new javax.swing.JPanel();
        jTabbedPaneSupp = new javax.swing.JTabbedPane();
        jPanelSupplier = new javax.swing.JPanel();
        jTextFieldISBN1 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_regusers = new javax.swing.JTable();
        jPanelUser = new javax.swing.JPanel();
        jTabbedPaneRequestedList = new javax.swing.JTabbedPane();
        jPanelAddUser = new javax.swing.JPanel();
        tx_userid = new javax.swing.JTextField();
        tx_expdate = new javax.swing.JTextField();
        tx_phone = new javax.swing.JTextField();
        tx_lname = new javax.swing.JTextField();
        tx_fname = new javax.swing.JTextField();
        btn_save = new javax.swing.JButton();
        tx_cpsw = new javax.swing.JPasswordField();
        tx_psw = new javax.swing.JPasswordField();
        tx_email = new javax.swing.JTextField();
        tx_dob = new com.toedter.calendar.JDateChooser();
        tx_nic = new javax.swing.JTextField();
        tx_usertype = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        tx_address = new javax.swing.JTextArea();
        tx_brrowcount = new javax.swing.JTextField();
        tx_maxbook = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jPanelDelete = new javax.swing.JPanel();
        jTabbedPanePendingReturn = new javax.swing.JTabbedPane();
        jPanelDeleteUser = new javax.swing.JPanel();
        tx_search_name = new javax.swing.JTextField();
        jScrollPane9 = new javax.swing.JScrollPane();
        tbl_deleteusers = new javax.swing.JTable();
        btn_delete = new javax.swing.JButton();
        jPanelWarn = new javax.swing.JPanel();
        jTabbedPanelHistory = new javax.swing.JTabbedPane();
        jPanelWarnUsers = new javax.swing.JPanel();
        tfAuthorName2 = new javax.swing.JTextField();
        jScrollPane10 = new javax.swing.JScrollPane();
        tbl_warnusers = new javax.swing.JTable();
        tx_warntype = new javax.swing.JComboBox<>();
        jScrollPane11 = new javax.swing.JScrollPane();
        tx_warnnote = new javax.swing.JTextArea();
        btn_warnsubmit = new javax.swing.JButton();
        tx_wid = new javax.swing.JTextField();
        jPanelReported = new javax.swing.JPanel();
        jTabbedPanelFeedback = new javax.swing.JTabbedPane();
        jPanelReportedUsers = new javax.swing.JPanel();
        tfAuthorName4 = new javax.swing.JTextField();
        jScrollPane14 = new javax.swing.JScrollPane();
        tbl_reportedusers = new javax.swing.JTable();
        jPanelEmail = new javax.swing.JPanel();
        jTabbedPanelFeedback1 = new javax.swing.JTabbedPane();
        jPanelSendEmail = new javax.swing.JPanel();
        tfAuthorName7 = new javax.swing.JTextField();
        tfAuthorName8 = new javax.swing.JTextField();
        tfAuthorName9 = new javax.swing.JTextField();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jButton5 = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        tbl_sendmail = new javax.swing.JTable();
        tx_searchmail = new javax.swing.JTextField();
        jPanelPayHistory = new javax.swing.JPanel();
        jTabbedPanelFeedback2 = new javax.swing.JTabbedPane();
        jPanelMemberShipPayment = new javax.swing.JPanel();
        tx_memberSearch = new javax.swing.JTextField();
        jScrollPane17 = new javax.swing.JScrollPane();
        tbl_payusers = new javax.swing.JTable();
        btn_paid = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        tx_amt = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tx_ptype = new javax.swing.JComboBox<>();
        checkExpireUsers = new javax.swing.JCheckBox();
        tx_pid = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbl_latefines = new javax.swing.JTable();
        tx_fineSearch = new javax.swing.JTextField();
        tx_ispaid = new javax.swing.JCheckBox();
        tx_famt = new javax.swing.JTextField();
        tx_fid = new javax.swing.JTextField();
        tx_fuid = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        tx_reason = new javax.swing.JTextArea();
        jButton8 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tbl_payhistory = new javax.swing.JTable();
        jTextField2 = new javax.swing.JTextField();
        jPanelSettings = new javax.swing.JPanel();
        jTabbedPanelSettings = new javax.swing.JTabbedPane();
        jPanelEditProfile = new javax.swing.JPanel();
        jTextFieldDOB = new javax.swing.JTextField();
        jButtonSave = new javax.swing.JButton();
        jPasswordFieldConfirmPw = new javax.swing.JPasswordField();
        jPasswordFieldCurrentPw = new javax.swing.JPasswordField();
        jPasswordFieldNewPw = new javax.swing.JPasswordField();
        jPanelProfilepic = new javax.swing.JPanel();
        jLabelPicture = new javax.swing.JLabel();
        jLabelUserEmail = new javax.swing.JLabel();
        jTextFieldFullName = new javax.swing.JTextField();
        jTextFieldMembership = new javax.swing.JTextField();
        jTextFieldNIC = new javax.swing.JTextField();
        jTextFieldContact = new javax.swing.JTextField();
        jTextFieldRegDate = new javax.swing.JTextField();
        jTextFieldExpiryDate = new javax.swing.JTextField();
        jButtonChangePassword = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Library Management System : Library Manager");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jDesktopPaneLibraryMain.setBackground(new java.awt.Color(255, 255, 255));
        jDesktopPaneLibraryMain.setForeground(new java.awt.Color(255, 255, 255));
        jDesktopPaneLibraryMain.setPreferredSize(new java.awt.Dimension(1022, 710));
        jDesktopPaneLibraryMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelHeader.setBackground(new java.awt.Color(0, 102, 102));
        jPanelHeader.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelLogo.setIcon(new javax.swing.ImageIcon("C:\\SLIIT\\LibraryManagementSystem\\src\\View\\Images\\librarylogo.png")); // NOI18N
        jPanelHeader.add(jLabelLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        jLabelWelcomeUser.setForeground(new java.awt.Color(204, 204, 204));
        jLabelWelcomeUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanelHeader.add(jLabelWelcomeUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(714, 10, 290, 20));

        jLableTime.setForeground(new java.awt.Color(204, 204, 204));
        jLableTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanelHeader.add(jLableTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(864, 94, 150, 20));

        jDesktopPaneLibraryMain.add(jPanelHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1022, 120));

        jPanelButtons.setBackground(new java.awt.Color(0, 102, 102));
        jPanelButtons.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnIssueBookTab.setBackground(new java.awt.Color(0, 102, 102));
        btnIssueBookTab.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        btnIssueBookTab.setForeground(new java.awt.Color(255, 255, 255));
        btnIssueBookTab.setIcon(new javax.swing.ImageIcon("C:\\SLIIT\\LibraryManagementSystem-master\\src\\View\\Images\\menuBtn.png")); // NOI18N
        btnIssueBookTab.setText("SUPPLIER");
        btnIssueBookTab.setFocusPainted(false);
        btnIssueBookTab.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnIssueBookTab.setIconTextGap(5);
        btnIssueBookTab.setMargin(new java.awt.Insets(2, 0, 2, 5));
        btnIssueBookTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnIssueBookTabMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnIssueBookTabMouseExited(evt);
            }
        });
        btnIssueBookTab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIssueBookTabActionPerformed(evt);
            }
        });
        jPanelButtons.add(btnIssueBookTab, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 190, 40));

        jButtonAddPayment.setBackground(new java.awt.Color(0, 102, 102));
        jButtonAddPayment.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        jButtonAddPayment.setForeground(new java.awt.Color(255, 255, 255));
        jButtonAddPayment.setIcon(new javax.swing.ImageIcon("C:\\SLIIT\\LibraryManagementSystem-master\\src\\View\\Images\\menuBtn.png")); // NOI18N
        jButtonAddPayment.setText("PAYMENTS");
        jButtonAddPayment.setFocusPainted(false);
        jButtonAddPayment.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonAddPayment.setIconTextGap(5);
        jButtonAddPayment.setMargin(new java.awt.Insets(2, 0, 2, 5));
        jButtonAddPayment.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonAddPayment11MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonAddPayment11MouseExited(evt);
            }
        });
        jButtonAddPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddPaymentActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonAddPayment, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 190, 40));

        jButtonSendMessage.setBackground(new java.awt.Color(0, 102, 102));
        jButtonSendMessage.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        jButtonSendMessage.setForeground(new java.awt.Color(255, 255, 255));
        jButtonSendMessage.setIcon(new javax.swing.ImageIcon("C:\\SLIIT\\LibraryManagementSystem-master\\src\\View\\Images\\menuBtn.png")); // NOI18N
        jButtonSendMessage.setText("SEND MESSAGE");
        jButtonSendMessage.setFocusPainted(false);
        jButtonSendMessage.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonSendMessage.setIconTextGap(5);
        jButtonSendMessage.setMargin(new java.awt.Insets(2, 0, 2, 5));
        jButtonSendMessage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonSendMessage11MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonSendMessage11MouseExited(evt);
            }
        });
        jButtonSendMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSendMessageActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonSendMessage, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 190, 40));

        jButtonReportedUsers.setBackground(new java.awt.Color(0, 102, 102));
        jButtonReportedUsers.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        jButtonReportedUsers.setForeground(new java.awt.Color(255, 255, 255));
        jButtonReportedUsers.setIcon(new javax.swing.ImageIcon("C:\\SLIIT\\LibraryManagementSystem-master\\src\\View\\Images\\menuBtn.png")); // NOI18N
        jButtonReportedUsers.setText("REPORTED USERS");
        jButtonReportedUsers.setFocusPainted(false);
        jButtonReportedUsers.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonReportedUsers.setIconTextGap(5);
        jButtonReportedUsers.setMargin(new java.awt.Insets(2, 0, 2, 5));
        jButtonReportedUsers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonReportedUsers11MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonReportedUsers11MouseExited(evt);
            }
        });
        jButtonReportedUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReportedUsersActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonReportedUsers, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 190, 40));

        jButtonWarnusers.setBackground(new java.awt.Color(0, 102, 102));
        jButtonWarnusers.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        jButtonWarnusers.setForeground(new java.awt.Color(255, 255, 255));
        jButtonWarnusers.setIcon(new javax.swing.ImageIcon("C:\\SLIIT\\LibraryManagementSystem-master\\src\\View\\Images\\menuBtn.png")); // NOI18N
        jButtonWarnusers.setText("WARN USERS");
        jButtonWarnusers.setFocusPainted(false);
        jButtonWarnusers.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonWarnusers.setIconTextGap(5);
        jButtonWarnusers.setMargin(new java.awt.Insets(2, 0, 2, 5));
        jButtonWarnusers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonWarnusersMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonWarnusersMouseExited(evt);
            }
        });
        jButtonWarnusers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWarnusersActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonWarnusers, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 190, 40));

        jButtonDeleteButton.setBackground(new java.awt.Color(0, 102, 102));
        jButtonDeleteButton.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        jButtonDeleteButton.setForeground(new java.awt.Color(255, 255, 255));
        jButtonDeleteButton.setIcon(new javax.swing.ImageIcon("C:\\SLIIT\\LibraryManagementSystem-master\\src\\View\\Images\\menuBtn.png")); // NOI18N
        jButtonDeleteButton.setText("DELETE USERS");
        jButtonDeleteButton.setFocusPainted(false);
        jButtonDeleteButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonDeleteButton.setIconTextGap(5);
        jButtonDeleteButton.setMargin(new java.awt.Insets(2, 0, 2, 5));
        jButtonDeleteButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonDeleteButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonDeleteButtonMouseExited(evt);
            }
        });
        jButtonDeleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteButtonActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonDeleteButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 190, 40));

        jButtonAddUsers.setBackground(new java.awt.Color(0, 102, 102));
        jButtonAddUsers.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        jButtonAddUsers.setForeground(new java.awt.Color(255, 255, 255));
        jButtonAddUsers.setIcon(new javax.swing.ImageIcon("C:\\SLIIT\\LibraryManagementSystem-master\\src\\View\\Images\\menuBtn.png")); // NOI18N
        jButtonAddUsers.setText("ADD USERS");
        jButtonAddUsers.setFocusPainted(false);
        jButtonAddUsers.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonAddUsers.setIconTextGap(5);
        jButtonAddUsers.setMargin(new java.awt.Insets(2, 0, 2, 5));
        jButtonAddUsers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonAddUsersMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonAddUsersMouseExited(evt);
            }
        });
        jButtonAddUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddUsersActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonAddUsers, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 190, 40));

        jButtonRegUsers.setBackground(new java.awt.Color(0, 102, 102));
        jButtonRegUsers.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        jButtonRegUsers.setForeground(new java.awt.Color(255, 255, 255));
        jButtonRegUsers.setIcon(new javax.swing.ImageIcon("C:\\SLIIT\\LibraryManagementSystem-master\\src\\View\\Images\\menuBtn.png")); // NOI18N
        jButtonRegUsers.setText("REGISTER USERS");
        jButtonRegUsers.setFocusPainted(false);
        jButtonRegUsers.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonRegUsers.setIconTextGap(5);
        jButtonRegUsers.setMargin(new java.awt.Insets(2, 0, 2, 5));
        jButtonRegUsers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonRegUsersMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonRegUsersMouseExited(evt);
            }
        });
        jButtonRegUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRegUsersActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonRegUsers, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 190, 40));

        jButtonLogout.setBackground(new java.awt.Color(0, 102, 102));
        jButtonLogout.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        jButtonLogout.setForeground(new java.awt.Color(255, 255, 255));
        jButtonLogout.setIcon(new javax.swing.ImageIcon("C:\\SLIIT\\LibraryManagementSystem\\src\\View\\Images\\logout.png")); // NOI18N
        jButtonLogout.setText("LOGOUT");
        jButtonLogout.setFocusPainted(false);
        jButtonLogout.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonLogout.setIconTextGap(5);
        jButtonLogout.setMargin(new java.awt.Insets(2, 0, 2, 5));
        jButtonLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonLogout11MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonLogout11MouseExited(evt);
            }
        });
        jButtonLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLogoutActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 470, 190, 40));

        jButtonSettings.setBackground(new java.awt.Color(0, 102, 102));
        jButtonSettings.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        jButtonSettings.setForeground(new java.awt.Color(255, 255, 255));
        jButtonSettings.setIcon(new javax.swing.ImageIcon("C:\\SLIIT\\LibraryManagementSystem\\src\\View\\Images\\settings.png")); // NOI18N
        jButtonSettings.setText("SETTINGS");
        jButtonSettings.setFocusPainted(false);
        jButtonSettings.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonSettings.setIconTextGap(5);
        jButtonSettings.setMargin(new java.awt.Insets(2, 0, 2, 5));
        jButtonSettings.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonSettings11MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonSettings11MouseExited(evt);
            }
        });
        jButtonSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSettingsActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonSettings, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 520, 190, 40));

        jDesktopPaneLibraryMain.add(jPanelButtons, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 210, 570));

        jPanelToggle.setBackground(new java.awt.Color(0, 102, 102));
        jPanelToggle.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlIssueBook.setBackground(new java.awt.Color(255, 255, 255));
        pnlIssueBook.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbIssueBook.setBackground(new java.awt.Color(255, 255, 255));
        tbIssueBook.setFocusable(false);
        tbIssueBook.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tbIssueBook.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbIssueBookMouseClicked(evt);
            }
        });

        jPanelSearchBookTab.setBackground(new java.awt.Color(255, 255, 255));
        jPanelSearchBookTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tfSearchSupplier.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfSearchSupplier.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Search"));
        tfSearchSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfSearchSupplierKeyReleased(evt);
            }
        });
        jPanelSearchBookTab.add(tfSearchSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 200, 40));

        tblSupplier.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSupplierMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSupplier);
        if (tblSupplier.getColumnModel().getColumnCount() > 0) {
            tblSupplier.getColumnModel().getColumn(0).setResizable(false);
            tblSupplier.getColumnModel().getColumn(1).setResizable(false);
            tblSupplier.getColumnModel().getColumn(2).setResizable(false);
            tblSupplier.getColumnModel().getColumn(3).setResizable(false);
        }

        jPanelSearchBookTab.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 750, 170));

        tfSupplerID.setEditable(false);
        tfSupplerID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfSupplerID.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Supplier ID"));
        jPanelSearchBookTab.add(tfSupplerID, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 200, 40));

        tfCompany.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfCompany.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Company"));
        jPanelSearchBookTab.add(tfCompany, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 320, 200, 40));

        tfAddress.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfAddress.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Address"));
        jPanelSearchBookTab.add(tfAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 320, 200, 40));

        tfMobile.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfMobile.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Mobile"));
        jPanelSearchBookTab.add(tfMobile, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 200, 40));

        btnDelete.setBackground(new java.awt.Color(0, 102, 102));
        btnDelete.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setIcon(new javax.swing.ImageIcon("C:\\SLIIT\\LibraryManagementSystem\\src\\View\\Images\\reservebtn.png")); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.setFocusable(false);
        btnDelete.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanelSearchBookTab.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 490, 110, 30));

        btnAdd.setBackground(new java.awt.Color(0, 102, 102));
        btnAdd.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setIcon(new javax.swing.ImageIcon("C:\\SLIIT\\LibraryManagementSystem\\src\\View\\Images\\reservebtn.png")); // NOI18N
        btnAdd.setText("Add");
        btnAdd.setFocusable(false);
        btnAdd.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        jPanelSearchBookTab.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 490, 110, 30));

        btnUpdate.setBackground(new java.awt.Color(0, 102, 102));
        btnUpdate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdate.setIcon(new javax.swing.ImageIcon("C:\\SLIIT\\LibraryManagementSystem\\src\\View\\Images\\reservebtn.png")); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.setFocusable(false);
        btnUpdate.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        jPanelSearchBookTab.add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 490, 110, 30));

        tbIssueBook.addTab("Supplier", jPanelSearchBookTab);

        pnlIssueBook.add(tbIssueBook, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelToggle.add(pnlIssueBook, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelRegisterdUsers.setBackground(new java.awt.Color(255, 255, 255));
        jPanelRegisterdUsers.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPaneSupp.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPaneSupp.setFocusable(false);
        jTabbedPaneSupp.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabbedPaneSupp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPaneSuppMouseClicked(evt);
            }
        });

        jPanelSupplier.setBackground(new java.awt.Color(255, 255, 255));
        jPanelSupplier.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextFieldISBN1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldISBN1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Search"));
        jTextFieldISBN1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldISBN1KeyReleased(evt);
            }
        });
        jPanelSupplier.add(jTextFieldISBN1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 200, 40));

        tbl_regusers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "UID", "First name", "Last name", "Email", "User type", "Contact", "Address", "Expire Date"
            }
        ));
        jScrollPane2.setViewportView(tbl_regusers);

        jPanelSupplier.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 750, 320));

        jTabbedPaneSupp.addTab("Registerd Users", jPanelSupplier);

        jPanelRegisterdUsers.add(jTabbedPaneSupp, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelToggle.add(jPanelRegisterdUsers, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelUser.setBackground(new java.awt.Color(255, 255, 255));
        jPanelUser.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPaneRequestedList.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPaneRequestedList.setFocusable(false);
        jTabbedPaneRequestedList.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jPanelAddUser.setBackground(new java.awt.Color(255, 255, 255));
        jPanelAddUser.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tx_userid.setEditable(false);
        tx_userid.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tx_userid.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "User ID"));
        jPanelAddUser.add(tx_userid, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, 200, 40));

        tx_expdate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tx_expdate.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Exprie Date"));
        jPanelAddUser.add(tx_expdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 270, 200, 40));

        tx_phone.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tx_phone.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Contact"));
        jPanelAddUser.add(tx_phone, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 140, 200, 40));

        tx_lname.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tx_lname.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Last Name"));
        tx_lname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tx_lnameActionPerformed(evt);
            }
        });
        jPanelAddUser.add(tx_lname, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, 200, 40));

        tx_fname.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tx_fname.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "First Name"));
        jPanelAddUser.add(tx_fname, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 200, 40));

        btn_save.setBackground(new java.awt.Color(0, 102, 102));
        btn_save.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_save.setForeground(new java.awt.Color(255, 255, 255));
        btn_save.setText("Save");
        btn_save.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)));
        btn_save.setFocusable(false);
        btn_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveActionPerformed(evt);
            }
        });
        jPanelAddUser.add(btn_save, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 490, 100, 30));

        tx_cpsw.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Confirm Password"));
        tx_cpsw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tx_cpswActionPerformed(evt);
            }
        });
        jPanelAddUser.add(tx_cpsw, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 340, 200, 40));

        tx_psw.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Password"));
        jPanelAddUser.add(tx_psw, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 270, 200, 40));

        tx_email.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tx_email.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Email"));
        jPanelAddUser.add(tx_email, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 70, 200, 40));

        tx_dob.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Date of Birth"));
        tx_dob.setDateFormatString("yyyy-MM-dd");
        jPanelAddUser.add(tx_dob, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 200, 200, 40));

        tx_nic.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tx_nic.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Nic"));
        jPanelAddUser.add(tx_nic, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 10, 200, 40));

        tx_usertype.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Member", "Librarian", "Manager" }));
        tx_usertype.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "User Type"));
        jPanelAddUser.add(tx_usertype, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 70, 200, 40));

        tx_address.setColumns(20);
        tx_address.setRows(5);
        tx_address.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Address"));
        jScrollPane3.setViewportView(tx_address);

        jPanelAddUser.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 420, 240, 100));

        tx_brrowcount.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Borrowed Book Count"));
        tx_brrowcount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tx_brrowcountActionPerformed(evt);
            }
        });
        jPanelAddUser.add(tx_brrowcount, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 420, 200, -1));

        tx_maxbook.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Max Book Count"));
        jPanelAddUser.add(tx_maxbook, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 350, 200, -1));

        jButton3.setText("Refresh");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanelAddUser.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 20, -1, -1));

        jButton9.setText("Demo");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanelAddUser.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 60, 80, -1));

        jTabbedPaneRequestedList.addTab("Add User", jPanelAddUser);

        jPanelUser.add(jTabbedPaneRequestedList, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelToggle.add(jPanelUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelDelete.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDelete.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPanePendingReturn.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPanePendingReturn.setFocusable(false);
        jTabbedPanePendingReturn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jPanelDeleteUser.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDeleteUser.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tx_search_name.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tx_search_name.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Search"));
        tx_search_name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tx_search_nameKeyReleased(evt);
            }
        });
        jPanelDeleteUser.add(tx_search_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 200, 40));

        tbl_deleteusers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "UID", "First name", "Last name", "Email", "User type", "Contact", "Address", "Expire Date"
            }
        ));
        jScrollPane9.setViewportView(tbl_deleteusers);

        jPanelDeleteUser.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 750, 320));

        btn_delete.setBackground(new java.awt.Color(0, 102, 102));
        btn_delete.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_delete.setForeground(new java.awt.Color(255, 255, 255));
        btn_delete.setText("Delete");
        btn_delete.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)));
        btn_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteActionPerformed(evt);
            }
        });
        jPanelDeleteUser.add(btn_delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 470, 100, 30));

        jTabbedPanePendingReturn.addTab("Delete User", jPanelDeleteUser);

        jPanelDelete.add(jTabbedPanePendingReturn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelToggle.add(jPanelDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelWarn.setBackground(new java.awt.Color(255, 255, 255));
        jPanelWarn.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPanelHistory.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPanelHistory.setFocusable(false);
        jTabbedPanelHistory.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jPanelWarnUsers.setBackground(new java.awt.Color(255, 255, 255));
        jPanelWarnUsers.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tfAuthorName2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfAuthorName2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Search"));
        tfAuthorName2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfAuthorName2KeyReleased(evt);
            }
        });
        jPanelWarnUsers.add(tfAuthorName2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 200, 40));

        tbl_warnusers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "UID", "First name", "Last name", "Email", "User Type", "Contact"
            }
        ));
        jScrollPane10.setViewportView(tbl_warnusers);

        jPanelWarnUsers.add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 750, 150));

        tx_warntype.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Damage", "Late Submission", "Disciplinary" }));
        tx_warntype.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Warning Type"));
        jPanelWarnUsers.add(tx_warntype, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 320, 170, 50));

        tx_warnnote.setColumns(20);
        tx_warnnote.setRows(5);
        tx_warnnote.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Warning Note"));
        jScrollPane11.setViewportView(tx_warnnote);

        jPanelWarnUsers.add(jScrollPane11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 470, -1));

        btn_warnsubmit.setBackground(new java.awt.Color(0, 102, 102));
        btn_warnsubmit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_warnsubmit.setForeground(new java.awt.Color(255, 255, 255));
        btn_warnsubmit.setText("Send");
        btn_warnsubmit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)));
        btn_warnsubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_warnsubmitActionPerformed(evt);
            }
        });
        jPanelWarnUsers.add(btn_warnsubmit, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 460, 100, 30));

        tx_wid.setEditable(false);
        tx_wid.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "WID"));
        jPanelWarnUsers.add(tx_wid, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 150, -1));

        jTabbedPanelHistory.addTab("Warn Users", jPanelWarnUsers);

        jPanelWarn.add(jTabbedPanelHistory, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelToggle.add(jPanelWarn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelReported.setBackground(new java.awt.Color(255, 255, 255));
        jPanelReported.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPanelFeedback.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPanelFeedback.setFocusable(false);
        jTabbedPanelFeedback.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jPanelReportedUsers.setBackground(new java.awt.Color(255, 255, 255));
        jPanelReportedUsers.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tfAuthorName4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfAuthorName4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Search"));
        tfAuthorName4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfAuthorName4KeyReleased(evt);
            }
        });
        jPanelReportedUsers.add(tfAuthorName4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 200, 40));

        tbl_reportedusers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "UID", "FULL NAME", "EMAIL", "CONTACT", "WARNING TYPE", "DESCRIPTION"
            }
        ));
        jScrollPane14.setViewportView(tbl_reportedusers);

        jPanelReportedUsers.add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 750, 370));

        jTabbedPanelFeedback.addTab("View Reported Users", jPanelReportedUsers);

        jPanelReported.add(jTabbedPanelFeedback, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelToggle.add(jPanelReported, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelEmail.setBackground(new java.awt.Color(255, 255, 255));
        jPanelEmail.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPanelFeedback1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPanelFeedback1.setFocusable(false);
        jTabbedPanelFeedback1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jPanelSendEmail.setBackground(new java.awt.Color(255, 255, 255));
        jPanelSendEmail.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tfAuthorName7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfAuthorName7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Name"));
        jPanelSendEmail.add(tfAuthorName7, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 190, 200, 40));

        tfAuthorName8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfAuthorName8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Subject"));
        jPanelSendEmail.add(tfAuthorName8, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 290, 200, 40));

        tfAuthorName9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfAuthorName9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Email to"));
        jPanelSendEmail.add(tfAuthorName9, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 240, 200, 40));

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jTextArea2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Message"));
        jScrollPane13.setViewportView(jTextArea2);

        jPanelSendEmail.add(jScrollPane13, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 360, 590, 110));

        jButton5.setBackground(new java.awt.Color(0, 102, 102));
        jButton5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Send");
        jButton5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanelSendEmail.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 490, 100, 30));

        tbl_sendmail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbl_sendmail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_sendmailMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tbl_sendmail);

        jPanelSendEmail.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 740, 120));

        tx_searchmail.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Search"));
        tx_searchmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tx_searchmailKeyReleased(evt);
            }
        });
        jPanelSendEmail.add(tx_searchmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 190, -1));

        jTabbedPanelFeedback1.addTab("Send Email", jPanelSendEmail);

        jPanelEmail.add(jTabbedPanelFeedback1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelToggle.add(jPanelEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelPayHistory.setBackground(new java.awt.Color(255, 255, 255));
        jPanelPayHistory.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPanelFeedback2.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPanelFeedback2.setFocusable(false);
        jTabbedPanelFeedback2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jPanelMemberShipPayment.setBackground(new java.awt.Color(255, 255, 255));
        jPanelMemberShipPayment.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tx_memberSearch.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tx_memberSearch.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Search"));
        tx_memberSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tx_memberSearchKeyReleased(evt);
            }
        });
        jPanelMemberShipPayment.add(tx_memberSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 200, 40));

        tbl_payusers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane17.setViewportView(tbl_payusers);

        jPanelMemberShipPayment.add(jScrollPane17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 750, 150));

        btn_paid.setBackground(new java.awt.Color(0, 102, 102));
        btn_paid.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_paid.setForeground(new java.awt.Color(255, 255, 255));
        btn_paid.setText("Paid");
        btn_paid.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)));
        btn_paid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_paidActionPerformed(evt);
            }
        });
        jPanelMemberShipPayment.add(btn_paid, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 470, 120, 30));

        jLabel3.setText("Amount:");
        jPanelMemberShipPayment.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, 70, -1));
        jPanelMemberShipPayment.add(tx_amt, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 380, 140, -1));

        jLabel2.setText("Payment Type:");
        jPanelMemberShipPayment.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, -1, -1));

        tx_ptype.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "New Membership", "Renew Membership" }));
        jPanelMemberShipPayment.add(tx_ptype, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 340, 140, -1));

        checkExpireUsers.setText("Expired Users");
        checkExpireUsers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkExpireUsersMouseClicked(evt);
            }
        });
        jPanelMemberShipPayment.add(checkExpireUsers, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 60, 150, 30));

        tx_pid.setEditable(false);
        jPanelMemberShipPayment.add(tx_pid, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 300, 140, -1));

        jLabel1.setText("PID :");
        jPanelMemberShipPayment.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, 40, -1));

        jTabbedPanelFeedback2.addTab("Manage Membership", jPanelMemberShipPayment);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbl_latefines.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbl_latefines.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_latefinesMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tbl_latefines);

        jPanel1.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 620, 160));

        tx_fineSearch.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Search"));
        tx_fineSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tx_fineSearchKeyReleased(evt);
            }
        });
        jPanel1.add(tx_fineSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 180, -1));

        tx_ispaid.setText("Is Paid");
        jPanel1.add(tx_ispaid, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 480, 110, -1));

        tx_famt.setEditable(false);
        tx_famt.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Amount"));
        jPanel1.add(tx_famt, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 430, 160, -1));

        tx_fid.setEditable(false);
        tx_fid.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "FID"));
        jPanel1.add(tx_fid, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 290, 160, -1));

        tx_fuid.setEditable(false);
        tx_fuid.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "UID"));
        jPanel1.add(tx_fuid, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 290, 160, -1));

        tx_reason.setEditable(false);
        tx_reason.setColumns(20);
        tx_reason.setRows(5);
        tx_reason.setBorder(javax.swing.BorderFactory.createTitledBorder("Reason"));
        jScrollPane4.setViewportView(tx_reason);

        jPanel1.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, 360, 70));

        jButton8.setBackground(new java.awt.Color(0, 102, 102));
        jButton8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Save");
        jButton8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 480, 120, 30));

        jTabbedPanelFeedback2.addTab("Manage Late Fines", jPanel1);

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbl_payhistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane6.setViewportView(tbl_payhistory);

        jPanel2.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 750, 380));

        jTextField2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Search"));
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });
        jPanel2.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 210, -1));

        jTabbedPanelFeedback2.addTab(" Payment History", jPanel2);

        jPanelPayHistory.add(jTabbedPanelFeedback2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelToggle.add(jPanelPayHistory, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelSettings.setBackground(new java.awt.Color(255, 255, 255));
        jPanelSettings.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPanelSettings.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPanelSettings.setFocusable(false);
        jTabbedPanelSettings.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jPanelEditProfile.setBackground(new java.awt.Color(255, 255, 255));
        jPanelEditProfile.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextFieldDOB.setEditable(false);
        jTextFieldDOB.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldDOB.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Date of Birth"));
        jPanelEditProfile.add(jTextFieldDOB, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 100, 160, 40));

        jButtonSave.setBackground(new java.awt.Color(0, 102, 102));
        jButtonSave.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonSave.setForeground(new java.awt.Color(255, 255, 255));
        jButtonSave.setIcon(new javax.swing.ImageIcon("C:\\SLIIT\\LibraryManagementSystem\\src\\View\\Images\\reservebtn.png")); // NOI18N
        jButtonSave.setText("Save Changes");
        jButtonSave.setFocusable(false);
        jButtonSave.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonSave.setMargin(new java.awt.Insets(2, 0, 2, 5));
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        jPanelEditProfile.add(jButtonSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 280, 130, 30));

        jPasswordFieldConfirmPw.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Confirm New Password"));
        jPanelEditProfile.add(jPasswordFieldConfirmPw, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 430, 340, 40));

        jPasswordFieldCurrentPw.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Current Password"));
        jPanelEditProfile.add(jPasswordFieldCurrentPw, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 330, 340, 40));

        jPasswordFieldNewPw.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "New Password"));
        jPanelEditProfile.add(jPasswordFieldNewPw, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 380, 340, 40));

        jPanelProfilepic.setBackground(new java.awt.Color(255, 255, 255));
        jPanelProfilepic.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 51)));
        jPanelProfilepic.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanelProfilepic.add(jLabelPicture, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, -1, -1));

        jLabelUserEmail.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelUserEmail.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelUserEmail.setText("UserEmail");
        jPanelProfilepic.add(jLabelUserEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 220, 30));

        jPanelEditProfile.add(jPanelProfilepic, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 50, 250, 240));

        jTextFieldFullName.setEditable(false);
        jTextFieldFullName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldFullName.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Full Name"));
        jPanelEditProfile.add(jTextFieldFullName, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 340, 40));

        jTextFieldMembership.setEditable(false);
        jTextFieldMembership.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldMembership.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Membership"));
        jPanelEditProfile.add(jTextFieldMembership, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 220, 160, 40));

        jTextFieldNIC.setEditable(false);
        jTextFieldNIC.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldNIC.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "NIC"));
        jPanelEditProfile.add(jTextFieldNIC, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 100, 160, 40));

        jTextFieldContact.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldContact.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Contact Number"));
        jPanelEditProfile.add(jTextFieldContact, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 220, 160, 40));

        jTextFieldRegDate.setEditable(false);
        jTextFieldRegDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldRegDate.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Registered Date"));
        jPanelEditProfile.add(jTextFieldRegDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 160, 160, 40));

        jTextFieldExpiryDate.setEditable(false);
        jTextFieldExpiryDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldExpiryDate.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Expiry Date"));
        jPanelEditProfile.add(jTextFieldExpiryDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 160, 160, 40));

        jButtonChangePassword.setBackground(new java.awt.Color(0, 102, 102));
        jButtonChangePassword.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonChangePassword.setForeground(new java.awt.Color(255, 255, 255));
        jButtonChangePassword.setIcon(new javax.swing.ImageIcon("C:\\SLIIT\\LibraryManagementSystem\\src\\View\\Images\\reservebtn.png")); // NOI18N
        jButtonChangePassword.setText("Save Changes");
        jButtonChangePassword.setFocusable(false);
        jButtonChangePassword.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonChangePassword.setMargin(new java.awt.Insets(2, 0, 2, 5));
        jButtonChangePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonChangePasswordActionPerformed(evt);
            }
        });
        jPanelEditProfile.add(jButtonChangePassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 490, 130, 30));

        jTabbedPanelSettings.addTab("Edit User Profile", jPanelEditProfile);

        jPanelSettings.add(jTabbedPanelSettings, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelToggle.add(jPanelSettings, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jDesktopPaneLibraryMain.add(jPanelToggle, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 130, 780, 570));

        getContentPane().add(jDesktopPaneLibraryMain, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1022, 710));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonLogout11MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonLogout11MouseEntered
        // TODO add your handling code here:
        jButtonLogout.setBackground(new Color(24, 142, 130));
    }//GEN-LAST:event_jButtonLogout11MouseEntered

    private void jButtonLogout11MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonLogout11MouseExited
        // TODO add your handling code here:
        jButtonLogout.setBackground(new Color(0, 102, 102));
    }//GEN-LAST:event_jButtonLogout11MouseExited

    private void btnIssueBookTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIssueBookTabMouseEntered
        // TODO add your handling code here:
        btnIssueBookTab.setBackground(new Color(24, 142, 130));
    }//GEN-LAST:event_btnIssueBookTabMouseEntered

    private void btnIssueBookTabMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIssueBookTabMouseExited
        // TODO add your handling code here:
        btnIssueBookTab.setBackground(new Color(0, 102, 102));
    }//GEN-LAST:event_btnIssueBookTabMouseExited

    private void jButtonSettings11MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSettings11MouseEntered
        // TODO add your handling code here:
        jButtonSettings.setBackground(new Color(24, 142, 130));
    }//GEN-LAST:event_jButtonSettings11MouseEntered

    private void jButtonSettings11MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSettings11MouseExited
        // TODO add your handling code here:
        jButtonSettings.setBackground(new Color(0, 102, 102));
    }//GEN-LAST:event_jButtonSettings11MouseExited

    private void btnIssueBookTabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIssueBookTabActionPerformed
        try {
            togglePanels("ReserveBook");
            loadSupplierTable();
            tfSupplerID.setText(SupplierController.autoGenerateSupplierID());
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(LibraryManagerMain.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_btnIssueBookTabActionPerformed

    private void jButtonSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSettingsActionPerformed
        // TODO add your handling code here:
        togglePanels("Settings");
    }//GEN-LAST:event_jButtonSettingsActionPerformed

    private void jButtonLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogoutActionPerformed

        int result = JOptionPane.showConfirmDialog(rootPane, "Are you sure want to Logout?", "Confirm Logout", 0);

        if (result == 0) {
            this.dispose();
            SystemLogin login = new SystemLogin();
            login.setVisible(true);
        }
    }//GEN-LAST:event_jButtonLogoutActionPerformed

    private void tbIssueBookMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbIssueBookMouseClicked
        try {
            // TODO add your handling code here:
            loadSupplierTable();

        } catch (IOException ex) {
            Logger.getLogger(LibraryManagerMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tbIssueBookMouseClicked

    private void tblSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSupplierMouseClicked

        int row = tblSupplier.getSelectedRow();

        tfSupplerID.setText(tblSupplier.getValueAt(row, 0).toString());
        tfCompany.setText(tblSupplier.getValueAt(row, 1).toString());
        tfAddress.setText(tblSupplier.getValueAt(row, 2).toString());
        tfMobile.setText(tblSupplier.getValueAt(row, 3).toString());

//        try {
//            //tfIssueBBID.setText(BookController.autoGenerateBorrowedID());
//        } catch (ClassNotFoundException | SQLException ex) {
//            Logger.getLogger(LibraryManagerMain.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }//GEN-LAST:event_tblSupplierMouseClicked

    private void tfSearchSupplierKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfSearchSupplierKeyReleased
        try {
            Utility.filter(tblSupplier, tfSearchSupplier.getText());
        } catch (PatternSyntaxException ex) {

        }

    }//GEN-LAST:event_tfSearchSupplierKeyReleased

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        if (!tfSupplerID.getText().isEmpty()) {
            try {
                if (SupplierController.deleteSupplier(tfSupplerID.getText())) {
                    JOptionPane.showMessageDialog(rootPane, "Successfully deleted", "Deleted!", JOptionPane.INFORMATION_MESSAGE);
                    loadSupplierTable();
                    clearSupplierData();
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Invalid Supplier Id", "Error!", JOptionPane.ERROR_MESSAGE);

                }
            } catch (ClassNotFoundException | SQLException | IOException ex) {
                Logger.getLogger(LibraryManagerMain.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            JOptionPane.showMessageDialog(rootPane, "Supplier ID Can't be empty", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed

        if (!tfCompany.getText().isEmpty()) {
            if (!tfAddress.getText().isEmpty()) {
                if (Validation.isNumbers(tfMobile.getText()) && tfMobile.getText().length() == 10) {
                    try {
                        if (SupplierController.addSupplier(new SupplierModel(tfSupplerID.getText(), tfCompany.getText(), tfAddress.getText(), Integer.parseInt(tfMobile.getText())))) {
                            JOptionPane.showMessageDialog(rootPane, "Successfully added", "Success!", JOptionPane.INFORMATION_MESSAGE);
                            loadSupplierTable();
                            clearSupplierData();
                        } else {
                            JOptionPane.showMessageDialog(rootPane, "Error occured while inserting", "Error!", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (ClassNotFoundException | SQLException | IOException ex) {
                        Logger.getLogger(LibraryManagerMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "Invalid phone number", "Error!", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(rootPane, "Company Name can't be empty", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        if (!tfSupplerID.getText().isEmpty()) {
            if (!tfCompany.getText().isEmpty()) {
                if (!tfAddress.getText().isEmpty()) {
                    if (Validation.isNumbers(tfMobile.getText()) && (tfMobile.getText().length() == 10 || tfMobile.getText().length() == 9)) {
                        try {
                            if (SupplierController.editSupplier(new SupplierModel(tfSupplerID.getText(), tfCompany.getText(), tfAddress.getText(), Integer.parseInt(tfMobile.getText())))) {
                                JOptionPane.showMessageDialog(rootPane, "Successfully updated", "Success!", JOptionPane.INFORMATION_MESSAGE);
                                loadSupplierTable();
                                clearSupplierData();
                            } else {
                                JOptionPane.showMessageDialog(rootPane, "Invalid Supplier id", "Error!", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (ClassNotFoundException | SQLException | IOException ex) {
                            Logger.getLogger(LibraryManagerMain.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Invalid phone number", "Error!", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Invalid Address", "Error!", JOptionPane.ERROR_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(rootPane, "Company Name can't be empty", "Error!", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(rootPane, "Supplier ID Can't be empty", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void jButtonAddPayment11MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonAddPayment11MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAddPayment11MouseEntered

    private void jButtonAddPayment11MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonAddPayment11MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAddPayment11MouseExited

    private void jButtonAddPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddPaymentActionPerformed
        // TODO add your handling code here:
        togglePanels("AddPayment");
        fetchPayUsersDataToTable();
        fetchLateFinesDataToTable();

    }//GEN-LAST:event_jButtonAddPaymentActionPerformed

    private void jButtonSendMessage11MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSendMessage11MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonSendMessage11MouseEntered

    private void jButtonSendMessage11MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSendMessage11MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonSendMessage11MouseExited

    private void jButtonSendMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSendMessageActionPerformed
        // TODO add your handling code here:
        togglePanels("SendMessage");
    }//GEN-LAST:event_jButtonSendMessageActionPerformed

    private void jButtonReportedUsers11MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonReportedUsers11MouseEntered
        // TODO add your handling code here:
        jButtonReportedUsers.setBackground(new Color(24, 142, 130));
    }//GEN-LAST:event_jButtonReportedUsers11MouseEntered

    private void jButtonReportedUsers11MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonReportedUsers11MouseExited
        // TODO add your handling code here:
        jButtonReportedUsers.setBackground(new Color(0, 102, 102));
    }//GEN-LAST:event_jButtonReportedUsers11MouseExited

    private void jButtonReportedUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReportedUsersActionPerformed
        // TODO add your handling code here:
        togglePanels("Feedbacks");
        fetchReportedUserDataToTable();
    }//GEN-LAST:event_jButtonReportedUsersActionPerformed

    private void jButtonWarnusersMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonWarnusersMouseEntered
        // TODO add your handling code here:
        jButtonWarnusers.setBackground(new Color(24, 142, 130));
    }//GEN-LAST:event_jButtonWarnusersMouseEntered

    private void jButtonWarnusersMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonWarnusersMouseExited
        // TODO add your handling code here:
        jButtonWarnusers.setBackground(new Color(0, 102, 102));
    }//GEN-LAST:event_jButtonWarnusersMouseExited

    private void jButtonWarnusersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWarnusersActionPerformed
        // TODO add your handling code here:
        togglePanels("History");
        fetchWarnUserDataToTable();
    }//GEN-LAST:event_jButtonWarnusersActionPerformed

    private void jButtonDeleteButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonDeleteButtonMouseEntered
        // TODO add your handling code here:
        jButtonDeleteButton.setBackground(new Color(24, 142, 130));
    }//GEN-LAST:event_jButtonDeleteButtonMouseEntered

    private void jButtonDeleteButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonDeleteButtonMouseExited
        // TODO add your handling code here:
        jButtonDeleteButton.setBackground(new Color(0, 102, 102));
    }//GEN-LAST:event_jButtonDeleteButtonMouseExited

    private void jButtonDeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteButtonActionPerformed
        // TODO add your handling code here:
        togglePanels("PendingBooks");
        fetchDeleteUserDataToTable();
    }//GEN-LAST:event_jButtonDeleteButtonActionPerformed

    private void jButtonAddUsersMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonAddUsersMouseEntered
        // TODO add your handling code here:
        jButtonAddUsers.setBackground(new Color(24, 142, 130));
    }//GEN-LAST:event_jButtonAddUsersMouseEntered

    private void jButtonAddUsersMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonAddUsersMouseExited
        // TODO add your handling code here:
        jButtonAddUsers.setBackground(new Color(0, 102, 102));
    }//GEN-LAST:event_jButtonAddUsersMouseExited

    private void jButtonAddUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddUsersActionPerformed
        // TODO add your handling code here:
        togglePanels("RequestedList");
    }//GEN-LAST:event_jButtonAddUsersActionPerformed

    private void jButtonRegUsersMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonRegUsersMouseEntered
        // TODO add your handling code here:
        jButtonRegUsers.setBackground(new Color(24, 142, 130));
    }//GEN-LAST:event_jButtonRegUsersMouseEntered

    private void jButtonRegUsersMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonRegUsersMouseExited
        // TODO add your handling code here:
        jButtonRegUsers.setBackground(new Color(0, 102, 102));
    }//GEN-LAST:event_jButtonRegUsersMouseExited

    private void jButtonRegUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRegUsersActionPerformed
        togglePanels("USERS");
        fetchUserDataToTable();

    }//GEN-LAST:event_jButtonRegUsersActionPerformed

    private void jTabbedPaneSuppMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPaneSuppMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jTabbedPaneSuppMouseClicked

    private void tx_lnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tx_lnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tx_lnameActionPerformed

    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveActionPerformed
        // TODO add your handling code here:

        int selectedOption = JOptionPane.showConfirmDialog(null,
                "Do you want to add this member ?",
                "Exit",
                JOptionPane.YES_NO_OPTION);
        if (selectedOption == JOptionPane.YES_OPTION) {

            if (UserValidation.vefName(tx_fname.getText()) == true) {

                if (UserValidation.vefName(tx_lname.getText()) == true) {

                    if (UserValidation.vefDOB(tx_dob.getDate()) == true) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String DOB = dateFormat.format(tx_dob.getDate());

                        if (UserValidation.vefNIC(tx_nic.getText()) == true) {

                            if (Validation.isNumbers(tx_brrowcount.getText()) == true) {

                                if (Validation.isNumbers(tx_maxbook.getText()) == true) {
                                    if (UserValidation.vefPassword(new String(tx_psw.getPassword()), new String(tx_cpsw.getPassword())) == true) {

                                        MD5 md = new MD5();
                                        String encryptedPassword = md.getMD5(new String(tx_psw.getPassword()));

                                        if (UserValidation.vefPhone(tx_phone.getText()) == true) {

                                            if (UserValidation.vefAddress(tx_address.getText()) == true) {

                                                if (UserValidation.validateEmail(tx_email.getText()) == true) {

                                                    UserController user = new UserController();
                                                    try {
                                                        if (UserValidation.checkNIC(tx_nic.getText()) == false) {

                                                            if (UserValidation.checkEmail(tx_email.getText()) == false) {

                                                                if (UserValidation.checkPhone(tx_phone.getText()) == false) {
                                                                    user.addUser(new UserModel(tx_userid.getText(), encryptedPassword, tx_fname.getText(), tx_lname.getText(), tx_nic.getText(), DOB, tx_address.getText(), tx_phone.getText(), tx_email.getText(), (String) tx_usertype.getSelectedItem(), tx_maxbook.getText(), tx_brrowcount.getText(), formatDate.format(regcalendar.getTime()).toString(), tx_expdate.getText()));
                                                                    refreshData();
                                                                    JOptionPane.showMessageDialog(null, "Registration successfully compeleted! ");

                                                                } else {
                                                                    JOptionPane.showMessageDialog(null, "This Phone number already taken! ");
                                                                }
                                                            } else {
                                                                JOptionPane.showMessageDialog(null, "This Email already taken! ");
                                                            }
                                                        } else {
                                                            JOptionPane.showMessageDialog(null, "This NIC already taken! ");
                                                        }
                                                    } catch (ClassNotFoundException | SQLException ex) {

                                                        LOG.info("SQL Error: " + ex.getMessage());
                                                    } catch (IOException ex) {

                                                        LOG.info("SQL Error: " + ex.getMessage());

                                                    }

                                                }
                                            }
                                        }
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Please enter numbers only for max book count", "Warning!", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Please enter numbers only for borrowed book count", "Warning!", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_btn_saveActionPerformed

    private void tx_cpswActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tx_cpswActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tx_cpswActionPerformed

    private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteActionPerformed
        // TODO add your handling code here:
        DefaultTableModel tablemodel = (DefaultTableModel) tbl_deleteusers.getModel();

        int column = 0;
        int row = tbl_deleteusers.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(null, "Please select the record you want to delete");

        } else {

            int selectedOption = JOptionPane.showConfirmDialog(null,
                    "Do you want to Delete this member ?",
                    "Exit",
                    JOptionPane.YES_NO_OPTION);
            if (selectedOption == JOptionPane.YES_OPTION) {
                String value = tbl_deleteusers.getModel().getValueAt(row, column).toString();

                UserController user = new UserController();
                try {
                    user.deleteUser(value);
                    JOptionPane.showMessageDialog(null, "Record has been deleted successfully ");
                    tablemodel.removeRow(tbl_deleteusers.getSelectedRow());

                } catch (ClassNotFoundException | SQLException ex) {

//                    log.info("SQL Error: " + ex.getMessage());
                } catch (IOException ex) {
                    Logger.getLogger(LibraryManagerMain.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }//GEN-LAST:event_btn_deleteActionPerformed

    private void btn_warnsubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_warnsubmitActionPerformed
        // TODO add your handling code here:

        DefaultTableModel tablemodel = (DefaultTableModel) tbl_warnusers.getModel();

        int column = 0;
        int column1 = 1;
        int column2 = 2;

        int row = tbl_warnusers.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(null, "Please select the user you want to send warning!");

        } else {

            int selectedOption = JOptionPane.showConfirmDialog(null,
                    "Do you want to send this warn ?",
                    "Exit",
                    JOptionPane.YES_NO_OPTION);
            if (selectedOption == JOptionPane.YES_OPTION) {
                String uid = tbl_warnusers.getModel().getValueAt(row, column).toString();

                String fname = tbl_warnusers.getModel().getValueAt(row, column1).toString();

                String lname = tbl_warnusers.getModel().getValueAt(row, column2).toString();

                String fullname = fname + " " + lname;
                if (tx_warnnote.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please type a warning note! ");
                } else {

                    UserController user = new UserController();
                    try {
                        user.addWarnsToUsers(new WarnedListModel(tx_wid.getText(), uid, fullname, tx_warnnote.getText(), (String) tx_warntype.getSelectedItem()));
                        JOptionPane.showMessageDialog(null, "Warning sent! ");

                    } catch (ClassNotFoundException | SQLException ex) {

//                    log.info("SQL Error: " + ex.getMessage());
                    } catch (IOException ex) {
                        Logger.getLogger(LibraryManagerMain.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        }

    }//GEN-LAST:event_btn_warnsubmitActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed

        try {
            String contact = jTextFieldContact.getText();
            if (contact.isEmpty() || contact.length() != 10 || !Validation.isNumbers(contact)) {
                JOptionPane.showMessageDialog(rootPane, "Please enter valid contact "
                        + "number", "Error!", JOptionPane.ERROR_MESSAGE);
            } else if (UserController.updateUser(UID, contact)) {
                JOptionPane.showMessageDialog(rootPane, "Details Updated Successfully!",
                        "Success!", JOptionPane.PLAIN_MESSAGE);
//                LOG.info("User details successfully updated to the database by user: " + UID);
            } else {
                JOptionPane.showMessageDialog(rootPane, "Error occured while Updating",
                        "Error!", JOptionPane.ERROR_MESSAGE);
//                LOG.info("Error occurred while updating to the database");
            }

        } catch (ClassNotFoundException | SQLException | IOException ex) {
//            LOG.error("Failed to save user details\n" + ex);
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonChangePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonChangePasswordActionPerformed
        try {

            UserModel user = new UserModel();
            user.setUid(UID);
            user.setPassword(jPasswordFieldCurrentPw.getText());

            if (UserController.checkPasswordforReset(user)) {
                if (UserValidation.vefPassword(jPasswordFieldNewPw.getText(),
                        jPasswordFieldConfirmPw.getText())) {
                    if (!jPasswordFieldCurrentPw.getText().equals(jPasswordFieldNewPw.getText())) {
                        if (UserController.updatePassword(UID, jPasswordFieldNewPw.getText())) {
                            JOptionPane.showMessageDialog(rootPane, "Password Updated "
                                    + "Successfully!", "Success!", JOptionPane.PLAIN_MESSAGE);
                            jPasswordFieldCurrentPw.setText("");
                            jPasswordFieldNewPw.setText("");
                            jPasswordFieldConfirmPw.setText("");
                            Utility.sendEmail(USER, "passwordchange");
//                        LOG.info("Password successfully updated to the database by user: " + UID);
                        }

                    } else {
                        JOptionPane.showMessageDialog(rootPane, "New password is "
                                + "same as the current password!", "Error!", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "Please enter your "
                        + "current password!", "Error!", JOptionPane.ERROR_MESSAGE);
            }

        } catch (ClassNotFoundException | SQLException | IOException ex) {
//            LOG.error("Failed to reset password by user " + UID + "\n" + ex);
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButtonChangePasswordActionPerformed

    private void tx_brrowcountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tx_brrowcountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tx_brrowcountActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            // TODO add your handling code here:
            refreshData();
        } catch (IOException ex) {
            Logger.getLogger(LibraryManagerMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        try {
            // TODO add your handling code here:
            demoData();
        } catch (IOException ex) {
            Logger.getLogger(LibraryManagerMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void checkExpireUsersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkExpireUsersMouseClicked
        // TODO add your handling code here:
        if (checkExpireUsers.isSelected()) {

            expireStatus();

        } else {

            fetchPayUsersDataToTable();
        }

    }//GEN-LAST:event_checkExpireUsersMouseClicked

    private void btn_paidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_paidActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:

        DefaultTableModel tablemodel = (DefaultTableModel) tbl_payusers.getModel();

        int column = 0;
        int column5 = 4;

        int row = tbl_payusers.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(null, "Please select the user you want to send warning!");

        } else {

            int selectedOption = JOptionPane.showConfirmDialog(null,
                    "Do you want to send this warn ?",
                    "Exit",
                    JOptionPane.YES_NO_OPTION);
            if (selectedOption == JOptionPane.YES_OPTION) {
                String uid = tbl_payusers.getModel().getValueAt(row, column).toString();
                String expDate = tbl_payusers.getModel().getValueAt(row, column5).toString();

//                DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                Date newDate = null;
                try {

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = sdf.parse(expDate);
                    Date date2 = sdf.parse(formatDate.format(SystemDate));

                    if (date1.compareTo(date2) < 0) {
                        calendar.setTime(SystemDate);
                        calendar.add(Calendar.YEAR, 1);
                        newDate = calendar.getTime();
                    } else if (date1.compareTo(date2) > 0) {
                        Date date = formatDate.parse(expDate);
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        c.add(Calendar.YEAR, 1);
                        newDate = c.getTime();
                    }

                    System.out.println(formatDate.format(newDate));

                } catch (ParseException ex) {
                    Logger.getLogger(LibraryManagerMain.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (tx_amt.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please type a amount! ");
                } else if (Validation.isNumbers(tx_amt.getText()) == true) {

                    UserController user = new UserController();
                    try {
                        if (user.addPayments(new PaymentModel(tx_pid.getText(), uid, (String) tx_ptype.getSelectedItem(), Double.parseDouble(tx_amt.getText())))) {
                            tx_pid.setText(UserController.autoGeneratePayID());
                            fetchPaymentHistoryToTable();
                            JOptionPane.showMessageDialog(null, "Succuessfully payment added! ");
                        }

                        if (tx_ptype.getSelectedIndex() == 1) {

                            user.updateExpireDate(formatDate.format(newDate), uid);

                        } else if (tx_ptype.getSelectedIndex() == 0) {

                            user.updateUserStatus("Active", uid);

                        }

                    } catch (ClassNotFoundException | SQLException ex) {

                        log.info("SQL Error: " + ex.getMessage());
                    } catch (IOException ex) {
                        Logger.getLogger(LibraryManagerMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {

                    JOptionPane.showMessageDialog(null, "Amount should be numbers only ! ");
                }
            }
        }
    }

    private void btn_tsistActionPerformed(java.awt.event.ActionEvent evt) {

    }//GEN-LAST:event_btn_paidActionPerformed

    private void tbl_latefinesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_latefinesMouseClicked
        // TODO add your handling code here:
        DefaultTableModel mTable = (DefaultTableModel) tbl_latefines.getModel();
        int selectedRow = tbl_latefines.getSelectedRow();

        tx_fid.setText(mTable.getValueAt(selectedRow, 0).toString());
        tx_fuid.setText(mTable.getValueAt(selectedRow, 1).toString());
        tx_reason.setText(mTable.getValueAt(selectedRow, 4).toString());
        tx_famt.setText(mTable.getValueAt(selectedRow, 5).toString());
        boolean selected = false;
        if (mTable.getValueAt(selectedRow, 6).toString().equals("true")) {
            selected = true;
        } else {
            selected = false;
        }
        tx_ispaid.setSelected(selected);

    }//GEN-LAST:event_tbl_latefinesMouseClicked

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        String value = "";
        if (tx_ispaid.isSelected()) {
            value = "1";
        } else {
            value = "0";
        }

        try {
            UserController.updateLateFineStatus(value, tx_fuid.getText());
            fetchLateFinesDataToTable();
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(LibraryManagerMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void tx_fineSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tx_fineSearchKeyReleased
        // TODO add your handling code here:
        Utility.filter(tbl_latefines, tx_fineSearch.getText());
    }//GEN-LAST:event_tx_fineSearchKeyReleased

    private void tx_memberSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tx_memberSearchKeyReleased
        // TODO add your handling code here:

        Utility.filter(tbl_payusers, tx_memberSearch.getText());
    }//GEN-LAST:event_tx_memberSearchKeyReleased

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        // TODO add your handling code here:
        Utility.filter(tbl_payhistory, jTextField2.getText());
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        try {
            Utility.sendMessage(tfAuthorName9.getText(), tfAuthorName8.getText()
                    + " : " + tfAuthorName7.getText(), jTextArea2.getText());
        } catch (IOException ex) {
            Logger.getLogger(LibraryManagerMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTextFieldISBN1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldISBN1KeyReleased
        // TODO add your handling code here:

        Utility.filter(tbl_regusers, jTextFieldISBN1.getText());
    }//GEN-LAST:event_jTextFieldISBN1KeyReleased

    private void tx_search_nameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tx_search_nameKeyReleased
        // TODO add your handling code here:
        Utility.filter(tbl_deleteusers, tx_search_name.getText());
    }//GEN-LAST:event_tx_search_nameKeyReleased

    private void tfAuthorName4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfAuthorName4KeyReleased
        // TODO add your handling code here:
        Utility.filter(tbl_reportedusers, tfAuthorName4.getText());
    }//GEN-LAST:event_tfAuthorName4KeyReleased

    private void tfAuthorName2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfAuthorName2KeyReleased
        // TODO add your handling code here:
        Utility.filter(tbl_warnusers, tfAuthorName2.getText());
    }//GEN-LAST:event_tfAuthorName2KeyReleased

    private void tbl_sendmailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_sendmailMouseClicked
        // TODO add your handling code here:
        DefaultTableModel mTable = (DefaultTableModel) tbl_sendmail.getModel();
        int selectedRow = tbl_sendmail.getSelectedRow();

        tfAuthorName7.setText(mTable.getValueAt(selectedRow, 1).toString() + " " + mTable.getValueAt(selectedRow, 2).toString());
        tfAuthorName9.setText(mTable.getValueAt(selectedRow, 4).toString());


    }//GEN-LAST:event_tbl_sendmailMouseClicked

    private void tx_searchmailKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tx_searchmailKeyReleased
        // TODO add your handling code here:

        Utility.filter(tbl_sendmail, tx_searchmail.getText());
    }//GEN-LAST:event_tx_searchmailKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LibraryManagerMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            try {
                new LibraryManagerMain(USER, UID).setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(LibraryManagerMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnIssueBookTab;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JButton btn_delete;
    private javax.swing.JButton btn_paid;
    private javax.swing.JButton btn_save;
    private javax.swing.JButton btn_warnsubmit;
    private javax.swing.JCheckBox checkExpireUsers;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonAddPayment;
    private javax.swing.JButton jButtonAddUsers;
    private javax.swing.JButton jButtonChangePassword;
    private javax.swing.JButton jButtonDeleteButton;
    private javax.swing.JButton jButtonLogout;
    private javax.swing.JButton jButtonRegUsers;
    private javax.swing.JButton jButtonReportedUsers;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonSendMessage;
    private javax.swing.JButton jButtonSettings;
    private javax.swing.JButton jButtonWarnusers;
    private javax.swing.JDesktopPane jDesktopPaneLibraryMain;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelLogo;
    private javax.swing.JLabel jLabelPicture;
    private javax.swing.JLabel jLabelUserEmail;
    private javax.swing.JLabel jLabelWelcomeUser;
    private javax.swing.JLabel jLableTime;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelAddUser;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelDelete;
    private javax.swing.JPanel jPanelDeleteUser;
    private javax.swing.JPanel jPanelEditProfile;
    private javax.swing.JPanel jPanelEmail;
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JPanel jPanelMemberShipPayment;
    private javax.swing.JPanel jPanelPayHistory;
    private javax.swing.JPanel jPanelProfilepic;
    private javax.swing.JPanel jPanelRegisterdUsers;
    private javax.swing.JPanel jPanelReported;
    private javax.swing.JPanel jPanelReportedUsers;
    private javax.swing.JPanel jPanelSearchBookTab;
    private javax.swing.JPanel jPanelSendEmail;
    private javax.swing.JPanel jPanelSettings;
    private javax.swing.JPanel jPanelSupplier;
    private javax.swing.JPanel jPanelToggle;
    private javax.swing.JPanel jPanelUser;
    private javax.swing.JPanel jPanelWarn;
    private javax.swing.JPanel jPanelWarnUsers;
    private javax.swing.JPasswordField jPasswordFieldConfirmPw;
    private javax.swing.JPasswordField jPasswordFieldCurrentPw;
    private javax.swing.JPasswordField jPasswordFieldNewPw;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPanePendingReturn;
    private javax.swing.JTabbedPane jTabbedPaneRequestedList;
    private javax.swing.JTabbedPane jTabbedPaneSupp;
    private javax.swing.JTabbedPane jTabbedPanelFeedback;
    private javax.swing.JTabbedPane jTabbedPanelFeedback1;
    private javax.swing.JTabbedPane jTabbedPanelFeedback2;
    private javax.swing.JTabbedPane jTabbedPanelHistory;
    private javax.swing.JTabbedPane jTabbedPanelSettings;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextFieldContact;
    private javax.swing.JTextField jTextFieldDOB;
    private javax.swing.JTextField jTextFieldExpiryDate;
    private javax.swing.JTextField jTextFieldFullName;
    private javax.swing.JTextField jTextFieldISBN1;
    private javax.swing.JTextField jTextFieldMembership;
    private javax.swing.JTextField jTextFieldNIC;
    private javax.swing.JTextField jTextFieldRegDate;
    private javax.swing.JPanel pnlIssueBook;
    private javax.swing.JTabbedPane tbIssueBook;
    private javax.swing.JTable tblSupplier;
    private javax.swing.JTable tbl_deleteusers;
    private javax.swing.JTable tbl_latefines;
    private javax.swing.JTable tbl_payhistory;
    private javax.swing.JTable tbl_payusers;
    private javax.swing.JTable tbl_regusers;
    private javax.swing.JTable tbl_reportedusers;
    private javax.swing.JTable tbl_sendmail;
    private javax.swing.JTable tbl_warnusers;
    private javax.swing.JTextField tfAddress;
    private javax.swing.JTextField tfAuthorName2;
    private javax.swing.JTextField tfAuthorName4;
    private javax.swing.JTextField tfAuthorName7;
    private javax.swing.JTextField tfAuthorName8;
    private javax.swing.JTextField tfAuthorName9;
    private javax.swing.JTextField tfCompany;
    private javax.swing.JTextField tfMobile;
    private javax.swing.JTextField tfSearchSupplier;
    private javax.swing.JTextField tfSupplerID;
    private javax.swing.JTextArea tx_address;
    private javax.swing.JTextField tx_amt;
    private javax.swing.JTextField tx_brrowcount;
    private javax.swing.JPasswordField tx_cpsw;
    private com.toedter.calendar.JDateChooser tx_dob;
    private javax.swing.JTextField tx_email;
    private javax.swing.JTextField tx_expdate;
    private javax.swing.JTextField tx_famt;
    private javax.swing.JTextField tx_fid;
    private javax.swing.JTextField tx_fineSearch;
    private javax.swing.JTextField tx_fname;
    private javax.swing.JTextField tx_fuid;
    private javax.swing.JCheckBox tx_ispaid;
    private javax.swing.JTextField tx_lname;
    private javax.swing.JTextField tx_maxbook;
    private javax.swing.JTextField tx_memberSearch;
    private javax.swing.JTextField tx_nic;
    private javax.swing.JTextField tx_phone;
    private javax.swing.JTextField tx_pid;
    private javax.swing.JPasswordField tx_psw;
    private javax.swing.JComboBox<String> tx_ptype;
    private javax.swing.JTextArea tx_reason;
    private javax.swing.JTextField tx_search_name;
    private javax.swing.JTextField tx_searchmail;
    private javax.swing.JTextField tx_userid;
    private javax.swing.JComboBox<String> tx_usertype;
    private javax.swing.JTextArea tx_warnnote;
    private javax.swing.JComboBox<String> tx_warntype;
    private javax.swing.JTextField tx_wid;
    // End of variables declaration//GEN-END:variables
}
