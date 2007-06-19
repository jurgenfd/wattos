-- Some useful SQL to analyze the content of the NMR-STAR 3.1 dictionary
-- 		enumerations
--		tag names
--		categories
--		etc.
--
-- 	Login: jurgen/jurg

SELECT * FROM "public"."valenums" e, tags t
where e.seq=t.seq AND
t.tagname LIKE '_Entity.Polymer_type%';

SELECT * FROM tags t
where t.tagname LIKE '_Org_constr_file_comment%';

SELECT * FROM tags t
where t.sfcat='constraint_statistics' AND
t.tagname LIKE '%' 
