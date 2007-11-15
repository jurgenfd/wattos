/*
 * Globals.java
 *
 * Created on November 27, 2001, 6:05 PM
 *
 *This software is copyright (c) 2002 Board of Regents, University of Wisconsin.
 *All Rights Reserved. No warranty implied or expressed.
 */

package Wattos.Episode_II;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import Wattos.Utils.General;
import Wattos.Utils.InOut;

import com.braju.format.Format;
import com.braju.format.Parameters;

/** Holds the global settings like directory locations and preferred text editors.
 * The nice thing is that most things can be modified after instantiating.
 *
 * @author Jurgen F. Doreleijers
 * @version 0.1
 */
public class Globals {       
    /** Main container of data of this class.
     */
    public HashMap m = new HashMap();
    /** Some special values for these variable types. Use them with caution.
     *In some cases it might make sense to have a standard for the boolean
     *value to denote an invalid value; rarely though
     */
    public static boolean   INVALID_BOOLEAN   = false;
    /**The unlikely value to be used as an indication of invalidity*/
    public static int       INVALID_INT       = -9999999;
    /**The unlikely value to be used as an indication of invalidity*/
    public static String    INVALID_STRING    = "INVALID_STRING_VALUE";

    /** Exception to the rule that the settings are instance rather than class based.
     *This variable will try to override some parameters otherwise defaulting to java.io.tmpdir.
    public static String    TMPDIR            = null;
     */

    private String fs = File.separator;        

    /** Show the content of the hash map m
     */
    public void showMap() {
        General.showOutput( getMap() );
    }
    
    public String getMap() {
        Parameters p = new Parameters(); // for printf
        Object key;
        String value;
        int size_print_max = 100;
        
        List keys = new ArrayList(Arrays.asList( m.keySet().toArray() ));
        Collections.sort(keys);
        
        StringBuffer sb = new StringBuffer();
        sb.append( Format.sprintf( "There are %d options set:\n", p.add( keys.size() )) );
        Iterator i = keys.iterator();
        while ( i.hasNext() ) {            
            key = i.next();
            value = m.get( key ).toString();
            // Don't show more than 1 line
            value = Wattos.Utils.Strings.removeEols(value);
            // Truncate values longer than specified size.
            if ( value.length() > size_print_max ) {
                value = value.substring(0,size_print_max-3)+"...";
            }
            sb.append( Format.sprintf( "%-30s : %-s\n", 
                p.add( key ).add( value )) );
        }
        return sb.toString();
    }
    
