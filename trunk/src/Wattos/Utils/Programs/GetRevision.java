/*
 * GetRevision
 */

package Wattos.Utils.Programs;

import Wattos.Database.Defs;
import Wattos.Utils.General;

/**
 * The simplest Java program I've ever written.
 * @author Jurgen F. Doreleijers
 */
class GetRevision {
    
    /** Creates a new instance of GetEpochTime */
    public GetRevision() {
    }
     
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if ( args.length < 1 ) {
            String message = "No arument given.";
            General.doErrorExit(message);
        }
        String rootName = args[0];
//        General.showOutput("rootName: " + rootName);
        int revision = General.getSvnRevision( rootName);
        if ( Defs.isNull(revision)) {
            General.doErrorExit("Failed Wattos.Utils.Programs.GetRevision");
        }
        String e = Long.toString( revision);
        System.out.println(e);
    }
}
