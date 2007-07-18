/*
 * Residue.java
 *
 * Created on November 8, 2002, 4:41 PM
 */

package Wattos.Soup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;

import Wattos.Database.DBMS;
import Wattos.Database.Defs;
import Wattos.Database.ForeignKeyConstrSet;
import Wattos.Database.Relation;
import Wattos.Database.RelationSet;
import Wattos.Database.RelationSoS;
import Wattos.Database.SQLSelect;
import Wattos.Database.Indices.Index;
import Wattos.Database.Indices.IndexSortedString;
import Wattos.Utils.General;
import Wattos.Utils.PrimitiveArray;
import Wattos.Utils.StringSet;
import Wattos.Utils.Strings;
import Wattos.Utils.Wiskunde.Geometry;
import Wattos.Utils.Wiskunde.Geometry3D;
import cern.colt.list.IntArrayList;

import com.braju.format.Format;
import com.braju.format.Parameters;

/**
 * Residue contains one or more atoms.
 * <P>
 * Residue names are unique across the universe; Wattos uses the 1 and 3 letter
 * PDB abbreviations. Ignoring difference between OGUA and RGUA as WHAT IF
 * distinguishes for instance. Those differences can be derived from the atoms
 * set in the residues. Other differences like HIS+ and HIS or CYS and CYSS will
 * not be noted in the residue name that is kept as a key.
 * <P>
 * The author of this software will probably not win the Nobel price for solving
 * the residue numbering problem but will attempt to deal with it. The physical
 * row id is the primary key into the residues and gives an id that is unique
 * over all residues in the soup (entries/models/molecules).
 * <P>
 * Within one molecule there is the unique number that starts at 1 and is most
 * like NMR-STAR, mmCIF, and the EBI residue numbering. Then there are several
 * other numbering schemes that will be maintained for convenience of the users
 * basically to maintain the information like WHAT IF does with the
 * 'old/original residue number' concept.
 * <P>
 * Mappings<BR>
 * 
 * <PRE>
 * 
 * Wattos NMRSTAR 3.0 number _Entity_comp_index.Num pdb_number . id
 * _Entity_poly_seq.Comp_index_num (for polymers only)
 * 
 * </PRE>
 * 
 * <PRE>
 * 
 * Wattos PDB formatted files number created on the fly pdb_number pdb res
 * number id pdb res number
 * 
 * </PRE>
 * 
 * @author Jurgen F. Doreleijers
 * @version 1
 */
public class Residue extends GumboItem implements Serializable {

    private static final long serialVersionUID = -1207795172754062330L;

    /** Convenience variables */
    public int[] molId;

    public int[] modelId;

    public int[] entryId;

    public String[] authResNameList;

    public StringSet authResNameListNR;

    public String[] authResIdList;

    public StringSet authResIdListNR;

    public Residue(DBMS dbms, RelationSoS relationSoSParent) {
        super(dbms, relationSoSParent);
        // General.showDebug("back in Residue constructor");
    }

    public Residue(DBMS dbms, String relationSetName,
            RelationSoS relationSoSParent) {
        super(dbms, relationSoSParent);
        name = relationSetName;
    }

