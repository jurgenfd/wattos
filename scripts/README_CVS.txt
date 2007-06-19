NB: Before throwing away a working version check for differences under Linux/Cygwin (see point 1b below)

Steps to do for cvs checkout in order to get a fully working development version up and running
     
-1- CVS checkout (can be done in Netbeans IDE 5.0
    cvs -d :pserver:medusa:/cvs_archive/cvs login
    cvs -d :pserver:medusa:/cvs_archive/cvs checkout Wattos

-1b- Check for differences under Linux/Cygwin
    diff -hwr -x "*CVS*" Wattos c:/Wattos
    
-2- Set WATTOSROOT directory in Windows properties.
    Add the %WATTOSROOT%\scripts dir to the PATH.

-3- Regenerate the keys for signing jars.
    rm %WATTOSROOT%\scripts\keystore
    keytool -genkey -dname "cn=Jurgen Doreleijers, ou=BMRB, o=UW-Madison, c=US" -alias business -keypass test123 -keystore "%WATTOSROOT%\scripts\keystore" -storepass secret -validity 180
    Resign the jars for Web applications because they need to be sign using the same keys.
    Make sure the webapp-jar-cp is run with all jars this time. Check by really running applets and webapps. Make sure
    the local cache of Java applets and webapps is flushed.
        
-5- Run test suite (JUnit 'test' under Ant)
 
-6- Run macro test suite (JUnit 'macro_tests' under Ant)
    Make sure that the list of macro files is set to all known in the 'macro' directory.
