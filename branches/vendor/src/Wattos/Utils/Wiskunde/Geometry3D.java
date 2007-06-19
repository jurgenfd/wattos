package Wattos.Utils.Wiskunde;

import Wattos.Database.*;
import Wattos.Utils.*;

/**
 *Static methods for calculating distances, angles, dihedral angles, areas, 
 * rotations, crossproducts, etc. in 3D.
 *
 * @author  jurgen
 */
public class Geometry3D extends Geometry {
        
    /**
     * For the cases where you don't want to use a matrix to do the rotation
     * This routine will rotate a number of points around the x,y, or z axis.
     *Rewrite for efficiency if more than a few points need to be rotated.
     */
    public static void rotatePoints(int axis_id, double angle, double[][] points) {
        double xx, yy, zz;
        for(int i=0; i<points.length; i++) {
            switch(axis_id) {
                case X_AXIS_ID: {
                    yy=points[i][1];
                    zz=points[i][2];
                    points[i][1]=yy*Math.cos(angle)-zz*Math.sin(angle);
                    points[i][2]=yy*Math.sin(angle)+zz*Math.cos(angle);
                    break;
                }
                case Y_AXIS_ID: {
                    zz=points[i][2];
                    xx=points[i][0];
                    points[i][2]=zz*Math.cos(angle)-xx*Math.sin(angle);
                    points[i][0]=zz*Math.sin(angle)+xx*Math.cos(angle);
                    break;
                }
                case Z_AXIS_ID: {
                    xx=points[i][0];
                    yy=points[i][1];
                    points[i][0]=xx*Math.cos(angle)-yy*Math.sin(angle);
                    points[i][1]=yy*Math.cos(angle)+xx*Math.sin(angle);
                    break;
                }
            }
        }
    }
    
    /**
     *Calculates the torsionangle between 4 points around the 2nd and 3rd point.
     *Adapted from the routine C program GENC by JFD based on routines
     *by Chang-Shung Tung and Eugene S. Carter (T10, Los Alamos 1994).
     */
    public static double calcDihedral( 
            double[] vector_a, double[] vector_b, 
            double[] vector_c, double[] vector_d ) {
//        final int ATOM_COUNT = 4;

        double dx, dy, dz, dxy, phi1, phi2, phi3, theta, tors;  
        
        /** Take a copy so the original is saved 
         */
        double v_0[] = (double[]) vector_a.clone();
        double v_1[] = (double[]) vector_b.clone();
        double v_2[] = (double[]) vector_c.clone();
        double v_3[] = (double[]) vector_d.clone();        
        
        double[][] vertices = new double[][] { v_0, v_1, v_2, v_3 };
        // Get the vector between the two middle points.
        double d[] = getVector( v_2, v_1 );
        dx = d[0];
        dy = d[1];
        dz = d[2]; 
        
        dxy = Math.sqrt(dx*dx + dy*dy);
        phi1  = Math.atan2(dy , dx + DISTANCE_EPSILON);
        theta = Math.atan2(dxy, dz + DISTANCE_EPSILON);
        rotatePoints(Z_AXIS_ID, -phi1,  vertices);
        rotatePoints(Y_AXIS_ID, -theta, vertices);
        phi2 = Math.atan2( v_0[1]-v_1[1], v_0[0]-v_1[0] + DISTANCE_EPSILON);
        phi3 = Math.atan2( v_3[1]-v_2[1], v_3[0]-v_2[0] + DISTANCE_EPSILON);
        tors = toMinusPIPlusPIRange( phi2-phi3 );
        /**
        General.showOutput("vector_a: " + toString( vector_a));
        General.showOutput("vector_b: " + toString( vector_b));
        General.showOutput("vector_c: " + toString( vector_c));
        General.showOutput("vector_d: " + toString( vector_d));
        General.showOutput("v_0: " + toString( v_0));
        General.showOutput("v_1: " + toString( v_1));
        General.showOutput("v_2: " + toString( v_2));
        General.showOutput("v_3: " + toString( v_3));
        General.showOutput("angles: theta    : " +  CF*theta );
        General.showOutput("angles: phi1     : " +  CF*phi1 );
        General.showOutput("angles: phi2     : " +  CF*phi2 );
        General.showOutput("angles: phi3     : " +  CF*phi3 );
        General.showOutput("angles: tors     : " +  CF*tors);
        */
        return tors;
    }   

    /**
     *Result is in radians. The formula is:
     *phi = arcsin( |x outproduct y|/|x|*|y| )
     */
    public static double calcAngle( double[] x, double[] y  ) {    
        double dxy = getVectorNorm(vectorCrossProduct( x, y ));
        double dx = getVectorNorm(x);
        double dy = getVectorNorm(y);        
        return Math.asin(dxy/(dx*dy));
    }
    
    /**
     *The second point is connected to a and c.
     *Result is in radians.
     */
    public static double calcAngle( double[] vector_a, double[] vector_b, double[] vector_c ) {    
        double[] v1 = getVector( vector_b, vector_a );
        double[] v2 = getVector( vector_b, vector_c );        
        normalizeVector(v1);
        normalizeVector(v2);
        double dp = vectorDotProduct(v1, v2);
        return Math.acos(dp);
    }
    

    public static double calcArea( double[] vector_a, double[] vector_b, double[] vector_c ) {    
        double result = Defs.NULL_DOUBLE;
        General.showError("Still to adapt from c code");
        /**
        double are, d[dim+1], e[dim+1], f[dim+1];
          for (i=1;i<=dim;i++) {
            d[i] = b[i] - a[i];
            e[i] = c[i] - a[i];
          }
          f[1] = d[2]*e[3] - e[2]*d[3];
          f[2] = d[3]*e[1] - e[3]*d[1];
          f[3] = d[1]*e[2] - e[1]*d[2];
          are = sqrt( f[1]*f[1] + f[2]*f[2] + f[3]*f[3] ) / 2.0;
          return (are);        
         */
        return result;
    }
    
    /**
     *Cross product is easiest to code for each dimension separately.
     */
    public static double[] vectorCrossProduct( double[] a, double[] b ) {    
        double r[] = new double[DIM];
        r[0] = a[1]*b[2] - a[2]*b[1];
        r[1] = a[2]*b[0] - a[0]*b[2];
        r[2] = a[0]*b[1] - a[1]*b[0];        
        return r;
    }
        
    /** Calculates volume of tetrahedron formed by a,b,c and d.
     * Alternativly it can be used to calculate a third of the det.of 3x3 matrix 
     * NN: negative volumes are possible
     */
    public static double volumeTetrahedron( 
            double[] vector_a, double[] vector_b, 
            double[] vector_c, double[] vector_d ) {
        General.showError("Check this routine.");
        double[] aa = getVector(vector_a, vector_b);
        double[] bb = getVector(vector_a, vector_c);
        double[] cc = getVector(vector_a, vector_d);
        double[] out = vectorCrossProduct(bb,cc);
        return vectorDotProduct(aa,out)/3.0;
    }        
}
