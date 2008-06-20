#!/bin/csh

# This script will back up all mrfiles that have been modified in the last x
# days.

# Sets classpath for java like always
# I do this so my software gets run every week and shows me in the cron
# output if errors occur; possibly before I commit them to production jobs
source ~/.cshrc

# Load common settings for CESG scripts
# Directory with cron scripts
set base_dir_script = /share/jurgen/CESG_Other/Gobbler/scripts
source $base_dir_script/settings.csh

# In future script this might be set to differ and actually get used.
set incremental = "true"

# The number of days the modification on a mrfile might be old in order
# to be back uped.
set max_days = 99999

# Limit the cpu usage to 100 hours.
set max_cpu_time = 360000
limit cputime $max_cpu_time

if ( ($max_days < 1) || ($max_days > 99999 ) ) then
	echo "ERROR: invalid parameter max_days:" $max_days
	echo "ERROR: should be between 1 and 99999 days"
	exit(1)
endif

echo "Maxdays              : " $max_days
echo "Doing incremental is : " $incremental
echo "Maximum cpu time is  : " $max_cpu_time
echo "PATH is              : " $PATH
echo "CLASSPATH for java is: " $CLASSPATH
echo "Cron script is       : " /share/jurgen/Java/Episode_II/Varia/backup_db_full.csh

# List of arguments
# 1 Load or dump (n):
# 2 Directory name 
# 3 Incremental(y) or full(n)
# 4 Dump one entry(y) or consecutive entries(n)? (y/n):
# 5 Start over? (y/n):

java Wattos.Episode_II.MRInterloop << EOD
n
/share/wattos/mr_anno_backup_2004-04-29
y
$max_days
n
n
EOD

echo "Done"
