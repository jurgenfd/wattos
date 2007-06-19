package Wattos.Database.Indices;

/*
 * Created on June 18, 2003, 11:02 AM
 */

import Wattos.Utils.Comparators.ComparatorShortIntPair;
import Wattos.Utils.ObjectIntPair;
import java.io.*;
import java.util.*;
import cern.colt.list.*;
import Wattos.Utils.*;
import Wattos.Database.*;
/**
 * @author Jurgen F. Doreleijers
 */
public class IndexSortedShort extends Index implements Serializable {
 
    private static final long serialVersionUID = -1207795172754062330L;        
    
    short[] values = null;
//    public int[] rids   = null;
    
    /** Creates a new instance of IndexStringToMany */
    public IndexSortedShort() {
        indexType = INDEX_TYPE_SORTED;
    }
        
    
    /**
     * @param show_elements
     * @param show_keys
     */    
    public String toString(boolean show_elements, boolean show_keys ) {
        StringBuffer sb = new StringBuffer();
        sb.append( "Number of elements is: ");
        sb.append( values.length );
        if ( show_elements ) {
            sb.append( General.eol);
            for ( int r=0; r<values.length; r++ ) {
                sb.append( values[r] );
                sb.append( ' ' );
                sb.append( rids[r] );
                sb.append( General.eol );
            }            
        }
        return sb.toString();
    }
        
        
    public boolean updateIndex(Relation relation, String columnLabel) { 
        Object object = relation.getColumn( columnLabel );
        if ( ! (object instanceof short[]) ) {
            General.showError( "Object should be of type short[] but found: " + object.getClass().getName());
            return false;
        }
        short[] column = (short[]) object;
    
        int sizeRows = relation.sizeRows;
        
        /** Approach may be optimized by not using objects but right now, no
         *time to code it
         */
        // Fill arrays
        ArrayList list = new ArrayList( sizeRows ); /** Use capacity size for efficiency */        

        BitSet used = relation.used;
        for (int r=used.nextSetBit(0); r>=0; r=used.nextSetBit(r+1)) {

            Short v = new Short( column[r] );
            //General.showDebug("in updateIndex found at row: " + r + " an integer:" + v);
            
            ObjectIntPair pair = new ObjectIntPair( v, r );
            if ( pair == null ) {
                General.showError("Code bug 2 found in updateIndex.");
            }
                
            list.add( pair );
        }
        if ( list.size() != sizeRows ) {
            General.showError("Code bug found in updateIndex sizes don't match.");
            General.showError("rows in relation found are: " + sizeRows + " and: " + list.size() );
            return false;
        }
        
        // Use a fast comparator
        Comparator comparator = new ComparatorShortIntPair();
        try {
            // Sorts on values column only.
            Collections.sort(list, comparator );
        } catch ( ClassCastException e ) {
            General.showThrowable(e);
            return false;
        }
        
        // Transfer back to original 
        values = new short[sizeRows];
        rids   = new int[sizeRows];
        
        for ( int r=sizeRows-1; r > -1; r-- ) {
            ObjectIntPair pair = (ObjectIntPair) list.get(r);
            values[r]   = ((Short) pair.o).shortValue();
            rids[r]     = pair.i;
        }            
        return true;
    }

