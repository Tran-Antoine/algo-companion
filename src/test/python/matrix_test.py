import unittest
import sys

sys.path.insert(0, '../../main/python')

from matrix import Matrix


class MatrixTest(unittest.TestCase):

        
    def test_matrix_parameters_are_correctly_set(self):
    
        m1 = Matrix()
        m2 = Matrix([[1,2,3],
                     [4,5,6]])
                
        self.assertEqual(m1.n_rows,    0)
        self.assertEqual(m1.n_columns, 0)
        
        self.assertEqual(m2.n_rows,    2)
        self.assertEqual(m2.n_columns, 3)
        
        
        
if __name__ == '__main__':
    unittest.main()