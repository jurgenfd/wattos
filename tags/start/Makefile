# Wattos makefile
# The development version uses Ant for compiling Wattos.jar from
# within the Netbeans IDE.
# This makefile is included for compiling under Linux. 
# Included in CVS

# Project 
WATTOSROOT          = /share/jurgen/Wattos
# Version number
VERSION             = 2.1

WATTOSSRCROOT       = $(WATTOSROOT)/src/Wattos
WATTOSCLASSROOT     = $(WATTOSROOT)/build/web/WEB-INF/classes
WATTOSEXPORT        = $(WATTOSROOT)/export
XPLORDIR            = $(WATTOSSRCROOT)/Converters/Xplor
DYANADIR            = $(WATTOSSRCROOT)/Converters/Dyana
DISCOVERDIR         = $(WATTOSSRCROOT)/Converters/Discover
STARDIR             = $(WATTOSSRCROOT)/Star

# Location of JFLEX jar
JFLEX               = $(WATTOSJARSDIR)/JFlex.jar
# Some temporary locations.
TMPDIR              = /tmp
FILESLIST           = $(TMPDIR)/FILESLIST

###########################################################################
# No changes needed after this line.
###########################################################################

# Home page web server location
WATTOSLOCHOME       = $(WEBDIR)/wattos

info:
	@echo "Type any of the following commands."
	@echo "	make info                   this info"
	@echo "	make wattos                 creates the class files for Wattos"
	@echo "	make doc                    creates the Wattos standard javadoc html pages"
	@echo "	make home                   creates the Wattos home page"
	@echo "	make list 	                shows the number of packages and classes"
	@echo "	make clean 	                removes junk and class files so do not use before make home"


## Source code
wattos:
	@echo "The presumption is that your class path includes the correct items"
	@echo "Set them using wsetup (and edit it so that you don't use the Wattos.jar you're."
	@echo 'Class path: $(CLASSPATH)'        
	@cd src;find . -name "*.java" > $(FILESLIST)
	cd src;javac -classpath $(CLASSPATH) -d ${WATTOSCLASSROOT} @$(FILESLIST)
	@rm -f $(FILESLIST)

## Javadoc
doc:
	@find ./src/Wattos -name "*.java" > $(FILESLIST)
	@echo "Compiling documentation. Please ignore warnings."
	@javadoc -classpath "$(CLASSPATH)" -source '1.5' -splitindex \
	    -use -quiet -d dist/javadoc -doctitle Wattos -header "" \
	    -overview src/Wattos/overview.html -windowtitle Wattos -breakiterator \
	    @"$(FILESLIST)"
	@rm -f $(FILESLIST)
	@echo "Copying gif, str, xls, html and some other files"
	@(cd src;tar -cf - `find . -name "*.gif" -or -name "*.str" -or -name "*.xls" -or -name "*.html" -or -name "*.csv" `)|(cd dist/javadoc;tar -xf - )

list:
	@find src -type d  -not -name "Data" | wc | gawk '{print "Found Wattos packages: " $$1}'
	@find src -name "*.java"             | wc | gawk '{print "Found Wattos classes:  " $$1}'

# Removes all class files and java source files that were back uped by an editor like the one in Forte for Java
clean:
	find . -name "*.class"   -exec rm {} \;
	find . -name "*java~"    -exec rm {} \;

# STAR scanner as in sansj project
star_scanner:
	cd $(STARDIR) && env CLASSPATH="${CLASSPATH}:${JFLEX}" java JFlex.Main star.flex

# Dyana parser compiler with javacc and javac
dyana_parser:
	- cd $(DYANADIR); rm DyanaParserAllConstants.* DyanaParserAllTokenManager.* TokenMgrError.* Token.*
	cd $(DYANADIR);   javacc DyanaParserAll.jj

# Xplor/CNS parser compiler with javacc and javac
xparser:
	- cd $(XPLORDIR); rm XplorParserAllConstants.* XplorParserAllTokenManager.* TokenMgrError.* Token.*
	cd $(XPLORDIR);   javacc XplorParserAll.jj

# Discover parser compiler with javacc and javac
discover_parser:
	- cd $(DISCOVERDIR); rm DiscoverParserAllConstants.* DiscoverParserAllTokenManager.* TokenMgrError.* Token.*
	cd $(DISCOVERDIR);   javacc DiscoverParserAll.jj    

# GNU make feature to specify that there are no deps for activity
.PHONY: list doc clean wattos

