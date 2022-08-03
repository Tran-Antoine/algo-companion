def run(arg):
    """
    Runs a generic Divide and Conquer algorithm using external functions defined by the user.
    The run function assumes existence of the following functions:
    - base_case(n, arg)
    - divide(n, arg)
    - combine(n, arg)
    """
    n = len(arg)
    if (out := base_case(n, arg)):
        return out
        
    division_outputs = divide(n, arg)
    solved_subdivisions = [run(sub_arg) for sub_arg in division_outputs]
    
    return combine(n, *solved_subdivisions)