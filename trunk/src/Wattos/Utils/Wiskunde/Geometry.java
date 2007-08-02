/*
 * Geometry.java
 *
 * Created on August 17, 2005, 9:59 AM
 */

package Wattos.Utils.Wiskunde;

import Wattos.Utils.General;

import com.braju.format.*;              // printf equivalent

/**
 *Static methods for calculating distances, angles, vector products etc.
 * @author  jurgen
 */
public class Geometry {
    /** Make the axes explicitely
     */
    public static final int X_AXIS_ID = 0;
    public static final int Y_AXIS_ID = 1;
    public static final int Z_AXIS_ID = 2;    
    /** How many dimensions do we have here on earth for objects (besides time and relativity)
     * This value might be overriden by specializing classes.
     */
    public static final int DIM = 3;
    /** Conversion factor from rad to degrees. Use the fact that the acos of 0 is
     *equal to 90 degrees but the value is returned in radians. CF times a value in
     *radians gives the value in degrees.
     */
    public static final double CF = 90d/Math.acos(0);
    /** Inverse of CF.
     */
    public static final double CFI = Math.acos(0)/90.0d;
    
    public static final double PI = Math.PI;
    public static final double TWO_PI = 2.0*Math.PI;
    
    /** A small distance that can be added to prevent division by zero etc.
     * was 1e-300d
     */
    public static double DISTANCE_EPSILON = 1e-30d; // 
    /** A small angle (in rads) that is used to determine real differences. */
    public static double ANGLE_EPSILON = 1e-10d;
        
    
    /**
     * Translates any angle to a value between -Pi and +Pi (inclusive)
     */
    public static double toMinusPIPlusPIRange(double ang) {
        if (ang > -PI && ang <= PI ) {
            return ang;
        }
        if (ang <= -PI) {
            ang += 2.0*PI;
        } else if ( ang > PI ) {
            ang -= 2.0*PI;
        }
        // Repeat as necessary (useally the value is between -2 pi and 2 pi so speed is no issue)
        return toMinusPIPlusPIRange(ang);
    }
    
    /** Dot product is easily generalized for any dimension
     */
    public static double dotProduct(double[] vector_a, double[] vector_b) {
        double result = 0;
        for (int i=0;i<DIM;i++) {
            result += vector_a[i]*vector_b[i];
        }
        return result;
    }
    
    
    /**
     *Translate a bunch of points by a vector.
     */
    public static void translate( double[] trans, double[][] points ) {
        /** Take reference for speed if the JIT doesn't already */
        double xx = trans[0];
        double yy = trans[1];
        double zz = trans[2];

        for(int i=0; i<points.length; i++) {
            double[] point = points[i];
            point[0] += xx;
            point[1] += yy;
            point[2] += zz;
        }
    }       
    
    /** Render a coordinate/vector */
    public static String toString(double[] vector) {
        Parameters p = new Parameters(); // for printf
        p.autoClear(true);
        StringBuffer sb = new StringBuffer();
        sb.append( "Coordinates: " );        
        for (int i=0;i<vector.length;i++) {
            p.add( vector[i]);
            sb.append( Format.sprintf( "%8.3f ", p));        
        }
        return sb.toString();
    }
    
    /**
     * No checks of validity. 
     *         Returns Defs.NULL_FLOAT on error
     */
    public static double distance(double[] pos_a, double[] pos_b) {
        double dist = 0;
        double diff;
        for (int i=0;i<DIM;i++) {
            diff = pos_a[i] - pos_b[i];
            dist += diff * diff;
        }
        return Math.sqrt( dist );
    }
    
    /**
     * No checks of validity. 
     */
    public static float distance(float[] pos_a, float[] pos_b) {
        double dist = 0;
        double diff;
        for (int i=0;i<DIM;i++) {
            diff = pos_a[i] - pos_b[i];
            dist += diff * diff;
        }
        return (float) Math.sqrt( dist );
    }
    
    
    /** Stolen from Aqua */
    public static double to_180 ( double a ) {
    // transforms the argument to the range <-180, 180]
        while ( a > 180 ) {
            a -= 360;
        }
        while ( a <= -180 ) {
            a += 360;
        }
        return a;  
    }
    /** Stolen from Aqua; Brings an angle in degrees
     * to a range [0,360>
     */
    public static double to_0_360 ( double a ) {
    // transforms the argument to the range [0, 360>
        while ( a >= 360 ) {
            a -= 360;
        }
        while ( a < 0 ) {
            a += 360;
        }
        return a;  
    }

    /** Stolen from Aqua brings an angle in radians
     * to a range [0,2pi>
     * */
    public static double to_0_2pi ( double a ) {
        while ( a >= TWO_PI ) {
            a -= TWO_PI;
        }
        while ( a < 0 ) {
            a += TWO_PI;
        }
        return a;  
    }
    
