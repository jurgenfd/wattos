package Wattos.Soup;

import java.util.Comparator;

/**
 * @author Jurgen F. Doreleijers
 *@see Atom#compareAuthor
 */
public class ComparatorAuthorAtomWithoutEntry implements Comparator {
    
    public Gumbo gumbo;
    /** Creates a new instance of ComparatorAtom */
    public ComparatorAuthorAtomWithoutEntry(Gumbo gumbo) {
        this.gumbo = gumbo;        
    }
    
    /** Just do comparison on first object with all but entry info. */
    public int compare(Object o1, Object o2) {               
        return gumbo.atom.compareAuthor( ((int[])  o1)[0], ((int[])  o2)[0], true );
    }
}
