/*
 * StringsTest.java
 * JUnit based test
 *
 * Created on January 19, 2006, 1:50 PM
 */

package Wattos.Utils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author jurgen
 */
public class Strings2Test extends TestCase {

    public Strings2Test(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(Strings2Test.class);

        return suite;
    }

    /**
    Possible 5 situations:
    a      # 1 # positive int
    -a     # 2 # single int
    -a-b   # 3 #
    -a--b  # 4 #
    a-b    # 5 # most common
     */
    public void testAsci2list() {
        General.setVerbosityToDebug();
        String[] inputList = new String[] {
                    "1",
                    "1-3",
                    "-3:1",
                    "-2--1",
                    "-2-1",
                    "-3",
                    "1,2,5-8,11,20-22",
                    "-20:-19,-2:-1,3:4"};
        String[] resultLoL = new String[] {
                        "[1]",
                        "[1;2;3]",
             "[-3;-2;-1;0;1]",
                "[-2;-1]",
                "[-2;-1;0;1]",
             "[-3]",
                        "[1;2;5;6;7;8;11;20;21;22]",
            "[-20;-19;-2;-1;3;4]"};

        for (int i=0; i<inputList.length;i++) {
            String inputStr = inputList[i];
//            General.showDebug("testAsci2list: " + i + " for input: " + inputStr);
            int[] result = Strings.asci2list(inputStr);
            String actual = PrimitiveArray.toString(result, true);
//            General.showDebug("actual: " + actual);
            String expected = resultLoL[i];
            assertEquals(expected, actual);
        }
        int saveVerbosity = General.verbosity;
        General.verbosity = General.verbosityNothing;
        int[] result = Strings.asci2list("1--2"); // will cause an error message and an empty return list.
        General.verbosity = saveVerbosity;
        assertEquals(0,result.length);
    }
}
