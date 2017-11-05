/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.BookFeedbackModel;
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
public class FeedbackControllerTest {
    
    public FeedbackControllerTest() {
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
     * Test of addFeedback method, of class FeedbackController.
     */
    @Test
    public void testAddFeedback() throws Exception {
        System.out.println("addFeedback");
        BookFeedbackModel bookfeedbackModel = new BookFeedbackModel("F100", "B001", "Test feedback", "Sameera Madushan");
        boolean expResult = true;
        boolean result = FeedbackController.addFeedback(bookfeedbackModel);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of autoGenerateFeedbackID method, of class FeedbackController.
     */
    @Test
    public void testAutoGenerateFeedbackID() throws Exception {
        System.out.println("autoGenerateFeedbackID");
        String expResult = "F101";
        String result = FeedbackController.autoGenerateFeedbackID();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    
}
