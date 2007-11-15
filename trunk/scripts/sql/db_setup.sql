-- STEP BY STEP PROCEDURE FOR SETTING UP NEW DB

-- set root password
--mysql -u root password '\!Ecj'ETC  

-- Notes:
    --  # no escapes for any char but the first.
    -- no backslash escape '\' needed when using double quotes in windows.

-- create it
create database wattos2;

-- creating the account 
# Not needed anymore?
CREATE USER 'wattos2'@'localhost'               ;
CREATE USER 'wattos2'@'localhost.localdomain'   ;
CREATE USER 'wattos2'@'tang.bmrb.wisc.edu'      ;
CREATE USER 'wattos2'@'whelk.bmrb.wisc.edu'     ;
CREATE USER 'wattos2'@'anthozoan.bmrb.wisc.edu' ;
CREATE USER 'wattos2'@'zebrafish.bmrb.wisc.edu' ;
CREATE USER 'repl'@'whelk.bmrb.wisc.edu'        ;


SET PASSWORD FOR 'wattos2'@'localhost'               = PASSWORD('4I4KMS');
SET PASSWORD FOR 'wattos2'@'localhost.localdomain'   = PASSWORD('4I4KMS');
SET PASSWORD FOR 'wattos2'@'tang.bmrb.wisc.edu'      = PASSWORD('4I4KMS');
SET PASSWORD FOR 'wattos2'@'whelk.bmrb.wisc.edu'     = PASSWORD('4I4KMS');
SET PASSWORD FOR 'wattos2'@'anthozoan.bmrb.wisc.edu' = PASSWORD('4I4KMS');
SET PASSWORD FOR 'wattos2'@'zebrafish.bmrb.wisc.edu' = PASSWORD('4I4KMU');
SET PASSWORD FOR 'repl'@'whelk.bmrb.wisc.edu'        = PASSWORD('slavepass');
                                                    
GRANT ALL ON wattos2.* TO 'wattos2'@'localhost';
GRANT ALL ON wattos2.* TO 'wattos2'@'tang.bmrb.wisc.edu';
GRANT ALL ON wattos2.* TO 'wattos2'@'halfbeak.bmrb.wisc.edu';
GRANT ALL ON wattos2.* TO 'wattos2'@'whelk.bmrb.wisc.edu';
GRANT ALL ON wattos2.* TO 'wattos2'@'anthozoan.bmrb.wisc.edu';
GRANT ALL ON wattos2.* TO 'wattos2'@'zebrafish.bmrb.wisc.edu';
GRANT ALL ON wattos2.* TO 'wattos2'@'localhost.localdomain';             

update user set file_priv='Y' where user='wattos2';

GRANT REPLICATION SLAVE ON *.* TO 'repl'@'whelk.bmrb.wisc.edu'   IDENTIFIED BY 'slavepass';
GRANT REPLICATION SLAVE ON *.* TO 'repl'@'localhost.localdomain' IDENTIFIED BY 'slavepass';

-- create the tables within-- type the following on the terminal--mysql -h tang.bmrb.wisc.edu -u wattos2 -p4I4KMS  wattos2 < "C:\Documents and Settings\jurgen.WHELK.000\workspace\Wattos\scripts\SQL\db_create.sql"
mysql -h localhost          -u wattos2 -p4I4KMS  wattos2 < "C:\Documents and Settings\jurgen.WHELK.000\workspace\Wattos\scripts\SQL\db_create.sql"
mysql -h localhost          -u wattos2 -p4I4KMS  wattos2 < /share/jurgen/db_create.sql

-- OR load very fast! Dump from 1 database to the next.
mysqldump --opt -u root -p'\!Ecj%Y&R' wattos2 > $SJ/filetosaveto.sql
-- add if speed is needed to begining of file: SET FOREIGN_KEY_CHECKS=0;
-- takes only 4 seconds without optimalization though.
mysql -u wattos2 -p4I4KMS wattos2 < $SJ/filetosaveto.sql
and then copy the mrgrid data itself too
cd /big/jurgen/DB/mrgrid/bfiles
cp -rvfup wattos2/* wattos2
/* just a comment to end the previous command interpreted by jedit as a comment **/

-- More config
set global query_cache_size=16000000;
SHOW STATUS LIKE 'Qcache%';
SHOW VARIABLES LIKE 'have_query_cache';
SHOW VARIABLES LIKE 'query_cache%';




-- JUNK BELOW

# Setttings within Eclips db browser.C:\Documents and Settings\jurgen.WHELK.000\workspace\Wattos\lib\mysql-connector-java-5.0.3-bin.jar
jdbc:mysql://localhost:3306/Wattos2
com.mysql.jdbc.Driver
~                                                                                         
~                                                                                         
~                                                                                         
~                                                                                         
~                                                                                         
~                                                                                         
~                                                                                         
~                                               
