# Maple

A clean type safe data structure.

## V2

This WIP branch rewrites things for a bit of cleaner code.

### Done:

- Simplify DataElement parenting / naming
    - Parenting and naming is not exposed anymore
    - Added disconnecting from parent. In V1, values overwritten in a map for example would still keep their parent,
      although not being in the structure.
- Simplify DataMap operation
    - Uses a final map instead of dynamically initializing when values were added
- Add DataValue abstract class representing both final and changing values.
- Switch project to Gradle
- Reimplement DataList
- Reimplement cloning
- Reimplement nice toString

### Incorporate

- Make the Maple class handle construction of elements

### ToDo:

- Reimplement DataPrimitive and a dynamic counterpart
- Make ElementException nicer?
- Add tests
- Merge with MapleTree??