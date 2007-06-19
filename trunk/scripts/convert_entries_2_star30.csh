#!/bin/csh

# This script will convert all mrfiles to star 3.0

# Sets classpath for java like always
# I do this so my software gets run every week and shows me in the cron
# output if errors occur; possibly before I commit them to production jobs
source ~/.cshrc



echo "PATH is              : " $PATH
echo "CLASSPATH for java is: " $CLASSPATH
echo "Cron script is       : " convert_entries_2_star30.csh

# List of arguments
# 1 Load(y), dump (n) or convert(c):
# 2 STAR version (1 for 3.0)
# 2 Directory name in
# 3 Directory name out
# 4 Convert one entry(y) or consecutive entries(n)? (y/n):
# 5 Start over? (y/n):

java Wattos.Episode_II.MRInterloop << EOD
c
1
.
.
y
.
y
n
EOD

echo "Done"
