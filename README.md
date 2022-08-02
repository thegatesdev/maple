# Maple

A simple, type safe data structure.

---

Maple consists of 3 data types extending 1 base class.

- DataContainer
- DataMap (Backed by HashMap)
- DataList (Backed by ArrayList)

DataMap is a map with String as keys, and DataContainer as values.

DataList is a list with as DataContainer as values.

DataContainer is a container for any value. The value can be type checked, all primitive types and DataMap/List have a
type check and getter method.

The base class is called DataElement. DataElement has ways of keeping track of where in the structure the element is.
It can have a parent and a name, with which it can calculate the path it exists at.

---

Maple is a tool I use for coding minecraft plugins with Spigot. Spigot has a data API (Configuration API) which is not
useful for the things I want to do.
That's why I coded this!
