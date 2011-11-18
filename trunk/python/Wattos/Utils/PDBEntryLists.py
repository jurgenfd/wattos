"""
Author: Jurgen F. Doreleijers, BMRB, Jan 2007
"""
# Standard
from Wattos.Utils.localConstants import urlDB
import urllib
# BMRB specific
#from localConstants import *

def getEntryListFromCsvFile(urlLocation):
    result = []
    ##108d
    ##149d
    r1 = urllib.urlopen(urlLocation)
    data = r1.read()
    r1.close()
    dataLines = data.split("\n")
    for dataLine in dataLines:
        if dataLine:
            (pdbCode,) = dataLine.split()
            result.append( pdbCode )
    return result


def getBmrbNmrGridEntries():
    result = []
    urlLocation = urlDB+"/entry.txt"
    ##4583    \N    108d    \N    \N
    ##4584    \N    149d    \N    \N
    r1 = urllib.urlopen(urlLocation)
    data = r1.read()
    r1.close()
    dataLines = data.split("\n")
    for dataLine in dataLines:
        if dataLine:
            # b is for bogus/unused
            print "DEBUG: read dataLine: [%s]" % dataLine
            (_b1,_b2,pdbCode,_b3,_b4) = dataLine.split() #@UnusedVariable
            result.append( pdbCode )
    return result

def getBmrbNmrGridEntriesDOCRfREDDone():
    result = []
    urlLocation = urlDB+"/mrfile.txt"
    ##61458    7567    4-filtered-FRED    2gov    2006-05-11
    ##61459    7567    4-filtered-FRED    2gov    2006-05-11
    r1 = urllib.urlopen(urlLocation)
    data = r1.read()
    r1.close()
    dataLines = data.split("\n")
    for dataLine in dataLines:
        if dataLine:
            # b is for bogus/unused
            (_b1,_b2,stage,pdbCode,_b3) = dataLine.split()
            if stage=="4-filtered-FRED":
                if pdbCode not in result:
                    result.append( pdbCode )
    return result

#### 1
##print "Number entries listDOCRfREDiteration1        : %s" % len(listDOCRfREDiteration1)
##
#### 2
##listCurrentEntries = getBmrbNmrGridEntries()
##listCurrentEntries.sort()
##print "Number entries listCurrentEntries            : %s" % len(listCurrentEntries)
##
#### 3
##listDOCRfREDiteration2 = []
##for x in listCurrentEntries:
##    if x not in listDOCRfREDiteration1:
##        listDOCRfREDiteration2.append(x)
##listDOCRfREDiteration2.sort()
##print "Number entries listDOCRfREDiteration2        : %s" % len(listDOCRfREDiteration2)
##
#### 4
##listDOCRfREDDone = getBmrbNmrGridEntriesDOCRfREDDone()
##listDOCRfREDDone.sort()
##print "Number entries listDOCRfREDDone              : %s" % len(listDOCRfREDDone)
##
#### 5
##listDOCRfREDiteration2Todo = []
##for x in listDOCRfREDiteration2:
##    if x not in listDOCRfREDDone:
##        listDOCRfREDiteration2Todo.append(x)
##print "Number entries listDOCRfREDiteration2Todo    : %s" % len(listDOCRfREDiteration2Todo)
###Number entries listDOCRfREDiteration1        : 545
###Number entries listCurrentEntries            : 2948
###Number entries listDOCRfREDiteration2        : 2404
###Number entries listDOCRfREDDone              : 1437
###Number entries listDOCRfREDiteration2Todo    : 1510
##
#### 6
##listDOCRfREDiteration2TodoTemp = getEntryListFromCsvFile(localConstants.urlLists + "/list_todo.csv")
##print "Number entries listDOCRfREDiteration2TodoTemp: %s" % len(listDOCRfREDiteration2TodoTemp)
##
#### 7
##listDOCRfREDWeekly = getEntryListFromCsvFile(localConstants.urlLists + "/list_weekly.csv")
##print "Number entries listDOCRfREDWeekly            : %s" % len(listDOCRfREDWeekly)

##for x in listDOCRfREDWeekly:
##    print x
