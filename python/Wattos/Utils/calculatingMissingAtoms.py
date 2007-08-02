from cpv import *

TET_TAN = 1.41
TRI_TAN = 1.732

def calcTetrahedral2Missing():
    #------------------------------------------------------------------------------
    # At this point atoms could be in a CYS:
    #    1 CB Anchor           3       1      4  
    #    2 HB2 The unknown     CA--d1--CB-d2--SG
    #    3 CA                          |
    #    4 SG                          HB3
    coor1 = [ 1, 1 , 1 ] # central coordinate in a cube of 2 unit
    coor3 = [ 0,0,2 ] 
    coor4 = [ 2,0,0 ] 
    d1 = sub(coor1,coor3)
    d2 = sub(coor1,coor4)
    print "d1", d1
    print "d2", d2
    v = add(normalize(d1),normalize(d2)) #sum of d1 and d2
    print "v", v
    p0 = normalize(v)
    p1 = normalize(cross_product(d2,p0))
    print "p0", p0
    print "p1", p1
    for factor in ( TET_TAN, -TET_TAN):
        w = scale(p1,factor)
        print "w", w
        x = normalize(add(p0,w))
        print "x", x
        l = length(d1)
        print "l", l
        coor2 = add(coor1,scale(x,l))
        print "expected coor2 to be 2,2,2 or 0,2,0"
        print "coor2", coor2


def calcPlanar1Missing():
    coor1 = [ 0,0,0 ] 
    coor3 = [ 1,0,0 ] 
    coor4 = [-1,1,0 ] 
    # At this point atoms could be in a PHE:
    #    1 CD1 the anchor           CG<-  d2   --
    #    2 HD1 the unknown            \\--CD1<d1-CE1        ^y axis
    #    3 CE1 the secondary with          |                |
    #    4 CG  helper both known.          |p2,v?           -----> x axis
    #                                      HD1              
    d1 = sub(coor1,coor3) 
    p0 = normalize(d1)
    d2 = sub(coor4,coor3)
    p1 = normalize(cross_product(d2,p0)) # Vector perpendicular to the plane of the 3 known atoms.
    p2 = normalize(cross_product(p0,p1)) # y axis
    print "p2", p2
    v = scale(p2,TRI_TAN)#JFD makes sure the angles ar 120 degrees
    v = normalize(add(p0,v))#JFD
    l = length(d1)
    coor2 = add(coor1,scale(v,l))
    print "expected coor2 to be -0.5, 0.866, 0.0"
    print "coor2", coor2

def calcPlanar2Missing():
    #------------------------------------------------------------------------------
    # At this point atoms could be in a ASN:
    #    1 ND2  Anchor      4    3      1 
    #    2 HD21 The unknown CB--CG--d1--ND2---HD21
    #    3 CG                           |
    #    4 CB                           HD22    
    coor1 = [ 0,0,0 ] # central coordinate in a cube of 2 unit
    coor3 = [ 1,0,0 ] 
    coor4 = [ 1,1,0 ] 
    d1 = sub(coor1,coor3)
    p0 = normalize(d1) # direction of two new atoms
    d2 = sub(coor4,coor3)
    p1 = normalize(cross_product(d2,p0)) # normal of plane of all atoms
    p2 = normalize(cross_product(p0,p1)) # deviation from straight forward.
    l = length(d1)
    for factor in ( TRI_TAN, -TRI_TAN):    
        v = scale(p2,factor)
        v = normalize(add(p0,v))
        coor2 = add(coor1,scale(v,l))
        print "expected coor2 to be ?,?"
        print "coor2", coor2
        
def calcTetrahedral3Missing():
    #------------------------------------------------------------------------------
    # At this point atoms could be in a ALA:
    #    1 CB Anchor        4  3       1         
    #    2 HB2 The unknown  N--CA--d1--CB---HB123
    #    3 CA                          |
    #    4 SG                          HB3
# missing 3 atoms
    coor1 = [ 0,0,0 ] # central coordinate in a cube of 2 unit
    coor3 = [ 1,0,0 ] 
    coor4 = [ 1,1,0 ] 
    d1 = sub(coor1,coor3)
    p0 = normalize(d1)
    l = length(d1)    
    d2 = sub(coor4,coor3)
    p1 = normalize(cross_product(d2,p0))
    p2 = normalize(cross_product(p0,p1))
    v = scale(p2,-TET_TAN)
    v = normalize(add(p0,v))
    coor2 = add(coor1,scale(v,l))                                                         
    print "expected coor2 to be ?,?"
    print "coor2", coor2    
    coor4 = coor2 # Use the new atom so we can stay out of the way of it?
    d1 = sub(coor1,coor3)
    d2 = sub(coor1,coor4)
    v = add(normalize(d1),normalize(d2))
    p0 = normalize(v)
    p1 = normalize(cross_product(d2,p0))
    for factor in ( TET_TAN, -TET_TAN):    
        v = scale(p1,factor)
        v = normalize(add(p0,v))
        coor2 = add(coor1,scale(v,l))   
        print "expected coor2 to be ?,?"
        print "coor2", coor2

#calcPlanar()    
#calcTetrahedral3Missing()
#calcPlanar2Missing()
calcPlanar1Missing()