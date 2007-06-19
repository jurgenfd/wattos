/*
 * PseudoLibTest.java
 * JUnit based test
 *
 * Created on January 10, 2006, 11:18 AM
 */

package Wattos.Soup;

import junit.framework.*;
import Wattos.Utils.*;
import Wattos.Database.*;

/**
 *
 * @author jurgen
 */
public class PseudoLibTest extends TestCase {
    
    public PseudoLibTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(PseudoLibTest.class);
        
        return suite;
    }

    public void testInit() {
        //General.verbosity = General.verbosityNothing;
        //General.verbosity = General.verbosityDebug;

        PseudoLib instance = new PseudoLib();
        
        if (! instance.readStarFile( null )) {
            General.showError(" in PseudoLib.main found:");
            General.showError(" reading PseudoLib star file.");
            System.exit(1);
        }        
        General.showDebug(" read PseudoLib star file.");

        if ( true ) {            
            General.showDebug("Found -1- : " + instance.fromAtoms.get("PHE", "HD1"));
            General.showDebug("Found:-2- : " + instance.toAtoms.get("PHE", "QD"));
            General.showDebug("Found:-3- : " + instance.pseudoAtomType.get("PHE", "QD"));
        }

        if ( true ) {
            String result = null;
            //General.showOutput("Found        -4- : " + instance.fromAtoms.get("VAL", "HG11"));
            //General.showOutput("Found common -5- : " + instance.getCommonPseudoParent("VAL", "VAL", "HG11", "HG13"));
            result = instance.getCommonPseudoParent("VAL", new String[] {"HG12", "HG13"}, true);
            General.showDebug("Found common -6a- : " + result);
            assertEquals(result, Defs.EMPTY_STRING);

            result = instance.getCommonPseudoParent("VAL", new String[] {"HG12", "HG13"}, false);
            General.showDebug("Found common -6b- : " + result);
            assertEquals(result, "MG1");

            result = instance.getCommonPseudoParent("VAL", new String[] {"HG21", "HG12", "HG13"}, true);
            General.showDebug("Found common -7a- : " + result);
            assertEquals(result, Defs.EMPTY_STRING);

            result = instance.getCommonPseudoParent("VAL", new String[] {"HG21", "HG12", "HG13"}, false);
            General.showDebug("Found common -7b- : " + result);
            assertEquals(result, "QG");

            result = instance.getCommonPseudoParent("VAL", new String[] {"HG11", "HG12", "HG13"}, true);
            General.showDebug("Found common -7c- : " + result);
            assertEquals(result, "MG1");

            boolean test = instance.hasSibling("VAL", "HG11" );
            General.showDebug("Found sibling -7d- : " + test);
            assertEquals(test, true);
        }
        
        if ( true ) {
            String sibling = instance.getStereoSibling("VAL", "HG11", "QG" );
            General.showDebug("Found stereo sibling: " + sibling);
            assertEquals(sibling,"HG21");
            // Hide the warning:
            int v = General.verbosity;
            General.verbosity = General.verbosityError;
            sibling = instance.getStereoSibling("VAL", "HG11", "MG1" );
            General.verbosity = v;
            General.showDebug("Found stereo sibling: " + sibling);
            assertEquals(sibling,null);
            sibling = instance.getStereoSibling("GLY", "HA2", "QA" );
            General.showDebug("Found stereo sibling: " + sibling);
            assertEquals(sibling,"HA3");
        }
        General.showDebug("Done with PseudoLib parsing.");
    }    
}
