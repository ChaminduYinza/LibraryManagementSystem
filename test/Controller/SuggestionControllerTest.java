/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.SuggestionsModel;
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
public class SuggestionControllerTest {

    public SuggestionControllerTest() {
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
     * Test of addSuggestion method, of class SuggestionController.
     */
    @Test
    public void testAddSuggestion() throws Exception {
        System.out.println("addSuggestion");
        SuggestionsModel suggestionsModel = new SuggestionsModel("S100", "U001", "TestSuggestion");
        boolean expResult = true;
        boolean result = SuggestionController.addSuggestion(suggestionsModel);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of autoGenerateSuggestionID method, of class SuggestionController.
     */
    @Test
    public void testAutoGenerateSuggestionID() throws Exception {
        System.out.println("autoGenerateSuggestionID");
        String expResult = "S101";
        String result = SuggestionController.autoGenerateSuggestionID();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

}
