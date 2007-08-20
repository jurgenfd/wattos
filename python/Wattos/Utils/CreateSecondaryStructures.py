python
import string


# Create generic secondary structure, based off a selection
def createSS(sel, sequence='ALA',repeat=1,terminal='C'):

    # Set selection
    selection = "%s and name %s" % (sel,terminal)

    # Pick atom for editing - interestingly only need to do this for the first addition
    cmd.edit(selection,None,None,None,pkresi=0,pkbond=0)

    # Array of residues
    seq = string.split(sequence,",")

    # Get residue numbering .. potential bug here if number is inconsistent.. (Only works at c-terminal)
    resi = int(cmd.get_model(sel).atom[0].resi) + 1
    
    # Loop and build new residues
    for i in range(1,repeat+1):
        for s in seq:
            print "residue[%i]: %s" % (i,s)
            editor.attach_amino_acid('pk1',s)

    # Loop and set phi,psi angles for new residues
    if terminal == 'N':
        resi -= repeat
        
#    for i in range(0,repeat+1):
#        for s in seq:
#            set_phipsi("resi %i" % (resi), phi,psi)
#            resi += 1

    # Remove extra OXT carboxylate atom (OXT1, OXT2 ?) .. fix as needed
    if terminal == 'C':
        cmd.remove("%s and name OXT" % sel)
    
    
def set_phipsi(sel,phi,psi):
    # Get atoms from selection
    atoms = cmd.get_model("byres ("+sel+")")

    # Loop through atoms in selection        
    for at in atoms.atom:
        if at.name == "N":
            # Check for a null chain id (some PDBs contain this) 
            unit_select = ""
            if not at.chain == "":
               unit_select = "chain "+str(at.chain)+" and "
    
            try:
                # Define residue selections     
                residue_def_prev = unit_select+'resi '+str(int(at.resi)-1)
                residue_def      = unit_select+'resi '+str(at.resi)        
#                print "residue_def_prev: [%s]" % residue_def_prev
#                print "residue_def     : [%s]" % residue_def
                if at.resn == "PRO":
                    print "Skipping setting phi for PRO"
                else:
                    old_phi = cmd.get_dihedral(residue_def_prev+' and name C',residue_def+' and name N', residue_def+' and name CA',residue_def+' and name C')        
                    print "Changing phi: "+at.resn+str(at.resi)+" from "+str(old_phi)+" to "+str(phi)        
                    cmd.set_dihedral(          residue_def_prev+' and name C',residue_def+' and name N', residue_def+' and name CA',residue_def+' and name C'      ,phi)
            except:
                print "Note skipping set of phi; this is normal for a N-terminal residue"
            try:
                residue_def      = unit_select+'resi '+str(at.resi)
                residue_def_next = unit_select+'resi '+str(int(at.resi)+1)
#                print "residue_def     : [%s]" % residue_def
#                print "residue_def_next: [%s]" % residue_def_next
                old_psi = cmd.get_dihedral(residue_def     +' and name N',residue_def+' and name CA',residue_def+' and name C', residue_def_next+' and name N')
                print "Changing psi: "+at.resn+str(at.resi)+" from "+str(old_psi)+" to "+str(psi)
                cmd.set_dihedral(          residue_def     +' and name N',residue_def+' and name CA',residue_def+' and name C', residue_def_next+' and name N',psi)
            except:
                print "Note skipping set of psi; this is normal for a C terminal residue"
                
python end
##############################################
#   Original Author:  Dan Kulp
#   Date  :  9/8/2005
#    MOdified by Jurgen F. Doreleijers
#    For Hamid Eghbalnia               
#
#############################################
# Call in window like : 
# @C:\Documents and Settings\jurgen.WHELK.000\workspace\Wattos\python\Wattos\Utils\CreateSecondaryStructures.py


cmd.delete("all")
# Creates residue TWO
editor.attach_amino_acid('pk1','SER') 
# Creates residue ONE
createSS('resi 2', sequence='MET',terminal='N')  
createSS('resi 2', sequence='ALA')
createSS('resi 3', sequence='SER')
createSS('resi 4', sequence='GLY')
createSS('resi 5', sequence='THR')
createSS('resi 6', sequence='PRO')
createSS('resi 7', sequence='TRP')

set_phipsi('resi 1',-57,-47)
set_phipsi('resi 2',-57,-47)
set_phipsi('resi 3',-57,-47)
set_phipsi('resi 4',-57,-47)
set_phipsi('resi 5',-57,-47)
set_phipsi('resi 6',-57,-47)
set_phipsi('resi 7',-57,-47)
set_phipsi('resi 8',-57,-47)
cmd.select('sele','all') # Select all atoms
save C:/Documents and Settings/jurgen.WHELK.000/Desktop/sele.pdb,(sele) # Write selection

