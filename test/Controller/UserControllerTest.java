/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.UserModel;
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
public class UserControllerTest {
    
    public UserControllerTest() {
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
     * Test of checkPasswordforReset method, of class UserController.
     */
    @Test
    public void testCheckPasswordforReset() throws Exception {
        System.out.println("checkPasswordforReset");
        UserModel userModel = new UserModel();
        userModel.setUid("U003");
        userModel.setPassword("123123a");
        
        
        boolean expResult = true;
        boolean result = UserController.checkPasswordforReset(userModel);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of updatePassword method, of class UserController.
     */
    @Test
    public void testUpdatePassword() throws Exception {
        System.out.println("updatePassword");
        String uID = "U003";
        String password = "000";
        boolean expResult = true;
        boolean result = UserController.updatePassword(uID, password);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }


    /**
     * Test of updateUser method, of class UserController.
     */
    @Test
    public void testUpdateUser() throws Exception {
        System.out.println("updateUser");
        String uID = "U003";
        String contact = "9876543210";
        boolean expResult = true;
        boolean result = UserController.updateUser(uID, contact);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    
}
