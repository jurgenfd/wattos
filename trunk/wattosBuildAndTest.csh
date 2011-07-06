#!/bin/tcsh -e

# Used by Jenkins to build and test this installation automatically on various platforms. 

cd $0:h

echo "DEBUG: PATH       1: $PATH"
echo "DEBUG: PYTHONPATH 1: $PYTHONPATH"
echo "DEBUG: CLASSPATH  1: $CLASSPATH"

setenv WATTOSROOT $cwd
source $WATTOSROOT/scripts/wsetup

echo "DEBUG: PATH       2  : $PATH"
echo "DEBUG: PYTHONPATH 2  : $PYTHONPATH"
echo "DEBUG: CLASSPATH  2  : $CLASSPATH"

# Comment out the next line after done testing for it's a security issue.
#setenv | sort

ant -f buildEclipse.xml clean compile jar
# Run as a test from the newly created jar.
getEpochTime
#ant -f buildEclipse.xml test

# For the few lines of Python code.
make -j nose
make -j pylint
make -j sloccount

echo "Done"
