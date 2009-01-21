-- STEP BY STEP PROCEDURE FOR SETTING UP NEW DB

-- login with root password
--mysql -u root -p'\!Ecj%Y&R' mysql
-- The db isn't available anywhere but on the host itself so this posses no security problem.

-- Notes:
    --  # no escapes for any char but the first.
    -- no backslash escape '\' needed when using double quotes in windows.

-- create it
create database wattos1;

use mysql;

-- creating the account 
CREATE USER 'wattos1'@'localhost'               ;
CREATE USER 'wattos1'@'localhost.localdomain'   ;
CREATE USER 'wattos1'@'tang.bmrb.wisc.edu'      ;


SET PASSWORD FOR 'wattos1'@'localhost'               = PASSWORD('4I4KMS');
SET PASSWORD FOR 'wattos1'@'localhost.localdomain'   = PASSWORD('4I4KMS');
SET PASSWORD FOR 'wattos1'@'tang.bmrb.wisc.edu'      = PASSWORD('4I4KMS');
                                                    

update user set file_priv='Y' where user='wattos1';

-- not sure where the next line belongs:
use wattos;
GRANT ALL ON * TO 'wattos1'@'localhost';
GRANT ALL ON * TO 'wattos1'@'tang.bmrb.wisc.edu';
GRANT ALL ON * TO 'wattos1'@'halfbeak.bmrb.wisc.edu';
GRANT ALL ON * TO 'wattos1'@'localhost.localdomain';             

GRANT REPLICATION SLAVE ON *.* TO 'repl'@'localhost.localdomain' IDENTIFIED BY 'slavepass';

-- create the tables within-- type the following on the terminal--mysql -h tang.bmrb.wisc.edu -u wattos1 -p4I4KMS  wattos1 < "C:\Documents and Settings\jurgen.WHELK.000\workspace\Wattos\scripts\SQL\db_create.sql"
mysql -h localhost          -u wattos1 -p4I4KMS  wattos1 < "C:\Documents and Settings\jurgen.WHELK.000\workspace\Wattos\scripts\SQL\db_create.sql"
mysql -h localhost          -u wattos1 -p4I4KMS  wattos1 < $WS/wattos/scripts/sql/db_create.sql

-- OR load very fast! Dump from 1 database to the next.
mysqldump --opt -u root -p'\!Ecj%Y&R' wattos1 > $SJ/filetosaveto.sql
-- add if speed is needed to begining of file: SET FOREIGN_KEY_CHECKS=0;
-- takes only 4 seconds without optimalization though.
mysql -u wattos1 -p4I4KMS wattos1 < $SJ/filetosaveto.sql
and then copy the mrgrid data itself too
cd /big/jurgen/DB/mrgrid/bfiles
cp -rvfup wattos1/* wattos1
/* just a comment to end the previous command interpreted by jedit as a comment **/

-- More config
set global query_cache_size=16000000;
SHOW STATUS LIKE 'Qcache%';
SHOW VARIABLES LIKE 'have_query_cache';
SHOW VARIABLES LIKE 'query_cache%';




-- JUNK BELOW

# Setttings within Eclips db browser.C:\Documents and Settings\jurgen.WHELK.000\workspace\Wattos\lib\mysql-connector-java-5.0.3-bin.jar
jdbc:mysql://localhost:3306/wattos1
com.mysql.jdbc.Driver
~                                                                                         
~                                                                                         
~                                                                                         
~                                                                                         
~                                                                                         
~                                                                                         
~                                                                                         
~                                               
