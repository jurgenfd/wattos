#!/bin/csh -f

# This script will convert all raw mrfiles to star files where possible.

# Limit the cpu usage to 20 hours.
set max_cpu_time = 72000

## NO CHANGES BELOW THIS LINE
setenv W /share/wattos/Wattos
setenv WATTOSROOT $W
source $W/wsetup

limit cputime $max_cpu_time

echo "Maximum cpu time is  : " $max_cpu_time
echo "PATH is              : " $PATH
echo "CLASSPATH for java is: " $CLASSPATH
echo "Cron script is       : " load_db_raw.csh

# List of arguments
# 1 Do incremental conversions only(y) or do all classified mr files (n)? : (y/n):n
# 2 Do one entry(y) or consecutive entries(n)? (y/n): n
# 3 Use all from above (y) or specify your own list(n) (y/n): y
# 4 to do first (or . for first): .
# 5 Do all now? (y/n):y
# 6 Start again:n
java -Xmx$WATTOSMEM Wattos.Episode_II.MRConvert << EOD
n
n
y
1awx
9999
n

echo "Done"
