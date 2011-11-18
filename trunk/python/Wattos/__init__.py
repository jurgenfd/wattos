# The TMPDIR environment variable will override the default above but not the one that
# might be defined in localConstants.py.
import os
import sys

starDirTmp             = os.path.join("/tmp", "watttos")

try:
    from localConstants import wattosDirTmp  #@UnresolvedImport
except:
    if os.environ.has_key("TMPDIR"):
        wattosDirTmp = os.path.join(os.environ["TMPDIR"], "watttos")
# end if

if not os.path.exists(wattosDirTmp):
#    print("DEBUG: Creating a temporary dir for wattos: [%s]" % wattosDirTmp)
    if os.mkdir(wattosDirTmp):
        print("ERROR: Failed to create a temporary dir for wattos at: " + wattosDirTmp)
        sys.exit(1)
#print 'DEBUG: using wattosDirTmp: ' + wattosDirTmp
