"""Unit test
"""
from Wattos import wattosDirTmp
from Wattos.Utils.Utils import mkdirs
from Wattos.Utils.Utils import rmdir
from Wattos.Utils.getMolTypes import getMolTypes
from unittest import TestCase
import os   
import unittest


wattosDirTmpTest = os.path.join( wattosDirTmp, 'test_getMolTypes' )
if os.path.exists(wattosDirTmpTest):
    rmdir( wattosDirTmpTest )
mkdirs( wattosDirTmpTest )
os.chdir(wattosDirTmpTest)


class test_getMolTypes(TestCase):
        
    def _test_getMolTypes(self):
        """Simplest test not useful except for testing testing framework."""

        self.assertFalse(getMolTypes())
     
if __name__ == "__main__":
    unittest.main()
