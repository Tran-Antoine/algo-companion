import math

class Matrix:
    """
    Simple representation of a matrix of arbitrary fixed size with basic functions commonly used in D&C algorithms.
    This data structure is immutable.
    """
    def __init__(self, lines=[]):
        self.lines = list(lines)
        self.n_rows = len(lines)
        self.n_columns = 0 if self.n_rows == 0 else len(lines[0])
        
    def as_tuple(self):
        return tuple(tuple(line) for line in self.lines)

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
            start_index += step
            
        return tuple(output)
        
    def slice_h(self, start, end, step=1):
        return Matrix(self.lines[start:end:step])
            
    
    def split_h(self, n):
        
        output = []
        step = math.ceil(self.n_rows / n)
        
        for start_index in range(0, self.n_rows, step):
            end_index = min(start_index + step, self.n_rows)
            slice_portion = self.slice_h(start_index, end_index)
            output.append(slice_portion)
            
        return tuple(output)
    
    def slice_vh(self, start_row, end_row, start_column, end_column, step_row=1, step_column=1):
        return self.slice_h(start_row, end_row, step_row).slice_v(start_column, end_column, step_column)
    
    def split_vh(self, n, m):
        return tuple(mat.split_v(m) for mat in self.split_h(n))
        
    def split_vh_flat(self, n, m):
        result = ()
        for line in self.split_vh(n, m):
            result += line
            
        return result
        
    def __str__(self):
        return self.as_tuple().__str__()
        
    def __repr__(self):
        return self.as_tuple().__repr__()
        
    def __eq__(self, other):
        return self.as_tuple() == other.as_tuple()
        
    def __len__(self):
        return self.n_rows * self.n_columns