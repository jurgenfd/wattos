# wsetup: Script to define Wattos environment variables and aliases
# ===========================================================
# Jurgen F. Doreleijers
# ===========================================================
# source the script before running Wattos.
# The variable 'WATTOSROOT' must exist and point to the Wattos top directory.
# The variable 'WATTOSHOME' must exist and point to a writable user-specific directory
#	(not needed to exist yet) where Wattos can save settings and state.

# How much memory is wattos allowed to gobble?
export WATTOSMEM=4g

# -------------------------------------------------
# leave the rest untouched

# Installation directories.
export     WATTOSSRCDIR=$WATTOSROOT/src
export   WATTOSCLASSDIR=$WATTOSROOT/bin
#export   WATTOSCLASSDIR=${WATTOSCLASSDIR}
export    WATTOSJARSDIR=$WATTOSROOT/lib
export     WATTOSDOCDIR=$WATTOSROOT/doc
export    WATTOSDATADIR=$WATTOSROOT/data
export     WATTOSBINDIR=$WATTOSROOT/bin
export WATTOSSCRIPTSDIR=$WATTOSROOT/scripts

# location of on-line manual for WATTOS:
export WATTOSHELPURL="file:///$WATTOSDOCDIR/index.html"

## Start wattos non-graphical ui
# Used to include: -DWATTOSHOME=$WATTOSHOME
alias wattos='java -Xmx$WATTOSMEM Wattos.CloneWars.UserInterface -at'
# the most simplest of routines used as a test program
alias getEpochTime='java Wattos.Utils.Programs.GetEpochTime'
# Program to do relational operations on sets like PDB ids
#alias relate='java -Xmx$WATTOSMEM Wattos.Utils.Programs.Relate $*'
# Filter or just reformat a star file
#alias filter='java -Xmx$WATTOSMEM  Wattos.Star.STARFilter $*'
# Filter or just reformat a star file
alias mrannotator='java -Xmx$WATTOSMEM Wattos.Episode_II.MRAnnotate'

# Wattos own classes
export CLASSPATH=${WATTOSJARSDIR}/Wattos.jar

# hostname shows Amok.local or Amok.ododo.nl
# lower case hostname on cygwin and upper case on windows.
#export wattoshost=(hostname|gawk -F'[.]' '{print tolower($1)}'| sed -e 's/[-0-9]//g'`)
if [ "$HOSTNAME" = "Amok.local" ]; then
    # Disable the next line if on development machine we still want to use the production jar.
    export CLASSPATH=${WATTOSCLASSDIR}
    # Note that the following statement interfers with scp when shown.
    echo "DEBUG: Wattos wsetup found to be on development machine"
else
    echo "DEBUG: Wattos setup on production."
fi


# For a description of the jar files see the README.txt in the lib dir.
export CLASSPATH=${CLASSPATH}:${WATTOSJARSDIR}/ant-contrib.jar
export CLASSPATH=${CLASSPATH}:${WATTOSJARSDIR}/colt.jar
export CLASSPATH=${CLASSPATH}:${WATTOSJARSDIR}/CSVutils.jar
export CLASSPATH=${CLASSPATH}:${WATTOSJARSDIR}/jakarta-regexp.jar
export CLASSPATH=${CLASSPATH}:${WATTOSJARSDIR}/javacc.jar
export CLASSPATH=${CLASSPATH}:${WATTOSJARSDIR}/JFlex.jar
export CLASSPATH=${CLASSPATH}:${WATTOSJARSDIR}/printf_hb15.jar
export CLASSPATH=${CLASSPATH}:${WATTOSJARSDIR}/mysql-connector-java-5.0.3-bin.jar
export CLASSPATH=${CLASSPATH}:${WATTOSJARSDIR}/starlibj_with_source.jar
export CLASSPATH=${CLASSPATH}:${WATTOSJARSDIR}/swing-layout-1.0.jar
export CLASSPATH=${CLASSPATH}:${WATTOSJARSDIR}/jfreechart-1.0.1.jar
export CLASSPATH=${CLASSPATH}:${WATTOSJARSDIR}/jcommon-1.0.0.jar
export CLASSPATH=${CLASSPATH}:${WATTOSJARSDIR}/gnujaxp.jar
export CLASSPATH=${CLASSPATH}:${WATTOSJARSDIR}/itext-1.4.jar
export CLASSPATH=${CLASSPATH}:${WATTOSJARSDIR}/junit-3.8.1.jar
# export CLASSPATH=${CLASSPATH}:${WATTOSJARSDIR}/jdbc20x.jar

echo "Initialized Wattos with Java classpath:" $CLASSPATH

