/*
 * GeneralTest.java
 * JUnit based test
 *
 * Created on March 1, 2006, 9:55 AM
 */

package Wattos.Utils;

import junit.framework.*;

/**
 *
 * @author jurgen
 */
public class GeneralTest extends TestCase {
    
    public GeneralTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(GeneralTest.class);        
        return suite;
    }



    /**
     * Test of showMemoryUsed method, of class Wattos.Utils.General.
     */
    public void testShowMemoryUsed() {
        //System.out.println("showMemoryUsed");
        //General.verbosity = General.verbosityNothing;
        General.showMemoryUsed();
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    } 
}
