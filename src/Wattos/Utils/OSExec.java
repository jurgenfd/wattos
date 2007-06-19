 /*
 *This software is copyright (c) 2002 Board of Regents, University of Wisconsin.
 *All Rights Reserved. No warranty implied or expressed.
 */
package Wattos.Utils;
 
import java.io.*;

/** Class to capture i/o.
 * Adapted from Michael Daconta's
 * {@link <a href='http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html'>
 * paper</a>}.
 * @author Michael Daconta
 * @author Jurgen F. Doreleijers
 * @version 0.1
 */
class StreamGobbler extends Thread
{
    InputStream is;
    String type;
    StreamGobbler(InputStream is, String type)
    {
        this.is = is;
        this.type = type;
    }

    /** Kick off the stream gobbling.
     */    
    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null)
                General.showOutput(type + ">" + line);    
            } catch (IOException t)
              {
                General.showThrowable(t);  
              }
    }
}

/** Execute a command in a system dependent way. 
 *see #exec
 */
public class OSExec
{  
    /** Execute in a system dependent way. So make sure you give the appropriate
     * command. I.e. don't try to 'ls' on a Windows system without such a command.
     * @param args Command to be executed. Examples that work are given below.
     * On Linux:
     * "nedit -geometry 80x50-0+0"
     * On Windows:
     * "\"D:\\Program Files\\Microsoft Office\\Office\\Winword.exe\""
     * @return Exit status of the command. 0 usually means success.
     *What is usually?
     */
    public static int exec( String args )
    {
        // Standard error exit value
        int exitVal = 9;
       
        try
        {            
            String osName = System.getProperty("os.name" );
            //General.showOutput("In OS: " + osName);
           
            String[] cmd = new String[3];         
            if( osName.equals( "Windows NT" ) || 
                osName.equals( "Windows 2000" ) ||
                osName.equals( "Windows XP" ) )
            {
                cmd[0] = "cmd.exe" ;
                cmd[1] = "/C";
            }
            else if( osName.equals( "Windows 95" )  )
            {
                cmd[0] = "command.com";
                cmd[1] = "/C";
            } 
            else if ( osName.equals("Linux") || osName.equals("SunOS")) {
                // It's a unix variant
                cmd[0] = "/bin/csh";
                cmd[1] = "-c";                
            } 
            else {
                General.showOutput("Any Operating System that is not:");
                General.showOutput("- Windows");
                General.showOutput("- Linux or SunOS");
                General.showOutput("is currently not supported by Wattos.Utils.OSExec.");
                General.showOutput("The OS of this machine is determined by Java as: " 
                    + osName);
                return exitVal;
            }
            cmd[2] = args;

            Runtime rt = Runtime.getRuntime();
            //General.showOutput("Execing: " + cmd[0].toString());
            //General.showOutput("Execing: " + cmd[1].toString());
            //General.showOutput("Execing: " + cmd[2].toString());
            Process proc = rt.exec(cmd);
            // any error message?
            StreamGobbler errorGobbler = new 
                StreamGobbler(proc.getErrorStream(), "STDERR");
            
            // any output?
            StreamGobbler outputGobbler = new 
                StreamGobbler(proc.getInputStream(), "STDOUT");
                
            // kick them off
            errorGobbler.start();
            outputGobbler.start();
                                   
            // any error???
            exitVal = proc.waitFor();
            //General.showOutput("ExitValue: " + exitVal);
        } catch (Throwable t)          {
            General.showThrowable(t);
        }
        return(exitVal);        
    }

    /** Self test; executes a simple "dir" command.
     *If the command doesn't exist on the machine an error will be issued.
     *On both my Windows and Linux machine it does however.
     * @param args Command line arguments; ignored
     */
    public static void main(String args[])
    {
        if (args.length < 0)
        {
            General.showOutput("USAGE: java OSExec <cmd>");
            System.exit(1);
        }

        // The following command exists on Windows and my Linux box
        //String infile = 
        //exec("format ");
        //exec("java -version");
        //exec("\"C:\\Progra~1\\jEdit4~1.1pr\\jedit.exe i:\\pdbmirror2\\nozip\\data\\structures\\all\\nmr_restraints\\1brv.mr\"");
        //"C:\Progra~1\jEdit4~1.1pr\jedit.exe i:\pdbmirror2\nozip\data\structures\all\nmr_restraints\1brv.mr
        //exec("\"dir i:\\pdbmirror2\\nozip\\data\\structures\\all\\nmr_restraints\\1brv.mr\"");
        /// i:\\pdbmirror2\\nozip\\data\\structures\\all\\nmr_restraints\\1brv.mr");
        //exec("\"C:\\Program Files\\Microsoft Office\\Office\\Winword.exe\"");
        //exec("java -Xmx96m -Xms24m -jar S:\\linux\\src\\jedit\\4.0.3\\jedit.jar");
        String cmd = "\"C:\\Program Files\\jEdit 4.1\\jedit.exe\" inputFile";
        int status = exec(cmd);
        if ( status != 0 ) {
            General.showError("Found an error exit status: " + status);
            System.exit(status);
        } else {
            General.showOutput("Finished OSExec successfully");
        }            
    }
}
