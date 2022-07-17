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
        
    
    def test_matrix_tuple_retrieval_works(self):
    
        m1 = Matrix([[1,2,3],
                     [4,5,6]])
                     
        expected = ((1,2,3), (4,5,6))
        
        self.assertEqual(m1.as_tuple(), expected)
    
    def test_matrix_indexing_works(self):
    
        m1 = Matrix([[1,2,3],
                     [4,5,6]])
                     
        self.assertEqual(m1[0][1], 2)
        self.assertEqual(m1[1][2], 6)
        
        
    def test_matrix_vertical_slicing_works(self):
    
        m1 = Matrix([[1, 2, 3, 4, 5],
                     [6, 7, 8, 9, 0],
                     [7, 6, 5, 4, 3]])
                     
        expected1 = ((2, 3), 
                     (7, 8), 
                     (6, 5))
        expected2 = ((2, 4), 
                     (7, 9), 
                     (6, 4))  
                     
        self.assertEqual(m1.slice_v(1, 3).as_tuple(), expected1)
        self.assertEqual(m1.slice_v(1, 4, 2).as_tuple(), expected2)
    
    def test_matrix_vertical_splitting_works(self):
    
        m1 = Matrix([[1, 2, 3, 4, 5],
                     [6, 7, 8, 9, 0],
                     [7, 6, 5, 4, 3]])
                     
        expected = (
        
            Matrix([[1, 2], 
                    [6, 7], 
                    [7, 6]]),
                    
            Matrix([[3, 4], 
                    [8, 9], 
                    [5, 4]]),
                    
            Matrix([[5], 
                    [0], 
                    [3]])

        )
        
        self.assertEqual(m1.split_v(3), expected)
        
    def test_matrix_horizontal_slicing_works(self):
    
        m1 = Matrix([[1, 2, 3, 4, 5],
                     [6, 7, 8, 9, 0],
                     [7, 6, 5, 4, 3]])
                     
        expected1 = ((1, 2, 3, 4, 5), 
                     (6, 7, 8, 9, 0))
                     
        expected2 = ((1, 2, 3, 4, 5), 
                     (7, 6, 5, 4, 3)) 
                     
        self.assertEqual(m1.slice_h(0, 2).as_tuple(), expected1)
        self.assertEqual(m1.slice_h(0, 3, 2).as_tuple(), expected2)
    
    def test_matrix_horizontal_splitting_works(self):
    
        m1 = Matrix([[1, 2, 3],
                     [6, 7, 8],
                     [7, 6, 5],
                     [4, 5, 6],
                     [2, 2, 2]])
                     
        expected = (
        
            Matrix([[1, 2, 3], 
                    [6, 7, 8]]),
                    
            Matrix([[7, 6, 5], 
                    [4, 5, 6]]),
                    
            Matrix([[2, 2, 2]])

        )
        
        self.assertEqual(m1.split_h(3), expected)

        
if __name__ == '__main__':
    unittest.main()