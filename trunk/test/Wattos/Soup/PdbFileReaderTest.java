/*
 * PdbFileReaderTest.java
 * JUnit based test
 *
 * Created on November 30, 2005, 10:04 AM
 */

package Wattos.Soup;

import java.io.File;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import Wattos.CloneWars.UserInterface;
import Wattos.Utils.General;
import Wattos.Utils.InOut;

/**
 *
 * @author jurgen
 */
public class PdbFileReaderTest extends TestCase {

    public PdbFileReaderTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(PdbFileReaderTest.class);
        return suite;
    }
/*
    public void testInitConvenienceVariables() {
        PdbFile instance = null;

        boolean expResult = true;
        boolean result = instance.initConvenienceVariables();
        assertEquals(expResult, result);
    }

    public void testWriteFile() {
        String file_name = "";
        PdbFile instance = null;

        boolean expResult = true;
        boolean result = instance.writeFile(file_name);
        assertEquals(expResult, result);
    }
 */

    public void testReadFile() {
        //General.verbosity = General.verbosityNothing;
        //General.verbosity = General.verbosityDebug;

        String fileNameBase = "1olh_small";

        boolean doWrite = true; // don't use in junit testing as it's not necessarily installed.
//        String atomNomenclatureFlavor = "";
//        PdbFile instance = null;

        UserInterface ui = UserInterface.init(true);
//        DBMS dbms = ui.dbms;
        Gumbo gumbo = ui.gumbo;
        Entry entry = gumbo.entry;
        //PdbFile pdbFile = new PdbFile(dbms);
        long time = System.currentTimeMillis();
        String wattosRoot   = InOut.getEnvVar("WATTOSROOT");
        File inputDir       = new File( wattosRoot,"data"+File.separator+"test_data" );
//        File tmpDir         = new File( System.getProperty("java.io.tmpdir"));
        String urlName = inputDir + File.separator + fileNameBase + ".pdb.gz";
        URL url = InOut.getUrlFileFromName( urlName );
        File outputDir      = new File( wattosRoot,"tmp_dir" );
        String outputfileName = new File(outputDir,fileNameBase+"_wattos.pdb").toString();
        boolean status = entry.readPdbFormattedFile( url, AtomMap.NOMENCLATURE_ID_PDB );
        time = System.currentTimeMillis() - time;
        if ( ! status ) {
            fail("Failed to read the PDB formatted file");
        }
        General.showDebug("Time for reading: " + time);
        if ( doWrite ) {
            //General.showDebug( dbms.toString(true));
            time = System.currentTimeMillis();
            Boolean generateStarFileToo = new Boolean(true);
            General.showDebug("Writing file: " + outputfileName);
            status = entry.writePdbFormattedFileSet( outputfileName, generateStarFileToo, ui );
            time = System.currentTimeMillis() - time;
            if ( ! status ) {
                fail("Failed to write the PDB formatted file");
            }
            General.showDebug("Time for writing: " + time);
        }
        // delete it again
//        entry.mainRelation.removeRowCascading(0, true);
        // next file to test with is AMBER
//        urlName = inputDir + File.separator + "1bau_amber.pdb.gz";
//        url = InOut.getUrlFileFromName( urlName );
//        assertEquals(true, entry.readPdbFormattedFile( url, AtomMap.NOMENCLATURE_ID_PDB ));

        int chainCount = gumbo.entry.getMolInMasterModel(false).cardinality();
        assertEquals(4,chainCount);
        int seqL = gumbo.entry.getResInMasterModel(false).cardinality() / chainCount; // only exact for homo multimers
        assertEquals(42,seqL); // The answer to all...
        int allCount = chainCount*seqL;


        General.setVerbosityToDebug();
        assertTrue( gumbo.res.selectResiduesByRangesExp(""));
        assertEquals(allCount, gumbo.res.selected.cardinality());
        assertTrue( gumbo.res.selectResiduesByRangesExp("A.1"));
        assertEquals(1, gumbo.res.selected.cardinality());
        assertTrue( gumbo.res.selectResiduesByRangesExp("A.1,B.1"));
        assertEquals(2, gumbo.res.selected.cardinality());
        assertTrue( gumbo.res.selectResiduesByRangesExp("A.1-2"));
        assertEquals(2, gumbo.res.selected.cardinality());
        assertTrue( gumbo.res.selectResiduesByRangesExp("1"));
        assertEquals(chainCount, gumbo.res.selected.cardinality());
        assertTrue( gumbo.res.selectResiduesByRangesExp("all"));
        assertEquals(allCount, gumbo.res.selected.cardinality());
        assertTrue( gumbo.res.selectResiduesByRangesExp(Gumbo.DEFAULT_ATTRIBUTE_ALL_RANGES_STR));
        assertEquals(allCount, gumbo.res.selected.cardinality());
        assertTrue( gumbo.res.selectResiduesByRangesExp(Gumbo.DEFAULT_ATTRIBUTE_EMPTY_RANGES_STR));
        assertEquals(allCount, gumbo.res.selected.cardinality());
        assertTrue( gumbo.res.selectResiduesByRangesExp(Gumbo.DEFAULT_ATTRIBUTE_AUTO_RANGES_STR)); // for now
        assertEquals(allCount, gumbo.res.selected.cardinality());
//      # Residues in negative crossing ranges
        assertTrue( gumbo.res.selectResiduesByRangesExp("-1-2" ));
        assertEquals(2*chainCount, gumbo.res.selected.cardinality());
        assertTrue( gumbo.res.selectResiduesByRangesExp("A.-1-2" ));
        assertEquals(2, gumbo.res.selected.cardinality());
        assertTrue( gumbo.res.selectResiduesByRangesExp("-10--2" ));
        assertEquals(0, gumbo.res.selected.cardinality());
        assertTrue( gumbo.res.selectResiduesByRangesExp("A.-10--2" ));
        assertEquals(0, gumbo.res.selected.cardinality());
        int verbositySaved = General.verbosity;
        General.verbosity = General.verbosityNothing;
        assertFalse( gumbo.res.selectResiduesByRangesExp("AB.1" )); // False
        General.verbosity = verbositySaved;
        assertEquals(allCount, gumbo.res.selected.cardinality());
    }


    public void testTranslateAtomNameToPdb() {
        String inputName = "HD2'";
        String expResult = "'HD2";
        String result = PdbFile.translateAtomNameToPdb(inputName);
        assertEquals(expResult, result);
    }


    public void testTranslateAtomNameFromPdb() {
        String atomName = "'H5'";
        char[] buf = atomName.toCharArray();
        PdbFile.translateAtomNameFromPdb( buf, 0 );
        String atomNameNew = new String( buf );

        if ( ! atomNameNew.equals("H5''")) {
            General.showDebug("Name before translateAtomName: [" + atomName + "]");
            General.showDebug("Name after  translateAtomName: [" + atomNameNew + "]");
            fail( "Expected [H5''] but got: [" + atomNameNew + "]");
        }
    }

    /*
    public void testTranslateResidueNameFromPDB() {
        char[] buf = null;
        int startIdx = 0;

        PdbFile.translateResidueNameFromPDB(buf, startIdx);
    }

    public void testTranslateAtomCharge() {
        char[] buf = null;
        int startIdx = 0;

        float expResult = 0.0F;
        float result = PdbFile.translateAtomCharge(buf, startIdx);
        assertEquals(expResult, result);
    }
     */
}
