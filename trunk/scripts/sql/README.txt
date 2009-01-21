This directory contains database related files used by Wattos.

Content files           Description
------------------------+-------------------------------------------------------
README.txt              This file.
analysesPaper.sql       Analysis on WattosAn database for paper DOCR2
createWattosAn.sql		Creates a database for analysis of the NMR Restraint Grid db.
importExternalTablesWattosAn.sql Imports relevant external tables.
wattosAn.clay			Clay related file for modeling databases.


Notes:
-0- check slave status; since it only runs when requested it gets out of sync; so check:
		SELECT count(distinct pdb_id) FROM `wattos2`.`entry`
		log in C:\Program Files\MySQL\MySQL Server 5.0\data
		login with mysql -h localhost -u root -p"aev etc. and STOP SLAVE
		/share/wattos/Wattos/scripts/backup_mysqldb.csh
		cp /var/www/servlet_data/viavia/mr_mysql_backup/dump_file.sql $SJ
		mysql -h localhost -u root -p"aevETC" wattos2 < S:\jurgen\dump_file.sql
		login and START SLAVE;
		check log if no problems were found.
-1- login to WattosAn database in eclipse user root;pass aev etc.
-2- customize and run $SJ/CloneWars/DOCR1000/scripts/getSTARinfo.csh
		make sure it runs on all 4 tables
-3- customize and run importExternalTablesWattosAn.sql
-4- customize and run createWattosAn.sql
-5- data/extract view: overviewall to csv in eclipse
-6- use excel to add column: =IF(AND(AND(A4>=0.8,B4>=0.3333),AND(D4<=2,E4<=0.25)),1,"")