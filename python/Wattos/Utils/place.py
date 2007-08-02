#A* -------------------------------------------------------------------
#B* This file contains source code for the PyMOL computer program
#C* copyright 1998-2000 by Warren Lyford Delano of DeLano Scientific. 
#D* -------------------------------------------------------------------
#E* It is unlawful to modify or remove this copyright notice.
#F* -------------------------------------------------------------------
#G* Please see the accompanying LICENSE file for further information. 
#H* -------------------------------------------------------------------
#I* Additional authors of this source file include:
#-* 
#-* 
#-*
#Z* -------------------------------------------------------------------
#
#
#

import bond_amber

from chempy.cpv import *
from chempy import feedback

TET_TAN = 1.41
TRI_TAN = 1.732

#------------------------------------------------------------------------------
"""JFD: finds none or two atoms with one of them being in the known list
and the other one also has known coordinates and is preferably not
a hydrogen.
"""
def find_known_secondary(model,anchor,known_list):
    at = model.atom[anchor] #JFD unused
    h_list = []
    for id in known_list: #JFD id is the atom idx of a atom with known coordinates
        for b in model.bond[id]: #JFD for each of it's bonds.
            atx2 = b.index[0] #JFD atx2 is the idx of another atom with coordinates preferably not a hydrogen
            if atx2 == id:
                atx2 = b.index[1]
            if atx2 != anchor: #JFD another bonded atom, not achor
                at2 = model.atom[atx2]
                if at2.has('coord'):
                    if at2.symbol != 'H':
                        return (id,atx2) #JFD returns 2 atoms
                    else:
                        h_list.append((id,atx2))
    if len(h_list): # only return hydrogen as a last resort
        return h_list[0] #JFD returns 2 atoms
    return None

