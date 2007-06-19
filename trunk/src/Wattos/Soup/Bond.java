package Wattos.Soup;

import java.io.*;
import java.util.*;
import Wattos.Utils.*;
import Wattos.Database.*;
/**
 * Property of 2 atoms. Value contains the bond length.
 *
 * The first atom in the bond in case of a hydrogen bond is the donor, the
 * second is the acceptor.
 *
 * @author Jurgen F. Doreleijers
 * @version 1
 */
public class Bond extends PropNAtom implements Serializable {
        
	private static final long serialVersionUID = -1886027960322072182L;
	
    /** So a value of zero denotes a 'single' bond type.
     */
    public static String[] BOND_TYPES = { 
        "unknown", 
        "single", 
        "double", 
        "peptide", 
        "partially double", 
        "hydrogen",
        "tentative"
    };
    public static final int BOND_TYPE_UNKNOWN               = 0;
    public static final int BOND_TYPE_SINGLE                = 1;
    public static final int BOND_TYPE_DOUBLE                = 2;
    public static final int BOND_TYPE_PEPTIDE               = 3;
    public static final int BOND_TYPE_PARTIALLY_DOUBLE      = 4;
    public static final int BOND_TYPE_HYDROGEN              = 5;
    public static final int BOND_TYPE_TENTATIVE             = 6; // Used for looking for potential bonds.
    
        
    public Bond(DBMS dbms, RelationSoS relationSoSParent) {
        super(dbms, relationSoSParent); 
        //General.showDebug("back in Bond constructor");
        resetConvenienceVariables();
    }

    /** The relationSetName is a parameter so non-standard relation sets 
     *can be created; e.g. AtomTmp with a relation named AtomTmpMain etc.
     */
    public Bond(DBMS dbms, String relationSetName, RelationSoS relationSoSParent) {
        super(dbms, relationSoSParent);
        //General.showDebug("back in Bond constructor");
        name = relationSetName;
        gumbo = (Gumbo) relationSoSParent;
        resetConvenienceVariables();
    }
    
    public boolean init(DBMS dbms) {
        //General.showDebug("now in Bond.init()");
        super.init(dbms);        
        //General.showDebug("back in Bond.init()");
        name =                Gumbo.DEFAULT_ATTRIBUTE_SET_BOND[RELATION_ID_SET_NAME];
        
        // MAIN RELATION in addition to the ones in PropNAtom item.        
        
        Relation relation = null;
        String relationName = Gumbo.DEFAULT_ATTRIBUTE_SET_BOND[RELATION_ID_MAIN_RELATION_NAME];        
        try {
            relation = new Relation(relationName, dbms, this);
        } catch ( Exception e ) {
            General.showThrowable(e);
            return false;
        }

        //General.showDebug( "DEFAULT_ATTRIBUTES_TYPES in bond: " + Strings.toString(DEFAULT_ATTRIBUTES_TYPES));
        // Create the fkcs without checking that the columns exist yet.
        DEFAULT_ATTRIBUTE_FKCS = ForeignKeyConstrSet.createFromRelation(dbms, DEFAULT_ATTRIBUTE_FKCS_FROM_TO, relationName);        
        relation.insertColumnSet( 0, DEFAULT_ATTRIBUTES_TYPES, DEFAULT_ATTRIBUTES_ORDER, 
            DEFAULT_ATTRIBUTE_VALUES, DEFAULT_ATTRIBUTE_FKCS);
        addRelation( relation );
        mainRelation = relation;

        // OTHER RELATIONS HERE
        //..
        
        return true;        
    }    

    /** Calculates the bond length.
     *
     */
    public void calculateValues(BitSet todo) {
        for (int rid=todo.nextSetBit(0);rid>=0;rid=todo.nextSetBit(rid+1)) {
            value_1[rid] = gumbo.atom.calcDistanceFast( atom_A_Id[rid], atom_B_Id[rid] );
        }
    }
    
    /**     */
    public boolean resetConvenienceVariables() {        
        super.resetConvenienceVariables();
        return true;
    }    
}
 