    public boolean init(DBMS dbms) {
        super.init(dbms);

        name = Gumbo.DEFAULT_ATTRIBUTE_SET_RES[RELATION_ID_SET_NAME];

        // MAIN RELATION in addition to the ones in gumbo item.
        DEFAULT_ATTRIBUTES_TYPES.put(
                Gumbo.DEFAULT_ATTRIBUTE_SET_MOL[RELATION_ID_COLUMN_NAME],
                new Integer(DATA_TYPE_INT));
        DEFAULT_ATTRIBUTES_TYPES.put(
                Gumbo.DEFAULT_ATTRIBUTE_SET_MODEL[RELATION_ID_COLUMN_NAME],
                new Integer(DATA_TYPE_INT));
        DEFAULT_ATTRIBUTES_TYPES.put(
                Gumbo.DEFAULT_ATTRIBUTE_SET_ENTRY[RELATION_ID_COLUMN_NAME],
                new Integer(DATA_TYPE_INT));
        DEFAULT_ATTRIBUTES_TYPES.put(Gumbo.DEFAULT_ATTRIBUTE_AUTH_RES_NAME,
                new Integer(DATA_TYPE_STRINGNR));
        DEFAULT_ATTRIBUTES_TYPES.put(Gumbo.DEFAULT_ATTRIBUTE_AUTH_RES_ID,
                new Integer(DATA_TYPE_STRINGNR));

        DEFAULT_ATTRIBUTES_ORDER
                .add(Gumbo.DEFAULT_ATTRIBUTE_SET_MOL[RELATION_ID_COLUMN_NAME]);
        DEFAULT_ATTRIBUTES_ORDER
                .add(Gumbo.DEFAULT_ATTRIBUTE_SET_MODEL[RELATION_ID_COLUMN_NAME]);
        DEFAULT_ATTRIBUTES_ORDER
                .add(Gumbo.DEFAULT_ATTRIBUTE_SET_ENTRY[RELATION_ID_COLUMN_NAME]);
        DEFAULT_ATTRIBUTES_ORDER.add(Gumbo.DEFAULT_ATTRIBUTE_AUTH_RES_NAME);
        DEFAULT_ATTRIBUTES_ORDER.add(Gumbo.DEFAULT_ATTRIBUTE_AUTH_RES_ID);

        DEFAULT_ATTRIBUTE_FKCS_FROM_TO
                .add(new String[] {
                        Gumbo.DEFAULT_ATTRIBUTE_SET_MOL[RELATION_ID_COLUMN_NAME],
                        Gumbo.DEFAULT_ATTRIBUTE_SET_MOL[RELATION_ID_MAIN_RELATION_NAME] });
        DEFAULT_ATTRIBUTE_FKCS_FROM_TO
                .add(new String[] {
                        Gumbo.DEFAULT_ATTRIBUTE_SET_MODEL[RELATION_ID_COLUMN_NAME],
                        Gumbo.DEFAULT_ATTRIBUTE_SET_MODEL[RELATION_ID_MAIN_RELATION_NAME] });
        DEFAULT_ATTRIBUTE_FKCS_FROM_TO
                .add(new String[] {
                        Gumbo.DEFAULT_ATTRIBUTE_SET_ENTRY[RELATION_ID_COLUMN_NAME],
                        Gumbo.DEFAULT_ATTRIBUTE_SET_ENTRY[RELATION_ID_MAIN_RELATION_NAME] });

        Relation relation = null;
        String relationName = Gumbo.DEFAULT_ATTRIBUTE_SET_RES[RELATION_ID_MAIN_RELATION_NAME];
        try {
            relation = new Relation(relationName, dbms, this);
        } catch (Exception e) {
            General.showThrowable(e);
            return false;
        }

        // Create the fkcs without checking that the columns exist yet.
        DEFAULT_ATTRIBUTE_FKCS = ForeignKeyConstrSet.createFromRelation(dbms,
                DEFAULT_ATTRIBUTE_FKCS_FROM_TO, relationName);
        relation.insertColumnSet(0, DEFAULT_ATTRIBUTES_TYPES,
                DEFAULT_ATTRIBUTES_ORDER, DEFAULT_ATTRIBUTE_VALUES,
                DEFAULT_ATTRIBUTE_FKCS);
        addRelation(relation);
        mainRelation = relation;

        // OTHER RELATIONS HERE

        return true;
    }

    /**
     * Adds a new res in the array, filling in all the required properties as
     * available from the parent. Make sure the number you insert is defined in
     * the same way Wattos does it: 1 through N within each molecule; no gaps!
     * Returns -1 for failure.
     */
    public int add(String name, int number, String auth_number,
            String auth_name, int parentId) {

        int result = super.add(name);
        if (result < 0) {
            General
                    .showCodeBug("Failed to get a new row id for a residue with name: "
                            + name);
            return -1;
        }
        authResNameList[result] = authResNameListNR.intern(auth_name);
        authResIdList[result] = authResIdListNR.intern(auth_number);
        this.number[result] = number;
        molId[result] = parentId;
        modelId[result] = gumbo.mol.modelId[parentId];
        entryId[result] = gumbo.mol.entryId[parentId];
        return result;
    }

    /**
     * Get a alphabetically sorted list of unique residue names in one model.
     * This is usefull to list unknown residue names.
     */
    public String[] getDistinctNamesInModel(int modelId) {

        General.showDebug("Looking for residues in model with rid: " + modelId);
        /**
         * Something like the sql: SELECT DISTINCT name FROM RESIDUE r WHERE
         * r.entry_id = entryId
         */
        BitSet resultSelection = SQLSelect
                .selectBitSet(dbms,
                        mainRelation, // relation
                        Gumbo.DEFAULT_ATTRIBUTE_SET_MODEL[RelationSet.RELATION_ID_COLUMN_NAME], // selection
                                                                                                // column
                        SQLSelect.OPERATION_TYPE_EQUALS, // selection
                                                            // operation
                        new Integer(modelId), false); // selection value
        if (resultSelection == null) {
            General
                    .showWarning("Failed to getDistinctNamesInEntry by SQLSelect method selectDistinct.");
            return null;
        }
        String[] result = Strings.getDistinctSorted(nameList, resultSelection);
        return result;
    }

