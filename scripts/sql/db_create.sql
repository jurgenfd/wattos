--
-- BMRB Analysis of the MR files collected in the PDB
-- SQL Definitions for creating tables
--     written: Thu Nov 29 16:28:11 CST 2001
--     revised: Tue May 25 17:41:40 CDT 2004
--              Tue Nov 23 11:41:45 CST 2004 for MySQL
-- Copyright 2004
--     Jurgen F. Doreleijers
--     BioMagResBank, University of Wisconsin - Madison, U.S.A.
--
-- Notes:
-- * Note that the EOL should be corrected for the system on which it is run.
-- * 	- Default is Windows for that's my development machine but no garantee.
-- * Setup commands are specific for database type: MySQL
-- * Schema is vizualized in an MS Visio file (db_analyses.vsd)
-- * Run by command like: 
-- * mysql -h tang.bmrb.wisc.edu -u wattos1 -p4I4KMS  wattos1 < "C:\Documents and Settings\jurgen.WHELK.000\workspace\Wattos\scripts\SQL\db_create.sql"
	-- no output means no errors.
-- * mysql -h tang.bmrb.wisc.edu -u wattos1 -p4I4KMS  wattos1 < /share/jurgen/db_create.sql
-- Should be autocommiting by default but I saw it didn't once.
SET AUTOCOMMIT=1;

-- Remove previous copies in bottom up order.
-- This will automatically drop the index created too.
DROP TABLE IF EXISTS temp_mrblock;
DROP TABLE IF EXISTS temp_mrfile;
DROP TABLE IF EXISTS mrblock;
DROP TABLE IF EXISTS mrfile;
DROP TABLE IF EXISTS entry;
                     
