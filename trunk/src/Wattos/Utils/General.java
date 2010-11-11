/*
 * Utils.java
 *
 * Created on December 6, 2001, 11:37 AM
 *
 * Utilities for dealing with memory, sleep, and reporting.
 *This software is copyright (c) 2002 Board of Regents, University of Wisconsin.
 *All Rights Reserved. No warranty implied or expressed.
 */

package Wattos.Utils;

import Wattos.CloneWars.UserInterface;
import Wattos.Database.Defs;

import com.braju.format.*;              // printf equivalent

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.jar.Attributes;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/** Static settings and methods for output stream handling, memory reporting, etc.
 *The show... methods should always end with a non-empty line and one that indicates
 *the error, debug, warning or regular status.
 *
 * @author Jurgen F. Doreleijers
 * @version 0.1
 */
public class General {

    /** The stream to print to. Default is System.out of course.
     */
    private static PrintStream out = System.out;

    public static PrintStream getOut() {
        return out;
    }

    /** If the argument is null then System.out will be used.
     */
    public static void setOut(PrintStream o) {
        if ( o == null ) {
            out = System.out;
        } else {
            out = o;
        }
    }

    /** If the argument is null then System.err will be used.
never used so don't define here.
     *
    public static void setErr(PrintStream o) {
        if ( o == null ) {
            out = System.err;
        } else {
            out = o;
        }
    }
     */

    /** Next variable controls e.g. how much debug statements will be done.
     *<PRE>
0 nothing
1 only errors
2 and warnings
3 and output (DEFAULT)
9 and debug info
</PRE>
     */
    public static final int verbosityNothing  = 0; // Even errors will be supressed
    public static final int verbosityError    = 1; // show only errrors
    public static final int verbosityWarning  = 2; // show errrors and warnings
    public static final int verbosityOutput   = 3; // and regular output DEFAULT
    public static final int verbosityDetail   = 4; // show more details
    public static final int verbosityDebug    = 9; // add debugging info (not recommended for casual user)
    public static int verbosity = verbosityOutput;
    //public static int verbosity = verbosityDebug;

    /** Should be the same value as the ResultSet.getInt methods return for nulls.
     * This is wrongly documented in O'Reilly's "Java enterprise in a nutshell",
     * 1st Ed., p.25.*/
    public static final int NULL_FOR_INTS = 0;

    /** When the program was successful. This is the only exit status
     * May be short cut (not always mention explicitly) because
     *it is the standard in unix any way.
     */
    public static final int EXIT_STATUS_SUCCESS                 = 0;
    /** When the program needs a better programmer.     */
    public static final int EXIT_STATUS_CODE_ERROR              = 1;
    /** When the program needs a better input.     */
    public static final int EXIT_STATUS_INPUT_ERROR             = 2;
    /** When the program needs more memory.     */
    public static final int EXIT_STATUS_OUT_OF_MEMORY           = 3;
    /** Undetermined error.     */
    public static final int EXIT_STATUS_ERROR                   = 9;

    public static String eol;

    static {
        Properties prop = System.getProperties();
        eol = prop.getProperty("line.separator");
        //showOutput("EOL is: [" + eol + "] with number of chars: " + eol.length());
    }
    /** Issues an error message saying this class can not be initiated.
     */
    public General() {
        General.showError("Don't try to initiate the General class; it's methods are static");
    }


    public static void sleep( long sleepTimeInMilliseconds ) {
        try {
            Thread.sleep( sleepTimeInMilliseconds ); // current thread sleeps
        } catch ( InterruptedException e ) {
            General.showWarning("received an interruption in my sleep");
        }
    }

    /** As documented in Sun's: The Java programming Language; 3rd edit.
     */
    public static void doFullestGarbageCollection() {
        Runtime rt = Runtime.getRuntime();
        long isFree = rt.freeMemory();
        long wasFree;
        do {
            wasFree = isFree;
            rt.runFinalization();
            rt.gc();
            isFree = rt.freeMemory();
            //General.showOutput("Memory free: " + isFree );
        } while ( isFree > wasFree );
    }

    public static long getMemoryUsed() {
        doFullestGarbageCollection();
        Runtime rt = Runtime.getRuntime();
        long total = rt.totalMemory();
        long free  = rt.freeMemory();
        return total - free;
    }