    /**
     * All residues in bitset given are renumbered according to the order they
     * are numbered or to the physical order if no numbering is present.
     * <P>
     * Input: Set R the residues to do the residue renumbering on.<BR>
     * The algorithm goes like this:<BR>
     * Find the unique molecules for residues in R -> set M<BR>
     * For each model Mi:<BR>
     * Find residues in Mi AND R -> Set R2<BR>
     * Renumber residues in R2 using preordering if present.<BR>
     */
    public boolean renumberRows(String columnLabel, BitSet toDo, int startNumber) {

        int mol;
        String columnLabelMolId = Gumbo.DEFAULT_ATTRIBUTE_SET_MOL[RelationSet.RELATION_ID_COLUMN_NAME];

        // Find the unique molecules for the given residues to do.
        // Actually finding residues that have different molecules as parent
        BitSet selResSub = SQLSelect.getDistinct(dbms, mainRelation,
                columnLabelMolId, toDo);
        if (selResSub == null) {
            General
                    .showCodeBug("Failed to get a distince set of molecules for the given residues: "
                            + PrimitiveArray.toString(toDo));
            return false;
        }
        // For each molecule (through the resid)
        for (int res = selResSub.nextSetBit(0); res >= 0; res = selResSub
                .nextSetBit(res + 1)) {
            mol = molId[res];
            // Get the residues for this molecule that are also in toDo.
            BitSet selResSub2 = SQLSelect.selectBitSet(dbms, mainRelation,
                    columnLabelMolId, SQLSelect.OPERATION_TYPE_EQUALS,
                    new Integer(mol), false);
            if (selResSub2 == null) {
                General
                        .showCodeBug("Failed to get a residue set in molecule with rid: "
                                + mol);
                return false;
            }
            selResSub2.and(toDo);
            // General.showDebug("In mol with rid: " + mol + " found residues to
            // renumber: " + selResSub2.cardinality());
            // Renumber the resiudes in R2 starting at 1.
            mainRelation.renumberRows(Relation.DEFAULT_ATTRIBUTE_NUMBER,
                    selResSub2, 1);
        }
        // General.showDebug("Renumbered residues at this point");
        return true;
    }

    /**     */
    public boolean resetConvenienceVariables() {
        super.resetConvenienceVariables();
        molId = (int[]) mainRelation
                .getColumn(Gumbo.DEFAULT_ATTRIBUTE_SET_MOL[RelationSet.RELATION_ID_COLUMN_NAME]);
        modelId = (int[]) mainRelation
                .getColumn(Gumbo.DEFAULT_ATTRIBUTE_SET_MODEL[RelationSet.RELATION_ID_COLUMN_NAME]);
        entryId = (int[]) mainRelation
                .getColumn(Gumbo.DEFAULT_ATTRIBUTE_SET_ENTRY[RelationSet.RELATION_ID_COLUMN_NAME]);
        authResNameList = (String[]) mainRelation
                .getColumn(Gumbo.DEFAULT_ATTRIBUTE_AUTH_RES_NAME);
        authResNameListNR = mainRelation
                .getColumnStringSet(Gumbo.DEFAULT_ATTRIBUTE_AUTH_RES_NAME);
        authResIdList = (String[]) mainRelation
                .getColumn(Gumbo.DEFAULT_ATTRIBUTE_AUTH_RES_ID);
        authResIdListNR = mainRelation
                .getColumnStringSet(Gumbo.DEFAULT_ATTRIBUTE_AUTH_RES_ID);
        if (molId == null || modelId == null || entryId == null
                || authResNameList == null || authResNameListNR == null
                || authResIdList == null) {
            return false;
        }
        return true;
    }

