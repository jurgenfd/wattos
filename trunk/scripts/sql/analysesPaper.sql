--Code to create the views based on the slave updated version of Wattos2 db. 
-- Use the other analyzeXXX scripts for printing some numbers.
-- Use IDE to export overall view to csv file for Excel.

DROP view IF EXISTS PERFREDTMP;
CREATE VIEW PERFREDTMP 
	AS 
SELECT F.PDB_ID, B.TEXT_TYPE, B.ITEM_COUNT
FROM Wattos2.MRBLOCK AS B, Wattos2.MRFILE AS F
WHERE B.MRFILE_ID=F.MRFILE_ID AND TYPE='entry' AND PROGRAM='STAR'
ORDER BY F.PDB_ID, B.TEXT_TYPE;
  
DROP view IF EXISTS PERFRED;
CREATE VIEW PERFRED 
	AS 
SELECT P1.PDB_ID AS PDB_ID, P1.ITEM_COUNT AS C_PARS, P2.ITEM_COUNT AS C_FILTER, P2.ITEM_COUNT/P1.ITEM_COUNT AS P_FILTER
FROM PERFREDTMP AS P1, PERFREDTMP AS P2
WHERE (((P1.PDB_ID)=P2.PDB_ID) AND ((P1.ITEM_COUNT)>0) AND ((P1.TEXT_TYPE)='2-parsed') AND ((P2.TEXT_TYPE)='4-filtered-FRED'))
ORDER BY P2.ITEM_COUNT/P1.ITEM_COUNT DESC;

DROP view IF EXISTS PERFREDBAD;
CREATE VIEW PERFREDBAD 
	AS 
SELECT PDB_ID
FROM PERFRED
WHERE P_FILTER<0.333333;

DROP view IF EXISTS PERDOCR;
CREATE VIEW PERDOCR 
	AS 
SELECT P1.PDB_ID AS PDB_ID, P1.ITEM_COUNT AS C_PARS, P2.ITEM_COUNT AS C_FILTER, P2.ITEM_COUNT/P1.ITEM_COUNT AS P_FILTER
FROM PERFREDTMP AS P1, PERFREDTMP AS P2
WHERE (((P1.PDB_ID)=P2.PDB_ID) AND ((P1.ITEM_COUNT)>0) AND ((P1.TEXT_TYPE)='2-parsed') AND ((P2.TEXT_TYPE)='3-converted-DOCR'))
ORDER BY P2.ITEM_COUNT/P1.ITEM_COUNT DESC;
	  
	  
DROP view IF EXISTS PERDOCRBad;
CREATE VIEW PERDOCRBAD
	AS 
SELECT PDB_ID
FROM PERDOCR
WHERE P_FILTER<0.8;
		  
		  
DROP view IF EXISTS _Distance_constraint_stats_list_Query_Rms;
CREATE VIEW _Distance_constraint_stats_list_Query_Rms
	AS 
SELECT _Distance_constraint_stats_list.pdb_id, 
	Sqrt(Sum(Constraint_count*Viol_rms*Viol_rms)/Sum(Constraint_count)) AS WgtAvgRms
FROM _Distance_constraint_stats_list
GROUP BY _Distance_constraint_stats_list.pdb_id;

DROP view IF EXISTS _Distance_constraint_stats_list_Query_Max;
CREATE VIEW _Distance_constraint_stats_list_Query_Max
	AS 
SELECT DISTINCTROW _Distance_constraint_stats_list.pdb_id, 
Max(_Distance_constraint_stats_list.Viol_max) AS MaxOfViol_max
FROM _Distance_constraint_stats_list
GROUP BY _Distance_constraint_stats_list.pdb_id;



DROP view IF EXISTS violRmsBad;
CREATE VIEW violRmsBad
	AS 
SELECT pdb_id, WgtAvgRms
FROM _Distance_constraint_stats_list_Query_rms
WHERE WgtAvgRms>0.25;


DROP view IF EXISTS violMaxBad;
CREATE VIEW violMaxBad
	AS 
SELECT _Distance_constraint_stats_list_Query_Max.pdb_id, 
		_Distance_constraint_stats_list_Query_Max.MaxOfViol_max
FROM   _Distance_constraint_stats_list_Query_Max
WHERE  _Distance_constraint_stats_list_Query_Max.MaxOfViol_max>2;


DROP view IF EXISTS Set14DOCRFREDBaddies;
CREATE VIEW Set14DOCRFREDBaddies
	AS 