    public static void showMemoryUsed() {
        Runtime rt = Runtime.getRuntime();

        long total = rt.totalMemory();
        long free  = rt.freeMemory();
        long used  = total - free;
        String pattern = "0,000,000,000";
        DecimalFormat nf = new DecimalFormat(pattern);
        String usedStr = nf.format(used);
        String totalStr = nf.format(total);

        General.showOutput( "Memory used before GC: "+usedStr+" out of: "+ totalStr);
        doFullestGarbageCollection();
        total = rt.totalMemory();
        free  = rt.freeMemory();
        used  = total - free;
        /**
        p.add( used );
        p.add( total );
        General.showOutput( Format.sprintf("Memory used before GC: %13d out of: %13d", p) );
         */
    }

    /** Show a little debug info on environment     */
    public static void showEnvironment( ) {
        Properties prop = System.getProperties();
        showOutput( Strings.toString( prop ));
        try {
            String hostname = InetAddress.getLocalHost().getHostName();
            showOutput("From General.showEnvironment: Hostname is: " + hostname);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /** Show a little info on memory limitations then exit.*/
    public static void doOutOfMemoryExit( Error e ) {
        String message = "";
        message += "Ran out of memory.\n";
        message += "A way to increase the allowed memory consumption is to\n";
        message += "specify this when starting the Java virtual machine, like:\n";
        message += "   java -Xmx512m Wattos.Utils.General\n";
        message += "to set the maximum memory of the heap to 512 Mb.\n\n";
        General.showError( message);
        if ( verbosity >= verbosityError ) {
            General.showThrowable(e);
        }
        showMemoryUsed();
        doExit(General.EXIT_STATUS_OUT_OF_MEMORY);
    }


    private static void doExit(int code) {
        System.exit(code);
    }

    public static void doErrorExit( String message ) {
        General.showError(message);
        doExit(General.EXIT_STATUS_ERROR);
    }

    public static void doCodeBugExit( String message ) {
        doCodeBugExit( message, null, null, null );
    }

    public static void doCodeBugExit( String message, String lastKnownPosition, Throwable t, Class c ) {
        showCodeBug( message, lastKnownPosition, t, c );
        doExit(General.EXIT_STATUS_CODE_ERROR);
    }


    public static void showCodeBug( String message, String lastKnownPosition, Throwable t, Class c ) {

        if ( verbosity < verbosityError ) {
            return;
        }
        if ( lastKnownPosition != null ) {
            message += "\nERROR: ran into a code bug at: ";
            if ( c != null ) {
                message += c.getName() + ' ';
            } else {
                message += "unknown class, ";
            }
            if ( lastKnownPosition != null ) {
                message += "at last known position: " + lastKnownPosition + General.eol;
            } else {
                message += "at unknown last position.";
            }
            if ( t != null ) {
                message += t.getMessage();
            }
        }
        General.showError(message);
    }

    public static void showCodeBug( String message ) {
        showCodeBug( message, null, null, null);
    }

    public static void showError( String message, String lastKnownPosition, Throwable t, Class c ) {

        if ( verbosity < verbosityError ) {
            return;
        }
        if ( lastKnownPosition != null ) {
            message += "\nERROR: ran into an error at: ";
            if ( c != null ) {
                message += c.getName() + ' ';
            } else {
                message += "unknown class, ";
            }
            if ( lastKnownPosition != null ) {
                message += "at last known position: " + lastKnownPosition + General.eol;
            } else {
                message += "at unknown last position.";
            }
            if ( t != null ) {
                message += t.getMessage();
            }
        } else {
            if ( message != null ) {
                message = "ERROR: ".concat( message );;
            } else {
                message = "ERROR: null";
            }
        }

        General.showOutput(message);
    }

    public static void showError( String message ) {
        showError( message, null, null, null);
    }

    public static void showThrowable( Throwable t ) {
        StringWriter stw = new StringWriter();
        PrintWriter pw = new PrintWriter(stw,true);
        //pw.println(       "Localized message    : " + t.getLocalizedMessage() );
        //pw.println(       "Message              : " + t.getMessage() );
        t.printStackTrace(pw);
        General.showError(General.eol+stw.toString());
        General.showError("Found throwable error above");
    }

    public static void showWarning( String message, String lastKnownPosition, Throwable t, Class c ) {
        if ( verbosity < verbosityWarning ) {
            return;
        }
        if ( lastKnownPosition != null ) {
            message += "\nWARNING: ran into a uncommon situation at: ";
            if ( c != null ) {
                message += c.getName() + ' ';
            } else {
                message += "unknown class, ";
            }
            if ( lastKnownPosition != null ) {
                message += "at last known position: " + lastKnownPosition + General.eol;
            } else {
                message += "at unknown last position.";
            }
            if ( t != null ) {
                message += t.getMessage();
            }
        } else {
            message = "WARNING: ".concat( message );;
        }
        General.showOutput(message);
    }

    public static void showWarning( String message ) {
        showWarning( message, null, null, null);
    }

    public static void showDebug( String message, String lastKnownPosition, Throwable t, Class c ) {
        if ( verbosity < verbosityDebug ) {
            return;
        }

        if ( lastKnownPosition != null ) {
            message += "\nDEBUG: ran into a uncommon situation at: ";
            if ( c != null ) {
                message += c.getName() + ' ';
            } else {
                message += "unknown class, ";
            }
            if ( lastKnownPosition != null ) {
                message += "at last known position: " + lastKnownPosition + General.eol;
            } else {
                message += "at unknown last position.";
            }
            if ( t != null ) {
                message += t.getMessage();
            }
        } else {
            message = "DEBUG: ".concat( message );;
        }
        General.showOutput(message);
    }

    public static void showDebug( String message ) {
        showDebug( message, null, null, null);
    }

    public static void showDetail( String message ) {
        if ( verbosity < verbosityDetail) {
            return;
        }
        General.showOutput(message);
    }

    public static void showOutput( String message ) {
        if ( verbosity < verbosityOutput) {
            return;
        }
        out.println(message);
    }

    public static void showOutputNoEOL( String message ) {
        if ( verbosity < verbosityOutput) {
            return;
        }
        out.print(message);
    }

    public static void showOutputNoEol( String message ) {
        if ( verbosity < verbosityOutput) {
            return;
        }
        out.print(message);
    }

    public static void showOutput( String message, int value ) {
        if ( verbosity < verbosityOutput) {
            return;
        }
        Parameters p = new Parameters(); // Printf parameters
        p.add( value );
        String output = Format.sprintf(message, p);
        General.showOutput(output);
    }

    public static void showOutput( String message, float value ) {
        if ( verbosity < verbosityOutput) {
            return;
        }
        Parameters p = new Parameters(); // Printf parameters
        p.add( value );
        String output = Format.sprintf(message, p);
        General.showOutput(output);
    }

    public static void showOutput( String message, boolean value ) {
        if ( verbosity < verbosityOutput) {
            return;
        }
        Parameters p = new Parameters(); // Printf parameters
        p.add( value );
        String output = Format.sprintf(message, p);
        General.showOutput(output);
    }

    /** Sort the ArrayList with the selected element first wrapping around at the
     * end. If the given first element doesn't occur, a warning will be issued and the
     * collection will be returned in a simple sorted order.
     * Multiple occurences of the same item in the list will be treated correctly.
     * @param al The ArrayList to be rotated.
     * @param first_element Element that should become the first element after this operation.
     */
    public static void rotateCollectionToFirst( ArrayList al, Object first_element ) {
        // Sort
        Collections.sort(al);
//        int last_element_index = al.size()-1;
        int first_position = al.indexOf(first_element);
        if ( first_position < 0 ) {
            General.showWarning("element given does not exist, rotateCollectionToFirst");
            return;
        }
        // Rotate
        for (int i=0;i<first_position;i++) {
            Object ob_temp=al.get(0);
            al.remove(0);
            al.add(ob_temp);
        }
        /* The following simpler/faster code didn't work
         *subList didn't cast well and actually only generates a view...
        al.subList(first_position,al.size());
        al.removeRange(first_position,al.size());
        al.addAll(0, al_temp);
         */
    }


    /** Combine the contents in two attributes, putting the result into the first by
     * overwriting the original.
     *
     * @param add The new stuff in here.
     */
    public static void appendAttributes(Attributes old, Attributes add) {
        if (add.size() > 0) {
            Iterator keys = (add.keySet()).iterator();
            while (keys.hasNext()) {
                String key = ((Attributes.Name)(keys.next())).toString();
                String value = add.getValue(key);
                old.putValue(key, value);
            }
        }
    }

    /** Self test; tests the method: rotateCollectionToFirst.
     * @param args Command line arguments; ignored
     */
    public static void main (String[] args) {
        General.showOutput("Starting test of check routine." );
//        ArrayList al = new ArrayList();
//        if ( false ) {
//            al = new ArrayList();
//            al.add("1hue");
//            al.add("1aub");
//            al.add("1brv");
//            al.add("1brv");
//            General.showOutput("Collection:" + al.toString());
//            rotateCollectionToFirst(al,"1brv");
//            General.showOutput("Sorted collection." );
//            General.showOutput("Collection:" + al.toString());
//        }
        if ( true ) {
            showEnvironment();
        }
        if ( true ) {
            showMemoryUsed();
        }
        showOutput( "this %8.3f is formatted output", 0.1234f );
        showOutput( "this %l is formatted output too", true );
        General.showOutput("Finished all selected check routines." );
    }

    public static void flushOutputStream() {
        out.flush();
    }
    public static void setVerbosityToDebug(){
        verbosity = verbosityDebug;
    }

    /** Shows an error message with the string prepended to the current date
     */
    public static void showErrorDate(String string) {
        String date = java.text.DateFormat.getDateTimeInstance(
            java.text.DateFormat.FULL, java.text.DateFormat.FULL).format(new java.util.Date());
        General.showError(string + date);
    }


    public static String[] getProcessIdAndHostName() {
        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        // Get name representing the running Java virtual machine.
        // It returns something like 6460@AURORA. Where the value
        // before the @ symbol is the PID.
        String jvmName = bean.getName();
//        General.showOutput("Name = " + jvmName);
        // Extract the PID by splitting the string returned by the
        // bean.getName() method.
//        long pid = Long.valueOf(jvmName.split("@")[0]);
//        General.showOutput("PID  = " + pid);
        String[] result = jvmName.split("@");
        return result;
    }

    /**
     * @return revision number (int) or null if the revision isn't known. It depends on svn being available on the system.
     *
     */
    public static int getSvnRevision() {

        String wattosRootDirStr = InOut.getEnvVar("WATTOSROOT");

        if ( wattosRootDirStr == null ) {
            General.showError("Without set WATTOSROOT variable can't getSvnRevision");
            return Defs.NULL_INT;
        }
        String cmd = "svn info " + wattosRootDirStr;
        String[] result = OSExec.getstatusoutput(cmd);
        if (! result[0].equals("0")) {
            General.showError("Failed to run svn info");
            return Defs.NULL_INT;
        }
        if (! result[2].equals("")) {
            General.showError("Got error from svn info. Returned in last element of string array " + Strings.toString(result));
            return Defs.NULL_INT;
        }
        ArrayList lineList = Strings.getLines(result[1]);
        for (int i=0;i<lineList.size();i++) {
            String line = (String) lineList.get(i);
            if  (line.startsWith("Revision:")) {
                String[] split = line.split(" ");
                if ( split.length < 2 ) {
                    General.showError("Got error from svn info on line with revision: [" + line + "]");
                    return Defs.NULL_INT;
                }
                int wattosRevision = Integer.valueOf(split[split.length-1]);
                return wattosRevision;
            }
        }
        General.showDebug("Failed get svn revision.");
        return Defs.NULL_INT;
    }


    public static String getStartMessage() {
//      Copy catted from xplor via CING
//      user = "jd"
//      on   = "Stella.local (darwin/32bit/2cores/2.6.6)
//      at   = "(3676) 29-Oct-08 15:36:22
      String user = System.getProperty("user.name", "Unknown user");
      String osType = System.getProperty("os.name", "Unknown os name");
      osType = osType.replaceAll(" ", "");
      String javaVersion = System.getProperty("java.version");

      String[] pidAndHostNameList = getProcessIdAndHostName();
      long pid = Long.valueOf(pidAndHostNameList[0]);
      String machine = pidAndHostNameList[1];

      int ncpus = Runtime.getRuntime().availableProcessors();
      String bitNumber = System.getProperty("os.arch", "Unknown os arch");
      Parameters p = new Parameters();
      p.add(machine);
      p.add(osType);
      p.add(bitNumber);
      p.add(ncpus);
      p.add(javaVersion);
      String on = Format.sprintf("%s (%s/%s/%scores/%s)",p);
      Date now = new java.util.Date();
      String at = Dates.getDateLikePythonAscTime( now );
      at = "("+pid+") " + at;

      p.add(user);
      p.add(on);
      p.add(at);
      String result = Format.sprintf("User: %s on: %-42s at: %32s",p);

      int thisYear = 1900 + now.getYear();
      String copyright_years = "1999-" + thisYear; // Never have to update this again...
//      String[] authorList      = new String[]  { "Jurgen F. Doreleijers", "jurgend@cmbi.ru.nl"};
//    String wattosRevisionUrl = "http://code.google.com/p/wattos/source/detail?r=";

      String versionStr = UserInterface.WATTOS_VERSION;
      int svnRevision = getSvnRevision();
      if ( ! Defs.isNull( svnRevision )){
            versionStr += " (r"+ svnRevision + ") ";
      }
      String header =
      "======================================================================================================\n" +
      "| Wattos: Programs for Structural Biology & NMR        version " + versionStr + "JFD,GWV,ELU " + copyright_years + " |\n"+
      "======================================================================================================";

      return header + '\n' + result;
  }

}
