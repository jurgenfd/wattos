import os

urlDB            = 'http://tang.bmrb.wisc.edu/servlet_data/viavia/mr_mysql_backup/'
urlLists         = 'http://tang.bmrb.wisc.edu/wattos/lists/'

share_dir         = '/share'
share2_dir        = '/share2'
big_dir           = '/big'
root_dir          = '/'
#print os.name # My windows xp returns nt here. Linux reports posix.
if os.name == 'nt': # Windows drives
    share_dir         = 'S:\\'
    share2_dir        = 'Y:\\'
    big_dir           = 'M:\\'
    root_dir          = 'C:\\'
    
base_dir         = os.path.join(share_dir,'jurgen','CloneWars','DOCR1000')
linkDir          = os.path.join(big_dir,'jurgen','DOCR_big_tmp_','link')
tmpDir           = os.path.join(root_dir,'tmp')

results_dir      = os.path.join(base_dir,'Results')
csh_script_dir   = os.path.join(base_dir,'scripts')

DUMP_DIR         = os.path.join(root_dir,'www','servlet_data','viavia','mr_mysql_backup')

#print "Read Wattos.Utils.localConstants.py version 0.3"