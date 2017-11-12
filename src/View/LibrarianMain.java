/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.BookController;
import Controller.FineController;
import Controller.UserController;
import static Controller.UserController.updateUser;
import Controller.WarnedListController;
import Model.BookModel;
import Model.BorrowedBookModel;
import Model.FineModel;
import Model.UserModel;
import Model.WarnedListModel;
import Util.Config;
import Util.Utility;
import Validation.UserValidation;
import Validation.Validation;
import static View.MemberMain.USER;
import java.awt.Color;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;
import javax.swing.JOptionPane;

/**
 *
 * @author rpa06
 */
public class LibrarianMain extends javax.swing.JFrame {

    /**
     * Creates new form LibraryMain
     */
    public static final org.apache.log4j.Logger LOG = Config.getLogger(LibrarianMain.class);
    static String USER;
    static String UID;
    static boolean isReserved;

    public LibrarianMain(String userName, String uid) {
        initComponents();

        USER = userName;
        UID = uid;
        jLabelWelcomeUser.setText("Welcome " + USER);
        pnlIssueBook.setVisible(false);
        pnlManageBook.setVisible(false);
        pnlMembers.setVisible(false);
        pnlBorrowedHistory.setVisible(false);
        pnlBookFeedBacks.setVisible(false);
        jPanelSettings.setVisible(false);
        Utility.clock(jLableTime);

    }

