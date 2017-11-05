/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.WarnedListModel;
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
public class WarnedListControllerTest {
    
    public WarnedListControllerTest() {
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
     * Test of autoGenerateWarnedListID method, of class WarnedListController.
     */
    @Test
    public void testAutoGenerateWarnedListID() throws Exception {
        System.out.println("autoGenerateWarnedListID");
        String expResult = "W004";
        String result = WarnedListController.autoGenerateWarnedListID();
        assertEquals(expResult, result);
      
    }

    /**
     * Test of addToWarnedList method, of class WarnedListController.
     */
    @Test
    public void testAddToWarnedList() throws Exception {
        System.out.println("addToWarnedList");
        WarnedListModel warn = new WarnedListModel("W004", "U001", "Chamindu Thiranjaya", "Late Submission", "Librarian Warning");
        boolean expResult = true;
        boolean result = WarnedListController.addToWarnedList(warn);
        assertEquals(expResult, result);
      
    }
    
}
