class MockSocket():
    def send(self, *args): pass
    
        
import sys
sys.path.insert(0, 'src/main/python')
sys.path.insert(0, '../../main/python')
from serde import *
import socket


receiver = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
receiver.connect(("localhost", 4000))
data = receiver.recv(1024)
script = ""
while data:
    script += data.decode()
    data = receiver.recv(1024)      
receiver.close()
    

arg = sys.argv[1]

call_str = "print(run([int(i) for i in ListSerde.deserialize(arg)], MockSocket(), DivideSerde, CombineSerde, ListSerde, 0))"
full_script = script + "\n" + call_str
exec(full_script)