    /** Loads the global variables */
    public void loadMap() {
       
        /** Use directories etc. that are not production but QA */
        m.put("testing", Boolean.valueOf( true )); 
        /** Use local servlet engine.  */
        m.put("act_locally", Boolean.valueOf( true ));
        /** Use the local database engine. */
        m.put("act_locally_db", Boolean.valueOf( true )); 
        /** Use the local mr files. */
        m.put("act_locally_mr", Boolean.valueOf( true )); 
 

        /** Initializing some variables
         *  Use unix notation as a standard for directories        */
        // Set the root
        String root         = "";

        /** These are set because on windows we need to go through drive letters */
//        String bmrb_root        = fs+"bmrb";
        String share_root       = fs+"share";
        Properties sp = InOut.getEnvVars();
        if ( sp == null ) {
            General.showDebug("Failed to get system environment settings; failing ensurePresenceUserDefs.");
// TODO:           return;
        }

        String wattosRootDirStr = sp.getProperty( "WATTOSROOT" );
//        General.showOutput("WATTOSROOT: " + wattosRootDirStr);
        
        //String dbfs_root        = fs+"mnt"+fs+"mrgrid";
        String dbfs_root        = fs+"big"+fs+"jurgen"+fs+"DB"+fs+"mrgrid";
//        String localTestingPlatform = "C:\\jurgen\\tmp_unb_";
        String localTestingPlatform = "/Users/jd/wattosTestingPlatform";
        //String pdbmirror_root   = fs+"pdbmirror2"; // different from "pdbmirr".
        String pdbmirror_root   = fs+"dumpzone"+fs+"pdb";
        
        String osName       = System.getProperty("os.name" );        
        if ( osName.startsWith("Windows") ) {
            root            = "I:";    //rootdir on 'Whelk' 
//            bmrb_root       = "K:";  //bmrb on 'Medusa'
            share_root      = "S:";    //share on 'Medusa'
            dbfs_root       = "M:"+fs+"jurgen"+fs+"DB"+fs+"mrgrid";  //big/jurgen/DB/mrgrid on 'tang'
            pdbmirror_root  = "P:"+fs+"pdb";  //dumpzone/pdb on 'clownfish'
        } else if ( osName.startsWith("Mac OS") ) {
            ; // default settings.
        } else if ( osName.equals("Linux") || osName.equals("SunOS")) {
            ; // default settings.
        } else {
            General.showOutput("Any Operating System that is not:");
            General.showOutput("- Windows NT, Windows 2000, Windows 98 or");
            General.showOutput("- Linux, Mac OS, or SunOS\n");
            General.showOutput("is currently not supported by Wattos.Episode_II.Globals.");
            General.showOutput("The OS of this machine is determined by Java as: " 
                + osName);
            return;
        }
        if ( ((Boolean) m.get( "act_locally_db" )).booleanValue() ) { 
            dbfs_root =  fs+"Users"+fs+"jd"+fs+"CloneWars"+fs+"DB"+fs+"mrgrid";
        }
        m.put( "dbfs_root", dbfs_root);

        // Favorite editor on Unix or Windows systems
        String jar_file_name = share_root+fs+"linux"+fs+"src"+fs+"jedit"+fs+"4.1"+fs+"jedit.jar";
        String java_binary_file_name = "java";
        if( ( (Boolean) m.get( "act_locally" )).booleanValue() ) {
//            jar_file_name = "\"C:\\Program Files\\jEdit4.3pre9\\jedit.jar\"";
//            jar_file_name = "/Users/bmrb/Desktop/jEdit.app/Contents/Resources/Java/jedit.jar";
            jar_file_name = "/Applications/jEdit.app/Contents/Resources/Java/jedit.jar";
//            java_binary_file_name = "/System/Library/Frameworks/JavaVM.framework/Versions/1.5.0/Home/bin/java";
        }
        // Note that the -noserver is not needed but watch out not to have any other Jedit applications/views open.
        String editorProgram = java_binary_file_name+" -Xmx96m -Xms24m -jar " + jar_file_name ;//+ " -noserver";
        m.put("editor", editorProgram );

 
        // Dir as base in which all info and scripts like this one resides
//        m.put("base_dir", share_root+fs+"jurgen"+fs+"BMRB"+fs+"MRAnalysis" );
        // Dir to publish results to (not including the html pages)
//        m.put("results_dir", root+fs+"usr2"+fs+"jurgen"+fs+m.get( "html_project_name" )+fs+"results" );
        // Dir with pdb restraints files
//        m.put("restraints_dir", root+fs+"usr2"+fs+"jurgen"+fs+m.get( "html_project_name" )+fs+"nmr_restraints" );
        // Directory with zipped pdb coordinate files
//        m.put("coordinates_dir", pdbmirror_root+fs+"pdb"+fs+"data"+fs+"structures"+divided+"all"+fs+"pdb" );
        // Directory with external amber pdb coordinate files
        m.put("amber_pdb_dir", pdbmirror_root+fs+"external"+fs+"amber_pdb" );
        
        // Directory with zipped unannotated mr files for annotation use within subdir structure.
        m.put("mr_dir", pdbmirror_root+fs+"pdb"+fs+"data"+fs+"structures"+fs+"divided"+fs+"nmr_restraints");
        // Directory with zipped pdb files within subdir structure.
        m.put("pdb_dir", pdbmirror_root+fs+"pdb"+fs+"data"+fs+"structures"+fs+"divided"+fs+"pdb");

        // Directory with annotated mr files, ready to be split
        // Used in MRInterloop and MRReclassify
        //m.put("mr_anno_dir", share_root+fs+"wattos"+fs+"mr_anno_test");
        m.put("mr_anno_dir", share_root+fs+"wattos"+fs+"mr_anno_backup");        
        if( ( (Boolean) m.get( "testing" )).booleanValue() ) {
              m.put("mr_anno_progress_dir", share_root+fs+"jurgen"+fs+"tmp_unb_"+fs+"Wattos"+fs+"mr_anno_progress");
        } else {
              m.put("mr_anno_progress_dir", pdbmirror_root+fs+"mr_anno_progress");
        }            

        if( ( (Boolean) m.get( "act_locally_mr" )).booleanValue() ) {
            m.put("mr_dir",               localTestingPlatform + fs + "pdb"+fs+"data"+fs+"structures"+fs+"divided"+fs+"nmr_restraints");
            m.put("mr_anno_dir",          localTestingPlatform + fs + "Wattos"+fs+"mr_anno_progress");
            m.put("mr_anno_progress_dir", localTestingPlatform + fs + "Wattos"+fs+"mr_anno_progress");
        }
        
        // Directory with data file 
//        m.put("data_dir", share_root+fs+"jurgen"+fs+"BMRB"+fs+"Wattos"+fs+"Episode_II"+fs+"Data");

        /** Base url for mr_analysis server
         *  should be base for both html and servlet locations
         */
//        m.put("base_html_dir", root+fs+"home"+fs+"jurgen"+fs+"external_html" );
        m.put("base_html_url", "http://restraintsgrid.bmrb.wisc.edu:8080" );

        // Dir/url with html pages
//        m.put("html_url", m.get( "base_html_url" ) + "/~jurgen/" +  m.get( "html_project_name" ) );

        /** Layout of directory structure is as follows:
         *<PRE>
         /bmrb/htdocs/wattos/                                                   {servlet_top_url}
         *  MRGridServlet                                                       {MRGridServlet}
         *      html (all html files)                                           {servlet_html_dir}    
         *      images (small giffies)                                          {servlet_image_dir}     // note singular
         *  molgrap (symlink to /pdbmirror/molgrap)                             {servlet_molgrap_dir}    OLD 
         *  pdb (symlink to /pdbmirror/pdb/data/structures/all/pdb)             {servlet_pdb_coordinate_dir} OLD
         *</PRE>
         */
                                  
        
        m.put("apache_data_url",                    "http://restraintsgrid.bmrb.wisc.edu/servlet_data");
        m.put("servlet_top_dir",                    "/bmrb/htdocs/wattos"); // Exists only on servlet machine
        m.put("MRGridServlet",                      "MRGridServlet");
        m.put("servlet_top_url",                    "WebModule");
        if ( getValueBoolean("act_locally" ) ) {
//            m.put("servlet_root_url",               "http://whelk.bmrb.wisc.edu:8080");
            m.put("servlet_root_url",               "http://localhost:8080");
        } else {
            m.put("servlet_root_url",               "http://restraintsgrid.bmrb.wisc.edu:8080");
        }
        m.put("servlet_mrgrid_url",                 getValueString("servlet_top_url") + "/" + getValueString("MRGridServlet"));
         
        m.put("servlet_wattos_dir",                 getValueString("servlet_top_url")           + "/wattos");
        m.put("servlet_servlet_dir",                getValueString("servlet_wattos_dir")        + "/"        + getValueString("MRGridServlet"));
        m.put("servlet_html_dir",                   getValueString("servlet_servlet_dir")       + "/html");
        m.put("servlet_image_dir",                  getValueString("servlet_servlet_dir")       + "/images"); // note it is plural
        
        m.put("servlet_top_absolute_url",           getValueString("servlet_root_url")          + "/" +        getValueString("servlet_top_url"));
        m.put("servlet_mrgrid_absolute_url",        getValueString("servlet_root_url")          + "/" +        getValueString("servlet_mrgrid_url"));
        m.put("servlet_html_absolute_url",          getValueString("servlet_root_url")          + "/" +        getValueString("servlet_html_dir"));
        m.put("servlet_image_absolute_url",         getValueString("servlet_root_url")          + "/" +        getValueString("servlet_image_dir"));

        m.put("servlet_molgrap_dir",                getValueString("apache_data_url")           + "/molgrap");
//        m.put("servlet_pdb_coordinate_dir",         getValueString("apache_data_url")           + "/pdb");
//        m.put("servlet_pdb_restraint_dir",          getValueString("apache_data_url")           + "/pdbmr");
         
        m.put("servlet_webmaster",                  "webmaster@bmrb.wisc.edu");        
        m.put("dbmatch",                            "http://www.bmrb.wisc.edu/cgi-bin/dbmatch.cgi?db=pdb&auto=yes&id=");
        m.put("bmrb_url",                           "http://www.bmrb.wisc.edu/cgi-bin/explore.cgi?bmrbId=");
        m.put("recoord_url",                        "http://www.ebi.ac.uk/msd-srv/msdlite/atlas/nmr");
        m.put("dress_url",                          "http://www2.cmbi.ru.nl/dress/index.spy?pdbid=");
        
        // Location for molgrap images
        m.put("molgrap_dir", root+fs+"pdbmirror2"+fs+"molgrap" );
        // Dir for temporary files (/tmp on tang is too small)
        
        /**
        if ( osName.startsWith("Windows") ) {
            m.put("tmp_dir", "C:" + fs + "temp" + fs + "wattos");
        } else {
            m.put("tmp_dir", "/opt/tmp/jurgen" );            
        }
        TMPDIR = getValueString("tmp_dir");         
         */
        // A default database tablespace name is chosen on the basis of the
        // user id in oracle
        // Make sure to use a different user id for each of the database to
        // run at the same time because the db name determines the space used
        // for the bfiles on the /mnt/mrgrid partition.
        
        m.put("db_port_number",     new Integer(3306));
        m.put("db_name",            getValueString("db_username"));
        m.put("db_driver",          "com.mysql.jdbc.Driver"); 
        m.put("db_conn_prefix",     "jdbc:mysql://");

        if ( getValueBoolean("testing" ) ) {  
            m.put("db_username",        "wattos2");
            m.put("db_password",        "4I4KMS");
            if ( getValueBoolean("act_locally_db" ) ) {
//                m.put("db_machine",         "whelk.bmrb.wisc.edu");
                m.put("db_machine",         "localhost");
            } else {
                m.put("db_machine",         "restraintsgrid.bmrb.wisc.edu");                    
            }
        } else {  // Production settings            
            m.put("db_username",        "wattos2");// 
            m.put("db_password",        "4I4KMU"); // was U for wattos2; S for 1.
            m.put("db_machine",         "restraintsgrid.bmrb.wisc.edu");                    
        }
        if ( ! setDbUserNameDerivedVariables()) {
            General.showError("Failed to setDbUserNameDerivedVariables in Globals");
            return;
        }

                
        // Don't forget to set the machine's display variable in the script
        // molgrap_db.csh
        // The dependency on the X windowing system should be easy to take out.
// No changes required below this line
// #############################################################################


        
        // This name should not be changed otherwise automatic look ups from server will fail
//        m.put("index_file", "index.html" );

        // List of entries with an mr file in directory specified
//        m.put("entry_list_all", new ArrayList());

        // List of 'new' entries for which hits were found
//        m.put("new_hits_entry_list", new ArrayList());
//        m.put("done_entry_list", new ArrayList());

//        m.put("mr_analysis", new HashMap()  );        
                
        /* Formatting of all pages derived from this header */
            /** No robots please. Current page is ok to index but not the links please. */

        m.put("html_header_text", 
            "<html><head>\n" +
            "<META NAME=\"ROBOTS\" CONTENT=\"NOFOLLOW\">" +
            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\n" +
            "<meta name=\"Author\" content=\"Jurgen F. Doreleijers\">\n" +
            "<title>NMR Restraints Grid</title></head>\n" +
            "<body text=\"#660000\" bgcolor=\"#FFFFCC\" link=\"#666633\" vlink=\"#999966\" alink=\"#999966\" REFRESH=\"10\">\n" +
            "&nbsp;\n" +
            "<table CELLSPACING=10 CELLPADDING=10 >\n" +
            "<tr>\n" +
            "<td><A HREF=\"http://www.bmrb.wisc.edu\"> <img SRC=\"" +
            "/" + m.get("servlet_image_dir") + "/" +
            "bmrb_logo_brown_fg_cream_bg.gif\"" +
            "TITLE=\"BMRB home\"" +
            "border=\"0\"" +
            "></A></td>\n" +
            "<td><!-- INSERT A TITLE HERE --></td>\n" +
            "<td><!-- INSERT AN IMAGE HERE --></td>\n" +
            "</table>" );

        // Formatting of all pages derived from this footer
        String html_location = getValueString("servlet_html_dir");
        m.put("html_footer_text", 
            "<p><hr>\n" +
            "Please acknowledge these <A HREF=\"/"+html_location+"/howto.html#References\">references</A>\n" +
            "in publications where the data from this site have been utilized.\n"+
            "<p>Contact the "+
            "<A HREF=\"mailto:" + m.get("servlet_webmaster") + "\">webmaster</a> "+
            "for help, if required. " +
            "<!-- INSERT DATE HERE --> \n" + 
            "(<!-- INSERT DB_USERNAME HERE -->)\n" +
            "</body></html>");              
    }
    