-- Remove unique sequence ids
DROP TABLE IF EXISTS entry_id;
DROP TABLE IF EXISTS mrfile_id;
DROP TABLE IF EXISTS mrblock_id;
DROP TABLE IF EXISTS dbfs_id;
CREATE TABLE entry_id   (id INT NOT NULL); INSERT INTO entry_id     VALUES (1000);
CREATE TABLE mrfile_id  (id INT NOT NULL); INSERT INTO mrfile_id    VALUES (1000);
CREATE TABLE mrblock_id (id INT NOT NULL); INSERT INTO mrblock_id   VALUES (1000);
CREATE TABLE dbfs_id    (id INT NOT NULL); INSERT INTO dbfs_id      VALUES (1000);
-- entry
CREATE TABLE entry
(
    entry_id                       INT              NOT NULL PRIMARY KEY,
    bmrb_id                        INT,
    pdb_id                         CHAR(4),
    in_recoord                     BOOLEAN DEFAULT 0,
    in_dress                       BOOLEAN DEFAULT 0   
) TYPE = INNODB;
CREATE INDEX entry_001 ON entry (bmrb_id);
CREATE INDEX entry_002 ON entry (pdb_id);
-- mrfile
-- MySQL doesn't accept the SYSDATE default for date_modified so always present date on insert.
-- From MySQL manual: 
-- For storage engines other than InnoDB, MySQL Server parses the FOREIGN KEY 
-- syntax in CREATE TABLE statements, but does not use or store it.
-- The solution is to define the innodb tables as below.
CREATE TABLE mrfile
(
    mrfile_id                       INT             NOT NULL PRIMARY KEY,
    entry_id                        INT             NOT NULL,
    detail                          VARCHAR(255)    DEFAULT 'raw' NOT NULL,
    pdb_id                          CHAR(4),
    date_modified                   DATE            NOT NULL,
    FOREIGN KEY (entry_id)          REFERENCES entry (entry_id) ON DELETE CASCADE
) TYPE = INNODB;
-- Some common queries are helped by these indexes..
CREATE INDEX mrfile_001 ON mrfile (entry_id);
CREATE INDEX mrfile_002 ON mrfile (detail);
CREATE INDEX mrfile_003 ON mrfile (pdb_id);
CREATE INDEX mrfile_004 ON mrfile (date_modified);
-- mrblock 
CREATE TABLE mrblock
(
    mrblock_id                     INT              NOT NULL PRIMARY KEY,
    mrfile_id                      INT              NOT NULL,
    position                       INT              NOT NULL,
    program                        VARCHAR(255)     NOT NULL,
    type                           VARCHAR(255)     NOT NULL,
    subtype                        VARCHAR(255)     NOT NULL,
    format                         VARCHAR(255)     NOT NULL,
    text_type                      VARCHAR(255)     DEFAULT 'raw' NOT NULL ,
    byte_count                     INT              ,
    item_count                     INT              DEFAULT NULL, 
    date_modified                  DATE             NOT NULL,
    other_prop                     VARCHAR(255)     ,
    dbfs_id                        INT              NOT NULL,
    file_name                      VARCHAR(255)     NOT NULL,
    md5_sum                        VARCHAR(32)      NOT NULL,
    FOREIGN KEY (mrfile_id)          REFERENCES mrfile (mrfile_id) ON DELETE CASCADE
) TYPE = INNODB;
--- When the actual textual data was included the following line was added as optimalization.
-- default values for both initial and next storage is 5 data blocks
-- 1 data block is in the order of 4 kb.
-- default value for percentage increase is 50 so 
--- STORAGE (INITIAL 256M NEXT 256M PCTINCREASE 0)
-- default value for chunk size is 1 data block
-- LOB (mol_image) STORE AS (CHUNK 4096)
-- Some common queries are helped by these indexes..
CREATE INDEX mrblock_001 ON mrblock (mrfile_id);
CREATE INDEX mrblock_002 ON mrblock (program);
CREATE INDEX mrblock_003 ON mrblock (type);
CREATE INDEX mrblock_004 ON mrblock (subtype);
CREATE INDEX mrblock_005 ON mrblock (format);
CREATE INDEX mrblock_006 ON mrblock (text_type);
--CREATE INDEX mrblock_007 ON mrblock (byte_count);
--CREATE INDEX mrblock_008 ON mrblock (date_modified);
--CREATE INDEX mrblock_009 ON mrblock (dbfs_id);
--CREATE INDEX mrblock_010 ON mrblock (file_extension);
----------------------------------------------------------------------------------------
CREATE TABLE temp_mrfile
(
    mrfile_id                       INT             NOT NULL PRIMARY KEY,
    entry_id                        INT             NOT NULL,
    detail                          VARCHAR(255)    DEFAULT 'raw' NOT NULL,
    pdb_id                          CHAR(4),
    date_modified                   DATE            NOT NULL,
    FOREIGN KEY (entry_id)          REFERENCES entry (entry_id) ON DELETE CASCADE
) TYPE = INNODB;
-- mrblock 
CREATE TABLE temp_mrblock
(
    mrblock_id                     INT              NOT NULL PRIMARY KEY,
    mrfile_id                      INT              NOT NULL,
    position                       INT              NOT NULL,
    program                        VARCHAR(255)     NOT NULL,
    type                           VARCHAR(255)     NOT NULL,
    subtype                        VARCHAR(255)     NOT NULL,
    format                         VARCHAR(255)     NOT NULL,
    text_type                      VARCHAR(255)     DEFAULT 'raw' NOT NULL ,
    byte_count                     INT              ,
    item_count                     INT              DEFAULT NULL, 
    date_modified                  DATE             NOT NULL,
    other_prop                     VARCHAR(255)     ,
    dbfs_id                        INT              NOT NULL,
    file_name                      VARCHAR(255)     NOT NULL,
    md5_sum                        VARCHAR(32)      NOT NULL,
    FOREIGN KEY (mrfile_id)          REFERENCES temp_mrfile (mrfile_id) ON DELETE CASCADE
) TYPE = INNODB;
