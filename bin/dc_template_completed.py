def base_case(n, arg):
    if n == 1: return arg
def divide(n, arg):
    return arg[:n//2], arg[n//2:]
def combine(n, *args):
    # create shortcut variables to make user code shorter
    if n > 0: arg0 = args[0]
    if n > 1: arg1 = args[1]
    if n > 2: arg2 = args[2]
    if n > 3: arg3 = args[3]
    if n > 4: arg4 = args[4]
    if n > 5: arg5 = args[5]
    return [arg0[0]] if arg0[0] > arg1[0] else [arg1[0]]

# Template of D&C Algorithm

def run(arg, socket, div_ser, comb_ser, input_serde, input_id, depth=0):
    """
    Runs a generic Divide and Conquer algorithm using external functions defined by the user.
    The run function assumes existence of the following functions:
    - base_case(n, arg)
    - divide(n, arg)
    - combine(n, arg)
    """
    if depth == 0:
        run.maxIndices = {0: 0} # reset dictionary
        socket.send(f"REGISTER {input_id}")
        
    n = len(arg)
    
    if not depth in run.maxIndices:
        run.maxIndices[depth] = 0
        
        
    current_index = run.maxIndices[depth]

    run.maxIndices[depth] += 1 # in all cases we have at least one result
    
    if not (out := base_case(n, arg)):
    
        division_outputs = divide(n, arg)
        
        socket.send(f"DIVIDE {div_ser.serialize((input_id, depth, current_index, division_outputs), input_serde)}")
        
        solved_subdivisions = [run(sub_arg, socket, div_ser, comb_ser, input_serde, input_id, depth + 1) 
                               for (i, sub_arg) in enumerate(division_outputs)]
        
        out = combine(len(solved_subdivisions), *solved_subdivisions)
        
        run.maxIndices[depth] += len(division_outputs) - 1
    
    
    socket.send(f"COMBINE {comb_ser.serialize((input_id, depth, current_index, out), input_serde)}")
    
    if depth == 0:
        socket.send(f"DONE {input_id}")
        
    return out
