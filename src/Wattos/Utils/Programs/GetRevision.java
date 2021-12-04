/*
 * GetRevision
 */

package Wattos.Utils.Programs;

import Wattos.Database.Defs;
import Wattos.Utils.General;

class GetRevision {
    public static void main(String[] args) {
        General.verbosity = General.verbosityDebug;

        String rootName = null;
        if (args.length > 0) {
            rootName = args[0];
        } else {
            General.showDebug("No argument given; default assumed.");
        }
        General.showDebug("rootName: " + rootName);
        String revision = General.getRevision(rootName);
        if (Defs.isNull(revision)) {
            General.doErrorExit("Failed Wattos.Utils.Programs.GetRevision");
        }
        System.out.println(revision);
    }
}
