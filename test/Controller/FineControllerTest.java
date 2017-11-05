/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.FineModel;
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
public class FineControllerTest {
    
    public FineControllerTest() {
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
     * Test of addToFine method, of class FineController.
     */
    @Test
    public void testAddToFine() throws Exception {
        System.out.println("addToFine");
        FineModel fine = new FineModel("F110", "U001", "Late Submission", 0, 250);
        boolean expResult = true;
        boolean result = FineController.addToFine(fine);
        assertEquals(expResult, result);

    }

    /**
     * Test of autoGenerateFineID method, of class FineController.
     */
    @Test
    public void testAutoGenerateFineID() throws Exception {
        System.out.println("autoGenerateFineID");
        String expResult = "F111";
        String result = FineController.autoGenerateFineID();
        assertEquals(expResult, result);
        
    }
    
}
