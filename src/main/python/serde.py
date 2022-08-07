import base64

class StringSerde():
    
    def serialize(arg):
        encodedBytes = base64.b64encode(arg.encode("utf-8"))
        return str(encodedBytes, "utf-8")
        
    def deserialize(data):
        decodedBytes = base64.b64decode(data)
        return str(decodedBytes, "utf-8")

class IntSerde():

    def serialize(arg): return str(arg)
    def deserialize(data): return int(data)
    
class ListSerde():

    def serialize(arg, sep=","):
        return sep.join([str(a) for a in arg])
        
    def deserialize(data, sep=","):
        return [] if len(data) == 0 else data.split(sep)

class MatrixSerde():

    def serialize(arg): pass
    def deserialize(data): pass


class BinaryTreeSerde():

    def serialize(arg): pass
    def deserialize(data): pass


class HeapSerde():

    def serialize(arg): pass
    def deserialize(data): pass

class DivideSerde():

    def serialize(arg, input_serde):
        elements = [
                    IntSerde.serialize(arg[0]),
                    IntSerde.serialize(arg[1]),
                    IntSerde.serialize(arg[2]),
                    ListSerde.serialize(arg[3]),
                    ListSerde.serialize([input_serde.serialize(i) for i in arg[4]], sep=";")
                    ]
        return ListSerde.serialize(elements, "/")
        
        
    def deserialize(data, input_serde): 
        arg = ListSerde.deserialize(data)
        return [
            IntSerde.deserialize(arg[0]),
            IntSerde.deserialize(arg[1]),
            IntSerde.deserialize(arg[2]),
            [IntSerde.deserialize(s) for s in ListSerde.deserialize(arg[3])],
            [input_serde.deserialize(i) for i in ListSerde.deserialize(arg[4])]
        ]

class CombineSerde():

    def serialize(arg, input_serde):
        elements = [
                    IntSerde.serialize(arg[0]),
                    IntSerde.serialize(arg[1]),
                    IntSerde.serialize(arg[2]),
                    ListSerde.serialize(arg[3]),
                    input_serde.serialize(arg[4])
                    ]
        return ListSerde.serialize(elements)
        
        
    def deserialize(data, input_serde): 
        arg = ListSerde.deserialize(data)
        return [
            IntSerde.deserialize(arg[0]),
            IntSerde.deserialize(arg[1]),
            IntSerde.deserialize(arg[2]),
            [IntSerde.deserialize(s) for s in ListSerde.deserialize(arg[3])],
            input_serde.deserialize(arg[4])
        ]
