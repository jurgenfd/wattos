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

    /** Creates a new instance of GetRevision */
    public GetRevision() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String rootName = null;
        if ( args.length >0 ) {
            rootName = args[0];
        } else {
//          General.showDebug("No argument given; default assumed.");
        }
//        General.showOutput("rootName: " + rootName);
        int revision = General.getSvnRevision( rootName );
        if ( Defs.isNull(revision)) {
            General.doErrorExit("Failed Wattos.Utils.Programs.GetRevision");
        }
//        String e = Long.toString( revision);
        System.out.println(revision); // can be unboxed automatically since Java 1.5.
    }
}