    private void loadIssueBookTable() throws IOException {
        try {
            Utility.loadTable(tblBookData, "SELECT BID, BOOKNAME AS TITLE,AUTHOR,"
                    + "ISBN,DESCRIPTION,AVAILABILITY,VERSION FROM BOOKS WHERE AVAILABILITY = 'Yes' AND QUANTITY > 0");
        } catch (ClassNotFoundException | SQLException ex) {
            LOG.error("Failed to load book list data to the table\n" + ex);
            Logger.getLogger(LibrarianMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void loadReservedBookList() {

        try {
            Utility.loadTable(tblReservedBookList, "SELECT * FROM requested_book_list");

        } catch (ClassNotFoundException | SQLException | IOException ex) {
            LOG.error("Failed to load requested book list data to the table\n" + ex);
            Logger.getLogger(LibrarianMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadManageBookTable() {
        try {
            Utility.loadTable(tblManageBook, "SELECT BID,BOOKNAME AS TITLE,AUTHOR,"
                    + "ISBN,DESCRIPTION,QUANTITY,PRICE,VERSION FROM BOOKS WHERE AVAILABILITY = 'Yes'");

        } catch (ClassNotFoundException | SQLException | IOException ex) {
            LOG.error("Failed to load book list data to the table\n" + ex);
            Logger.getLogger(LibrarianMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void loadMembersTable() {

        try {
            Utility.loadTable(tblMemebers, "SELECT UID, CONCAT(FIRSTNAME,' ',LASTNAME)"
                    + " AS 'NAME', NIC, ADDRESS, CONTACT, EMAIL,MAXIMUMBOOKCOUNT,BORROWEDBOOKCOUNT"
                    + " FROM USERS WHERE USERTYPE = 'MEMBER'");

        } catch (ClassNotFoundException | SQLException | IOException ex) {
            LOG.error("Failed to load user list data to the table \n" + ex);
            Logger.getLogger(LibrarianMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void loadBorrowedBookData(String Query) {

        try {
            Utility.loadTable(tblBorrowedBookHistory, Query);

        } catch (ClassNotFoundException | SQLException | IOException ex) {
            LOG.error("Failed to load borrowed book list data to the table\n" + ex);
            Logger.getLogger(LibrarianMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void loadFeedbackTable() {

        try {
            Utility.loadTable(tblFeedbacks, "SELECT FID AS 'FEEDBACK ID', BID AS "
                    + "'BOOK ID', FEEDBACK, ISAPPROVED AS 'APPROVED STATUS', FULLNAME AS 'NAME' FROM BOOKFEEDBACKS");

        } catch (ClassNotFoundException | SQLException | IOException ex) {
            LOG.error("Failed to book feedback list data to the table\n" + ex);
            Logger.getLogger(LibrarianMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void clearReserveBookData() {

        tfIssueBookAuthor.setText("");
        tfIssueBookBID.setText("");
        tfIssueBookAvailability.setText("");
        tfIssueBookBookName.setText("");
        tfIssueBookUID.setText("");
        tfIssueBookISBN.setText("");
        tfIssueBookRenewDate.setDate(null);
        tfIssueBookDescription.setText("");
        try {
            tfIssueBBID.setText(BookController.autoGenerateBorrowedID());
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            LOG.error("Error occured while generating borrowed book id\n" + ex);
            Logger.getLogger(LibrarianMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void clearAddBookdata() {

        tfAddBookBookName.setText("");
        tfAddBookAuthorName.setText("");
        tfAddBookPrice.setText("");
        tfAddBookISBN.setText("");
        tfAddBookQty.setText("");
        tfAddBookVersion.setText("");
        tfAddBookDescription.setText("");
        try {
            tfAddBookBID.setText(BookController.autoGenerateBookID());
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            LOG.error("Error occured while generating book id\n" + ex);
            Logger.getLogger(LibrarianMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void clearUpdateBookData() {

        tfEditBookAuthor.setText("");
        tfEditBookName.setText("");
        tfEditBookPrice.setText("");
        tfEditBookISBN.setText("");
        tfEditBookQuantity.setText("");
        tfEditBookVersion.setText("");
        tfEditBookDescription.setText("");
        tfEditBookDescription.setText("");
        tfEditBookBID.setText("");

    }

    private void clearHistoryData() {
        tfHistoryBBID.setText("");
        tfHistoryAuthor.setText("");
        tfHistoryBID.setText("");
        tfHistoryBookName.setText("");
        tfHistoryDescription.setText("");
        tfHistoryISBN.setText("");
        tfHistoryRenewDate.setDate(null);
        tfHistoryUID.setText("");
    }

    //this method call for show and hide toggle panels when button click
    private void togglePanels(String buttonName) {
        switch (buttonName) {
            case "ReserveBook":
                pnlIssueBook.setVisible(true);
                pnlManageBook.setVisible(false);
                pnlMembers.setVisible(false);
                pnlBorrowedHistory.setVisible(false);
                pnlBookFeedBacks.setVisible(false);
                jPanelSettings.setVisible(false);
                break;
            case "RequestedList":
                pnlIssueBook.setVisible(false);
                pnlManageBook.setVisible(true);
                pnlMembers.setVisible(false);
                pnlBorrowedHistory.setVisible(false);
                pnlBookFeedBacks.setVisible(false);
                jPanelSettings.setVisible(false);
                break;
            case "PendingBooks":
                pnlIssueBook.setVisible(false);
                pnlManageBook.setVisible(false);
                pnlMembers.setVisible(true);
                pnlBorrowedHistory.setVisible(false);
                pnlBookFeedBacks.setVisible(false);
                jPanelSettings.setVisible(false);
                break;
            case "History":
                pnlIssueBook.setVisible(false);
                pnlManageBook.setVisible(false);
                pnlMembers.setVisible(false);
                pnlBorrowedHistory.setVisible(true);
                pnlBookFeedBacks.setVisible(false);
                jPanelSettings.setVisible(false);
                btnMarkRecieved.setEnabled(false);
                loadBorrowedBookData("SELECT BBID AS "
                        + "'Borrowed ID', UID , BID, "
                        + "ISSUEDATE, RENEWDATE FROM "
                        + "borrowedbooks GROUP BY BBID DESC");

                break;
            case "Feedbacks":
                pnlIssueBook.setVisible(false);
                pnlManageBook.setVisible(false);
                pnlMembers.setVisible(false);
                pnlBorrowedHistory.setVisible(false);
                pnlBookFeedBacks.setVisible(true);
                jPanelSettings.setVisible(false);
                break;
            case "Settings":
                pnlIssueBook.setVisible(false);
                pnlManageBook.setVisible(false);
                pnlMembers.setVisible(false);
                pnlBorrowedHistory.setVisible(false);
                pnlBookFeedBacks.setVisible(false);
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
        btnManageBooks = new javax.swing.JButton();
        btnMembersTab = new javax.swing.JButton();
        btnBorrowedHistory = new javax.swing.JButton();
        btnBookFeedBack = new javax.swing.JButton();
        jButtonLogout = new javax.swing.JButton();
        jButtonSettings = new javax.swing.JButton();
        jPanelToggle = new javax.swing.JPanel();
        pnlIssueBook = new javax.swing.JPanel();
        tbIssueBook = new javax.swing.JTabbedPane();
        jPanelSearchBookTab = new javax.swing.JPanel();
        tfRsvBookISBNSearch = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBookData = new javax.swing.JTable();
        jPanelReserveBookTab = new javax.swing.JPanel();
        tfIssueBookBID = new javax.swing.JTextField();
        tfIssueBookAvailability = new javax.swing.JTextField();
        tfIssueBookISBN = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tfIssueBookDescription = new javax.swing.JTextArea();
        tfIssueBookAuthor = new javax.swing.JTextField();
        tfIssueBookBookName = new javax.swing.JTextField();
        btnIssueBook = new javax.swing.JButton();
        tfIssueBookUID = new javax.swing.JTextField();
        tfIssueBookRenewDate = new com.toedter.calendar.JDateChooser();
        tfIssueBBID = new javax.swing.JTextField();
        tfIssueBookRequestedID = new javax.swing.JTextField();
        jPanelSearchBookTab2 = new javax.swing.JPanel();
        tfRsvBookSearch = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblReservedBookList = new javax.swing.JTable();
        pnlManageBook = new javax.swing.JPanel();
        tbpnlManageBook = new javax.swing.JTabbedPane();
        jPanelSearchBookTab1 = new javax.swing.JPanel();
        tfManageBookSearch = new javax.swing.JTextField();
        jScrollPane10 = new javax.swing.JScrollPane();
        tblManageBook = new javax.swing.JTable();
        btnDeleteBook = new javax.swing.JButton();
        btnEditBook = new javax.swing.JButton();
        jPanelAddBook = new javax.swing.JPanel();
        tfAddBookBID = new javax.swing.JTextField();
        tfAddBookVersion = new javax.swing.JTextField();
        jScrollPane9 = new javax.swing.JScrollPane();
        tfAddBookDescription = new javax.swing.JTextArea();
        tfAddBookAuthorName = new javax.swing.JTextField();
        tfAddBookQty = new javax.swing.JTextField();
        btnAddBook = new javax.swing.JButton();
        tfAddBookBookName = new javax.swing.JTextField();
        tfAddBookPrice = new javax.swing.JTextField();
        tfAddBookISBN = new javax.swing.JTextField();
        jPanelAddBook1 = new javax.swing.JPanel();
        tfEditBookBID = new javax.swing.JTextField();
        tfEditBookVersion = new javax.swing.JTextField();
        jScrollPane11 = new javax.swing.JScrollPane();
        tfEditBookDescription = new javax.swing.JTextArea();
        tfEditBookAuthor = new javax.swing.JTextField();
        tfEditBookQuantity = new javax.swing.JTextField();
        tfEditBookName = new javax.swing.JTextField();
        tfEditBookPrice = new javax.swing.JTextField();
        tfEditBookISBN = new javax.swing.JTextField();
        btnUpdateBook = new javax.swing.JButton();
        pnlMembers = new javax.swing.JPanel();
        jTabbedPanePendingReturn = new javax.swing.JTabbedPane();
        jPanelPending = new javax.swing.JPanel();
        tfMembersSearch = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblMemebers = new javax.swing.JTable();
        jButtonCancelRequest1 = new javax.swing.JButton();
        pnlBorrowedHistory = new javax.swing.JPanel();
        tbBorrowedHistory = new javax.swing.JTabbedPane();
        jPanelHistory = new javax.swing.JPanel();
        tfBorrowedSearch = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblBorrowedBookHistory = new javax.swing.JTable();
        btnHistoryEdit = new javax.swing.JButton();
        btnHistoryDelete = new javax.swing.JButton();
        btnMarkRecieved = new javax.swing.JButton();
        chkPendingList = new javax.swing.JCheckBox();
        jPanelReserveBookTab1 = new javax.swing.JPanel();
        tfHistoryBID = new javax.swing.JTextField();
        tfHistoryISBN = new javax.swing.JTextField();
        jScrollPane12 = new javax.swing.JScrollPane();
        tfHistoryDescription = new javax.swing.JTextArea();
        tfHistoryAuthor = new javax.swing.JTextField();
        tfHistoryBookName = new javax.swing.JTextField();
        tfHistoryUID = new javax.swing.JTextField();
        tfHistoryRenewDate = new com.toedter.calendar.JDateChooser();
        tfHistoryBBID = new javax.swing.JTextField();
        btnHistoryUpdate = new javax.swing.JButton();
        pnlBookFeedBacks = new javax.swing.JPanel();
        jTabbedPanelFeedback = new javax.swing.JTabbedPane();
        jPanelFeedback1 = new javax.swing.JPanel();
        tfFeedbackSearch = new javax.swing.JTextField();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblFeedbacks = new javax.swing.JTable();
        btnDeleteBookFeedback = new javax.swing.JButton();
        btnApproveBookFeedback = new javax.swing.JButton();
        jPanelSubmitFeedback = new javax.swing.JPanel();
        jTextFieldBookId1 = new javax.swing.JTextField();
        jTextFieldAvailability1 = new javax.swing.JTextField();
        jTextFieldReserveISBN1 = new javax.swing.JTextField();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextAreaDescription1 = new javax.swing.JTextArea();
        jTextFieldAuthor1 = new javax.swing.JTextField();
        jTextFieldReserveBookName1 = new javax.swing.JTextField();
        jButtonConfirmReservation1 = new javax.swing.JButton();
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
        setTitle("Library Management System : Librarian");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jDesktopPaneLibraryMain.setBackground(new java.awt.Color(255, 255, 255));
        jDesktopPaneLibraryMain.setForeground(new java.awt.Color(255, 255, 255));
        jDesktopPaneLibraryMain.setPreferredSize(new java.awt.Dimension(1022, 710));
        jDesktopPaneLibraryMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelHeader.setBackground(new java.awt.Color(0, 102, 102));
        jPanelHeader.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/librarylogo.png"))); // NOI18N
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
        btnIssueBookTab.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/menuBtn.png"))); // NOI18N
        btnIssueBookTab.setText("ISSUE BOOK");
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

        btnManageBooks.setBackground(new java.awt.Color(0, 102, 102));
        btnManageBooks.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        btnManageBooks.setForeground(new java.awt.Color(255, 255, 255));
        btnManageBooks.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/menuBtn.png"))); // NOI18N
        btnManageBooks.setText("MANAGE BOOKS");
        btnManageBooks.setFocusPainted(false);
        btnManageBooks.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnManageBooks.setIconTextGap(5);
        btnManageBooks.setMargin(new java.awt.Insets(2, 0, 2, 5));
        btnManageBooks.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnManageBooksMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnManageBooksMouseExited(evt);
            }
        });
        btnManageBooks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManageBooksActionPerformed(evt);
            }
        });
        jPanelButtons.add(btnManageBooks, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 190, 40));

        btnMembersTab.setBackground(new java.awt.Color(0, 102, 102));
        btnMembersTab.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        btnMembersTab.setForeground(new java.awt.Color(255, 255, 255));
        btnMembersTab.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/menuBtn.png"))); // NOI18N
        btnMembersTab.setText("MEMBERS");
        btnMembersTab.setFocusPainted(false);
        btnMembersTab.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnMembersTab.setIconTextGap(5);
        btnMembersTab.setMargin(new java.awt.Insets(2, 0, 2, 5));
        btnMembersTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMembersTabMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMembersTabMouseExited(evt);
            }
        });
        btnMembersTab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMembersTabActionPerformed(evt);
            }
        });
        jPanelButtons.add(btnMembersTab, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 190, 40));

        btnBorrowedHistory.setBackground(new java.awt.Color(0, 102, 102));
        btnBorrowedHistory.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        btnBorrowedHistory.setForeground(new java.awt.Color(255, 255, 255));
        btnBorrowedHistory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/menuBtn.png"))); // NOI18N
        btnBorrowedHistory.setText("HISTORY");
        btnBorrowedHistory.setFocusPainted(false);
        btnBorrowedHistory.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnBorrowedHistory.setIconTextGap(5);
        btnBorrowedHistory.setMargin(new java.awt.Insets(2, 0, 2, 5));
        btnBorrowedHistory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBorrowedHistoryMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBorrowedHistoryMouseExited(evt);
            }
        });
        btnBorrowedHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrowedHistoryActionPerformed(evt);
            }
        });
        jPanelButtons.add(btnBorrowedHistory, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 190, 40));

        btnBookFeedBack.setBackground(new java.awt.Color(0, 102, 102));
        btnBookFeedBack.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        btnBookFeedBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBookFeedBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/menuBtn.png"))); // NOI18N
        btnBookFeedBack.setText("FEEDBACK");
        btnBookFeedBack.setFocusPainted(false);
        btnBookFeedBack.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnBookFeedBack.setIconTextGap(5);
        btnBookFeedBack.setMargin(new java.awt.Insets(2, 0, 2, 5));
        btnBookFeedBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBookFeedBack11MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBookFeedBack11MouseExited(evt);
            }
        });
        btnBookFeedBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBookFeedBackActionPerformed(evt);
            }
        });
        jPanelButtons.add(btnBookFeedBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 190, 40));

        jButtonLogout.setBackground(new java.awt.Color(0, 102, 102));
        jButtonLogout.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 18)); // NOI18N
        jButtonLogout.setForeground(new java.awt.Color(255, 255, 255));
        jButtonLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/logout.png"))); // NOI18N
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
        jButtonSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/settings.png"))); // NOI18N
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

        tfRsvBookISBNSearch.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfRsvBookISBNSearch.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Search"));
        tfRsvBookISBNSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfRsvBookISBNSearchKeyReleased(evt);
            }
        });
        jPanelSearchBookTab.add(tfRsvBookISBNSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 200, 40));

        tblBookData.setModel(new javax.swing.table.DefaultTableModel(
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
        tblBookData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBookDataMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblBookData);
        if (tblBookData.getColumnModel().getColumnCount() > 0) {
            tblBookData.getColumnModel().getColumn(0).setResizable(false);
            tblBookData.getColumnModel().getColumn(1).setResizable(false);
            tblBookData.getColumnModel().getColumn(2).setResizable(false);
            tblBookData.getColumnModel().getColumn(3).setResizable(false);
        }

        jPanelSearchBookTab.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 750, 400));

        tbIssueBook.addTab("Search Book", jPanelSearchBookTab);

        jPanelReserveBookTab.setBackground(new java.awt.Color(255, 255, 255));
        jPanelReserveBookTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tfIssueBookBID.setEditable(false);
        tfIssueBookBID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfIssueBookBID.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Book ID"));
        jPanelReserveBookTab.add(tfIssueBookBID, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 200, 40));

        tfIssueBookAvailability.setEditable(false);
        tfIssueBookAvailability.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfIssueBookAvailability.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Availability"));
        jPanelReserveBookTab.add(tfIssueBookAvailability, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 130, 200, 40));

        tfIssueBookISBN.setEditable(false);
        tfIssueBookISBN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfIssueBookISBN.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Book ISBN"));
        jPanelReserveBookTab.add(tfIssueBookISBN, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 270, 200, 40));

        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Description"));

        tfIssueBookDescription.setEditable(false);
        tfIssueBookDescription.setColumns(20);
        tfIssueBookDescription.setRows(6);
        jScrollPane3.setViewportView(tfIssueBookDescription);

        jPanelReserveBookTab.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 340, 520, 140));

        tfIssueBookAuthor.setEditable(false);
        tfIssueBookAuthor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfIssueBookAuthor.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Author Name"));
        jPanelReserveBookTab.add(tfIssueBookAuthor, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 60, 200, 40));

        tfIssueBookBookName.setEditable(false);
        tfIssueBookBookName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfIssueBookBookName.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Book Name"));
        jPanelReserveBookTab.add(tfIssueBookBookName, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, 200, 40));

        btnIssueBook.setBackground(new java.awt.Color(0, 102, 102));
        btnIssueBook.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnIssueBook.setForeground(new java.awt.Color(255, 255, 255));
        btnIssueBook.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/reservebtn.png"))); // NOI18N
        btnIssueBook.setText("Issue");
        btnIssueBook.setFocusable(false);
        btnIssueBook.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnIssueBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIssueBookActionPerformed(evt);
            }
        });
        jPanelReserveBookTab.add(btnIssueBook, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 500, 110, 30));

        tfIssueBookUID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfIssueBookUID.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "UID"));
        jPanelReserveBookTab.add(tfIssueBookUID, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 200, 200, 40));

        tfIssueBookRenewDate.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Renew Date"));
        jPanelReserveBookTab.add(tfIssueBookRenewDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 270, 200, 45));

        tfIssueBBID.setEditable(false);
        tfIssueBBID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfIssueBBID.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Borrowed ID"));
        jPanelReserveBookTab.add(tfIssueBBID, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 200, 40));

        tfIssueBookRequestedID.setEditable(false);
        tfIssueBookRequestedID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfIssueBookRequestedID.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Reserved ID"));
        jPanelReserveBookTab.add(tfIssueBookRequestedID, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 130, 200, 40));

        tbIssueBook.addTab("Issue Book", jPanelReserveBookTab);

        jPanelSearchBookTab2.setBackground(new java.awt.Color(255, 255, 255));
        jPanelSearchBookTab2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tfRsvBookSearch.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfRsvBookSearch.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Search"));
        tfRsvBookSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfRsvBookSearchKeyReleased(evt);
            }
        });
        jPanelSearchBookTab2.add(tfRsvBookSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 200, 40));

        tblReservedBookList.setModel(new javax.swing.table.DefaultTableModel(
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
        tblReservedBookList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblReservedBookListMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblReservedBookList);
        if (tblReservedBookList.getColumnModel().getColumnCount() > 0) {
            tblReservedBookList.getColumnModel().getColumn(0).setResizable(false);
            tblReservedBookList.getColumnModel().getColumn(1).setResizable(false);
            tblReservedBookList.getColumnModel().getColumn(2).setResizable(false);
            tblReservedBookList.getColumnModel().getColumn(3).setResizable(false);
        }

        jPanelSearchBookTab2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 750, 400));

        tbIssueBook.addTab("Reserved Book List", jPanelSearchBookTab2);

        pnlIssueBook.add(tbIssueBook, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelToggle.add(pnlIssueBook, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        pnlManageBook.setBackground(new java.awt.Color(255, 255, 255));
        pnlManageBook.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbpnlManageBook.setBackground(new java.awt.Color(255, 255, 255));
        tbpnlManageBook.setFocusable(false);
        tbpnlManageBook.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jPanelSearchBookTab1.setBackground(new java.awt.Color(255, 255, 255));
        jPanelSearchBookTab1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tfManageBookSearch.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfManageBookSearch.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Search"));
        tfManageBookSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfManageBookSearchKeyReleased(evt);
            }
        });
        jPanelSearchBookTab1.add(tfManageBookSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 200, 40));

        tblManageBook.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblManageBook.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblManageBookMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(tblManageBook);
        if (tblManageBook.getColumnModel().getColumnCount() > 0) {
            tblManageBook.getColumnModel().getColumn(0).setResizable(false);
            tblManageBook.getColumnModel().getColumn(1).setResizable(false);
            tblManageBook.getColumnModel().getColumn(2).setResizable(false);
            tblManageBook.getColumnModel().getColumn(3).setResizable(false);
        }

        jPanelSearchBookTab1.add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 750, 320));

        btnDeleteBook.setBackground(new java.awt.Color(0, 102, 102));
        btnDeleteBook.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnDeleteBook.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteBook.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/reservebtn.png"))); // NOI18N
        btnDeleteBook.setText("Delete");
        btnDeleteBook.setFocusable(false);
        btnDeleteBook.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDeleteBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteBookActionPerformed(evt);
            }
        });
        jPanelSearchBookTab1.add(btnDeleteBook, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 480, 110, 30));

        btnEditBook.setBackground(new java.awt.Color(0, 102, 102));
        btnEditBook.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnEditBook.setForeground(new java.awt.Color(255, 255, 255));
        btnEditBook.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/reservebtn.png"))); // NOI18N
        btnEditBook.setText("Edit");
        btnEditBook.setFocusable(false);
        btnEditBook.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnEditBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditBookActionPerformed(evt);
            }
        });
        jPanelSearchBookTab1.add(btnEditBook, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 480, 110, 30));

        tbpnlManageBook.addTab("View", jPanelSearchBookTab1);

        jPanelAddBook.setBackground(new java.awt.Color(255, 255, 255));
        jPanelAddBook.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tfAddBookBID.setEditable(false);
        tfAddBookBID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfAddBookBID.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Book ID"));
        jPanelAddBook.add(tfAddBookBID, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 200, 40));

        tfAddBookVersion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfAddBookVersion.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Version"));
        jPanelAddBook.add(tfAddBookVersion, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 270, 200, 40));

        jScrollPane9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Description"));

        tfAddBookDescription.setColumns(20);
        tfAddBookDescription.setRows(6);
        jScrollPane9.setViewportView(tfAddBookDescription);

        jPanelAddBook.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 340, 520, 140));

        tfAddBookAuthorName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfAddBookAuthorName.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Author Name"));
        jPanelAddBook.add(tfAddBookAuthorName, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 60, 200, 40));

        tfAddBookQty.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfAddBookQty.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Quantity"));
        tfAddBookQty.setName(""); // NOI18N
        tfAddBookQty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfAddBookQtyActionPerformed(evt);
            }
        });
        jPanelAddBook.add(tfAddBookQty, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 200, 200, 40));

        btnAddBook.setBackground(new java.awt.Color(0, 102, 102));
        btnAddBook.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnAddBook.setForeground(new java.awt.Color(255, 255, 255));
        btnAddBook.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/reservebtn.png"))); // NOI18N
        btnAddBook.setText("ADD");
        btnAddBook.setFocusable(false);
        btnAddBook.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAddBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddBookActionPerformed(evt);
            }
        });
        jPanelAddBook.add(btnAddBook, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 500, 110, 30));

        tfAddBookBookName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfAddBookBookName.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Book Name"));
        jPanelAddBook.add(tfAddBookBookName, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 200, 40));

        tfAddBookPrice.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfAddBookPrice.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Price"));
        tfAddBookPrice.setName(""); // NOI18N
        tfAddBookPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfAddBookPriceActionPerformed(evt);
            }
        });
        jPanelAddBook.add(tfAddBookPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 130, 200, 40));

        tfAddBookISBN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfAddBookISBN.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Book ISBN"));
        jPanelAddBook.add(tfAddBookISBN, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, 200, 40));

        tbpnlManageBook.addTab("Add", jPanelAddBook);

        jPanelAddBook1.setBackground(new java.awt.Color(255, 255, 255));
        jPanelAddBook1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tfEditBookBID.setEditable(false);
        tfEditBookBID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfEditBookBID.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Book ID"));
        jPanelAddBook1.add(tfEditBookBID, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 200, 40));

        tfEditBookVersion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfEditBookVersion.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Version"));
        jPanelAddBook1.add(tfEditBookVersion, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 270, 200, 40));

        jScrollPane11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Description"));

        tfEditBookDescription.setColumns(20);
        tfEditBookDescription.setRows(6);
        jScrollPane11.setViewportView(tfEditBookDescription);

        jPanelAddBook1.add(jScrollPane11, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 340, 520, 140));

        tfEditBookAuthor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfEditBookAuthor.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Author Name"));
        jPanelAddBook1.add(tfEditBookAuthor, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 60, 200, 40));

        tfEditBookQuantity.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfEditBookQuantity.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Quantity"));
        tfEditBookQuantity.setName(""); // NOI18N
        tfEditBookQuantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfEditBookQuantityActionPerformed(evt);
            }
        });
        jPanelAddBook1.add(tfEditBookQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 200, 200, 40));

        tfEditBookName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfEditBookName.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Book Name"));
        jPanelAddBook1.add(tfEditBookName, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 200, 40));

        tfEditBookPrice.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfEditBookPrice.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Price"));
        tfEditBookPrice.setName(""); // NOI18N
        tfEditBookPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfEditBookPriceActionPerformed(evt);
            }
        });
        jPanelAddBook1.add(tfEditBookPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 130, 200, 40));

        tfEditBookISBN.setEditable(false);
        tfEditBookISBN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfEditBookISBN.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Book ISBN"));
        jPanelAddBook1.add(tfEditBookISBN, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, 200, 40));

        btnUpdateBook.setBackground(new java.awt.Color(0, 102, 102));
        btnUpdateBook.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnUpdateBook.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdateBook.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/reservebtn.png"))); // NOI18N
        btnUpdateBook.setText("Update");
        btnUpdateBook.setFocusable(false);
        btnUpdateBook.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnUpdateBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateBookActionPerformed(evt);
            }
        });
        jPanelAddBook1.add(btnUpdateBook, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 500, 110, 30));

        tbpnlManageBook.addTab("Update", jPanelAddBook1);

        pnlManageBook.add(tbpnlManageBook, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelToggle.add(pnlManageBook, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        pnlMembers.setBackground(new java.awt.Color(255, 255, 255));
        pnlMembers.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPanePendingReturn.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPanePendingReturn.setFocusable(false);
        jTabbedPanePendingReturn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jPanelPending.setBackground(new java.awt.Color(255, 255, 255));
        jPanelPending.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tfMembersSearch.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfMembersSearch.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Search"));
        tfMembersSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfMembersSearchKeyReleased(evt);
            }
        });
        jPanelPending.add(tfMembersSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 200, 40));

        tblMemebers.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(tblMemebers);

        jPanelPending.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 750, 320));

        jButtonCancelRequest1.setBackground(new java.awt.Color(0, 102, 102));
        jButtonCancelRequest1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonCancelRequest1.setForeground(new java.awt.Color(255, 255, 255));
        jButtonCancelRequest1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/settings.png"))); // NOI18N
        jButtonCancelRequest1.setText("Update Max Book Count");
        jButtonCancelRequest1.setFocusable(false);
        jButtonCancelRequest1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonCancelRequest1.setMargin(new java.awt.Insets(2, 0, 2, 5));
        jButtonCancelRequest1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelRequest1ActionPerformed(evt);
            }
        });
        jPanelPending.add(jButtonCancelRequest1, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 490, 190, 30));

        jTabbedPanePendingReturn.addTab("Members", jPanelPending);

        pnlMembers.add(jTabbedPanePendingReturn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelToggle.add(pnlMembers, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        pnlBorrowedHistory.setBackground(new java.awt.Color(255, 255, 255));
        pnlBorrowedHistory.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbBorrowedHistory.setBackground(new java.awt.Color(255, 255, 255));
        tbBorrowedHistory.setFocusable(false);
        tbBorrowedHistory.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jPanelHistory.setBackground(new java.awt.Color(255, 255, 255));
        jPanelHistory.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tfBorrowedSearch.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfBorrowedSearch.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Search"));
        tfBorrowedSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfBorrowedSearchKeyReleased(evt);
            }
        });
        jPanelHistory.add(tfBorrowedSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 200, 40));

        tblBorrowedBookHistory.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane5.setViewportView(tblBorrowedBookHistory);

        jPanelHistory.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 750, 320));

        btnHistoryEdit.setBackground(new java.awt.Color(0, 102, 102));
        btnHistoryEdit.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnHistoryEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnHistoryEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/settings.png"))); // NOI18N
        btnHistoryEdit.setText("Edit");
        btnHistoryEdit.setFocusable(false);
        btnHistoryEdit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnHistoryEdit.setMargin(new java.awt.Insets(2, 0, 2, 5));
        btnHistoryEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistoryEditActionPerformed(evt);
            }
        });
        jPanelHistory.add(btnHistoryEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 490, 120, 30));

        btnHistoryDelete.setBackground(new java.awt.Color(0, 102, 102));
        btnHistoryDelete.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnHistoryDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnHistoryDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/reservebtn.png"))); // NOI18N
        btnHistoryDelete.setText("Delete");
        btnHistoryDelete.setFocusable(false);
        btnHistoryDelete.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnHistoryDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistoryDeleteActionPerformed(evt);
            }
        });
        jPanelHistory.add(btnHistoryDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 490, 110, 30));

        btnMarkRecieved.setBackground(new java.awt.Color(0, 102, 102));
        btnMarkRecieved.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnMarkRecieved.setForeground(new java.awt.Color(255, 255, 255));
        btnMarkRecieved.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/reservebtn.png"))); // NOI18N
        btnMarkRecieved.setText("Mark Received");
        btnMarkRecieved.setFocusable(false);
        btnMarkRecieved.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnMarkRecieved.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMarkRecievedActionPerformed(evt);
            }
        });
        jPanelHistory.add(btnMarkRecieved, new org.netbeans.lib.awtextra.AbsoluteConstraints(331, 490, -1, 30));

        chkPendingList.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Pending List"));
        chkPendingList.setBorderPainted(true);
        chkPendingList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chkPendingListMouseClicked(evt);
            }
        });
        chkPendingList.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                chkPendingListPropertyChange(evt);
            }
        });
        jPanelHistory.add(chkPendingList, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 50, 190, 40));

        tbBorrowedHistory.addTab("Borrowed Book History", jPanelHistory);

        jPanelReserveBookTab1.setBackground(new java.awt.Color(255, 255, 255));
        jPanelReserveBookTab1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tfHistoryBID.setEditable(false);
        tfHistoryBID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfHistoryBID.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Book ID"));
        jPanelReserveBookTab1.add(tfHistoryBID, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 200, 40));

        tfHistoryISBN.setEditable(false);
        tfHistoryISBN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfHistoryISBN.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Book ISBN"));
        jPanelReserveBookTab1.add(tfHistoryISBN, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 270, 200, 40));

        jScrollPane12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Description"));

        tfHistoryDescription.setEditable(false);
        tfHistoryDescription.setColumns(20);
        tfHistoryDescription.setRows(6);
        jScrollPane12.setViewportView(tfHistoryDescription);

        jPanelReserveBookTab1.add(jScrollPane12, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 340, 520, 140));

        tfHistoryAuthor.setEditable(false);
        tfHistoryAuthor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfHistoryAuthor.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Author Name"));
        jPanelReserveBookTab1.add(tfHistoryAuthor, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 60, 200, 40));

        tfHistoryBookName.setEditable(false);
        tfHistoryBookName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfHistoryBookName.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Book Name"));
        jPanelReserveBookTab1.add(tfHistoryBookName, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, 200, 40));

        tfHistoryUID.setEditable(false);
        tfHistoryUID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfHistoryUID.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "UID"));
        jPanelReserveBookTab1.add(tfHistoryUID, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 130, 200, 40));

        tfHistoryRenewDate.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Renew Date"));
        jPanelReserveBookTab1.add(tfHistoryRenewDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 200, 200, 45));

        tfHistoryBBID.setEditable(false);
        tfHistoryBBID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfHistoryBBID.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Borrowed ID"));
        jPanelReserveBookTab1.add(tfHistoryBBID, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 200, 40));

        btnHistoryUpdate.setBackground(new java.awt.Color(0, 102, 102));
        btnHistoryUpdate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnHistoryUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnHistoryUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/reservebtn.png"))); // NOI18N
        btnHistoryUpdate.setText("Update");
        btnHistoryUpdate.setFocusable(false);
        btnHistoryUpdate.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnHistoryUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistoryUpdateActionPerformed(evt);
            }
        });
        jPanelReserveBookTab1.add(btnHistoryUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 500, 110, 30));

        tbBorrowedHistory.addTab("Edit Issued Book", jPanelReserveBookTab1);

        pnlBorrowedHistory.add(tbBorrowedHistory, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelToggle.add(pnlBorrowedHistory, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        pnlBookFeedBacks.setBackground(new java.awt.Color(255, 255, 255));
        pnlBookFeedBacks.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPanelFeedback.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPanelFeedback.setFocusable(false);
        jTabbedPanelFeedback.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jPanelFeedback1.setBackground(new java.awt.Color(255, 255, 255));
        jPanelFeedback1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tfFeedbackSearch.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfFeedbackSearch.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Search"));
        tfFeedbackSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfFeedbackSearchKeyReleased(evt);
            }
        });
        jPanelFeedback1.add(tfFeedbackSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 200, 40));

        tblFeedbacks.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane6.setViewportView(tblFeedbacks);

        jPanelFeedback1.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 750, 320));

        btnDeleteBookFeedback.setBackground(new java.awt.Color(0, 102, 102));
        btnDeleteBookFeedback.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnDeleteBookFeedback.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteBookFeedback.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/settings.png"))); // NOI18N
        btnDeleteBookFeedback.setText("Delete");
        btnDeleteBookFeedback.setFocusable(false);
        btnDeleteBookFeedback.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDeleteBookFeedback.setMargin(new java.awt.Insets(2, 0, 2, 5));
        btnDeleteBookFeedback.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteBookFeedbackActionPerformed(evt);
            }
        });
        jPanelFeedback1.add(btnDeleteBookFeedback, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 490, 120, 30));

        btnApproveBookFeedback.setBackground(new java.awt.Color(0, 102, 102));
        btnApproveBookFeedback.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnApproveBookFeedback.setForeground(new java.awt.Color(255, 255, 255));
        btnApproveBookFeedback.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/settings.png"))); // NOI18N
        btnApproveBookFeedback.setText("Approve");
        btnApproveBookFeedback.setFocusable(false);
        btnApproveBookFeedback.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnApproveBookFeedback.setMargin(new java.awt.Insets(2, 0, 2, 5));
        btnApproveBookFeedback.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApproveBookFeedbackActionPerformed(evt);
            }
        });
        jPanelFeedback1.add(btnApproveBookFeedback, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 490, 120, 30));

        jTabbedPanelFeedback.addTab("Feedbacks of Books", jPanelFeedback1);

        jPanelSubmitFeedback.setBackground(new java.awt.Color(255, 255, 255));
        jPanelSubmitFeedback.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextFieldBookId1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldBookId1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Book ID"));
        jPanelSubmitFeedback.add(jTextFieldBookId1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 200, 40));

        jTextFieldAvailability1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldAvailability1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Availability"));
        jPanelSubmitFeedback.add(jTextFieldAvailability1, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 130, 200, 40));

        jTextFieldReserveISBN1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldReserveISBN1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Book ISBN"));
        jPanelSubmitFeedback.add(jTextFieldReserveISBN1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, 200, 40));

        jScrollPane7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Description"));

        jTextAreaDescription1.setColumns(20);
        jTextAreaDescription1.setRows(6);
        jScrollPane7.setViewportView(jTextAreaDescription1);

        jPanelSubmitFeedback.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 270, 520, 140));

        jTextFieldAuthor1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldAuthor1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Author Name"));
        jPanelSubmitFeedback.add(jTextFieldAuthor1, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 60, 200, 40));

        jTextFieldReserveBookName1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldReserveBookName1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)), "Book Name"));
        jPanelSubmitFeedback.add(jTextFieldReserveBookName1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 200, 40));

        jButtonConfirmReservation1.setBackground(new java.awt.Color(0, 102, 102));
        jButtonConfirmReservation1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonConfirmReservation1.setForeground(new java.awt.Color(255, 255, 255));
        jButtonConfirmReservation1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Images/reservebtn.png"))); // NOI18N
        jButtonConfirmReservation1.setText("Confirm");
        jButtonConfirmReservation1.setFocusable(false);
        jButtonConfirmReservation1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonConfirmReservation1.setMargin(new java.awt.Insets(2, 0, 2, 5));
        jPanelSubmitFeedback.add(jButtonConfirmReservation1, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 450, 100, 30));

        jTabbedPanelFeedback.addTab("Submit Feedback", jPanelSubmitFeedback);

        pnlBookFeedBacks.add(jTabbedPanelFeedback, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

        jPanelToggle.add(pnlBookFeedBacks, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 570));

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

    private void btnManageBooksMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnManageBooksMouseEntered
        // TODO add your handling code here:
        btnManageBooks.setBackground(new Color(24, 142, 130));
    }//GEN-LAST:event_btnManageBooksMouseEntered

    private void btnManageBooksMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnManageBooksMouseExited
        // TODO add your handling code here:
        btnManageBooks.setBackground(new Color(0, 102, 102));
    }//GEN-LAST:event_btnManageBooksMouseExited

    private void btnMembersTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMembersTabMouseEntered
        // TODO add your handling code here:
        btnMembersTab.setBackground(new Color(24, 142, 130));
    }//GEN-LAST:event_btnMembersTabMouseEntered

    private void btnMembersTabMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMembersTabMouseExited
        // TODO add your handling code here:
        btnMembersTab.setBackground(new Color(0, 102, 102));
    }//GEN-LAST:event_btnMembersTabMouseExited

    private void btnBorrowedHistoryMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBorrowedHistoryMouseEntered
        // TODO add your handling code here:
        btnBorrowedHistory.setBackground(new Color(24, 142, 130));
    }//GEN-LAST:event_btnBorrowedHistoryMouseEntered

    private void btnBorrowedHistoryMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBorrowedHistoryMouseExited
        // TODO add your handling code here:
        btnBorrowedHistory.setBackground(new Color(0, 102, 102));
    }//GEN-LAST:event_btnBorrowedHistoryMouseExited

    private void btnBookFeedBack11MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBookFeedBack11MouseEntered
        // TODO add your handling code here:
        btnBookFeedBack.setBackground(new Color(24, 142, 130));
    }//GEN-LAST:event_btnBookFeedBack11MouseEntered

    private void btnBookFeedBack11MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBookFeedBack11MouseExited
        // TODO add your handling code here:
        btnBookFeedBack.setBackground(new Color(0, 102, 102));
    }//GEN-LAST:event_btnBookFeedBack11MouseExited

    private void jButtonSettings11MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSettings11MouseEntered
        // TODO add your handling code here:
        jButtonSettings.setBackground(new Color(24, 142, 130));
    }//GEN-LAST:event_jButtonSettings11MouseEntered

    private void jButtonSettings11MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSettings11MouseExited
        // TODO add your handling code here:
        jButtonSettings.setBackground(new Color(0, 102, 102));
    }//GEN-LAST:event_jButtonSettings11MouseExited

    private void btnIssueBookTabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIssueBookTabActionPerformed
        togglePanels("ReserveBook");
        try {
            loadIssueBookTable();
        } catch (IOException ex) {
            LOG.error("Error loading borrowed history data to the table \n" + ex);
            Logger.getLogger(LibrarianMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadReservedBookList();

    }//GEN-LAST:event_btnIssueBookTabActionPerformed

    private void btnManageBooksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManageBooksActionPerformed
        // TODO add your handling code here:
        togglePanels("RequestedList");
        loadManageBookTable();
        tfAddBookBID.setVisible(true);
        if (tfAddBookBID.getText().isEmpty()) {
            try {
                tfAddBookBID.setText(BookController.autoGenerateBookID());
            } catch (ClassNotFoundException | SQLException | IOException ex) {
                LOG.error("Error occured while generating borrowed book id\n" + ex);
                Logger.getLogger(LibrarianMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_btnManageBooksActionPerformed

    private void btnMembersTabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMembersTabActionPerformed
        // TODO add your handling code here:
        togglePanels("PendingBooks");
        loadMembersTable();
    }//GEN-LAST:event_btnMembersTabActionPerformed

    private void btnBorrowedHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrowedHistoryActionPerformed
        // TODO add your handling code here:
        togglePanels("History");
        chkPendingList.setSelected(false);
    }//GEN-LAST:event_btnBorrowedHistoryActionPerformed

    private void btnBookFeedBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBookFeedBackActionPerformed
        // TODO add your handling code here:
        togglePanels("Feedbacks");
        loadFeedbackTable();
    }//GEN-LAST:event_btnBookFeedBackActionPerformed

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
            LOG.error("Error occurred while setting user data into settings id\n" + ex);
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButtonSettingsActionPerformed

    private void jButtonLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogoutActionPerformed

        int result = JOptionPane.showConfirmDialog(rootPane, "Are you sure want to Logout?", "Confirm Logout", 0);

        if (result == 0) {
            this.dispose();
            SystemLogin login = new SystemLogin();
            LOG.info("User - " + UID + " has logged out from the system");
            login.setVisible(true);
        }
    }//GEN-LAST:event_jButtonLogoutActionPerformed

    private void tbIssueBookMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbIssueBookMouseClicked
        try {
            // TODO add your handling code here:
            loadIssueBookTable();
        } catch (IOException ex) {
            LOG.error("Error occured while loading issued book history to the table\n" + ex);
            Logger.getLogger(LibrarianMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadReservedBookList();
        if (tfAddBookBID.getText().isEmpty()) {
            try {
                tfAddBookBID.setText(BookController.autoGenerateBookID());
            } catch (ClassNotFoundException | SQLException | IOException ex) {
                LOG.error("Error occured while generating book id\n" + ex);
                Logger.getLogger(LibrarianMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (tfIssueBBID.getText().isEmpty()) {
            try {
                tfIssueBBID.setText(BookController.autoGenerateBorrowedID());
            } catch (ClassNotFoundException | SQLException | IOException ex) {
                LOG.error("Error occured while generating borrowed book id\n" + ex);
                Logger.getLogger(LibrarianMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_tbIssueBookMouseClicked

    private void btnIssueBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIssueBookActionPerformed

        if (tfIssueBookRenewDate.getDate() == null || !Validation.validRenewDate(tfIssueBookRenewDate.getDate())) {

            JOptionPane.showMessageDialog(rootPane, "Invalid renew date", "Error!", JOptionPane.ERROR_MESSAGE);
        } else if (!tfIssueBookBID.getText().isEmpty()) {
            try {
                if (!UserController.isExpiredUser(tfIssueBookUID.getText())) {
                    System.out.println(UserController.isExpiredUser(tfIssueBookUID.getText()));
                    JOptionPane.showMessageDialog(rootPane, "The user account is expired, "
                            + "Can't issue books for the mentioned user", "Notify!", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                String message = BookController.issueBook(new BorrowedBookModel(tfIssueBBID.getText(), tfIssueBookUID.getText(), tfIssueBookBID.getText(), "", tfIssueBookRenewDate.getDate()), isReserved);
                JOptionPane.showMessageDialog(rootPane, message, "Notify!", JOptionPane.INFORMATION_MESSAGE);
                LOG.info(message);
                clearReserveBookData();
                tfIssueBookUID.setEditable(true);
                isReserved = false;
                tfIssueBookRequestedID.setVisible(false);
                tfIssueBookAvailability.setVisible(true);

            } catch (ClassNotFoundException | SQLException | IOException ex) {
                LOG.error("Error occured while issuing book to the user\n" + ex);
                Logger.getLogger(LibrarianMain.class.getName()).log(Level.SEVERE, null, ex);

            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a book to issue", "Error!", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_btnIssueBookActionPerformed

    private void tblBookDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBookDataMouseClicked

        int row = tblBookData.getSelectedRow();

        tfIssueBookBID.setText(tblBookData.getValueAt(row, 0).toString());
        tfIssueBookBookName.setText(tblBookData.getValueAt(row, 1).toString());
        tfIssueBookAuthor.setText(tblBookData.getValueAt(row, 2).toString());
        tfIssueBookAvailability.setText(tblBookData.getValueAt(row, 5).toString());
        tfIssueBookISBN.setText(tblBookData.getValueAt(row, 3).toString());
        tfIssueBookDescription.setText(tblBookData.getValueAt(row, 4).toString());
        tfIssueBookUID.setText("");
        tfIssueBookUID.setEditable(true);
        tfIssueBookRequestedID.setVisible(false);
        tfIssueBookAvailability.setVisible(true);
        isReserved = false;

        tbIssueBook.setSelectedIndex(1);

        try {
            tfIssueBBID.setText(BookController.autoGenerateBorrowedID());
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            LOG.error("Error occured while generating borrowed book id\n" + ex);
            Logger.getLogger(LibrarianMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_tblBookDataMouseClicked

    private void tblManageBookMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblManageBookMouseClicked


    }//GEN-LAST:event_tblManageBookMouseClicked

    private void tfAddBookPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfAddBookPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfAddBookPriceActionPerformed

    private void btnAddBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddBookActionPerformed

        String Description;
        if (!tfAddBookBookName.getText().isEmpty() && tfAddBookBookName.getText().length() <= 30) {
            if (Validation.isLetters(tfAddBookAuthorName.getText())) {
                if (Validation.isISBN(tfAddBookISBN.getText())) {
                    if (!tfAddBookVersion.getText().isEmpty()) {
                        if (Validation.isNumbers(tfAddBookQty.getText())) {

                            if (Validation.isDouble(tfAddBookPrice.getText())) {

                                if (tfAddBookDescription.getText().isEmpty()) {
                                    Description = "A description is can not be found for the given book";
                                } else {
                                    Description = tfAddBookDescription.getText();
                                }

                                try {
                                    if (BookController.addBook(new BookModel(tfAddBookBID.getText(), tfAddBookBookName.getText(), tfAddBookAuthorName.getText(), tfAddBookQty.getText(), tfAddBookISBN.getText(), Description, "Yes", tfAddBookVersion.getText(), tfAddBookQty.getText()))) {
                                        JOptionPane.showMessageDialog(rootPane, "Successfully added", "Success!", JOptionPane.INFORMATION_MESSAGE);
                                        loadManageBookTable();
                                        LOG.info("Successfully inserted (" + tfAddBookISBN.getText() + ") book to the database");
                                        clearAddBookdata();

                                    } else {
                                        JOptionPane.showMessageDialog(rootPane, "Error occured while inserting", "Error!", JOptionPane.ERROR_MESSAGE);
                                        LOG.info("Error occured while inserting (" + tfAddBookISBN.getText() + ") book to the database");
                                    }

                                } catch (ClassNotFoundException | SQLException | IOException ex) {
                                    LOG.error("Error occured while inserting book to the database\n" + ex);
                                    Logger.getLogger(LibrarianMain.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            } else {
                                JOptionPane.showMessageDialog(rootPane, "Invalid price", "Error!", JOptionPane.ERROR_MESSAGE);
                            }

                        } else {
                            JOptionPane.showMessageDialog(rootPane, "Invalid quantity", "Error!", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Version number can't be empty", "Error!", JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(rootPane, "Invalid ISBN", "Error!", JOptionPane.ERROR_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(rootPane, "Invalid author name", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Invalid book name", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAddBookActionPerformed

    private void tfAddBookQtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfAddBookQtyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfAddBookQtyActionPerformed

    private void tfEditBookQuantityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfEditBookQuantityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfEditBookQuantityActionPerformed

    private void tfEditBookPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfEditBookPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfEditBookPriceActionPerformed

    private void btnUpdateBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateBookActionPerformed

        if (JOptionPane.showConfirmDialog(null,
                "Do you want to edit this record ?", "Edit?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

            String Description;
            if (!tfEditBookName.getText().isEmpty() && tfEditBookName.getText().length() <= 30) {
                if (Validation.isLetters(tfEditBookAuthor.getText())) {
                    if (!tfEditBookVersion.getText().isEmpty()) {
                        if (Validation.isNumbers(tfEditBookQuantity.getText())) {

                            if (Validation.isDouble(tfEditBookPrice.getText())) {

                                if (tfEditBookDescription.getText().isEmpty()) {
                                    Description = "A description is can not be found for the given book";
                                } else {
                                    Description = tfEditBookDescription.getText();
                                }

                                try {
                                    if (BookController.updateBookByISBN(new BookModel(tfEditBookBID.getText(), tfEditBookName.getText(), tfEditBookAuthor.getText(), tfEditBookPrice.getText(), tfEditBookISBN.getText(), Description, "Yes", tfEditBookVersion.getText(), tfEditBookQuantity.getText()))) {
                                        JOptionPane.showMessageDialog(rootPane, "Successfully updated", "Success!", JOptionPane.INFORMATION_MESSAGE);
                                        LOG.info("Successfully updated (" + tfEditBookName.getText().toString() + ") book");
                                        loadManageBookTable();
                                        clearUpdateBookData();
                                    } else {
                                        JOptionPane.showMessageDialog(rootPane, "Error occured while updating", "Error!", JOptionPane.ERROR_MESSAGE);
                                        LOG.info("Error occurred while updating (" + tfEditBookName.getText().toString() + ") book");
                                    }

                                } catch (ClassNotFoundException | SQLException | IOException ex) {
                                    LOG.error("Error occurred while updating book data\n" + ex);
                                    Logger.getLogger(LibrarianMain.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            } else {
                                JOptionPane.showMessageDialog(rootPane, "Invalid price", "Error!", JOptionPane.ERROR_MESSAGE);
                            }

                        } else {
                            JOptionPane.showMessageDialog(rootPane, "Invalid quantity", "Error!", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Version number can't be empty", "Error!", JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(rootPane, "Invalid author name", "Error!", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "Invalid book name", "Error!", JOptionPane.ERROR_MESSAGE);
            }

        }


    }//GEN-LAST:event_btnUpdateBookActionPerformed

    private void btnHistoryDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistoryDeleteActionPerformed

        int row = tblBorrowedBookHistory.getSelectedRow();
        if (row >= 0) {

            if (JOptionPane.showConfirmDialog(null,
                    "Do you want to delete this record ?", "Delete?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                try {
                    if (BookController.deleteBorrowedByBBID(tblBorrowedBookHistory.getValueAt(row, 0).toString(), tblBorrowedBookHistory.getValueAt(row, 2).toString(), tblBorrowedBookHistory.getValueAt(row, 1).toString())) {
                        JOptionPane.showMessageDialog(rootPane, "Successfully deleted", "Deleted!", JOptionPane.INFORMATION_MESSAGE);
                        LOG.info("Successfully deleted (" + tblBorrowedBookHistory.getValueAt(row, 1).toString() + ") book");
                        loadBorrowedBookData("SELECT BBID AS 'Borrowed ID', UID , BID, ISSUEDATE, RENEWDATE FROM borrowedbooks GROUP BY BBID DESC");
                        
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Error occured, please try again", "Error!", JOptionPane.ERROR_MESSAGE);
                        LOG.info("Error occurred while deleting (" + tblBorrowedBookHistory.getValueAt(row, 1).toString() + ") book");
                    }
                } catch (ClassNotFoundException | SQLException | IOException ex) {
                    LOG.error("Error occurred while deleting book data\n" + ex);
                    Logger.getLogger(LibrarianMain.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a record to delete", "Error!", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnHistoryDeleteActionPerformed

    private void btnHistoryUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistoryUpdateActionPerformed
        try {
            if (tfHistoryBBID.getText().isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Please select a record before start editing", "Error!", JOptionPane.ERROR_MESSAGE);
            } else if (tfHistoryRenewDate.getDate() == null || !Validation.validRenewDate(tfHistoryRenewDate.getDate())) {
                JOptionPane.showMessageDialog(rootPane, "Invalid renew date", "Error!", JOptionPane.ERROR_MESSAGE);
            } else if (BookController.updateBorrowedBook(tfHistoryBBID.getText(), tfHistoryRenewDate.getDate(), 0)) {
                JOptionPane.showMessageDialog(rootPane, "Successfully edited", "Success!", JOptionPane.INFORMATION_MESSAGE);
                LOG.info("Successfully updated (" + tfHistoryBookName.getText() + ") borrowed book renew date for user (" + tfHistoryUID.getText() + ")");
                loadBorrowedBookData("SELECT BBID AS 'Borrowed ID', UID , BID, ISSUEDATE, RENEWDATE FROM borrowedbooks GROUP BY BBID DESC");
                clearHistoryData();
            } else {
                LOG.info("Error occurred while updating (" + tfHistoryBookName.getText() + ") borrowed book renew date for user (" + tfHistoryUID.getText() + ")");
                JOptionPane.showMessageDialog(rootPane, "Error occrued while processing", "Error!", JOptionPane.ERROR_MESSAGE);
            }

        } catch (ClassNotFoundException | SQLException | IOException ex) {
            LOG.error("Error occurred while updating borrowed book history (Renew Date)\n" + ex);
            Logger.getLogger(LibrarianMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnHistoryUpdateActionPerformed

    private void btnHistoryEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistoryEditActionPerformed

        try {
            int row = tblBorrowedBookHistory.getSelectedRow();

            tfHistoryBBID.setText(tblBorrowedBookHistory.getValueAt(row, 0).toString());
            tfHistoryUID.setText(tblBorrowedBookHistory.getValueAt(row, 1).toString());
            tfHistoryBID.setText(tblBorrowedBookHistory.getValueAt(row, 2).toString());
            BookModel Book = BookController.searchBookByBID(tblBorrowedBookHistory.getValueAt(row, 2).toString());
            tfHistoryBookName.setText(Book.getBookName());
            tfHistoryDescription.setText(Book.getDescription());
            tfHistoryISBN.setText(Book.getIsbn());
            tfHistoryAuthor.setText(Book.getAuthor());
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse((String) tblBorrowedBookHistory.getValueAt(row, 4).toString());

            tfHistoryRenewDate.setDate(date);
            tbBorrowedHistory.setSelectedIndex(1);
        } catch (ClassNotFoundException | ParseException | SQLException | IOException ex) {
            LOG.error("Error occurred while loading borrowed book history table\n" + ex);
            Logger.getLogger(LibrarianMain.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_btnHistoryEditActionPerformed

    private void btnDeleteBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteBookActionPerformed
        int row = tblManageBook.getSelectedRow();
        if (row >= 0) {
            try {
                if (JOptionPane.showConfirmDialog(null,
                        "Do you want to delete this record ?", "Delete?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    if (BookController.deleteBookByISBN(tblManageBook.getValueAt(row, 3).toString())) {
                        JOptionPane.showMessageDialog(rootPane, "Successfully deleted", "Deleted!", JOptionPane.INFORMATION_MESSAGE);
                        LOG.info("Successfully deleted (" + tblManageBook.getValueAt(row, 1).toString() + ") borrowed book");
                        loadManageBookTable();

                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Error occured, please try again", "Error!", JOptionPane.ERROR_MESSAGE);
                        LOG.info("Error occurred while deleting (" + tblManageBook.getValueAt(row, 1).toString() + ") borrowed book");

                    }
                }

            } catch (ClassNotFoundException | SQLException | IOException ex) {
                LOG.error("Error occurred while deleting borrowed book data\n" + ex);
                Logger.getLogger(LibrarianMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a record to delete", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnDeleteBookActionPerformed

    private void btnEditBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditBookActionPerformed
        int row = tblManageBook.getSelectedRow();

        if (row >= 0) {
            tfEditBookBID.setText(tblManageBook.getValueAt(row, 0).toString());
            tfEditBookName.setText(tblManageBook.getValueAt(row, 1).toString());
            tfEditBookAuthor.setText(tblManageBook.getValueAt(row, 2).toString());
            tfEditBookISBN.setText(tblManageBook.getValueAt(row, 3).toString());
            tfEditBookDescription.setText(tblManageBook.getValueAt(row, 4).toString());
            tfEditBookQuantity.setText(tblManageBook.getValueAt(row, 5).toString());
            tfEditBookPrice.setText(tblManageBook.getValueAt(row, 6).toString());
            tfEditBookVersion.setText(tblManageBook.getValueAt(row, 7).toString());

            tbpnlManageBook.setSelectedIndex(2);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a record to edit", "Error!", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnEditBookActionPerformed

    private void btnDeleteBookFeedbackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteBookFeedbackActionPerformed
        int row = tblFeedbacks.getSelectedRow();
        if (row >= 0) {
            try {
                if (JOptionPane.showConfirmDialog(null,
                        "Do you want to delete this record ?", "Delete?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    if (BookController.deleteBookFeedbacksByFID(tblFeedbacks.getValueAt(row, 0).toString())) {
                        LOG.info("Successfully deleted FID (" + tblFeedbacks.getValueAt(row, 0).toString() + ") book feedback");
                        JOptionPane.showMessageDialog(rootPane, "Successfully deleted", "Deleted!", JOptionPane.INFORMATION_MESSAGE);
                        loadFeedbackTable();
                    } else {
                        LOG.info("Error occurred while deleting FID (" + tblFeedbacks.getValueAt(row, 0).toString() + ") book feedback");
                        JOptionPane.showMessageDialog(rootPane, "Error occurred, please try again", "Error!", JOptionPane.ERROR_MESSAGE);

                    }
                }

            } catch (ClassNotFoundException | SQLException | IOException ex) {
                LOG.error("Error occurred while deleting book feedback data\n" + ex);
                Logger.getLogger(LibrarianMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a record to delete", "Error!", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_btnDeleteBookFeedbackActionPerformed

    private void btnApproveBookFeedbackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApproveBookFeedbackActionPerformed
        // TODO add your handling code here:
        int row = tblFeedbacks.getSelectedRow();
        if (row >= 0) {
            try {
                if (JOptionPane.showConfirmDialog(null,
                        "Do you want to approve this feedback on the book?", "Approve?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

                    if (tblFeedbacks.getValueAt(row, 3).toString().equals("true")) {

                        JOptionPane.showMessageDialog(rootPane, "Selected feedback is already approved", "Error!", JOptionPane.ERROR_MESSAGE);
                    } else if (BookController.approveBookFeedBack(tblFeedbacks.getValueAt(row, 0).toString())) {
                        JOptionPane.showMessageDialog(rootPane, "Successfully approved", "Approved!", JOptionPane.INFORMATION_MESSAGE);
                        LOG.info("Successfully approved FID : (" + tblFeedbacks.getValueAt(row, 0).toString() + ") book feedback");
                        loadFeedbackTable();
                    } else {
                        LOG.info("Error occurred while approving FID : (" + tblFeedbacks.getValueAt(row, 0).toString() + ") book feedback");
                        JOptionPane.showMessageDialog(rootPane, "Error occured, please try again", "Error!", JOptionPane.ERROR_MESSAGE);

                    }

                }

            } catch (ClassNotFoundException | SQLException | IOException ex) {
                LOG.error("Error occurred while approving book feedback data\n" + ex);
                Logger.getLogger(LibrarianMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a feedback to approve", "Error!", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnApproveBookFeedbackActionPerformed

    private void btnMarkRecievedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMarkRecievedActionPerformed

        int row = tblBorrowedBookHistory.getSelectedRow();
        if (row >= 0) {

            if (JOptionPane.showConfirmDialog(null,
                    "Do you want to mark this book as recieved?", "Recieved?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse((String) tblBorrowedBookHistory.getValueAt(row, 4).toString());
                    Date currentDate = new Date();
                    if (date.before(currentDate)) {

                        String fine = JOptionPane.showInputDialog(rootPane, "Please input daily fine for late submission ", "Calculate Fine!", JOptionPane.ERROR_MESSAGE);
                        if (fine.isEmpty() || !Validation.isDouble(fine)) {
                            JOptionPane.showMessageDialog(rootPane, "Invalid fine value", "Error!", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        int diffInDays = (int) ((currentDate.getTime() - date.getTime()) / (1000 * 60 * 60 * 24));

                        double fineValue = diffInDays * Float.parseFloat(fine);

                        if (!FineController.addToFine(new FineModel(FineController.autoGenerateFineID(), tblBorrowedBookHistory.getValueAt(row, 1).toString(), "Late Submission Fine", 0, fineValue))) {
                            JOptionPane.showMessageDialog(rootPane, "Error occured while inserting fine", "Error!", JOptionPane.ERROR_MESSAGE);
                            return;
                        } else {
                            UserModel user = UserController.getUser(tblBorrowedBookHistory.getValueAt(row, 1).toString());

                            WarnedListController.addToWarnedList(new WarnedListModel(WarnedListController.autoGenerateWarnedListID(), tblBorrowedBookHistory.getValueAt(row, 1).toString(), user.getFirstName() + " " + user.getLastName(), "Late Submission", "Librarian Warn"));
                        }

                    }
                    if (BookController.updateBorrowedBook(tblBorrowedBookHistory.getValueAt(row, 0).toString(), date, 1)) {
                        JOptionPane.showMessageDialog(rootPane, "Successfully marked as received", "Success!", JOptionPane.INFORMATION_MESSAGE);

                        loadBorrowedBookData("SELECT BBID AS 'Borrowed ID', UID , BID, ISSUEDATE, RENEWDATE FROM borrowedbooks WHERE ISSUBBMITTED = '0' GROUP BY BBID DESC");

                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Error occured while processing", "Error!", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (ParseException | ClassNotFoundException | SQLException | IOException ex) {
                    Logger.getLogger(LibrarianMain.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a record to mark received", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnMarkRecievedActionPerformed

    private void chkPendingListPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_chkPendingListPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_chkPendingListPropertyChange

    private void chkPendingListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chkPendingListMouseClicked
        if (chkPendingList.isSelected()) {
            loadBorrowedBookData("SELECT BBID AS 'Borrowed ID', UID , BID, ISSUEDATE, RENEWDATE FROM borrowedbooks WHERE ISSUBBMITTED = '0' GROUP BY BBID DESC");
            btnMarkRecieved.setEnabled(true);
        } else {
            loadBorrowedBookData("SELECT BBID AS 'Borrowed ID', UID , BID, ISSUEDATE, RENEWDATE FROM borrowedbooks GROUP BY BBID DESC");
            btnMarkRecieved.setEnabled(false);
        }


    }//GEN-LAST:event_chkPendingListMouseClicked

    private void tfRsvBookISBNSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfRsvBookISBNSearchKeyReleased
        try {
            Utility.filter(tblBookData, tfRsvBookISBNSearch.getText());
        } catch (PatternSyntaxException ex) {

        }

    }//GEN-LAST:event_tfRsvBookISBNSearchKeyReleased

    private void tfManageBookSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfManageBookSearchKeyReleased
        try {
            Utility.filter(tblManageBook, tfManageBookSearch.getText());
        } catch (PatternSyntaxException ex) {

        }
    }//GEN-LAST:event_tfManageBookSearchKeyReleased

    private void tfMembersSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfMembersSearchKeyReleased
        try {
            Utility.filter(tblMemebers, tfMembersSearch.getText());
        } catch (PatternSyntaxException ex) {

        }
    }//GEN-LAST:event_tfMembersSearchKeyReleased

    private void tfBorrowedSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfBorrowedSearchKeyReleased
        try {
            Utility.filter(tblBorrowedBookHistory, tfBorrowedSearch.getText());
        } catch (PatternSyntaxException ex) {

        }

    }//GEN-LAST:event_tfBorrowedSearchKeyReleased

    private void tfFeedbackSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfFeedbackSearchKeyReleased
        try {
            Utility.filter(tblFeedbacks, tfFeedbackSearch.getText());
        } catch (PatternSyntaxException ex) {

        }
    }//GEN-LAST:event_tfFeedbackSearchKeyReleased

    private void tfRsvBookSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfRsvBookSearchKeyReleased
        try {
            Utility.filter(tblReservedBookList, tfRsvBookSearch.getText());
        } catch (PatternSyntaxException ex) {

        }
    }//GEN-LAST:event_tfRsvBookSearchKeyReleased

    private void tblReservedBookListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblReservedBookListMouseClicked

        try {

            int row = tblReservedBookList.getSelectedRow();

            tfIssueBookBID.setText(tblReservedBookList.getValueAt(row, 2).toString());
            tfIssueBookUID.setText(tblReservedBookList.getValueAt(row, 1).toString());
            BookModel book = BookController.searchBookByBID(tblReservedBookList.getValueAt(row, 2).toString());
            tfIssueBookAuthor.setText(book.getAuthor());
            tfIssueBookAvailability.setText(book.getAvailability());
            tfIssueBookBookName.setText(book.getBookName());
            tfIssueBookISBN.setText(book.getIsbn());
            tfIssueBookDescription.setText(book.getDescription());
            tfIssueBookRequestedID.setText(tblReservedBookList.getValueAt(row, 0).toString());
            tfIssueBookRequestedID.setVisible(true);
            tfIssueBookAvailability.setVisible(false);
            tfIssueBookUID.setEditable(false);
            isReserved = true;
            tbIssueBook.setSelectedIndex(1);
            tfIssueBBID.setText(BookController.autoGenerateBorrowedID());
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(LibrarianMain.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_tblReservedBookListMouseClicked

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed

        try {
            String contact = jTextFieldContact.getText();
            if (contact.isEmpty() || contact.length() != 10 || !Validation.isNumbers(contact)) {
                JOptionPane.showMessageDialog(rootPane, "Please enter valid contact "
                        + "number", "Error!", JOptionPane.ERROR_MESSAGE);
            } else if (updateUser(UID, contact)) {
                JOptionPane.showMessageDialog(rootPane, "Details Updated Successfully!",
                        "Success!", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(rootPane, "Error occured while Updating",
                        "Error!", JOptionPane.ERROR_MESSAGE);
            }

        } catch (ClassNotFoundException | SQLException | IOException ex) {
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

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MemberMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButtonChangePasswordActionPerformed

    private void jButtonCancelRequest1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelRequest1ActionPerformed

        int row = tblMemebers.getSelectedRow();
        if (row >= 0) {
            String UID = tblMemebers.getValueAt(row, 0).toString();
            String maxBookCount = JOptionPane.showInputDialog(rootPane, "Please input daily fine for late submission ",
                    "Calculate Fine!", JOptionPane.INFORMATION_MESSAGE);
            if (Integer.parseInt(maxBookCount) > 0 && Validation.isNumbers(maxBookCount)) {
                try {
                    if (UserController.updateMaxBookCount(UID, maxBookCount)) {
                        JOptionPane.showMessageDialog(rootPane, "Successfully Updated!",
                                "Success!", JOptionPane.INFORMATION_MESSAGE);
                        loadMembersTable();
                    }
                } catch (ClassNotFoundException | SQLException | IOException ex) {
                    Logger.getLogger(LibrarianMain.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "Please Enter Valid Number!",
                        "Error!", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select record to update!",
                    "Warnning!", JOptionPane.WARNING_MESSAGE);
        }

    }//GEN-LAST:event_jButtonCancelRequest1ActionPerformed

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
            java.util.logging.Logger.getLogger(LibrarianMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new LibrarianMain(USER, UID).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddBook;
    private javax.swing.JButton btnApproveBookFeedback;
    private javax.swing.JButton btnBookFeedBack;
    private javax.swing.JButton btnBorrowedHistory;
    private javax.swing.JButton btnDeleteBook;
    private javax.swing.JButton btnDeleteBookFeedback;
    private javax.swing.JButton btnEditBook;
    private javax.swing.JButton btnHistoryDelete;
    private javax.swing.JButton btnHistoryEdit;
    private javax.swing.JButton btnHistoryUpdate;
    private javax.swing.JButton btnIssueBook;
    private javax.swing.JButton btnIssueBookTab;
    private javax.swing.JButton btnManageBooks;
    private javax.swing.JButton btnMarkRecieved;
    private javax.swing.JButton btnMembersTab;
    private javax.swing.JButton btnUpdateBook;
    private javax.swing.JCheckBox chkPendingList;
    private javax.swing.JButton jButtonCancelRequest1;
    private javax.swing.JButton jButtonChangePassword;
    private javax.swing.JButton jButtonConfirmReservation1;
    private javax.swing.JButton jButtonLogout;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonSettings;
    private javax.swing.JDesktopPane jDesktopPaneLibraryMain;
    private javax.swing.JLabel jLabelLogo;
    private javax.swing.JLabel jLabelPicture;
    private javax.swing.JLabel jLabelUserEmail;
    private javax.swing.JLabel jLabelWelcomeUser;
    private javax.swing.JLabel jLableTime;
    private javax.swing.JPanel jPanelAddBook;
    private javax.swing.JPanel jPanelAddBook1;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelEditProfile;
    private javax.swing.JPanel jPanelFeedback1;
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JPanel jPanelHistory;
    private javax.swing.JPanel jPanelPending;
    private javax.swing.JPanel jPanelProfilepic;
    private javax.swing.JPanel jPanelReserveBookTab;
    private javax.swing.JPanel jPanelReserveBookTab1;
    private javax.swing.JPanel jPanelSearchBookTab;
    private javax.swing.JPanel jPanelSearchBookTab1;
    private javax.swing.JPanel jPanelSearchBookTab2;
    private javax.swing.JPanel jPanelSettings;
    private javax.swing.JPanel jPanelSubmitFeedback;
    private javax.swing.JPanel jPanelToggle;
    private javax.swing.JPasswordField jPasswordFieldConfirmPw;
    private javax.swing.JPasswordField jPasswordFieldCurrentPw;
    private javax.swing.JPasswordField jPasswordFieldNewPw;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPanePendingReturn;
    private javax.swing.JTabbedPane jTabbedPanelFeedback;
    private javax.swing.JTabbedPane jTabbedPanelSettings;
    private javax.swing.JTextArea jTextAreaDescription1;
    private javax.swing.JTextField jTextFieldAuthor1;
    private javax.swing.JTextField jTextFieldAvailability1;
    private javax.swing.JTextField jTextFieldBookId1;
    private javax.swing.JTextField jTextFieldContact;
    private javax.swing.JTextField jTextFieldDOB;
    private javax.swing.JTextField jTextFieldExpiryDate;
    private javax.swing.JTextField jTextFieldFullName;
    private javax.swing.JTextField jTextFieldMembership;
    private javax.swing.JTextField jTextFieldNIC;
    private javax.swing.JTextField jTextFieldRegDate;
    private javax.swing.JTextField jTextFieldReserveBookName1;
    private javax.swing.JTextField jTextFieldReserveISBN1;
    private javax.swing.JPanel pnlBookFeedBacks;
    private javax.swing.JPanel pnlBorrowedHistory;
    private javax.swing.JPanel pnlIssueBook;
    private javax.swing.JPanel pnlManageBook;
    private javax.swing.JPanel pnlMembers;
    private javax.swing.JTabbedPane tbBorrowedHistory;
    private javax.swing.JTabbedPane tbIssueBook;
    private javax.swing.JTable tblBookData;
    private javax.swing.JTable tblBorrowedBookHistory;
    private javax.swing.JTable tblFeedbacks;
    private javax.swing.JTable tblManageBook;
    private javax.swing.JTable tblMemebers;
    private javax.swing.JTable tblReservedBookList;
    private javax.swing.JTabbedPane tbpnlManageBook;
    private javax.swing.JTextField tfAddBookAuthorName;
    private javax.swing.JTextField tfAddBookBID;
    private javax.swing.JTextField tfAddBookBookName;
    private javax.swing.JTextArea tfAddBookDescription;
    private javax.swing.JTextField tfAddBookISBN;
    private javax.swing.JTextField tfAddBookPrice;
    private javax.swing.JTextField tfAddBookQty;
    private javax.swing.JTextField tfAddBookVersion;
    private javax.swing.JTextField tfBorrowedSearch;
    private javax.swing.JTextField tfEditBookAuthor;
    private javax.swing.JTextField tfEditBookBID;
    private javax.swing.JTextArea tfEditBookDescription;
    private javax.swing.JTextField tfEditBookISBN;
    private javax.swing.JTextField tfEditBookName;
    private javax.swing.JTextField tfEditBookPrice;
    private javax.swing.JTextField tfEditBookQuantity;
    private javax.swing.JTextField tfEditBookVersion;
    private javax.swing.JTextField tfFeedbackSearch;
    private javax.swing.JTextField tfHistoryAuthor;
    private javax.swing.JTextField tfHistoryBBID;
    private javax.swing.JTextField tfHistoryBID;
    private javax.swing.JTextField tfHistoryBookName;
    private javax.swing.JTextArea tfHistoryDescription;
    private javax.swing.JTextField tfHistoryISBN;
    private com.toedter.calendar.JDateChooser tfHistoryRenewDate;
    private javax.swing.JTextField tfHistoryUID;
    private javax.swing.JTextField tfIssueBBID;
    private javax.swing.JTextField tfIssueBookAuthor;
    private javax.swing.JTextField tfIssueBookAvailability;
    private javax.swing.JTextField tfIssueBookBID;
    private javax.swing.JTextField tfIssueBookBookName;
    private javax.swing.JTextArea tfIssueBookDescription;
    private javax.swing.JTextField tfIssueBookISBN;
    private com.toedter.calendar.JDateChooser tfIssueBookRenewDate;
    private javax.swing.JTextField tfIssueBookRequestedID;
    private javax.swing.JTextField tfIssueBookUID;
    private javax.swing.JTextField tfManageBookSearch;
    private javax.swing.JTextField tfMembersSearch;
    private javax.swing.JTextField tfRsvBookISBNSearch;
    private javax.swing.JTextField tfRsvBookSearch;
    // End of variables declaration//GEN-END:variables
}