    /** Call whenever db user name changes */
    public boolean setDbUserNameDerivedVariables() {
        String db_username = getValueString("db_username");
        // Directory with database like file system.
        String dbfs_root = getValueString("dbfs_root");
        m.put("dbfs_dir", dbfs_root+fs+"bfiles"+fs+db_username);
        m.put("db_name",  db_username); // not actually used in this case.
        m.put("db_conn_string",     getValueString("db_conn_prefix") + getValueString("db_machine") +
                                    ":" + getValueString("db_port_number") + 
                                    "/" + getValueString("db_name"));
        return true;
    }
       
    /** Generic version of get returning an object.
     * @param key The key for the object to get.
     * @return The object reference or null if the key is invalid.
     */    
    public Object getValue( Object key ) {
        if( key != null ) {
            return m.get( key );
        }
        General.showError("in Globals.getValue found:"); 
        General.showError("key was not valid reference key for any object in hashmap");
        return null;
    }
    
    /** Returns a boolean value.
     * @param key The key for the object to get.
     * @return <CODE>true</CODE> if key exists and the value of the referenced object is
     * <CODE>true</CODE>.
     */    
    public boolean getValueBoolean( Object key ) {
        if( key != null ) {           
            return  ((Boolean) m.get( key )).booleanValue();
        }
        General.showError("in Globals.getValueBoolean found:");
        General.showError("key was not valid reference key for any object in hashmap");
        return INVALID_BOOLEAN;
    }

    /** Returns a int value.
     * @param key The key for the object to get.
     * @return The integer value referenced by key.
     */    
    public int getValueInt( Object key ) {
        if( key != null ) {           
            return  ((Integer) m.get( key )).intValue();
        }
        General.showError("in Globals.getValueInt found:");
        General.showError("key was not valid reference key for any object in hashmap");
        return INVALID_INT;
    }
    
    /** Returns a string reference. If the key is null or doesn't
     *occur in the hash it will return the default string reference
     *indicating invalidity.
     * @param key The key for the object to get.
     * @return The reference to a string referenced by key.
     */    
    public String getValueString( Object key ) {
        if( key == null ) {           
            General.showError("in Globals.getValueString found:");
            General.showError("key was not valid reference key for any object in hashmap");
            return INVALID_STRING;
        }
        
        Object o = m.get( key );
        if ( o == null ) {
            return INVALID_STRING;
        }
        
        return o.toString();
    }
    
    /** Creates new Globals */
    public Globals() {    
        loadMap();
    }

    /** Self test; tests the function <CODE>showmap</CODE>.
     * @param args Ignored.
     */
    public static void main (String[] args) 
    {
        Globals g = new Globals();
        g.showMap();
    }            
}
 