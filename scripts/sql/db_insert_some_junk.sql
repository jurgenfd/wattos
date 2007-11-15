INSERT INTO entry ( entry_id, pdb_id ) VALUES ( 2, '1brv' );

INSERT INTO entry (entry_id, pdb_id) VALUES ( 1009, '1brv');


INSERT INTO mrfile (
mrfile_id,            
entry_id,             
detail,      
pdb_id,                
date_modified
) VALUES ( 
1, 
1,
'detail',
'1brv',
{TIMESTAMP '2004-06-15 04:43:26'}
);

INSERT INTO mrblock (
mrblock_id,      
mrfile_id,       
position,        
program,         
type,            
subtype,         
format,          
text_type,       
line_count,      
date_modified,   
other_prop,      
dbfs_id,         
file_extension,  
md5_sum
) VALUES ( 
1, 
1,
4,
'program',
'type',
'subtype',
'format',
'text_type',
9,
SYSDATE,
'no other prop',
3,
'tgz',
'060a8849c50c0440917a8713902a29bb'
)


DELETE FROM mrblock 
WHERE dbfs_id IN ( 5, 3 );




