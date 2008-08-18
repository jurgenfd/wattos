#!/bin/csh -f

# This script will back up all mrfiles that have been modified in the last x
# days.

# The number of days the modification on a mrfile might be old in order
# to be back uped.
set max_days = 9999

# Limit the cpu usage to 10 hours.
set max_cpu_time = 36000
limit cputime $max_cpu_time

if ( ($max_days < 1) || ($max_days > 9999 ) ) then
	echo "ERROR: invalid parameter max_days:" $max_days
	echo "ERROR: should be between 1 and 9999 days"
	exit(1)
endif

echo "Maxdays              : " $max_days
echo "Maximum cpu time is  : " $max_cpu_time
echo "PATH is              : " $PATH
echo "CLASSPATH for java is: " $CLASSPATH
echo "Cron script is       : " backup_db.csh

# List of arguments
# 1 Load or dump (n):
# 2 Directory name 
# 3 Incremental(y) or full(n)
# 4 Dump one entry(y) or consecutive entries(n)? (y/n):
# 5 Start over? (y/n):

java Wattos.Episode_II.MRInterloop << EOD
n
/share/wattos/mr_anno_backup
y
$max_days
n
n
EOD

echo "Done"