    /**
     * Return the entry, model, mol, res, and atom ids as a string.
     */
    public String toString(int resRid) {
        if (resRid < 0) {
            General.showError("Can't do toString for residue rid < 0");
            return null;
        }
        int entryNumber = gumbo.entry.number[entryId[resRid]];
        int modelNumber = gumbo.model.number[modelId[resRid]];
        int molNumber = gumbo.mol.number[molId[resRid]];
        int resNumber = number[resRid];
        String resName = nameList[resRid];
        // sb.append( "Atom: " + name + " Res: " + resNumber + "(" + resName +
        // ") Mol: " + molNumber + " Model: " + modelNumber + " Entry: " +
        // entryNumber);
        Parameters p = new Parameters(); // for printf
        p.add(resNumber);
        p.add(resName);
        p.add(molNumber);
        p.add(modelNumber);
        p.add(entryNumber);
        return Format.sprintf(
                "Residue: %3d(%4s) Mol: %3d Model: %3d Entry: %2d", p);
    }

    /**
     * Returns nucleic acid residues
     */
    public BitSet getNAResidues(BitSet todo) {
        BitSet result = new BitSet();
        for (int i = todo.nextSetBit(0); i >= 0; i = todo.nextSetBit(i + 1)) {
            if (Biochemistry.commonResidueNameNA.containsKey(nameList[i])) {
                result.set(i);
            } else {
                // General.showDebug("Given residue isn't a NA: [" + nameList[i]
                // + "]");
            }
        }
        return result;
    }

    /**
     * Returns the list of atoms in given residue
     */
    public BitSet getAtoms(int rid) {
        return SQLSelect.selectBitSet(dbms, gumbo.atom.mainRelation,
                Gumbo.DEFAULT_ATTRIBUTE_SET_RES[RELATION_ID_COLUMN_NAME],
                SQLSelect.OPERATION_TYPE_EQUALS, new Integer(rid), false);
    }

