# Maple

A simple, type safe data structure.

## Structure

Maple consists of 3 data types extending 1 base class.

- `DataContainer`
- `DataMap` (Backed by HashMap)
- `DataList` (Backed by ArrayList)

**DataMap** is a map with String as keys, and DataContainer as values.

**DataList** is a list with as DataContainer as values.

**DataContainer** is a container for any value. The value can be type checked, all primitive types and DataMap/List have a
type check and getter method.

**DataElement** has ways of keeping track of where in the structure the element is.
It can have a parent and a name, with which it can calculate the path it exists at.

## Basic usage

Use the `DataMap#read` method to read a `Map<String, Object>` to a DataMap, or the `DataList#read` method to read a `List<Object>` to a DataList.
Both will try to convert subsequent Map or List instances to DataMap or DataList, as demonstrated in the example ( meaning this is a **recursive** action ).

Example:
```java
Map<String, Object> map = Map.of(
  "intElement", 1,
  "listElement", List.of("one", 2),
  "mapElement", Map.of("one", 1, "two", "two")
);
DataMap dataMap = DataMap.read(map);
```
After this operation, 'dataMap' now contains this:
```
DataContainer with name 'intElement' value (int) 1
DataContainer with name 'listElement' value (DataList) containing:
  DataContainer with name '[0]' value (String) "one"
  DataContainer with name '[1]' value (int) 2
DataContainer with name 'mapElement' value (DataMap) containing:
  DataContainer with name 'one' value (int) 1
  DataContainer with name 'two' value (String) 'two'
```


## Idk *about* r smth

Maple is a tool I use for coding minecraft plugins with Spigot. Spigot has a data API (Configuration API) which is not
useful for the things I want to do.
That's why I coded this!
