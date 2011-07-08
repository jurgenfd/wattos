/*
 * GetRevision
 */

package Wattos.Utils.Programs;

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
        String e = Long.toString( General.getSvnRevision() );
        System.out.println(e);
    }   
}