    /**
     * Calculate the angle between the normals of the two base pairs returning a
     * number between 0 and 90 degrees (or pi/2). Few checks are done for this
     * purpose. Result will be in radians (0-pi/2).
     */
    public float calcGAngleBasePair(int rid_i, int rid_j) {
        // the normals
        double[][] normal = new double[2][];
        int rid;
        int rid_n1;
        int rid_n3;
        int rid_c5;
        IndexSortedString indexName = (IndexSortedString) gumbo.atom.mainRelation
                .getIndex(Relation.DEFAULT_ATTRIBUTE_NAME,
                        Index.INDEX_TYPE_SORTED);

        BitSet atomsNameN1 = (BitSet) indexName.getRidList("N1",
                Index.LIST_TYPE_BITSET, null);
        BitSet atomsNameN3 = (BitSet) indexName.getRidList("N3",
                Index.LIST_TYPE_BITSET, null);
        BitSet atomsNameC5 = (BitSet) indexName.getRidList("C5",
                Index.LIST_TYPE_BITSET, null);
        if ((atomsNameN1 == null) || (atomsNameN3 == null)
                || (atomsNameC5 == null)) {
            General
                    .showError("Failed to get n1, n3, and c5 atoms in calcGAngleBasePair(1) for all residues.");
            return Defs.NULL_FLOAT;
        }
        // General.showDebug( "Found number of N1 atoms in residue: " +
        // atomsNameN1.cardinality());
        // General.showDebug( "N1 atoms are:\n" +
        // gumbo.atom.toString(atomsNameN1));
        BitSet tmp = new BitSet();

        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                rid = rid_i;
            } else {
                rid = rid_j;
            }
            // find the atoms N1, N3, and C5 in the six-membered (sub-)ring
            BitSet atomsResidues = getAtoms(rid);
            if (atomsResidues == null) {
                General.showError("Failed to get atoms in this residue:\n"
                        + toString(rid));
                return Defs.NULL_FLOAT;
            }
            // General.showDebug( "Found number of atoms in residue: " +
            // atomsResidues.cardinality());
            // General.showDebug( "Atoms are: " +
            // gumbo.atom.toString(atomsResidues));
            tmp.clear();
            tmp.or(atomsResidues);
            tmp.and(atomsNameN1);
            rid_n1 = tmp.nextSetBit(0);
            tmp.clear();
            tmp.or(atomsResidues);
            tmp.and(atomsNameN3);
            rid_n3 = tmp.nextSetBit(0);
            tmp.clear();
            tmp.or(atomsResidues);
            tmp.and(atomsNameC5);
            rid_c5 = tmp.nextSetBit(0);
            if ((rid_n1 < 0) || (rid_n3 < 0) || (rid_c5 < 0)) {
                General
                        .showError("Failed to get n1, n3, and c5 atoms in calcGAngleBasePair(2) for residue: "
                                + toString(rid));
                return Defs.NULL_FLOAT;
            }
            double[] n1 = gumbo.atom.getVector(rid_n1);
            double[] n3 = gumbo.atom.getVector(rid_n3);
            double[] c5 = gumbo.atom.getVector(rid_c5);
            double[] v1 = Geometry.getVector(n1, n3);
            double[] v2 = Geometry.getVector(n1, c5);
            normal[i] = Geometry3D.vectorCrossProduct(v1, v2);
        }
        double angle = Geometry3D.calcAngle(normal[0], normal[1]);
        if (angle > (Math.PI / 2)) {
            angle = Math.PI - angle;
        }
        return (float) angle;
    }

    /**
     * This method requires the presence of hydrogen bonds before this call. The
     * hydrogen bonds don't need to be selected.<BR>
     * <OL>
     * <LI>A base (Bx) is in a set of co-planar bases (Si) if it has at least
     * two hydrogen bonds to one other base By in Si. The angle between the
     * normals of Bx and By should be less than the value given, or 45 degrees
     * (default) if the given value is negative.
     * <LI>A set is called a pair, triplet, or quartet for when there are 2,3,
     * or 4 members.
     * <LI>U is a superset of all bases.
     * <LI>V is a superset of all bases in any set Si so: V = Sum Si.
     * <LI>W is a superset of all bases not in any set Si so W = U - V.
     * <LI>Only 4 bases can be in Si. The bases with the least hydrogen bonds
     * to the other bases in Si will be deleted.
     * <LI>A base can be only in one set Si.
     * <LI>The rmsd from a plane defined by the bases in Si is below a given
     * threshold multiplied by the number of bases. (TODO).
     * <LI>Direct neighbours in sequence can not be in the same set (Check with
     * CST if this ever occurs).
     * <LI>Routine will print results but not store them for now.
     * </OL>
     * 
     * The algorithm is simple.
     * <OL>
     * <LI>Loop1 over all selected bases Bx of the one selected entry (using
     * first model only)
     * <LI> Add Bx to Si
     * <LI> Loop2 until no new bases are added
     * <LI> Loop3 over base pairs By in W
     * <LI> If By matches with any in Si then add By to Si (loop 4)
     * <LI> EndLoop3
     * <LI> EndLoop2
     * <LI> Remove the worst B's until no more than 4 are left in Si
     * <LI> If only Bx is Si then remove Si from V
     * <LI> Store the results
     * <LI>EndLoop1
     * <LI>
     * </OL>
     * 
     * <P>
     * The 45 degree maximum angle between the base normals is as suggested by:
     * Ban,N., Nissen,P., Hansen,J., Moore,P.B. and Steitz,T.A. (2000). The
     * complete atomic structure of the large ribosomal subunit at 2.4 A
     * resolution. Science 289, 905-920. NB
     * 
     * @see Entry#calcHydrogenBond
     * @param resInMaster
     *            residues in the master model
     * @param thresholdBasePairAngle
     * @param onlyWC
     *            ignore non-Watson/Crick Basepairs. Note that only 2 WC
     *            hydrogen bonds need to be present.
     * @return true for success
     */
    public boolean calcCoPlanarBasesSet(BitSet resInMaster,
            float thresholdBasePairAngle, boolean onlyWC, String location) {
        int resRidA = -1;
        int resRidB = -1;
        /** Order residue numbers in list of existing NAs */
        int resA, resB;
        int swap;

        int entryRid = gumbo.entry.getEntryId();
        if (entryRid < 0) {
            return false;
        }
        General
                .showDebug("Trying to match bases to eachother for number of residues: "
                        + resInMaster.cardinality());
        BitSet basesInMaster = getNAResidues(resInMaster);
        int basesInMasterCount = basesInMaster.cardinality();
        General
                .showOutput("Trying to match bases to eachother for number of bases: "
                        + basesInMasterCount);
        // General.showDebug("Bases:\n" + toString(basesInMaster));
        // BitSet S = new BitSet();
        BitSet U = new BitSet();
        BitSet V = new BitSet();
        BitSet W = new BitSet();
        ArrayList result = new ArrayList();

        /** Setup a square matrix with the number of HB between bases. */
        int[][] countHB = new int[basesInMasterCount][];
        for (int i = 0; i < basesInMasterCount; i++) {
            countHB[i] = new int[basesInMasterCount];
        }
        /** Define a map from res rid to res number considered */
        int[] mapResFromRid = new int[mainRelation.sizeMax];
        // Provoke errors in case of abuse by initializing the map with a bad
        // value.
        Arrays.fill(mapResFromRid, -1);
        int ii = 0;
        for (int i = basesInMaster.nextSetBit(0); i >= 0; i = basesInMaster
                .nextSetBit(i + 1)) {
            mapResFromRid[i] = ii;
            ii++;
        }
        // General.showDebug("Map:\n" + PrimitiveArray.toString(mapResFromRid));

        // Cache the number of hb between the bases in a matrix. No size
        // worries.
        // Reduce to short if needed.
        Index indexType = gumbo.bond.mainRelation.getIndex(
                Relation.DEFAULT_ATTRIBUTE_TYPE, Index.INDEX_TYPE_SORTED);
        Index indexEntry = gumbo.bond.mainRelation.getIndex(
                Gumbo.DEFAULT_ATTRIBUTE_SET_ENTRY[RELATION_ID_COLUMN_NAME],
                Index.INDEX_TYPE_SORTED);
        BitSet bondHBSet = (BitSet) indexType.getRidList(new Integer(
                Bond.BOND_TYPE_HYDROGEN), Index.LIST_TYPE_BITSET, null);
        BitSet bondEntrySet = (BitSet) indexEntry.getRidList(new Integer(
                entryRid), Index.LIST_TYPE_BITSET, null);
        bondHBSet.and(bondEntrySet);
        General.showOutput("Considering number of hydrogen bonds: "
                + bondHBSet.cardinality());
        int atomRidA = -1;
        int atomRidB = -1;
        String atomNameA = null;
        String atomNameB = null;
        int hbSkippedCount = 0;
        for (int bondRid = bondHBSet.nextSetBit(0); bondRid >= 0; bondRid = bondHBSet
                .nextSetBit(bondRid + 1)) {
            // General.showDebug("HB:" + gumbo.bond.toString(bondRid));
            atomRidA = gumbo.bond.atom_A_Id[bondRid];
            atomRidB = gumbo.bond.atom_B_Id[bondRid];
            atomNameA = gumbo.atom.nameList[atomRidA];
            atomNameB = gumbo.atom.nameList[atomRidB];
            if (!(basesInMaster.get(gumbo.atom.resId[atomRidA]) && basesInMaster
                    .get(gumbo.atom.resId[atomRidB]))) {
                // General.showDebug("Skipping hydrogen bonds other than
                // nucleotide to nucleotide: " + gumbo.bond.toString(bondRid));
                bondHBSet.clear(bondRid);
                hbSkippedCount++;
                continue;
            }
            if (Biochemistry.backboneNA.containsKey(atomNameA)
                    || Biochemistry.backboneNA.containsKey(atomNameB)) {
                // General.showDebug("Skipping hydrogen bonds other than base to
                // base: " + gumbo.bond.toString(bondRid));
                bondHBSet.clear(bondRid);
                hbSkippedCount++;
                continue;
            }

            resRidA = gumbo.atom.resId[atomRidA]; // donor residue
            resRidB = gumbo.atom.resId[atomRidB];
            String resNameA = gumbo.res.nameList[resRidA];
            String resNameB = gumbo.res.nameList[resRidB];
            if (onlyWC
                    && !Biochemistry.isWCBond(atomNameA, resNameA, atomNameB,
                            resNameB)) {
                General.showDebug("Skipping hydrogen bonds other than wc: "
                        + gumbo.bond.toString(bondRid));
                bondHBSet.clear(bondRid);
                hbSkippedCount++;
                continue;
            }

            resA = mapResFromRid[resRidA];
            resB = mapResFromRid[resRidB];
            if (resA > resB) {
                swap = resA;
                resA = resB;
                resB = swap;
            }
            // Now resA is the 'smaller' residue so the matrix will only be
            // filled
            // on the top right of the diagonal (NOT in Rid space).
            // General.showDebug("Adding hydrogen bond between bases: " + resA +
            // " and: " + resB);
            countHB[resA][resB]++;
        }
        General.showDebug("Skipped hydrogen bonds                : "
                + hbSkippedCount);
        General.showDebug("Number of hydrogen bonds in matrix (B):"
                + bondHBSet.cardinality());
//        if (General.verbosity >= General.verbosityDebug) { // inserted clause
//                                                            // for speed.
//            General.showDebug("Number of hydrogen bonds in matrix (A):\n"
//                    + PrimitiveMatrix.toString(countHB));
//        }

        U.or(basesInMaster);
        W.or(basesInMaster);
        BitSet Wtemp = new BitSet();
        int x, y, z, zz = 0;

        IntArrayList Slist = new IntArrayList();
        for (x = U.nextSetBit(0); x >= 0; x = U.nextSetBit(x + 1)) { // loop
                                                                        // 1
            Slist.setSize(0); // clears matrix without allocating a new
                                // object.
            Slist.add(x);
            Wtemp.clear(); // clears bits without allocating new data.
            Wtemp.or(W);
            Wtemp.clear(x);
            boolean baseAdded = true;
            int iterationCount = 0;
            // loop 2
            while (baseAdded && (iterationCount < 5)) { // try to add bases 5
                                                        // times
                iterationCount++;
                // General.showDebug("Now in iteration: " + iterationCount + "
                // for first base: " + x);
                baseAdded = false;
                // loop 3
                for (y = Wtemp.nextSetBit(0); y >= 0; y = Wtemp
                        .nextSetBit(y + 1)) { // y is the candidate (res rid)
                    // loop 4
                    for (zz = 0; zz < Slist.size(); zz++) { // to match with
                                                            // bases already in
                                                            // S which includes
                                                            // at least x.
                        // define the set candidate z (res rid)
                        z = Slist.getQuick(zz);
                        resA = mapResFromRid[y];
                        resB = mapResFromRid[z];
                        if (resA > resB) {
                            swap = resA;
                            resA = resB;
                            resB = swap;
                        }
                        if (countHB[resA][resB] > 1) {
                            float angle = calcGAngleBasePair(y, z);
                            if (Defs.isNull(angle)) {
                                General
                                        .showError("Failed calcGAngleBasePair between:\n"
                                                + toString(y)
                                                + General.eol
                                                + toString(z));
                                // General.showError("Ignoring this potential
                                // pair.");
                                return false;
                            }
                            // General.showDebug("Angle between bases is: " +
                            // Geometry.CF*angle);
                            if ((Geometry.CF * angle) > thresholdBasePairAngle) {
                                continue;
                            }
                            // Getting zzzzleepy.
                            boolean foundConsequetive = false;
                            for (int zzz = 0; zzz < Slist.size(); zzz++) {
                                int zzzz = Slist.getQuick(zzz);
                                int sep = getSeparation(y, zzzz);
                                if (Math.abs(sep) == 1) {
                                    General
                                            .showWarning("Ignoring potential pair between consequetive bases:\n"
                                                    + toString(y)
                                                    + General.eol
                                                    + toString(zzzz));
                                    foundConsequetive = true;
                                    break;
                                }
                            }
                            if (foundConsequetive) {
                                continue;
                            }

                            Slist.add(y);
                            Wtemp.clear(y);
                            baseAdded = true;
                            if (General.verbosity >= General.verbosityDebug) {
                                General
                                        .showDebug("Found pair between new base:\n"
                                                + toString(y)
                                                + "and base already in set:\n"
                                                + toString(z));
                                int[] residueList = new int[] { y, z };
                                BitSet newBondSet = gumbo.bond.scanForResidues(
                                        bondHBSet, residueList);
                                General
                                        .showDebug("Found hydrogen bonds from one or both of them:\n"
                                                + gumbo.bond
                                                        .toString(newBondSet));
                            }
                            break;
                        }
                    } // end of loop 4
                } // end of loop 3
            } // end of loop 2
            if (iterationCount == 5) {
                General
                        .showCodeBug("Unlikely that the routine had to cycle more than 4 iterations, check code");
                return false;
            }
            /**
             * todo while ( Slist.size() > 4 ) { General.showCodeBug("Please
             * check this code before continueing"); int Scount = Slist.size(); //
             * remove the 2's, 3's and even 4's at random until the set is ok. //
             * first assemble the totals for each residue. int[] hbBondInS = new
             * int[S.size()]; // of course a waste of space. for (int
             * z=S.nextSetBit(0); z>=0; z=S.nextSetBit(z+1)) { for (int
             * zz=S.nextSetBit(z); zz>=0; zz=S.nextSetBit(zz+1)) { // only do
             * upper right triangle if ( z == zz ) { continue; } hbBondInS[ z ] +=
             * countHB[z][zz]; } } int killerCount = 2; while ( (Scount > 4) &&
             * (killerCount < 5)) { for (int z=S.nextSetBit(0); z>=0;
             * z=S.nextSetBit(z+1)) { if ( hbBondInS[ z ] == killerCount ) {
             * General.showDebug("Killing base from too large list:\n" +
             * toString(z)); S.clear(z); Scount--; if ( Scount < 4 ) { break; } } }
             * killerCount++; } if ( killerCount == 5 ) {
             * General.showError("Killer count of 5 is unusual, again just check
             * the code as it's applied to base:\n" + toString(x)); return
             * false; } }
             */
            if (Slist.size() == 1) {
                continue;
            }
            BitSet S = PrimitiveArray.toBitSet(Slist, -1);
            // Now operate on the non-temp sets.
            V.or(S);
            W.andNot(S);
            result.add(S);
        } // end of loop 1 over all selected bases Bx of the one selected
            // entry (using first model only)

        /**
         * entry code entity code A residue number A residue name A
         * 
         * entity code B residue number B residue name B
         * 
         * 1jj2 1 19 G 2 469 C
         */
        Relation rel = null;
        try {
            rel = new Relation("basepairsets", dbms);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
//        rel.addColumnForOverallOrder();
        rel.insertColumn("entry_id");
        for (int i = 0; i < 5; i++) { // assume never more than 5 (4 is the most seen so far)
            rel.insertColumn("entity code "     + i);
            rel.insertColumn("residue number "  + i);
            rel.insertColumn("residue name "    + i);
        }
        int resultSize = result.size();
        if ( resultSize == 0 ) {
            General.showOutput("No sets of co-planar bases found", resultSize);
        } else {
            rel.getNewRows(resultSize); 
            General.showOutput("Results contains number of sets of co-planar bases: "
                            + resultSize);
    //        General.showOutput("Results with internal residue numbering:\n"
    //                + Strings.toString(result, true));
            General.showOutput("Results:");
            for (int i = 0; i < resultSize; i++) {
                BitSet bs = (BitSet) result.get(i);
                General.showOutput(toString(bs));
                int j = 0;
                rel.setValue(i, 0, gumbo.entry.nameList[entryId[bs.nextSetBit(0)]]);
                for (int resIdx = bs.nextSetBit(0); resIdx >= 0; resIdx = bs.nextSetBit(resIdx + 1)) {
                    rel.setValue(i, 1 + (j * 3), Integer.toString(molId[resIdx] + 1));
                    rel.setValue(i, 2 + (j * 3), Integer.toString(number[resIdx]));
                    rel.setValue(i, 3 + (j * 3), nameList[resIdx]);
                    j++;
                }
            }            
            General.showOutput("Writing results to file: " + location);
            if ( ! rel.writeCsvFile(location, true)) {
                General.showError("Failed to write");
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the signed difference between the two residue numbers (b-a) or
     * Defs.NULL_INT if they're not in the same molecule/model/entry
     */
    public int getSeparation(int rid_a, int rid_b) {
        int result = Defs.NULL_INT;
        if (molId[rid_a] == molId[rid_b]) {
            result = number[rid_b] - number[rid_a];
        }
        // General.showOutput("Separation between residues is: " + result);
        // General.showOutput("For residues:\n" + toString(rid_a) + General.eol
        // + toString(rid_b));
        return result;
    }

    public float getMass(int j) {
        float result = 0f;
        BitSet bs = getAtoms(j);
        for (int i=bs.nextSetBit(0);i>=0;i=bs.nextSetBit(i+1)) {
            float mass = gumbo.atom.getMass(i);
            result += mass;            
        }
        return result;
    }

    /** Finds rid of neighbour in residue number distance.
     * Needs to be in same molecule.
     * @param res_rid
     * @param distance in number of residues.
     * @return
     */
    public int getNeighbour(int res_rid, int distance) {
        int resNumber = number[res_rid] + distance;        
        int molRid = molId[res_rid];
        BitSet residuesInSameMol = SQLSelect.selectCombinationBitSet(
                gumbo.dbms,
                mainRelation,
                Relation.DEFAULT_ATTRIBUTE_NUMBER,                                    SQLSelect.OPERATION_TYPE_EQUALS, new Integer(resNumber),
                Gumbo.DEFAULT_ATTRIBUTE_SET_MOL[RelationSet.RELATION_ID_COLUMN_NAME], SQLSelect.OPERATION_TYPE_EQUALS, new Integer(molRid),
                   SQLSelect.OPERATOR_AND, false );                
        return residuesInSameMol.nextSetBit(0);
    }

    public int getOrganic_ligands(BitSet allResSet) {
        // TODO Auto-generated method stub
        return 0;
    }
}
