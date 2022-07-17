import math

class Matrix:
    """
    Simple representation of a matrix of arbitrary fixed size with basic functions commonly used in D&C algorithms.
    This data structure is immutable.
    """
    def __init__(self, lines=[]):
        self.lines = lines
        self.n_rows = len(lines)
        self.n_columns = 0 if self.n_rows == 0 else len(lines[0])
        
    def as_tuple(self):
        return tuple(tuple(line) for line in lines)

    def __getitem__(self, key):
        return tuple(self.lines[key])
    
    def slice_v(self, start, end, step=1):
        new_lines = [line[start:end:step] for line in self.lines]
        return Matrix(new_lines)
    
    
    def split_v(self, n):
        step = math.ceil(self.n_columns / n)
        output = []
        start_index = 0
        for i in range(n):
            end_index = min(start_index + step, self.n_columns)
            slice_portion = self.slice_v(start_index, end_index)
            output.append(slice_portion)
            
        return tuple(output)
            