    /** 
     * @param o Value to look for. Needs to be an encapsulated data type like Integer for int.
     * @param listType Selects the output object as a BitSet, int[], or so.
     * @return null if the key is not exactly met.
     * null in case of error: if the list is not present or not sorted before.
     */
    public Object getRidList( Object o, int listType) {
        if ( values == null ) {            
            General.showCodeBug("in getRidList no values present in index: "+ Strings.getClassName(this));
            return null;
        }
        
        BitSet          resultBitSet    = new BitSet();        
        IntArrayList    resultList      = new IntArrayList();
        
        short key = ((Short) o).shortValue();
        int idxFirst = Arrays.binarySearch(values, key);
        if ( idxFirst < 0 ) {
            //General.showDebug("Returning empty rid list as no records for key: " + key);
            if ( listType == Index.LIST_TYPE_BITSET ) {
                return resultBitSet;
            } else if ( listType == Index.LIST_TYPE_INT_ARRAY_LIST ) {
                return resultList;
            }
        }
        //General.showDebug("Found first idx: " + idxFirst);
        
        // Scan backward
        int idx = idxFirst;
        int idxBegin = -1;            
        while ( idx > -1 ) {
            idxBegin = idx;
            //General.showDebug("found begin idx: " + idx);
            idx--;
            if ( idx < 0 || values[idx] != key ) {
                idx = -1;
            }
        }
        // Scan forward
        idx = idxFirst;
        int idxEnd = -1;            
        while ( idx > -1 ) {
            idxEnd = idx;
            //General.showDebug("found end idx: " + idx);
            idx++;
            if ( idx >= values.length || values[idx] != key ) {
                idx = -1;
            }
        }
        
        if ( listType == Index.LIST_TYPE_INT_ARRAY_LIST ) {
            int[] elements = new int[idxEnd-idxBegin+1];
            int j = 0;
            for (int i=idxBegin;i<=idxEnd;i++ ) {                                
                elements[j] = rids[i];
                j++;
            }
            resultList.elements(elements);
            return resultList;
        }
        
        if ( listType == Index.LIST_TYPE_BITSET ) {            
            // Only set those that need setting.
            for (int i=idxBegin;i<=idxEnd;i++ ) {                
                resultBitSet.set( rids[i] );
            }
            return resultBitSet;
        }
         
        General.showError("list type requested is not supported: " + Index.list_type_names[listType] );
        return null;
    }
    
    /** 
     * @param searchKeys A BitSet used as value where the position in the list of the set bits are interpretted as the 
     *short values for which the column needs to be searched and the row ids to be returned for. It will nest 
     *calls to getRidList for individual values (and ranges later on for speed).
     * @return null if the key is not exactly met.
     * null if the list is not present or not sorted before.
     */
    public Object getRidList( BitSet searchKeys ) {
        if ( values == null ) {            
            General.showError("Code bug found in getRidList no values present in index: "+ Strings.getClassName(this));
            return null;
        }        
        
        BitSet result = new BitSet( values.length );
        int i = searchKeys.nextSetBit(0);
        // This loop needs to be replaced by a more sophisticated algorithm
        // that is more efficient for selections on ranges like {10..20} which
        // require only 1 lookup/call to getRidList i.s.o. 11 or so.
        while ( i > -1 ) {
            //General.showDebug("checking in child for parent id: " + i);
            BitSet temp = (BitSet) getRidList( new Short((short)i), LIST_TYPE_BITSET );
            // Make sure the result wasn't empty in which case the function returned null.
            if ( temp != null ) {
                result.or( temp ); 
            }
            i++;
            i = searchKeys.nextSetBit(i);
        }
        return result;
    }
    
    /** This Method should implement a structured query like:
     *SELECT * FROM this_table
     *WHERE this_column == 7
     *In this example the following arguments need to be given:
     *operation: OPERATION_TYPE_EQUALS
     *threshold: 7.3 encapsulated in a Float object
     *For string operations sorted lists are less useful in general
     *but still possible with this method signature using operation type:
     *OPERATION_TYPE_EQUALS_REGULAR_EXPRESSION.
     */
    public BitSet getRidListForQuery( int operator, Object value ) {
        switch ( operator ) {
            case SQLSelect.OPERATION_TYPE_EQUALS: {
                return (BitSet) getRidList( value, LIST_TYPE_BITSET);
            }
            default: {
                General.showCodeBug("Operation not supported in getRidListForQuery: " + SQLSelect.operation_type_names[ operator ]);
                return null;
            }
        }
    }    
}