    /** Stolen from Aqua. Angles should be in range [-pi,pi>>.
     * Resulting average is also in this range.
     * In case of impossible average (vector average too close to 0,0 
     * the routine returns Double.NaN.
     * */
    public static double averageAngles( double[] alpha ) {
        double sumX = 0;
        double sumY = 0;
        for (int i=0;i<alpha.length;i++) {
            sumX += Math.cos(alpha[i]);
            sumY += Math.sin(alpha[i]);
        }
        sumX /= alpha.length;
        sumY /= alpha.length;
        if ( Math.abs(sumX)<DISTANCE_EPSILON && Math.abs(sumY)<DISTANCE_EPSILON ) {
            General.showWarning("Failed to average angles; average to close to center.");
            General.showWarning("X: "+ sumX);
            General.showWarning("Y: "+ sumY);
            return Double.NaN;
        }
        
//        if ( sumX < DISTANCE_EPSILON ) {
//            sumX = Double.MIN_VALUE;
//        }
        // -180 to 180 degrees.
        double average = Math.atan2(sumY,sumX);
        if (average==Double.NaN) { // 
            General.showWarning("Failed to get Math.atan2(sumY,sumX).");
            return Double.NaN;
        }
        return average;
    }
    
    /** Angles don't need to be in any range.
     * Resulting average is also in this range.
     * Uses the Wiki http://en.wikipedia.org/wiki/Rotation_matrix info.
     * <PRE>
     * M(t)=[cos t - sin t]
     *      [sin t   cos t]
     * Rotating the second angle by the oposite of the first angle and
     * returning it's value.
     * The result is signed going from t to s. E.g. a result matrix is:
     *   5  15 ->  10
     * 345   5 ->  20
     *   5 345 -> -20
     * 180 180 ->   0
     *  90 -70 ->-170
     *  </PRE>
     * *
    public static double differenceAngles( double t, double s ) {
        double sx = Math.cos(s);
        double sy = Math.sin(s);
        double srx = Math.cos(-t)*sx - Math.sin(-t)*sy; // rotated s by -t angle
        double sry = Math.sin(-t)*sx + Math.cos(-t)*sy;        
        return Math.atan2(sry,srx);
    }
    */
    
    /** Angles don't need to be in any range but they'll come out after
     * toMinusPIPlusPIRange().
     *  The below from Tim Stevens is correct and it surely will be faster:
     * <PRE>
     * The result is signed going from t to s. E.g. a result matrix is:
     *   5  15 ->  10
     * 345   5 ->  20
     *   5 345 -> -20
     * 180 180 ->   0
     *  90 -70 ->-160
     *  </PRE>
     */
    public static double differenceAngles( double t, double s ) {
        t %= TWO_PI;
        s %= TWO_PI;
        return toMinusPIPlusPIRange(s-t);
    }
        
    /** Algortihm used first rotates the angles back by lowerbound so that all
     * reference the lowerbound at zero. Makes for easy detection of 'in range'
     * when the angles also expressed in 0-2pi range.
    */
    public static double violationAngles( double lowerBound, double upperBound, double value) {
        General.showDebug("     lowerBoundOrg: " + Math.toDegrees(lowerBound));
        General.showDebug("     upperBoundOrg: " + Math.toDegrees(upperBound));
        General.showDebug("     value        : " + Math.toDegrees(value));
        upperBound -= lowerBound;
        value      -= lowerBound;
        upperBound = to_0_2pi(upperBound);
        value      = to_0_2pi(value);
//        lowerBound = 0; not used

        // Upperbound violation in range [0,2pi>
        double dU = value-upperBound;
        if ( dU <= 0 ) {
            return 0;
        }
        // Lowerbound violation in range <0,2pi>
        double dL = TWO_PI - value; 

        // Here's the funny part, eventhough dU and dL can be up to 2pi
        // the smallest of the 2 is always only up to pi.
        // minViol in [0,pi]
        double min_viol = Math.min(dU, dL);
        return min_viol;
    }

    public static boolean[] isLowUppViolationAngles(
            double lowerBound, double upperBound, double value, double viol) {
        boolean[] result = new boolean[2];
        if ( viol < 0 ) {
            return result;
        }
        double l_d = differenceAngles(lowerBound, value); 
        double u_d = differenceAngles(upperBound, value);
        if ( Math.abs(l_d) > Math.abs(u_d) ) {
            result[0] = true;
        } else {
            result[1] = true;            
        }
        return result;
    }

    /** Returns the largest range that contains all input */
    public static double[] getMinMaxAngle(double[] ds) {
        
        if ( ds.length < 1 ) {            
            return null;
        }
        double[] result = new double[2];
        result[0] = ds[0];
        result[1] = ds[0];
        
        for (int i=1;i<ds.length;i++) {
            double viol = violationAngles(result[0], result[1], ds[i]);
            if (viol<ANGLE_EPSILON) {
                continue;
            }
            // which of the two sides was violated?
            boolean[] isLowUppViol = isLowUppViolationAngles(result[0], result[1], ds[i], viol);
            if ( isLowUppViol[0] ) {
                result[0] = ds[i];
            } else {
                result[1] = ds[i];
            }
        }
        return null;
    }

    /** Get the vector from a to b */
    public static double[] sub(double[] pos_a, double[] pos_b) {
        double[] result = new double[DIM];
        for (int i=0;i<DIM;i++) {
            result[i] = pos_b[i]-pos_a[i];
        }
        return result;
    }

    /**
     * Get the vector lenght.
     */
    public static double size(double[] vector) {
        double result = 0;
        for (int i=0;i<DIM;i++) {
            result += vector[i]*vector[i];
        }
        return Math.sqrt(result);
    }
    
}
