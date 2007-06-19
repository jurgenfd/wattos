
-- With parsable restraints
SELECT count(*) FROM `Wattos2`.`mrblock`
where item_count >=1 AND
text_type='2-parsed' AND
type = 'entry' AND
subtype = 'full';

-- With distance restraints
SELECT count(distinct pdb_id)
FROM `Wattos2`.`mrblock` b, `Wattos2`.`mrfile` f
where b.mrfile_id=f.mrfile_id AND
text_type='2-parsed' AND
type = 'distance' AND
item_count >= 1;