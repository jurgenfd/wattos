package Wattos.Star.NMRStar;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import Wattos.Database.DBMS;
import Wattos.Star.StarFileReader;
import Wattos.Star.StarNode;
import Wattos.Utils.General;

public class ValidatorDictionaryTest extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite(ValidatorDictionaryTest.class);        
        return suite;
    }

    
    public void testReadFile() {
        General.setVerbosityToDebug();
        ValidatorDictionary vd = new ValidatorDictionary();
        vd.readFile(null);
        General.setVerbosityToDebug();
        String buf = "data_test "+
        
        "save_testing  "+
        "_Study_list.Sf_category  study_list " +
        "loop_ _Study.Name yyy stop_ "+
        "save_ "+
        
        "save_non_existing  "+
        "_Mytag.part2B  non_existing " +
        "_Mytag.part2A  non_existing " +
        "loop_ _Mytag.loop_B _Mytag.loop_A 1 2 3 4 stop_ "+
        "save_ "+

        "save_entry_information  "+
        "_Entry.Sf_category  entry_information " +
        "loop_ _Related_entries.Database_name PDB stop_ "+
        "save_ "+
        
        "save_MR_file_comment_1 "+
        "_Org_constr_file_comment.Sf_category         org_constr_file_comment "+
        "_Org_constr_file_comment.Constraint_file_ID  1 "+
        "_Org_constr_file_comment.Block_ID            1 "+
        "_Org_constr_file_comment.Details             'Generated by Wattos' "+
        "_Org_constr_file_comment.Comment             bla "+
        "_Org_constr_file_comment.Entry_ID            parsed_2hgh "+
        "_Org_constr_file_comment.ID                  1 "+
        "save_ ";
                     ;
        DBMS dbms = new DBMS();
        StarFileReader sfr = new StarFileReader(dbms); 
        StarNode sn = sfr.parse( buf );
        assertTrue(sn!=null);                             
        assertTrue(vd.sortTagNames(sn));
        General.showDebug("After sortTagNames: "+sn.toSTAR());
        
    }

}