#------------------------------------------------------------------------------
def simple_unknowns(model,bondfield=bond_amber):
    print "JFD notes"
    if feedback['actions']:
        print " "+str(__name__)+": placing unknowns..."
    # this can be used to build hydrogens and would robably work for
    # acyclic carbons as well
    if str(model.__class__) != 'chempy.models.Connected':
        raise ValueError('model is not a "Connected" model object')
    if model.nAtom: #JFD: number of atoms in model
        if not model.index:
            model.update_index()
        idx = model.index
        last_count = -1
        while 1:
            need = [ [], [], [], [] ]#JFD: 4 types of needed atoms
            bnd_len = bondfield.length #JFD: bond lenghts from amber
    # find known atoms with missing neighbors, and keep track of the neighbors
            for a in model.atom:
                if a.has('coord'):
                    miss = [] #JFD: list of atoms with missing coordinates bonded to this atom
                    know = []
                    atx1 = idx[id(a)]
                    bnd = model.bond[atx1] #JFD: bond list for this atom
                    for b in bnd:
                        atx2 = b.index[0] #JFD: bonded atom idx
                        if atx2 == atx1:
                            atx2 = b.index[1]
                        at2 = model.atom[atx2] #JFD: bonded atom
                        if not at2.has('coord'):
                            miss.append(atx2)
                        else:
                            know.append(atx2)
                    c = len(miss)
                    if c:
                        # append the missing atom idx list to the right needed.
                        # e.g. if 2 atoms are missing it will be added to the need[1]
                        need[c-1].append((atx1,miss,know))

            for a in need[0]: # missing only one atom
                atx1 = a[0] #JFD atom idx of atom with known coordinates
                at1 = model.atom[atx1]
                atx2 = a[1][0] #JFD FIRST atom idx with missing coor.
                at2 = model.atom[atx2]
                know = a[2] #JFD known atom idx list
                #JFD Is the known coordinate atom's type in a nonlinear arrangement?
                # E.g. OW is nonlinear but others are: tetrahedral and planer
                # dictionaries are used for fast lookup
                # Why tetrahedral and nonlinear are distinguished is unclear to me now.
                if bondfield.nonlinear.has_key(at1.text_type): 
                    near = find_known_secondary(model,atx1,know)
                    if near:
                        at3 = model.atom[near[0]]
                        if bondfield.planer.has_key(at3.text_type): # Phenolic hydrogens, etc.
                            at4 = model.atom[near[1]]
                            # At this point atoms could be in a PHE:
                            #    1 CD1 the anchor            <-  d2   --
                            #    2 HD1 the unknown          CG----CD1<d1-CE1
                            #    3 CE1 the secondary with          |
                            #    4 CG  helper both known.          |p2,v?
                            #                                      HD1
                            d1 = sub(at1.coord,at3.coord) 
                            p0 = normalize(d1)
                            d2 = sub(at4.coord,at3.coord)
                            p1 = normalize(cross_product(d2,p0)) # Vector perpendicular to the plane of the 3 known atoms.
                            p2 = normalize(cross_product(p0,p1)) # Vector from CD1 to HD1
                            v = scale(p2,TRI_TAN)
                            v = normalize(add(p0,v))
                            at2.coord = add(at1.coord,scale(v,
                                bnd_len[(at1.text_type,at2.text_type)]))
                        else: # Ser, Cys, Thr hydroxyl hydrogens
                            at4 = model.atom[near[1]] # Can be moved out of the if/else
                            # At this point atoms could be in a CYS:
                            #    1 SG Anchor           4      3      1     2  
                            #    2 HG The unknown     CA--d2->CB-----SG----HG
                            #    3 CB the secondary with          
                            #    4 CA helper both known.          
                            d2 = sub(at3.coord,at4.coord) # same direction as CA-CB
                            v = normalize(d2)
                            at2.coord = add(at1.coord,scale(v,
                                bnd_len[(at1.text_type,at2.text_type)]))
                    elif len(know): #JFD when no known secondaries with it's neighbours known could be found but there are known
                        d2 = [1.0,0,0] # Just assumed to be something.
                        at3 = model.atom[know[0]]
                            # At this point atoms could be in a PHE:
                            #    1 CD1 the anchor             
                            #    2 HD1 the unknown                 CD1<p0-CE1
                            #    3 CE1 the secondary with          |
                            #                                      |p2,v?
                            #                                      HD1                        
                        p0 = normalize(sub(at1.coord,at3))
                        p1 = normalize(cross_product(d2,p0))  # if p0 is same as assumed d2 this will give the zero vector
                        v = scale(p1,TET_TAN)
                        v = normalize(add(p0,v))
                        at2.coord = add(at1.coord,scale(v,
                                bnd_len[(at1.text_type,at2.text_type)])) # above condition will place the HD1 on top of CD1.
                    else:
                        # placed on a random position at bondlength if no attached atom was known.
                        at2.coord = random_sphere(at1.coord,  
                             bnd_len[(at1.text_type,at2.text_type)])
                #JFD the anchor was not nonlinear (must have been tetrahedral or planer)
                #JFD extending the chain in the same or random direction
                #JFD clearly this isn't normally done for say a CB todo and a CA,N known
                #JFD don't understand the algorithm.
                #JFD Or is it done and then optimized
                elif len(know): # linear sum...amide, tbu, etc
                    v = [0.0,0.0,0.0]
                    for b in know:
                        d = sub(at1.coord,model.atom[b].coord)
                        v = add(v,normalize(d))
                    v = normalize(v)
                    at2.coord = add(at1.coord,scale(v,
                         bnd_len[(at1.text_type,at2.text_type)]))
                else:
                    at2.coord = random_sphere(at1.coord,
                          bnd_len[(at1.text_type,at2.text_type)])

            for a in need[1]: # missing two atoms
                atx1 = a[0]
                at1 = model.atom[atx1]
                atx2 = a[1][0]
                at2 = model.atom[atx2]
                know = a[2]
                if bondfield.planer.has_key(at1.text_type): # guanido, etc
                    near = find_known_secondary(model,atx1,know)
                    if near: # 1-4 present
                        at3 = model.atom[near[0]]
                        at4 = model.atom[near[1]]
                        d1 = sub(at1.coord,at3.coord)
                        p0 = normalize(d1)
                        d2 = sub(at4.coord,at3.coord)
                        p1 = normalize(cross_product(d2,p0))
                        p2 = normalize(cross_product(p0,p1))
                        v = scale(p2,TRI_TAN)
                        v = normalize(add(p0,v))
                        at2.coord = add(at1.coord,scale(v,
                          bnd_len[(at1.text_type,at2.text_type)]))                                                         
                        at2 = model.atom[a[1][1]]
                        v = scale(p2,-TRI_TAN)
                        v = normalize(add(p0,v))
                        at2.coord = add(at1.coord,scale(v,
                          bnd_len[(at1.text_type,at2.text_type)]))
                    elif len(know): # no 1-4 found
                        d2 = [1.0,0,0]
                        at3 = model.atom[know[0]]
                        d1 = sub(at1.coord,at3.coord)
                        p0 = normalize(d1)                  
                        p1 = normalize(cross_product(d2,p0))
                        p2 = normalize(cross_product(p0,p1))
                        v = scale(p2,TRI_TAN)
                        v = normalize(add(p0,v))
                        at2.coord = add(at1.coord,scale(v,
                          bnd_len[(at1.text_type,at2.text_type)]))
                        at2 = model.atom[a[1][1]]
                        v = scale(p2,-TRI_TAN)
                        v = normalize(add(p0,v))
                        at2.coord = add(at1.coord,scale(v,
                          bnd_len[(at1.text_type,at2.text_type)]))
                    else: #JFD Doesn't calculate the second!
                        at2.coord = random_sphere(at1.coord,
                            bnd_len[(at1.text_type,at2.text_type)])
                elif len(know)>=2: # simple tetrahedral
                    # At this point atoms could be in a CYS:
                    #    1 CB Anchor           3       1      4  
                    #    2 HB2 The unknown     CA--d1--CB-d2--SG
                    #    3 CA                          |
                    #    4 SG                          HB3
                    at3 = model.atom[know[0]]
                    at4 = model.atom[know[1]]
                    v = [0.0,0.0,0.0]#useless
                    d1 = sub(at1.coord,at3.coord)
                    d2 = sub(at1.coord,at4.coord)
                    v = add(normalize(d1),normalize(d2)) #sum of d1 and d2
                    p0 = normalize(v)
                    p1 = normalize(cross_product(d2,p0))
                    v = scale(p1,TET_TAN)
                    v = normalize(add(p0,v))
                    at2.coord = add(at1.coord,scale(v,
                            bnd_len[(at1.text_type,at2.text_type)]))
                    at2 = model.atom[a[1][1]]               
                    v = scale(p1,-TET_TAN)
                    v = normalize(add(p0,v))
                    at2.coord = add(at1.coord,scale(v,
                            bnd_len[(at1.text_type,at2.text_type)]))
                else:
                    if len(know): # sulfonamide? 
                        d2 = [1.0,0,0]
                        at3 = model.atom[know[0]]
                        d1 = sub(at1.coord,at3.coord)
                        p0 = normalize(d1)                                    
                        p1 = normalize(cross_product(d2,p0))
                        v = scale(p1,TET_TAN)
                        v = normalize(add(p0,v))
                        at2.coord = add(at1.coord,scale(v,
                            bnd_len[(at1.text_type,at2.text_type)]))
                    else: # blind
                        at2.coord = random_sphere(at1.coord,
                            bnd_len[(at1.text_type,at2.text_type)])
                    at4=at2 #JFD not really needed to add second atom now right? It can be done in next round.
                    at2=model.atom[a[1][1]]
                    v = [0.0,0.0,0.0]
                    d1 = sub(at1.coord,at3.coord)
                    d2 = sub(at1.coord,at4.coord)
                    v = add(normalize(d1),normalize(d2))
                    p0 = normalize(v)
                    p1 = normalize(cross_product(d2,p0))
                    v = scale(p1,TET_TAN)
                    v = normalize(add(p0,v))
                    at2.coord = add(at1.coord,scale(v,
                        bnd_len[(at1.text_type,at2.text_type)]))

            for a in need[2]: # missing 3 atoms
                atx1 = a[0]
                at1 = model.atom[atx1]
                atx2 = a[1][0]
                at2 = model.atom[atx2]
                know = a[2]
                near = find_known_secondary(model,atx1,know)
                if near: # 1-4 present
                    at3 = model.atom[near[0]]
                    at4 = model.atom[near[1]]
                    d1 = sub(at1.coord,at3.coord)
                    p0 = normalize(d1)
                    d2 = sub(at4.coord,at3.coord)
                    p1 = normalize(cross_product(d2,p0))
                    p2 = normalize(cross_product(p0,p1))
                    v = scale(p2,-TET_TAN)
                    v = normalize(add(p0,v))
                    at2.coord = add(at1.coord,scale(v,
                          bnd_len[(at1.text_type,at2.text_type)]))                                                         
                    at4 = at2 # Leave the others for later.
                    at2 = model.atom[a[1][1]]
                    d1 = sub(at1.coord,at3.coord)
                    d2 = sub(at1.coord,at4.coord)
                    v = add(normalize(d1),normalize(d2))
                    p0 = normalize(v)
                    p1 = normalize(cross_product(d2,p0))
                    v = scale(p1,TET_TAN)
                    v = normalize(add(p0,v))
                    at2.coord = add(at1.coord,scale(v,
                            bnd_len[(at1.text_type,at2.text_type)]))
                    at2 = model.atom[a[1][2]]               
                    v = scale(p1,-TET_TAN)
                    v = normalize(add(p0,v))
                    at2.coord = add(at1.coord,scale(v,
                            bnd_len[(at1.text_type,at2.text_type)]))
                elif len(know): # fall-back
                    d2 = [1.0,0,0]
                    at3 = model.atom[know[0]]                  
                    p1 = normalize(cross_product(d2,p0)) # p0 isn't set yet; BUG?
                    v = scale(p1,TET_TAN)
                    v = normalize(add(p0,v))
                    at2.coord = add(at1.coord,scale(v,
                        bnd_len[(at1.text_type,at2.text_type)]))
                    at4=at2
                    at2=model.atom[a[1][1]]
                    v = [0.0,0.0,0.0]
                    d1 = sub(at1.coord,at3.coord)
                    d2 = sub(at1.coord,at4.coord)
                    v = add(normalize(d1),normalize(d2))
                    p0 = normalize(v)
                    p1 = normalize(cross_product(d2,p0))
                    v = scale(p1,TET_TAN)
                    v = normalize(add(p0,v))
                    at2.coord = add(at1.coord,scale(v,
                        bnd_len[(at1.text_type,at2.text_type)]))
                    at2=model.atom[a[1][2]]
                    v = scale(p1,-TET_TAN)
                    v = normalize(add(p0,v))
                    at2.coord = add(at1.coord,scale(v,
                        bnd_len[(at1.text_type,at2.text_type)]))
                else: # worst case: add one and get rest next time around
                    at2.coord=random_sphere(at2.coord,
                        bnd_len[(at1.text_type,at2.text_type)])

            for a in need[3]: # missing 4 atoms
                atx1 = a[0]
                at1 = model.atom[atx1]
                atx2 = a[1][0]
                at2 = model.atom[atx2]
                # add coordinate and get the rest next time around
                at2.coord=random_sphere(at2.coord,
                     bnd_len[(at1.text_type,at2.text_type)])

            #JFD: iterate until no atoms are placed anymore.
            c = 0
            for a in model.atom:
                if not a.has('coord'):
                    c = c + 1
            if not c:
                break;
            if c==last_count:
                break;
            last_count = c




