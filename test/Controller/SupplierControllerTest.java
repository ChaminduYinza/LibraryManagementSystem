/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.SupplierModel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Rushan
 */
public class SupplierControllerTest {
    
    public SupplierControllerTest() {
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
     * Test of addSupplier method, of class SupplierController.
     */
    @Test
    public void testAddSupplier() throws Exception {
        System.out.println("addSupplier");
        SupplierModel supplier = null;
        boolean expResult = false;
        boolean result = SupplierController.addSupplier(supplier);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getSupplierBySID method, of class SupplierController.
     */
    @Test
    public void testGetSupplierBySID() throws Exception {
        System.out.println("getSupplierBySID");
        String SID = "";
        SupplierModel expResult = null;
        SupplierModel result = SupplierController.getSupplierBySID(SID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteSupplier method, of class SupplierController.
     */
    @Test
    public void testDeleteSupplier() throws Exception {
        System.out.println("deleteSupplier");
        String SID = "";
        boolean expResult = false;
        boolean result = SupplierController.deleteSupplier(SID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of editSupplier method, of class SupplierController.
     */
    @Test
    public void testEditSupplier() throws Exception {
        System.out.println("editSupplier");
        SupplierModel Supplier = null;
        boolean expResult = false;
        boolean result = SupplierController.editSupplier(Supplier);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of autoGenerateSupplierID method, of class SupplierController.
     */
    @Test
    public void testAutoGenerateSupplierID() throws Exception {
        System.out.println("autoGenerateSupplierID");
        String expResult = "";
        String result = SupplierController.autoGenerateSupplierID();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
    
}
