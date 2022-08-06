class MockSocket():
    def send(self, *args): pass
    
        
import sys
sys.path.insert(0, 'src/main/python')
from serde import *

arg = sys.argv[1]

script = ''.join(open(sys.argv[2]))


call_str = "print(run([int(i) for i in ListSerde.deserialize(arg)], MockSocket(), DivideSerde, CombineSerde, ListSerde, 0))"
full_script = script + "\n" + call_str
exec(full_script)