SELECT d.pdb_id
from perDocrBad d
UNION 
SELECT f.pdb_id
from perFredBad f
UNION 
SELECT v1.pdb_id
from violMaxBad v1
UNION 
SELECT v2.pdb_id
from violRmsBad v2;

-- Note in the below that using a NATURAL keyword instead of the ON clause
-- fails to pick up all the values because perFred and perDocr share
-- more than the one column name 'pdb_id' in common.
DROP view IF EXISTS overviewAll;
CREATE VIEW OverviewAll AS
SELECT 
P.p_filter as perDocr, 
F.p_filter as perFred, 
S.pdb_id,
M.MaxOfViol_Max, 
R.WgtAvgRms 
FROM 
((((Wattos2.entry S
LEFT OUTER JOIN perDocr P ON S.pdb_id = P.pdb_id)
LEFT OUTER JOIN perFred F ON S.pdb_id = F.pdb_id)
LEFT OUTER JOIN _Distance_constraint_stats_list_Query_Max M ON S.pdb_id = M.pdb_id)
LEFT OUTER JOIN _Distance_constraint_stats_list_Query_rms R ON S.pdb_id = R.pdb_id)
ORDER BY S.pdb_id;

--DROP view IF EXISTS overviewAllGottingen;
--CREATE VIEW OverviewAllGottingen AS
--SELECT * FROM overviewAll O
--where O.pdb_id in ( select S.pdb_id from docrFredBaddies786 S )
--ORDER BY O.pdb_id;


-- Recorded in excel worksheet as fixed but not fixed in db.
--DROP view IF EXISTS Overview_baddies_recorded_as_fixed;
--CREATE VIEW Overview_baddies_recorded_as_fixed AS 
--SELECT *
--from overviewAll b
--where
--    b.pdb_id in     ( select pdb_id from Set14DOCRFREDBaddies )
--AND b.pdb_id not in ( select pdb_id from docrFredBaddiesNotFixed )
--order by b.pdb_id;
--
--DROP view IF EXISTS Overview_goodies_recorded_as_baddies;
--CREATE VIEW Overview_goodies_recorded_as_baddies AS 
--SELECT *
--from overviewAll b
--where
--    b.pdb_id not in ( select pdb_id from Set14DOCRFREDBaddies )
--AND b.pdb_id in ( select pdb_id from docrFredBaddiesNotFixed )
--order by b.pdb_id;

------------------------------------------------------------------------------------------------
--Queries for analyses used in paper DOCR2.

SELECT count(DISTINCT f.pdb_id), b.text_type, b.type
FROM wattos2.mrblock AS b, wattos2.mrfile AS f
WHERE b.mrfile_id=f.mrfile_id and (
	b.type='dipolar coupling' OR
	b.type='distance' OR
	b.type='dihedral angle'
)	
group by b.text_type, b.type
order by b.text_type, b.type;

SELECT count(DISTINCT f.pdb_id), b.text_type,b.type
FROM wattos2.mrblock AS b, wattos2.mrfile AS f
WHERE b.mrfile_id=f.mrfile_id and (
	b.type='dipolar coupling' OR
	b.type='distance' OR
	b.type='dihedral angle'
)	
group by b.text_type,b.type
order by b.text_type,b.type;

-- Get the number of entries that violate Gottingen criteria 1-4
-- Last update on number is: Sept 13, 2007
SELECT count(*) FROM perdocrbad; --184
SELECT count(*) FROM perfredbad; --100
SELECT count(*) FROM violmaxbad; --209
SELECT count(*) FROM violrmsbad; --151


-- Get the counts of the unions of these 4 sets.
DROP view IF EXISTS set12;
CREATE VIEW set12 AS 
SELECT d.pdb_id
from perDocrBad d
UNION 
SELECT f.pdb_id
from perFredBad f;

DROP view IF EXISTS set34;
CREATE VIEW set34 AS 
SELECT v1.pdb_id
from violMaxBad v1
UNION 
SELECT v2.pdb_id
from violRmsBad v2;


-- Get allEntries
DROP VIEW IF EXISTS DOCRFREDEntries;
CREATE VIEW DOCRFREDEntries AS
SELECT DISTINCT pdb_id
FROM wattos2.mrfile
order by pdb_id;

