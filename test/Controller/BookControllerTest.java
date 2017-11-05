/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.BookModel;
import Model.BorrowedBookModel;
import java.sql.ResultSet;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Y`inza
 */
public class BookControllerTest {

    public BookControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addBook method, of class BookController.
     * @throws java.lang.Exception
     */
    @Test
    public void testAddBook() throws Exception {
        System.out.println("addBook");
        BookModel bModel = new BookModel("B001", "Book Title", "Author", "100", "0112603324", "Description", "Yes", "V1", "10");
        boolean expResult = true;
        boolean result = BookController.addBook(bModel);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
       
    }
    

    /**
     * Test of searchBookByBID method, of class BookController.
     * @throws java.lang.Exception
     */
    @Test
    public void testSearchBookByBID() throws Exception {
        System.out.println("searchBookByBID");
        String BID = "";
        BookModel expResult = null;
        BookModel result = BookController.searchBookByBID(BID);
        assertEquals(expResult, result);
       
    }
    /**
     * Test of deleteBookByISBN method, of class BookController.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testDeleteBookByISBN() throws Exception {
        System.out.println("deleteBookByISBN");
        String ISBN = "0112603324";
        boolean expResult = true;
        boolean result = BookController.deleteBookByISBN(ISBN);
        assertEquals(expResult, result);
        
    }
    /**
     * Test of deleteBorrowedByBBID method, of class BookController.
     */
    @Test
    public void testDeleteBorrowedByBBID() throws Exception {
        System.out.println("deleteBorrowedByBBID");
        String BBID = "B021";
        String BID = "B001";
        String UID = "U001";
        boolean expResult = true;
        boolean result = BookController.deleteBorrowedByBBID(BBID, BID, UID);
        assertEquals(expResult, result);
       
       
    }
    /**
     * Test of updateBookByISBN method, of class BookController.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testUpdateBookByISBN() throws Exception {
        System.out.println("updateBookByISBN");
        BookModel bModel = new BookModel("B001", "Book Title", "Author", "100", "0112603324", "New Description", "Yes", "V1", "10");
        boolean expResult = true;
        boolean result = BookController.updateBookByISBN(bModel);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of autoGenerateBookID method, of class BookController.
     */
    @Test
    public void testAutoGenerateBookID() throws Exception {
        System.out.println("autoGenerateBookID");
        String expResult = "B112";
        String result = BookController.autoGenerateBookID();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
       
    }
//
    /**
     * Test of loadTable method, of class BookController.
     */
    @Test
    public void testLoadTable() throws Exception {
        System.out.println("loadTable");
        String Query = "SELECT * FROM NOTABLE";
        ResultSet expResult = null;
        ResultSet result = BookController.loadTable(Query);
        assertEquals(expResult, result);
      
        
    }

    /**
     * Test of autoGenerateBorrowedID method, of class BookController.
     */
    @Test
    public void testAutoGenerateBorrowedID() throws Exception {
        System.out.println("autoGenerateBorrowedID");
        String expResult = "B024";
        String result = BookController.autoGenerateBorrowedID();
        assertEquals(expResult, result);
        
    }

    /**
     * Test of reserveBook method, of class BookController.
     */
    @Test
    public void testReserveBook() throws Exception {
        System.out.println("reserveBook");
        Date today = new Date();
        BorrowedBookModel bModel = new BorrowedBookModel("B024", "U001", "B001", null, today);
        boolean isReserved = false;
        String expResult = "Successfully added";
        String result = BookController.issueBook(bModel, isReserved);
        assertEquals(expResult, result);
      
    }

    /**
     * Test of deleteBookFeedbacksByFID method, of class BookController.
     */
    @Test
    public void testDeleteBookFeedbacksByFID() throws Exception {
        System.out.println("deleteBookFeedbacksByFID");
        String FID = "F001";
        boolean expResult = true;
        boolean result = BookController.deleteBookFeedbacksByFID(FID);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of approveFeedBack method, of class BookController.
     * @throws java.lang.Exception
     */
    @Test
    public void testApproveFeedBack() throws Exception {
        System.out.println("approveFeedBack");
        String FID = "F001";
        boolean expResult = false;
        boolean result = BookController.approveBookFeedBack(FID);
        assertEquals(expResult, result);

    }

    /**
     * Test of updateBorrowedBook method, of class BookController.
     */
    @Test
    public void testUpdateBorrowedBook() throws Exception {
        Date today = new Date();
        System.out.println("updateBorrowedBook");
        String BBID = "B001";
        Date renewDate = today;
        int statusFlag = 0;
        boolean expResult = false;
        boolean result = BookController.updateBorrowedBook(BBID, renewDate, statusFlag);
        assertEquals(expResult, result);
        
    }

}
