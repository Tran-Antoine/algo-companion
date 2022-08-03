class MockSocket():
    def send(self, *args): pass
    
        
import sys
sys.path.insert(0, '../../main/python')
from serde import *

arg = sys.argv[0]
script = sys.argv[1:]

call_str = "run(arg, MockSocket(), DivideSerde, CombineSerde, ListSerde, 0)"

full_script = script + "\n" + call_str

print(exec(full_script))

