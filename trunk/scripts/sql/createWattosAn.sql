--Code to create the views based on the slave updated version of Wattos2 db. 
DROP view IF EXISTS PERFREDTMP;
CREATE VIEW PERFREDTMP 
	AS 
SELECT F.PDB_ID, B.TEXT_TYPE, B.ITEM_COUNT
FROM Wattos2.mrblock AS B, Wattos2.mrfile AS F
WHERE B.mrfile_ID=F.mrfile_ID AND TYPE='entry' AND PROGRAM='STAR'
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

DROP view IF EXISTS overviewAllGottingen;
CREATE VIEW OverviewAllGottingen AS
SELECT * FROM overviewAll O
where O.pdb_id in ( select S.pdb_id from docrFredBaddies786 S )
ORDER BY O.pdb_id;


-- Recorded in excel worksheet as fixed but not fixed in db.
DROP view IF EXISTS Overview_baddies_recorded_as_fixed;
CREATE VIEW Overview_baddies_recorded_as_fixed AS 
SELECT *
from overviewAll b
where
    b.pdb_id in     ( select pdb_id from Set14DOCRFREDBaddies )
AND b.pdb_id not in ( select pdb_id from docrFredBaddiesNotFixed )
order by b.pdb_id;

DROP view IF EXISTS Overview_goodies_recorded_as_baddies;
CREATE VIEW Overview_goodies_recorded_as_baddies AS 
SELECT *
from overviewAll b
where
    b.pdb_id not in ( select pdb_id from Set14DOCRFREDBaddies )
AND b.pdb_id in ( select pdb_id from docrFredBaddiesNotFixed )
order by b.pdb_id;
