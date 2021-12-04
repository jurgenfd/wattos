/*
 * GeneralTest.java
 * JUnit based test
 * Run like: java junit.textui.TestRunner test Wattos.Utils.GeneralTest
 *
 * Created on March 1, 2006, 9:55 AM
 */

package Wattos.Utils;

import junit.framework.*;

public class GeneralTest extends TestCase {

    public GeneralTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(GeneralTest.class);
        return suite;
    }

    public void testShowMemoryUsed() {
        // System.out.println("showMemoryUsed");
        General.verbosity = General.verbosityDebug;
        // General.showMemoryUsed();
        // General.showEnvironment();
        String startMsg = General.getStartMessage();
        this.assertTrue(startMsg.length() > 10);
        General.showOutput("start msg:\n" + startMsg);
        String revision = General.getRevision();
        General.showOutput("GeneralTest.java found: SVN revision: " + revision);
        this.assertTrue(revision.length() == 7);
    }
}