-- Get goodies
DROP VIEW IF EXISTS DOCRFREDGoodies;
CREATE VIEW DOCRFREDGoodies AS
SELECT df.pdb_id
FROM DOCRFREDEntries df
where df.pdb_id not in ( select pdb_id from Set14DOCRFREDBaddies )
order by df.pdb_id;

select count(distinct pdb_id) from set12;
select count(distinct pdb_id) from set34;
select count(distinct pdb_id) from Set14DOCRFREDBaddies;
select count(distinct pdb_id) from DOCRFREDEntries;
select count(distinct pdb_id) from DOCRFREDGoodies;

SELECT count(DISTINCT f.pdb_id), b.program
FROM wattos2.mrblock AS b, wattos2.mrfile AS f
WHERE b.mrfile_id=f.mrfile_id and (
	b.type='dipolar coupling' OR
	b.type='distance' OR
	b.type='dihedral angle'
)	AND
b.text_type = '1-original'
group by b.program
order by count(DISTINCT f.pdb_id);


-- Surplus analysis
DROP VIEW IF EXISTS _Distance_constraint_surplus_percentages;

CREATE VIEW _Distance_constraint_surplus_percentages AS
SELECT pdb_id, 
		Constraint_surplus_count/Constraint_count as surplus,
		Constraint_redundant_count/Constraint_count as redundant,
		Constraint_double_count/Constraint_count as dubbel,
		Constraint_fixed_count/Constraint_count as fixed,
		Constraint_impossible_count/Constraint_count as impossible,
		Constraint_exceptional_count/Constraint_count as exceptional		
from _Distance_constraint_surplus
where Constraint_count!=0 AND
	pdb_id not in ( select b.pdb_id from Set14DOCRFREDBaddies b );


DROP VIEW IF EXISTS RDC_Entries;

CREATE VIEW RDC_Entries AS
SELECT DISTINCT f.pdb_id
FROM wattos2.mrblock AS b, wattos2.mrfile AS f
WHERE b.mrfile_id=f.mrfile_id and text_type='3-converted-DOCR' and type='dipolar coupling'
order by f.pdb_id;
	
-- Get the entries that only have 1 type (1 or more entities of it)
DROP VIEW IF EXISTS moltypesOverview;

CREATE VIEW moltypesOverview AS
SELECT pdb_id,type,typescount
FROM moltypes
group by pdb_id
having count(*)=1
order by pdb_id;
	
-- Get the completeness per moltype/entries
DROP VIEW IF EXISTS complMoltypes;
CREATE VIEW complMoltypes AS
SELECT m.pdb_id,m.type,c.compl
FROM moltypesOverview m, _NOE_completeness_stats c 
where m.pdb_id=c.pdb_id AND
	m.pdb_id not in ( select pdb_id from RDC_Entries ) and
	m.pdb_id not in ( select pdb_id from Set14DOCRFREDBaddies )
order by m.type,c.compl desc,m.pdb_id;
	
DROP VIEW IF EXISTS complMoltypesStats;
CREATE VIEW complMoltypesStats AS
SELECT m.type, avg(c.compl), count(m.pdb_id)
FROM moltypesOverview m, _NOE_completeness_stats c 
where m.pdb_id=c.pdb_id AND
	m.pdb_id not in ( select pdb_id from RDC_Entries ) and
	m.pdb_id not in ( select pdb_id from Set14DOCRFREDBaddies )
group by m.type
order by count(m.pdb_id) desc;

-- Get the number of residues per entry	
DROP VIEW IF EXISTS residueCount;
CREATE VIEW residueCount AS
SELECT m.pdb_id,sum(m.rescount) AS rescount
FROM moltypes m
group by m.pdb_id
order by rescount;
	
-- Get the completeness per moltype/entries
DROP VIEW IF EXISTS stereoGroupPercentage;
CREATE VIEW stereoGroupPercentage AS
SELECT r.pdb_id,s.Triplet_count/r.rescount as percTriplet
FROM residueCount r, _Stereo_assign_list s 
where r.pdb_id=s.pdb_id 
order by percTriplet asc;
	
-- Get the unreleased Rutgers PDB entries with MR files
DROP VIEW IF EXISTS unreleasedMREntriesPDB;

CREATE VIEW unreleasedMREntriesPDB AS
SELECT p.pdb_id
FROM rcsb_id_with_mr m, rcsb_pdb_id p 
where m.rcsb_id=p.rcsb_id and
p.pdb_id not in ( select pdb_id from DOCRFREDEntries);


