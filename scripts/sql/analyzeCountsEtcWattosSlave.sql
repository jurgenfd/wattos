
-- With parsable restraints
SELECT count(*) FROM `mrblock`
where item_count >=1 AND
text_type='2-parsed' AND
type = 'entry' AND
subtype = 'full';

-- With distance restraints
SELECT count(distinct pdb_id)
FROM `mrblock` b, `mrfile` f
where b.mrfile_id=f.mrfile_id AND
text_type='2-parsed' AND
type = 'distance' AND
item_count >= 1; ()