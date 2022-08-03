
import sys
import socket
from serde import *

def get_serde(type_id):
    if type_id == "ListType": return ListSerde
    if type_id == "MatrixType": return MatrixSerde
    if type_id == "BinaryTreeType": return BinaryTreeSerde
    if type_id == "HeapType": return HeapSerde
    
def main():

    input_serde = get_serde(sys.argv[0])
    

    sender = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    port = int(sys.argv[1])

    sender.bind(("localhost", port))

    for (i, arg) in enumerate(sys.argv[2:]):
        run(arg, sender, DivideSerde, CombineSerde, input_serde, i)
        
        
if __name__ == '__main__':
    main()
        