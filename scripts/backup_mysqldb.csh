#!/bin/csh -f

# This script will dump all contents of the mysql database.
# into a directory.

# TODO: Change the below to the right password. Don't want to include it with distribution
# but is required for using --master-data option
set password = '\!Ecj%Y&R'

# Limit the cpu usage to 6 minutes.
set max_cpu_time = 360
setenv DUMP_DIR /var/www/servlet_data/viavia/mr_mysql_backup
## NO CHANGES BELOW THIS LINE 
##############################################################
echo "Doing backup_mysqldb.csh"

limit cputime $max_cpu_time

\rm -rf $DUMP_DIR >& /dev/null
mkdir -p $DUMP_DIR
chmod o+w $DUMP_DIR
mysqldump --master-data --tab=$DUMP_DIR --opt -u root -p$password wattos1
mysqldump --master-data                       -u root -p$password wattos1 > $DUMP_DIR/dump_file.sql

chmod o-w $DUMP_DIR
echo "dump dir: $DUMP_DIR"
ls -al $DUMP_DIR
echo "Done"
