/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.RequestedBookListModel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sameera
 */
public class RequestedBookControllerTest {
    
    public RequestedBookControllerTest() {
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
     * Test of addBookRequest method, of class RequestedBookController.
     */
    @Test
    public void testAddBookRequest() throws Exception {
        System.out.println("addBookRequest");
        RequestedBookListModel bRequestModel = new RequestedBookListModel("R100", "U001", "B001", "TestBook");
        boolean expResult = true;
        boolean result = RequestedBookController.addBookRequest(bRequestModel);
        assertEquals(expResult, result);
//         TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of autoGenerateRequestedBookID method, of class RequestedBookController.
     */
    @Test
    public void testAutoGenerateRequestedBookID() throws Exception {
        System.out.println("autoGenerateRequestedBookID");
        String expResult = "R101";
        String result = RequestedBookController.autoGenerateRequestedBookID();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of cancelBookRequest method, of class RequestedBookController.
     */
    @Test
    public void testCancelBookRequest() throws Exception {
        System.out.println("cancelBookRequest");
        RequestedBookListModel requestedBookList = new RequestedBookListModel("R100", "U001", "B001", "TestBook");
        boolean expResult = true;
        boolean result = RequestedBookController.cancelBookRequest(requestedBookList);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
    
}
