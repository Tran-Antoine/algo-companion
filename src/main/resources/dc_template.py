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
        
        socket.send(f"DIVIDE {div_ser.serialize((input_id, depth, current_index, [], division_outputs), input_serde)}")
        
        solved_subdivisions = [run(sub_arg, socket, div_ser, comb_ser, input_serde, input_id, depth + 1) 
                               for (i, sub_arg) in enumerate(division_outputs)]
        
        out = combine(len(solved_subdivisions), *solved_subdivisions)
        
        run.maxIndices[depth] += len(division_outputs) - 1
    
    
    socket.send(f"COMBINE {comb_ser.serialize((input_id, depth, current_index, [], out), input_serde)}")
    
    if depth == 0:
        socket.send(f"DONE {input_id}")
        
    return out