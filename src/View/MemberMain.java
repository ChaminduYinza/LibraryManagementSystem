/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.FeedbackController;
import Controller.RequestedBookController;
import Controller.SuggestionController;
import Controller.UserController;
import static Controller.UserController.updateUser;
import Model.BookFeedbackModel;
import Model.RequestedBookListModel;
import Model.SuggestionsModel;
import Model.UserModel;
import Util.Config;
import Util.Utility;
import Validation.UserValidation;
import Validation.Validation;
import java.awt.Color;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Sameera
 */
public class MemberMain extends javax.swing.JFrame {

    /**
     * Creates new form LibraryMain
     */
    public static final org.apache.log4j.Logger LOG = Config.getLogger(MemberMain.class);
    static String USER;
    static String UID;
    String rID = null;
    String bID = null;
    String bookName = null;

    public MemberMain(String userName, String uid) {
        initComponents();

        USER = userName;
        UID = uid;
        jLabelWelcomeUser.setText("Welcome " + USER);
        jPanelReserveBook.setVisible(false);
        jPanelRequestedBookList.setVisible(false);
        jPanelPendingReturn.setVisible(false);
        jPanelHsitory.setVisible(false);
        jPanelPayments.setVisible(false);
        jPanelFeedback.setVisible(false);
        jPanelSettings.setVisible(false);
        Util.Utility.clock(jLableTime);

    }

    //this method call for show and hide toggle panels when button click
    private void togglePanels(String buttonName) {
        switch (buttonName) {
            case "ReserveBook":
                jPanelReserveBook.setVisible(true);
                jPanelRequestedBookList.setVisible(false);
                jPanelPendingReturn.setVisible(false);
                jPanelHsitory.setVisible(false);
                jPanelPayments.setVisible(false);
                jPanelFeedback.setVisible(false);
                jPanelSettings.setVisible(false);
                break;
            case "RequestedList":
                jPanelReserveBook.setVisible(false);
                jPanelRequestedBookList.setVisible(true);
                jPanelPendingReturn.setVisible(false);
                jPanelHsitory.setVisible(false);
                jPanelPayments.setVisible(false);
                jPanelFeedback.setVisible(false);
                jPanelSettings.setVisible(false);
                break;
            case "PendingBooks":
                jPanelReserveBook.setVisible(false);
                jPanelRequestedBookList.setVisible(false);
                jPanelPendingReturn.setVisible(true);
                jPanelHsitory.setVisible(false);
                jPanelPayments.setVisible(false);
                jPanelFeedback.setVisible(false);
                jPanelSettings.setVisible(false);
                break;
            case "History":
                jPanelReserveBook.setVisible(false);
                jPanelRequestedBookList.setVisible(false);
                jPanelPendingReturn.setVisible(false);
                jPanelHsitory.setVisible(true);
                jPanelPayments.setVisible(false);
                jPanelFeedback.setVisible(false);
                jPanelSettings.setVisible(false);
                break;
            case "Payments":
                jPanelReserveBook.setVisible(false);
                jPanelRequestedBookList.setVisible(false);
                jPanelPendingReturn.setVisible(false);
                jPanelHsitory.setVisible(false);
                jPanelPayments.setVisible(true);
                jPanelFeedback.setVisible(false);
                jPanelSettings.setVisible(false);
                break;
            case "Feedbacks":
                jPanelReserveBook.setVisible(false);
                jPanelRequestedBookList.setVisible(false);
                jPanelPendingReturn.setVisible(false);
                jPanelHsitory.setVisible(false);
                jPanelPayments.setVisible(false);
                jPanelFeedback.setVisible(true);
                jPanelSettings.setVisible(false);
                break;
            case "Settings":
                jPanelReserveBook.setVisible(false);
                jPanelRequestedBookList.setVisible(false);
                jPanelPendingReturn.setVisible(false);
                jPanelHsitory.setVisible(false);
                jPanelPayments.setVisible(false);
                jPanelFeedback.setVisible(false);
                jPanelSettings.setVisible(true);
                break;

            default:
                break;
        }
    }

    private void loadReserveBookData() throws IOException {
        try {
            Util.Utility.loadTable(jTable1, "SELECT BID, BOOKNAME"
                    + " AS TITLE,AUTHOR,ISBN,DESCRIPTION,AVAILABILITY,VERSION FROM BOOKS");
        } catch (ClassNotFoundException | SQLException ex) {
            LOG.error("Failed to load Reserved book data data to the table\n" + ex);
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void loadRequestedBookData() throws IOException {
        try {
            Util.Utility.loadTable(jTableRequestedList, "SELECT r.RID AS REQID, b.BID AS BOOKID, "
                    + "r.BOOKNAME AS TITLE, b.AUTHOR, b.ISBN, b.DESCRIPTION, "
                    + "b.VERSION FROM BOOKS b, REQUESTED_BOOK_LIST r WHERE "
                    + "b.BID = r.BID and r.UID = '" + UID + "'");
        } catch (ClassNotFoundException | SQLException ex) {
            LOG.error("Failed to load Requested book list data to the table\n" + ex);
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void loadPendingBookData() throws IOException {
        try {
            Util.Utility.loadTable(jTablePendingReturnBooks, "SELECT b.BID, "
                    + "b.BOOKNAME AS TITLE, b.AUTHOR, b.ISBN, b.DESCRIPTION, "
                    + "b.VERSION FROM BOOKS b, BORROWEDBOOKS r WHERE "
                    + "b.BID = r.BID and r.UID = '" + UID + "'");
        } catch (ClassNotFoundException | SQLException ex) {
            LOG.error("Failed to load Pending book data to the table\n" + ex);
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void loadHistoryBookData() throws IOException {
        try {
            Util.Utility.loadTable(jTableHistoryBooks, "SELECT b.BID, "
                    + "b.BOOKNAME AS TITLE, b.AUTHOR, b.ISBN, b.DESCRIPTION, "
                    + "b.VERSION FROM BOOKS b, BORROWEDBOOKS r WHERE "
                    + "b.BID = r.BID AND r.UID = '" + UID + "' AND r.ISSUBBMITTED = 1");
        } catch (ClassNotFoundException | SQLException ex) {
            LOG.error("Failed to load History book data to the table\n" + ex);
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void loadPaymentHistory() throws IOException {
        try {
            Util.Utility.loadTable(jTablePaymentHistory, "SELECT PID AS PAYMENTID,"
                    + " UID AS USERID, PAYMENTTYPE, AMOUNT, NO_OF_DAYS FROM PAYMENT"
                    + " WHERE UID = '" + UID + "'");
        } catch (ClassNotFoundException | SQLException ex) {
            LOG.error("Failed to load payment data to the table\n" + ex);
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void loadFeedback() throws IOException {
        try {
            Util.Utility.loadTable(jTableFeedbacks, "SELECT f.FID , b.BID "
                    + "AS BOOKID, b.BOOKNAME, "
                    + "f.FEEDBACK, b.AUTHOR, b.ISBN, f.FULLNAME, "
                    + "b.VERSION FROM BOOKS b, BOOKFEEDBACKS f WHERE "
                    + "b.BID = f.BID AND ISAPPROVED = 1");
        } catch (ClassNotFoundException | SQLException ex) {
            LOG.error("Failed to load Feedback data to the table\n" + ex);
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
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
        jButtonReserveBook = new javax.swing.JButton();
        jButtonRequestedBookList = new javax.swing.JButton();
        jButtonPendingReturn = new javax.swing.JButton();
        jButtonHistory = new javax.swing.JButton();
        jButtonPayments = new javax.swing.JButton();
        jButtonFeedback = new javax.swing.JButton();
        jButtonLogout = new javax.swing.JButton();
        jButtonSettings = new javax.swing.JButton();
        jPanelToggle = new javax.swing.JPanel();
        jPanelReserveBook = new javax.swing.JPanel();
        jTabbedPaneReserveBook = new javax.swing.JTabbedPane();
        jPanelSearchBookTab = new javax.swing.JPanel();
        jTextFieldSearchBook = new javax.swing.JTextField();
        jScrollPaneSearchBook = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabelSearchBookInfo = new javax.swing.JLabel();
        jPanelReserveBookTab = new javax.swing.JPanel();
        jTextFieldBookId = new javax.swing.JTextField();
        jTextFieldAvailability = new javax.swing.JTextField();
        jTextFieldReserveISBN = new javax.swing.JTextField();
        jScrollPaneDescription = new javax.swing.JScrollPane();
        jTextAreaDescription = new javax.swing.JTextArea();
        jTextFieldAuthor = new javax.swing.JTextField();
        jTextFieldReserveBookName = new javax.swing.JTextField();
        jButtonConfirmReservation = new javax.swing.JButton();
        jTextFieldRequestedBookId = new javax.swing.JTextField();
        jTextFieldRsvBookUID = new javax.swing.JTextField();
        jPanelRequestedBookList = new javax.swing.JPanel();
        jTabbedPaneRequestedList = new javax.swing.JTabbedPane();
        jPanelRequestedList = new javax.swing.JPanel();
        jScrollPaneRequestedBooks = new javax.swing.JScrollPane();
        jTableRequestedList = new javax.swing.JTable();
        jButtonCancelRequest = new javax.swing.JButton();
        jTextFieldSearchRequestedBook = new javax.swing.JTextField();
        jPanelPendingReturn = new javax.swing.JPanel();
        jTabbedPanePendingReturn = new javax.swing.JTabbedPane();
        jPanelPendingBooks = new javax.swing.JPanel();
        jScrollPanePendingBooks = new javax.swing.JScrollPane();
        jTablePendingReturnBooks = new javax.swing.JTable();
        jTextFieldSearchPendingReturnBooks = new javax.swing.JTextField();
        jPanelHsitory = new javax.swing.JPanel();
        jTabbedPanelHistory = new javax.swing.JTabbedPane();
        jPanelHistory = new javax.swing.JPanel();
        jScrollPaneHistory = new javax.swing.JScrollPane();
        jTableHistoryBooks = new javax.swing.JTable();
        jButtonSubmitFeedback = new javax.swing.JButton();
        jLabelHistoryInfo = new javax.swing.JLabel();
        jTextFieldSearchHistoryBooks = new javax.swing.JTextField();
        jPanelFeedback = new javax.swing.JPanel();
        jTabbedPanelFeedback = new javax.swing.JTabbedPane();
        jPanelFeedbacks = new javax.swing.JPanel();
        jScrollPaneFeedbacks = new javax.swing.JScrollPane();
        jTableFeedbacks = new javax.swing.JTable();
        jScrollPaneFeedbackDes = new javax.swing.JScrollPane();
        jTextAreaFeedbackInfo = new javax.swing.JTextArea();
        jTextFieldSearchFeedback = new javax.swing.JTextField();
        jPanelSubmitFeedback = new javax.swing.JPanel();
        jTextFieldBookIdFeedback = new javax.swing.JTextField();
        jTextFieldFeedbackFullName = new javax.swing.JTextField();
        jScrollPaneDescriptionFeedback = new javax.swing.JScrollPane();
        jTextAreaDescriptionFeedback = new javax.swing.JTextArea();
        jTextFieldFID = new javax.swing.JTextField();
        jTextFieldFeedbackBookName = new javax.swing.JTextField();
        jButtonFeedbackSumbit = new javax.swing.JButton();
        jPanelSubmitSuggestions = new javax.swing.JPanel();
        jTextFieldSuggestionUid = new javax.swing.JTextField();
        jScrollPaneSuggestionDes = new javax.swing.JScrollPane();
        jTextAreaSuggestionDes = new javax.swing.JTextArea();
        jTextFieldSuggestionId = new javax.swing.JTextField();
        jButtonSubmitSuggestion = new javax.swing.JButton();
        jPanelPayments = new javax.swing.JPanel();
        jTabbedPanelPayments = new javax.swing.JTabbedPane();
        jPanelPayment = new javax.swing.JPanel();
        jScrollPanePaymentHistory = new javax.swing.JScrollPane();
        jTablePaymentHistory = new javax.swing.JTable();
        jTextFieldSearchPayment = new javax.swing.JTextField();
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
        setTitle("Library Management System : Member");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jDesktopPaneLibraryMain.setBackground(new java.awt.Color(255, 255, 255));
        jDesktopPaneLibraryMain.setForeground(new java.awt.Color(255, 255, 255));
        jDesktopPaneLibraryMain.setPreferredSize(new java.awt.Dimension(1022, 710));
        jDesktopPaneLibraryMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelHeader.setBackground(new java.awt.Color(0, 102, 102));
        jPanelHeader.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelLogo.setIcon(new javax.swing.ImageIcon("C:\\SLIIT\\LibraryManagementSystem-master\\src\\View\\Images\\librarylogo.png")); // NOI18N
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

        jButtonReserveBook.setBackground(new java.awt.Color(0, 102, 102));
        jButtonReserveBook.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        jButtonReserveBook.setForeground(new java.awt.Color(255, 255, 255));
        jButtonReserveBook.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/menuBtn.png"))); // NOI18N
        jButtonReserveBook.setText("RESERVE BOOK");
        jButtonReserveBook.setFocusPainted(false);
        jButtonReserveBook.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonReserveBook.setIconTextGap(5);
        jButtonReserveBook.setMargin(new java.awt.Insets(2, 0, 2, 5));
        jButtonReserveBook.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonReserveBookMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonReserveBookMouseExited(evt);
            }
        });
        jButtonReserveBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReserveBookActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonReserveBook, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 190, 40));

        jButtonRequestedBookList.setBackground(new java.awt.Color(0, 102, 102));
        jButtonRequestedBookList.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        jButtonRequestedBookList.setForeground(new java.awt.Color(255, 255, 255));
        jButtonRequestedBookList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/menuBtn.png"))); // NOI18N
        jButtonRequestedBookList.setText("REQUESTED LIST");
        jButtonRequestedBookList.setFocusPainted(false);
        jButtonRequestedBookList.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonRequestedBookList.setIconTextGap(5);
        jButtonRequestedBookList.setMargin(new java.awt.Insets(2, 0, 2, 5));
        jButtonRequestedBookList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonRequestedBookListMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonRequestedBookListMouseExited(evt);
            }
        });
        jButtonRequestedBookList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRequestedBookListActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonRequestedBookList, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 190, 40));

        jButtonPendingReturn.setBackground(new java.awt.Color(0, 102, 102));
        jButtonPendingReturn.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        jButtonPendingReturn.setForeground(new java.awt.Color(255, 255, 255));
        jButtonPendingReturn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/menuBtn.png"))); // NOI18N
        jButtonPendingReturn.setText("PENDING BOOKS");
        jButtonPendingReturn.setFocusPainted(false);
        jButtonPendingReturn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonPendingReturn.setIconTextGap(5);
        jButtonPendingReturn.setMargin(new java.awt.Insets(2, 0, 2, 5));
        jButtonPendingReturn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonPendingReturnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonPendingReturnMouseExited(evt);
            }
        });
        jButtonPendingReturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPendingReturnActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonPendingReturn, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 190, 40));

        jButtonHistory.setBackground(new java.awt.Color(0, 102, 102));
        jButtonHistory.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        jButtonHistory.setForeground(new java.awt.Color(255, 255, 255));
        jButtonHistory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/menuBtn.png"))); // NOI18N
        jButtonHistory.setText("HISTORY");
        jButtonHistory.setFocusPainted(false);
        jButtonHistory.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonHistory.setIconTextGap(5);
        jButtonHistory.setMargin(new java.awt.Insets(2, 0, 2, 5));
        jButtonHistory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonHistoryMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonHistoryMouseExited(evt);
            }
        });
        jButtonHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHistoryActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonHistory, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 190, 40));

        jButtonPayments.setBackground(new java.awt.Color(0, 102, 102));
        jButtonPayments.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        jButtonPayments.setForeground(new java.awt.Color(255, 255, 255));
        jButtonPayments.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/menuBtn.png"))); // NOI18N
        jButtonPayments.setText("PAYMENTS");
        jButtonPayments.setFocusPainted(false);
        jButtonPayments.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonPayments.setIconTextGap(5);
        jButtonPayments.setMargin(new java.awt.Insets(2, 0, 2, 5));
        jButtonPayments.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPaymentsActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonPayments, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 190, 40));

        jButtonFeedback.setBackground(new java.awt.Color(0, 102, 102));
        jButtonFeedback.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        jButtonFeedback.setForeground(new java.awt.Color(255, 255, 255));
        jButtonFeedback.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/menuBtn.png"))); // NOI18N
        jButtonFeedback.setText("FEEDBACK");
        jButtonFeedback.setFocusPainted(false);
        jButtonFeedback.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonFeedback.setIconTextGap(5);
        jButtonFeedback.setMargin(new java.awt.Insets(2, 0, 2, 5));
        jButtonFeedback.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonFeedback11MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonFeedback11MouseExited(evt);
            }
        });
        jButtonFeedback.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFeedbackActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonFeedback, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 190, 40));

        jButtonLogout.setBackground(new java.awt.Color(0, 102, 102));
        jButtonLogout.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        jButtonLogout.setForeground(new java.awt.Color(255, 255, 255));
        jButtonLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/menuBtn.png"))); // NOI18N
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
        jButtonSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/menuBtn.png"))); // NOI18N
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

        jPanelReserveBook.setBackground(new java.awt.Color(255, 255, 255));
        jPanelReserveBook.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPaneReserveBook.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPaneReserveBook.setFocusable(false);
        jTabbedPaneReserveBook.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jPanelSearchBookTab.setBackground(new java.awt.Color(255, 255, 255));
        jPanelSearchBookTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextFieldSearchBook.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldSearchBook.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Search"));
        jTextFieldSearchBook.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldSearchBookKeyReleased(evt);
            }
        });
        jPanelSearchBookTab.add(jTextFieldSearchBook, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 210, 40));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPaneSearchBook.setViewportView(jTable1);

        jPanelSearchBookTab.add(jScrollPaneSearchBook, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 750, 390));

        jLabelSearchBookInfo.setText("Select book from the table to reservation.");
        jPanelSearchBookTab.add(jLabelSearchBookInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        jTabbedPaneReserveBook.addTab("Search Book", jPanelSearchBookTab);

        jPanelReserveBookTab.setBackground(new java.awt.Color(255, 255, 255));
        jPanelReserveBookTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextFieldBookId.setEditable(false);
        jTextFieldBookId.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldBookId.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Book ID"));
        jPanelReserveBookTab.add(jTextFieldBookId, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 60, 200, 40));

        jTextFieldAvailability.setEditable(false);
        jTextFieldAvailability.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldAvailability.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Availability"));
        jPanelReserveBookTab.add(jTextFieldAvailability, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 270, 200, 40));

        jTextFieldReserveISBN.setEditable(false);
        jTextFieldReserveISBN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldReserveISBN.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Book ISBN"));
        jPanelReserveBookTab.add(jTextFieldReserveISBN, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, 200, 40));

        jScrollPaneDescription.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Description"));

        jTextAreaDescription.setEditable(false);
        jTextAreaDescription.setColumns(20);
        jTextAreaDescription.setRows(4);
        jScrollPaneDescription.setViewportView(jTextAreaDescription);

        jPanelReserveBookTab.add(jScrollPaneDescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 340, 520, 110));

        jTextFieldAuthor.setEditable(false);
        jTextFieldAuthor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldAuthor.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Author Name"));
        jPanelReserveBookTab.add(jTextFieldAuthor, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 130, 200, 40));

        jTextFieldReserveBookName.setEditable(false);
        jTextFieldReserveBookName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldReserveBookName.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Book Name"));
        jPanelReserveBookTab.add(jTextFieldReserveBookName, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 200, 40));

        jButtonConfirmReservation.setBackground(new java.awt.Color(0, 102, 102));
        jButtonConfirmReservation.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonConfirmReservation.setForeground(new java.awt.Color(255, 255, 255));
        jButtonConfirmReservation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/reservebtn.png"))); // NOI18N
        jButtonConfirmReservation.setText("Confirm");
        jButtonConfirmReservation.setFocusable(false);
        jButtonConfirmReservation.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonConfirmReservation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConfirmReservationActionPerformed(evt);
            }
        });
        jPanelReserveBookTab.add(jButtonConfirmReservation, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 480, 120, 30));

        jTextFieldRequestedBookId.setEditable(false);
        jTextFieldRequestedBookId.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldRequestedBookId.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Request Book ID"));
        jPanelReserveBookTab.add(jTextFieldRequestedBookId, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 200, 40));

        jTextFieldRsvBookUID.setEditable(false);
        jTextFieldRsvBookUID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldRsvBookUID.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "UID"));
        jPanelReserveBookTab.add(jTextFieldRsvBookUID, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 200, 200, 40));

        jTabbedPaneReserveBook.addTab("Reserve Book", jPanelReserveBookTab);

        jPanelReserveBook.add(jTabbedPaneReserveBook, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelToggle.add(jPanelReserveBook, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelRequestedBookList.setBackground(new java.awt.Color(255, 255, 255));
        jPanelRequestedBookList.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPaneRequestedList.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPaneRequestedList.setFocusable(false);
        jTabbedPaneRequestedList.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jPanelRequestedList.setBackground(new java.awt.Color(255, 255, 255));
        jPanelRequestedList.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTableRequestedList.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableRequestedList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableRequestedListMouseClicked(evt);
            }
        });
        jScrollPaneRequestedBooks.setViewportView(jTableRequestedList);

        jPanelRequestedList.add(jScrollPaneRequestedBooks, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 750, 350));

        jButtonCancelRequest.setBackground(new java.awt.Color(0, 102, 102));
        jButtonCancelRequest.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonCancelRequest.setForeground(new java.awt.Color(255, 255, 255));
        jButtonCancelRequest.setIcon(new javax.swing.ImageIcon("C:\\SLIIT\\LibraryManagementSystem-master\\src\\View\\Images\\reservebtn.png")); // NOI18N
        jButtonCancelRequest.setText("Cancel Request");
        jButtonCancelRequest.setFocusable(false);
        jButtonCancelRequest.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonCancelRequest.setMargin(new java.awt.Insets(2, 0, 2, 5));
        jButtonCancelRequest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelRequestActionPerformed(evt);
            }
        });
        jPanelRequestedList.add(jButtonCancelRequest, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 480, 140, 30));

        jTextFieldSearchRequestedBook.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldSearchRequestedBook.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Search"));
        jTextFieldSearchRequestedBook.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldSearchRequestedBookKeyReleased(evt);
            }
        });
        jPanelRequestedList.add(jTextFieldSearchRequestedBook, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 210, 40));

        jTabbedPaneRequestedList.addTab("Requested Book List", jPanelRequestedList);

        jPanelRequestedBookList.add(jTabbedPaneRequestedList, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelToggle.add(jPanelRequestedBookList, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelPendingReturn.setBackground(new java.awt.Color(255, 255, 255));
        jPanelPendingReturn.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPanePendingReturn.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPanePendingReturn.setFocusable(false);
        jTabbedPanePendingReturn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jPanelPendingBooks.setBackground(new java.awt.Color(255, 255, 255));
        jPanelPendingBooks.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTablePendingReturnBooks.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPanePendingBooks.setViewportView(jTablePendingReturnBooks);

        jPanelPendingBooks.add(jScrollPanePendingBooks, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 750, 410));

        jTextFieldSearchPendingReturnBooks.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldSearchPendingReturnBooks.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Search"));
        jTextFieldSearchPendingReturnBooks.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldSearchPendingReturnBooksKeyReleased(evt);
            }
        });
        jPanelPendingBooks.add(jTextFieldSearchPendingReturnBooks, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 210, 40));

        jTabbedPanePendingReturn.addTab("Pending Return Book List", jPanelPendingBooks);

        jPanelPendingReturn.add(jTabbedPanePendingReturn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelToggle.add(jPanelPendingReturn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelHsitory.setBackground(new java.awt.Color(255, 255, 255));
        jPanelHsitory.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPanelHistory.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPanelHistory.setFocusable(false);
        jTabbedPanelHistory.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jPanelHistory.setBackground(new java.awt.Color(255, 255, 255));
        jPanelHistory.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTableHistoryBooks.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableHistoryBooks.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableHistoryBooksMouseClicked(evt);
            }
        });
        jScrollPaneHistory.setViewportView(jTableHistoryBooks);

        jPanelHistory.add(jScrollPaneHistory, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 750, 350));

        jButtonSubmitFeedback.setBackground(new java.awt.Color(0, 102, 102));
        jButtonSubmitFeedback.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonSubmitFeedback.setForeground(new java.awt.Color(255, 255, 255));
        jButtonSubmitFeedback.setIcon(new javax.swing.ImageIcon("C:\\SLIIT\\LibraryManagementSystem-master\\src\\View\\Images\\reservebtn.png")); // NOI18N
        jButtonSubmitFeedback.setText("Submit Feedback");
        jButtonSubmitFeedback.setFocusable(false);
        jButtonSubmitFeedback.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonSubmitFeedback.setMargin(new java.awt.Insets(2, 0, 2, 5));
        jButtonSubmitFeedback.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSubmitFeedbackActionPerformed(evt);
            }
        });
        jPanelHistory.add(jButtonSubmitFeedback, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 490, 140, 30));

        jLabelHistoryInfo.setText("Select Book and Press 'Submit Feedback Button' to provide feedback.");
        jPanelHistory.add(jLabelHistoryInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, -1, -1));

        jTextFieldSearchHistoryBooks.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldSearchHistoryBooks.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Search"));
        jTextFieldSearchHistoryBooks.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldSearchHistoryBooksKeyReleased(evt);
            }
        });
        jPanelHistory.add(jTextFieldSearchHistoryBooks, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 210, 40));

        jTabbedPanelHistory.addTab("Borrowed Book History", jPanelHistory);

        jPanelHsitory.add(jTabbedPanelHistory, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelToggle.add(jPanelHsitory, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelFeedback.setBackground(new java.awt.Color(255, 255, 255));
        jPanelFeedback.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPanelFeedback.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPanelFeedback.setFocusable(false);
        jTabbedPanelFeedback.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jPanelFeedbacks.setBackground(new java.awt.Color(255, 255, 255));
        jPanelFeedbacks.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTableFeedbacks.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableFeedbacks.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableFeedbacksMouseClicked(evt);
            }
        });
        jScrollPaneFeedbacks.setViewportView(jTableFeedbacks);

        jPanelFeedbacks.add(jScrollPaneFeedbacks, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 750, 250));

        jScrollPaneFeedbackDes.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Feedback Description"));

        jTextAreaFeedbackInfo.setEditable(false);
        jTextAreaFeedbackInfo.setColumns(20);
        jTextAreaFeedbackInfo.setLineWrap(true);
        jTextAreaFeedbackInfo.setRows(4);
        jScrollPaneFeedbackDes.setViewportView(jTextAreaFeedbackInfo);

        jPanelFeedbacks.add(jScrollPaneFeedbackDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 750, 140));

        jTextFieldSearchFeedback.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldSearchFeedback.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Search"));
        jTextFieldSearchFeedback.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldSearchFeedbackKeyReleased(evt);
            }
        });
        jPanelFeedbacks.add(jTextFieldSearchFeedback, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 210, 40));

        jTabbedPanelFeedback.addTab("Feedbacks of Books", jPanelFeedbacks);

        jPanelSubmitFeedback.setBackground(new java.awt.Color(255, 255, 255));
        jPanelSubmitFeedback.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextFieldBookIdFeedback.setEditable(false);
        jTextFieldBookIdFeedback.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldBookIdFeedback.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Book ID"));
        jPanelSubmitFeedback.add(jTextFieldBookIdFeedback, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 60, 200, 40));

        jTextFieldFeedbackFullName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldFeedbackFullName.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Full Name"));
        jPanelSubmitFeedback.add(jTextFieldFeedbackFullName, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 130, 200, 40));

        jScrollPaneDescriptionFeedback.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Description"));

        jTextAreaDescriptionFeedback.setColumns(20);
        jTextAreaDescriptionFeedback.setRows(6);
        jScrollPaneDescriptionFeedback.setViewportView(jTextAreaDescriptionFeedback);

        jPanelSubmitFeedback.add(jScrollPaneDescriptionFeedback, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 210, 520, 200));

        jTextFieldFID.setEditable(false);
        jTextFieldFID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldFID.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Feedback ID"));
        jPanelSubmitFeedback.add(jTextFieldFID, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 200, 40));

        jTextFieldFeedbackBookName.setEditable(false);
        jTextFieldFeedbackBookName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldFeedbackBookName.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Book Name"));
        jPanelSubmitFeedback.add(jTextFieldFeedbackBookName, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 200, 40));

        jButtonFeedbackSumbit.setBackground(new java.awt.Color(0, 102, 102));
        jButtonFeedbackSumbit.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonFeedbackSumbit.setForeground(new java.awt.Color(255, 255, 255));
        jButtonFeedbackSumbit.setIcon(new javax.swing.ImageIcon("C:\\SLIIT\\LibraryManagementSystem-master\\src\\View\\Images\\reservebtn.png")); // NOI18N
        jButtonFeedbackSumbit.setText("Submit");
        jButtonFeedbackSumbit.setFocusable(false);
        jButtonFeedbackSumbit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonFeedbackSumbit.setMargin(new java.awt.Insets(2, 0, 2, 5));
        jButtonFeedbackSumbit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFeedbackSumbitActionPerformed(evt);
            }
        });
        jPanelSubmitFeedback.add(jButtonFeedbackSumbit, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 450, 100, 30));

        jTabbedPanelFeedback.addTab("Submit Feedback", jPanelSubmitFeedback);

        jPanelSubmitSuggestions.setBackground(new java.awt.Color(255, 255, 255));
        jPanelSubmitSuggestions.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextFieldSuggestionUid.setEditable(false);
        jTextFieldSuggestionUid.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldSuggestionUid.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "User ID"));
        jPanelSubmitSuggestions.add(jTextFieldSuggestionUid, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 60, 240, 40));

        jScrollPaneSuggestionDes.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Description"));

        jTextAreaSuggestionDes.setColumns(20);
        jTextAreaSuggestionDes.setRows(6);
        jScrollPaneSuggestionDes.setViewportView(jTextAreaSuggestionDes);

        jPanelSubmitSuggestions.add(jScrollPaneSuggestionDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 150, 520, 260));

        jTextFieldSuggestionId.setEditable(false);
        jTextFieldSuggestionId.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldSuggestionId.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Suggestion ID"));
        jPanelSubmitSuggestions.add(jTextFieldSuggestionId, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 230, 40));

        jButtonSubmitSuggestion.setBackground(new java.awt.Color(0, 102, 102));
        jButtonSubmitSuggestion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonSubmitSuggestion.setForeground(new java.awt.Color(255, 255, 255));
        jButtonSubmitSuggestion.setIcon(new javax.swing.ImageIcon("C:\\SLIIT\\LibraryManagementSystem-master\\src\\View\\Images\\reservebtn.png")); // NOI18N
        jButtonSubmitSuggestion.setText("Submit");
        jButtonSubmitSuggestion.setFocusable(false);
        jButtonSubmitSuggestion.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonSubmitSuggestion.setMargin(new java.awt.Insets(2, 0, 2, 5));
        jButtonSubmitSuggestion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSubmitSuggestionActionPerformed(evt);
            }
        });
        jPanelSubmitSuggestions.add(jButtonSubmitSuggestion, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 450, 100, 30));

        jTabbedPanelFeedback.addTab("Submit Suggestions", jPanelSubmitSuggestions);

        jPanelFeedback.add(jTabbedPanelFeedback, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelToggle.add(jPanelFeedback, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelPayments.setBackground(new java.awt.Color(255, 255, 255));
        jPanelPayments.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPanelPayments.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPanelPayments.setFocusable(false);
        jTabbedPanelPayments.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jPanelPayment.setBackground(new java.awt.Color(255, 255, 255));
        jPanelPayment.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTablePaymentHistory.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPanePaymentHistory.setViewportView(jTablePaymentHistory);

        jPanelPayment.add(jScrollPanePaymentHistory, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 750, 410));

        jTextFieldSearchPayment.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldSearchPayment.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Search"));
        jTextFieldSearchPayment.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldSearchPaymentKeyReleased(evt);
            }
        });
        jPanelPayment.add(jTextFieldSearchPayment, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 210, 40));

        jTabbedPanelPayments.addTab("PAYMENT HISTORY", jPanelPayment);

        jPanelPayments.add(jTabbedPanelPayments, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelToggle.add(jPanelPayments, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

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
        jButtonSave.setIcon(new javax.swing.ImageIcon("C:\\SLIIT\\LibraryManagementSystem-master\\src\\View\\Images\\reservebtn.png")); // NOI18N
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

        jLabelPicture.setIcon(new javax.swing.ImageIcon("C:\\SLIIT\\LibraryManagementSystem-master\\src\\View\\Images\\ProfilePic.png")); // NOI18N
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
        jButtonChangePassword.setIcon(new javax.swing.ImageIcon("C:\\SLIIT\\LibraryManagementSystem-master\\src\\View\\Images\\reservebtn.png")); // NOI18N
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

    private void jButtonReserveBookMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonReserveBookMouseEntered
        // TODO add your handling code here:
        jButtonReserveBook.setBackground(new Color(24, 142, 130));
    }//GEN-LAST:event_jButtonReserveBookMouseEntered

    private void jButtonReserveBookMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonReserveBookMouseExited
        // TODO add your handling code here:
        jButtonReserveBook.setBackground(new Color(0, 102, 102));
    }//GEN-LAST:event_jButtonReserveBookMouseExited

    private void jButtonRequestedBookListMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonRequestedBookListMouseEntered
        // TODO add your handling code here:
        jButtonRequestedBookList.setBackground(new Color(24, 142, 130));
    }//GEN-LAST:event_jButtonRequestedBookListMouseEntered

    private void jButtonRequestedBookListMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonRequestedBookListMouseExited
        // TODO add your handling code here:
        jButtonRequestedBookList.setBackground(new Color(0, 102, 102));
    }//GEN-LAST:event_jButtonRequestedBookListMouseExited

    private void jButtonPendingReturnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonPendingReturnMouseEntered
        // TODO add your handling code here:
        jButtonPendingReturn.setBackground(new Color(24, 142, 130));
    }//GEN-LAST:event_jButtonPendingReturnMouseEntered

    private void jButtonPendingReturnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonPendingReturnMouseExited
        // TODO add your handling code here:
        jButtonPendingReturn.setBackground(new Color(0, 102, 102));
    }//GEN-LAST:event_jButtonPendingReturnMouseExited

    private void jButtonHistoryMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonHistoryMouseEntered
        // TODO add your handling code here:
        jButtonHistory.setBackground(new Color(24, 142, 130));
    }//GEN-LAST:event_jButtonHistoryMouseEntered

    private void jButtonHistoryMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonHistoryMouseExited
        // TODO add your handling code here:
        jButtonHistory.setBackground(new Color(0, 102, 102));
    }//GEN-LAST:event_jButtonHistoryMouseExited

    private void jButtonFeedback11MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonFeedback11MouseEntered
        // TODO add your handling code here:
        jButtonFeedback.setBackground(new Color(24, 142, 130));
    }//GEN-LAST:event_jButtonFeedback11MouseEntered

    private void jButtonFeedback11MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonFeedback11MouseExited
        // TODO add your handling code here:
        jButtonFeedback.setBackground(new Color(0, 102, 102));
    }//GEN-LAST:event_jButtonFeedback11MouseExited

    private void jButtonSettings11MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSettings11MouseEntered
        // TODO add your handling code here:
        jButtonSettings.setBackground(new Color(24, 142, 130));
    }//GEN-LAST:event_jButtonSettings11MouseEntered

    private void jButtonSettings11MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSettings11MouseExited
        // TODO add your handling code here:
        jButtonSettings.setBackground(new Color(0, 102, 102));
    }//GEN-LAST:event_jButtonSettings11MouseExited

    private void jButtonReserveBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReserveBookActionPerformed

        togglePanels("ReserveBook");
        try {
            loadReserveBookData();
        } catch (IOException ex) {
            LOG.error("Failed to load Reserve book data to the table\n" + ex);
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTabbedPaneReserveBook.setEnabledAt(1, false);
        jTabbedPaneReserveBook.setSelectedIndex(0);

    }//GEN-LAST:event_jButtonReserveBookActionPerformed

    private void jButtonRequestedBookListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRequestedBookListActionPerformed

        try {
            loadRequestedBookData();
        } catch (IOException ex) {
            LOG.error("Failed to load Reuested book data to the table\n" + ex);
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        togglePanels("RequestedList");
        rID = null;
        bID = null;
        bookName = null;
    }//GEN-LAST:event_jButtonRequestedBookListActionPerformed

    private void jButtonPendingReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPendingReturnActionPerformed
        try {
            loadPendingBookData();
        } catch (IOException ex) {
            LOG.error("Failed to load pending book data to the table\n" + ex);
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        togglePanels("PendingBooks");
    }//GEN-LAST:event_jButtonPendingReturnActionPerformed

    private void jButtonHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHistoryActionPerformed
        try {
            // TODO add your handling code here:
            loadHistoryBookData();
        } catch (IOException ex) {
            LOG.error("Failed to load history data to the table\n" + ex);
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        togglePanels("History");
    }//GEN-LAST:event_jButtonHistoryActionPerformed

    private void jButtonFeedbackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFeedbackActionPerformed
        // TODO add your handling code here:
        try {
            loadFeedback();
            togglePanels("Feedbacks");
            jTabbedPanelFeedback.setEnabledAt(1, false);
            jTabbedPanelFeedback.setEnabledAt(0, true);
            jTabbedPanelFeedback.setSelectedIndex(0);
            jTextFieldSuggestionId.setText(SuggestionController.autoGenerateSuggestionID());
            jTextFieldSuggestionUid.setText(UID);
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            LOG.error("Failed to load Feedback data to the table\n" + ex);
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonFeedbackActionPerformed

    private void jButtonSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSettingsActionPerformed
        // TODO add your handling code here:
        togglePanels("Settings");
        try {
            UserModel user = UserController.getUser(UID);
            jLabelUserEmail.setText(user.getEmail());
            jTextFieldFullName.setText(user.getFirstName() + " " + user.getLastName());
            jTextFieldNIC.setText(user.getNic());
            jTextFieldDOB.setText(user.getDob());
            jTextFieldExpiryDate.setText(user.getExpiryDate());
            jTextFieldRegDate.setText(user.getRegDate());
            jTextFieldMembership.setText(user.getUserType());
            jTextFieldContact.setText(user.getContact());

        } catch (ClassNotFoundException | SQLException | IOException ex) {
            LOG.error("Faild to get user details from user controller\n" + ex);
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonSettingsActionPerformed

    private void jButtonLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogoutActionPerformed

        int result = JOptionPane.showConfirmDialog(rootPane, "Are you sure want "
                + "to Logout?", "Confirm Logout", 0);

        if (result == 0) {
            this.dispose();
            SystemLogin login = new SystemLogin();
            login.setVisible(true);
            LOG.info("------User " + UID + " logged out from the system------");
        }
    }//GEN-LAST:event_jButtonLogoutActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        try {
            int row = jTable1.getSelectedRow();

            jTextFieldBookId.setText(jTable1.getValueAt(row, 0).toString());
            jTextFieldReserveBookName.setText(jTable1.getValueAt(row, 1).toString());
            jTextFieldAuthor.setText(jTable1.getValueAt(row, 2).toString());
            jTextFieldAvailability.setText(jTable1.getValueAt(row, 5).toString());
            jTextFieldReserveISBN.setText(jTable1.getValueAt(row, 3).toString());
            jTextAreaDescription.setText(jTable1.getValueAt(row, 4).toString());
            jTextFieldRsvBookUID.setText(UID);
            if (jTable1.getValueAt(row, 5).toString().equals("NO")
                    || jTable1.getValueAt(row, 5).toString().equals("No")) {
                JOptionPane.showMessageDialog(rootPane, "Selected Book Is Not Available!",
                        "Warning!", JOptionPane.ERROR_MESSAGE);
            } else {
                jTabbedPaneReserveBook.setSelectedIndex(1);
                jTabbedPaneReserveBook.setEnabledAt(1, true);
                jTextFieldRequestedBookId.setText(RequestedBookController.autoGenerateRequestedBookID());
                LOG.info("Data retrieved from the table row");
            }

        } catch (ClassNotFoundException | SQLException | IOException ex) {
            LOG.error("Failed to select data row from the table\n" + ex);
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButtonConfirmReservationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConfirmReservationActionPerformed

        try {
            if (RequestedBookController.addBookRequest(new RequestedBookListModel(jTextFieldRequestedBookId.getText(), UID, jTextFieldBookId.getText(),
                    jTextFieldReserveBookName.getText()))) {
                JOptionPane.showMessageDialog(rootPane, "Request Successfully Added!",
                        "Success!", JOptionPane.INFORMATION_MESSAGE);
                jTabbedPaneReserveBook.setSelectedIndex(0);
                jTabbedPaneReserveBook.setEnabledAt(1, false);
                LOG.info("Book request " + jTextFieldRequestedBookId.getText() + " added to the database");
            } else {
                JOptionPane.showMessageDialog(rootPane, "Error occured while Requesting!",
                        "Error!", JOptionPane.ERROR_MESSAGE);
                LOG.info("Failed to place book request to the database");
            }

        } catch (ClassNotFoundException | SQLException | IOException ex) {
            LOG.error("Failed to place book request to the database\n" + ex);
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButtonConfirmReservationActionPerformed

    private void jButtonCancelRequestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelRequestActionPerformed

        try {
            if (rID == null || bID == null) {
                JOptionPane.showMessageDialog(rootPane, "Please select record for"
                        + " cancel!", "Error!", JOptionPane.ERROR_MESSAGE);
            } else {
                int result = JOptionPane.showConfirmDialog(rootPane, "Are you sure"
                        + " want to cancel this request?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (result == 0) {
                    if (RequestedBookController.cancelBookRequest(new RequestedBookListModel(rID, UID, bID, bookName))) {
                        JOptionPane.showMessageDialog(rootPane, "Book Request "
                                + "Cancelled Successfully!", "Success!", JOptionPane.INFORMATION_MESSAGE);
                        loadRequestedBookData();
                        LOG.info("Book request " + rID + " canceled/deleted by user " + UID);
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Error occured"
                                + " while Cancelling!", "Error!", JOptionPane.ERROR_MESSAGE);
                        LOG.error("Failed to Cancel the request from the database--> User" + UID);
                    }
                }
            }

        } catch (ClassNotFoundException | SQLException | IOException ex) {
            LOG.error("Failed to Cancel the request\n" + ex);
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonCancelRequestActionPerformed

    private void jTableRequestedListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableRequestedListMouseClicked

        int row = jTableRequestedList.getSelectedRow();
        rID = jTableRequestedList.getValueAt(row, 0).toString();
        bID = jTableRequestedList.getValueAt(row, 1).toString();
        bookName = jTableRequestedList.getValueAt(row, 2).toString();
    }//GEN-LAST:event_jTableRequestedListMouseClicked

    private void jButtonPaymentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPaymentsActionPerformed
        try {
            loadPaymentHistory();
        } catch (IOException ex) {
            LOG.error("Failed to load History data to the table\n" + ex);
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        togglePanels("Payments");
    }//GEN-LAST:event_jButtonPaymentsActionPerformed

    private void jTableFeedbacksMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableFeedbacksMouseClicked

        int row = jTableFeedbacks.getSelectedRow();
        jTextAreaFeedbackInfo.setText(jTableFeedbacks.getValueAt(row, 3).toString());
    }//GEN-LAST:event_jTableFeedbacksMouseClicked

    private void jButtonSubmitFeedbackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSubmitFeedbackActionPerformed

        try {
            int row = jTableHistoryBooks.getSelectedRow();
            jTableHistoryBooks.getValueAt(row, 0).toString().isEmpty();
            togglePanels("Feedbacks");
            jTabbedPanelFeedback.setEnabledAt(0, false);
            jTabbedPanelFeedback.setEnabledAt(1, true);
            jTabbedPanelFeedback.setSelectedIndex(1);

            jTextFieldSuggestionId.setText(SuggestionController.autoGenerateSuggestionID());
            jTextFieldSuggestionUid.setText(UID);
        } catch (ArrayIndexOutOfBoundsException ex) {
            LOG.error("Failed to get data from the table to the next step\n" + ex);
            JOptionPane.showMessageDialog(rootPane, "Please select book for provide feedback!",
                    "Warning!", 1);
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            LOG.error("Failed to get data from the table\n" + ex);
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jButtonSubmitFeedbackActionPerformed

    private void jTableHistoryBooksMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableHistoryBooksMouseClicked

        try {
            int row = jTableHistoryBooks.getSelectedRow();
            jTextFieldBookIdFeedback.setText(jTableHistoryBooks.getValueAt(row, 0).toString());
            jTextFieldFeedbackBookName.setText(jTableHistoryBooks.getValueAt(row, 1).toString());

            jTextFieldFID.setText(FeedbackController.autoGenerateFeedbackID());
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            LOG.error("Failed to get data from the history table\n" + ex);
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jTableHistoryBooksMouseClicked

    private void jButtonFeedbackSumbitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFeedbackSumbitActionPerformed

        try {
            if (!jTextFieldFeedbackFullName.getText().isEmpty()
                    && !jTextAreaDescriptionFeedback.getText().isEmpty()) {
                if (FeedbackController.addFeedback(new BookFeedbackModel(jTextFieldFID.getText(), jTextFieldBookIdFeedback.getText(),
                        jTextAreaDescriptionFeedback.getText(), jTextFieldFeedbackFullName.getText()))) {
                    JOptionPane.showMessageDialog(rootPane, "Feedback Successfully"
                            + " Submitted for Approval!", "Success!", JOptionPane.INFORMATION_MESSAGE);
                    loadHistoryBookData();
                    togglePanels("History");
                    jTextFieldFeedbackFullName.setText("");
                    jTextAreaDescriptionFeedback.setText("");
                    LOG.info("Feedback successfully added to the database by user: " + UID);
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Error occured while "
                            + "Sumbitting", "Error!", JOptionPane.ERROR_MESSAGE);
                    LOG.info("Error occured while submitting feedback to the database");
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "Please fill all the fields!",
                        "Error!", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            LOG.error("Failed to add feedback to the database\n" + ex);
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonFeedbackSumbitActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed

        try {
            String contact = jTextFieldContact.getText();
            if (contact.isEmpty() || contact.length() != 10 || !Validation.isNumbers(contact)) {
                JOptionPane.showMessageDialog(rootPane, "Please enter valid contact "
                        + "number", "Error!", JOptionPane.ERROR_MESSAGE);
            } else if (updateUser(UID, contact)) {
                JOptionPane.showMessageDialog(rootPane, "Details Updated Successfully!",
                        "Success!", JOptionPane.INFORMATION_MESSAGE);
                LOG.info("User details successfully updated to the database by user: " + UID);
            } else {
                JOptionPane.showMessageDialog(rootPane, "Error occured while Updating",
                        "Error!", JOptionPane.ERROR_MESSAGE);
                LOG.info("Error occurred while updating to the database");
            }

        } catch (ClassNotFoundException | SQLException | IOException ex) {
            LOG.error("Failed to save user details\n" + ex);
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
                                    + "Successfully!", "Success!", JOptionPane.INFORMATION_MESSAGE);
                            jPasswordFieldCurrentPw.setText("");
                            jPasswordFieldNewPw.setText("");
                            jPasswordFieldConfirmPw.setText("");
                            Utility.sendEmail(USER, "passwordchange");
                            LOG.info("Password successfully updated to the database by user: " + UID);
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
            LOG.error("Failed to reset password by user " + UID + "\n" + ex);
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jButtonChangePasswordActionPerformed

    private void jTextFieldSearchBookKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSearchBookKeyReleased
        Util.Utility.filter(jTable1, jTextFieldSearchBook.getText());
    }//GEN-LAST:event_jTextFieldSearchBookKeyReleased

    private void jTextFieldSearchRequestedBookKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSearchRequestedBookKeyReleased
        Util.Utility.filter(jTableRequestedList, jTextFieldSearchRequestedBook.getText());
    }//GEN-LAST:event_jTextFieldSearchRequestedBookKeyReleased

    private void jTextFieldSearchPendingReturnBooksKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSearchPendingReturnBooksKeyReleased
        Util.Utility.filter(jTablePendingReturnBooks, jTextFieldSearchPendingReturnBooks.getText());
    }//GEN-LAST:event_jTextFieldSearchPendingReturnBooksKeyReleased

    private void jTextFieldSearchHistoryBooksKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSearchHistoryBooksKeyReleased
        Util.Utility.filter(jTableHistoryBooks, jTextFieldSearchHistoryBooks.getText());
    }//GEN-LAST:event_jTextFieldSearchHistoryBooksKeyReleased

    private void jTextFieldSearchFeedbackKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSearchFeedbackKeyReleased
        Util.Utility.filter(jTableFeedbacks, jTextFieldSearchFeedback.getText());
    }//GEN-LAST:event_jTextFieldSearchFeedbackKeyReleased

    private void jTextFieldSearchPaymentKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSearchPaymentKeyReleased
        Util.Utility.filter(jTablePaymentHistory, jTextFieldSearchPayment.getText());
    }//GEN-LAST:event_jTextFieldSearchPaymentKeyReleased

    private void jButtonSubmitSuggestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSubmitSuggestionActionPerformed
        try {
            if (!jTextAreaSuggestionDes.getText().isEmpty()) {
                boolean result = SuggestionController.addSuggestion(new SuggestionsModel(jTextFieldSuggestionId.getText(), jTextFieldSuggestionUid.getText(),
                        jTextAreaSuggestionDes.getText()));
                if (result) {
                    JOptionPane.showMessageDialog(rootPane, "Suggesstion Submitted"
                            + " to the Manager!", "Success!", JOptionPane.INFORMATION_MESSAGE);
                    jTextAreaSuggestionDes.setText("");
                    jTextFieldSuggestionId.setText(SuggestionController.autoGenerateSuggestionID());
                    LOG.info("Suggestions successfully added to the database by user: " + UID);
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "Please enter your description!",
                        "Error!", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            LOG.error("Failed to submit suggestions to the database\n" + ex);
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonSubmitSuggestionActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MemberMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MemberMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MemberMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MemberMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MemberMain(USER, UID).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancelRequest;
    private javax.swing.JButton jButtonChangePassword;
    private javax.swing.JButton jButtonConfirmReservation;
    private javax.swing.JButton jButtonFeedback;
    private javax.swing.JButton jButtonFeedbackSumbit;
    private javax.swing.JButton jButtonHistory;
    private javax.swing.JButton jButtonLogout;
    private javax.swing.JButton jButtonPayments;
    private javax.swing.JButton jButtonPendingReturn;
    private javax.swing.JButton jButtonRequestedBookList;
    private javax.swing.JButton jButtonReserveBook;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonSettings;
    private javax.swing.JButton jButtonSubmitFeedback;
    private javax.swing.JButton jButtonSubmitSuggestion;
    private javax.swing.JDesktopPane jDesktopPaneLibraryMain;
    private javax.swing.JLabel jLabelHistoryInfo;
    private javax.swing.JLabel jLabelLogo;
    private javax.swing.JLabel jLabelPicture;
    private javax.swing.JLabel jLabelSearchBookInfo;
    private javax.swing.JLabel jLabelUserEmail;
    private javax.swing.JLabel jLabelWelcomeUser;
    private javax.swing.JLabel jLableTime;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelEditProfile;
    private javax.swing.JPanel jPanelFeedback;
    private javax.swing.JPanel jPanelFeedbacks;
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JPanel jPanelHistory;
    private javax.swing.JPanel jPanelHsitory;
    private javax.swing.JPanel jPanelPayment;
    private javax.swing.JPanel jPanelPayments;
    private javax.swing.JPanel jPanelPendingBooks;
    private javax.swing.JPanel jPanelPendingReturn;
    private javax.swing.JPanel jPanelProfilepic;
    private javax.swing.JPanel jPanelRequestedBookList;
    private javax.swing.JPanel jPanelRequestedList;
    private javax.swing.JPanel jPanelReserveBook;
    private javax.swing.JPanel jPanelReserveBookTab;
    private javax.swing.JPanel jPanelSearchBookTab;
    private javax.swing.JPanel jPanelSettings;
    private javax.swing.JPanel jPanelSubmitFeedback;
    private javax.swing.JPanel jPanelSubmitSuggestions;
    private javax.swing.JPanel jPanelToggle;
    private javax.swing.JPasswordField jPasswordFieldConfirmPw;
    private javax.swing.JPasswordField jPasswordFieldCurrentPw;
    private javax.swing.JPasswordField jPasswordFieldNewPw;
    private javax.swing.JScrollPane jScrollPaneDescription;
    private javax.swing.JScrollPane jScrollPaneDescriptionFeedback;
    private javax.swing.JScrollPane jScrollPaneFeedbackDes;
    private javax.swing.JScrollPane jScrollPaneFeedbacks;
    private javax.swing.JScrollPane jScrollPaneHistory;
    private javax.swing.JScrollPane jScrollPanePaymentHistory;
    private javax.swing.JScrollPane jScrollPanePendingBooks;
    private javax.swing.JScrollPane jScrollPaneRequestedBooks;
    private javax.swing.JScrollPane jScrollPaneSearchBook;
    private javax.swing.JScrollPane jScrollPaneSuggestionDes;
    private javax.swing.JTabbedPane jTabbedPanePendingReturn;
    private javax.swing.JTabbedPane jTabbedPaneRequestedList;
    private javax.swing.JTabbedPane jTabbedPaneReserveBook;
    private javax.swing.JTabbedPane jTabbedPanelFeedback;
    private javax.swing.JTabbedPane jTabbedPanelHistory;
    private javax.swing.JTabbedPane jTabbedPanelPayments;
    private javax.swing.JTabbedPane jTabbedPanelSettings;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTableFeedbacks;
    private javax.swing.JTable jTableHistoryBooks;
    private javax.swing.JTable jTablePaymentHistory;
    private javax.swing.JTable jTablePendingReturnBooks;
    private javax.swing.JTable jTableRequestedList;
    private javax.swing.JTextArea jTextAreaDescription;
    private javax.swing.JTextArea jTextAreaDescriptionFeedback;
    private javax.swing.JTextArea jTextAreaFeedbackInfo;
    private javax.swing.JTextArea jTextAreaSuggestionDes;
    private javax.swing.JTextField jTextFieldAuthor;
    private javax.swing.JTextField jTextFieldAvailability;
    private javax.swing.JTextField jTextFieldBookId;
    private javax.swing.JTextField jTextFieldBookIdFeedback;
    private javax.swing.JTextField jTextFieldContact;
    private javax.swing.JTextField jTextFieldDOB;
    private javax.swing.JTextField jTextFieldExpiryDate;
    private javax.swing.JTextField jTextFieldFID;
    private javax.swing.JTextField jTextFieldFeedbackBookName;
    private javax.swing.JTextField jTextFieldFeedbackFullName;
    private javax.swing.JTextField jTextFieldFullName;
    private javax.swing.JTextField jTextFieldMembership;
    private javax.swing.JTextField jTextFieldNIC;
    private javax.swing.JTextField jTextFieldRegDate;
    private javax.swing.JTextField jTextFieldRequestedBookId;
    private javax.swing.JTextField jTextFieldReserveBookName;
    private javax.swing.JTextField jTextFieldReserveISBN;
    private javax.swing.JTextField jTextFieldRsvBookUID;
    private javax.swing.JTextField jTextFieldSearchBook;
    private javax.swing.JTextField jTextFieldSearchFeedback;
    private javax.swing.JTextField jTextFieldSearchHistoryBooks;
    private javax.swing.JTextField jTextFieldSearchPayment;
    private javax.swing.JTextField jTextFieldSearchPendingReturnBooks;
    private javax.swing.JTextField jTextFieldSearchRequestedBook;
    private javax.swing.JTextField jTextFieldSuggestionId;
    private javax.swing.JTextField jTextFieldSuggestionUid;
    // End of variables declaration//GEN-END:variables

}
